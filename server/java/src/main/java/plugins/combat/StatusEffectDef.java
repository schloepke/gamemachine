package plugins.combat;

import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.PlayerSkills;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffects;
import plugins.clientDbLoader.ClientDbLoader;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import java.util.List;

public class StatusEffectDef {

	private static final Logger logger = LoggerFactory.getLogger(StatusEffectDef.class);
	
	public static void createStatusEffects() {
		StatusEffectHandler.statusEffects = new ConcurrentHashMap<String, StatusEffect>();
		StatusEffectHandler.skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();
		
		StatusEffects effects = ClientDbLoader.getStatusEffects();
		
		PlayerSkills playerSkills = ClientDbLoader.getPlayerSkills();
		for (PlayerSkill playerSkill : playerSkills.playerSkill) {
			if (!Strings.isNullOrEmpty(playerSkill.statusEffects)) {
				for (String effectId : playerSkill.statusEffects.split(",")) {
					for (StatusEffect effect : effects.statusEffect) {
						if (effect.id.equals(effectId)) {
							addSkill(playerSkill.id,effect);
						}
					}
				}
			}
		}
		
		return;

		// Passive
//		StatusEffect spellResist = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.SpellResistIncrease, "spell_resist", 30, "passive", 100, 100, null);
//
//		StatusEffect root = createStatusEffect(StatusEffect.DamageType.None, StatusEffect.Type.Root, "root", 4,
//				"passive", 100, 100, null);
//
//		StatusEffect staff = createStatusEffect(StatusEffect.DamageType.None, StatusEffect.Type.ArmorIncrease, "staff",
//				120, "passive", 100, 100, null);
//		addSkill("staff", staff);
//
//		// Active
//		StatusEffect deadlyAttack = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.AttributeDecrease, "deadly_attack", 1, "health", 500, 1000, null);
//		StatusEffect cleave = createStatusEffect(StatusEffect.DamageType.None, StatusEffect.Type.AttributeDecrease,
//				"cleave", 1, "health", 80, 100, null);
//		StatusEffect charge = createStatusEffect(StatusEffect.DamageType.None, StatusEffect.Type.AttributeDecrease,
//				"charge", 1, "health", 50, 60, null);
//		StatusEffect poison = createStatusEffect(StatusEffect.DamageType.None, StatusEffect.Type.AttributeDecrease,
//				"poison", 8, "health", 20, 30, null);
//		StatusEffect lightningBolt = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.AttributeDecrease, "lightning_bolt", 1, "health", 40, 50, "Thunder");
//		StatusEffect healingRing = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.AttributeIncrease, "healing_ring", 10, "health", 20, 25, "Eternal Light");
//		StatusEffect fireField = createStatusEffect(StatusEffect.DamageType.None, StatusEffect.Type.AttributeDecrease,
//				"fire_field", 10, "health", 20, 25, "Eternal Flame");
//		StatusEffect catapultExplosive = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.AttributeDecrease, "catapult_explosive", 1, "health", 300, 500, null);
//		StatusEffect ballistaExplosive = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.AttributeDecrease, "ballista_explosive", 1, "health", 300, 500, null);
//		StatusEffect speed = createStatusEffect(StatusEffect.DamageType.None, StatusEffect.Type.Speed, "speed", 1,
//				"stamina", 5, 5, null);
//		StatusEffect lightAttack = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 40, 50, null);
//		StatusEffect meleeAutoAttack = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.AttributeDecrease, "melee_auto_attack", 1, "health", 40, 50, null);
//		StatusEffect rangedAutoAttack = createStatusEffect(StatusEffect.DamageType.None,
//				StatusEffect.Type.AttributeDecrease, "ranged_auto_attack", 1, "health", 40, 50, null);
//		StatusEffect fireAura = createStatusEffect(StatusEffect.DamageType.None, StatusEffect.Type.AttributeDecrease,
//				"fire_aura", 10, "health", 10, 15, null);
//
//		addSkill("fire_grasp_damage", fireAura);
//		addSkill("fire_grasp_resist", spellResist);
//		addSkill("fire_grasp_root", root);
//
//		addSkill("1hsword_auto_attack", meleeAutoAttack);
//		addSkill("2hsword_auto_attack", meleeAutoAttack);
//		addSkill("bow_auto_attack", rangedAutoAttack);
//		addSkill("staff_auto_attack", rangedAutoAttack);
//
//		addSkill("light_attack", lightAttack);
//		addSkill("deadly_attack", deadlyAttack);
//		addSkill("cleave", cleave);
//		addSkill("charge", charge);
//		addSkill("poison_arrow", poison);
//		addSkill("poison_arrow", lightAttack);
//		addSkill("poison_blade", poison);
//		addSkill("poison_blade", lightAttack);
//		addSkill("lightning_bolt", lightningBolt);
//		addSkill("staff_heal", healingRing);
//		addSkill("fire_field", fireField);
//		addSkill("catapult_explosive", catapultExplosive);
//		addSkill("ballista_explosive", ballistaExplosive);
//		addSkill("speed", speed);

		// createStatusEffect("health_regen1",
		// StatusEffect.Type.AttributeIncrease, "health_regen1", 120, "health",
		// 5, 10, null);
		// createStatusEffect("stamina_regen1",
		// StatusEffect.Type.AttributeIncrease, "stamina_regen1", 120,
		// "stamina", 5, 10, null);
		// createStatusEffect("magic_regen1",
		// StatusEffect.Type.AttributeIncrease, "magic_regen1", 120, "magic", 5,
		// 10,null);

	}

	private static StatusEffect createStatusEffect(StatusEffect.DamageType damageType, StatusEffect.Type type,
			String id, int ticks, String attribute, int minValue, int maxValue, String particleEffect) {
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
