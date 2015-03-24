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
		createStatusEffect("spell_resist_potion", StatusEffect.Type.SpellResistIncrease, "spell_resist_potion", 30,
				"passive", 100, 100, null);
		createStatusEffect("staff", StatusEffect.Type.ArmorIncrease, "staff", 120, "passive", 100, 100, null);

		// Active
		createStatusEffect("deadly_attack", StatusEffect.Type.AttributeDecrease, "deadly_attack", 1, "health", 500,
				1000, null);
		createStatusEffect("light_attack", StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 50, 100,
				null);

		createStatusEffect("cleave", StatusEffect.Type.AttributeDecrease, "cleave", 1, "health", 50, 100, null);
		createStatusEffect("charge", StatusEffect.Type.AttributeDecrease, "charge", 1, "health", 100, 100, null);

		createStatusEffect("poison_arrow", StatusEffect.Type.AttributeDecrease, "poison", 8, "health", 50, 100, null);
		createStatusEffect("poison_arrow", StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 50, 100,
				null);

		createStatusEffect("poison_blade", StatusEffect.Type.AttributeDecrease, "poison", 8, "health", 50, 100, null);
		createStatusEffect("poison_blade", StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 50, 100,
				null);
		createStatusEffect("lightning_bolt", StatusEffect.Type.AttributeDecrease, "lightning_bolt", 1, "health", 200,
				400, "Thunder");
		createStatusEffect("staff_heal", StatusEffect.Type.AttributeIncrease, "healing_ring", 5, "health", 100, 200,
				"Eternal Light");
		createStatusEffect("fire_field", StatusEffect.Type.AttributeDecrease, "fire_field", 5, "health", 50, 100,
				"Eternal Flame");
		createStatusEffect("catapult_explosive", StatusEffect.Type.AttributeDecrease, "catapult_explosive", 1,
				"health", 200, 300, null);

		createStatusEffect("health_regen1", StatusEffect.Type.AttributeIncrease, "health_regen1", 120, "health", 5, 10,
				null);
		createStatusEffect("stamina_regen1", StatusEffect.Type.AttributeIncrease, "stamina_regen1", 120, "stamina", 5,
				10, null);
		createStatusEffect("magic_regen1", StatusEffect.Type.AttributeIncrease, "magic_regen1", 120, "magic", 5, 10,
				null);
		
		createStatusEffect("speed", StatusEffect.Type.Speed, "speed", 1, "stamina", 5, 5,
				null);

	}

	private static void createStatusEffect(String skill, StatusEffect.Type type, String id, int ticks,
			String attribute, int minValue, int maxValue, String particleEffect) {
		StatusEffect e = new StatusEffect();
		e.type = type;
		e.id = id;
		e.ticks = ticks;
		e.attribute = attribute;
		e.minValue = minValue;
		e.maxValue = maxValue;
		e.particleEffect = particleEffect;
		if (!StatusEffectHandler.skillEffects.containsKey(skill)) {
			StatusEffectHandler.skillEffects.put(skill, new ArrayList<StatusEffect>());
		}
		StatusEffectHandler.skillEffects.get(skill).add(e);
		StatusEffectHandler.statusEffects.put(id, e);
	}
}
