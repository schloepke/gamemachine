package plugins.combat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameEntityManager;
import io.gamemachine.core.PlayerService;
import io.gamemachine.grid.GameGrid;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.Vitals;
import plugins.core.combat.ClientDbLoader;
import plugins.core.combat.VitalsHandler;
import plugins.landrush.BuildObjectHandler;

public class SimpleGameEntityManager implements GameEntityManager {

	private static final Logger logger = LoggerFactory.getLogger(SimpleGameEntityManager.class);
	private List<Vitals> baseVitals;
	public Map<Integer, BuildObject> buildObjects = new ConcurrentHashMap<Integer, BuildObject>();
	private ConcurrentHashMap<String, Vitals> characterVitals = new ConcurrentHashMap<String, Vitals>();
	private CombatDamage combatDamage;

	public SimpleGameEntityManager() {
		baseVitals = ClientDbLoader.getVitalsContainer().vitals;
		for (BuildObject buildObject : ClientDbLoader.getBuildObjects().getBuildObjectList()) {
			buildObjects.put(buildObject.templateId, buildObject);
		}

		combatDamage = new CombatDamage();
	}

	private Vitals getVitalsTemplate(Vitals.Template template) {
		for (Vitals vitals : baseVitals) {
			if (vitals.template == template) {
				return vitals.clone();
			}
		}
		throw new RuntimeException("Unable to find vitals template " + template.toString());
	}

	@Override
	public void OnCharacterCreated(Character character, Object data) {
		character.vitalsTemplate = Vitals.Template.PlayerTemplate;
		Character.db().save(character);
	}

	@Override
	public Vitals getBaseVitals(String characterId) {
		Character character = CharacterService.instance().find(characterId);
		return getVitalsTemplate(character.vitalsTemplate);
	}

	@Override
	public Vitals getBaseVitals(String entityId, Vitals.VitalsType vitalsType) {
		if (vitalsType == Vitals.VitalsType.BuildObject) {
			Vitals vitals = getVitalsTemplate(Vitals.Template.BuildObjectTemplate);

			BuildObject buildObject = BuildObjectHandler.find(entityId);
			vitals.health = buildObjects.get(buildObject.templateId).health;
			return vitals;
		} else {
			throw new RuntimeException("Invalid vitals type " + vitalsType.toString());
		}

	}

	@Override
	public int getEffectValue(StatusEffect statusEffect, PlayerSkill playerSkill, String characterId) {
		return combatDamage.getEffectValue(statusEffect, playerSkill, characterId);
	}

	@Override
	public void skillUsed(PlayerSkill playerSkill, String characterId) {
		combatDamage.skillUsed(playerSkill, characterId);
	}

	@Override
	public void OnPlayerConnected(String playerId) {
		logger.warn("OnPlayerConnect " + playerId);
		Player player = PlayerService.getInstance().find(playerId);
		int zone = GameGrid.getEntityZone(player.id);
	}

	@Override
	public void OnPlayerDisConnected(String playerId) {
		// TODO Auto-generated method stub

	}

}
