package plugins.core.combat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;
import io.gamemachine.messages.Vitals;
import plugins.landrush.BuildObjectHandler;
import scala.concurrent.duration.Duration;

public class VitalsRegen extends UntypedActor {

    public static String name = VitalsRegen.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(VitalsRegen.class);
    private Map<String, Long> deathTimer = new ConcurrentHashMap<String, Long>();
    private long outOfCombatTime = 10000L;
    private String zone = null;

    public static String actorName(String zone) {
        return VitalsRegen.name + zone;
    }

    public VitalsRegen(String zone) {
        this.zone = zone;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            if (message.equals("vitals_tick")) {
                updateVitals();
                tick(1000L, "vitals_tick");
            }
        }
    }

    @Override
    public void preStart() {
        tick(1000L, "vitals_tick");
    }

    public void tick(long delay, String message) {
        getContext().system().scheduler().scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(),
                message, getContext().dispatcher(), null);
    }

    private void updateVitals() {

        for (VitalsProxy vitalsProxy : VitalsHandler.getVitalsForZone(zone)) {

            int maxStamina = vitalsProxy.getMax("stamina");
            int maxMagic = vitalsProxy.getMax("magic");
            int maxHealth = vitalsProxy.getMax("health");

            if (vitalsProxy.isDead()) {
                if (deathTimer.containsKey(vitalsProxy.getEntityId())) {
                    Long timeDead = deathTimer.get(vitalsProxy.getEntityId());
                    if ((System.currentTimeMillis() - timeDead) > vitalsProxy.getDeathTime() * 1000L) {
                        revive(vitalsProxy);
                        deathTimer.remove(vitalsProxy.getEntityId());
                    }
                }
                continue;
            }

            if (vitalsProxy.get("health") <= 0) {
                die(vitalsProxy);
                if (vitalsProxy.getType() != Vitals.VitalsType.BuildObject) {
                    deathTimer.put(vitalsProxy.getEntityId(), System.currentTimeMillis());
                }

                continue;
            }

            int healthRegen = vitalsProxy.getMax("healthRegen");
            int magicRegen = vitalsProxy.getMax("magicRegen");
            int staminaRegen = vitalsProxy.getMax("staminaRegen");

            long lastCombat = System.currentTimeMillis() - vitalsProxy.getLastCombat();
            if (lastCombat < outOfCombatTime && vitalsProxy.getCombatRegenMod() > 0) {
                vitalsProxy.setInCombat(true);

                healthRegen = Math.round(healthRegen * (vitalsProxy.getCombatRegenMod() / 100f));
                magicRegen = Math.round(magicRegen * (vitalsProxy.getCombatRegenMod() / 100f));
                staminaRegen = Math.round(staminaRegen * (vitalsProxy.getCombatRegenMod() / 100f));

            } else {
                vitalsProxy.setInCombat(false);
            }

            if (vitalsProxy.get("health") < maxHealth && healthRegen > 0) {
                vitalsProxy.add("health", healthRegen);
                if (vitalsProxy.getType() == Vitals.VitalsType.BuildObject) {
                    BuildObjectHandler.setHealth(vitalsProxy.getEntityId(), vitalsProxy.get("health"), zone);
                }
            }

            if (vitalsProxy.get("stamina") < maxStamina && staminaRegen > 0) {
                vitalsProxy.add("stamina", staminaRegen);
            }

            if (vitalsProxy.get("magic") < maxMagic && magicRegen > 0) {
                vitalsProxy.add("magic", magicRegen);
            }

        }
    }

    private void die(VitalsProxy vitalsProxy) {
        logger.warn("Die " + vitalsProxy.getEntityId() + " " + vitalsProxy.getZoneName());
        vitalsProxy.setDead(1);
        vitalsProxy.set("health", 0);
        vitalsProxy.set("stamina", 0);
        vitalsProxy.set("magic", 0);
        vitalsProxy.setInCombat(false);
    }

    private void revive(VitalsProxy vitalsProxy) {
        logger.warn("Revive " + vitalsProxy.getEntityId() + " " + vitalsProxy.getZoneName());
        vitalsProxy.setDead(0);
        vitalsProxy.reset();
    }

}
