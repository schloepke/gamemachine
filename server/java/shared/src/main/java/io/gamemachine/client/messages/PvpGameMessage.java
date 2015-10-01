
package io.gamemachine.client.messages;

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


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class PvpGameMessage implements Externalizable, Message<PvpGameMessage>, Schema<PvpGameMessage>{



    public static Schema<PvpGameMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PvpGameMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PvpGameMessage DEFAULT_INSTANCE = new PvpGameMessage();

    			public Character character;
	    
        			public Characters characters;
	    
        			public Integer command;
	    
      
    public PvpGameMessage()
    {
        
    }


	

	    
    public Boolean hasCharacter()  {
        return character == null ? false : true;
    }
        
		public Character getCharacter() {
		return character;
	}
	
	public PvpGameMessage setCharacter(Character character) {
		this.character = character;
		return this;	}
	
		    
    public Boolean hasCharacters()  {
        return characters == null ? false : true;
    }
        
		public Characters getCharacters() {
		return characters;
	}
	
	public PvpGameMessage setCharacters(Characters characters) {
		this.characters = characters;
		return this;	}
	
		    
    public Boolean hasCommand()  {
        return command == null ? false : true;
    }
        
		public Integer getCommand() {
		return command;
	}
	
	public PvpGameMessage setCommand(Integer command) {
		this.command = command;
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

    public Schema<PvpGameMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PvpGameMessage newMessage()
    {
        return new PvpGameMessage();
    }

    public Class<PvpGameMessage> typeClass()
    {
        return PvpGameMessage.class;
    }

    public String messageName()
    {
        return PvpGameMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PvpGameMessage.class.getName();
    }

    public boolean isInitialized(PvpGameMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, PvpGameMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.character = input.mergeObject(message.character, Character.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.characters = input.mergeObject(message.characters, Characters.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.command = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PvpGameMessage message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.character != null)
    		output.writeObject(1, message.character, Character.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.characters != null)
    		output.writeObject(2, message.characters, Characters.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.command != null)
            output.writeInt32(3, message.command, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "character";
        	        	case 2: return "characters";
        	        	case 3: return "command";
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
    	    	__fieldMap.put("character", 1);
    	    	__fieldMap.put("characters", 2);
    	    	__fieldMap.put("command", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PvpGameMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PvpGameMessage parseFrom(byte[] bytes) {
	PvpGameMessage message = new PvpGameMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, PvpGameMessage.getSchema());
	return message;
}

public static PvpGameMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PvpGameMessage message = new PvpGameMessage();
	JsonIOUtil.mergeFrom(bytes, message, PvpGameMessage.getSchema(), false);
	return message;
}

public PvpGameMessage clone() {
	byte[] bytes = this.toByteArray();
	PvpGameMessage pvpGameMessage = PvpGameMessage.parseFrom(bytes);
	return pvpGameMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PvpGameMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PvpGameMessage> schema = PvpGameMessage.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PvpGameMessage.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
