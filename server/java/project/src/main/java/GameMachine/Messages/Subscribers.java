
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

public final class Subscribers  implements Externalizable, Message<Subscribers>, Schema<Subscribers>

{

    public static Schema<Subscribers> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Subscribers getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Subscribers DEFAULT_INSTANCE = new Subscribers();

    public List<String> subscriberId;

    public Subscribers()
    {
        
    }

	public static void clearModel(Model model) {

    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static Subscribers fromModel(Model model) {
		boolean hasFields = false;
    	Subscribers message = new Subscribers();

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public List<String> getSubscriberIdList() {
		return subscriberId;
	}

	public Subscribers setSubscriberIdList(List<String> subscriberId) {
		this.subscriberId = subscriberId;
		return this;
	}

	public String getSubscriberId(int index)  {
        return subscriberId == null ? null : subscriberId.get(index);
    }

    public int getSubscriberIdCount()  {
        return subscriberId == null ? 0 : subscriberId.size();
    }

    public Subscribers addSubscriberId(String subscriberId)  {
        if(this.subscriberId == null)
            this.subscriberId = new ArrayList<String>();
        this.subscriberId.add(subscriberId);
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

    public Schema<Subscribers> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Subscribers newMessage()
    {
        return new Subscribers();
    }

    public Class<Subscribers> typeClass()
    {
        return Subscribers.class;
    }

    public String messageName()
    {
        return Subscribers.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Subscribers.class.getName();
    }

    public boolean isInitialized(Subscribers message)
    {
        return true;
    }

    public void mergeFrom(Input input, Subscribers message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:
            	
            		if(message.subscriberId == null)
                        message.subscriberId = new ArrayList<String>();
                    
                	message.subscriberId.add(input.readString());
                	
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Subscribers message) throws IOException
    {

    	if(message.subscriberId != null)
        {
            for(String subscriberId : message.subscriberId)
            {
                if(subscriberId != null) {
                   	
            		output.writeString(1, subscriberId, true);
    				
    			}
            }
        }

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "subscriberId";
        	
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
    	
    	__fieldMap.put("subscriberId", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Subscribers.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Subscribers parseFrom(byte[] bytes) {
	Subscribers message = new Subscribers();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Subscribers.class));
	return message;
}

public Subscribers clone() {
	byte[] bytes = this.toByteArray();
	Subscribers subscribers = Subscribers.parseFrom(bytes);
	return subscribers;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Subscribers.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Subscribers> schema = RuntimeSchema.getSchema(Subscribers.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Subscribers.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(Subscribers.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
