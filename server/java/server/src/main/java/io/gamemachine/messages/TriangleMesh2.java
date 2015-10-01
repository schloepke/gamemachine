
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
public final class TriangleMesh2 implements Externalizable, Message<TriangleMesh2>, Schema<TriangleMesh2>{



    public static Schema<TriangleMesh2> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TriangleMesh2 getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TriangleMesh2 DEFAULT_INSTANCE = new TriangleMesh2();
    static final String defaultScope = TriangleMesh2.class.getSimpleName();

        public List<Float> vertices;
	        public List<Integer> indices;
	    


    public TriangleMesh2()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	    }
    
	public static TriangleMesh2 fromModel(Model model) {
		boolean hasFields = false;
    	TriangleMesh2 message = new TriangleMesh2();
    	    	    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasVertices()  {
        return vertices == null ? false : true;
    }
        
		public List<Float> getVerticesList() {
		if(this.vertices == null)
            this.vertices = new ArrayList<Float>();
		return vertices;
	}

	public TriangleMesh2 setVerticesList(List<Float> vertices) {
		this.vertices = vertices;
		return this;
	}

	public Float getVertices(int index)  {
        return vertices == null ? null : vertices.get(index);
    }

    public int getVerticesCount()  {
        return vertices == null ? 0 : vertices.size();
    }

    public TriangleMesh2 addVertices(Float vertices)  {
        if(this.vertices == null)
            this.vertices = new ArrayList<Float>();
        this.vertices.add(vertices);
        return this;
    }
        	
    
    
    
		    
    public Boolean hasIndices()  {
        return indices == null ? false : true;
    }
        
		public List<Integer> getIndicesList() {
		if(this.indices == null)
            this.indices = new ArrayList<Integer>();
		return indices;
	}

	public TriangleMesh2 setIndicesList(List<Integer> indices) {
		this.indices = indices;
		return this;
	}

	public Integer getIndices(int index)  {
        return indices == null ? null : indices.get(index);
    }

    public int getIndicesCount()  {
        return indices == null ? 0 : indices.size();
    }

    public TriangleMesh2 addIndices(Integer indices)  {
        if(this.indices == null)
            this.indices = new ArrayList<Integer>();
        this.indices.add(indices);
        return this;
    }
        	
    
    
    
	
  
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

    public Schema<TriangleMesh2> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TriangleMesh2 newMessage()
    {
        return new TriangleMesh2();
    }

    public Class<TriangleMesh2> typeClass()
    {
        return TriangleMesh2.class;
    }

    public String messageName()
    {
        return TriangleMesh2.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TriangleMesh2.class.getName();
    }

    public boolean isInitialized(TriangleMesh2 message)
    {
        return true;
    }

    public void mergeFrom(Input input, TriangleMesh2 message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.vertices == null)
                        message.vertices = new ArrayList<Float>();
                                    	message.vertices.add(input.readFloat());
                	                    break;
                            	            	case 2:
            	            		if(message.indices == null)
                        message.indices = new ArrayList<Integer>();
                                    	message.indices.add(input.readInt32());
                	                    break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TriangleMesh2 message) throws IOException
    {
    	    	
    	    	
    	    	if(message.vertices != null)
        {
            for(Float vertices : message.vertices)
            {
                if(vertices != null) {
                   	            		output.writeFloat(1, vertices, true);
    				    			}
            }
        }
    	            	
    	    	
    	    	if(message.indices != null)
        {
            for(Integer indices : message.indices)
            {
                if(indices != null) {
                   	            		output.writeInt32(2, indices, true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START TriangleMesh2");
    	    	if(this.vertices != null) {
    		System.out.println("vertices="+this.vertices);
    	}
    	    	if(this.indices != null) {
    		System.out.println("indices="+this.indices);
    	}
    	    	System.out.println("END TriangleMesh2");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "vertices";
        	        	case 2: return "indices";
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
    	    	__fieldMap.put("vertices", 1);
    	    	__fieldMap.put("indices", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TriangleMesh2.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TriangleMesh2 parseFrom(byte[] bytes) {
	TriangleMesh2 message = new TriangleMesh2();
	ProtobufIOUtil.mergeFrom(bytes, message, TriangleMesh2.getSchema());
	return message;
}

public static TriangleMesh2 parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TriangleMesh2 message = new TriangleMesh2();
	JsonIOUtil.mergeFrom(bytes, message, TriangleMesh2.getSchema(), false);
	return message;
}

public TriangleMesh2 clone() {
	byte[] bytes = this.toByteArray();
	TriangleMesh2 triangleMesh2 = TriangleMesh2.parseFrom(bytes);
	return triangleMesh2;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TriangleMesh2.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TriangleMesh2> schema = TriangleMesh2.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TriangleMesh2.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, TriangleMesh2.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
