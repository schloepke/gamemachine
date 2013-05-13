package com.game_machine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.proto.Components;
import com.game_machine.proto.GameCommand;
import com.game_machine.proto.Player;
import com.game_machine.proto.PlayersAroundMe;

public class Entities {

	private static final Logger log = LoggerFactory.getLogger(Entities.class);

	private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();

	public Entities() {
	}

	public void addEntity(Entity entity) {
		entities.put(entity.getId(), entity);
	}

	public Entity getEntity(Integer id) {
		return entities.get(id);
	}

	public HashMap<Integer, Entity> getEntities() {
		return this.entities;
	}

	

	public static void fromComponents(Components components) {
		ArrayList<String> fieldNames = new ArrayList<String>();
		
		for (int i = 1; i < 100; i++) {
			String fieldName = components.getSchema().getFieldName(i);
			if (fieldName == null) {
				break;
			}
			fieldNames.add(fieldName);
			
		}
		
	}

	public static void template() {
		ArrayList<String> fieldNames = new ArrayList<String>();
		fieldNames.add("Player");
		fieldNames.add("PlayersAroundMe");
		fieldNames.add("GameCommand");
		Object[] args = new Object[] {fieldNames};
		Ruby.run("template.rb", "create_components", args, Boolean.class);
	}
	
	public Components toComponents() {
		Components components = new Components();
		for (Entity entity : this.entities.values()) {
			for (Map.Entry<String, Component> entry : entity.getComponents().entrySet()) {
				
				if (entry.getKey().equals("Player")) {
					components.addPlayer(Player.class.cast(entry.getValue()));
				}
				
				if (entry.getKey().equals("PlayersAroundMe")) {
					components.addPlayersAroundMe(PlayersAroundMe.class.cast(entry.getValue()));
				}
				
				if (entry.getKey().equals("GameCommand")) {
					components.addGameCommand(GameCommand.class.cast(entry.getValue()));
				}
			}
		}
		return components;
	}

	public static Entities createFrom(Components components) {
		Method method;
		Class<? extends Component> klass = null;
		String methodName = null;

		EntitiesHelper.
		ArrayList<Component> componentList = new ArrayList<Component>();
		Entities entities = new Entities();
		String fieldName = null;
		for (int i = 1; i < 10; i++) {
			fieldName = components.getSchema().getFieldName(i);
			if (fieldName != null) {
				methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1) + "List";
				log.info("methodName=" + methodName);
			}
		}
		return entities;

	}

	public static Class<? extends Component> findClassByName(String name) {
		String qualifiedClassName = null;
		Class<? extends Component> klass = null;

		qualifiedClassName = "com.game_machine.proto." + name;
		try {
			klass = (Class<? extends Component>) Class.forName(Class.forName(qualifiedClassName).getName());

		} catch (ClassNotFoundException e) {
		}
		if (klass == null) {
			throw new RuntimeException("Unable to find class (" + qualifiedClassName + ") from @class=" + name);
		}
		return klass;

	}

}
