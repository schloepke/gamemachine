
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
public final class PlayerNotification implements Externalizable, Message<PlayerNotification>, Schema<PlayerNotification>{



    public static Schema<PlayerNotification> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerNotification getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerNotification DEFAULT_INSTANCE = new PlayerNotification();
    static final String defaultScope = PlayerNotification.class.getSimpleName();

    	
	    	    public String playerId= null;
	    		
    
        	
	    	    public String action= null;
	    		
    
        


    public PlayerNotification()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("player_notification_player_id",null);
    	    	    	    	    	    	model.set("player_notification_action",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("player_notification_player_id",playerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (action != null) {
    	       	    	model.setString("player_notification_action",action);
    	        		
    	//}
    	    	    }
    
	public static PlayerNotification fromModel(Model model) {
		boolean hasFields = false;
    	PlayerNotification message = new PlayerNotification();
    	    	    	    	    	
    	    	    	String playerIdTestField = model.getString("player_notification_player_id");
    	if (playerIdTestField != null) {
    		String playerIdField = playerIdTestField;
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String actionTestField = model.getString("player_notification_action");
    	if (actionTestField != null) {
    		String actionField = actionTestField;
    		message.setAction(actionField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getPlayerId() {
		return playerId;
	}
	
	public PlayerNotification setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		            
		public String getAction() {
		return action;
	}
	
	public PlayerNotification setAction(String action) {
		this.action = action;
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

    public Schema<PlayerNotification> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerNotification newMessage()
    {
        return new PlayerNotification();
    }

    public Class<PlayerNotification> typeClass()
    {
        return PlayerNotification.class;
    }

    public String messageName()
    {
        return PlayerNotification.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerNotification.class.getName();
    }

    public boolean isInitialized(PlayerNotification message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerNotification message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.action = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerNotification message) throws IOException
    {
    	    	
    	    	//if(message.playerId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(1, message.playerId, false);
        }
    	    	
    	            	
    	    	//if(message.action == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.action != null) {
            output.writeString(2, message.action, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START PlayerNotification");
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	//if(this.action != null) {
    		System.out.println("action="+this.action);
    	//}
    	    	System.out.println("END PlayerNotification");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerId";
        	        	case 2: return "action";
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
    	    	__fieldMap.put("playerId", 1);
    	    	__fieldMap.put("action", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerNotification.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerNotification parseFrom(byte[] bytes) {
	PlayerNotification message = new PlayerNotification();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerNotification.getSchema());
	return message;
}

public static PlayerNotification parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerNotification message = new PlayerNotification();
	JsonIOUtil.mergeFrom(bytes, message, PlayerNotification.getSchema(), false);
	return message;
}

public PlayerNotification clone() {
	byte[] bytes = this.toByteArray();
	PlayerNotification playerNotification = PlayerNotification.parseFrom(bytes);
	return playerNotification;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerNotification.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerNotification> schema = PlayerNotification.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerNotification.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, PlayerNotification.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
