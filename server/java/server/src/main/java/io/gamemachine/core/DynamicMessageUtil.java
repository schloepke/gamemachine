package io.gamemachine.core;


import io.gamemachine.messages.DynamicMessage;
import io.gamemachine.util.LocalLinkedBuffer;
import io.protostuff.ByteString;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

public class DynamicMessageUtil {

	private static String packageName = "user.messages.";
	
//	public static <T> GameMessage toGameMessage(T message) {
//		DynamicMessage dynamicMessage = toDynamicMessage(message);
//		GameMessage gameMessage = new GameMessage();
//		gameMessage.setDynamicMessage(dynamicMessage);
//		return gameMessage;
//	}
	
	public static <T> DynamicMessage toDynamicMessage(T message) {
		DynamicMessage dynamicMessage = new DynamicMessage();
		byte[] bytes = serialize(message);
		dynamicMessage.setMessage(ByteString.copyFrom(bytes));
		dynamicMessage.setType(message.getClass().getSimpleName());
		return dynamicMessage;
	}
	
//	public static <T> T fromGameMessage(GameMessage gameMessage)  {
//		return deserialize(gameMessage.getDynamicMessage().getType(),gameMessage.getDynamicMessage().getMessage().toByteArray());
//	}
	
	public static <T> T fromDynamicMessage(DynamicMessage dynamicMessage)  {
		return deserialize(dynamicMessage.getType(),dynamicMessage.getMessage().toByteArray());
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String classname, byte[] bytes)  {
		try {
			Class<?> clazz = Class.forName(packageName + classname);
			return (T) deserialize(clazz,bytes);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid class name for message");
		}
	}
	
	public static <T> T deserialize(Class<T> klass, byte[] bytes)  {
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
	
	
	
	public static <T> byte[] serialize(T message) {
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) Class.forName(packageName + message.getClass().getSimpleName());
			return serialize(clazz,message);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid class name for message");
		}
	}
	
	public static <T> byte[] serialize(Class<T> klass,T message) {
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

	public static String getPackageName() {
		return packageName;
	}

	public static void setPackageName(String packageName) {
		DynamicMessageUtil.packageName = packageName;
	}
}