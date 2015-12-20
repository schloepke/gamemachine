package plugins.core.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.VitalsContainer;
import io.gamemachine.messages.VitalsUpdateContainer;
import scala.concurrent.duration.Duration;

public class VitalsSender extends UntypedActor {

	public static String name = VitalsSender.class.getSimpleName();
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private Map<String, Integer> vitalTicks = new ConcurrentHashMap<String, Integer>();
	private double vitalsDistance = 50;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			if (message.equals("vitals_tick")) {
				sendVitals();
				tick(1000L, "vitals_tick");
			}
		}
	}

	private void sendVitals() {
		for (GridSet gridSet : StatusEffectManager.gridsets) {
			for (TrackData playerTrackData : gridSet.playerGrid.getAll()) {
				if (playerTrackData.entityType == TrackData.EntityType.Player) {
					
					List<List<VitalsProxy>> sublists = Lists.partition(livingVitals(gridSet.zone), 20);
					for (List<VitalsProxy> sublist : sublists) {
						sendVitalsToPlayer(gridSet.playerGrid, playerTrackData, sublist);
					}
					
					sublists = Lists.partition(objectVitals(gridSet.zone), 20);
					for (List<VitalsProxy> sublist : sublists) {
						sendVitalsToPlayer(gridSet.objectGrid, playerTrackData, sublist);
					}
				}
			}
		}
	}

	private void sendVitalsToPlayer(Grid grid, TrackData playerTrackData, List<VitalsProxy> proxies) {
		VitalsUpdateContainer toSend = new VitalsUpdateContainer();
		for (VitalsProxy proxy : proxies) {
			TrackData vitalsTrackData = grid.get(proxy.getEntityId());

			// Dead most likely
			if (vitalsTrackData == null) {
				continue;
			}

			double distance = AoeUtil.distance(vitalsTrackData, playerTrackData);
			if (distance <= vitalsDistance) {
				toSend.addVitalsUpdate(proxy.getVitalsUpdate().clone());
			} else {
				if (proxy.getType() == Vitals.VitalsType.Object) {
					//logger.warning("Distance too far "+distance);
				}
			}
		}

		GameMessage msg = new GameMessage();
		msg.vitalsUpdateContainer = toSend;
		PlayerMessage.tell(msg, playerTrackData.id);
	}

	private List<VitalsProxy> objectVitals(String zone) {
		List<VitalsProxy> container = new ArrayList<VitalsProxy>();
		for (VitalsProxy vitalsProxy : VitalsHandler.getVitalsForZone(zone)) {
			if (vitalsProxy.getType() == Vitals.VitalsType.BuildObject || vitalsProxy.getType() == Vitals.VitalsType.Object) {
				if (vitalsProxy.hasChanged()) {
					container.add(vitalsProxy);
				}
			}
		}
		return container;
	}

	private List<VitalsProxy> livingVitals(String zone) {
		List<VitalsProxy> container = new ArrayList<VitalsProxy>();
		for (VitalsProxy vitalsProxy : VitalsHandler.getVitalsForZone(zone)) {
			//logger.warning("LivingVitals "+vitals.characterId+" "+vitals.health);
			if (vitalsProxy.getType() == Vitals.VitalsType.Character) {
				if (vitalsProxy.hasChanged() ||
						vitalsProxy.get("health") < vitalsProxy.getMax("health") ||
						vitalsProxy.get("magic") < vitalsProxy.getMax("magic") ||
						vitalsProxy.get("stamina") < vitalsProxy.getMax("stamina")) {
					container.add(vitalsProxy);
				}
			}
		}
		return container;
	}

	@Override
	public void preStart() {
		tick(1000L, "vitals_tick");
	}

	public void tick(long delay, String message) {
		getContext().system().scheduler().scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(),
				message, getContext().dispatcher(), null);
	}
}
