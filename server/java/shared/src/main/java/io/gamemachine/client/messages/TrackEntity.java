
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
public final class TrackEntity implements Externalizable, Message<TrackEntity>, Schema<TrackEntity>{



    public static Schema<TrackEntity> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackEntity getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackEntity DEFAULT_INSTANCE = new TrackEntity();

    			public Boolean value;
	    
        			public Boolean internal;
	    
        			public TrackData trackData;
	    
      
    public TrackEntity()
    {
        
    }


	

	    
    public Boolean hasValue()  {
        return value == null ? false : true;
    }
        
		public Boolean getValue() {
		return value;
	}
	
	public TrackEntity setValue(Boolean value) {
		this.value = value;
		return this;	}
	
		    
    public Boolean hasInternal()  {
        return internal == null ? false : true;
    }
        
		public Boolean getInternal() {
		return internal;
	}
	
	public TrackEntity setInternal(Boolean internal) {
		this.internal = internal;
		return this;	}
	
		    
    public Boolean hasTrackData()  {
        return trackData == null ? false : true;
    }
        
		public TrackData getTrackData() {
		return trackData;
	}
	
	public TrackEntity setTrackData(TrackData trackData) {
		this.trackData = trackData;
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

    public Schema<TrackEntity> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackEntity newMessage()
    {
        return new TrackEntity();
    }

    public Class<TrackEntity> typeClass()
    {
        return TrackEntity.class;
    }

    public String messageName()
    {
        return TrackEntity.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackEntity.class.getName();
    }

    public boolean isInitialized(TrackEntity message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackEntity message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.value = input.readBool();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.internal = input.readBool();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.trackData = input.mergeObject(message.trackData, TrackData.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackEntity message) throws IOException
    {
    	    	
    	    	if(message.value == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.value != null)
            output.writeBool(1, message.value, false);
    	    	
    	            	
    	    	
    	    	    	if(message.internal != null)
            output.writeBool(2, message.internal, false);
    	    	
    	            	
    	    	
    	    	    	if(message.trackData != null)
    		output.writeObject(3, message.trackData, TrackData.getSchema(), false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "value";
        	        	case 2: return "internal";
        	        	case 3: return "trackData";
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
    	    	__fieldMap.put("value", 1);
    	    	__fieldMap.put("internal", 2);
    	    	__fieldMap.put("trackData", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackEntity.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackEntity parseFrom(byte[] bytes) {
	TrackEntity message = new TrackEntity();
	ProtobufIOUtil.mergeFrom(bytes, message, TrackEntity.getSchema());
	return message;
}

public static TrackEntity parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TrackEntity message = new TrackEntity();
	JsonIOUtil.mergeFrom(bytes, message, TrackEntity.getSchema(), false);
	return message;
}

public TrackEntity clone() {
	byte[] bytes = this.toByteArray();
	TrackEntity trackEntity = TrackEntity.parseFrom(bytes);
	return trackEntity;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackEntity.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TrackEntity> schema = TrackEntity.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TrackEntity.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
