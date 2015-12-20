package plugins.combat;

import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.ItemSlot;
import io.gamemachine.messages.ItemSlots;
import io.gamemachine.messages.Vitals;
import plugins.core.combat.ClientDbLoader;
import plugins.core.combat.VitalsHandler;
import plugins.core.combat.VitalsProxy;

public class ItemVitals {

	private static final Logger logger = LoggerFactory.getLogger(ItemVitals.class);
	private Map<String,Vitals> vitalsIndex = new ConcurrentHashMap<String,Vitals>();
	private Map<String,ConcurrentHashMap<String,Vitals>> characterItemVitals = new ConcurrentHashMap<String,ConcurrentHashMap<String,Vitals>>();
	private List<Vitals> vitalsTemplates;
	
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
	
	public Vitals getTemplate(String playerItemName) {
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
	
	public void ItemSlotsUpdated(String playerId, String characterId, ItemSlots itemSlots) {
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
	
}
