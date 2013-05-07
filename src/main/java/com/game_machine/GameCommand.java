package com.game_machine;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class GameCommand  {

	private ImmutableMap<String, String> stringValues;
	private ImmutableMap<String, Integer> integerValues;
	private ImmutableMap<String, Boolean> booleanValues;
	
	private final Builder<String, String> stringBuilder;
	private final Builder<String, Integer> integerBuilder;
	private final Builder<String, Boolean> booleanBuilder;
	
	private final String command;
	private NetMessage netMessage = null;
	
	public GameCommand(String command) {
		this.command = command;
		stringBuilder = new ImmutableMap.Builder<String, String>();
		integerBuilder = new ImmutableMap.Builder<String, Integer>();
		booleanBuilder = new ImmutableMap.Builder<String, Boolean>();
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public GameCommand setNetMessage(NetMessage netMessage) {
		this.netMessage = netMessage;
		return this;
	}
	
	public NetMessage getNetMessage() {
		return this.netMessage;
	}
	
	public GameCommand build() {
		stringValues = stringBuilder.build();
		integerValues = integerBuilder.build();
		booleanValues = booleanBuilder.build();
		return this;
	}
	
	public String getString(String key) {
		return stringValues.get(key);
	}
	
	public Integer getInteger(String key) {
		return integerValues.get(key);
	}
	
	public Boolean getBoolean(String key) {
		return booleanValues.get(key);
	}
	
	public GameCommand setInteger(String key, Integer value) {
		integerBuilder.put(key,value);
		return this;
	}
	
	public GameCommand setString(String key, String value) {
		stringBuilder.put(key,value);
		return this;
	}
	
	public GameCommand setBoolean(String key, Boolean value) {
		booleanBuilder.put(key,value);
		return this;
	}
}
