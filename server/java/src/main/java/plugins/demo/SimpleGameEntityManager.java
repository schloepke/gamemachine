package plugins.demo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.gamemachine.core.GameEntityManager;
import io.gamemachine.core.CharacterService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Vitals.VitalsType;
import plugins.clientDbLoader.ClientDbLoader;

public class SimpleGameEntityManager implements GameEntityManager {

	private List<Vitals> baseVitals;
	private ConcurrentHashMap<String, Vitals> characterVitals = new ConcurrentHashMap<String, Vitals>();
	
	public SimpleGameEntityManager() {
		baseVitals = ClientDbLoader.getVitalsContainer().vitals;
	}
	
	
	public Vitals getVitalsTemplate(String templateName) {
		for (Vitals template : baseVitals) {
			if (template.templateName == templateName) {
				return template.clone();
			}
		}
		throw new RuntimeException("Unable to find vitals template "+templateName);
	}
	
	
	

	@Override
	public void OnCharacterCreated(Character character, Object data) {
		character.vitalsTemplate = "player";
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
			return getVitalsTemplate("build_object");
		} else {
			throw new RuntimeException("Invalid vitals type "+vitalsType.toString());
		}
		
	}

}
