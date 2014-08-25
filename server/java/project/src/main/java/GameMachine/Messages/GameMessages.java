
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

public final class GameMessages  implements Externalizable, Message<GameMessages>, Schema<GameMessages>

{

    public static Schema<GameMessages> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GameMessages getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GameMessages DEFAULT_INSTANCE = new GameMessages();

    public List<GameMessage> gameMessage;

    public GameMessages()
    {
        
    }

	public static void clearModel(Model model) {

    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static GameMessages fromModel(Model model) {
		boolean hasFields = false;
    	GameMessages message = new GameMessages();

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public List<GameMessage> getGameMessageList() {
		return gameMessage;
	}

	public GameMessages setGameMessageList(List<GameMessage> gameMessage) {
		this.gameMessage = gameMessage;
		return this;
	}

	public GameMessage getGameMessage(int index)  {
        return gameMessage == null ? null : gameMessage.get(index);
    }

    public int getGameMessageCount()  {
        return gameMessage == null ? 0 : gameMessage.size();
    }

    public GameMessages addGameMessage(GameMessage gameMessage)  {
        if(this.gameMessage == null)
            this.gameMessage = new ArrayList<GameMessage>();
        this.gameMessage.add(gameMessage);
        return this;
    }

    public GameMessages removeGameMessageByPlayerId(GameMessage gameMessage)  {
    	if(this.gameMessage == null)
           return this;
            
       	Iterator<GameMessage> itr = this.gameMessage.iterator();
       	while (itr.hasNext()) {
    	GameMessage obj = itr.next();

    		if (gameMessage.playerId.equals(obj.playerId)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public GameMessages removeGameMessageByMessageId(GameMessage gameMessage)  {
    	if(this.gameMessage == null)
           return this;
            
       	Iterator<GameMessage> itr = this.gameMessage.iterator();
       	while (itr.hasNext()) {
    	GameMessage obj = itr.next();

    		if (gameMessage.messageId.equals(obj.messageId)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public GameMessages removeGameMessageByDestinationId(GameMessage gameMessage)  {
    	if(this.gameMessage == null)
           return this;
            
       	Iterator<GameMessage> itr = this.gameMessage.iterator();
       	while (itr.hasNext()) {
    	GameMessage obj = itr.next();

    		if (gameMessage.destinationId.equals(obj.destinationId)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public GameMessages removeGameMessageByDestination(GameMessage gameMessage)  {
    	if(this.gameMessage == null)
           return this;
            
       	Iterator<GameMessage> itr = this.gameMessage.iterator();
       	while (itr.hasNext()) {
    	GameMessage obj = itr.next();

    		if (gameMessage.destination.equals(obj.destination)) {
    	
      			itr.remove();
    		}
		}
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

    public Schema<GameMessages> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GameMessages newMessage()
    {
        return new GameMessages();
    }

    public Class<GameMessages> typeClass()
    {
        return GameMessages.class;
    }

    public String messageName()
    {
        return GameMessages.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GameMessages.class.getName();
    }

    public boolean isInitialized(GameMessages message)
    {
        return true;
    }

    public void mergeFrom(Input input, GameMessages message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:
            	
            		if(message.gameMessage == null)
                        message.gameMessage = new ArrayList<GameMessage>();
                    
                    message.gameMessage.add(input.mergeObject(null, GameMessage.getSchema()));
                    
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, GameMessages message) throws IOException
    {

    	if(message.gameMessage != null)
        {
            for(GameMessage gameMessage : message.gameMessage)
            {
                if(gameMessage != null) {
                   	
    				output.writeObject(1, gameMessage, GameMessage.getSchema(), true);
    				
    			}
            }
        }

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "gameMessage";
        	
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
    	
    	__fieldMap.put("gameMessage", 1);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GameMessages.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GameMessages parseFrom(byte[] bytes) {
	GameMessages message = new GameMessages();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(GameMessages.class));
	return message;
}

public GameMessages clone() {
	byte[] bytes = this.toByteArray();
	GameMessages gameMessages = GameMessages.parseFrom(bytes);
	return gameMessages;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GameMessages.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GameMessages> schema = RuntimeSchema.getSchema(GameMessages.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(GameMessages.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(GameMessages.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
