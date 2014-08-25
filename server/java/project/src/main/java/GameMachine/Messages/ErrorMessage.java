
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

public final class ErrorMessage  implements Externalizable, Message<ErrorMessage>, Schema<ErrorMessage>

{

    public static Schema<ErrorMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ErrorMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ErrorMessage DEFAULT_INSTANCE = new ErrorMessage();

		public String code;

		public String message;

    public ErrorMessage()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("error_message_code",null);

    	model.set("error_message_message",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (code != null) {
    		model.setString("error_message_code",code);
    	}

    	if (message != null) {
    		model.setString("error_message_message",message);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static ErrorMessage fromModel(Model model) {
		boolean hasFields = false;
    	ErrorMessage message = new ErrorMessage();

    	String codeField = model.getString("error_message_code");
    	if (codeField != null) {
    		message.setCode(codeField);
    		hasFields = true;
    	}

    	String messageField = model.getString("error_message_message");
    	if (messageField != null) {
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
		return this;
	}
	
	public Boolean hasCode()  {
        return code == null ? false : true;
    }

	public String getMessage() {
		return message;
	}
	
	public ErrorMessage setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public Boolean hasMessage()  {
        return message == null ? false : true;
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

    	if(message.code == null)
            throw new UninitializedMessageException(message);

    	if(message.code != null)
            output.writeString(1, message.code, false);

    	if(message.message == null)
            throw new UninitializedMessageException(message);

    	if(message.message != null)
            output.writeString(2, message.message, false);

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
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ErrorMessage.class));
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

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ErrorMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ErrorMessage> schema = RuntimeSchema.getSchema(ErrorMessage.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ErrorMessage.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(ErrorMessage.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
