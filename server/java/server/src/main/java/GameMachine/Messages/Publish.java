
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
public final class Publish implements Externalizable, Message<Publish>, Schema<Publish>
{

    public static Schema<Publish> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Publish getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Publish DEFAULT_INSTANCE = new Publish();
    static final String defaultScope = Publish.class.getSimpleName();

		public String topic;

		public Entity message;

		public String path;

		public String gameId;

    public Publish()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("publish_topic",null);

    	model.set("publish_path",null);

    	model.set("publish_game_id",null);
    	
    }
    
	public void toModel(Model model) {

    	if (topic != null) {
    		model.setString("publish_topic",topic);
    	}

    	if (path != null) {
    		model.setString("publish_path",path);
    	}

    	if (gameId != null) {
    		model.setString("publish_game_id",gameId);
    	}

    }
    
	public static Publish fromModel(Model model) {
		boolean hasFields = false;
    	Publish message = new Publish();

    	String topicField = model.getString("publish_topic");
    	if (topicField != null) {
    		message.setTopic(topicField);
    		hasFields = true;
    	}

    	String pathField = model.getString("publish_path");
    	if (pathField != null) {
    		message.setPath(pathField);
    		hasFields = true;
    	}

    	String gameIdField = model.getString("publish_game_id");
    	if (gameIdField != null) {
    		message.setGameId(gameIdField);
    		hasFields = true;
    	}

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

    public Boolean hasTopic()  {
        return topic == null ? false : true;
    }

	public String getTopic() {
		return topic;
	}
	
	public Publish setTopic(String topic) {
		this.topic = topic;
		return this;
	}

    public Boolean hasMessage()  {
        return message == null ? false : true;
    }

	public Entity getMessage() {
		return message;
	}
	
	public Publish setMessage(Entity message) {
		this.message = message;
		return this;
	}

    public Boolean hasPath()  {
        return path == null ? false : true;
    }

	public String getPath() {
		return path;
	}
	
	public Publish setPath(String path) {
		this.path = path;
		return this;
	}

    public Boolean hasGameId()  {
        return gameId == null ? false : true;
    }

	public String getGameId() {
		return gameId;
	}
	
	public Publish setGameId(String gameId) {
		this.gameId = gameId;
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

    public Schema<Publish> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Publish newMessage()
    {
        return new Publish();
    }

    public Class<Publish> typeClass()
    {
        return Publish.class;
    }

    public String messageName()
    {
        return Publish.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Publish.class.getName();
    }

    public boolean isInitialized(Publish message)
    {
        return true;
    }

    public void mergeFrom(Input input, Publish message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.topic = input.readString();
                	break;

            	case 2:

                	message.message = input.mergeObject(message.message, Entity.getSchema());
                    break;

            	case 3:

                	message.path = input.readString();
                	break;

            	case 4:

                	message.gameId = input.readString();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Publish message) throws IOException
    {

    	if(message.topic != null)
            output.writeString(1, message.topic, false);

    	if(message.message == null)
            throw new UninitializedMessageException(message);

    	if(message.message != null)
    		output.writeObject(2, message.message, Entity.getSchema(), false);

    	if(message.path != null)
            output.writeString(3, message.path, false);

    	if(message.gameId == null)
            throw new UninitializedMessageException(message);

    	if(message.gameId != null)
            output.writeString(4, message.gameId, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "topic";
        	
        	case 2: return "message";
        	
        	case 3: return "path";
        	
        	case 4: return "gameId";
        	
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
    	
    	__fieldMap.put("topic", 1);
    	
    	__fieldMap.put("message", 2);
    	
    	__fieldMap.put("path", 3);
    	
    	__fieldMap.put("gameId", 4);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Publish.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Publish parseFrom(byte[] bytes) {
	Publish message = new Publish();
	ProtobufIOUtil.mergeFrom(bytes, message, Publish.getSchema());
	return message;
}

public static Publish parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Publish message = new Publish();
	JsonIOUtil.mergeFrom(bytes, message, Publish.getSchema(), false);
	return message;
}

public Publish clone() {
	byte[] bytes = this.toByteArray();
	Publish publish = Publish.parseFrom(bytes);
	return publish;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Publish.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Publish> schema = Publish.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Publish.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Publish.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
