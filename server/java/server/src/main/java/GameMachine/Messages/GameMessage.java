
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
public final class GameMessage implements Externalizable, Message<GameMessage>, Schema<GameMessage>
{

    public static Schema<GameMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GameMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GameMessage DEFAULT_INSTANCE = new GameMessage();
    static final String defaultScope = GameMessage.class.getSimpleName();

		public String playerId;

		public String messageId;

		public Integer destinationId;

		public String destination;

		public String agentId;

		public DynamicMessage dynamicMessage;

		public PlayerItems playerItems;

		public AddPlayerItem addPlayerItem;

		public RemovePlayerItem removePlayerItem;

		public RequestPlayerItems requestPlayerItems;

    public GameMessage()
    {
        
    }

	public static void clearModel(Model model) {

    	model.set("game_message_player_id",null);

    	model.set("game_message_message_id",null);

    	model.set("game_message_destination_id",null);

    	model.set("game_message_destination",null);

    	model.set("game_message_agent_id",null);

    }
    
	public void toModel(Model model) {

    	if (playerId != null) {
    		model.setString("game_message_player_id",playerId);
    	}

    	if (messageId != null) {
    		model.setString("game_message_message_id",messageId);
    	}

    	if (destinationId != null) {
    		model.setInteger("game_message_destination_id",destinationId);
    	}

    	if (destination != null) {
    		model.setString("game_message_destination",destination);
    	}

    	if (agentId != null) {
    		model.setString("game_message_agent_id",agentId);
    	}

    }
    
	public static GameMessage fromModel(Model model) {
		boolean hasFields = false;
    	GameMessage message = new GameMessage();

    	String playerIdField = model.getString("game_message_player_id");
    	if (playerIdField != null) {
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}

    	String messageIdField = model.getString("game_message_message_id");
    	if (messageIdField != null) {
    		message.setMessageId(messageIdField);
    		hasFields = true;
    	}

    	Integer destinationIdField = model.getInteger("game_message_destination_id");
    	if (destinationIdField != null) {
    		message.setDestinationId(destinationIdField);
    		hasFields = true;
    	}

    	String destinationField = model.getString("game_message_destination");
    	if (destinationField != null) {
    		message.setDestination(destinationField);
    		hasFields = true;
    	}

    	String agentIdField = model.getString("game_message_agent_id");
    	if (agentIdField != null) {
    		message.setAgentId(agentIdField);
    		hasFields = true;
    	}

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }

	public String getPlayerId() {
		return playerId;
	}
	
	public GameMessage setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;
	}

    public Boolean hasMessageId()  {
        return messageId == null ? false : true;
    }

	public String getMessageId() {
		return messageId;
	}
	
	public GameMessage setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}

    public Boolean hasDestinationId()  {
        return destinationId == null ? false : true;
    }

	public Integer getDestinationId() {
		return destinationId;
	}
	
	public GameMessage setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
		return this;
	}

    public Boolean hasDestination()  {
        return destination == null ? false : true;
    }

	public String getDestination() {
		return destination;
	}
	
	public GameMessage setDestination(String destination) {
		this.destination = destination;
		return this;
	}

    public Boolean hasAgentId()  {
        return agentId == null ? false : true;
    }

	public String getAgentId() {
		return agentId;
	}
	
	public GameMessage setAgentId(String agentId) {
		this.agentId = agentId;
		return this;
	}

    public Boolean hasDynamicMessage()  {
        return dynamicMessage == null ? false : true;
    }

	public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public GameMessage setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
		return this;
	}

    public Boolean hasPlayerItems()  {
        return playerItems == null ? false : true;
    }

	public PlayerItems getPlayerItems() {
		return playerItems;
	}
	
	public GameMessage setPlayerItems(PlayerItems playerItems) {
		this.playerItems = playerItems;
		return this;
	}

    public Boolean hasAddPlayerItem()  {
        return addPlayerItem == null ? false : true;
    }

	public AddPlayerItem getAddPlayerItem() {
		return addPlayerItem;
	}
	
	public GameMessage setAddPlayerItem(AddPlayerItem addPlayerItem) {
		this.addPlayerItem = addPlayerItem;
		return this;
	}

    public Boolean hasRemovePlayerItem()  {
        return removePlayerItem == null ? false : true;
    }

	public RemovePlayerItem getRemovePlayerItem() {
		return removePlayerItem;
	}
	
	public GameMessage setRemovePlayerItem(RemovePlayerItem removePlayerItem) {
		this.removePlayerItem = removePlayerItem;
		return this;
	}

    public Boolean hasRequestPlayerItems()  {
        return requestPlayerItems == null ? false : true;
    }

	public RequestPlayerItems getRequestPlayerItems() {
		return requestPlayerItems;
	}
	
	public GameMessage setRequestPlayerItems(RequestPlayerItems requestPlayerItems) {
		this.requestPlayerItems = requestPlayerItems;
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

    public Schema<GameMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GameMessage newMessage()
    {
        return new GameMessage();
    }

    public Class<GameMessage> typeClass()
    {
        return GameMessage.class;
    }

    public String messageName()
    {
        return GameMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GameMessage.class.getName();
    }

    public boolean isInitialized(GameMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, GameMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.playerId = input.readString();
                	break;

            	case 2:

                	message.messageId = input.readString();
                	break;

            	case 3:

                	message.destinationId = input.readInt32();
                	break;

            	case 4:

                	message.destination = input.readString();
                	break;

            	case 5:

                	message.agentId = input.readString();
                	break;

            	case 6:

                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;

            	case 10:

                	message.playerItems = input.mergeObject(message.playerItems, PlayerItems.getSchema());
                    break;

            	case 11:

                	message.addPlayerItem = input.mergeObject(message.addPlayerItem, AddPlayerItem.getSchema());
                    break;

            	case 12:

                	message.removePlayerItem = input.mergeObject(message.removePlayerItem, RemovePlayerItem.getSchema());
                    break;

            	case 13:

                	message.requestPlayerItems = input.mergeObject(message.requestPlayerItems, RequestPlayerItems.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, GameMessage message) throws IOException
    {

    	if(message.playerId != null)
            output.writeString(1, message.playerId, false);

    	if(message.messageId != null)
            output.writeString(2, message.messageId, false);

    	if(message.destinationId != null)
            output.writeInt32(3, message.destinationId, false);

    	if(message.destination != null)
            output.writeString(4, message.destination, false);

    	if(message.agentId != null)
            output.writeString(5, message.agentId, false);

    	if(message.dynamicMessage != null)
    		output.writeObject(6, message.dynamicMessage, DynamicMessage.getSchema(), false);

    	if(message.playerItems != null)
    		output.writeObject(10, message.playerItems, PlayerItems.getSchema(), false);

    	if(message.addPlayerItem != null)
    		output.writeObject(11, message.addPlayerItem, AddPlayerItem.getSchema(), false);

    	if(message.removePlayerItem != null)
    		output.writeObject(12, message.removePlayerItem, RemovePlayerItem.getSchema(), false);

    	if(message.requestPlayerItems != null)
    		output.writeObject(13, message.requestPlayerItems, RequestPlayerItems.getSchema(), false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "playerId";
        	
        	case 2: return "messageId";
        	
        	case 3: return "destinationId";
        	
        	case 4: return "destination";
        	
        	case 5: return "agentId";
        	
        	case 6: return "dynamicMessage";
        	
        	case 10: return "playerItems";
        	
        	case 11: return "addPlayerItem";
        	
        	case 12: return "removePlayerItem";
        	
        	case 13: return "requestPlayerItems";
        	
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
    	
    	__fieldMap.put("playerId", 1);
    	
    	__fieldMap.put("messageId", 2);
    	
    	__fieldMap.put("destinationId", 3);
    	
    	__fieldMap.put("destination", 4);
    	
    	__fieldMap.put("agentId", 5);
    	
    	__fieldMap.put("dynamicMessage", 6);
    	
    	__fieldMap.put("playerItems", 10);
    	
    	__fieldMap.put("addPlayerItem", 11);
    	
    	__fieldMap.put("removePlayerItem", 12);
    	
    	__fieldMap.put("requestPlayerItems", 13);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GameMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GameMessage parseFrom(byte[] bytes) {
	GameMessage message = new GameMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, GameMessage.getSchema());
	return message;
}

public static GameMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GameMessage message = new GameMessage();
	JsonIOUtil.mergeFrom(bytes, message, GameMessage.getSchema(), false);
	return message;
}

public GameMessage clone() {
	byte[] bytes = this.toByteArray();
	GameMessage gameMessage = GameMessage.parseFrom(bytes);
	return gameMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GameMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GameMessage> schema = GameMessage.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GameMessage.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GameMessage.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
