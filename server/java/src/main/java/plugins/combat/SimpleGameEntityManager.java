package plugins.combat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameEntityManager;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Vitals;
import plugins.core.combat.ClientDbLoader;
import plugins.landrush.BuildObjectHandler;

public class SimpleGameEntityManager implements GameEntityManager {

	private List<Vitals> baseVitals;
	public Map<Integer,BuildObject> buildObjects = new ConcurrentHashMap<Integer,BuildObject>();
	private ConcurrentHashMap<String, Vitals> characterVitals = new ConcurrentHashMap<String, Vitals>();
	
	public SimpleGameEntityManager() {
		baseVitals = ClientDbLoader.getVitalsContainer().vitals;
		for (BuildObject buildObject : ClientDbLoader.getBuildObjects().getBuildObjectList()) {
			buildObjects.put(buildObject.templateId, buildObject);
		}
	}
	
	
	public Vitals getVitalsTemplate(Vitals.Template template) {
		for (Vitals vitals : baseVitals) {
			if (vitals.template == template) {
				return vitals.clone();
			}
		}
		throw new RuntimeException("Unable to find vitals template "+template.toString());
	}
	
	
	

	@Override
	public void OnCharacterCreated(Character character, Object data) {
		character.vitalsTemplate = Vitals.Template.PlayerTemplate;
		Character.db().save(character);
	}


	@Override
	public Vitals getBaseVitals(String characterId) {
		if (!characterVitals.containsKey(characterId)) {
			Character character = CharacterService.instance().find(characterId);
			Vitals template = getVitalsTemplate(character.vitalsTemplate);
			characterVitals.put(characterId, template);
		}
		return characterVitals.get(characterId);
	}


	@Override
	public Vitals getBaseVitals(String entityId, Vitals.VitalsType vitalsType) {
		if (vitalsType == Vitals.VitalsType.BuildObject) {
			Vitals vitals = getVitalsTemplate(Vitals.Template.BuildObjectTemplate);
			
			BuildObject buildObject = BuildObjectHandler.find(entityId);
			vitals.health = buildObjects.get(buildObject.templateId).health;
			return vitals;
		} else {
			throw new RuntimeException("Invalid vitals type "+vitalsType.toString());
		}
		
	}

}
