
package io.gamemachine.messages;

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

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;





import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class GameMessages implements Externalizable, Message<GameMessages>, Schema<GameMessages>{



    public static Schema<GameMessages> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GameMessages getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GameMessages DEFAULT_INSTANCE = new GameMessages();
    static final String defaultScope = GameMessages.class.getSimpleName();

        public List<GameMessage> gameMessage;
	    


    public GameMessages()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
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
		if(this.gameMessage == null)
            this.gameMessage = new ArrayList<GameMessage>();
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
    	
    	    		if (gameMessage.destinationId == obj.destinationId) {
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
    
        	    	    	    	
    public GameMessages removeGameMessageByAgentId(GameMessage gameMessage)  {
    	if(this.gameMessage == null)
           return this;
            
       	Iterator<GameMessage> itr = this.gameMessage.iterator();
       	while (itr.hasNext()) {
    	GameMessage obj = itr.next();
    	
    	    		if (gameMessage.agentId.equals(obj.agentId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	    	    	    	    	    	    	
    public GameMessages removeGameMessageByAuthtoken(GameMessage gameMessage)  {
    	if(this.gameMessage == null)
           return this;
            
       	Iterator<GameMessage> itr = this.gameMessage.iterator();
       	while (itr.hasNext()) {
    	GameMessage obj = itr.next();
    	
    	    		if (gameMessage.authtoken == obj.authtoken) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GameMessages removeGameMessageByUnityMessageId(GameMessage gameMessage)  {
    	if(this.gameMessage == null)
           return this;
            
       	Iterator<GameMessage> itr = this.gameMessage.iterator();
       	while (itr.hasNext()) {
    	GameMessage obj = itr.next();
    	
    	    		if (gameMessage.unityMessageId == obj.unityMessageId) {
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
                if( (GameMessage) gameMessage != null) {
                   	    				output.writeObject(1, gameMessage, GameMessage.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GameMessages");
    	    	//if(this.gameMessage != null) {
    		System.out.println("gameMessage="+this.gameMessage);
    	//}
    	    	System.out.println("END GameMessages");
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
	ProtobufIOUtil.mergeFrom(bytes, message, GameMessages.getSchema());
	return message;
}

public static GameMessages parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GameMessages message = new GameMessages();
	JsonIOUtil.mergeFrom(bytes, message, GameMessages.getSchema(), false);
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

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GameMessages.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GameMessages> schema = GameMessages.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GameMessages.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, GameMessages.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
