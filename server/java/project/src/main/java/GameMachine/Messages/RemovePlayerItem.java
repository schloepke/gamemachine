
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

public final class RemovePlayerItem  implements Externalizable, Message<RemovePlayerItem>, Schema<RemovePlayerItem>

{

    public static Schema<RemovePlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static RemovePlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final RemovePlayerItem DEFAULT_INSTANCE = new RemovePlayerItem();

		public String id;

		public Integer quantity;

    public RemovePlayerItem()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("remove_player_item_id",null);

    	model.set("remove_player_item_quantity",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (id != null) {
    		model.setString("remove_player_item_id",id);
    	}

    	if (quantity != null) {
    		model.setInteger("remove_player_item_quantity",quantity);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static RemovePlayerItem fromModel(Model model) {
		boolean hasFields = false;
    	RemovePlayerItem message = new RemovePlayerItem();

    	String idField = model.getString("remove_player_item_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}

    	Integer quantityField = model.getInteger("remove_player_item_quantity");
    	if (quantityField != null) {
    		message.setQuantity(quantityField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public String getId() {
		return id;
	}
	
	public RemovePlayerItem setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }

	public Integer getQuantity() {
		return quantity;
	}
	
	public RemovePlayerItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}
	
	public Boolean hasQuantity()  {
        return quantity == null ? false : true;
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

    public Schema<RemovePlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public RemovePlayerItem newMessage()
    {
        return new RemovePlayerItem();
    }

    public Class<RemovePlayerItem> typeClass()
    {
        return RemovePlayerItem.class;
    }

    public String messageName()
    {
        return RemovePlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return RemovePlayerItem.class.getName();
    }

    public boolean isInitialized(RemovePlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, RemovePlayerItem message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.id = input.readString();
                	break;

            	case 2:

                	message.quantity = input.readInt32();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, RemovePlayerItem message) throws IOException
    {

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	if(message.id != null)
            output.writeString(1, message.id, false);

    	if(message.quantity == null)
            throw new UninitializedMessageException(message);

    	if(message.quantity != null)
            output.writeInt32(2, message.quantity, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "id";
        	
        	case 2: return "quantity";
        	
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
    	
    	__fieldMap.put("id", 1);
    	
    	__fieldMap.put("quantity", 2);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = RemovePlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static RemovePlayerItem parseFrom(byte[] bytes) {
	RemovePlayerItem message = new RemovePlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(RemovePlayerItem.class));
	return message;
}

public RemovePlayerItem clone() {
	byte[] bytes = this.toByteArray();
	RemovePlayerItem removePlayerItem = RemovePlayerItem.parseFrom(bytes);
	return removePlayerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, RemovePlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<RemovePlayerItem> schema = RuntimeSchema.getSchema(RemovePlayerItem.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(RemovePlayerItem.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(RemovePlayerItem.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
