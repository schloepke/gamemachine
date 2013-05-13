package com.game_machine.entity_system;

import java.util.ArrayList;

import com.dyuproject.protostuff.compiler.CompilerMain;
import com.game_machine.entity_system.Ruby;
import com.game_machine.entity_system.generated.Components;

public class ErbTemplate {

	public static void main(String[] args) {
		
		try {
			CompilerMain.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createEntitiesHelper();
	}
	
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
