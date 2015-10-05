
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
public final class GuildMembers implements Externalizable, Message<GuildMembers>, Schema<GuildMembers>{



    public static Schema<GuildMembers> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GuildMembers getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GuildMembers DEFAULT_INSTANCE = new GuildMembers();

    			public String guildId;
	    
        			public int recordId;
	    
        			public String playerId;
	    
      
    public GuildMembers()
    {
        
    }


	

	    
    public Boolean hasGuildId()  {
        return guildId == null ? false : true;
    }
        
		public String getGuildId() {
		return guildId;
	}
	
	public GuildMembers setGuildId(String guildId) {
		this.guildId = guildId;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public int getRecordId() {
		return recordId;
	}
	
	public GuildMembers setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public GuildMembers setPlayerId(String playerId) {
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

    public Schema<GuildMembers> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GuildMembers newMessage()
    {
        return new GuildMembers();
    }

    public Class<GuildMembers> typeClass()
    {
        return GuildMembers.class;
    }

    public String messageName()
    {
        return GuildMembers.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GuildMembers.class.getName();
    }

    public boolean isInitialized(GuildMembers message)
    {
        return true;
    }

    public void mergeFrom(Input input, GuildMembers message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.guildId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GuildMembers message) throws IOException
    {
    	    	
    	    	if(message.guildId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.guildId != null)
            output.writeString(1, message.guildId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(2, message.recordId, false);
    	    	
    	            	
    	    	if(message.playerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(3, message.playerId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "guildId";
        	        	case 2: return "recordId";
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
    	    	__fieldMap.put("guildId", 1);
    	    	__fieldMap.put("recordId", 2);
    	    	__fieldMap.put("playerId", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GuildMembers.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GuildMembers parseFrom(byte[] bytes) {
	GuildMembers message = new GuildMembers();
	ProtobufIOUtil.mergeFrom(bytes, message, GuildMembers.getSchema());
	return message;
}

public static GuildMembers parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GuildMembers message = new GuildMembers();
	JsonIOUtil.mergeFrom(bytes, message, GuildMembers.getSchema(), false);
	return message;
}

public GuildMembers clone() {
	byte[] bytes = this.toByteArray();
	GuildMembers guildMembers = GuildMembers.parseFrom(bytes);
	return guildMembers;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GuildMembers.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GuildMembers> schema = GuildMembers.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GuildMembers.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
