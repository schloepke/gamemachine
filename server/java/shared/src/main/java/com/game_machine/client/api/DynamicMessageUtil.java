package com.game_machine.client.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import Client.Messages.DynamicMessage;
import Client.Messages.GameMessage;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.game_machine.util.LocalLinkedBuffer;

public class DynamicMessageUtil {

	private static String packageName = "user.messages.";
	
	/**
	 * Serializes the {@code message} into a {@link GameMessage}
	 * @return a {@link GameMessage}
	 */
	public static <T> GameMessage toGameMessage(T message) {
		DynamicMessage dynamicMessage = toDynamicMessage(message);
		GameMessage gameMessage = new GameMessage();
		gameMessage.setDynamicMessage(dynamicMessage);
		return gameMessage;
	}
	
	/**
	 * Serializes the {@code message} into a {@link DynamicMessage}
	 * @return a {@link DynamicMessage}
	 */
	public static <T> DynamicMessage toDynamicMessage(T message) {
		DynamicMessage dynamicMessage = new DynamicMessage();
		byte[] bytes = toByteArray(message);
		dynamicMessage.setMessage(ByteString.copyFrom(bytes));
		dynamicMessage.setType(message.getClass().getSimpleName());
		return dynamicMessage;
	}
	
	/**
	 * Extracts a generic message from a {@link GameMessage}
	 * @return a generic message
	 */
	public static <T> T fromGameMessage(GameMessage gameMessage)  {
		return fromByteArray(gameMessage.getDynamicMessage().getType(),gameMessage.getDynamicMessage().getMessage().toByteArray());
	}
	
	/**
	 * Extracts a generic message from a {@link DynamicMessage}
	 * @return a generic message
	 */
	public static <T> T fromDynamicMessage(DynamicMessage dynamicMessage)  {
		return fromByteArray(dynamicMessage.getType(),dynamicMessage.getMessage().toByteArray());
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T fromByteArray(String classname, byte[] bytes)  {
		try {
			Class<?> clazz = Class.forName(packageName + classname);
			return (T) fromByteArray(clazz,bytes);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid class name for message");
		}
	}
	
	public static <T> T fromByteArray(Class<T> klass, byte[] bytes)  {
		T message;
		try {
			message = (T) klass.newInstance();
			ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(klass));
			return message;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Protobuf decoding failed");
		}
		
	}
	
	public static <T> T fromJson(Class<T> klass, String json)  {
		T message;
		byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
		try {
			message = (T) klass.newInstance();
			JsonIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(klass), false);
			return message;
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Protobuf decoding failed");
		}
	}
	
	
	
	public static <T> byte[] toByteArray(T message) {
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) Class.forName(packageName + message.getClass().getSimpleName());
			return toByteArray(clazz,message);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid class name for message");
		}
	}
	
	public static <T> byte[] toByteArray(Class<T> klass,T message) {
		LinkedBuffer buffer = LocalLinkedBuffer.get();
		byte[] bytes = null;

		try {
			bytes = ProtobufIOUtil.toByteArray(message, RuntimeSchema.getSchema(klass), buffer);
			buffer.clear();
		} catch (Exception e) {
			buffer.clear();
			e.printStackTrace();
			throw new RuntimeException("Protobuf encoding failed");
		}
		return bytes;
	}
	
	public static <T> String toJson(Class<T> klass,T message) {
		boolean numeric = false;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			JsonIOUtil.writeTo(out, message, RuntimeSchema.getSchema(klass), numeric);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Json encoding failed");
		}
		String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
		return json;
	}

	public static String getPackageName() {
		return packageName;
	}

	public static void setPackageName(String packageName) {
		DynamicMessageUtil.packageName = packageName;
	}
}