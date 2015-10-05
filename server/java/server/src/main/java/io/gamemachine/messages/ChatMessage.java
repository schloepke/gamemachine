
package io.gamemachine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;





import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class ChatMessage implements Externalizable, Message<ChatMessage>, Schema<ChatMessage>{



    public static Schema<ChatMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatMessage DEFAULT_INSTANCE = new ChatMessage();
    static final String defaultScope = ChatMessage.class.getSimpleName();

    	
	    	    public ChatChannel chatChannel;
	    		
    
        	
	    	    public String message= null;
	    		
    
        	
	    	    public String type= null;
	    		
    
        	
	    	    public String senderId= null;
	    		
    
        	
	    	    public Entity entity;
	    		
    
        	
	    	    public DynamicMessage dynamicMessage;
	    		
    
        


    public ChatMessage()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	    	model.set("chat_message_message",null);
    	    	    	    	    	    	model.set("chat_message_type",null);
    	    	    	    	    	    	model.set("chat_message_sender_id",null);
    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	
    	    	    	//if (message != null) {
    	       	    	model.setString("chat_message_message",message);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (type != null) {
    	       	    	model.setString("chat_message_type",type);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (senderId != null) {
    	       	    	model.setString("chat_message_sender_id",senderId);
    	        		
    	//}
    	    	    	    	    }
    
	public static ChatMessage fromModel(Model model) {
		boolean hasFields = false;
    	ChatMessage message = new ChatMessage();
    	    	    	    	    	    	
    	    	    	String messageTestField = model.getString("chat_message_message");
    	if (messageTestField != null) {
    		String messageField = messageTestField;
    		message.setMessage(messageField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String typeTestField = model.getString("chat_message_type");
    	if (typeTestField != null) {
    		String typeField = typeTestField;
    		message.setType(typeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String senderIdTestField = model.getString("chat_message_sender_id");
    	if (senderIdTestField != null) {
    		String senderIdField = senderIdTestField;
    		message.setSenderId(senderIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public ChatChannel getChatChannel() {
		return chatChannel;
	}
	
	public ChatMessage setChatChannel(ChatChannel chatChannel) {
		this.chatChannel = chatChannel;
		return this;	}
	
		            
		public String getMessage() {
		return message;
	}
	
	public ChatMessage setMessage(String message) {
		this.message = message;
		return this;	}
	
		            
		public String getType() {
		return type;
	}
	
	public ChatMessage setType(String type) {
		this.type = type;
		return this;	}
	
		            
		public String getSenderId() {
		return senderId;
	}
	
	public ChatMessage setSenderId(String senderId) {
		this.senderId = senderId;
		return this;	}
	
		            
		public Entity getEntity() {
		return entity;
	}
	
	public ChatMessage setEntity(Entity entity) {
		this.entity = entity;
		return this;	}
	
		            
		public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public ChatMessage setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
		return this;	}
	
	
  
    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<ChatMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatMessage newMessage()
    {
        return new ChatMessage();
    }

    public Class<ChatMessage> typeClass()
    {
        return ChatMessage.class;
    }

    public String messageName()
    {
        return ChatMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatMessage.class.getName();
    }

    public boolean isInitialized(ChatMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.chatChannel = input.mergeObject(message.chatChannel, ChatChannel.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.message = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.type = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.senderId = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.entity = input.mergeObject(message.entity, Entity.getSchema());
                    break;
                                    	
                            	            	case 6:
            	                	                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ChatMessage message) throws IOException
    {
    	    	
    	    	//if(message.chatChannel == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.chatChannel != null)
    		output.writeObject(1, message.chatChannel, ChatChannel.getSchema(), false);
    	    	
    	            	
    	    	//if(message.message == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.message != null) {
            output.writeString(2, message.message, false);
        }
    	    	
    	            	
    	    	//if(message.type == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.type != null) {
            output.writeString(3, message.type, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.senderId != null) {
            output.writeString(4, message.senderId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.entity != null)
    		output.writeObject(5, message.entity, Entity.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.dynamicMessage != null)
    		output.writeObject(6, message.dynamicMessage, DynamicMessage.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ChatMessage");
    	    	//if(this.chatChannel != null) {
    		System.out.println("chatChannel="+this.chatChannel);
    	//}
    	    	//if(this.message != null) {
    		System.out.println("message="+this.message);
    	//}
    	    	//if(this.type != null) {
    		System.out.println("type="+this.type);
    	//}
    	    	//if(this.senderId != null) {
    		System.out.println("senderId="+this.senderId);
    	//}
    	    	//if(this.entity != null) {
    		System.out.println("entity="+this.entity);
    	//}
    	    	//if(this.dynamicMessage != null) {
    		System.out.println("dynamicMessage="+this.dynamicMessage);
    	//}
    	    	System.out.println("END ChatMessage");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "chatChannel";
        	        	case 2: return "message";
        	        	case 3: return "type";
        	        	case 4: return "senderId";
        	        	case 5: return "entity";
        	        	case 6: return "dynamicMessage";
        	            default: return null;
        }
    }

    public int getFieldNumber(String name)
    {
        final Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    private static final java.util.HashMap<String,Integer> __fieldMap = new java.util.HashMap<String,Integer>();
    static
    {
    	    	__fieldMap.put("chatChannel", 1);
    	    	__fieldMap.put("message", 2);
    	    	__fieldMap.put("type", 3);
    	    	__fieldMap.put("senderId", 4);
    	    	__fieldMap.put("entity", 5);
    	    	__fieldMap.put("dynamicMessage", 6);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatMessage parseFrom(byte[] bytes) {
	ChatMessage message = new ChatMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, ChatMessage.getSchema());
	return message;
}

public static ChatMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ChatMessage message = new ChatMessage();
	JsonIOUtil.mergeFrom(bytes, message, ChatMessage.getSchema(), false);
	return message;
}

public ChatMessage clone() {
	byte[] bytes = this.toByteArray();
	ChatMessage chatMessage = ChatMessage.parseFrom(bytes);
	return chatMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatMessage> schema = ChatMessage.getSchema();
    
	final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ProtobufOutput output = new ProtobufOutput(buffer);
    try
    {
    	schema.writeTo(output, this);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        assert size == msgSize;
        
        buffer.clear();
        return out.toByteArray();
    }
    catch (IOException e)
    {
        throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                "(should never happen).", e);
    }
 
}

public byte[] toProtobuf() {
	LinkedBuffer buffer = LocalLinkedBuffer.get();
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, ChatMessage.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, ChatMessage.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
