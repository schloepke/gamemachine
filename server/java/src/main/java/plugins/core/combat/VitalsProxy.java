package plugins.core.combat;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.messages.Vitals;

public class VitalsProxy {

	private static final Logger logger = LoggerFactory.getLogger(VitalsProxy.class);
	
	private static ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<String, Field>();
	
	
	public Vitals vitals;
	public Vitals baseVitals;
	
	public VitalsProxy(Vitals vitals) {
		this.vitals = vitals;
		this.baseVitals = vitals.clone();
	}
	
	public int get(String name) {
		return get(vitals,name);
	}
	
	public void set(String name, int value) {
		set(vitals,name,value);
	}
	
	public void add(String name, int value) {
		int current = get(vitals,name);
		int baseValue = get(baseVitals,name);
		if (current + value > baseValue) {
			set(vitals,name,baseValue);
		} else {
			set(vitals,name,current+value);
		}
	}
	
	public void subtract(String name, int value) {
		int current = get(vitals,name);
		if (current - value < 0) {
			set(vitals,name,0);
		} else {
			set(vitals,name,current-value);
		}
	}
	
	public void setToBase(String name) {
		int baseValue = get(baseVitals,name);
		set(vitals,name,baseValue);
	}
	
	public int getBase(String name) {
		return (int)get(baseVitals,name);
	}
	
	public void setBase(String name, int value) {
		set(baseVitals,name,value);
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
