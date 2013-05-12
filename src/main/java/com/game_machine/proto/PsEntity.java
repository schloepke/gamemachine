package com.game_machine.proto;

import java.util.ArrayList;
import java.util.HashMap;

import com.dyuproject.protostuff.Tag;

public class PsEntity {

	@Tag(2)
	public PsModelTest model = null;
	
	@Tag(3)
	public ArrayList<String> componentList = new ArrayList<String>();
	
	@Tag(4)
	public HashMap<String,String> map = new HashMap<String,String>();
	
	@Tag(5)
	public HashMap<String,PsComponent> components = new HashMap<String,PsComponent>();
	
	public PsEntity() {
		
	}
	
	public void addComponent(PsComponent component) {
		components.put(component.getName(), component);
	}
	
	public <T extends PsComponent> T getComponent(String name) {
		return (T) components.get(name);
	}
}
