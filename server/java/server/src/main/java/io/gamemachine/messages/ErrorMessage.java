
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
public final class ErrorMessage implements Externalizable, Message<ErrorMessage>, Schema<ErrorMessage>{



    public static Schema<ErrorMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ErrorMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ErrorMessage DEFAULT_INSTANCE = new ErrorMessage();
    static final String defaultScope = ErrorMessage.class.getSimpleName();

    	
							    public String code= null;
		    			    
		
    
        	
							    public String message= null;
		    			    
		
    
        


    public ErrorMessage()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("error_message_code",null);
    	    	    	    	    	    	model.set("error_message_message",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (code != null) {
    	       	    	model.setString("error_message_code",code);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (message != null) {
    	       	    	model.setString("error_message_message",message);
    	        		
    	//}
    	    	    }
    
	public static ErrorMessage fromModel(Model model) {
		boolean hasFields = false;
    	ErrorMessage message = new ErrorMessage();
    	    	    	    	    	
    	    	    	String codeTestField = model.getString("error_message_code");
    	if (codeTestField != null) {
    		String codeField = codeTestField;
    		message.setCode(codeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String messageTestField = model.getString("error_message_message");
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


	            
		public String getCode() {
		return code;
	}
	
	public ErrorMessage setCode(String code) {
		this.code = code;
		return this;	}
	
		            
		public String getMessage() {
		return message;
	}
	
	public ErrorMessage setMessage(String message) {
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

    public Schema<ErrorMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ErrorMessage newMessage()
    {
        return new ErrorMessage();
    }

    public Class<ErrorMessage> typeClass()
    {
        return ErrorMessage.class;
    }

    public String messageName()
    {
        return ErrorMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ErrorMessage.class.getName();
    }

    public boolean isInitialized(ErrorMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, ErrorMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.code = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.message = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ErrorMessage message) throws IOException
    {
    	    	
    	    	//if(message.code == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.code != null) {
            output.writeString(1, message.code, false);
        }
    	    	
    	            	
    	    	//if(message.message == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.message != null) {
            output.writeString(2, message.message, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ErrorMessage");
    	    	//if(this.code != null) {
    		System.out.println("code="+this.code);
    	//}
    	    	//if(this.message != null) {
    		System.out.println("message="+this.message);
    	//}
    	    	System.out.println("END ErrorMessage");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "code";
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
    	    	__fieldMap.put("code", 1);
    	    	__fieldMap.put("message", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ErrorMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ErrorMessage parseFrom(byte[] bytes) {
	ErrorMessage message = new ErrorMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, ErrorMessage.getSchema());
	return message;
}

public static ErrorMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ErrorMessage message = new ErrorMessage();
	JsonIOUtil.mergeFrom(bytes, message, ErrorMessage.getSchema(), false);
	return message;
}

public ErrorMessage clone() {
	byte[] bytes = this.toByteArray();
	ErrorMessage errorMessage = ErrorMessage.parseFrom(bytes);
	return errorMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ErrorMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ErrorMessage> schema = ErrorMessage.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ErrorMessage.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ErrorMessage.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
