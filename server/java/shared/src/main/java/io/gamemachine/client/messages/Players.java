
package io.gamemachine.client.messages;

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


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class Players implements Externalizable, Message<Players>, Schema<Players>{



    public static Schema<Players> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Players getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Players DEFAULT_INSTANCE = new Players();

        public List<Player> player;
	  
    public Players()
    {
        
    }


	

	    
    public Boolean hasPlayer()  {
        return player == null ? false : true;
    }
        
		public List<Player> getPlayerList() {
		if(this.player == null)
            this.player = new ArrayList<Player>();
		return player;
	}

	public Players setPlayerList(List<Player> player) {
		this.player = player;
		return this;
	}

	public Player getPlayer(int index)  {
        return player == null ? null : player.get(index);
    }

    public int getPlayerCount()  {
        return player == null ? 0 : player.size();
    }

    public Players addPlayer(Player player)  {
        if(this.player == null)
            this.player = new ArrayList<Player>();
        this.player.add(player);
        return this;
    }
            	    	    	    	
    public Players removePlayerById(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByAuthenticated(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.authenticated.equals(obj.authenticated)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByAuthtoken(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.authtoken.equals(obj.authtoken)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByPasswordHash(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.passwordHash.equals(obj.passwordHash)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByGameId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.gameId.equals(obj.gameId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByRecordId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByRole(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.role.equals(obj.role)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByLocked(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.locked.equals(obj.locked)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByIp(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.ip.equals(obj.ip)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByIpChangedAt(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.ipChangedAt.equals(obj.ipChangedAt)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Players removePlayerByCharacterId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.characterId.equals(obj.characterId)) {
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

    public Schema<Players> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Players newMessage()
    {
        return new Players();
    }

    public Class<Players> typeClass()
    {
        return Players.class;
    }

    public String messageName()
    {
        return Players.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Players.class.getName();
    }

    public boolean isInitialized(Players message)
    {
        return true;
    }

    public void mergeFrom(Input input, Players message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.player == null)
                        message.player = new ArrayList<Player>();
                                        message.player.add(input.mergeObject(null, Player.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Players message) throws IOException
    {
    	    	
    	    	
    	    	if(message.player != null)
        {
            for(Player player : message.player)
            {
                if(player != null) {
                   	    				output.writeObject(1, player, Player.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "player";
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
    	    	__fieldMap.put("player", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Players.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Players parseFrom(byte[] bytes) {
	Players message = new Players();
	ProtobufIOUtil.mergeFrom(bytes, message, Players.getSchema());
	return message;
}

public static Players parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Players message = new Players();
	JsonIOUtil.mergeFrom(bytes, message, Players.getSchema(), false);
	return message;
}

public Players clone() {
	byte[] bytes = this.toByteArray();
	Players players = Players.parseFrom(bytes);
	return players;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Players.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Players> schema = Players.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Players.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
