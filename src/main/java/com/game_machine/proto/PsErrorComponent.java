package com.game_machine.proto;

import com.dyuproject.protostuff.Tag;

public class PsErrorComponent extends PsComponent {

	@Tag(1)
	public String name;
	
	@Tag(2)
	public String message;
	
	public PsErrorComponent() {
		this.name = this.getClass().getSimpleName();
	}
		
	public String getName() {
		return this.name;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
