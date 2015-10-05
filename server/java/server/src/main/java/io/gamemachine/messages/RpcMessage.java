
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
public final class RpcMessage implements Externalizable, Message<RpcMessage>, Schema<RpcMessage>{

	public enum MessageType implements io.protostuff.EnumLite<MessageType>
    {
    	
    	    	NONE(0),    	    	TEST(1);    	        
        public final int number;
        
        private MessageType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static MessageType defaultValue() {
        	return (NONE);
        }
        
        public static MessageType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (TEST);
    			                default: return null;
            }
        }
    }


    public static Schema<RpcMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static RpcMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final RpcMessage DEFAULT_INSTANCE = new RpcMessage();
    static final String defaultScope = RpcMessage.class.getSimpleName();

    	
					public MessageType messageType = MessageType.defaultValue();
			    
		
    
        	
					public GameMessage gameMessage = null;
			    
		
    
        	
							    public long messageId= 0L;
		    			    
		
    
        	
							    public String playerId= null;
		    			    
		
    
        


    public RpcMessage()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	    	    	model.set("rpc_message_message_id",null);
    	    	    	    	    	    	model.set("rpc_message_player_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	    	
    	    	    	//if (messageId != null) {
    	       	    	model.setLong("rpc_message_message_id",messageId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("rpc_message_player_id",playerId);
    	        		
    	//}
    	    	    }
    
	public static RpcMessage fromModel(Model model) {
		boolean hasFields = false;
    	RpcMessage message = new RpcMessage();
    	    	    	    	    	    	    	
    	    	    	Long messageIdTestField = model.getLong("rpc_message_message_id");
    	if (messageIdTestField != null) {
    		long messageIdField = messageIdTestField;
    		message.setMessageId(messageIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String playerIdTestField = model.getString("rpc_message_player_id");
    	if (playerIdTestField != null) {
    		String playerIdField = playerIdTestField;
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public MessageType getMessageType() {
		return messageType;
	}
	
	public RpcMessage setMessageType(MessageType messageType) {
		this.messageType = messageType;
		return this;	}
	
		            
		public GameMessage getGameMessage() {
		return gameMessage;
	}
	
	public RpcMessage setGameMessage(GameMessage gameMessage) {
		this.gameMessage = gameMessage;
		return this;	}
	
		            
		public long getMessageId() {
		return messageId;
	}
	
	public RpcMessage setMessageId(long messageId) {
		this.messageId = messageId;
		return this;	}
	
		            
		public String getPlayerId() {
		return playerId;
	}
	
	public RpcMessage setPlayerId(String playerId) {
		this.playerId = playerId;
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

    public Schema<RpcMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public RpcMessage newMessage()
    {
        return new RpcMessage();
    }

    public Class<RpcMessage> typeClass()
    {
        return RpcMessage.class;
    }

    public String messageName()
    {
        return RpcMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return RpcMessage.class.getName();
    }

    public boolean isInitialized(RpcMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, RpcMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                    message.messageType = MessageType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 2:
            	                	                	message.gameMessage = input.mergeObject(message.gameMessage, GameMessage.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.messageId = input.readInt64();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, RpcMessage message) throws IOException
    {
    	    	
    	    	//if(message.messageType == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.messageType != null)
    	 	output.writeEnum(1, message.messageType.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gameMessage != null)
    		output.writeObject(2, message.gameMessage, GameMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.messageId != null) {
            output.writeInt64(3, message.messageId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(4, message.playerId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START RpcMessage");
    	    	//if(this.messageType != null) {
    		System.out.println("messageType="+this.messageType);
    	//}
    	    	//if(this.gameMessage != null) {
    		System.out.println("gameMessage="+this.gameMessage);
    	//}
    	    	//if(this.messageId != null) {
    		System.out.println("messageId="+this.messageId);
    	//}
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	System.out.println("END RpcMessage");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "messageType";
        	        	case 2: return "gameMessage";
        	        	case 3: return "messageId";
        	        	case 4: return "playerId";
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
    	    	__fieldMap.put("messageType", 1);
    	    	__fieldMap.put("gameMessage", 2);
    	    	__fieldMap.put("messageId", 3);
    	    	__fieldMap.put("playerId", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = RpcMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static RpcMessage parseFrom(byte[] bytes) {
	RpcMessage message = new RpcMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, RpcMessage.getSchema());
	return message;
}

public static RpcMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	RpcMessage message = new RpcMessage();
	JsonIOUtil.mergeFrom(bytes, message, RpcMessage.getSchema(), false);
	return message;
}

public RpcMessage clone() {
	byte[] bytes = this.toByteArray();
	RpcMessage rpcMessage = RpcMessage.parseFrom(bytes);
	return rpcMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, RpcMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<RpcMessage> schema = RpcMessage.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RpcMessage.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RpcMessage.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
