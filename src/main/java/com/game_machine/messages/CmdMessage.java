package com.game_machine.messages;

public class CmdMessage {

	private final String cmd;
	private final Object data;
	private final String result;
	private final Boolean error;
	
	public CmdMessage(String cmd, Object data, String result, Boolean error) {
		this.cmd = cmd;
		this.data = data;
		this.result = result;
		this.error = error;
	}
	
	public String getCmd() {
		return this.cmd;
	}
	
	public Object getData() {
		return this.data;
	}
	
	public String getResult() {
		return this.result;
	}
	
	public CmdMessage setResult(String result) {
		return new CmdMessage(this.cmd, this.data, result, this.error);
	}
	
	public CmdMessage setError(Boolean error) {
		return new CmdMessage(this.cmd, this.data, this.result, error);
	}
}
