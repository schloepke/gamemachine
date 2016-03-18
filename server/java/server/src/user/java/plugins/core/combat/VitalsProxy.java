package plugins.core.combat;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.VitalsUpdate;

public class VitalsProxy {

    private static final Logger logger = LoggerFactory.getLogger(VitalsProxy.class);

    private static Map<String, Field> fields = new ConcurrentHashMap<String, Field>();
    private Map<String, Boolean> changed = new ConcurrentHashMap<String, Boolean>();
    private static AtomicInteger nextUpdateId = new AtomicInteger();

    private long lastUpdate = 0L;
    private long changeTime = 5000L;
    private final String max = "Max";

    private Vitals vitals;

    public VitalsProxy(Vitals vitals) {
        vitals.updateId = nextUpdateId.incrementAndGet();
        this.vitals = vitals;
        reset();
    }

    public VitalsUpdate getVitalsUpdate() {
        VitalsUpdate update = new VitalsUpdate();
        update.updateId = vitals.updateId;
        update.dead = vitals.dead;
        update.inCombat = vitals.inCombat;
        for (String attribute : changed.keySet()) {
            update.addAttribute(attribute);
            update.addValue(get(attribute));
            update.addMax(getMax(attribute));
        }
        return update;
    }

    public boolean hasChanged() {
        if (System.currentTimeMillis() - lastUpdate < changeTime) {
            return true;
        } else {
            changed.clear();
            return false;
        }
    }

    public void reset() {
        for (Vitals.Attribute attribute : Vitals.Attribute.values()) {
            String name = Introspector.decapitalize(attribute.toString());
            int maxValue = getMax(name);
            int value = get(name);
            if (maxValue != value) {
                set(name, maxValue);
            }
        }
        lastUpdate = System.currentTimeMillis();
    }

    public Vitals getVitals() {
        return vitals;
    }

    public float getDeathTime() {
        return vitals.deathTime;
    }
    public void setDeathTime(float deathTime) {
        vitals.deathTime = deathTime;
    }

    public float getCombatRegenMod() {
        return vitals.combatRegenMod;
    }

    public long getLastCombat() {
        return vitals.lastCombat;
    }

    public String getZoneName() {
        return vitals.zoneName;
    }

    public void setLastCombat(long lastCombat) {
        vitals.lastCombat = lastCombat;
        lastUpdate = System.currentTimeMillis();
    }

    public String getCharacterId() {
        return vitals.characterId;
    }

    public String getEntityId() {
        return vitals.entityId;
    }

    public Vitals.VitalsType getType() {
        return vitals.type;
    }

    public Vitals.Template getTemplate() {
        return vitals.template;
    }

    public void setInCombat(boolean inCombat) {
        vitals.inCombat = inCombat;
    }

    public void setDead(int dead) {
        vitals.dead = dead;
        lastUpdate = System.currentTimeMillis();
    }

    public boolean isDead() {
        return (vitals.dead == 1);
    }

    public int get(String name) {
        return get(vitals, name);
    }

    public void set(String name, int value) {
        set(vitals, name, value);
        lastUpdate = System.currentTimeMillis();
        changed.put(name, true);
    }

    public void add(String name, int value) {
        int current = get(vitals, name);
        int baseValue = getMax(name);
        if (current + value > baseValue) {
            set(vitals, name, baseValue);
        } else {
            set(vitals, name, current + value);
        }
        lastUpdate = System.currentTimeMillis();
        changed.put(name, true);
    }

    public void subtract(String name, int value) {
        int current = get(vitals, name);
        if (current - value < 0) {
            set(vitals, name, 0);
        } else {
            set(vitals, name, current - value);
        }
        lastUpdate = System.currentTimeMillis();
        changed.put(name, true);
    }

    public void setToMax(String name) {
        int baseValue = getMax(name + max);
        set(vitals, name, baseValue);
    }

    public int getMax(String name) {
        return (int) get(vitals, name + max);
    }

    public void setMax(String name, int value) {
        int current = get(vitals, name + max);
        set(vitals, name + max, value);
        updateFromMax(name, current, value);
    }

    public void addMax(String name, int value) {
        int current = get(vitals, name + max);
        set(vitals, name + max, current + value);
        updateFromMax(name, current, current + value);
    }

    public void subtractMax(String name, int value) {
        int current = get(vitals, name + max);
        set(vitals, name + max, current - value);
        updateFromMax(name, current, current - value);
    }

    private void updateFromMax(String name, int current, int updated) {
        add(name, updated - current);
    }


    private int get(Vitals vitals, String name) {
        try {
            Field field = getField(name);
            return field.getInt(vitals);
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
            logger.warn("getAttribute error " + name + " " + e.getMessage() + " returning 0");
            return 0;
        }
    }

    private void set(Vitals vitals, String name, int value) {
        try {
            Field field = getField(name);
            field.setInt(vitals, value);
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
            logger.warn("setAttribute error " + name + " " + e.getMessage() + " no value set");
        }
    }

    private Field getField(String name) {
        if (fields.containsKey(name)) {
            return fields.get(name);
        } else {
            Field field;
            try {
                field = Vitals.class.getField(name);
                fields.put(name, field);
                return field;
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                throw new RuntimeException("Vitals field error " + e.getMessage());
            }

        }
    }

}
