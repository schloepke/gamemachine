
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
public final class TrackDataResponse implements Externalizable, Message<TrackDataResponse>, Schema<TrackDataResponse>{

	public enum REASON implements io.protostuff.EnumLite<REASON>
    {
    	
    	    	RESEND(0),    	    	VALIDATION_FAILED(1),    	    	REMOVED(2);    	        
        public final int number;
        
        private REASON (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static REASON valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (RESEND);
    			    			case 1: return (VALIDATION_FAILED);
    			    			case 2: return (REMOVED);
    			                default: return null;
            }
        }
    }


    public static Schema<TrackDataResponse> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackDataResponse getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackDataResponse DEFAULT_INSTANCE = new TrackDataResponse();

    			public String id;
	    
        			public REASON reason; // = RESEND:0;
	    
      
    public TrackDataResponse()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public TrackDataResponse setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasReason()  {
        return reason == null ? false : true;
    }
        
		public REASON getReason() {
		return reason;
	}
	
	public TrackDataResponse setReason(REASON reason) {
		this.reason = reason;
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

    public Schema<TrackDataResponse> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackDataResponse newMessage()
    {
        return new TrackDataResponse();
    }

    public Class<TrackDataResponse> typeClass()
    {
        return TrackDataResponse.class;
    }

    public String messageName()
    {
        return TrackDataResponse.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackDataResponse.class.getName();
    }

    public boolean isInitialized(TrackDataResponse message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackDataResponse message) throws IOException
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
            	                	                    message.reason = REASON.valueOf(input.readEnum());
                    break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackDataResponse message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.reason != null)
    	 	output.writeEnum(2, message.reason.number, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "reason";
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
    	    	__fieldMap.put("reason", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackDataResponse.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackDataResponse parseFrom(byte[] bytes) {
	TrackDataResponse message = new TrackDataResponse();
	ProtobufIOUtil.mergeFrom(bytes, message, TrackDataResponse.getSchema());
	return message;
}

public static TrackDataResponse parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TrackDataResponse message = new TrackDataResponse();
	JsonIOUtil.mergeFrom(bytes, message, TrackDataResponse.getSchema(), false);
	return message;
}

public TrackDataResponse clone() {
	byte[] bytes = this.toByteArray();
	TrackDataResponse trackDataResponse = TrackDataResponse.parseFrom(bytes);
	return trackDataResponse;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackDataResponse.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TrackDataResponse> schema = TrackDataResponse.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TrackDataResponse.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
