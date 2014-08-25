
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

public final class Weapon  implements Externalizable, Message<Weapon>, Schema<Weapon>

{

    public static Schema<Weapon> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Weapon getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Weapon DEFAULT_INSTANCE = new Weapon();

		public Integer attack;

		public Integer delay;

    public Weapon()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("weapon_attack",null);

    	model.set("weapon_delay",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (attack != null) {
    		model.setInteger("weapon_attack",attack);
    	}

    	if (delay != null) {
    		model.setInteger("weapon_delay",delay);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static Weapon fromModel(Model model) {
		boolean hasFields = false;
    	Weapon message = new Weapon();

    	Integer attackField = model.getInteger("weapon_attack");
    	if (attackField != null) {
    		message.setAttack(attackField);
    		hasFields = true;
    	}

    	Integer delayField = model.getInteger("weapon_delay");
    	if (delayField != null) {
    		message.setDelay(delayField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public Integer getAttack() {
		return attack;
	}
	
	public Weapon setAttack(Integer attack) {
		this.attack = attack;
		return this;
	}
	
	public Boolean hasAttack()  {
        return attack == null ? false : true;
    }

	public Integer getDelay() {
		return delay;
	}
	
	public Weapon setDelay(Integer delay) {
		this.delay = delay;
		return this;
	}
	
	public Boolean hasDelay()  {
        return delay == null ? false : true;
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

    public Schema<Weapon> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Weapon newMessage()
    {
        return new Weapon();
    }

    public Class<Weapon> typeClass()
    {
        return Weapon.class;
    }

    public String messageName()
    {
        return Weapon.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Weapon.class.getName();
    }

    public boolean isInitialized(Weapon message)
    {
        return true;
    }

    public void mergeFrom(Input input, Weapon message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.attack = input.readInt32();
                	break;

            	case 2:

                	message.delay = input.readInt32();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Weapon message) throws IOException
    {

    	if(message.attack == null)
            throw new UninitializedMessageException(message);

    	if(message.attack != null)
            output.writeInt32(1, message.attack, false);

    	if(message.delay == null)
            throw new UninitializedMessageException(message);

    	if(message.delay != null)
            output.writeInt32(2, message.delay, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "attack";
        	
        	case 2: return "delay";
        	
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
    	
    	__fieldMap.put("attack", 1);
    	
    	__fieldMap.put("delay", 2);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Weapon.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Weapon parseFrom(byte[] bytes) {
	Weapon message = new Weapon();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Weapon.class));
	return message;
}

public Weapon clone() {
	byte[] bytes = this.toByteArray();
	Weapon weapon = Weapon.parseFrom(bytes);
	return weapon;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Weapon.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Weapon> schema = RuntimeSchema.getSchema(Weapon.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Weapon.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(Weapon.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
