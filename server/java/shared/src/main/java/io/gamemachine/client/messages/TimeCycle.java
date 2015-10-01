
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
public final class TimeCycle implements Externalizable, Message<TimeCycle>, Schema<TimeCycle>{



    public static Schema<TimeCycle> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TimeCycle getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TimeCycle DEFAULT_INSTANCE = new TimeCycle();

    			public Float currentTime;
	    
        			public Float dayCycleLength;
	    
      
    public TimeCycle()
    {
        
    }


	

	    
    public Boolean hasCurrentTime()  {
        return currentTime == null ? false : true;
    }
        
		public Float getCurrentTime() {
		return currentTime;
	}
	
	public TimeCycle setCurrentTime(Float currentTime) {
		this.currentTime = currentTime;
		return this;	}
	
		    
    public Boolean hasDayCycleLength()  {
        return dayCycleLength == null ? false : true;
    }
        
		public Float getDayCycleLength() {
		return dayCycleLength;
	}
	
	public TimeCycle setDayCycleLength(Float dayCycleLength) {
		this.dayCycleLength = dayCycleLength;
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

    public Schema<TimeCycle> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TimeCycle newMessage()
    {
        return new TimeCycle();
    }

    public Class<TimeCycle> typeClass()
    {
        return TimeCycle.class;
    }

    public String messageName()
    {
        return TimeCycle.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TimeCycle.class.getName();
    }

    public boolean isInitialized(TimeCycle message)
    {
        return true;
    }

    public void mergeFrom(Input input, TimeCycle message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.currentTime = input.readFloat();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.dayCycleLength = input.readFloat();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TimeCycle message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.currentTime != null)
            output.writeFloat(1, message.currentTime, false);
    	    	
    	            	
    	    	
    	    	    	if(message.dayCycleLength != null)
            output.writeFloat(2, message.dayCycleLength, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "currentTime";
        	        	case 2: return "dayCycleLength";
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
    	    	__fieldMap.put("currentTime", 1);
    	    	__fieldMap.put("dayCycleLength", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TimeCycle.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TimeCycle parseFrom(byte[] bytes) {
	TimeCycle message = new TimeCycle();
	ProtobufIOUtil.mergeFrom(bytes, message, TimeCycle.getSchema());
	return message;
}

public static TimeCycle parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TimeCycle message = new TimeCycle();
	JsonIOUtil.mergeFrom(bytes, message, TimeCycle.getSchema(), false);
	return message;
}

public TimeCycle clone() {
	byte[] bytes = this.toByteArray();
	TimeCycle timeCycle = TimeCycle.parseFrom(bytes);
	return timeCycle;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TimeCycle.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TimeCycle> schema = TimeCycle.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TimeCycle.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
