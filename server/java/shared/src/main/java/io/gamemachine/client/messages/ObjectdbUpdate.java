
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
public final class ObjectdbUpdate implements Externalizable, Message<ObjectdbUpdate>, Schema<ObjectdbUpdate>{



    public static Schema<ObjectdbUpdate> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbUpdate getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbUpdate DEFAULT_INSTANCE = new ObjectdbUpdate();

    			public String currentEntityId;
	    
        			public String updateClass;
	    
        			public String updateMethod;
	    
        			public Entity updateEntity;
	    
      
    public ObjectdbUpdate()
    {
        
    }


	

	    
    public Boolean hasCurrentEntityId()  {
        return currentEntityId == null ? false : true;
    }
        
		public String getCurrentEntityId() {
		return currentEntityId;
	}
	
	public ObjectdbUpdate setCurrentEntityId(String currentEntityId) {
		this.currentEntityId = currentEntityId;
		return this;	}
	
		    
    public Boolean hasUpdateClass()  {
        return updateClass == null ? false : true;
    }
        
		public String getUpdateClass() {
		return updateClass;
	}
	
	public ObjectdbUpdate setUpdateClass(String updateClass) {
		this.updateClass = updateClass;
		return this;	}
	
		    
    public Boolean hasUpdateMethod()  {
        return updateMethod == null ? false : true;
    }
        
		public String getUpdateMethod() {
		return updateMethod;
	}
	
	public ObjectdbUpdate setUpdateMethod(String updateMethod) {
		this.updateMethod = updateMethod;
		return this;	}
	
		    
    public Boolean hasUpdateEntity()  {
        return updateEntity == null ? false : true;
    }
        
		public Entity getUpdateEntity() {
		return updateEntity;
	}
	
	public ObjectdbUpdate setUpdateEntity(Entity updateEntity) {
		this.updateEntity = updateEntity;
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

    public Schema<ObjectdbUpdate> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbUpdate newMessage()
    {
        return new ObjectdbUpdate();
    }

    public Class<ObjectdbUpdate> typeClass()
    {
        return ObjectdbUpdate.class;
    }

    public String messageName()
    {
        return ObjectdbUpdate.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbUpdate.class.getName();
    }

    public boolean isInitialized(ObjectdbUpdate message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbUpdate message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.currentEntityId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.updateClass = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.updateMethod = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.updateEntity = input.mergeObject(message.updateEntity, Entity.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ObjectdbUpdate message) throws IOException
    {
    	    	
    	    	if(message.currentEntityId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.currentEntityId != null)
            output.writeString(1, message.currentEntityId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.updateClass != null)
            output.writeString(2, message.updateClass, false);
    	    	
    	            	
    	    	if(message.updateMethod == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.updateMethod != null)
            output.writeString(3, message.updateMethod, false);
    	    	
    	            	
    	    	if(message.updateEntity == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.updateEntity != null)
    		output.writeObject(4, message.updateEntity, Entity.getSchema(), false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "currentEntityId";
        	        	case 2: return "updateClass";
        	        	case 3: return "updateMethod";
        	        	case 4: return "updateEntity";
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
    	    	__fieldMap.put("currentEntityId", 1);
    	    	__fieldMap.put("updateClass", 2);
    	    	__fieldMap.put("updateMethod", 3);
    	    	__fieldMap.put("updateEntity", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbUpdate.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbUpdate parseFrom(byte[] bytes) {
	ObjectdbUpdate message = new ObjectdbUpdate();
	ProtobufIOUtil.mergeFrom(bytes, message, ObjectdbUpdate.getSchema());
	return message;
}

public static ObjectdbUpdate parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ObjectdbUpdate message = new ObjectdbUpdate();
	JsonIOUtil.mergeFrom(bytes, message, ObjectdbUpdate.getSchema(), false);
	return message;
}

public ObjectdbUpdate clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbUpdate objectdbUpdate = ObjectdbUpdate.parseFrom(bytes);
	return objectdbUpdate;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbUpdate.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ObjectdbUpdate> schema = ObjectdbUpdate.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ObjectdbUpdate.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
