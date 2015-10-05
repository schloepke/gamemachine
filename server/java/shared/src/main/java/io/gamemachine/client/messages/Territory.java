
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
public final class Territory implements Externalizable, Message<Territory>, Schema<Territory>{



    public static Schema<Territory> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Territory getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Territory DEFAULT_INSTANCE = new Territory();

    			public String id;
	    
        			public String owner;
	    
        			public int recordId;
	    
        			public String keep;
	    
      
    public Territory()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Territory setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasOwner()  {
        return owner == null ? false : true;
    }
        
		public String getOwner() {
		return owner;
	}
	
	public Territory setOwner(String owner) {
		this.owner = owner;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public int getRecordId() {
		return recordId;
	}
	
	public Territory setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasKeep()  {
        return keep == null ? false : true;
    }
        
		public String getKeep() {
		return keep;
	}
	
	public Territory setKeep(String keep) {
		this.keep = keep;
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

    public Schema<Territory> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Territory newMessage()
    {
        return new Territory();
    }

    public Class<Territory> typeClass()
    {
        return Territory.class;
    }

    public String messageName()
    {
        return Territory.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Territory.class.getName();
    }

    public boolean isInitialized(Territory message)
    {
        return true;
    }

    public void mergeFrom(Input input, Territory message) throws IOException
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
                	                	
                            	            	case 2:
            	                	                	message.owner = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.keep = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Territory message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	if(message.owner == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.owner != null)
            output.writeString(2, message.owner, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(3, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.keep != null)
            output.writeString(4, message.keep, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "owner";
        	        	case 3: return "recordId";
        	        	case 4: return "keep";
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
    	    	__fieldMap.put("owner", 2);
    	    	__fieldMap.put("recordId", 3);
    	    	__fieldMap.put("keep", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Territory.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Territory parseFrom(byte[] bytes) {
	Territory message = new Territory();
	ProtobufIOUtil.mergeFrom(bytes, message, Territory.getSchema());
	return message;
}

public static Territory parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Territory message = new Territory();
	JsonIOUtil.mergeFrom(bytes, message, Territory.getSchema(), false);
	return message;
}

public Territory clone() {
	byte[] bytes = this.toByteArray();
	Territory territory = Territory.parseFrom(bytes);
	return territory;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Territory.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Territory> schema = Territory.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Territory.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
