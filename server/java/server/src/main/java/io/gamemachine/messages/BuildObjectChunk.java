
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
public final class BuildObjectChunk implements Externalizable, Message<BuildObjectChunk>, Schema<BuildObjectChunk>{

private static final Logger logger = LoggerFactory.getLogger(BuildObjectChunk.class);



    public static Schema<BuildObjectChunk> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildObjectChunk getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildObjectChunk DEFAULT_INSTANCE = new BuildObjectChunk();
    static final String defaultScope = BuildObjectChunk.class.getSimpleName();

    	
							    public ByteString data;
		    			    
		
    
        	
							    public int chunk= 0;
		    			    
		
    
        


    public BuildObjectChunk()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("build_object_chunk_data",null);
    	    	    	    	    	    	model.set("build_object_chunk_chunk",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (data != null) {
    	       	        if (data != null) {
    	    		model.set("build_object_chunk_data",data.toByteArray());
    	    	}
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (chunk != null) {
    	       	    	model.setInteger("build_object_chunk_chunk",chunk);
    	        		
    	//}
    	    	    }
    
	public static BuildObjectChunk fromModel(Model model) {
		boolean hasFields = false;
    	BuildObjectChunk message = new BuildObjectChunk();
    	    	    	    	    	
    	    	    	
    	ByteString dataField = null;
		Object dataValue = model.get("build_object_chunk_data");
		if (dataValue != null) {
			byte[] dataBytes = Convert.toBytes(dataValue);
			dataField = ByteString.copyFrom(dataBytes);
		}
    	    	
    	    	
    	    	    	    	    	    	
    	    			Integer chunkTestField = model.getInteger("build_object_chunk_chunk");
		if (chunkTestField != null) {
			int chunkField = chunkTestField;
			message.setChunk(chunkField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public ByteString getData() {
		return data;
	}
	
	public BuildObjectChunk setData(ByteString data) {
		this.data = data;
		return this;	}
	
		            
		public int getChunk() {
		return chunk;
	}
	
	public BuildObjectChunk setChunk(int chunk) {
		this.chunk = chunk;
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

    public Schema<BuildObjectChunk> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildObjectChunk newMessage()
    {
        return new BuildObjectChunk();
    }

    public Class<BuildObjectChunk> typeClass()
    {
        return BuildObjectChunk.class;
    }

    public String messageName()
    {
        return BuildObjectChunk.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildObjectChunk.class.getName();
    }

    public boolean isInitialized(BuildObjectChunk message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildObjectChunk message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.data = input.readBytes();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.chunk = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildObjectChunk message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (ByteString)message.data != null) {
            output.writeBytes(1, message.data, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.chunk != null) {
            output.writeInt32(2, message.chunk, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START BuildObjectChunk");
    	    	//if(this.data != null) {
    		System.out.println("data="+this.data);
    	//}
    	    	//if(this.chunk != null) {
    		System.out.println("chunk="+this.chunk);
    	//}
    	    	System.out.println("END BuildObjectChunk");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "data";
        	        	case 2: return "chunk";
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
    	    	__fieldMap.put("data", 1);
    	    	__fieldMap.put("chunk", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildObjectChunk.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildObjectChunk parseFrom(byte[] bytes) {
	BuildObjectChunk message = new BuildObjectChunk();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildObjectChunk.getSchema());
	return message;
}

public static BuildObjectChunk parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildObjectChunk message = new BuildObjectChunk();
	JsonIOUtil.mergeFrom(bytes, message, BuildObjectChunk.getSchema(), false);
	return message;
}

public BuildObjectChunk clone() {
	byte[] bytes = this.toByteArray();
	BuildObjectChunk buildObjectChunk = BuildObjectChunk.parseFrom(bytes);
	return buildObjectChunk;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildObjectChunk.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildObjectChunk> schema = BuildObjectChunk.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildObjectChunk.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, BuildObjectChunk.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
