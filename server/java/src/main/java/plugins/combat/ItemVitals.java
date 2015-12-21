package plugins.combat;

import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.messages.ItemSlot;
import io.gamemachine.messages.ItemSlots;
import io.gamemachine.messages.Vitals;
import plugins.core.combat.ClientDbLoader;
import plugins.core.combat.VitalsHandler;
import plugins.core.combat.VitalsProxy;

public class ItemVitals extends UntypedActor {

	public static class ItemVitalsRequest {
		public String playerId;
		public String characterId;
		public ItemSlots itemSlots;
		public String action;
	}
	
	public static String name = ItemVitals.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(ItemVitals.class);
	private static Map<String,Vitals> vitalsIndex = new ConcurrentHashMap<String,Vitals>();
	private static Map<String,ConcurrentHashMap<String,Vitals>> characterItemVitals = new ConcurrentHashMap<String,ConcurrentHashMap<String,Vitals>>();
	private List<Vitals> vitalsTemplates;
	
	
	public static void tell(ItemVitalsRequest request) {
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		sel.tell(request, null);
	}
	
	public ItemVitals() {
		vitalsTemplates = ClientDbLoader.getVitalsContainer().vitals;
	}
	
	public void removePlayer(String playerId) {
		characterItemVitals.remove(playerId);
	}
	
	private Map<String,Vitals> getPlayerItemVitals(String characterId) {
		if (!characterItemVitals.containsKey(characterId)) {
			characterItemVitals.put(characterId, new ConcurrentHashMap<String,Vitals>());
		}
		return characterItemVitals.get(characterId);
	}
	
	private Vitals getTemplate(String playerItemName) {
		for (Vitals vitals : vitalsTemplates) {
			if (Strings.isNullOrEmpty(vitals.id)) {
				continue;
			}
			if (vitals.id.equals(playerItemName)) {
				return vitals.clone();
			}
		}
		throw new RuntimeException("Unable to find vitals for player item " + playerItemName);
	}
	
	private Vitals getVitals(ItemSlot itemSlot, String characterId) {
		String key = itemSlot.playerItemId+itemSlot.playerItemName;
		if (!vitalsIndex.containsKey(key)) {
			Vitals vitals = Vitals.db().findFirst("vitals_id = ?", key);
			if (vitals == null) {
				vitals = getTemplate(itemSlot.playerItemName);
				vitals.characterId = characterId;
				Vitals.db().save(vitals);
			}
			vitalsIndex.put(key, vitals);
		}
		return vitalsIndex.get(key);
	}
	
	private void setPlayerVitals(Vitals itemVitals, String playerId, int type) {
		VitalsProxy itemProxy = new VitalsProxy(itemVitals);
		VitalsProxy proxy = VitalsHandler.get(playerId);
		
		for (Vitals.Attribute attribute : Vitals.Attribute.values()) {
			String name = Introspector.decapitalize(attribute.toString());
			int value = itemProxy.getMax(name);
			if (value > 0) {
				if (type == 0) {
					proxy.subtractMax(name,value);
				} else {
					proxy.addMax(name,value);
				}
			}
		}
	}
	
	private void ItemSlotsUpdated(String playerId, String characterId, ItemSlots itemSlots) {
		Map<String,Vitals> itemVitals = getPlayerItemVitals(playerId);
		for (String playerItemId : itemVitals.keySet()) {
			Vitals vitals = itemVitals.get(playerItemId);
			setPlayerVitals(vitals, playerId, 0);
			itemVitals.remove(playerItemId);
		}
		
		for (ItemSlot slot : itemSlots.getItemSlotsList()) {
			Vitals vitals = getVitals(slot,characterId);
			setPlayerVitals(vitals, playerId, 1);
			itemVitals.put(slot.playerItemId, vitals);
		}
		
	}

	@Override
	public void onReceive(Object message) throws Exception {
		ItemVitalsRequest request = (ItemVitalsRequest)message;
		if (request.action.equals("update")) {
			ItemSlotsUpdated(request.playerId, request.characterId, request.itemSlots);
		} else if (request.action.equals("remove")) {
			removePlayer(request.playerId);
		}
	}
	
}
