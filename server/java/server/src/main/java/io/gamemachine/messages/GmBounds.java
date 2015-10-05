
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
public final class GmBounds implements Externalizable, Message<GmBounds>, Schema<GmBounds>{



    public static Schema<GmBounds> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GmBounds getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GmBounds DEFAULT_INSTANCE = new GmBounds();
    static final String defaultScope = GmBounds.class.getSimpleName();

    	
	    	    public GmVector3 min;
	    		
    
        	
	    	    public GmVector3 max;
	    		
    
        


    public GmBounds()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    }
    
	public static GmBounds fromModel(Model model) {
		boolean hasFields = false;
    	GmBounds message = new GmBounds();
    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public GmVector3 getMin() {
		return min;
	}
	
	public GmBounds setMin(GmVector3 min) {
		this.min = min;
		return this;	}
	
		            
		public GmVector3 getMax() {
		return max;
	}
	
	public GmBounds setMax(GmVector3 max) {
		this.max = max;
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

    public Schema<GmBounds> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GmBounds newMessage()
    {
        return new GmBounds();
    }

    public Class<GmBounds> typeClass()
    {
        return GmBounds.class;
    }

    public String messageName()
    {
        return GmBounds.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GmBounds.class.getName();
    }

    public boolean isInitialized(GmBounds message)
    {
        return true;
    }

    public void mergeFrom(Input input, GmBounds message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.min = input.mergeObject(message.min, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.max = input.mergeObject(message.max, GmVector3.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GmBounds message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.min != null)
    		output.writeObject(1, message.min, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.max != null)
    		output.writeObject(2, message.max, GmVector3.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GmBounds");
    	    	//if(this.min != null) {
    		System.out.println("min="+this.min);
    	//}
    	    	//if(this.max != null) {
    		System.out.println("max="+this.max);
    	//}
    	    	System.out.println("END GmBounds");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "min";
        	        	case 2: return "max";
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
    	    	__fieldMap.put("min", 1);
    	    	__fieldMap.put("max", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GmBounds.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GmBounds parseFrom(byte[] bytes) {
	GmBounds message = new GmBounds();
	ProtobufIOUtil.mergeFrom(bytes, message, GmBounds.getSchema());
	return message;
}

public static GmBounds parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GmBounds message = new GmBounds();
	JsonIOUtil.mergeFrom(bytes, message, GmBounds.getSchema(), false);
	return message;
}

public GmBounds clone() {
	byte[] bytes = this.toByteArray();
	GmBounds gmBounds = GmBounds.parseFrom(bytes);
	return gmBounds;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GmBounds.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GmBounds> schema = GmBounds.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GmBounds.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GmBounds.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
