
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

public final class GetNeighbors  implements Externalizable, Message<GetNeighbors>, Schema<GetNeighbors>

{

    public static Schema<GetNeighbors> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GetNeighbors getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GetNeighbors DEFAULT_INSTANCE = new GetNeighbors();

		public Integer search_radius;

		public Vector3 vector3;

		public String neighborType;

		public String gridName;

    public GetNeighbors()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("get_neighbors_search_radius",null);

    	model.set("get_neighbors_neighbor_type",null);

    	model.set("get_neighbors_grid_name",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (search_radius != null) {
    		model.setInteger("get_neighbors_search_radius",search_radius);
    	}

    	if (neighborType != null) {
    		model.setString("get_neighbors_neighbor_type",neighborType);
    	}

    	if (gridName != null) {
    		model.setString("get_neighbors_grid_name",gridName);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static GetNeighbors fromModel(Model model) {
		boolean hasFields = false;
    	GetNeighbors message = new GetNeighbors();

    	Integer search_radiusField = model.getInteger("get_neighbors_search_radius");
    	if (search_radiusField != null) {
    		message.setSearch_radius(search_radiusField);
    		hasFields = true;
    	}

    	String neighborTypeField = model.getString("get_neighbors_neighbor_type");
    	if (neighborTypeField != null) {
    		message.setNeighborType(neighborTypeField);
    		hasFields = true;
    	}

    	String gridNameField = model.getString("get_neighbors_grid_name");
    	if (gridNameField != null) {
    		message.setGridName(gridNameField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public Integer getSearch_radius() {
		return search_radius;
	}
	
	public GetNeighbors setSearch_radius(Integer search_radius) {
		this.search_radius = search_radius;
		return this;
	}
	
	public Boolean hasSearch_radius()  {
        return search_radius == null ? false : true;
    }

	public Vector3 getVector3() {
		return vector3;
	}
	
	public GetNeighbors setVector3(Vector3 vector3) {
		this.vector3 = vector3;
		return this;
	}
	
	public Boolean hasVector3()  {
        return vector3 == null ? false : true;
    }

	public String getNeighborType() {
		return neighborType;
	}
	
	public GetNeighbors setNeighborType(String neighborType) {
		this.neighborType = neighborType;
		return this;
	}
	
	public Boolean hasNeighborType()  {
        return neighborType == null ? false : true;
    }

	public String getGridName() {
		return gridName;
	}
	
	public GetNeighbors setGridName(String gridName) {
		this.gridName = gridName;
		return this;
	}
	
	public Boolean hasGridName()  {
        return gridName == null ? false : true;
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

    public Schema<GetNeighbors> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GetNeighbors newMessage()
    {
        return new GetNeighbors();
    }

    public Class<GetNeighbors> typeClass()
    {
        return GetNeighbors.class;
    }

    public String messageName()
    {
        return GetNeighbors.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GetNeighbors.class.getName();
    }

    public boolean isInitialized(GetNeighbors message)
    {
        return true;
    }

    public void mergeFrom(Input input, GetNeighbors message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 2:

                	message.search_radius = input.readUInt32();
                	break;

            	case 3:

                	message.vector3 = input.mergeObject(message.vector3, Vector3.getSchema());
                    break;

            	case 4:

                	message.neighborType = input.readString();
                	break;

            	case 5:

                	message.gridName = input.readString();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, GetNeighbors message) throws IOException
    {

    	if(message.search_radius != null)
            output.writeUInt32(2, message.search_radius, false);

    	if(message.vector3 == null)
            throw new UninitializedMessageException(message);

    	if(message.vector3 != null)
    		output.writeObject(3, message.vector3, Vector3.getSchema(), false);

    	if(message.neighborType != null)
            output.writeString(4, message.neighborType, false);

    	if(message.gridName != null)
            output.writeString(5, message.gridName, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 2: return "search_radius";
        	
        	case 3: return "vector3";
        	
        	case 4: return "neighborType";
        	
        	case 5: return "gridName";
        	
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
    	
    	__fieldMap.put("search_radius", 2);
    	
    	__fieldMap.put("vector3", 3);
    	
    	__fieldMap.put("neighborType", 4);
    	
    	__fieldMap.put("gridName", 5);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GetNeighbors.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GetNeighbors parseFrom(byte[] bytes) {
	GetNeighbors message = new GetNeighbors();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(GetNeighbors.class));
	return message;
}

public GetNeighbors clone() {
	byte[] bytes = this.toByteArray();
	GetNeighbors getNeighbors = GetNeighbors.parseFrom(bytes);
	return getNeighbors;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GetNeighbors.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GetNeighbors> schema = RuntimeSchema.getSchema(GetNeighbors.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(GetNeighbors.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(GetNeighbors.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
