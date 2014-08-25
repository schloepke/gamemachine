
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

public final class ChatRegister  implements Externalizable, Message<ChatRegister>, Schema<ChatRegister>

{

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

	public static void clearModel(Model model) {

    	model.set("chat_register_chat_id",null);

    	model.set("chat_register_register_as",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (chatId != null) {
    		model.setString("chat_register_chat_id",chatId);
    	}

    	if (registerAs != null) {
    		model.setString("chat_register_register_as",registerAs);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static ChatRegister fromModel(Model model) {
		boolean hasFields = false;
    	ChatRegister message = new ChatRegister();

    	String chatIdField = model.getString("chat_register_chat_id");
    	if (chatIdField != null) {
    		message.setChatId(chatIdField);
    		hasFields = true;
    	}

    	String registerAsField = model.getString("chat_register_register_as");
    	if (registerAsField != null) {
    		message.setRegisterAs(registerAsField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public String getChatId() {
		return chatId;
	}
	
	public ChatRegister setChatId(String chatId) {
		this.chatId = chatId;
		return this;
	}
	
	public Boolean hasChatId()  {
        return chatId == null ? false : true;
    }

	public String getRegisterAs() {
		return registerAs;
	}
	
	public ChatRegister setRegisterAs(String registerAs) {
		this.registerAs = registerAs;
		return this;
	}
	
	public Boolean hasRegisterAs()  {
        return registerAs == null ? false : true;
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
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ChatRegister.class));
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

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatRegister.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatRegister> schema = RuntimeSchema.getSchema(ChatRegister.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ChatRegister.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(ChatRegister.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
