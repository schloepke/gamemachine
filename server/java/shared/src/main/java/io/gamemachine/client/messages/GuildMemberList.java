
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
public final class GuildMemberList implements Externalizable, Message<GuildMemberList>, Schema<GuildMemberList>{



    public static Schema<GuildMemberList> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GuildMemberList getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GuildMemberList DEFAULT_INSTANCE = new GuildMemberList();

    			public String guildId;
	    
            public List<String> playerId;
	  
    public GuildMemberList()
    {
        
    }


	

	    
    public Boolean hasGuildId()  {
        return guildId == null ? false : true;
    }
        
		public String getGuildId() {
		return guildId;
	}
	
	public GuildMemberList setGuildId(String guildId) {
		this.guildId = guildId;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public List<String> getPlayerIdList() {
		if(this.playerId == null)
            this.playerId = new ArrayList<String>();
		return playerId;
	}

	public GuildMemberList setPlayerIdList(List<String> playerId) {
		this.playerId = playerId;
		return this;
	}

	public String getPlayerId(int index)  {
        return playerId == null ? null : playerId.get(index);
    }

    public int getPlayerIdCount()  {
        return playerId == null ? 0 : playerId.size();
    }

    public GuildMemberList addPlayerId(String playerId)  {
        if(this.playerId == null)
            this.playerId = new ArrayList<String>();
        this.playerId.add(playerId);
        return this;
    }
        	
    
    
    
	
  
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

    public Schema<GuildMemberList> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GuildMemberList newMessage()
    {
        return new GuildMemberList();
    }

    public Class<GuildMemberList> typeClass()
    {
        return GuildMemberList.class;
    }

    public String messageName()
    {
        return GuildMemberList.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GuildMemberList.class.getName();
    }

    public boolean isInitialized(GuildMemberList message)
    {
        return true;
    }

    public void mergeFrom(Input input, GuildMemberList message) throws IOException
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
            	            		if(message.playerId == null)
                        message.playerId = new ArrayList<String>();
                                    	message.playerId.add(input.readString());
                	                    break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GuildMemberList message) throws IOException
    {
    	    	
    	    	if(message.guildId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.guildId != null)
            output.writeString(1, message.guildId, false);
    	    	
    	            	
    	    	
    	    	if(message.playerId != null)
        {
            for(String playerId : message.playerId)
            {
                if(playerId != null) {
                   	            		output.writeString(2, playerId, true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "guildId";
        	        	case 2: return "playerId";
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
    	    	__fieldMap.put("playerId", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GuildMemberList.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GuildMemberList parseFrom(byte[] bytes) {
	GuildMemberList message = new GuildMemberList();
	ProtobufIOUtil.mergeFrom(bytes, message, GuildMemberList.getSchema());
	return message;
}

public static GuildMemberList parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GuildMemberList message = new GuildMemberList();
	JsonIOUtil.mergeFrom(bytes, message, GuildMemberList.getSchema(), false);
	return message;
}

public GuildMemberList clone() {
	byte[] bytes = this.toByteArray();
	GuildMemberList guildMemberList = GuildMemberList.parseFrom(bytes);
	return guildMemberList;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GuildMemberList.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GuildMemberList> schema = GuildMemberList.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GuildMemberList.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
