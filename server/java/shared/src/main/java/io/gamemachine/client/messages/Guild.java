
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
public final class Guild implements Externalizable, Message<Guild>, Schema<Guild>{



    public static Schema<Guild> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Guild getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Guild DEFAULT_INSTANCE = new Guild();

    			public String id;
	    
        			public String ownerId;
	    
        			public Integer recordId;
	    
        			public String name;
	    
      
    public Guild()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Guild setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasOwnerId()  {
        return ownerId == null ? false : true;
    }
        
		public String getOwnerId() {
		return ownerId;
	}
	
	public Guild setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public Guild setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public Guild setName(String name) {
		this.name = name;
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

    public Schema<Guild> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Guild newMessage()
    {
        return new Guild();
    }

    public Class<Guild> typeClass()
    {
        return Guild.class;
    }

    public String messageName()
    {
        return Guild.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Guild.class.getName();
    }

    public boolean isInitialized(Guild message)
    {
        return true;
    }

    public void mergeFrom(Input input, Guild message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.ownerId = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Guild message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	if(message.ownerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.ownerId != null)
            output.writeString(4, message.ownerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(5, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.name != null)
            output.writeString(6, message.name, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 4: return "ownerId";
        	        	case 5: return "recordId";
        	        	case 6: return "name";
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
    	    	__fieldMap.put("id", 1);
    	    	__fieldMap.put("ownerId", 4);
    	    	__fieldMap.put("recordId", 5);
    	    	__fieldMap.put("name", 6);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Guild.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Guild parseFrom(byte[] bytes) {
	Guild message = new Guild();
	ProtobufIOUtil.mergeFrom(bytes, message, Guild.getSchema());
	return message;
}

public static Guild parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Guild message = new Guild();
	JsonIOUtil.mergeFrom(bytes, message, Guild.getSchema(), false);
	return message;
}

public Guild clone() {
	byte[] bytes = this.toByteArray();
	Guild guild = Guild.parseFrom(bytes);
	return guild;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Guild.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Guild> schema = Guild.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Guild.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
