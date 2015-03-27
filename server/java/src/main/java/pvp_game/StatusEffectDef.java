package pvp_game;

import io.gamemachine.messages.StatusEffect;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

public class StatusEffectDef {

	public static void createStatusEffects() {
		StatusEffectHandler.statusEffects = new ConcurrentHashMap<String, StatusEffect>();
		StatusEffectHandler.skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();
		
		
		// Passive
		StatusEffect spellResistPotion = createStatusEffect(StatusEffect.Type.SpellResistIncrease, "spell_resist_potion", 30, "passive", 100, 100, null);
		addSkill("spell_resist_potion",spellResistPotion);
		
		StatusEffect staff = createStatusEffect(StatusEffect.Type.ArmorIncrease, "staff", 120, "passive", 100, 100, null);
		addSkill("staff",staff);
		
		
		// Active
		StatusEffect deadlyAttack = createStatusEffect(StatusEffect.Type.AttributeDecrease, "deadly_attack", 1, "health", 500, 1000, null);
		StatusEffect cleave = createStatusEffect(StatusEffect.Type.AttributeDecrease, "cleave", 1, "health", 80, 100, null);
		StatusEffect charge = createStatusEffect(StatusEffect.Type.AttributeDecrease, "charge", 1, "health", 50, 60, null);
		StatusEffect poison = createStatusEffect(StatusEffect.Type.AttributeDecrease, "poison", 8, "health", 10, 15, null);
		StatusEffect lightningBolt = createStatusEffect(StatusEffect.Type.AttributeDecrease, "lightning_bolt", 1, "health", 40,50, "Thunder");
		StatusEffect healingRing = createStatusEffect(StatusEffect.Type.AttributeIncrease, "healing_ring", 10, "health", 20, 25,"Eternal Light");
		StatusEffect fireField = createStatusEffect(StatusEffect.Type.AttributeDecrease, "fire_field", 10, "health", 20, 25,"Eternal Flame");
		StatusEffect catapultExplosive = createStatusEffect(StatusEffect.Type.AttributeDecrease, "catapult_explosive", 1,	"health", 200, 300, null);
		StatusEffect speed = createStatusEffect(StatusEffect.Type.Speed, "speed", 1, "stamina", 5, 5, null);
		StatusEffect lightAttack = createStatusEffect(StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 40, 50, null);
				
		
		addSkill("light_attack",lightAttack);
		addSkill("deadly_attack",deadlyAttack);
		addSkill("cleave",cleave);
		addSkill("charge",charge);
		addSkill("poison_arrow",poison);
		addSkill("poison_arrow",lightAttack);
		addSkill("poison_blade",poison);
		addSkill("poison_blade",lightAttack);
		addSkill("lightning_bolt",lightningBolt);
		addSkill("staff_heal",healingRing);
		addSkill("fire_field",fireField);
		addSkill("catapult_explosive",catapultExplosive);
		addSkill("speed",speed);
		
		

//		createStatusEffect("health_regen1", StatusEffect.Type.AttributeIncrease, "health_regen1", 120, "health", 5, 10,	null);
//		createStatusEffect("stamina_regen1", StatusEffect.Type.AttributeIncrease, "stamina_regen1", 120, "stamina", 5,	10, null);
//		createStatusEffect("magic_regen1", StatusEffect.Type.AttributeIncrease, "magic_regen1", 120, "magic", 5, 10,null);
	
	}

	private static StatusEffect createStatusEffect(StatusEffect.Type type, String id, int ticks,
			String attribute, int minValue, int maxValue, String particleEffect) {
		StatusEffect e = new StatusEffect();
		e.type = type;
		e.id = id;
		e.ticks = ticks;
		e.attribute = attribute;
		e.minValue = minValue;
		e.maxValue = maxValue;
		e.particleEffect = particleEffect;
		return e;
	}
	
	private static void addSkill(String skill, StatusEffect e) {
		if (!StatusEffectHandler.skillEffects.containsKey(skill)) {
			StatusEffectHandler.skillEffects.put(skill, new ArrayList<StatusEffect>());
		}
		StatusEffectHandler.skillEffects.get(skill).add(e);
		StatusEffectHandler.statusEffects.put(e.id, e);
	}
}
