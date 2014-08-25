
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

public final class NativeBytes  implements Externalizable, Message<NativeBytes>, Schema<NativeBytes>

{

    public static Schema<NativeBytes> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static NativeBytes getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final NativeBytes DEFAULT_INSTANCE = new NativeBytes();

		public ByteString bytes;

    public List<ByteString> byteArray;

    public NativeBytes()
    {
        
    }

	public static void clearModel(Model model) {

    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static NativeBytes fromModel(Model model) {
		boolean hasFields = false;
    	NativeBytes message = new NativeBytes();

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public ByteString getBytes() {
		return bytes;
	}
	
	public NativeBytes setBytes(ByteString bytes) {
		this.bytes = bytes;
		return this;
	}
	
	public Boolean hasBytes()  {
        return bytes == null ? false : true;
    }

	public List<ByteString> getByteArrayList() {
		return byteArray;
	}

	public NativeBytes setByteArrayList(List<ByteString> byteArray) {
		this.byteArray = byteArray;
		return this;
	}

	public ByteString getByteArray(int index)  {
        return byteArray == null ? null : byteArray.get(index);
    }

    public int getByteArrayCount()  {
        return byteArray == null ? 0 : byteArray.size();
    }

    public NativeBytes addByteArray(ByteString byteArray)  {
        if(this.byteArray == null)
            this.byteArray = new ArrayList<ByteString>();
        this.byteArray.add(byteArray);
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

    public Schema<NativeBytes> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public NativeBytes newMessage()
    {
        return new NativeBytes();
    }

    public Class<NativeBytes> typeClass()
    {
        return NativeBytes.class;
    }

    public String messageName()
    {
        return NativeBytes.class.getSimpleName();
    }

    public String messageFullName()
    {
        return NativeBytes.class.getName();
    }

    public boolean isInitialized(NativeBytes message)
    {
        return true;
    }

    public void mergeFrom(Input input, NativeBytes message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.bytes = input.readBytes();
                	break;

            	case 2:
            	
            		if(message.byteArray == null)
                        message.byteArray = new ArrayList<ByteString>();
                    
                	message.byteArray.add(input.readBytes());
                	
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, NativeBytes message) throws IOException
    {

    	if(message.bytes != null)
            output.writeBytes(1, message.bytes, false);

    	if(message.byteArray != null)
        {
            for(ByteString byteArray : message.byteArray)
            {
                if(byteArray != null) {
                   	
            		output.writeBytes(2, byteArray, true);
    				
    			}
            }
        }

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "bytes";
        	
        	case 2: return "byteArray";
        	
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
    	
    	__fieldMap.put("bytes", 1);
    	
    	__fieldMap.put("byteArray", 2);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = NativeBytes.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static NativeBytes parseFrom(byte[] bytes) {
	NativeBytes message = new NativeBytes();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(NativeBytes.class));
	return message;
}

public NativeBytes clone() {
	byte[] bytes = this.toByteArray();
	NativeBytes nativeBytes = NativeBytes.parseFrom(bytes);
	return nativeBytes;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, NativeBytes.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<NativeBytes> schema = RuntimeSchema.getSchema(NativeBytes.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(NativeBytes.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(NativeBytes.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
