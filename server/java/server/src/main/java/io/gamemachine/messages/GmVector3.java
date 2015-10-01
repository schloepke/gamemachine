
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
public final class GmVector3 implements Externalizable, Message<GmVector3>, Schema<GmVector3>{



    public static Schema<GmVector3> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GmVector3 getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GmVector3 DEFAULT_INSTANCE = new GmVector3();
    static final String defaultScope = GmVector3.class.getSimpleName();

    			public Float x;
	    
        			public Float y;
	    
        			public Float z;
	    
        			public Integer xi;
	    
        			public Integer yi;
	    
        			public Integer zi;
	    
        			public Integer vertice;
	    
        


    public GmVector3()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("gm_vector3_x",null);
    	    	    	    	    	    	model.set("gm_vector3_y",null);
    	    	    	    	    	    	model.set("gm_vector3_z",null);
    	    	    	    	    	    	model.set("gm_vector3_xi",null);
    	    	    	    	    	    	model.set("gm_vector3_yi",null);
    	    	    	    	    	    	model.set("gm_vector3_zi",null);
    	    	    	    	    	    	model.set("gm_vector3_vertice",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (x != null) {
    	       	    	model.setFloat("gm_vector3_x",x);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (y != null) {
    	       	    	model.setFloat("gm_vector3_y",y);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (z != null) {
    	       	    	model.setFloat("gm_vector3_z",z);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (xi != null) {
    	       	    	model.setInteger("gm_vector3_xi",xi);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (yi != null) {
    	       	    	model.setInteger("gm_vector3_yi",yi);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (zi != null) {
    	       	    	model.setInteger("gm_vector3_zi",zi);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (vertice != null) {
    	       	    	model.setInteger("gm_vector3_vertice",vertice);
    	        		
    	}
    	    	    }
    
	public static GmVector3 fromModel(Model model) {
		boolean hasFields = false;
    	GmVector3 message = new GmVector3();
    	    	    	    	    	
    	    	    	Float xField = model.getFloat("gm_vector3_x");
    	    	
    	if (xField != null) {
    		message.setX(xField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Float yField = model.getFloat("gm_vector3_y");
    	    	
    	if (yField != null) {
    		message.setY(yField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Float zField = model.getFloat("gm_vector3_z");
    	    	
    	if (zField != null) {
    		message.setZ(zField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer xiField = model.getInteger("gm_vector3_xi");
    	    	
    	if (xiField != null) {
    		message.setXi(xiField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer yiField = model.getInteger("gm_vector3_yi");
    	    	
    	if (yiField != null) {
    		message.setYi(yiField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer ziField = model.getInteger("gm_vector3_zi");
    	    	
    	if (ziField != null) {
    		message.setZi(ziField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer verticeField = model.getInteger("gm_vector3_vertice");
    	    	
    	if (verticeField != null) {
    		message.setVertice(verticeField);
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
	
	public GmVector3 setX(Float x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Float getY() {
		return y;
	}
	
	public GmVector3 setY(Float y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasZ()  {
        return z == null ? false : true;
    }
        
		public Float getZ() {
		return z;
	}
	
	public GmVector3 setZ(Float z) {
		this.z = z;
		return this;	}
	
		    
    public Boolean hasXi()  {
        return xi == null ? false : true;
    }
        
		public Integer getXi() {
		return xi;
	}
	
	public GmVector3 setXi(Integer xi) {
		this.xi = xi;
		return this;	}
	
		    
    public Boolean hasYi()  {
        return yi == null ? false : true;
    }
        
		public Integer getYi() {
		return yi;
	}
	
	public GmVector3 setYi(Integer yi) {
		this.yi = yi;
		return this;	}
	
		    
    public Boolean hasZi()  {
        return zi == null ? false : true;
    }
        
		public Integer getZi() {
		return zi;
	}
	
	public GmVector3 setZi(Integer zi) {
		this.zi = zi;
		return this;	}
	
		    
    public Boolean hasVertice()  {
        return vertice == null ? false : true;
    }
        
		public Integer getVertice() {
		return vertice;
	}
	
	public GmVector3 setVertice(Integer vertice) {
		this.vertice = vertice;
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

    public Schema<GmVector3> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GmVector3 newMessage()
    {
        return new GmVector3();
    }

    public Class<GmVector3> typeClass()
    {
        return GmVector3.class;
    }

    public String messageName()
    {
        return GmVector3.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GmVector3.class.getName();
    }

    public boolean isInitialized(GmVector3 message)
    {
        return true;
    }

    public void mergeFrom(Input input, GmVector3 message) throws IOException
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
            	                	                	message.xi = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.yi = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.zi = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.vertice = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GmVector3 message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.x != null)
            output.writeFloat(1, message.x, false);
    	    	
    	            	
    	    	
    	    	    	if(message.y != null)
            output.writeFloat(2, message.y, false);
    	    	
    	            	
    	    	
    	    	    	if(message.z != null)
            output.writeFloat(3, message.z, false);
    	    	
    	            	
    	    	
    	    	    	if(message.xi != null)
            output.writeInt32(4, message.xi, false);
    	    	
    	            	
    	    	
    	    	    	if(message.yi != null)
            output.writeInt32(5, message.yi, false);
    	    	
    	            	
    	    	
    	    	    	if(message.zi != null)
            output.writeInt32(6, message.zi, false);
    	    	
    	            	
    	    	
    	    	    	if(message.vertice != null)
            output.writeInt32(7, message.vertice, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GmVector3");
    	    	if(this.x != null) {
    		System.out.println("x="+this.x);
    	}
    	    	if(this.y != null) {
    		System.out.println("y="+this.y);
    	}
    	    	if(this.z != null) {
    		System.out.println("z="+this.z);
    	}
    	    	if(this.xi != null) {
    		System.out.println("xi="+this.xi);
    	}
    	    	if(this.yi != null) {
    		System.out.println("yi="+this.yi);
    	}
    	    	if(this.zi != null) {
    		System.out.println("zi="+this.zi);
    	}
    	    	if(this.vertice != null) {
    		System.out.println("vertice="+this.vertice);
    	}
    	    	System.out.println("END GmVector3");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "x";
        	        	case 2: return "y";
        	        	case 3: return "z";
        	        	case 4: return "xi";
        	        	case 5: return "yi";
        	        	case 6: return "zi";
        	        	case 7: return "vertice";
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
    	    	__fieldMap.put("xi", 4);
    	    	__fieldMap.put("yi", 5);
    	    	__fieldMap.put("zi", 6);
    	    	__fieldMap.put("vertice", 7);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GmVector3.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GmVector3 parseFrom(byte[] bytes) {
	GmVector3 message = new GmVector3();
	ProtobufIOUtil.mergeFrom(bytes, message, GmVector3.getSchema());
	return message;
}

public static GmVector3 parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GmVector3 message = new GmVector3();
	JsonIOUtil.mergeFrom(bytes, message, GmVector3.getSchema(), false);
	return message;
}

public GmVector3 clone() {
	byte[] bytes = this.toByteArray();
	GmVector3 gmVector3 = GmVector3.parseFrom(bytes);
	return gmVector3;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GmVector3.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GmVector3> schema = GmVector3.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GmVector3.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GmVector3.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
