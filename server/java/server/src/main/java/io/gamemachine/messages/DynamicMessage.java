
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
public final class DynamicMessage implements Externalizable, Message<DynamicMessage>, Schema<DynamicMessage>{



    public static Schema<DynamicMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static DynamicMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final DynamicMessage DEFAULT_INSTANCE = new DynamicMessage();
    static final String defaultScope = DynamicMessage.class.getSimpleName();

    			public String type;
	    
        			public ByteString message;
	    
        


    public DynamicMessage()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("dynamic_message_type",null);
    	    	    	    	    	    	model.set("dynamic_message_message",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (type != null) {
    	       	    	model.setString("dynamic_message_type",type);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (message != null) {
    	       	    	model.set("dynamic_message_message",message.toByteArray());
    	        		
    	}
    	    	    }
    
	public static DynamicMessage fromModel(Model model) {
		boolean hasFields = false;
    	DynamicMessage message = new DynamicMessage();
    	    	    	    	    	
    	    	    	String typeField = model.getString("dynamic_message_type");
    	    	
    	if (typeField != null) {
    		message.setType(typeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	ByteString messageField = null;
    	Object messageValue = model.get("dynamic_message_message");
    	if (messageValue != null) {
    		byte[] messageBytes = Convert.toBytes(messageValue);
    		messageField = ByteString.copyFrom(messageBytes);
    	}
    	    	
    	    	
    	if (messageField != null) {
    		message.setMessage(messageField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public String getType() {
		return type;
	}
	
	public DynamicMessage setType(String type) {
		this.type = type;
		return this;	}
	
		    
    public Boolean hasMessage()  {
        return message == null ? false : true;
    }
        
		public ByteString getMessage() {
		return message;
	}
	
	public DynamicMessage setMessage(ByteString message) {
		this.message = message;
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

    public Schema<DynamicMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public DynamicMessage newMessage()
    {
        return new DynamicMessage();
    }

    public Class<DynamicMessage> typeClass()
    {
        return DynamicMessage.class;
    }

    public String messageName()
    {
        return DynamicMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return DynamicMessage.class.getName();
    }

    public boolean isInitialized(DynamicMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, DynamicMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.type = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.message = input.readBytes();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, DynamicMessage message) throws IOException
    {
    	    	
    	    	if(message.type == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.type != null)
            output.writeString(1, message.type, false);
    	    	
    	            	
    	    	if(message.message == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.message != null)
            output.writeBytes(2, message.message, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START DynamicMessage");
    	    	if(this.type != null) {
    		System.out.println("type="+this.type);
    	}
    	    	if(this.message != null) {
    		System.out.println("message="+this.message);
    	}
    	    	System.out.println("END DynamicMessage");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "type";
        	        	case 2: return "message";
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
    	    	__fieldMap.put("type", 1);
    	    	__fieldMap.put("message", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = DynamicMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static DynamicMessage parseFrom(byte[] bytes) {
	DynamicMessage message = new DynamicMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, DynamicMessage.getSchema());
	return message;
}

public static DynamicMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	DynamicMessage message = new DynamicMessage();
	JsonIOUtil.mergeFrom(bytes, message, DynamicMessage.getSchema(), false);
	return message;
}

public DynamicMessage clone() {
	byte[] bytes = this.toByteArray();
	DynamicMessage dynamicMessage = DynamicMessage.parseFrom(bytes);
	return dynamicMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, DynamicMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<DynamicMessage> schema = DynamicMessage.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, DynamicMessage.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, DynamicMessage.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
