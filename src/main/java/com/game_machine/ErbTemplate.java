package com.game_machine;

import java.util.ArrayList;

import com.game_machine.proto.Components;

public class ErbTemplate {

	public static void createEntitiesHelper() {
		ArrayList<String> fieldNames = new ArrayList<String>();
		String fieldName = null;
		
		for (int i = 1; i < 1000; i++) {
			fieldName = Components.getSchema().getFieldName(i);
			if (fieldName == null) {
				break;
			}
			System.out.println(fieldName);
			fieldNames.add(fieldName);
		}
		Object[] args = new Object[] {fieldNames};
		Ruby.run("template.rb", "create_components", args, Boolean.class);
	}

}
