package plugins.combat;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameEntityManager;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Vitals;
import plugins.core.combat.ClientDbLoader;

public class SimpleGameEntityManager implements GameEntityManager {

	private List<Vitals> baseVitals;
	private ConcurrentHashMap<String, Vitals> characterVitals = new ConcurrentHashMap<String, Vitals>();
	
	public SimpleGameEntityManager() {
		baseVitals = ClientDbLoader.getVitalsContainer().vitals;
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
			return getVitalsTemplate(Vitals.Template.BuildObjectTemplate);
		} else {
			throw new RuntimeException("Invalid vitals type "+vitalsType.toString());
		}
		
	}

}
