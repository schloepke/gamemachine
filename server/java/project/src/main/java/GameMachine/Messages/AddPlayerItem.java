
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

public final class AddPlayerItem  implements Externalizable, Message<AddPlayerItem>, Schema<AddPlayerItem>

{

    public static Schema<AddPlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static AddPlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final AddPlayerItem DEFAULT_INSTANCE = new AddPlayerItem();

		public PlayerItem playerItem;

    public AddPlayerItem()
    {
        
    }

	public static void clearModel(Model model) {

    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static AddPlayerItem fromModel(Model model) {
		boolean hasFields = false;
    	AddPlayerItem message = new AddPlayerItem();

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public PlayerItem getPlayerItem() {
		return playerItem;
	}
	
	public AddPlayerItem setPlayerItem(PlayerItem playerItem) {
		this.playerItem = playerItem;
		return this;
	}
	
	public Boolean hasPlayerItem()  {
        return playerItem == null ? false : true;
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

    public Schema<AddPlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public AddPlayerItem newMessage()
    {
        return new AddPlayerItem();
    }

    public Class<AddPlayerItem> typeClass()
    {
        return AddPlayerItem.class;
    }

    public String messageName()
    {
        return AddPlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return AddPlayerItem.class.getName();
    }

    public boolean isInitialized(AddPlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, AddPlayerItem message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.playerItem = input.mergeObject(message.playerItem, PlayerItem.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, AddPlayerItem message) throws IOException
    {

    	if(message.playerItem == null)
            throw new UninitializedMessageException(message);

    	if(message.playerItem != null)
    		output.writeObject(1, message.playerItem, PlayerItem.getSchema(), false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "playerItem";
        	
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
    	
    	__fieldMap.put("playerItem", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = AddPlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static AddPlayerItem parseFrom(byte[] bytes) {
	AddPlayerItem message = new AddPlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(AddPlayerItem.class));
	return message;
}

public AddPlayerItem clone() {
	byte[] bytes = this.toByteArray();
	AddPlayerItem addPlayerItem = AddPlayerItem.parseFrom(bytes);
	return addPlayerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, AddPlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<AddPlayerItem> schema = RuntimeSchema.getSchema(AddPlayerItem.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(AddPlayerItem.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(AddPlayerItem.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
