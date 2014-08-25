
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

public final class ClientConnection  implements Externalizable, Message<ClientConnection>, Schema<ClientConnection>

{

    public static Schema<ClientConnection> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientConnection getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientConnection DEFAULT_INSTANCE = new ClientConnection();

		public String id;

		public String gateway;

		public String server;

		public String type;

    public ClientConnection()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("client_connection_id",null);

    	model.set("client_connection_gateway",null);

    	model.set("client_connection_server",null);

    	model.set("client_connection_type",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (id != null) {
    		model.setString("client_connection_id",id);
    	}

    	if (gateway != null) {
    		model.setString("client_connection_gateway",gateway);
    	}

    	if (server != null) {
    		model.setString("client_connection_server",server);
    	}

    	if (type != null) {
    		model.setString("client_connection_type",type);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static ClientConnection fromModel(Model model) {
		boolean hasFields = false;
    	ClientConnection message = new ClientConnection();

    	String idField = model.getString("client_connection_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}

    	String gatewayField = model.getString("client_connection_gateway");
    	if (gatewayField != null) {
    		message.setGateway(gatewayField);
    		hasFields = true;
    	}

    	String serverField = model.getString("client_connection_server");
    	if (serverField != null) {
    		message.setServer(serverField);
    		hasFields = true;
    	}

    	String typeField = model.getString("client_connection_type");
    	if (typeField != null) {
    		message.setType(typeField);
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
	
	public ClientConnection setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }

	public String getGateway() {
		return gateway;
	}
	
	public ClientConnection setGateway(String gateway) {
		this.gateway = gateway;
		return this;
	}
	
	public Boolean hasGateway()  {
        return gateway == null ? false : true;
    }

	public String getServer() {
		return server;
	}
	
	public ClientConnection setServer(String server) {
		this.server = server;
		return this;
	}
	
	public Boolean hasServer()  {
        return server == null ? false : true;
    }

	public String getType() {
		return type;
	}
	
	public ClientConnection setType(String type) {
		this.type = type;
		return this;
	}
	
	public Boolean hasType()  {
        return type == null ? false : true;
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

    public Schema<ClientConnection> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientConnection newMessage()
    {
        return new ClientConnection();
    }

    public Class<ClientConnection> typeClass()
    {
        return ClientConnection.class;
    }

    public String messageName()
    {
        return ClientConnection.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientConnection.class.getName();
    }

    public boolean isInitialized(ClientConnection message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientConnection message) throws IOException
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

                	message.gateway = input.readString();
                	break;

            	case 3:

                	message.server = input.readString();
                	break;

            	case 4:

                	message.type = input.readString();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, ClientConnection message) throws IOException
    {

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	if(message.id != null)
            output.writeString(1, message.id, false);

    	if(message.gateway != null)
            output.writeString(2, message.gateway, false);

    	if(message.server != null)
            output.writeString(3, message.server, false);

    	if(message.type != null)
            output.writeString(4, message.type, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "id";
        	
        	case 2: return "gateway";
        	
        	case 3: return "server";
        	
        	case 4: return "type";
        	
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
    	
    	__fieldMap.put("gateway", 2);
    	
    	__fieldMap.put("server", 3);
    	
    	__fieldMap.put("type", 4);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientConnection.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientConnection parseFrom(byte[] bytes) {
	ClientConnection message = new ClientConnection();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ClientConnection.class));
	return message;
}

public ClientConnection clone() {
	byte[] bytes = this.toByteArray();
	ClientConnection clientConnection = ClientConnection.parseFrom(bytes);
	return clientConnection;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientConnection.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientConnection> schema = RuntimeSchema.getSchema(ClientConnection.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ClientConnection.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(ClientConnection.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
