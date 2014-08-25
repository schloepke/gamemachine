
package GameMachine.Messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import GameMachine.Messages.Entity;
import com.game_machine.core.LocalLinkedBuffer;

import org.javalite.activejdbc.Model;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

public final class ChatChannels  implements Externalizable, Message<ChatChannels>, Schema<ChatChannels>

{

    public static Schema<ChatChannels> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatChannels getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatChannels DEFAULT_INSTANCE = new ChatChannels();

    public List<ChatChannel> chatChannel;

    public ChatChannels()
    {
        
    }

	public static void clearModel(Model model) {

    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static ChatChannels fromModel(Model model) {
		boolean hasFields = false;
    	ChatChannels message = new ChatChannels();

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public List<ChatChannel> getChatChannelList() {
		return chatChannel;
	}

	public ChatChannels setChatChannelList(List<ChatChannel> chatChannel) {
		this.chatChannel = chatChannel;
		return this;
	}

	public ChatChannel getChatChannel(int index)  {
        return chatChannel == null ? null : chatChannel.get(index);
    }

    public int getChatChannelCount()  {
        return chatChannel == null ? 0 : chatChannel.size();
    }

    public ChatChannels addChatChannel(ChatChannel chatChannel)  {
        if(this.chatChannel == null)
            this.chatChannel = new ArrayList<ChatChannel>();
        this.chatChannel.add(chatChannel);
        return this;
    }

    public ChatChannels removeChatChannelByName(ChatChannel chatChannel)  {
    	if(this.chatChannel == null)
           return this;
            
       	Iterator<ChatChannel> itr = this.chatChannel.iterator();
       	while (itr.hasNext()) {
    	ChatChannel obj = itr.next();

    		if (chatChannel.name.equals(obj.name)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ChatChannels removeChatChannelByFlags(ChatChannel chatChannel)  {
    	if(this.chatChannel == null)
           return this;
            
       	Iterator<ChatChannel> itr = this.chatChannel.iterator();
       	while (itr.hasNext()) {
    	ChatChannel obj = itr.next();

    		if (chatChannel.flags.equals(obj.flags)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ChatChannels removeChatChannelByInvite_id(ChatChannel chatChannel)  {
    	if(this.chatChannel == null)
           return this;
            
       	Iterator<ChatChannel> itr = this.chatChannel.iterator();
       	while (itr.hasNext()) {
    	ChatChannel obj = itr.next();

    		if (chatChannel.invite_id.equals(obj.invite_id)) {
    	
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

    public Schema<ChatChannels> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatChannels newMessage()
    {
        return new ChatChannels();
    }

    public Class<ChatChannels> typeClass()
    {
        return ChatChannels.class;
    }

    public String messageName()
    {
        return ChatChannels.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatChannels.class.getName();
    }

    public boolean isInitialized(ChatChannels message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatChannels message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:
            	
            		if(message.chatChannel == null)
                        message.chatChannel = new ArrayList<ChatChannel>();
                    
                    message.chatChannel.add(input.mergeObject(null, ChatChannel.getSchema()));
                    
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, ChatChannels message) throws IOException
    {

    	if(message.chatChannel != null)
        {
            for(ChatChannel chatChannel : message.chatChannel)
            {
                if(chatChannel != null) {
                   	
    				output.writeObject(1, chatChannel, ChatChannel.getSchema(), true);
    				
    			}
            }
        }

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "chatChannel";
        	
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
    	
    	__fieldMap.put("chatChannel", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatChannels.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatChannels parseFrom(byte[] bytes) {
	ChatChannels message = new ChatChannels();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ChatChannels.class));
	return message;
}

public ChatChannels clone() {
	byte[] bytes = this.toByteArray();
	ChatChannels chatChannels = ChatChannels.parseFrom(bytes);
	return chatChannels;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatChannels.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatChannels> schema = RuntimeSchema.getSchema(ChatChannels.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ChatChannels.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(ChatChannels.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
