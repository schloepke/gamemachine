package pvp_game;

import io.gamemachine.core.Commands;
import io.gamemachine.messages.Character;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Bounds;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.UseItem;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.WorldObject;
import io.gamemachine.messages.WorldObjects;
import io.gamemachine.pathfinding.grid.BoundingBox;
import io.gamemachine.util.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ConsumableItemHandler extends GameMessageActor {

	public static String name = ConsumableItemHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	public static ConcurrentHashMap<String, WorldObject> playerAttached = new ConcurrentHashMap<String, WorldObject>();
	public static ConcurrentHashMap<String, WorldObject> wobjects = new ConcurrentHashMap<String, WorldObject>();
	public static AtomicLong counter = new AtomicLong();
	private Grid worldObjectGrid;
	private Grid defaultGrid;

	@Override
	public void awake() {
		Commands.clientManagerRegister(name);
		worldObjectGrid = GameGrid.getGameGrid(Common.gameId, "world_objects");
		defaultGrid = GameGrid.getGameGrid(Common.gameId, "default");
		load();
		scheduleOnce(5000l, new GameMessage());

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (gameMessage.hasUseItem()) {
			handleUseItem(gameMessage.useItem);
			for (TrackData trackData : defaultGrid.getAll()) {
				PlayerCommands.sendGameMessage(gameMessage, trackData.id);
			}
			return;
		}

		if (gameMessage.hasWorldObjects()) {

			for (WorldObject worldObject : gameMessage.worldObjects.worldObject) {
				if (worldObject.action == 1) {
					if (gameMessage.hasBounds()) {
						deployInBounds(worldObject, gameMessage.bounds);
					} else {
						deploy(worldObject);
					}

				} else if (worldObject.action == 2) {
					updatePosition(worldObject);
				} else if (worldObject.action == 3) {
					create(worldObject);
				} else if (worldObject.action == 5) {
					remove(worldObject.id, worldObject.ownerId);
				}
			}
			sendWorldObjects();
		} else {
			sendWorldObjects();
			scheduleOnce(5000l, new GameMessage());
		}

	}

	private void handleUseItem(UseItem useItem) {
		Character character = CharacterHandler.currentCharacter(playerId);
		PlayerItem playerItem = PlayerItem(useItem.id, character.id);
		if (playerItem.hasStatusEffects()) {
			List<String> items = Arrays.asList(playerItem.statusEffects.split(","));
			Vitals vitals = CombatHandler.playerVitals.get(playerId);

			for (String item : items) {
				if (StatusEffectHandler.statusEffects.containsKey(item)) {
					StatusEffect statusEffect = StatusEffectHandler.statusEffects.get(item);
					int value;

					if (useItem.action.equals("equip")) {
						value = statusEffect.minValue;
					} else if (useItem.action.equals("unequip")) {
						value = -statusEffect.minValue;
					} else if (useItem.action.equals("consume")) {
						value = statusEffect.minValue;
					} else {
						continue;
					}

					if (statusEffect.type == StatusEffect.Type.Armor) {
						vitals.armor += value;
					} else if (statusEffect.type == StatusEffect.Type.SpellResist) {
						vitals.spellResist += value;
					} else if (statusEffect.type == StatusEffect.Type.ElementalResist) {
						vitals.elementalResist += value;
					} else if (statusEffect.type == StatusEffect.Type.SpellPenetration) {
						vitals.spellPenetration += value;
					} else if (statusEffect.type == StatusEffect.Type.MagicRegen) {
						vitals.magicRegen += value;
					} else if (statusEffect.type == StatusEffect.Type.HealthRegen) {
						vitals.healthRegen += value;
					} else if (statusEffect.type == StatusEffect.Type.StaminaRegen) {
						vitals.staminaRegen += value;
					} else if (statusEffect.type == StatusEffect.Type.AttributeIncrease) {
						vitals.staminaRegen += value;
					}
					logger.warning("Passive "+statusEffect.type+" applied value "+value);
				}
			}

		}
	}

	private void sendWorldObjects() {
		for (TrackData trackData : defaultGrid.getAll()) {
			GameMessage gameMessage = new GameMessage();
			WorldObjects worldObjects = new WorldObjects();
			List<String> targets = Common
					.getTargetsInRange(150, trackData.x, trackData.y, trackData.z, "world_objects");
			for (String target : targets) {
				if (wobjects.containsKey(target)) {
					WorldObject worldObject = wobjects.get(target);
					if (worldObject.hasHealth() && worldObject.health <= 0) {
						if (worldObject.hasPlayerItemId()) {
							remove(worldObject.id);
							continue;
						}
					}
					worldObjects.addWorldObject(worldObject);
				}
			}
			gameMessage.worldObjects = worldObjects;
			PlayerCommands.sendGameMessage(gameMessage, trackData.id);
		}
	}

	private String genId(WorldObject worldObject) {
		return worldObject.ownerId + System.currentTimeMillis() + counter.getAndIncrement();
	}

	private void remove(String id, String ownerId) {
		remove(id);
	}

	public static void remove(String id) {
		WorldObject.db().deleteWhere("world_object_id = ?", id);
		wobjects.remove(id);
		GameGrid.getGameGrid(Common.gameId, "world_objects").remove(id);
	}

	private WorldObject find(String id) {
		return WorldObject.db().findFirst("world_object_id = ?", id);
	}

	private void deployInBounds(WorldObject worldObject, Bounds bounds) {
		if (!canSpawn(bounds)) {
			logger.warning("Objects in way of deploying " + worldObject.playerItemId);
			return;
		}

		PlayerItem playerItem = PlayerItem(worldObject.playerItemId, worldObject.ownerId);
		if (playerItem == null) {
			logger.warning("Player does not own " + worldObject.playerItemId);
			return;
		}

		worldObject.id = worldObject.playerItemId + "_" + worldObject.ownerId;
		if (worldObject.playerItemId.equals("ship")) {
			if (playerAttached.containsKey(worldObject.id)) {
				logger.warning("Player already has instance of " + worldObject.playerItemId);
				return;
			}
			playerAttached.put(worldObject.id, worldObject);
		}
		GameMessage gameMessage = new GameMessage();
		WorldObjects worldObjects = new WorldObjects();
		worldObjects.addWorldObject(worldObject);
		gameMessage.worldObjects = worldObjects;
		PlayerCommands.sendGameMessage(gameMessage, playerId);
	}

	private Boolean canSpawn(Bounds bounds) {
		io.gamemachine.util.Vector3 min = new io.gamemachine.util.Vector3(bounds.min.x, bounds.min.y, 0l);
		io.gamemachine.util.Vector3 max = new io.gamemachine.util.Vector3(bounds.max.x, bounds.max.y, 0l);
		BoundingBox box = new BoundingBox(min, max);
		io.gamemachine.util.Vector3 entityPosition;
		for (TrackData trackData : defaultGrid.getAll()) {
			entityPosition = Common.toVector3(trackData.x, trackData.y);
			if (box.contains(entityPosition)) {
				return false;
			}
		}
		return true;
	}

	private void deploy(WorldObject worldObject) {
		PlayerItem playerItem = PlayerItem(worldObject.playerItemId, worldObject.ownerId);
		if (playerItem == null) {
			logger.warning("Player does not own " + worldObject.playerItemId);
			return;
		}
		Commands.removePlayerItem(playerId, worldObject.playerItemId, 1);
		create(worldObject);
	}

	private void create(WorldObject worldObject) {
		if (worldObject.hasId()) {
			WorldObject existing = find(worldObject.id);
			if (existing != null) {
				return;
			}
		} else {
			worldObject.id = genId(worldObject);
		}

		PlayerItem global = PlayerItemGlobal(worldObject.playerItemId);
		if (global != null) {
			worldObject.maxHealth = global.maxHealth;
			worldObject.health = global.maxHealth;
		}

		WorldObject.db().save(worldObject);
		wobjects.put(worldObject.id, worldObject);
		worldObjectGrid.set(toTrackData(worldObject));
	}

	private PlayerItem PlayerItemGlobal(String id) {
		return PlayerItem(id, "global");

	}

	private PlayerItem PlayerItem(String id, String playerId) {
		return PlayerItem.db().findFirst("player_item_id = ? and player_item_player_id = ?", id, playerId);
	}

	private void updatePosition(WorldObject worldObject) {
		WorldObject existing = find(worldObject.id);
		existing.x = worldObject.x;
		existing.y = worldObject.y;
		existing.z = worldObject.z;
		existing.rx = worldObject.rx;
		existing.ry = worldObject.ry;
		existing.rz = worldObject.rz;
		existing.rw = worldObject.rw;

		WorldObject.db().save(existing);
		wobjects.put(existing.id, existing);
		worldObjectGrid.set(toTrackData(existing));
	}

	private TrackData toTrackData(WorldObject worldObject) {
		TrackData trackData = new TrackData();
		trackData.x = worldObject.x;
		trackData.y = worldObject.y;
		trackData.z = worldObject.z;
		trackData.id = worldObject.id;
		trackData.entityType = TrackData.EntityType.OTHER;
		return trackData;
	}

	private void load() {
		for (WorldObject worldObject : WorldObject.db().findAll()) {
			wobjects.put(worldObject.id, worldObject);
			worldObjectGrid.set(toTrackData(worldObject));
		}
	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		logger.warning("Player disconnect " + playerId);
		List<String> removed = new ArrayList<String>();
		String characterId = PlayerService.getInstance().getCharacter(playerId);
		for (WorldObject worldObject : playerAttached.values()) {
			if (worldObject.ownerId.equals(characterId)) {
				logger.warning("Removing  " + worldObject.id + " from grid");
				defaultGrid.remove(worldObject.id);
				removed.add(worldObject.id);
			}
		}
		for (String id : removed) {
			playerAttached.remove(id);
		}
	}

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub

	}

}
