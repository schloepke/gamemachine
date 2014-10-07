package com.game_machine.core;

public class CacheUpdate {

	public static final int SET = 0;
	public static final int INCREMENT = 1;
	public static final int DECREMENT = 2;
	
	private Class<?> klass;
	private Object message;
	private String id;
	private Object fieldValue = null;
	private String field = null;
	private int updateType = 0;
	
	public CacheUpdate(Class<?> klass, Object message) {
		this.setKlass(klass);
		this.setMessage(message);
	}
	
	public CacheUpdate(Class<?> klass, String id, Object fieldValue, String field, int updateType) {
		this.setKlass(klass);
		this.setId(id);
		this.setFieldValue(fieldValue);
		this.setField(field);
		this.setUpdateType(updateType);
	}

	
	public Class<?> getKlass() {
		return klass;
	}

	public void setKlass(Class<?> klass) {
		this.klass = klass;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public int getUpdateType() {
		return updateType;
	}

	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}
}
