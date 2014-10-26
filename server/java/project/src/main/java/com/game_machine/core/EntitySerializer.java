package com.game_machine.core;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.serialization.JSerializer;

public class EntitySerializer extends JSerializer {

	private static ConcurrentHashMap<Class<?>, Method> serializeMethods = new ConcurrentHashMap<Class<?>, Method>();
	private static ConcurrentHashMap<Class<?>, Method> deserializeMethods = new ConcurrentHashMap<Class<?>, Method>();
	
	private static ConcurrentHashMap<Class<?>, Method> serializeJsonMethods = new ConcurrentHashMap<Class<?>, Method>();
	private static ConcurrentHashMap<Class<?>, Method> deserializeJsonMethods = new ConcurrentHashMap<Class<?>, Method>();
	private static final Logger logger = LoggerFactory.getLogger(EntitySerializer.class);
	
	public static String toJson(Object obj) {
		String value = null;
		Method m;
		Class<?> clazz = obj.getClass();
		try {
			if (serializeJsonMethods.containsKey(clazz)) {
				m = serializeJsonMethods.get(clazz);
			} else {
				m = obj.getClass().getMethod("toJson");
				serializeJsonMethods.put(clazz, m);
			}
			value = (String) m.invoke(obj);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return value;
	}
	
	public static byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		Method m;
		Class<?> clazz = obj.getClass();
		try {
			if (serializeMethods.containsKey(clazz)) {
				m = serializeMethods.get(clazz);
			} else {
				m = obj.getClass().getMethod("toByteArray");
				serializeMethods.put(clazz, m);
			}
			bytes = (byte[]) m.invoke(obj);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

	public static Object fromJson(String value, Class<?> clazz) {
		Method m;
		try {
			if (deserializeJsonMethods.containsKey(clazz)) {
				m = deserializeJsonMethods.get(clazz);
			} else {
				m = clazz.getMethod("parseFromJson", String.class);
				deserializeJsonMethods.put(clazz, m);
			}
			return clazz.cast(m.invoke(clazz, value));
		} catch (Exception e) {
			logger.error("clazz = " + clazz);
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object fromByteArray(byte[] bytes, Class<?> clazz) {
		Method m;
		try {
			if (deserializeMethods.containsKey(clazz)) {
				m = deserializeMethods.get(clazz);
			} else {
				m = clazz.getMethod("parseFrom", byte[].class);
				deserializeMethods.put(clazz, m);
			}
			return clazz.cast(m.invoke(clazz, bytes));
		} catch (Exception e) {
			logger.error("clazz = " + clazz);
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	// This is whether "fromBinary" requires a "clazz" or not
	@Override
	public boolean includeManifest() {
		return true;
	}

	@Override
	public int identifier() {
		return 1234567;
	}

	@Override
	public byte[] toBinary(Object obj) {
		return toByteArray(obj);
	}

	@Override
	public Object fromBinaryJava(byte[] bytes, Class<?> clazz) {
		return fromByteArray(bytes, clazz);
	}
}
