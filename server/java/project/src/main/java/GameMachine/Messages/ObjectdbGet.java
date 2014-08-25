
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

public final class ObjectdbGet  implements Externalizable, Message<ObjectdbGet>, Schema<ObjectdbGet>

{

    public static Schema<ObjectdbGet> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbGet getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbGet DEFAULT_INSTANCE = new ObjectdbGet();

		public String entityId;

		public String playerId;

    public ObjectdbGet()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("objectdb_get_entity_id",null);

    	model.set("objectdb_get_player_id",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (entityId != null) {
    		model.setString("objectdb_get_entity_id",entityId);
    	}

    	if (playerId != null) {
    		model.setString("objectdb_get_player_id",playerId);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static ObjectdbGet fromModel(Model model) {
		boolean hasFields = false;
    	ObjectdbGet message = new ObjectdbGet();

    	String entityIdField = model.getString("objectdb_get_entity_id");
    	if (entityIdField != null) {
    		message.setEntityId(entityIdField);
    		hasFields = true;
    	}

    	String playerIdField = model.getString("objectdb_get_player_id");
    	if (playerIdField != null) {
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public String getEntityId() {
		return entityId;
	}
	
	public ObjectdbGet setEntityId(String entityId) {
		this.entityId = entityId;
		return this;
	}
	
	public Boolean hasEntityId()  {
        return entityId == null ? false : true;
    }

	public String getPlayerId() {
		return playerId;
	}
	
	public ObjectdbGet setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;
	}
	
	public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
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

    public Schema<ObjectdbGet> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbGet newMessage()
    {
        return new ObjectdbGet();
    }

    public Class<ObjectdbGet> typeClass()
    {
        return ObjectdbGet.class;
    }

    public String messageName()
    {
        return ObjectdbGet.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbGet.class.getName();
    }

    public boolean isInitialized(ObjectdbGet message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbGet message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.entityId = input.readString();
                	break;

            	case 2:

                	message.playerId = input.readString();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, ObjectdbGet message) throws IOException
    {

    	if(message.entityId == null)
            throw new UninitializedMessageException(message);

    	if(message.entityId != null)
            output.writeString(1, message.entityId, false);

    	if(message.playerId != null)
            output.writeString(2, message.playerId, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "entityId";
        	
        	case 2: return "playerId";
        	
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
    	
    	__fieldMap.put("entityId", 1);
    	
    	__fieldMap.put("playerId", 2);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbGet.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbGet parseFrom(byte[] bytes) {
	ObjectdbGet message = new ObjectdbGet();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ObjectdbGet.class));
	return message;
}

public ObjectdbGet clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbGet objectdbGet = ObjectdbGet.parseFrom(bytes);
	return objectdbGet;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbGet.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ObjectdbGet> schema = RuntimeSchema.getSchema(ObjectdbGet.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ObjectdbGet.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(ObjectdbGet.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
