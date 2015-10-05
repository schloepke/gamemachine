
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
public final class EchoTest implements Externalizable, Message<EchoTest>, Schema<EchoTest>{



    public static Schema<EchoTest> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static EchoTest getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final EchoTest DEFAULT_INSTANCE = new EchoTest();
    static final String defaultScope = EchoTest.class.getSimpleName();

    	
	    	    public String message= null;
	    		
    
        


    public EchoTest()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("echo_test_message",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (message != null) {
    	       	    	model.setString("echo_test_message",message);
    	        		
    	//}
    	    	    }
    
	public static EchoTest fromModel(Model model) {
		boolean hasFields = false;
    	EchoTest message = new EchoTest();
    	    	    	    	    	
    	    	    	String messageTestField = model.getString("echo_test_message");
    	if (messageTestField != null) {
    		String messageField = messageTestField;
    		message.setMessage(messageField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getMessage() {
		return message;
	}
	
	public EchoTest setMessage(String message) {
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

    public Schema<EchoTest> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public EchoTest newMessage()
    {
        return new EchoTest();
    }

    public Class<EchoTest> typeClass()
    {
        return EchoTest.class;
    }

    public String messageName()
    {
        return EchoTest.class.getSimpleName();
    }

    public String messageFullName()
    {
        return EchoTest.class.getName();
    }

    public boolean isInitialized(EchoTest message)
    {
        return true;
    }

    public void mergeFrom(Input input, EchoTest message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.message = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, EchoTest message) throws IOException
    {
    	    	
    	    	//if(message.message == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.message != null) {
            output.writeString(1, message.message, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START EchoTest");
    	    	//if(this.message != null) {
    		System.out.println("message="+this.message);
    	//}
    	    	System.out.println("END EchoTest");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "message";
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
    	    	__fieldMap.put("message", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = EchoTest.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static EchoTest parseFrom(byte[] bytes) {
	EchoTest message = new EchoTest();
	ProtobufIOUtil.mergeFrom(bytes, message, EchoTest.getSchema());
	return message;
}

public static EchoTest parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	EchoTest message = new EchoTest();
	JsonIOUtil.mergeFrom(bytes, message, EchoTest.getSchema(), false);
	return message;
}

public EchoTest clone() {
	byte[] bytes = this.toByteArray();
	EchoTest echoTest = EchoTest.parseFrom(bytes);
	return echoTest;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, EchoTest.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<EchoTest> schema = EchoTest.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, EchoTest.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, EchoTest.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
