
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

public final class Neighbor  implements Externalizable, Message<Neighbor>, Schema<Neighbor>

{

    public static Schema<Neighbor> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Neighbor getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Neighbor DEFAULT_INSTANCE = new Neighbor();

		public TrackData trackData;

		public Vector3 location;

		public String id;

    public Neighbor()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("neighbor_id",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (id != null) {
    		model.setString("neighbor_id",id);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static Neighbor fromModel(Model model) {
		boolean hasFields = false;
    	Neighbor message = new Neighbor();

    	String idField = model.getString("neighbor_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public TrackData getTrackData() {
		return trackData;
	}
	
	public Neighbor setTrackData(TrackData trackData) {
		this.trackData = trackData;
		return this;
	}
	
	public Boolean hasTrackData()  {
        return trackData == null ? false : true;
    }

	public Vector3 getLocation() {
		return location;
	}
	
	public Neighbor setLocation(Vector3 location) {
		this.location = location;
		return this;
	}
	
	public Boolean hasLocation()  {
        return location == null ? false : true;
    }

	public String getId() {
		return id;
	}
	
	public Neighbor setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
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

    public Schema<Neighbor> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Neighbor newMessage()
    {
        return new Neighbor();
    }

    public Class<Neighbor> typeClass()
    {
        return Neighbor.class;
    }

    public String messageName()
    {
        return Neighbor.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Neighbor.class.getName();
    }

    public boolean isInitialized(Neighbor message)
    {
        return true;
    }

    public void mergeFrom(Input input, Neighbor message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.trackData = input.mergeObject(message.trackData, TrackData.getSchema());
                    break;

            	case 2:

                	message.location = input.mergeObject(message.location, Vector3.getSchema());
                    break;

            	case 3:

                	message.id = input.readString();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Neighbor message) throws IOException
    {

    	if(message.trackData != null)
    		output.writeObject(1, message.trackData, TrackData.getSchema(), false);

    	if(message.location == null)
            throw new UninitializedMessageException(message);

    	if(message.location != null)
    		output.writeObject(2, message.location, Vector3.getSchema(), false);

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	if(message.id != null)
            output.writeString(3, message.id, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "trackData";
        	
        	case 2: return "location";
        	
        	case 3: return "id";
        	
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
    	
    	__fieldMap.put("trackData", 1);
    	
    	__fieldMap.put("location", 2);
    	
    	__fieldMap.put("id", 3);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Neighbor.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Neighbor parseFrom(byte[] bytes) {
	Neighbor message = new Neighbor();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Neighbor.class));
	return message;
}

public Neighbor clone() {
	byte[] bytes = this.toByteArray();
	Neighbor neighbor = Neighbor.parseFrom(bytes);
	return neighbor;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Neighbor.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Neighbor> schema = RuntimeSchema.getSchema(Neighbor.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Neighbor.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(Neighbor.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
