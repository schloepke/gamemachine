
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

public final class PlayerConnect  implements Externalizable, Message<PlayerConnect>, Schema<PlayerConnect>

{

    public static Schema<PlayerConnect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerConnect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerConnect DEFAULT_INSTANCE = new PlayerConnect();

    public PlayerConnect()
    {
        
    }

	public static void clearModel(Model model) {
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static PlayerConnect fromModel(Model model) {
		boolean hasFields = false;
    	PlayerConnect message = new PlayerConnect();
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
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

    public Schema<PlayerConnect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerConnect newMessage()
    {
        return new PlayerConnect();
    }

    public Class<PlayerConnect> typeClass()
    {
        return PlayerConnect.class;
    }

    public String messageName()
    {
        return PlayerConnect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerConnect.class.getName();
    }

    public boolean isInitialized(PlayerConnect message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerConnect message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, PlayerConnect message) throws IOException
    {

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
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
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerConnect.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerConnect parseFrom(byte[] bytes) {
	PlayerConnect message = new PlayerConnect();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(PlayerConnect.class));
	return message;
}

public PlayerConnect clone() {
	byte[] bytes = this.toByteArray();
	PlayerConnect playerConnect = PlayerConnect.parseFrom(bytes);
	return playerConnect;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerConnect.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerConnect> schema = RuntimeSchema.getSchema(PlayerConnect.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(PlayerConnect.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(PlayerConnect.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
