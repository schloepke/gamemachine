
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

public final class Polygon  implements Externalizable, Message<Polygon>, Schema<Polygon>

{

    public static Schema<Polygon> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Polygon getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Polygon DEFAULT_INSTANCE = new Polygon();

    public List<Vector3> vertex;

    public Polygon()
    {
        
    }

	public static void clearModel(Model model) {

    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static Polygon fromModel(Model model) {
		boolean hasFields = false;
    	Polygon message = new Polygon();

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public List<Vector3> getVertexList() {
		return vertex;
	}

	public Polygon setVertexList(List<Vector3> vertex) {
		this.vertex = vertex;
		return this;
	}

	public Vector3 getVertex(int index)  {
        return vertex == null ? null : vertex.get(index);
    }

    public int getVertexCount()  {
        return vertex == null ? 0 : vertex.size();
    }

    public Polygon addVertex(Vector3 vertex)  {
        if(this.vertex == null)
            this.vertex = new ArrayList<Vector3>();
        this.vertex.add(vertex);
        return this;
    }

    public Polygon removeVertexByX(Vector3 vertex)  {
    	if(this.vertex == null)
           return this;
            
       	Iterator<Vector3> itr = this.vertex.iterator();
       	while (itr.hasNext()) {
    	Vector3 obj = itr.next();

    		if (vertex.x.equals(obj.x)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public Polygon removeVertexByY(Vector3 vertex)  {
    	if(this.vertex == null)
           return this;
            
       	Iterator<Vector3> itr = this.vertex.iterator();
       	while (itr.hasNext()) {
    	Vector3 obj = itr.next();

    		if (vertex.y.equals(obj.y)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public Polygon removeVertexByZ(Vector3 vertex)  {
    	if(this.vertex == null)
           return this;
            
       	Iterator<Vector3> itr = this.vertex.iterator();
       	while (itr.hasNext()) {
    	Vector3 obj = itr.next();

    		if (vertex.z.equals(obj.z)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public Polygon removeVertexByXi(Vector3 vertex)  {
    	if(this.vertex == null)
           return this;
            
       	Iterator<Vector3> itr = this.vertex.iterator();
       	while (itr.hasNext()) {
    	Vector3 obj = itr.next();

    		if (vertex.xi.equals(obj.xi)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public Polygon removeVertexByYi(Vector3 vertex)  {
    	if(this.vertex == null)
           return this;
            
       	Iterator<Vector3> itr = this.vertex.iterator();
       	while (itr.hasNext()) {
    	Vector3 obj = itr.next();

    		if (vertex.yi.equals(obj.yi)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public Polygon removeVertexByZi(Vector3 vertex)  {
    	if(this.vertex == null)
           return this;
            
       	Iterator<Vector3> itr = this.vertex.iterator();
       	while (itr.hasNext()) {
    	Vector3 obj = itr.next();

    		if (vertex.zi.equals(obj.zi)) {
    	
      			itr.remove();
    		}
		}
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

    public Schema<Polygon> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Polygon newMessage()
    {
        return new Polygon();
    }

    public Class<Polygon> typeClass()
    {
        return Polygon.class;
    }

    public String messageName()
    {
        return Polygon.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Polygon.class.getName();
    }

    public boolean isInitialized(Polygon message)
    {
        return true;
    }

    public void mergeFrom(Input input, Polygon message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:
            	
            		if(message.vertex == null)
                        message.vertex = new ArrayList<Vector3>();
                    
                    message.vertex.add(input.mergeObject(null, Vector3.getSchema()));
                    
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Polygon message) throws IOException
    {

    	if(message.vertex != null)
        {
            for(Vector3 vertex : message.vertex)
            {
                if(vertex != null) {
                   	
    				output.writeObject(1, vertex, Vector3.getSchema(), true);
    				
    			}
            }
        }

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "vertex";
        	
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
    	
    	__fieldMap.put("vertex", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Polygon.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Polygon parseFrom(byte[] bytes) {
	Polygon message = new Polygon();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Polygon.class));
	return message;
}

public Polygon clone() {
	byte[] bytes = this.toByteArray();
	Polygon polygon = Polygon.parseFrom(bytes);
	return polygon;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Polygon.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Polygon> schema = RuntimeSchema.getSchema(Polygon.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Polygon.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(Polygon.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
