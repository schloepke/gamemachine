
package io.gamemachine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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





import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



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
    static final String defaultScope = NeighborsRequest.class.getSimpleName();

    			public String neighborEntityType;
	    
        			public String gridName;
	    
        


    public NeighborsRequest()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("neighbors_request_neighbor_entity_type",null);
    	    	    	    	    	    	model.set("neighbors_request_grid_name",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (neighborEntityType != null) {
    	       	    	model.setString("neighbors_request_neighbor_entity_type",neighborEntityType);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (gridName != null) {
    	       	    	model.setString("neighbors_request_grid_name",gridName);
    	        		
    	}
    	    	    }
    
	public static NeighborsRequest fromModel(Model model) {
		boolean hasFields = false;
    	NeighborsRequest message = new NeighborsRequest();
    	    	    	    	    	
    	    	    	String neighborEntityTypeField = model.getString("neighbors_request_neighbor_entity_type");
    	    	
    	if (neighborEntityTypeField != null) {
    		message.setNeighborEntityType(neighborEntityTypeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String gridNameField = model.getString("neighbors_request_grid_name");
    	    	
    	if (gridNameField != null) {
    		message.setGridName(gridNameField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
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

	public void dumpObject()
    {
    	System.out.println("START NeighborsRequest");
    	    	if(this.neighborEntityType != null) {
    		System.out.println("neighborEntityType="+this.neighborEntityType);
    	}
    	    	if(this.gridName != null) {
    		System.out.println("gridName="+this.gridName);
    	}
    	    	System.out.println("END NeighborsRequest");
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

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, NeighborsRequest.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
