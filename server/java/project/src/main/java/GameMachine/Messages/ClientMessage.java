
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

public final class ClientMessage  implements Externalizable, Message<ClientMessage>, Schema<ClientMessage>

{

    public static Schema<ClientMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientMessage DEFAULT_INSTANCE = new ClientMessage();

    public List<Entity> entity;

		public Player player;

		public ClientConnection clientConnection;

		public PlayerLogout playerLogout;

		public ErrorMessage errorMessage;

		public PlayerConnect playerConnect;

		public PlayerConnected playerConnected;

		public Integer connection_type;

		public Boolean fastpath;

    public ClientMessage()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("client_message_connection_type",null);

    	model.set("client_message_fastpath",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (connection_type != null) {
    		model.setInteger("client_message_connection_type",connection_type);
    	}

    	if (fastpath != null) {
    		model.setBoolean("client_message_fastpath",fastpath);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static ClientMessage fromModel(Model model) {
		boolean hasFields = false;
    	ClientMessage message = new ClientMessage();

    	Integer connection_typeField = model.getInteger("client_message_connection_type");
    	if (connection_typeField != null) {
    		message.setConnection_type(connection_typeField);
    		hasFields = true;
    	}

    	Boolean fastpathField = model.getBoolean("client_message_fastpath");
    	if (fastpathField != null) {
    		message.setFastpath(fastpathField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public List<Entity> getEntityList() {
		return entity;
	}

	public ClientMessage setEntityList(List<Entity> entity) {
		this.entity = entity;
		return this;
	}

	public Entity getEntity(int index)  {
        return entity == null ? null : entity.get(index);
    }

    public int getEntityCount()  {
        return entity == null ? 0 : entity.size();
    }

    public ClientMessage addEntity(Entity entity)  {
        if(this.entity == null)
            this.entity = new ArrayList<Entity>();
        this.entity.add(entity);
        return this;
    }

    public ClientMessage removeEntityById(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.id.equals(obj.id)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ClientMessage removeEntityByPublished(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.published.equals(obj.published)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ClientMessage removeEntityByEntityType(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.entityType.equals(obj.entityType)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ClientMessage removeEntityBySendToPlayer(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.sendToPlayer.equals(obj.sendToPlayer)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ClientMessage removeEntityBySave(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.save.equals(obj.save)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ClientMessage removeEntityByDestination(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.destination.equals(obj.destination)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ClientMessage removeEntityByJson(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.json.equals(obj.json)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ClientMessage removeEntityByParams(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.params.equals(obj.params)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public ClientMessage removeEntityByFastpath(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();

    		if (entity.fastpath.equals(obj.fastpath)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

	public Player getPlayer() {
		return player;
	}
	
	public ClientMessage setPlayer(Player player) {
		this.player = player;
		return this;
	}
	
	public Boolean hasPlayer()  {
        return player == null ? false : true;
    }

	public ClientConnection getClientConnection() {
		return clientConnection;
	}
	
	public ClientMessage setClientConnection(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
		return this;
	}
	
	public Boolean hasClientConnection()  {
        return clientConnection == null ? false : true;
    }

	public PlayerLogout getPlayerLogout() {
		return playerLogout;
	}
	
	public ClientMessage setPlayerLogout(PlayerLogout playerLogout) {
		this.playerLogout = playerLogout;
		return this;
	}
	
	public Boolean hasPlayerLogout()  {
        return playerLogout == null ? false : true;
    }

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
	
	public ClientMessage setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}
	
	public Boolean hasErrorMessage()  {
        return errorMessage == null ? false : true;
    }

	public PlayerConnect getPlayerConnect() {
		return playerConnect;
	}
	
	public ClientMessage setPlayerConnect(PlayerConnect playerConnect) {
		this.playerConnect = playerConnect;
		return this;
	}
	
	public Boolean hasPlayerConnect()  {
        return playerConnect == null ? false : true;
    }

	public PlayerConnected getPlayerConnected() {
		return playerConnected;
	}
	
	public ClientMessage setPlayerConnected(PlayerConnected playerConnected) {
		this.playerConnected = playerConnected;
		return this;
	}
	
	public Boolean hasPlayerConnected()  {
        return playerConnected == null ? false : true;
    }

	public Integer getConnection_type() {
		return connection_type;
	}
	
	public ClientMessage setConnection_type(Integer connection_type) {
		this.connection_type = connection_type;
		return this;
	}
	
	public Boolean hasConnection_type()  {
        return connection_type == null ? false : true;
    }

	public Boolean getFastpath() {
		return fastpath;
	}
	
	public ClientMessage setFastpath(Boolean fastpath) {
		this.fastpath = fastpath;
		return this;
	}
	
	public Boolean hasFastpath()  {
        return fastpath == null ? false : true;
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

    public Schema<ClientMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientMessage newMessage()
    {
        return new ClientMessage();
    }

    public Class<ClientMessage> typeClass()
    {
        return ClientMessage.class;
    }

    public String messageName()
    {
        return ClientMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientMessage.class.getName();
    }

    public boolean isInitialized(ClientMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:
            	
            		if(message.entity == null)
                        message.entity = new ArrayList<Entity>();
                    
                    message.entity.add(input.mergeObject(null, Entity.getSchema()));
                    
                    break;

            	case 2:

                	message.player = input.mergeObject(message.player, Player.getSchema());
                    break;

            	case 4:

                	message.clientConnection = input.mergeObject(message.clientConnection, ClientConnection.getSchema());
                    break;

            	case 5:

                	message.playerLogout = input.mergeObject(message.playerLogout, PlayerLogout.getSchema());
                    break;

            	case 6:

                	message.errorMessage = input.mergeObject(message.errorMessage, ErrorMessage.getSchema());
                    break;

            	case 7:

                	message.playerConnect = input.mergeObject(message.playerConnect, PlayerConnect.getSchema());
                    break;

            	case 8:

                	message.playerConnected = input.mergeObject(message.playerConnected, PlayerConnected.getSchema());
                    break;

            	case 9:

                	message.connection_type = input.readInt32();
                	break;

            	case 10:

                	message.fastpath = input.readBool();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, ClientMessage message) throws IOException
    {

    	if(message.entity != null)
        {
            for(Entity entity : message.entity)
            {
                if(entity != null) {
                   	
    				output.writeObject(1, entity, Entity.getSchema(), true);
    				
    			}
            }
        }

    	if(message.player != null)
    		output.writeObject(2, message.player, Player.getSchema(), false);

    	if(message.clientConnection != null)
    		output.writeObject(4, message.clientConnection, ClientConnection.getSchema(), false);

    	if(message.playerLogout != null)
    		output.writeObject(5, message.playerLogout, PlayerLogout.getSchema(), false);

    	if(message.errorMessage != null)
    		output.writeObject(6, message.errorMessage, ErrorMessage.getSchema(), false);

    	if(message.playerConnect != null)
    		output.writeObject(7, message.playerConnect, PlayerConnect.getSchema(), false);

    	if(message.playerConnected != null)
    		output.writeObject(8, message.playerConnected, PlayerConnected.getSchema(), false);

    	if(message.connection_type != null)
            output.writeInt32(9, message.connection_type, false);

    	if(message.fastpath != null)
            output.writeBool(10, message.fastpath, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "entity";
        	
        	case 2: return "player";
        	
        	case 4: return "clientConnection";
        	
        	case 5: return "playerLogout";
        	
        	case 6: return "errorMessage";
        	
        	case 7: return "playerConnect";
        	
        	case 8: return "playerConnected";
        	
        	case 9: return "connection_type";
        	
        	case 10: return "fastpath";
        	
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
    	
    	__fieldMap.put("entity", 1);
    	
    	__fieldMap.put("player", 2);
    	
    	__fieldMap.put("clientConnection", 4);
    	
    	__fieldMap.put("playerLogout", 5);
    	
    	__fieldMap.put("errorMessage", 6);
    	
    	__fieldMap.put("playerConnect", 7);
    	
    	__fieldMap.put("playerConnected", 8);
    	
    	__fieldMap.put("connection_type", 9);
    	
    	__fieldMap.put("fastpath", 10);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientMessage parseFrom(byte[] bytes) {
	ClientMessage message = new ClientMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ClientMessage.class));
	return message;
}

public ClientMessage clone() {
	byte[] bytes = this.toByteArray();
	ClientMessage clientMessage = ClientMessage.parseFrom(bytes);
	return clientMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientMessage> schema = RuntimeSchema.getSchema(ClientMessage.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ClientMessage.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(ClientMessage.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
