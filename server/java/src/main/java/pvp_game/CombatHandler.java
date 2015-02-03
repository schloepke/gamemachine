package pvp_game;

import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.Attack;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.PvpGameMessage;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vector3;
import io.gamemachine.messages.Vitals;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CombatHandler extends GameMessageActor {

	public static String name = CombatHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private static int maxHealth = 1000;
	private static int worldOffset = 7000;

	private static ConcurrentHashMap<String, PlayerSkill> playerSkills = new ConcurrentHashMap<String, PlayerSkill>();
	private static ConcurrentHashMap<String, Vitals> playerVitals = new ConcurrentHashMap<String, Vitals>();

	@Override
	public void awake() {
		List<PlayerSkill> skills = PlayerSkill.db().where("player_skill_character_id = ?", "global");
		for (PlayerSkill playerSkill : skills) {
			playerSkills.put(playerSkill.id, playerSkill);
			logger.warning("Loading skill "+playerSkill.id);
		}
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (gameMessage.hasPvpGameMessage()) {
			updateVitals();
			tick(1000L, tickMessage());
			return;
		}

		if (gameMessage.hasAttack()) {
			doAttack(gameMessage.attack);
		}

	}

	private void updateVitals() {
		for (Vitals vitals : playerVitals.values()) {
			if (vitals.health < maxHealth) {
				vitals.health += 20;
				if (vitals.health > maxHealth) {
					vitals.health = maxHealth;
				}
			}
		}
		sendVitals();
	}

	private void sendVitals() {
		GameMessage msg = new GameMessage();

		Grid grid = GameGrid.getGameGrid("mygame", "default");
		for (TrackData trackData : grid.getAll()) {
			msg.vitals = getVitals(trackData.id);
			PlayerCommands.sendGameMessage(msg, trackData.id);
		}
	}

	private void sendAttack(Attack attack) {
		GameMessage msg = new GameMessage();

		Grid grid = GameGrid.getGameGrid("mygame", "default");
		for (TrackData trackData : grid.getAll()) {
			msg.attack = attack;
			PlayerCommands.sendGameMessage(msg, trackData.id);
		}
	}

	private void doAttack(Attack attack) {
		logger.warning("Attack skill "+attack.skill);
		PlayerSkill skill = playerSkills.get(attack.skill);
		int damage = 0;
		if (skill.damageType.equals("aoe_heal")) {
			damage = randInt(50, 200);
			applyAoe(attack,1, damage, skill.radius,attack.targetLocation);
		} else if (skill.damageType.equals("aoe")) {
			damage = randInt(50, 200);
			applyAoe(attack,0, damage, skill.radius,attack.targetLocation);
		} else if (skill.damageType.equals("pbaoe")) {
			damage = randInt(100, 200);
			applyAoe(attack,0, damage, skill.radius,attack.targetLocation);
		} else if (skill.damageType.equals("st")) {
			attack.health = randInt(150, 300);
			applySt(attack.target,0, attack.health);
		}
		logger.warning("damageType "+skill.damageType+" damage "+attack.health);
		sendVitals();
		sendAttack(attack);
	}

	private void applySt(String target, int type, int damage) {
		Vitals vitals = getVitals(target);
		if (type == 0) {
			vitals.health -= damage;
		} else {
			vitals.health += damage;
		}
	}

	private void applyAoe(Attack attack, int type, int damage, int radius, Vector3 location) {
		Grid grid = GameGrid.getGameGrid("mygame", "default");
		for (TrackData trackData : grid.getAll()) {
			double distance = distance(location.xi,location.yi,location.zi,trackData);
			//logger.warning("Distance "+distance);
			if (distance <= radius) {
				applySt(trackData.id,type,damage);
				attack.health = damage;
			}
			
		}
	}

	private double scale(int i) {
		return (i / 100l) - worldOffset;
	}
	
	public double distance(int x, int y, int z, TrackData tdata) {
		double x2 = scale(tdata.x);
		double y2 = scale(tdata.y);
		double z2 = scale(tdata.z);
		double x1 = scale(x);
		double y1 = scale(y);
		double z1 = scale(z);
		//logger.warning("origin: "+x1+"."+y1+"."+z1+" target: "+x2+"."+y2+"."+z2);
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
	}
	
	private Vitals getVitals(String id) {
		if (!playerVitals.containsKey(id)) {
			Vitals vitals = new Vitals();
			vitals.id = id;
			vitals.health = maxHealth;
			playerVitals.put(vitals.id, vitals);
		}
		return playerVitals.get(id);
	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		// TODO Auto-generated method stub

	}

	private GameMessage tickMessage() {
		GameMessage msg = new GameMessage();
		PvpGameMessage pvpGameMessage = new PvpGameMessage();
		pvpGameMessage.command = 1;
		msg.pvpGameMessage = pvpGameMessage;
		return msg;
	}

	@Override
	public void preStart() {
		awake();
		tick(1000L, tickMessage());
	}

	public void tick(long delay, GameMessage message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
