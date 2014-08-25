
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

public final class TrackData  implements Externalizable, Message<TrackData>, Schema<TrackData>

{

    public static Schema<TrackData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackData DEFAULT_INSTANCE = new TrackData();

		public Float speed;

		public Float velocity;

		public Vector3 direction;

		public String json;

		public String id;

		public Float x;

		public Float y;

		public Float z;

		public String entityType;

		public String neighborEntityType;

		public Integer action;

    public TrackData()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("track_data_speed",null);

    	model.set("track_data_velocity",null);

    	model.set("track_data_json",null);

    	model.set("track_data_id",null);

    	model.set("track_data_x",null);

    	model.set("track_data_y",null);

    	model.set("track_data_z",null);

    	model.set("track_data_entity_type",null);

    	model.set("track_data_neighbor_entity_type",null);

    	model.set("track_data_action",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (speed != null) {
    		model.setFloat("track_data_speed",speed);
    	}

    	if (velocity != null) {
    		model.setFloat("track_data_velocity",velocity);
    	}

    	if (json != null) {
    		model.setString("track_data_json",json);
    	}

    	if (id != null) {
    		model.setString("track_data_id",id);
    	}

    	if (x != null) {
    		model.setFloat("track_data_x",x);
    	}

    	if (y != null) {
    		model.setFloat("track_data_y",y);
    	}

    	if (z != null) {
    		model.setFloat("track_data_z",z);
    	}

    	if (entityType != null) {
    		model.setString("track_data_entity_type",entityType);
    	}

    	if (neighborEntityType != null) {
    		model.setString("track_data_neighbor_entity_type",neighborEntityType);
    	}

    	if (action != null) {
    		model.setInteger("track_data_action",action);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static TrackData fromModel(Model model) {
		boolean hasFields = false;
    	TrackData message = new TrackData();

    	Float speedField = model.getFloat("track_data_speed");
    	if (speedField != null) {
    		message.setSpeed(speedField);
    		hasFields = true;
    	}

    	Float velocityField = model.getFloat("track_data_velocity");
    	if (velocityField != null) {
    		message.setVelocity(velocityField);
    		hasFields = true;
    	}

    	String jsonField = model.getString("track_data_json");
    	if (jsonField != null) {
    		message.setJson(jsonField);
    		hasFields = true;
    	}

    	String idField = model.getString("track_data_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}

    	Float xField = model.getFloat("track_data_x");
    	if (xField != null) {
    		message.setX(xField);
    		hasFields = true;
    	}

    	Float yField = model.getFloat("track_data_y");
    	if (yField != null) {
    		message.setY(yField);
    		hasFields = true;
    	}

    	Float zField = model.getFloat("track_data_z");
    	if (zField != null) {
    		message.setZ(zField);
    		hasFields = true;
    	}

    	String entityTypeField = model.getString("track_data_entity_type");
    	if (entityTypeField != null) {
    		message.setEntityType(entityTypeField);
    		hasFields = true;
    	}

    	String neighborEntityTypeField = model.getString("track_data_neighbor_entity_type");
    	if (neighborEntityTypeField != null) {
    		message.setNeighborEntityType(neighborEntityTypeField);
    		hasFields = true;
    	}

    	Integer actionField = model.getInteger("track_data_action");
    	if (actionField != null) {
    		message.setAction(actionField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public Float getSpeed() {
		return speed;
	}
	
	public TrackData setSpeed(Float speed) {
		this.speed = speed;
		return this;
	}
	
	public Boolean hasSpeed()  {
        return speed == null ? false : true;
    }

	public Float getVelocity() {
		return velocity;
	}
	
	public TrackData setVelocity(Float velocity) {
		this.velocity = velocity;
		return this;
	}
	
	public Boolean hasVelocity()  {
        return velocity == null ? false : true;
    }

	public Vector3 getDirection() {
		return direction;
	}
	
	public TrackData setDirection(Vector3 direction) {
		this.direction = direction;
		return this;
	}
	
	public Boolean hasDirection()  {
        return direction == null ? false : true;
    }

	public String getJson() {
		return json;
	}
	
	public TrackData setJson(String json) {
		this.json = json;
		return this;
	}
	
	public Boolean hasJson()  {
        return json == null ? false : true;
    }

	public String getId() {
		return id;
	}
	
	public TrackData setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }

	public Float getX() {
		return x;
	}
	
	public TrackData setX(Float x) {
		this.x = x;
		return this;
	}
	
	public Boolean hasX()  {
        return x == null ? false : true;
    }

	public Float getY() {
		return y;
	}
	
	public TrackData setY(Float y) {
		this.y = y;
		return this;
	}
	
	public Boolean hasY()  {
        return y == null ? false : true;
    }

	public Float getZ() {
		return z;
	}
	
	public TrackData setZ(Float z) {
		this.z = z;
		return this;
	}
	
	public Boolean hasZ()  {
        return z == null ? false : true;
    }

	public String getEntityType() {
		return entityType;
	}
	
	public TrackData setEntityType(String entityType) {
		this.entityType = entityType;
		return this;
	}
	
	public Boolean hasEntityType()  {
        return entityType == null ? false : true;
    }

	public String getNeighborEntityType() {
		return neighborEntityType;
	}
	
	public TrackData setNeighborEntityType(String neighborEntityType) {
		this.neighborEntityType = neighborEntityType;
		return this;
	}
	
	public Boolean hasNeighborEntityType()  {
        return neighborEntityType == null ? false : true;
    }

	public Integer getAction() {
		return action;
	}
	
	public TrackData setAction(Integer action) {
		this.action = action;
		return this;
	}
	
	public Boolean hasAction()  {
        return action == null ? false : true;
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

    public Schema<TrackData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackData newMessage()
    {
        return new TrackData();
    }

    public Class<TrackData> typeClass()
    {
        return TrackData.class;
    }

    public String messageName()
    {
        return TrackData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackData.class.getName();
    }

    public boolean isInitialized(TrackData message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.speed = input.readFloat();
                	break;

            	case 2:

                	message.velocity = input.readFloat();
                	break;

            	case 3:

                	message.direction = input.mergeObject(message.direction, Vector3.getSchema());
                    break;

            	case 4:

                	message.json = input.readString();
                	break;

            	case 5:

                	message.id = input.readString();
                	break;

            	case 6:

                	message.x = input.readFloat();
                	break;

            	case 7:

                	message.y = input.readFloat();
                	break;

            	case 8:

                	message.z = input.readFloat();
                	break;

            	case 9:

                	message.entityType = input.readString();
                	break;

            	case 11:

                	message.neighborEntityType = input.readString();
                	break;

            	case 12:

                	message.action = input.readInt32();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, TrackData message) throws IOException
    {

    	if(message.speed != null)
            output.writeFloat(1, message.speed, false);

    	if(message.velocity != null)
            output.writeFloat(2, message.velocity, false);

    	if(message.direction != null)
    		output.writeObject(3, message.direction, Vector3.getSchema(), false);

    	if(message.json != null)
            output.writeString(4, message.json, false);

    	if(message.id != null)
            output.writeString(5, message.id, false);

    	if(message.x != null)
            output.writeFloat(6, message.x, false);

    	if(message.y != null)
            output.writeFloat(7, message.y, false);

    	if(message.z != null)
            output.writeFloat(8, message.z, false);

    	if(message.entityType != null)
            output.writeString(9, message.entityType, false);

    	if(message.neighborEntityType != null)
            output.writeString(11, message.neighborEntityType, false);

    	if(message.action != null)
            output.writeInt32(12, message.action, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "speed";
        	
        	case 2: return "velocity";
        	
        	case 3: return "direction";
        	
        	case 4: return "json";
        	
        	case 5: return "id";
        	
        	case 6: return "x";
        	
        	case 7: return "y";
        	
        	case 8: return "z";
        	
        	case 9: return "entityType";
        	
        	case 11: return "neighborEntityType";
        	
        	case 12: return "action";
        	
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
    	
    	__fieldMap.put("speed", 1);
    	
    	__fieldMap.put("velocity", 2);
    	
    	__fieldMap.put("direction", 3);
    	
    	__fieldMap.put("json", 4);
    	
    	__fieldMap.put("id", 5);
    	
    	__fieldMap.put("x", 6);
    	
    	__fieldMap.put("y", 7);
    	
    	__fieldMap.put("z", 8);
    	
    	__fieldMap.put("entityType", 9);
    	
    	__fieldMap.put("neighborEntityType", 11);
    	
    	__fieldMap.put("action", 12);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackData parseFrom(byte[] bytes) {
	TrackData message = new TrackData();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(TrackData.class));
	return message;
}

public TrackData clone() {
	byte[] bytes = this.toByteArray();
	TrackData trackData = TrackData.parseFrom(bytes);
	return trackData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TrackData> schema = RuntimeSchema.getSchema(TrackData.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(TrackData.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(TrackData.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
