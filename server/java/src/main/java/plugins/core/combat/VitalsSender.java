package plugins.core.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GmVector3;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.VitalsContainer;
import plugins.CoreCombatPlugin;
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

	private int nextVitalTick(String id) {
		if (vitalTicks.containsKey(id)) {
			int tick = vitalTicks.get(id);
			if (tick > 1000) {
				tick = 50;
			}
			vitalTicks.put(id, tick + 1);
			return tick;
		} else {
			vitalTicks.put(id, 0);
			return 0;
		}
	}

	private void resetVitalTick(String id) {
		vitalTicks.put(id, 0);
	}

	private void sendVitals() {
		for (GridSet gridSet : StatusEffectManager.gridsets) {
			for (TrackData playerTrackData : gridSet.playerGrid.getAll()) {
				if (playerTrackData.entityType == TrackData.EntityType.Player) {
					sendVitalsToPlayer(gridSet.playerGrid, playerTrackData, livingVitals(gridSet.zone));
					sendVitalsToPlayer(gridSet.objectGrid, playerTrackData, objectVitals(gridSet.zone));
				}
				
			}
		}
	}

	private void sendVitalsToPlayer(Grid grid, TrackData playerTrackData, List<Vitals> container) {
		VitalsContainer toSend = new VitalsContainer();
		for (Vitals vitals : container) {
			TrackData vitalsTrackData = grid.get(vitals.entityId);

			if (cleanupBadVitals(vitalsTrackData, vitals)) {
				logger.warning("Cleanup trackdata "+vitals.entityId);
				continue;
			}

			double distance = AoeUtil.distance(vitalsTrackData, playerTrackData);
			if (distance <= vitalsDistance) {
				toSend.addVitals(vitals);
			} else {
				if (vitals.type == Vitals.VitalsType.Object) {
					//logger.warning("Distance too far "+distance);
				}
			}
		}

		GameMessage msg = new GameMessage();
		msg.vitalsContainer = toSend;
		PlayerCommands.sendGameMessage(msg, playerTrackData.id);
	}

	// Vitals might end up existing after player disconnects. Lost
	// messages, etc... Ensure we catch that here
	private boolean cleanupBadVitals(TrackData trackData, Vitals vitals) {
		if (trackData == null) {
			VitalsHandler.remove(vitals.entityId);
			return true;
		} else {
			return false;
		}
	}

	private List<Vitals> objectVitals(int zone) {
		List<Vitals> container = new ArrayList<Vitals>();
		for (VitalsProxy vitalsProxy : VitalsHandler.getVitalsForZone(zone)) {
			if (vitalsProxy.vitals.type == Vitals.VitalsType.Object) {
				if (vitalsProxy.vitals.changed == 1) {
					resetVitalTick(vitalsProxy.vitals.entityId);
					vitalsProxy.vitals.changed = 0;
				} else {
					int tick = nextVitalTick(vitalsProxy.vitals.entityId);
					if (tick >= 4) {
						continue;
					}
				}
				container.add(vitalsProxy.vitals);
			}

		}
		return container;
	}

	private List<Vitals> livingVitals(int zone) {
		List<Vitals> container = new ArrayList<Vitals>();
		for (VitalsProxy vitalsProxy : VitalsHandler.getVitalsForZone(zone)) {
			Vitals vitals = vitalsProxy.vitals;
			if (vitals.type == Vitals.VitalsType.Character) {
				Vitals template = vitalsProxy.baseVitals;
				if (vitals.changed == 1 || vitals.health < template.health || vitals.magic < template.magic
						|| vitals.stamina < template.stamina) {
					resetVitalTick(vitals.entityId);
					vitals.changed = 0;
				} else {
					int tick = nextVitalTick(vitals.entityId);
					if (tick >= 4) {
						continue;
					}
				}
				container.add(vitals);
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
