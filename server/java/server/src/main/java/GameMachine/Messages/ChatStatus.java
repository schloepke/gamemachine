
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
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

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

import com.game_machine.util.LocalLinkedBuffer;

import java.nio.charset.Charset;

import org.javalite.activejdbc.Model;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

@SuppressWarnings("unused")
public final class ChatStatus implements Externalizable, Message<ChatStatus>, Schema<ChatStatus>
{

    public static Schema<ChatStatus> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatStatus getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatStatus DEFAULT_INSTANCE = new ChatStatus();
    static final String defaultScope = ChatStatus.class.getSimpleName();

		public Integer notused;

    public ChatStatus()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("chat_status_notused",null);
    	
    }
    
	public void toModel(Model model) {

    	if (notused != null) {
    		model.setInteger("chat_status_notused",notused);
    	}

    }
    
	public static ChatStatus fromModel(Model model) {
		boolean hasFields = false;
    	ChatStatus message = new ChatStatus();

    	Integer notusedField = model.getInteger("chat_status_notused");
    	if (notusedField != null) {
    		message.setNotused(notusedField);
    		hasFields = true;
    	}

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

    public Boolean hasNotused()  {
        return notused == null ? false : true;
    }

	public Integer getNotused() {
		return notused;
	}
	
	public ChatStatus setNotused(Integer notused) {
		this.notused = notused;
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

    public Schema<ChatStatus> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatStatus newMessage()
    {
        return new ChatStatus();
    }

    public Class<ChatStatus> typeClass()
    {
        return ChatStatus.class;
    }

    public String messageName()
    {
        return ChatStatus.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatStatus.class.getName();
    }

    public boolean isInitialized(ChatStatus message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatStatus message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.notused = input.readInt32();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, ChatStatus message) throws IOException
    {

    	if(message.notused != null)
            output.writeInt32(1, message.notused, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "notused";
        	
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
    	
    	__fieldMap.put("notused", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatStatus.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatStatus parseFrom(byte[] bytes) {
	ChatStatus message = new ChatStatus();
	ProtobufIOUtil.mergeFrom(bytes, message, ChatStatus.getSchema());
	return message;
}

public static ChatStatus parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ChatStatus message = new ChatStatus();
	JsonIOUtil.mergeFrom(bytes, message, ChatStatus.getSchema(), false);
	return message;
}

public ChatStatus clone() {
	byte[] bytes = this.toByteArray();
	ChatStatus chatStatus = ChatStatus.parseFrom(bytes);
	return chatStatus;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatStatus.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ChatStatus> schema = ChatStatus.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ChatStatus.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ChatStatus.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
