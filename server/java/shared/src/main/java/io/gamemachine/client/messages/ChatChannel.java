
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
public final class ChatChannel implements Externalizable, Message<ChatChannel>, Schema<ChatChannel>{



    public static Schema<ChatChannel> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatChannel getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatChannel DEFAULT_INSTANCE = new ChatChannel();

    			public String name;
	    
        			public Subscribers subscribers;
	    
        			public String flags;
	    
        			public String invite_id;
	    
      
    public ChatChannel()
    {
        
    }


	

	    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public ChatChannel setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasSubscribers()  {
        return subscribers == null ? false : true;
    }
        
		public Subscribers getSubscribers() {
		return subscribers;
	}
	
	public ChatChannel setSubscribers(Subscribers subscribers) {
		this.subscribers = subscribers;
		return this;	}
	
		    
    public Boolean hasFlags()  {
        return flags == null ? false : true;
    }
        
		public String getFlags() {
		return flags;
	}
	
	public ChatChannel setFlags(String flags) {
		this.flags = flags;
		return this;	}
	
		    
    public Boolean hasInvite_id()  {
        return invite_id == null ? false : true;
    }
        
		public String getInvite_id() {
		return invite_id;
	}
	
	public ChatChannel setInvite_id(String invite_id) {
		this.invite_id = invite_id;
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

    public Schema<ChatChannel> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatChannel newMessage()
    {
        return new ChatChannel();
    }

    public Class<ChatChannel> typeClass()
    {
        return ChatChannel.class;
    }

    public String messageName()
    {
        return ChatChannel.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatChannel.class.getName();
    }

    public boolean isInitialized(ChatChannel message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatChannel message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.subscribers = input.mergeObject(message.subscribers, Subscribers.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.flags = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.invite_id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ChatChannel message) throws IOException
    {
    	    	
    	    	if(message.name == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.name != null)
            output.writeString(1, message.name, false);
    	    	
    	            	
    	    	
    	    	    	if(message.subscribers != null)
    		output.writeObject(2, message.subscribers, Subscribers.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.flags != null)
            output.writeString(3, message.flags, false);
    	    	
    	            	
    	    	
    	    	    	if(message.invite_id != null)
            output.writeString(5, message.invite_id, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "name";
        	        	case 2: return "subscribers";
        	        	case 3: return "flags";
        	        	case 5: return "invite_id";
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
    	    	__fieldMap.put("name", 1);
    	    	__fieldMap.put("subscribers", 2);
    	    	__fieldMap.put("flags", 3);
    	    	__fieldMap.put("invite_id", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatChannel.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatChannel parseFrom(byte[] bytes) {
	ChatChannel message = new ChatChannel();
	ProtobufIOUtil.mergeFrom(bytes, message, ChatChannel.getSchema());
	return message;
}

public static ChatChannel parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ChatChannel message = new ChatChannel();
	JsonIOUtil.mergeFrom(bytes, message, ChatChannel.getSchema(), false);
	return message;
}

public ChatChannel clone() {
	byte[] bytes = this.toByteArray();
	ChatChannel chatChannel = ChatChannel.parseFrom(bytes);
	return chatChannel;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatChannel.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatChannel> schema = ChatChannel.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ChatChannel.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
