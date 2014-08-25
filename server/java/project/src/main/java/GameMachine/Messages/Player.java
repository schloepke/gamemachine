
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

public final class Player  implements Externalizable, Message<Player>, Schema<Player>

{

    public static Schema<Player> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Player getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Player DEFAULT_INSTANCE = new Player();

		public String id;

		public String name;

		public Boolean authenticated;

		public String authtoken;

		public Transform transform;

    public Player()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("player_id",null);

    	model.set("player_name",null);

    	model.set("player_authenticated",null);

    	model.set("player_authtoken",null);

    }
    
	public void toModel(Model model, String playerId) {

    	if (id != null) {
    		model.setString("player_id",id);
    	}

    	if (name != null) {
    		model.setString("player_name",name);
    	}

    	if (authenticated != null) {
    		model.setBoolean("player_authenticated",authenticated);
    	}

    	if (authtoken != null) {
    		model.setString("player_authtoken",authtoken);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static Player fromModel(Model model) {
		boolean hasFields = false;
    	Player message = new Player();

    	String idField = model.getString("player_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}

    	String nameField = model.getString("player_name");
    	if (nameField != null) {
    		message.setName(nameField);
    		hasFields = true;
    	}

    	Boolean authenticatedField = model.getBoolean("player_authenticated");
    	if (authenticatedField != null) {
    		message.setAuthenticated(authenticatedField);
    		hasFields = true;
    	}

    	String authtokenField = model.getString("player_authtoken");
    	if (authtokenField != null) {
    		message.setAuthtoken(authtokenField);
    		hasFields = true;
    	}

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public String getId() {
		return id;
	}
	
	public Player setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }

	public String getName() {
		return name;
	}
	
	public Player setName(String name) {
		this.name = name;
		return this;
	}
	
	public Boolean hasName()  {
        return name == null ? false : true;
    }

	public Boolean getAuthenticated() {
		return authenticated;
	}
	
	public Player setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
		return this;
	}
	
	public Boolean hasAuthenticated()  {
        return authenticated == null ? false : true;
    }

	public String getAuthtoken() {
		return authtoken;
	}
	
	public Player setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
		return this;
	}
	
	public Boolean hasAuthtoken()  {
        return authtoken == null ? false : true;
    }

	public Transform getTransform() {
		return transform;
	}
	
	public Player setTransform(Transform transform) {
		this.transform = transform;
		return this;
	}
	
	public Boolean hasTransform()  {
        return transform == null ? false : true;
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

    public Schema<Player> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Player newMessage()
    {
        return new Player();
    }

    public Class<Player> typeClass()
    {
        return Player.class;
    }

    public String messageName()
    {
        return Player.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Player.class.getName();
    }

    public boolean isInitialized(Player message)
    {
        return true;
    }

    public void mergeFrom(Input input, Player message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.id = input.readString();
                	break;

            	case 2:

                	message.name = input.readString();
                	break;

            	case 3:

                	message.authenticated = input.readBool();
                	break;

            	case 4:

                	message.authtoken = input.readString();
                	break;

            	case 5:

                	message.transform = input.mergeObject(message.transform, Transform.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, Player message) throws IOException
    {

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	if(message.id != null)
            output.writeString(1, message.id, false);

    	if(message.name != null)
            output.writeString(2, message.name, false);

    	if(message.authenticated != null)
            output.writeBool(3, message.authenticated, false);

    	if(message.authtoken != null)
            output.writeString(4, message.authtoken, false);

    	if(message.transform != null)
    		output.writeObject(5, message.transform, Transform.getSchema(), false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "id";
        	
        	case 2: return "name";
        	
        	case 3: return "authenticated";
        	
        	case 4: return "authtoken";
        	
        	case 5: return "transform";
        	
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
    	
    	__fieldMap.put("id", 1);
    	
    	__fieldMap.put("name", 2);
    	
    	__fieldMap.put("authenticated", 3);
    	
    	__fieldMap.put("authtoken", 4);
    	
    	__fieldMap.put("transform", 5);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Player.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Player parseFrom(byte[] bytes) {
	Player message = new Player();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Player.class));
	return message;
}

public Player clone() {
	byte[] bytes = this.toByteArray();
	Player player = Player.parseFrom(bytes);
	return player;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Player.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Player> schema = RuntimeSchema.getSchema(Player.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Player.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(Player.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
