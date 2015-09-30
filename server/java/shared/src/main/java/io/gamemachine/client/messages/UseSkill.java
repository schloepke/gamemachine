
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
public final class UseSkill implements Externalizable, Message<UseSkill>, Schema<UseSkill>{



    public static Schema<UseSkill> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static UseSkill getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final UseSkill DEFAULT_INSTANCE = new UseSkill();

    			public String id;
	    
        			public String targetId;
	    
        			public GmVector3 location;
	    
        			public GmVector3 direction;
	    
      
    public UseSkill()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public UseSkill setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasTargetId()  {
        return targetId == null ? false : true;
    }
        
		public String getTargetId() {
		return targetId;
	}
	
	public UseSkill setTargetId(String targetId) {
		this.targetId = targetId;
		return this;	}
	
		    
    public Boolean hasLocation()  {
        return location == null ? false : true;
    }
        
		public GmVector3 getLocation() {
		return location;
	}
	
	public UseSkill setLocation(GmVector3 location) {
		this.location = location;
		return this;	}
	
		    
    public Boolean hasDirection()  {
        return direction == null ? false : true;
    }
        
		public GmVector3 getDirection() {
		return direction;
	}
	
	public UseSkill setDirection(GmVector3 direction) {
		this.direction = direction;
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

    public Schema<UseSkill> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public UseSkill newMessage()
    {
        return new UseSkill();
    }

    public Class<UseSkill> typeClass()
    {
        return UseSkill.class;
    }

    public String messageName()
    {
        return UseSkill.class.getSimpleName();
    }

    public String messageFullName()
    {
        return UseSkill.class.getName();
    }

    public boolean isInitialized(UseSkill message)
    {
        return true;
    }

    public void mergeFrom(Input input, UseSkill message) throws IOException
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
            	                	                	message.targetId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.location = input.mergeObject(message.location, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.direction = input.mergeObject(message.direction, GmVector3.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, UseSkill message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.targetId != null)
            output.writeString(2, message.targetId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.location != null)
    		output.writeObject(3, message.location, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.direction != null)
    		output.writeObject(4, message.direction, GmVector3.getSchema(), false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "targetId";
        	        	case 3: return "location";
        	        	case 4: return "direction";
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
    	    	__fieldMap.put("targetId", 2);
    	    	__fieldMap.put("location", 3);
    	    	__fieldMap.put("direction", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = UseSkill.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static UseSkill parseFrom(byte[] bytes) {
	UseSkill message = new UseSkill();
	ProtobufIOUtil.mergeFrom(bytes, message, UseSkill.getSchema());
	return message;
}

public static UseSkill parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	UseSkill message = new UseSkill();
	JsonIOUtil.mergeFrom(bytes, message, UseSkill.getSchema(), false);
	return message;
}

public UseSkill clone() {
	byte[] bytes = this.toByteArray();
	UseSkill useSkill = UseSkill.parseFrom(bytes);
	return useSkill;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, UseSkill.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<UseSkill> schema = UseSkill.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, UseSkill.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
