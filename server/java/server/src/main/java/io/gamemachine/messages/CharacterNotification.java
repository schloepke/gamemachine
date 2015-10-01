
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
public final class CharacterNotification implements Externalizable, Message<CharacterNotification>, Schema<CharacterNotification>{



    public static Schema<CharacterNotification> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CharacterNotification getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CharacterNotification DEFAULT_INSTANCE = new CharacterNotification();
    static final String defaultScope = CharacterNotification.class.getSimpleName();

    			public String playerId;
	    
        			public String action;
	    
        			public String characterId;
	    
        


    public CharacterNotification()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("character_notification_player_id",null);
    	    	    	    	    	    	model.set("character_notification_action",null);
    	    	    	    	    	    	model.set("character_notification_character_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (playerId != null) {
    	       	    	model.setString("character_notification_player_id",playerId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (action != null) {
    	       	    	model.setString("character_notification_action",action);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (characterId != null) {
    	       	    	model.setString("character_notification_character_id",characterId);
    	        		
    	}
    	    	    }
    
	public static CharacterNotification fromModel(Model model) {
		boolean hasFields = false;
    	CharacterNotification message = new CharacterNotification();
    	    	    	    	    	
    	    	    	String playerIdField = model.getString("character_notification_player_id");
    	    	
    	if (playerIdField != null) {
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String actionField = model.getString("character_notification_action");
    	    	
    	if (actionField != null) {
    		message.setAction(actionField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String characterIdField = model.getString("character_notification_character_id");
    	    	
    	if (characterIdField != null) {
    		message.setCharacterId(characterIdField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public CharacterNotification setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		    
    public Boolean hasAction()  {
        return action == null ? false : true;
    }
        
		public String getAction() {
		return action;
	}
	
	public CharacterNotification setAction(String action) {
		this.action = action;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public CharacterNotification setCharacterId(String characterId) {
		this.characterId = characterId;
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

    public Schema<CharacterNotification> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CharacterNotification newMessage()
    {
        return new CharacterNotification();
    }

    public Class<CharacterNotification> typeClass()
    {
        return CharacterNotification.class;
    }

    public String messageName()
    {
        return CharacterNotification.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CharacterNotification.class.getName();
    }

    public boolean isInitialized(CharacterNotification message)
    {
        return true;
    }

    public void mergeFrom(Input input, CharacterNotification message) throws IOException
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
                	                	
                            	            	case 3:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CharacterNotification message) throws IOException
    {
    	    	
    	    	if(message.playerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(1, message.playerId, false);
    	    	
    	            	
    	    	if(message.action == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.action != null)
            output.writeString(2, message.action, false);
    	    	
    	            	
    	    	if(message.characterId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(3, message.characterId, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START CharacterNotification");
    	    	if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	}
    	    	if(this.action != null) {
    		System.out.println("action="+this.action);
    	}
    	    	if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	}
    	    	System.out.println("END CharacterNotification");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerId";
        	        	case 2: return "action";
        	        	case 3: return "characterId";
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
    	    	__fieldMap.put("characterId", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CharacterNotification.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CharacterNotification parseFrom(byte[] bytes) {
	CharacterNotification message = new CharacterNotification();
	ProtobufIOUtil.mergeFrom(bytes, message, CharacterNotification.getSchema());
	return message;
}

public static CharacterNotification parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CharacterNotification message = new CharacterNotification();
	JsonIOUtil.mergeFrom(bytes, message, CharacterNotification.getSchema(), false);
	return message;
}

public CharacterNotification clone() {
	byte[] bytes = this.toByteArray();
	CharacterNotification characterNotification = CharacterNotification.parseFrom(bytes);
	return characterNotification;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CharacterNotification.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CharacterNotification> schema = CharacterNotification.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CharacterNotification.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, CharacterNotification.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
