package com.game_machine;

import java.util.HashMap;

public class Entity {

	private HashMap<String,Component> components = new HashMap<String,Component>();
	
	public Entity() {
	}
	
	public void addComponent(Component component) {
		String componentName = component.getClass().getSimpleName();
		if (components.containsKey(componentName)) {
			throw new RuntimeException("Entity already has a component named " + componentName);
		}
		components.put(componentName, component);
	}

	public void removeComponent(Component component) {
		components.remove(component.getClass().getSimpleName());
	}

	public <T extends Component> T getComponent(String name) {
		return (T) components.get(name);
	}
	
	public Boolean hasComponent(String name) {
		return getComponent(name) == null ? false : true;
	}

}
