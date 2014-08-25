
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

public final class Quaternion  implements Externalizable, Message<Quaternion>, Schema<Quaternion>

{

    public static Schema<Quaternion> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Quaternion getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Quaternion DEFAULT_INSTANCE = new Quaternion();

		public Float w;

		public Float x;

		public Float y;

		public Float z;

		public Integer wi;

		public Integer xi;

		public Integer yi;

		public Integer zi;

    public Quaternion()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("quaternion_w",null);

    	model.set("quaternion_x",null);

    	model.set("quaternion_y",null);

    	model.set("quaternion_z",null);

    	model.set("quaternion_wi",null);

    	model.set("quaternion_xi",null);

    	model.set("quaternion_yi",null);

    	model.set("quaternion_zi",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (w != null) {
    		model.setFloat("quaternion_w",w);
    	}

    	if (x != null) {
    		model.setFloat("quaternion_x",x);
    	}

    	if (y != null) {
    		model.setFloat("quaternion_y",y);
    	}

    	if (z != null) {
    		model.setFloat("quaternion_z",z);
    	}

    	if (wi != null) {
    		model.setInteger("quaternion_wi",wi);
    	}

    	if (xi != null) {
    		model.setInteger("quaternion_xi",xi);
    	}

    	if (yi != null) {
    		model.setInteger("quaternion_yi",yi);
    	}

    	if (zi != null) {
    		model.setInteger("quaternion_zi",zi);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static Quaternion fromModel(Model model) {
		boolean hasFields = false;
    	Quaternion message = new Quaternion();

    	Float wField = model.getFloat("quaternion_w");
    	if (wField != null) {
    		message.setW(wField);
    		hasFields = true;
    	}

    	Float xField = model.getFloat("quaternion_x");
    	if (xField != null) {
    		message.setX(xField);
    		hasFields = true;
    	}

    	Float yField = model.getFloat("quaternion_y");
    	if (yField != null) {
    		message.setY(yField);
    		hasFields = true;
    	}

    	Float zField = model.getFloat("quaternion_z");
    	if (zField != null) {
    		message.setZ(zField);
    		hasFields = true;
    	}

    	Integer wiField = model.getInteger("quaternion_wi");
    	if (wiField != null) {
    		message.setWi(wiField);
    		hasFields = true;
    	}

    	Integer xiField = model.getInteger("quaternion_xi");
    	if (xiField != null) {
    		message.setXi(xiField);
    		hasFields = true;
    	}

    	Integer yiField = model.getInteger("quaternion_yi");
    	if (yiField != null) {
    		message.setYi(yiField);
    		hasFields = true;
    	}

    	Integer ziField = model.getInteger("quaternion_zi");
    	if (ziField != null) {
    		message.setZi(ziField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public Float getW() {
		return w;
	}
	
	public Quaternion setW(Float w) {
		this.w = w;
		return this;
	}
	
	public Boolean hasW()  {
        return w == null ? false : true;
    }

	public Float getX() {
		return x;
	}
	
	public Quaternion setX(Float x) {
		this.x = x;
		return this;
	}
	
	public Boolean hasX()  {
        return x == null ? false : true;
    }

	public Float getY() {
		return y;
	}
	
	public Quaternion setY(Float y) {
		this.y = y;
		return this;
	}
	
	public Boolean hasY()  {
        return y == null ? false : true;
    }

	public Float getZ() {
		return z;
	}
	
	public Quaternion setZ(Float z) {
		this.z = z;
		return this;
	}
	
	public Boolean hasZ()  {
        return z == null ? false : true;
    }

	public Integer getWi() {
		return wi;
	}
	
	public Quaternion setWi(Integer wi) {
		this.wi = wi;
		return this;
	}
	
	public Boolean hasWi()  {
        return wi == null ? false : true;
    }

	public Integer getXi() {
		return xi;
	}
	
	public Quaternion setXi(Integer xi) {
		this.xi = xi;
		return this;
	}
	
	public Boolean hasXi()  {
        return xi == null ? false : true;
    }

	public Integer getYi() {
		return yi;
	}
	
	public Quaternion setYi(Integer yi) {
		this.yi = yi;
		return this;
	}
	
	public Boolean hasYi()  {
        return yi == null ? false : true;
    }

	public Integer getZi() {
		return zi;
	}
	
	public Quaternion setZi(Integer zi) {
		this.zi = zi;
		return this;
	}
	
	public Boolean hasZi()  {
        return zi == null ? false : true;
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

    public Schema<Quaternion> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Quaternion newMessage()
    {
        return new Quaternion();
    }

    public Class<Quaternion> typeClass()
    {
        return Quaternion.class;
    }

    public String messageName()
    {
        return Quaternion.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Quaternion.class.getName();
    }

    public boolean isInitialized(Quaternion message)
    {
        return true;
    }

    public void mergeFrom(Input input, Quaternion message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.w = input.readFloat();
                	break;

            	case 2:

                	message.x = input.readFloat();
                	break;

            	case 3:

                	message.y = input.readFloat();
                	break;

            	case 4:

                	message.z = input.readFloat();
                	break;

            	case 5:

                	message.wi = input.readInt32();
                	break;

            	case 6:

                	message.xi = input.readInt32();
                	break;

            	case 7:

                	message.yi = input.readInt32();
                	break;

            	case 8:

                	message.zi = input.readInt32();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Quaternion message) throws IOException
    {

    	if(message.w != null)
            output.writeFloat(1, message.w, false);

    	if(message.x != null)
            output.writeFloat(2, message.x, false);

    	if(message.y != null)
            output.writeFloat(3, message.y, false);

    	if(message.z != null)
            output.writeFloat(4, message.z, false);

    	if(message.wi != null)
            output.writeInt32(5, message.wi, false);

    	if(message.xi != null)
            output.writeInt32(6, message.xi, false);

    	if(message.yi != null)
            output.writeInt32(7, message.yi, false);

    	if(message.zi != null)
            output.writeInt32(8, message.zi, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "w";
        	
        	case 2: return "x";
        	
        	case 3: return "y";
        	
        	case 4: return "z";
        	
        	case 5: return "wi";
        	
        	case 6: return "xi";
        	
        	case 7: return "yi";
        	
        	case 8: return "zi";
        	
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
    	
    	__fieldMap.put("w", 1);
    	
    	__fieldMap.put("x", 2);
    	
    	__fieldMap.put("y", 3);
    	
    	__fieldMap.put("z", 4);
    	
    	__fieldMap.put("wi", 5);
    	
    	__fieldMap.put("xi", 6);
    	
    	__fieldMap.put("yi", 7);
    	
    	__fieldMap.put("zi", 8);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Quaternion.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Quaternion parseFrom(byte[] bytes) {
	Quaternion message = new Quaternion();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Quaternion.class));
	return message;
}

public Quaternion clone() {
	byte[] bytes = this.toByteArray();
	Quaternion quaternion = Quaternion.parseFrom(bytes);
	return quaternion;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Quaternion.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Quaternion> schema = RuntimeSchema.getSchema(Quaternion.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Quaternion.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(Quaternion.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
