
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
public final class GmQuaternion implements Externalizable, Message<GmQuaternion>, Schema<GmQuaternion>{



    public static Schema<GmQuaternion> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GmQuaternion getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GmQuaternion DEFAULT_INSTANCE = new GmQuaternion();
    static final String defaultScope = GmQuaternion.class.getSimpleName();

    			public Float x;
	    
        			public Float y;
	    
        			public Float z;
	    
        			public Float w;
	    
        


    public GmQuaternion()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("gm_quaternion_x",null);
    	    	    	    	    	    	model.set("gm_quaternion_y",null);
    	    	    	    	    	    	model.set("gm_quaternion_z",null);
    	    	    	    	    	    	model.set("gm_quaternion_w",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (x != null) {
    	       	    	model.setFloat("gm_quaternion_x",x);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (y != null) {
    	       	    	model.setFloat("gm_quaternion_y",y);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (z != null) {
    	       	    	model.setFloat("gm_quaternion_z",z);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (w != null) {
    	       	    	model.setFloat("gm_quaternion_w",w);
    	        		
    	}
    	    	    }
    
	public static GmQuaternion fromModel(Model model) {
		boolean hasFields = false;
    	GmQuaternion message = new GmQuaternion();
    	    	    	    	    	
    	    	    	Float xField = model.getFloat("gm_quaternion_x");
    	    	
    	if (xField != null) {
    		message.setX(xField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Float yField = model.getFloat("gm_quaternion_y");
    	    	
    	if (yField != null) {
    		message.setY(yField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Float zField = model.getFloat("gm_quaternion_z");
    	    	
    	if (zField != null) {
    		message.setZ(zField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Float wField = model.getFloat("gm_quaternion_w");
    	    	
    	if (wField != null) {
    		message.setW(wField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasX()  {
        return x == null ? false : true;
    }
        
		public Float getX() {
		return x;
	}
	
	public GmQuaternion setX(Float x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Float getY() {
		return y;
	}
	
	public GmQuaternion setY(Float y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasZ()  {
        return z == null ? false : true;
    }
        
		public Float getZ() {
		return z;
	}
	
	public GmQuaternion setZ(Float z) {
		this.z = z;
		return this;	}
	
		    
    public Boolean hasW()  {
        return w == null ? false : true;
    }
        
		public Float getW() {
		return w;
	}
	
	public GmQuaternion setW(Float w) {
		this.w = w;
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

    public Schema<GmQuaternion> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GmQuaternion newMessage()
    {
        return new GmQuaternion();
    }

    public Class<GmQuaternion> typeClass()
    {
        return GmQuaternion.class;
    }

    public String messageName()
    {
        return GmQuaternion.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GmQuaternion.class.getName();
    }

    public boolean isInitialized(GmQuaternion message)
    {
        return true;
    }

    public void mergeFrom(Input input, GmQuaternion message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.x = input.readFloat();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.y = input.readFloat();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.z = input.readFloat();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.w = input.readFloat();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GmQuaternion message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.x != null)
            output.writeFloat(1, message.x, false);
    	    	
    	            	
    	    	
    	    	    	if(message.y != null)
            output.writeFloat(2, message.y, false);
    	    	
    	            	
    	    	
    	    	    	if(message.z != null)
            output.writeFloat(3, message.z, false);
    	    	
    	            	
    	    	
    	    	    	if(message.w != null)
            output.writeFloat(4, message.w, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GmQuaternion");
    	    	if(this.x != null) {
    		System.out.println("x="+this.x);
    	}
    	    	if(this.y != null) {
    		System.out.println("y="+this.y);
    	}
    	    	if(this.z != null) {
    		System.out.println("z="+this.z);
    	}
    	    	if(this.w != null) {
    		System.out.println("w="+this.w);
    	}
    	    	System.out.println("END GmQuaternion");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "x";
        	        	case 2: return "y";
        	        	case 3: return "z";
        	        	case 4: return "w";
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
    	    	__fieldMap.put("x", 1);
    	    	__fieldMap.put("y", 2);
    	    	__fieldMap.put("z", 3);
    	    	__fieldMap.put("w", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GmQuaternion.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GmQuaternion parseFrom(byte[] bytes) {
	GmQuaternion message = new GmQuaternion();
	ProtobufIOUtil.mergeFrom(bytes, message, GmQuaternion.getSchema());
	return message;
}

public static GmQuaternion parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GmQuaternion message = new GmQuaternion();
	JsonIOUtil.mergeFrom(bytes, message, GmQuaternion.getSchema(), false);
	return message;
}

public GmQuaternion clone() {
	byte[] bytes = this.toByteArray();
	GmQuaternion gmQuaternion = GmQuaternion.parseFrom(bytes);
	return gmQuaternion;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GmQuaternion.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GmQuaternion> schema = GmQuaternion.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GmQuaternion.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GmQuaternion.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
