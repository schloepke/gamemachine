package com.game_machine.core;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import GameMachine.Messages.Entity;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.serialization.JSerializer;

public class EntitySerializer extends JSerializer {

	private static final Logger log = LoggerFactory.getLogger(EntitySerializer.class);
	// This is whether "fromBinary" requires a "clazz" or not
	@Override
	public boolean includeManifest() {
		return true;
	}

	// Pick a unique identifier for your Serializer,
	// you've got a couple of billions to choose from,
	// 0 - 16 is reserved by Akka itself
	@Override
	public int identifier() {
		return 1234567;
	}

	// "toBinary" serializes the given object to an Array of Bytes
	@Override
	public byte[] toBinary(Object obj) {
		// Entity entity = (Entity) obj;
		// return entity.toByteArray();
		byte[] bytes = null;
		Method m;
		try {
			m = obj.getClass().getMethod("toByteArray");
			bytes = (byte[]) m.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	// "fromBinary" deserializes the given array,
	// using the type hint (if any, see "includeManifest" above)
	@Override
	public Object fromBinaryJava(byte[] bytes, Class<?> clazz) {
		//log.error("fromBinaryJava = " + clazz.getName());
		Method m;
		try {
			m = clazz.getMethod("parseFrom", byte[].class);
			return clazz.cast(m.invoke(clazz,bytes));
		} catch (Exception e) {
			log.error("clazz = " + clazz);
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		//Entity entity = Entity.parseFrom(bytes);
		//return entity;
	}
}
