
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
public final class ObjectdbGetResponse implements Externalizable, Message<ObjectdbGetResponse>, Schema<ObjectdbGetResponse>{



    public static Schema<ObjectdbGetResponse> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbGetResponse getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbGetResponse DEFAULT_INSTANCE = new ObjectdbGetResponse();

    			public boolean entityFound;
	    
      
    public ObjectdbGetResponse()
    {
        
    }


	

	    
    public Boolean hasEntityFound()  {
        return entityFound == null ? false : true;
    }
        
		public boolean getEntityFound() {
		return entityFound;
	}
	
	public ObjectdbGetResponse setEntityFound(boolean entityFound) {
		this.entityFound = entityFound;
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

    public Schema<ObjectdbGetResponse> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbGetResponse newMessage()
    {
        return new ObjectdbGetResponse();
    }

    public Class<ObjectdbGetResponse> typeClass()
    {
        return ObjectdbGetResponse.class;
    }

    public String messageName()
    {
        return ObjectdbGetResponse.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbGetResponse.class.getName();
    }

    public boolean isInitialized(ObjectdbGetResponse message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbGetResponse message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.entityFound = input.readBool();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ObjectdbGetResponse message) throws IOException
    {
    	    	
    	    	if(message.entityFound == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.entityFound != null)
            output.writeBool(1, message.entityFound, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "entityFound";
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
    	    	__fieldMap.put("entityFound", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbGetResponse.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbGetResponse parseFrom(byte[] bytes) {
	ObjectdbGetResponse message = new ObjectdbGetResponse();
	ProtobufIOUtil.mergeFrom(bytes, message, ObjectdbGetResponse.getSchema());
	return message;
}

public static ObjectdbGetResponse parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ObjectdbGetResponse message = new ObjectdbGetResponse();
	JsonIOUtil.mergeFrom(bytes, message, ObjectdbGetResponse.getSchema(), false);
	return message;
}

public ObjectdbGetResponse clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbGetResponse objectdbGetResponse = ObjectdbGetResponse.parseFrom(bytes);
	return objectdbGetResponse;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbGetResponse.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ObjectdbGetResponse> schema = ObjectdbGetResponse.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ObjectdbGetResponse.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
