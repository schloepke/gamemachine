package com.game_machine.entity_system;

import java.util.HashMap;

public class Entity {

	private HashMap<String,Component> components = new HashMap<String,Component>();
	private final Integer id;
	private String clientId;
	
	public Entity(Integer id) {
		this.id = id;
	}
		
	public Integer getId() {
		return this.id;
	}
	
	public String getClientId() {
		return this.clientId;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public HashMap<String,Component> getComponents() {
		return this.components;
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

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(String name) {
		return (T) components.get(name);
	}
	
	public Boolean hasComponent(String name) {
		return getComponent(name) == null ? false : true;
	}

}
