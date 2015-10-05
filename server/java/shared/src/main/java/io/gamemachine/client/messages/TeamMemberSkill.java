
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
public final class TeamMemberSkill implements Externalizable, Message<TeamMemberSkill>, Schema<TeamMemberSkill>{



    public static Schema<TeamMemberSkill> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TeamMemberSkill getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TeamMemberSkill DEFAULT_INSTANCE = new TeamMemberSkill();

    			public String name;
	    
        			public int value;
	    
        			public String playerId;
	    
      
    public TeamMemberSkill()
    {
        
    }


	

	    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public TeamMemberSkill setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasValue()  {
        return value == null ? false : true;
    }
        
		public int getValue() {
		return value;
	}
	
	public TeamMemberSkill setValue(int value) {
		this.value = value;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public TeamMemberSkill setPlayerId(String playerId) {
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

    public Schema<TeamMemberSkill> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TeamMemberSkill newMessage()
    {
        return new TeamMemberSkill();
    }

    public Class<TeamMemberSkill> typeClass()
    {
        return TeamMemberSkill.class;
    }

    public String messageName()
    {
        return TeamMemberSkill.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TeamMemberSkill.class.getName();
    }

    public boolean isInitialized(TeamMemberSkill message)
    {
        return true;
    }

    public void mergeFrom(Input input, TeamMemberSkill message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.value = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TeamMemberSkill message) throws IOException
    {
    	    	
    	    	if(message.name == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.name != null)
            output.writeString(1, message.name, false);
    	    	
    	            	
    	    	if(message.value == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.value != null)
            output.writeInt32(2, message.value, false);
    	    	
    	            	
    	    	if(message.playerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(3, message.playerId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "name";
        	        	case 2: return "value";
        	        	case 3: return "playerId";
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
    	    	__fieldMap.put("name", 1);
    	    	__fieldMap.put("value", 2);
    	    	__fieldMap.put("playerId", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TeamMemberSkill.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TeamMemberSkill parseFrom(byte[] bytes) {
	TeamMemberSkill message = new TeamMemberSkill();
	ProtobufIOUtil.mergeFrom(bytes, message, TeamMemberSkill.getSchema());
	return message;
}

public static TeamMemberSkill parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TeamMemberSkill message = new TeamMemberSkill();
	JsonIOUtil.mergeFrom(bytes, message, TeamMemberSkill.getSchema(), false);
	return message;
}

public TeamMemberSkill clone() {
	byte[] bytes = this.toByteArray();
	TeamMemberSkill teamMemberSkill = TeamMemberSkill.parseFrom(bytes);
	return teamMemberSkill;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TeamMemberSkill.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TeamMemberSkill> schema = TeamMemberSkill.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TeamMemberSkill.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
