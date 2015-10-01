
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
public final class BuildObjectChunks implements Externalizable, Message<BuildObjectChunks>, Schema<BuildObjectChunks>{



    public static Schema<BuildObjectChunks> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildObjectChunks getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildObjectChunks DEFAULT_INSTANCE = new BuildObjectChunks();
    static final String defaultScope = BuildObjectChunks.class.getSimpleName();

        public List<BuildObjectChunk> buildObjectChunk;
	    


    public BuildObjectChunks()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static BuildObjectChunks fromModel(Model model) {
		boolean hasFields = false;
    	BuildObjectChunks message = new BuildObjectChunks();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasBuildObjectChunk()  {
        return buildObjectChunk == null ? false : true;
    }
        
		public List<BuildObjectChunk> getBuildObjectChunkList() {
		if(this.buildObjectChunk == null)
            this.buildObjectChunk = new ArrayList<BuildObjectChunk>();
		return buildObjectChunk;
	}

	public BuildObjectChunks setBuildObjectChunkList(List<BuildObjectChunk> buildObjectChunk) {
		this.buildObjectChunk = buildObjectChunk;
		return this;
	}

	public BuildObjectChunk getBuildObjectChunk(int index)  {
        return buildObjectChunk == null ? null : buildObjectChunk.get(index);
    }

    public int getBuildObjectChunkCount()  {
        return buildObjectChunk == null ? 0 : buildObjectChunk.size();
    }

    public BuildObjectChunks addBuildObjectChunk(BuildObjectChunk buildObjectChunk)  {
        if(this.buildObjectChunk == null)
            this.buildObjectChunk = new ArrayList<BuildObjectChunk>();
        this.buildObjectChunk.add(buildObjectChunk);
        return this;
    }
            	    	    	    	
    public BuildObjectChunks removeBuildObjectChunkByData(BuildObjectChunk buildObjectChunk)  {
    	if(this.buildObjectChunk == null)
           return this;
            
       	Iterator<BuildObjectChunk> itr = this.buildObjectChunk.iterator();
       	while (itr.hasNext()) {
    	BuildObjectChunk obj = itr.next();
    	
    	    		if (buildObjectChunk.data.equals(obj.data)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjectChunks removeBuildObjectChunkByChunk(BuildObjectChunk buildObjectChunk)  {
    	if(this.buildObjectChunk == null)
           return this;
            
       	Iterator<BuildObjectChunk> itr = this.buildObjectChunk.iterator();
       	while (itr.hasNext()) {
    	BuildObjectChunk obj = itr.next();
    	
    	    		if (buildObjectChunk.chunk.equals(obj.chunk)) {
    	      			itr.remove();
    		}
		}
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

    public Schema<BuildObjectChunks> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildObjectChunks newMessage()
    {
        return new BuildObjectChunks();
    }

    public Class<BuildObjectChunks> typeClass()
    {
        return BuildObjectChunks.class;
    }

    public String messageName()
    {
        return BuildObjectChunks.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildObjectChunks.class.getName();
    }

    public boolean isInitialized(BuildObjectChunks message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildObjectChunks message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.buildObjectChunk == null)
                        message.buildObjectChunk = new ArrayList<BuildObjectChunk>();
                                        message.buildObjectChunk.add(input.mergeObject(null, BuildObjectChunk.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildObjectChunks message) throws IOException
    {
    	    	
    	    	
    	    	if(message.buildObjectChunk != null)
        {
            for(BuildObjectChunk buildObjectChunk : message.buildObjectChunk)
            {
                if(buildObjectChunk != null) {
                   	    				output.writeObject(1, buildObjectChunk, BuildObjectChunk.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START BuildObjectChunks");
    	    	if(this.buildObjectChunk != null) {
    		System.out.println("buildObjectChunk="+this.buildObjectChunk);
    	}
    	    	System.out.println("END BuildObjectChunks");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "buildObjectChunk";
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
    	    	__fieldMap.put("buildObjectChunk", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildObjectChunks.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildObjectChunks parseFrom(byte[] bytes) {
	BuildObjectChunks message = new BuildObjectChunks();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildObjectChunks.getSchema());
	return message;
}

public static BuildObjectChunks parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildObjectChunks message = new BuildObjectChunks();
	JsonIOUtil.mergeFrom(bytes, message, BuildObjectChunks.getSchema(), false);
	return message;
}

public BuildObjectChunks clone() {
	byte[] bytes = this.toByteArray();
	BuildObjectChunks buildObjectChunks = BuildObjectChunks.parseFrom(bytes);
	return buildObjectChunks;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildObjectChunks.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildObjectChunks> schema = BuildObjectChunks.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildObjectChunks.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, BuildObjectChunks.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
