
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

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class DynamicMessage implements Externalizable, Message<DynamicMessage>, Schema<DynamicMessage>{



    public static Schema<DynamicMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static DynamicMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final DynamicMessage DEFAULT_INSTANCE = new DynamicMessage();

    			public String type;
	    
        			public ByteString message;
	    
      
    public DynamicMessage()
    {
        
    }


	

	    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public String getType() {
		return type;
	}
	
	public DynamicMessage setType(String type) {
		this.type = type;
		return this;	}
	
		    
    public Boolean hasMessage()  {
        return message == null ? false : true;
    }
        
		public ByteString getMessage() {
		return message;
	}
	
	public DynamicMessage setMessage(ByteString message) {
		this.message = message;
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

    public Schema<DynamicMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public DynamicMessage newMessage()
    {
        return new DynamicMessage();
    }

    public Class<DynamicMessage> typeClass()
    {
        return DynamicMessage.class;
    }

    public String messageName()
    {
        return DynamicMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return DynamicMessage.class.getName();
    }

    public boolean isInitialized(DynamicMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, DynamicMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.type = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.message = input.readBytes();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, DynamicMessage message) throws IOException
    {
    	    	
    	    	if(message.type == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.type != null)
            output.writeString(1, message.type, false);
    	    	
    	            	
    	    	if(message.message == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.message != null)
            output.writeBytes(2, message.message, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "type";
        	        	case 2: return "message";
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
    	    	__fieldMap.put("type", 1);
    	    	__fieldMap.put("message", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = DynamicMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static DynamicMessage parseFrom(byte[] bytes) {
	DynamicMessage message = new DynamicMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, DynamicMessage.getSchema());
	return message;
}

public static DynamicMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	DynamicMessage message = new DynamicMessage();
	JsonIOUtil.mergeFrom(bytes, message, DynamicMessage.getSchema(), false);
	return message;
}

public DynamicMessage clone() {
	byte[] bytes = this.toByteArray();
	DynamicMessage dynamicMessage = DynamicMessage.parseFrom(bytes);
	return dynamicMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, DynamicMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<DynamicMessage> schema = DynamicMessage.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, DynamicMessage.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
