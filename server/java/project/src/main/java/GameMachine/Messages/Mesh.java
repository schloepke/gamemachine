
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

public final class Mesh  implements Externalizable, Message<Mesh>, Schema<Mesh>

{

    public static Schema<Mesh> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Mesh getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Mesh DEFAULT_INSTANCE = new Mesh();

    public List<Polygon> polygon;

    public Mesh()
    {
        
    }

	public static void clearModel(Model model) {

    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static Mesh fromModel(Model model) {
		boolean hasFields = false;
    	Mesh message = new Mesh();

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public List<Polygon> getPolygonList() {
		return polygon;
	}

	public Mesh setPolygonList(List<Polygon> polygon) {
		this.polygon = polygon;
		return this;
	}

	public Polygon getPolygon(int index)  {
        return polygon == null ? null : polygon.get(index);
    }

    public int getPolygonCount()  {
        return polygon == null ? 0 : polygon.size();
    }

    public Mesh addPolygon(Polygon polygon)  {
        if(this.polygon == null)
            this.polygon = new ArrayList<Polygon>();
        this.polygon.add(polygon);
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

    public Schema<Mesh> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Mesh newMessage()
    {
        return new Mesh();
    }

    public Class<Mesh> typeClass()
    {
        return Mesh.class;
    }

    public String messageName()
    {
        return Mesh.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Mesh.class.getName();
    }

    public boolean isInitialized(Mesh message)
    {
        return true;
    }

    public void mergeFrom(Input input, Mesh message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:
            	
            		if(message.polygon == null)
                        message.polygon = new ArrayList<Polygon>();
                    
                    message.polygon.add(input.mergeObject(null, Polygon.getSchema()));
                    
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Mesh message) throws IOException
    {

    	if(message.polygon != null)
        {
            for(Polygon polygon : message.polygon)
            {
                if(polygon != null) {
                   	
    				output.writeObject(1, polygon, Polygon.getSchema(), true);
    				
    			}
            }
        }

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "polygon";
        	
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
    	
    	__fieldMap.put("polygon", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Mesh.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Mesh parseFrom(byte[] bytes) {
	Mesh message = new Mesh();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Mesh.class));
	return message;
}

public Mesh clone() {
	byte[] bytes = this.toByteArray();
	Mesh mesh = Mesh.parseFrom(bytes);
	return mesh;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Mesh.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Mesh> schema = RuntimeSchema.getSchema(Mesh.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Mesh.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(Mesh.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
