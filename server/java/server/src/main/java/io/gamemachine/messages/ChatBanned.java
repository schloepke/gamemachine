
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
public final class ChatBanned implements Externalizable, Message<ChatBanned>, Schema<ChatBanned>{



    public static Schema<ChatBanned> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatBanned getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatBanned DEFAULT_INSTANCE = new ChatBanned();
    static final String defaultScope = ChatBanned.class.getSimpleName();

    			public String banned_id;
	    
        			public String channelNname;
	    
        			public String reason;
	    
        


    public ChatBanned()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("chat_banned_banned_id",null);
    	    	    	    	    	    	model.set("chat_banned_channel_nname",null);
    	    	    	    	    	    	model.set("chat_banned_reason",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (banned_id != null) {
    	       	    	model.setString("chat_banned_banned_id",banned_id);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (channelNname != null) {
    	       	    	model.setString("chat_banned_channel_nname",channelNname);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (reason != null) {
    	       	    	model.setString("chat_banned_reason",reason);
    	        		
    	}
    	    	    }
    
	public static ChatBanned fromModel(Model model) {
		boolean hasFields = false;
    	ChatBanned message = new ChatBanned();
    	    	    	    	    	
    	    	    	String banned_idField = model.getString("chat_banned_banned_id");
    	    	
    	if (banned_idField != null) {
    		message.setBanned_id(banned_idField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String channelNnameField = model.getString("chat_banned_channel_nname");
    	    	
    	if (channelNnameField != null) {
    		message.setChannelNname(channelNnameField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String reasonField = model.getString("chat_banned_reason");
    	    	
    	if (reasonField != null) {
    		message.setReason(reasonField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasBanned_id()  {
        return banned_id == null ? false : true;
    }
        
		public String getBanned_id() {
		return banned_id;
	}
	
	public ChatBanned setBanned_id(String banned_id) {
		this.banned_id = banned_id;
		return this;	}
	
		    
    public Boolean hasChannelNname()  {
        return channelNname == null ? false : true;
    }
        
		public String getChannelNname() {
		return channelNname;
	}
	
	public ChatBanned setChannelNname(String channelNname) {
		this.channelNname = channelNname;
		return this;	}
	
		    
    public Boolean hasReason()  {
        return reason == null ? false : true;
    }
        
		public String getReason() {
		return reason;
	}
	
	public ChatBanned setReason(String reason) {
		this.reason = reason;
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

    public Schema<ChatBanned> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatBanned newMessage()
    {
        return new ChatBanned();
    }

    public Class<ChatBanned> typeClass()
    {
        return ChatBanned.class;
    }

    public String messageName()
    {
        return ChatBanned.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatBanned.class.getName();
    }

    public boolean isInitialized(ChatBanned message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatBanned message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.banned_id = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.channelNname = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.reason = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ChatBanned message) throws IOException
    {
    	    	
    	    	if(message.banned_id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.banned_id != null)
            output.writeString(1, message.banned_id, false);
    	    	
    	            	
    	    	if(message.channelNname == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.channelNname != null)
            output.writeString(2, message.channelNname, false);
    	    	
    	            	
    	    	
    	    	    	if(message.reason != null)
            output.writeString(3, message.reason, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ChatBanned");
    	    	if(this.banned_id != null) {
    		System.out.println("banned_id="+this.banned_id);
    	}
    	    	if(this.channelNname != null) {
    		System.out.println("channelNname="+this.channelNname);
    	}
    	    	if(this.reason != null) {
    		System.out.println("reason="+this.reason);
    	}
    	    	System.out.println("END ChatBanned");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "banned_id";
        	        	case 2: return "channelNname";
        	        	case 3: return "reason";
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
    	    	__fieldMap.put("banned_id", 1);
    	    	__fieldMap.put("channelNname", 2);
    	    	__fieldMap.put("reason", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatBanned.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatBanned parseFrom(byte[] bytes) {
	ChatBanned message = new ChatBanned();
	ProtobufIOUtil.mergeFrom(bytes, message, ChatBanned.getSchema());
	return message;
}

public static ChatBanned parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ChatBanned message = new ChatBanned();
	JsonIOUtil.mergeFrom(bytes, message, ChatBanned.getSchema(), false);
	return message;
}

public ChatBanned clone() {
	byte[] bytes = this.toByteArray();
	ChatBanned chatBanned = ChatBanned.parseFrom(bytes);
	return chatBanned;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatBanned.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatBanned> schema = ChatBanned.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ChatBanned.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ChatBanned.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
