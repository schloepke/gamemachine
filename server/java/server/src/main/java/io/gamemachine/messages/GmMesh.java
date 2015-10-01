
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
public final class GmMesh implements Externalizable, Message<GmMesh>, Schema<GmMesh>{



    public static Schema<GmMesh> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GmMesh getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GmMesh DEFAULT_INSTANCE = new GmMesh();
    static final String defaultScope = GmMesh.class.getSimpleName();

        public List<MeshNode> nodes;
	    


    public GmMesh()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static GmMesh fromModel(Model model) {
		boolean hasFields = false;
    	GmMesh message = new GmMesh();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasNodes()  {
        return nodes == null ? false : true;
    }
        
		public List<MeshNode> getNodesList() {
		if(this.nodes == null)
            this.nodes = new ArrayList<MeshNode>();
		return nodes;
	}

	public GmMesh setNodesList(List<MeshNode> nodes) {
		this.nodes = nodes;
		return this;
	}

	public MeshNode getNodes(int index)  {
        return nodes == null ? null : nodes.get(index);
    }

    public int getNodesCount()  {
        return nodes == null ? 0 : nodes.size();
    }

    public GmMesh addNodes(MeshNode nodes)  {
        if(this.nodes == null)
            this.nodes = new ArrayList<MeshNode>();
        this.nodes.add(nodes);
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

    public Schema<GmMesh> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GmMesh newMessage()
    {
        return new GmMesh();
    }

    public Class<GmMesh> typeClass()
    {
        return GmMesh.class;
    }

    public String messageName()
    {
        return GmMesh.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GmMesh.class.getName();
    }

    public boolean isInitialized(GmMesh message)
    {
        return true;
    }

    public void mergeFrom(Input input, GmMesh message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.nodes == null)
                        message.nodes = new ArrayList<MeshNode>();
                                        message.nodes.add(input.mergeObject(null, MeshNode.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GmMesh message) throws IOException
    {
    	    	
    	    	
    	    	if(message.nodes != null)
        {
            for(MeshNode nodes : message.nodes)
            {
                if(nodes != null) {
                   	    				output.writeObject(1, nodes, MeshNode.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GmMesh");
    	    	if(this.nodes != null) {
    		System.out.println("nodes="+this.nodes);
    	}
    	    	System.out.println("END GmMesh");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "nodes";
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
    	    	__fieldMap.put("nodes", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GmMesh.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GmMesh parseFrom(byte[] bytes) {
	GmMesh message = new GmMesh();
	ProtobufIOUtil.mergeFrom(bytes, message, GmMesh.getSchema());
	return message;
}

public static GmMesh parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GmMesh message = new GmMesh();
	JsonIOUtil.mergeFrom(bytes, message, GmMesh.getSchema(), false);
	return message;
}

public GmMesh clone() {
	byte[] bytes = this.toByteArray();
	GmMesh gmMesh = GmMesh.parseFrom(bytes);
	return gmMesh;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GmMesh.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GmMesh> schema = GmMesh.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GmMesh.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GmMesh.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
