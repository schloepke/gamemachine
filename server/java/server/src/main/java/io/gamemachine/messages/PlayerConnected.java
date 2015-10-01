
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
public final class PlayerConnected implements Externalizable, Message<PlayerConnected>, Schema<PlayerConnected>{



    public static Schema<PlayerConnected> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerConnected getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerConnected DEFAULT_INSTANCE = new PlayerConnected();
    static final String defaultScope = PlayerConnected.class.getSimpleName();

    			public Integer notused;
	    
        


    public PlayerConnected()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("player_connected_notused",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (notused != null) {
    	       	    	model.setInteger("player_connected_notused",notused);
    	        		
    	}
    	    	    }
    
	public static PlayerConnected fromModel(Model model) {
		boolean hasFields = false;
    	PlayerConnected message = new PlayerConnected();
    	    	    	    	    	
    	    	    	Integer notusedField = model.getInteger("player_connected_notused");
    	    	
    	if (notusedField != null) {
    		message.setNotused(notusedField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasNotused()  {
        return notused == null ? false : true;
    }
        
		public Integer getNotused() {
		return notused;
	}
	
	public PlayerConnected setNotused(Integer notused) {
		this.notused = notused;
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

    public Schema<PlayerConnected> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerConnected newMessage()
    {
        return new PlayerConnected();
    }

    public Class<PlayerConnected> typeClass()
    {
        return PlayerConnected.class;
    }

    public String messageName()
    {
        return PlayerConnected.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerConnected.class.getName();
    }

    public boolean isInitialized(PlayerConnected message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerConnected message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.notused = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerConnected message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.notused != null)
            output.writeInt32(1, message.notused, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START PlayerConnected");
    	    	if(this.notused != null) {
    		System.out.println("notused="+this.notused);
    	}
    	    	System.out.println("END PlayerConnected");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "notused";
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
    	    	__fieldMap.put("notused", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerConnected.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerConnected parseFrom(byte[] bytes) {
	PlayerConnected message = new PlayerConnected();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerConnected.getSchema());
	return message;
}

public static PlayerConnected parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerConnected message = new PlayerConnected();
	JsonIOUtil.mergeFrom(bytes, message, PlayerConnected.getSchema(), false);
	return message;
}

public PlayerConnected clone() {
	byte[] bytes = this.toByteArray();
	PlayerConnected playerConnected = PlayerConnected.parseFrom(bytes);
	return playerConnected;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerConnected.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerConnected> schema = PlayerConnected.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerConnected.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, PlayerConnected.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
