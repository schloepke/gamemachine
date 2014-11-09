
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
public final class Regions implements Externalizable, Message<Regions>, Schema<Regions>
{

    public static Schema<Regions> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Regions getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Regions DEFAULT_INSTANCE = new Regions();
    static final String defaultScope = Regions.class.getSimpleName();

		public String regions;

    public Regions()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("regions_regions",null);
    	
    }
    
	public void toModel(Model model) {

    	if (regions != null) {
    		model.setString("regions_regions",regions);
    	}

    }
    
	public static Regions fromModel(Model model) {
		boolean hasFields = false;
    	Regions message = new Regions();

    	String regionsField = model.getString("regions_regions");
    	if (regionsField != null) {
    		message.setRegions(regionsField);
    		hasFields = true;
    	}

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

    public Boolean hasRegions()  {
        return regions == null ? false : true;
    }

	public String getRegions() {
		return regions;
	}
	
	public Regions setRegions(String regions) {
		this.regions = regions;
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

    public Schema<Regions> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Regions newMessage()
    {
        return new Regions();
    }

    public Class<Regions> typeClass()
    {
        return Regions.class;
    }

    public String messageName()
    {
        return Regions.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Regions.class.getName();
    }

    public boolean isInitialized(Regions message)
    {
        return true;
    }

    public void mergeFrom(Input input, Regions message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.regions = input.readString();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Regions message) throws IOException
    {

    	if(message.regions == null)
            throw new UninitializedMessageException(message);

    	if(message.regions != null)
            output.writeString(1, message.regions, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "regions";
        	
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
    	
    	__fieldMap.put("regions", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Regions.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Regions parseFrom(byte[] bytes) {
	Regions message = new Regions();
	ProtobufIOUtil.mergeFrom(bytes, message, Regions.getSchema());
	return message;
}

public static Regions parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Regions message = new Regions();
	JsonIOUtil.mergeFrom(bytes, message, Regions.getSchema(), false);
	return message;
}

public Regions clone() {
	byte[] bytes = this.toByteArray();
	Regions regions = Regions.parseFrom(bytes);
	return regions;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Regions.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Regions> schema = Regions.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Regions.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Regions.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
