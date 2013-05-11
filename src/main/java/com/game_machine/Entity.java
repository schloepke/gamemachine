package com.game_machine;

import java.util.ArrayList;
import java.util.HashMap;

import com.dyuproject.protostuff.Tag;

public class Entity {

	@Tag(2)
	public ModelTest model = null;
	
	@Tag(3)
	public ArrayList<String> componentList = new ArrayList<String>();
	
	@Tag(4)
	public HashMap<String,String> map = new HashMap<String,String>();
	
	@Tag(5)
	public HashMap<String,Component> components = new HashMap<String,Component>();
	
	public Entity() {
		
	}
	
	public void addComponent(Component component) {
		components.put(component.getName(), component);
	}
	
	public <T extends Component> T getComponent(String name) {
		return (T) components.get(name);
	}
}
