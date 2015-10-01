
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
public final class GuildAction implements Externalizable, Message<GuildAction>, Schema<GuildAction>{



    public static Schema<GuildAction> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GuildAction getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GuildAction DEFAULT_INSTANCE = new GuildAction();

    			public String action;
	    
        			public String to;
	    
        			public String from;
	    
        			public String response;
	    
        			public String guildId;
	    
        			public Integer recordId;
	    
        			public String inviteId;
	    
        			public String guildName;
	    
      
    public GuildAction()
    {
        
    }


	

	    
    public Boolean hasAction()  {
        return action == null ? false : true;
    }
        
		public String getAction() {
		return action;
	}
	
	public GuildAction setAction(String action) {
		this.action = action;
		return this;	}
	
		    
    public Boolean hasTo()  {
        return to == null ? false : true;
    }
        
		public String getTo() {
		return to;
	}
	
	public GuildAction setTo(String to) {
		this.to = to;
		return this;	}
	
		    
    public Boolean hasFrom()  {
        return from == null ? false : true;
    }
        
		public String getFrom() {
		return from;
	}
	
	public GuildAction setFrom(String from) {
		this.from = from;
		return this;	}
	
		    
    public Boolean hasResponse()  {
        return response == null ? false : true;
    }
        
		public String getResponse() {
		return response;
	}
	
	public GuildAction setResponse(String response) {
		this.response = response;
		return this;	}
	
		    
    public Boolean hasGuildId()  {
        return guildId == null ? false : true;
    }
        
		public String getGuildId() {
		return guildId;
	}
	
	public GuildAction setGuildId(String guildId) {
		this.guildId = guildId;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public GuildAction setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasInviteId()  {
        return inviteId == null ? false : true;
    }
        
		public String getInviteId() {
		return inviteId;
	}
	
	public GuildAction setInviteId(String inviteId) {
		this.inviteId = inviteId;
		return this;	}
	
		    
    public Boolean hasGuildName()  {
        return guildName == null ? false : true;
    }
        
		public String getGuildName() {
		return guildName;
	}
	
	public GuildAction setGuildName(String guildName) {
		this.guildName = guildName;
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

    public Schema<GuildAction> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GuildAction newMessage()
    {
        return new GuildAction();
    }

    public Class<GuildAction> typeClass()
    {
        return GuildAction.class;
    }

    public String messageName()
    {
        return GuildAction.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GuildAction.class.getName();
    }

    public boolean isInitialized(GuildAction message)
    {
        return true;
    }

    public void mergeFrom(Input input, GuildAction message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.action = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.to = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.from = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.response = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.guildId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.inviteId = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.guildName = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GuildAction message) throws IOException
    {
    	    	
    	    	if(message.action == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.action != null)
            output.writeString(1, message.action, false);
    	    	
    	            	
    	    	
    	    	    	if(message.to != null)
            output.writeString(2, message.to, false);
    	    	
    	            	
    	    	
    	    	    	if(message.from != null)
            output.writeString(3, message.from, false);
    	    	
    	            	
    	    	
    	    	    	if(message.response != null)
            output.writeString(4, message.response, false);
    	    	
    	            	
    	    	
    	    	    	if(message.guildId != null)
            output.writeString(5, message.guildId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(6, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.inviteId != null)
            output.writeString(7, message.inviteId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.guildName != null)
            output.writeString(8, message.guildName, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "action";
        	        	case 2: return "to";
        	        	case 3: return "from";
        	        	case 4: return "response";
        	        	case 5: return "guildId";
        	        	case 6: return "recordId";
        	        	case 7: return "inviteId";
        	        	case 8: return "guildName";
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
    	    	__fieldMap.put("action", 1);
    	    	__fieldMap.put("to", 2);
    	    	__fieldMap.put("from", 3);
    	    	__fieldMap.put("response", 4);
    	    	__fieldMap.put("guildId", 5);
    	    	__fieldMap.put("recordId", 6);
    	    	__fieldMap.put("inviteId", 7);
    	    	__fieldMap.put("guildName", 8);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GuildAction.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GuildAction parseFrom(byte[] bytes) {
	GuildAction message = new GuildAction();
	ProtobufIOUtil.mergeFrom(bytes, message, GuildAction.getSchema());
	return message;
}

public static GuildAction parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GuildAction message = new GuildAction();
	JsonIOUtil.mergeFrom(bytes, message, GuildAction.getSchema(), false);
	return message;
}

public GuildAction clone() {
	byte[] bytes = this.toByteArray();
	GuildAction guildAction = GuildAction.parseFrom(bytes);
	return guildAction;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GuildAction.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GuildAction> schema = GuildAction.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GuildAction.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
