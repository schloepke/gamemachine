
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
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class GridNode implements Externalizable, Message<GridNode>, Schema<GridNode>{

private static final Logger logger = LoggerFactory.getLogger(GridNode.class);



    public static Schema<GridNode> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GridNode getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GridNode DEFAULT_INSTANCE = new GridNode();
    static final String defaultScope = GridNode.class.getSimpleName();

    	
					public GmVector3 point = null;
			    
		
    
        	
							    public float slope= 0F;
		    			    
		
    
        


    public GridNode()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	    	model.set("grid_node_slope",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	
    	    	    	//if (slope != null) {
    	       	    	model.setFloat("grid_node_slope",slope);
    	        		
    	//}
    	    	    }
    
	public static GridNode fromModel(Model model) {
		boolean hasFields = false;
    	GridNode message = new GridNode();
    	    	    	    	    	    	
    	    			Float slopeTestField = model.getFloat("grid_node_slope");
		if (slopeTestField != null) {
			float slopeField = slopeTestField;
			message.setSlope(slopeField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public GmVector3 getPoint() {
		return point;
	}
	
	public GridNode setPoint(GmVector3 point) {
		this.point = point;
		return this;	}
	
		            
		public float getSlope() {
		return slope;
	}
	
	public GridNode setSlope(float slope) {
		this.slope = slope;
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

    public Schema<GridNode> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GridNode newMessage()
    {
        return new GridNode();
    }

    public Class<GridNode> typeClass()
    {
        return GridNode.class;
    }

    public String messageName()
    {
        return GridNode.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GridNode.class.getName();
    }

    public boolean isInitialized(GridNode message)
    {
        return true;
    }

    public void mergeFrom(Input input, GridNode message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.point = input.mergeObject(message.point, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.slope = input.readFloat();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GridNode message) throws IOException
    {
    	    	
    	    	//if(message.point == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.point != null)
    		output.writeObject(1, message.point, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (Float)message.slope != null) {
            output.writeFloat(2, message.slope, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GridNode");
    	    	//if(this.point != null) {
    		System.out.println("point="+this.point);
    	//}
    	    	//if(this.slope != null) {
    		System.out.println("slope="+this.slope);
    	//}
    	    	System.out.println("END GridNode");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "point";
        	        	case 2: return "slope";
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
    	    	__fieldMap.put("point", 1);
    	    	__fieldMap.put("slope", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GridNode.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GridNode parseFrom(byte[] bytes) {
	GridNode message = new GridNode();
	ProtobufIOUtil.mergeFrom(bytes, message, GridNode.getSchema());
	return message;
}

public static GridNode parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GridNode message = new GridNode();
	JsonIOUtil.mergeFrom(bytes, message, GridNode.getSchema(), false);
	return message;
}

public GridNode clone() {
	byte[] bytes = this.toByteArray();
	GridNode gridNode = GridNode.parseFrom(bytes);
	return gridNode;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GridNode.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GridNode> schema = GridNode.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GridNode.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, GridNode.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
