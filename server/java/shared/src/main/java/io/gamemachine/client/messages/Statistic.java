
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
public final class Statistic implements Externalizable, Message<Statistic>, Schema<Statistic>{



    public static Schema<Statistic> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Statistic getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Statistic DEFAULT_INSTANCE = new Statistic();

    			public String name;
	    
        			public Float value;
	    
        			public String type;
	    
      
    public Statistic()
    {
        
    }


	

	    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public Statistic setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasValue()  {
        return value == null ? false : true;
    }
        
		public Float getValue() {
		return value;
	}
	
	public Statistic setValue(Float value) {
		this.value = value;
		return this;	}
	
		    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public String getType() {
		return type;
	}
	
	public Statistic setType(String type) {
		this.type = type;
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

    public Schema<Statistic> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Statistic newMessage()
    {
        return new Statistic();
    }

    public Class<Statistic> typeClass()
    {
        return Statistic.class;
    }

    public String messageName()
    {
        return Statistic.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Statistic.class.getName();
    }

    public boolean isInitialized(Statistic message)
    {
        return true;
    }

    public void mergeFrom(Input input, Statistic message) throws IOException
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
            	                	                	message.value = input.readFloat();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.type = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Statistic message) throws IOException
    {
    	    	
    	    	if(message.name == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.name != null)
            output.writeString(1, message.name, false);
    	    	
    	            	
    	    	if(message.value == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.value != null)
            output.writeFloat(2, message.value, false);
    	    	
    	            	
    	    	if(message.type == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.type != null)
            output.writeString(3, message.type, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "name";
        	        	case 2: return "value";
        	        	case 3: return "type";
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
    	    	__fieldMap.put("type", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Statistic.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Statistic parseFrom(byte[] bytes) {
	Statistic message = new Statistic();
	ProtobufIOUtil.mergeFrom(bytes, message, Statistic.getSchema());
	return message;
}

public static Statistic parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Statistic message = new Statistic();
	JsonIOUtil.mergeFrom(bytes, message, Statistic.getSchema(), false);
	return message;
}

public Statistic clone() {
	byte[] bytes = this.toByteArray();
	Statistic statistic = Statistic.parseFrom(bytes);
	return statistic;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Statistic.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Statistic> schema = Statistic.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Statistic.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
