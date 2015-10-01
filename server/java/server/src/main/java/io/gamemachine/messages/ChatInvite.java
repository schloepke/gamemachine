
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
public final class ChatInvite implements Externalizable, Message<ChatInvite>, Schema<ChatInvite>{



    public static Schema<ChatInvite> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatInvite getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatInvite DEFAULT_INSTANCE = new ChatInvite();
    static final String defaultScope = ChatInvite.class.getSimpleName();

    			public String invitee;
	    
        			public String inviter;
	    
        			public String channelName;
	    
        			public String invite_id;
	    
        


    public ChatInvite()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("chat_invite_invitee",null);
    	    	    	    	    	    	model.set("chat_invite_inviter",null);
    	    	    	    	    	    	model.set("chat_invite_channel_name",null);
    	    	    	    	    	    	model.set("chat_invite_invite_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (invitee != null) {
    	       	    	model.setString("chat_invite_invitee",invitee);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (inviter != null) {
    	       	    	model.setString("chat_invite_inviter",inviter);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (channelName != null) {
    	       	    	model.setString("chat_invite_channel_name",channelName);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (invite_id != null) {
    	       	    	model.setString("chat_invite_invite_id",invite_id);
    	        		
    	}
    	    	    }
    
	public static ChatInvite fromModel(Model model) {
		boolean hasFields = false;
    	ChatInvite message = new ChatInvite();
    	    	    	    	    	
    	    	    	String inviteeField = model.getString("chat_invite_invitee");
    	    	
    	if (inviteeField != null) {
    		message.setInvitee(inviteeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String inviterField = model.getString("chat_invite_inviter");
    	    	
    	if (inviterField != null) {
    		message.setInviter(inviterField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String channelNameField = model.getString("chat_invite_channel_name");
    	    	
    	if (channelNameField != null) {
    		message.setChannelName(channelNameField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String invite_idField = model.getString("chat_invite_invite_id");
    	    	
    	if (invite_idField != null) {
    		message.setInvite_id(invite_idField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasInvitee()  {
        return invitee == null ? false : true;
    }
        
		public String getInvitee() {
		return invitee;
	}
	
	public ChatInvite setInvitee(String invitee) {
		this.invitee = invitee;
		return this;	}
	
		    
    public Boolean hasInviter()  {
        return inviter == null ? false : true;
    }
        
		public String getInviter() {
		return inviter;
	}
	
	public ChatInvite setInviter(String inviter) {
		this.inviter = inviter;
		return this;	}
	
		    
    public Boolean hasChannelName()  {
        return channelName == null ? false : true;
    }
        
		public String getChannelName() {
		return channelName;
	}
	
	public ChatInvite setChannelName(String channelName) {
		this.channelName = channelName;
		return this;	}
	
		    
    public Boolean hasInvite_id()  {
        return invite_id == null ? false : true;
    }
        
		public String getInvite_id() {
		return invite_id;
	}
	
	public ChatInvite setInvite_id(String invite_id) {
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

    public Schema<ChatInvite> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatInvite newMessage()
    {
        return new ChatInvite();
    }

    public Class<ChatInvite> typeClass()
    {
        return ChatInvite.class;
    }

    public String messageName()
    {
        return ChatInvite.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatInvite.class.getName();
    }

    public boolean isInitialized(ChatInvite message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatInvite message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.invitee = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.inviter = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.channelName = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.invite_id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ChatInvite message) throws IOException
    {
    	    	
    	    	if(message.invitee == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.invitee != null)
            output.writeString(1, message.invitee, false);
    	    	
    	            	
    	    	if(message.inviter == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.inviter != null)
            output.writeString(2, message.inviter, false);
    	    	
    	            	
    	    	if(message.channelName == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.channelName != null)
            output.writeString(4, message.channelName, false);
    	    	
    	            	
    	    	if(message.invite_id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.invite_id != null)
            output.writeString(5, message.invite_id, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ChatInvite");
    	    	if(this.invitee != null) {
    		System.out.println("invitee="+this.invitee);
    	}
    	    	if(this.inviter != null) {
    		System.out.println("inviter="+this.inviter);
    	}
    	    	if(this.channelName != null) {
    		System.out.println("channelName="+this.channelName);
    	}
    	    	if(this.invite_id != null) {
    		System.out.println("invite_id="+this.invite_id);
    	}
    	    	System.out.println("END ChatInvite");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "invitee";
        	        	case 2: return "inviter";
        	        	case 4: return "channelName";
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
    	    	__fieldMap.put("invitee", 1);
    	    	__fieldMap.put("inviter", 2);
    	    	__fieldMap.put("channelName", 4);
    	    	__fieldMap.put("invite_id", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatInvite.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatInvite parseFrom(byte[] bytes) {
	ChatInvite message = new ChatInvite();
	ProtobufIOUtil.mergeFrom(bytes, message, ChatInvite.getSchema());
	return message;
}

public static ChatInvite parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ChatInvite message = new ChatInvite();
	JsonIOUtil.mergeFrom(bytes, message, ChatInvite.getSchema(), false);
	return message;
}

public ChatInvite clone() {
	byte[] bytes = this.toByteArray();
	ChatInvite chatInvite = ChatInvite.parseFrom(bytes);
	return chatInvite;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatInvite.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatInvite> schema = ChatInvite.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ChatInvite.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, ChatInvite.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
