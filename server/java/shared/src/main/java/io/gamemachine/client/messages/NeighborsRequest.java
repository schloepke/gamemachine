
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
public final class NeighborsRequest implements Externalizable, Message<NeighborsRequest>, Schema<NeighborsRequest>{



    public static Schema<NeighborsRequest> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static NeighborsRequest getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final NeighborsRequest DEFAULT_INSTANCE = new NeighborsRequest();

    			public String neighborEntityType;
	    
        			public String gridName;
	    
      
    public NeighborsRequest()
    {
        
    }


	

	    
    public Boolean hasNeighborEntityType()  {
        return neighborEntityType == null ? false : true;
    }
        
		public String getNeighborEntityType() {
		return neighborEntityType;
	}
	
	public NeighborsRequest setNeighborEntityType(String neighborEntityType) {
		this.neighborEntityType = neighborEntityType;
		return this;	}
	
		    
    public Boolean hasGridName()  {
        return gridName == null ? false : true;
    }
        
		public String getGridName() {
		return gridName;
	}
	
	public NeighborsRequest setGridName(String gridName) {
		this.gridName = gridName;
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

    public Schema<NeighborsRequest> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public NeighborsRequest newMessage()
    {
        return new NeighborsRequest();
    }

    public Class<NeighborsRequest> typeClass()
    {
        return NeighborsRequest.class;
    }

    public String messageName()
    {
        return NeighborsRequest.class.getSimpleName();
    }

    public String messageFullName()
    {
        return NeighborsRequest.class.getName();
    }

    public boolean isInitialized(NeighborsRequest message)
    {
        return true;
    }

    public void mergeFrom(Input input, NeighborsRequest message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.neighborEntityType = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.gridName = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, NeighborsRequest message) throws IOException
    {
    	    	
    	    	if(message.neighborEntityType == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.neighborEntityType != null)
            output.writeString(1, message.neighborEntityType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gridName != null)
            output.writeString(2, message.gridName, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "neighborEntityType";
        	        	case 2: return "gridName";
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
    	    	__fieldMap.put("neighborEntityType", 1);
    	    	__fieldMap.put("gridName", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = NeighborsRequest.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static NeighborsRequest parseFrom(byte[] bytes) {
	NeighborsRequest message = new NeighborsRequest();
	ProtobufIOUtil.mergeFrom(bytes, message, NeighborsRequest.getSchema());
	return message;
}

public static NeighborsRequest parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	NeighborsRequest message = new NeighborsRequest();
	JsonIOUtil.mergeFrom(bytes, message, NeighborsRequest.getSchema(), false);
	return message;
}

public NeighborsRequest clone() {
	byte[] bytes = this.toByteArray();
	NeighborsRequest neighborsRequest = NeighborsRequest.parseFrom(bytes);
	return neighborsRequest;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, NeighborsRequest.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<NeighborsRequest> schema = NeighborsRequest.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, NeighborsRequest.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
