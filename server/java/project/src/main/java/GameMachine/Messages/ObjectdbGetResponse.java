
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

public final class ObjectdbGetResponse  implements Externalizable, Message<ObjectdbGetResponse>, Schema<ObjectdbGetResponse>

{

    public static Schema<ObjectdbGetResponse> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbGetResponse getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbGetResponse DEFAULT_INSTANCE = new ObjectdbGetResponse();

		public Boolean entityFound;

    public ObjectdbGetResponse()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("objectdb_get_response_entity_found",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (entityFound != null) {
    		model.setBoolean("objectdb_get_response_entity_found",entityFound);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static ObjectdbGetResponse fromModel(Model model) {
		boolean hasFields = false;
    	ObjectdbGetResponse message = new ObjectdbGetResponse();

    	Boolean entityFoundField = model.getBoolean("objectdb_get_response_entity_found");
    	if (entityFoundField != null) {
    		message.setEntityFound(entityFoundField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public Boolean getEntityFound() {
		return entityFound;
	}
	
	public ObjectdbGetResponse setEntityFound(Boolean entityFound) {
		this.entityFound = entityFound;
		return this;
	}
	
	public Boolean hasEntityFound()  {
        return entityFound == null ? false : true;
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

    public Schema<ObjectdbGetResponse> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbGetResponse newMessage()
    {
        return new ObjectdbGetResponse();
    }

    public Class<ObjectdbGetResponse> typeClass()
    {
        return ObjectdbGetResponse.class;
    }

    public String messageName()
    {
        return ObjectdbGetResponse.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbGetResponse.class.getName();
    }

    public boolean isInitialized(ObjectdbGetResponse message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbGetResponse message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.entityFound = input.readBool();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, ObjectdbGetResponse message) throws IOException
    {

    	if(message.entityFound == null)
            throw new UninitializedMessageException(message);

    	if(message.entityFound != null)
            output.writeBool(1, message.entityFound, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "entityFound";
        	
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
    	
    	__fieldMap.put("entityFound", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbGetResponse.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbGetResponse parseFrom(byte[] bytes) {
	ObjectdbGetResponse message = new ObjectdbGetResponse();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ObjectdbGetResponse.class));
	return message;
}

public ObjectdbGetResponse clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbGetResponse objectdbGetResponse = ObjectdbGetResponse.parseFrom(bytes);
	return objectdbGetResponse;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbGetResponse.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ObjectdbGetResponse> schema = RuntimeSchema.getSchema(ObjectdbGetResponse.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ObjectdbGetResponse.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(ObjectdbGetResponse.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
