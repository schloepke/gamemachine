package plugins.core.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.GameEntityManager;
import io.gamemachine.core.GameEntityManagerService;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.VitalsContainer;
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
					sendVitalsToPlayer(gridSet.playerGrid, playerTrackData, livingVitals(gridSet.zone),true);
					sendVitalsToPlayer(gridSet.objectGrid, playerTrackData, objectVitals(gridSet.zone),false);
				}
				
			}
		}
	}

	private void sendVitalsToPlayer(Grid grid, TrackData playerTrackData, List<Vitals> vitalsList, boolean sendBaseUpdate) {
		VitalsContainer toSend = new VitalsContainer();
		for (Vitals vitals : vitalsList) {
			TrackData vitalsTrackData = grid.get(vitals.entityId);

			// Dead most likely
			if (vitalsTrackData == null) {
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

		if (sendBaseUpdate) {
			GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();
			Player player = PlayerService.getInstance().find(playerTrackData.id);
			VitalsProxy proxy = VitalsHandler.get(player.id);
			if (proxy != null) {
				Vitals vitals = proxy.baseVitals.clone();
				vitals.isBaseVitals = true;
				toSend.addVitals(vitals);
			}
		}
		
		GameMessage msg = new GameMessage();
		msg.vitalsContainer = toSend;
		PlayerMessage.tell(msg, playerTrackData.id);
	}

	private List<Vitals> objectVitals(int zone) {
		List<Vitals> container = new ArrayList<Vitals>();
		for (VitalsProxy vitalsProxy : VitalsHandler.getVitalsForZone(zone)) {
			if (vitalsProxy.vitals.type == Vitals.VitalsType.BuildObject || vitalsProxy.vitals.type == Vitals.VitalsType.Object) {
				Vitals template = vitalsProxy.baseVitals;
				if (vitalsProxy.vitals.changed == 1) {
					resetVitalTick(vitalsProxy.vitals.entityId);
					vitalsProxy.vitals.changed = 0;
				} else {
					int tick = nextVitalTick(vitalsProxy.vitals.entityId);
					if (tick >= 10) {
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
			//logger.warning("LivingVitals "+vitals.characterId+" "+vitals.health);
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
