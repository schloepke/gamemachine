package com.game_machine;

import com.dyuproject.protostuff.Tag;

public class ErrorComponent extends Component {

	@Tag(1)
	public String name;
	
	@Tag(2)
	public String message;
	
	public ErrorComponent() {
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
