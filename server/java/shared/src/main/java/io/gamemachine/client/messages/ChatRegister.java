
package io.gamemachine.client.messages;

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


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class ChatRegister implements Externalizable, Message<ChatRegister>, Schema<ChatRegister>{



    public static Schema<ChatRegister> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatRegister getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatRegister DEFAULT_INSTANCE = new ChatRegister();

    			public String chatId;
	    
        			public String registerAs;
	    
      
    public ChatRegister()
    {
        
    }


	

	    
    public Boolean hasChatId()  {
        return chatId == null ? false : true;
    }
        
		public String getChatId() {
		return chatId;
	}
	
	public ChatRegister setChatId(String chatId) {
		this.chatId = chatId;
		return this;	}
	
		    
    public Boolean hasRegisterAs()  {
        return registerAs == null ? false : true;
    }
        
		public String getRegisterAs() {
		return registerAs;
	}
	
	public ChatRegister setRegisterAs(String registerAs) {
		this.registerAs = registerAs;
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

    public Schema<ChatRegister> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatRegister newMessage()
    {
        return new ChatRegister();
    }

    public Class<ChatRegister> typeClass()
    {
        return ChatRegister.class;
    }

    public String messageName()
    {
        return ChatRegister.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatRegister.class.getName();
    }

    public boolean isInitialized(ChatRegister message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatRegister message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.chatId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.registerAs = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ChatRegister message) throws IOException
    {
    	    	
    	    	if(message.chatId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.chatId != null)
            output.writeString(1, message.chatId, false);
    	    	
    	            	
    	    	if(message.registerAs == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.registerAs != null)
            output.writeString(2, message.registerAs, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "chatId";
        	        	case 2: return "registerAs";
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
    	    	__fieldMap.put("chatId", 1);
    	    	__fieldMap.put("registerAs", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatRegister.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatRegister parseFrom(byte[] bytes) {
	ChatRegister message = new ChatRegister();
	ProtobufIOUtil.mergeFrom(bytes, message, ChatRegister.getSchema());
	return message;
}

public static ChatRegister parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ChatRegister message = new ChatRegister();
	JsonIOUtil.mergeFrom(bytes, message, ChatRegister.getSchema(), false);
	return message;
}

public ChatRegister clone() {
	byte[] bytes = this.toByteArray();
	ChatRegister chatRegister = ChatRegister.parseFrom(bytes);
	return chatRegister;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatRegister.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatRegister> schema = ChatRegister.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ChatRegister.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
