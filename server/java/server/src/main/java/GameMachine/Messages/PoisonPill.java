
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
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

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

import com.game_machine.util.LocalLinkedBuffer;

import java.nio.charset.Charset;

import org.javalite.activejdbc.Model;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

@SuppressWarnings("unused")
public final class PoisonPill implements Externalizable, Message<PoisonPill>, Schema<PoisonPill>
{

    public static Schema<PoisonPill> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PoisonPill getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PoisonPill DEFAULT_INSTANCE = new PoisonPill();
    static final String defaultScope = PoisonPill.class.getSimpleName();

		public Integer notused;

    public PoisonPill()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("poison_pill_notused",null);
    	
    }
    
	public void toModel(Model model) {

    	if (notused != null) {
    		model.setInteger("poison_pill_notused",notused);
    	}

    }
    
	public static PoisonPill fromModel(Model model) {
		boolean hasFields = false;
    	PoisonPill message = new PoisonPill();

    	Integer notusedField = model.getInteger("poison_pill_notused");
    	if (notusedField != null) {
    		message.setNotused(notusedField);
    		hasFields = true;
    	}

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

    public Boolean hasNotused()  {
        return notused == null ? false : true;
    }

	public Integer getNotused() {
		return notused;
	}
	
	public PoisonPill setNotused(Integer notused) {
		this.notused = notused;
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

    public Schema<PoisonPill> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PoisonPill newMessage()
    {
        return new PoisonPill();
    }

    public Class<PoisonPill> typeClass()
    {
        return PoisonPill.class;
    }

    public String messageName()
    {
        return PoisonPill.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PoisonPill.class.getName();
    }

    public boolean isInitialized(PoisonPill message)
    {
        return true;
    }

    public void mergeFrom(Input input, PoisonPill message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.notused = input.readInt32();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, PoisonPill message) throws IOException
    {

    	if(message.notused != null)
            output.writeInt32(1, message.notused, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "notused";
        	
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
    	
    	__fieldMap.put("notused", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PoisonPill.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PoisonPill parseFrom(byte[] bytes) {
	PoisonPill message = new PoisonPill();
	ProtobufIOUtil.mergeFrom(bytes, message, PoisonPill.getSchema());
	return message;
}

public static PoisonPill parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PoisonPill message = new PoisonPill();
	JsonIOUtil.mergeFrom(bytes, message, PoisonPill.getSchema(), false);
	return message;
}

public PoisonPill clone() {
	byte[] bytes = this.toByteArray();
	PoisonPill poisonPill = PoisonPill.parseFrom(bytes);
	return poisonPill;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PoisonPill.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PoisonPill> schema = PoisonPill.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PoisonPill.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, PoisonPill.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
