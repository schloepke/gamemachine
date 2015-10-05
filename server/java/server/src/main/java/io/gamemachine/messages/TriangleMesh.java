
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
public final class TriangleMesh implements Externalizable, Message<TriangleMesh>, Schema<TriangleMesh>{



    public static Schema<TriangleMesh> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TriangleMesh getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TriangleMesh DEFAULT_INSTANCE = new TriangleMesh();
    static final String defaultScope = TriangleMesh.class.getSimpleName();

        public List<GmVector3> vertices;
	        public List<Integer> indices;
	    


    public TriangleMesh()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	    }
    
	public static TriangleMesh fromModel(Model model) {
		boolean hasFields = false;
    	TriangleMesh message = new TriangleMesh();
    	    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public List<GmVector3> getVerticesList() {
		if(this.vertices == null)
            this.vertices = new ArrayList<GmVector3>();
		return vertices;
	}

	public TriangleMesh setVerticesList(List<GmVector3> vertices) {
		this.vertices = vertices;
		return this;
	}

	public GmVector3 getVertices(int index)  {
        return vertices == null ? null : vertices.get(index);
    }

    public int getVerticesCount()  {
        return vertices == null ? 0 : vertices.size();
    }

    public TriangleMesh addVertices(GmVector3 vertices)  {
        if(this.vertices == null)
            this.vertices = new ArrayList<GmVector3>();
        this.vertices.add(vertices);
        return this;
    }
            	    	    	    	
    public TriangleMesh removeVerticesByX(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.x == obj.x) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TriangleMesh removeVerticesByY(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.y == obj.y) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TriangleMesh removeVerticesByZ(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.z == obj.z) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TriangleMesh removeVerticesByXi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.xi == obj.xi) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TriangleMesh removeVerticesByYi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.yi == obj.yi) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TriangleMesh removeVerticesByZi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.zi == obj.zi) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TriangleMesh removeVerticesByVertice(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.vertice == obj.vertice) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		            
		public List<Integer> getIndicesList() {
		if(this.indices == null)
            this.indices = new ArrayList<Integer>();
		return indices;
	}

	public TriangleMesh setIndicesList(List<Integer> indices) {
		this.indices = indices;
		return this;
	}

	public int getIndices(int index)  {
        return indices == null ? null : indices.get(index);
    }

    public int getIndicesCount()  {
        return indices == null ? 0 : indices.size();
    }

    public TriangleMesh addIndices(int indices)  {
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

    public Schema<TriangleMesh> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TriangleMesh newMessage()
    {
        return new TriangleMesh();
    }

    public Class<TriangleMesh> typeClass()
    {
        return TriangleMesh.class;
    }

    public String messageName()
    {
        return TriangleMesh.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TriangleMesh.class.getName();
    }

    public boolean isInitialized(TriangleMesh message)
    {
        return true;
    }

    public void mergeFrom(Input input, TriangleMesh message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.vertices == null)
                        message.vertices = new ArrayList<GmVector3>();
                                        message.vertices.add(input.mergeObject(null, GmVector3.getSchema()));
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


    public void writeTo(Output output, TriangleMesh message) throws IOException
    {
    	    	
    	    	
    	    	if(message.vertices != null)
        {
            for(GmVector3 vertices : message.vertices)
            {
                if( (GmVector3) vertices != null) {
                   	    				output.writeObject(1, vertices, GmVector3.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	if(message.indices != null)
        {
            for(int indices : message.indices)
            {
                if( (Integer) indices != null) {
                   	            		output.writeInt32(2, indices, true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START TriangleMesh");
    	    	//if(this.vertices != null) {
    		System.out.println("vertices="+this.vertices);
    	//}
    	    	//if(this.indices != null) {
    		System.out.println("indices="+this.indices);
    	//}
    	    	System.out.println("END TriangleMesh");
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
		fieldName = TriangleMesh.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TriangleMesh parseFrom(byte[] bytes) {
	TriangleMesh message = new TriangleMesh();
	ProtobufIOUtil.mergeFrom(bytes, message, TriangleMesh.getSchema());
	return message;
}

public static TriangleMesh parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TriangleMesh message = new TriangleMesh();
	JsonIOUtil.mergeFrom(bytes, message, TriangleMesh.getSchema(), false);
	return message;
}

public TriangleMesh clone() {
	byte[] bytes = this.toByteArray();
	TriangleMesh triangleMesh = TriangleMesh.parseFrom(bytes);
	return triangleMesh;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TriangleMesh.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TriangleMesh> schema = TriangleMesh.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TriangleMesh.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, TriangleMesh.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
