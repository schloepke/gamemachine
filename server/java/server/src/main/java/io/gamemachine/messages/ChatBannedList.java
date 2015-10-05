
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
public final class ChatBannedList implements Externalizable, Message<ChatBannedList>, Schema<ChatBannedList>{



    public static Schema<ChatBannedList> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatBannedList getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatBannedList DEFAULT_INSTANCE = new ChatBannedList();
    static final String defaultScope = ChatBannedList.class.getSimpleName();

        public List<ChatBanned> chatBanned;
	    


    public ChatBannedList()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static ChatBannedList fromModel(Model model) {
		boolean hasFields = false;
    	ChatBannedList message = new ChatBannedList();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public List<ChatBanned> getChatBannedList() {
		if(this.chatBanned == null)
            this.chatBanned = new ArrayList<ChatBanned>();
		return chatBanned;
	}

	public ChatBannedList setChatBannedList(List<ChatBanned> chatBanned) {
		this.chatBanned = chatBanned;
		return this;
	}

	public ChatBanned getChatBanned(int index)  {
        return chatBanned == null ? null : chatBanned.get(index);
    }

    public int getChatBannedCount()  {
        return chatBanned == null ? 0 : chatBanned.size();
    }

    public ChatBannedList addChatBanned(ChatBanned chatBanned)  {
        if(this.chatBanned == null)
            this.chatBanned = new ArrayList<ChatBanned>();
        this.chatBanned.add(chatBanned);
        return this;
    }
            	    	    	    	
    public ChatBannedList removeChatBannedByBanned_id(ChatBanned chatBanned)  {
    	if(this.chatBanned == null)
           return this;
            
       	Iterator<ChatBanned> itr = this.chatBanned.iterator();
       	while (itr.hasNext()) {
    	ChatBanned obj = itr.next();
    	
    	    		if (chatBanned.banned_id.equals(obj.banned_id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ChatBannedList removeChatBannedByChannelNname(ChatBanned chatBanned)  {
    	if(this.chatBanned == null)
           return this;
            
       	Iterator<ChatBanned> itr = this.chatBanned.iterator();
       	while (itr.hasNext()) {
    	ChatBanned obj = itr.next();
    	
    	    		if (chatBanned.channelNname.equals(obj.channelNname)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ChatBannedList removeChatBannedByReason(ChatBanned chatBanned)  {
    	if(this.chatBanned == null)
           return this;
            
       	Iterator<ChatBanned> itr = this.chatBanned.iterator();
       	while (itr.hasNext()) {
    	ChatBanned obj = itr.next();
    	
    	    		if (chatBanned.reason.equals(obj.reason)) {
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

    public Schema<ChatBannedList> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatBannedList newMessage()
    {
        return new ChatBannedList();
    }

    public Class<ChatBannedList> typeClass()
    {
        return ChatBannedList.class;
    }

    public String messageName()
    {
        return ChatBannedList.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatBannedList.class.getName();
    }

    public boolean isInitialized(ChatBannedList message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatBannedList message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.chatBanned == null)
                        message.chatBanned = new ArrayList<ChatBanned>();
                                        message.chatBanned.add(input.mergeObject(null, ChatBanned.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ChatBannedList message) throws IOException
    {
    	    	
    	    	
    	    	if(message.chatBanned != null)
        {
            for(ChatBanned chatBanned : message.chatBanned)
            {
                if( (ChatBanned) chatBanned != null) {
                   	    				output.writeObject(1, chatBanned, ChatBanned.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ChatBannedList");
    	    	//if(this.chatBanned != null) {
    		System.out.println("chatBanned="+this.chatBanned);
    	//}
    	    	System.out.println("END ChatBannedList");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "chatBanned";
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
    	    	__fieldMap.put("chatBanned", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatBannedList.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatBannedList parseFrom(byte[] bytes) {
	ChatBannedList message = new ChatBannedList();
	ProtobufIOUtil.mergeFrom(bytes, message, ChatBannedList.getSchema());
	return message;
}

public static ChatBannedList parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ChatBannedList message = new ChatBannedList();
	JsonIOUtil.mergeFrom(bytes, message, ChatBannedList.getSchema(), false);
	return message;
}

public ChatBannedList clone() {
	byte[] bytes = this.toByteArray();
	ChatBannedList chatBannedList = ChatBannedList.parseFrom(bytes);
	return chatBannedList;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatBannedList.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatBannedList> schema = ChatBannedList.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ChatBannedList.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ChatBannedList.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
