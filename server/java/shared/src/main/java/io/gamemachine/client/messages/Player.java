
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
public final class Player implements Externalizable, Message<Player>, Schema<Player>{



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
	    
        			public boolean authenticated;
	    
        			public int authtoken;
	    
        			public String passwordHash;
	    
        			public String gameId;
	    
        			public int recordId;
	    
        			public String role;
	    
        			public boolean locked;
	    
        			public int ip;
	    
        			public long ipChangedAt;
	    
        			public String characterId;
	    
            public List<Character> characters;
	  
    public Player()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Player setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasAuthenticated()  {
        return authenticated == null ? false : true;
    }
        
		public boolean getAuthenticated() {
		return authenticated;
	}
	
	public Player setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
		return this;	}
	
		    
    public Boolean hasAuthtoken()  {
        return authtoken == null ? false : true;
    }
        
		public int getAuthtoken() {
		return authtoken;
	}
	
	public Player setAuthtoken(int authtoken) {
		this.authtoken = authtoken;
		return this;	}
	
		    
    public Boolean hasPasswordHash()  {
        return passwordHash == null ? false : true;
    }
        
		public String getPasswordHash() {
		return passwordHash;
	}
	
	public Player setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		return this;	}
	
		    
    public Boolean hasGameId()  {
        return gameId == null ? false : true;
    }
        
		public String getGameId() {
		return gameId;
	}
	
	public Player setGameId(String gameId) {
		this.gameId = gameId;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public int getRecordId() {
		return recordId;
	}
	
	public Player setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasRole()  {
        return role == null ? false : true;
    }
        
		public String getRole() {
		return role;
	}
	
	public Player setRole(String role) {
		this.role = role;
		return this;	}
	
		    
    public Boolean hasLocked()  {
        return locked == null ? false : true;
    }
        
		public boolean getLocked() {
		return locked;
	}
	
	public Player setLocked(boolean locked) {
		this.locked = locked;
		return this;	}
	
		    
    public Boolean hasIp()  {
        return ip == null ? false : true;
    }
        
		public int getIp() {
		return ip;
	}
	
	public Player setIp(int ip) {
		this.ip = ip;
		return this;	}
	
		    
    public Boolean hasIpChangedAt()  {
        return ipChangedAt == null ? false : true;
    }
        
		public long getIpChangedAt() {
		return ipChangedAt;
	}
	
	public Player setIpChangedAt(long ipChangedAt) {
		this.ipChangedAt = ipChangedAt;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public Player setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasCharacters()  {
        return characters == null ? false : true;
    }
        
		public List<Character> getCharactersList() {
		if(this.characters == null)
            this.characters = new ArrayList<Character>();
		return characters;
	}

	public Player setCharactersList(List<Character> characters) {
		this.characters = characters;
		return this;
	}

	public Character getCharacters(int index)  {
        return characters == null ? null : characters.get(index);
    }

    public int getCharactersCount()  {
        return characters == null ? 0 : characters.size();
    }

    public Player addCharacters(Character characters)  {
        if(this.characters == null)
            this.characters = new ArrayList<Character>();
        this.characters.add(characters);
        return this;
    }
            	    	    	    	
    public Player removeCharactersById(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByUmaData(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.umaData.equals(obj.umaData)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByRecordId(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByPlayerId(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.playerId.equals(obj.playerId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByPart(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.part.equals(obj.part)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByParts(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.parts.equals(obj.parts)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByWorldx(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldx.equals(obj.worldx)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByWorldy(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldy.equals(obj.worldy)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByWorldz(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldz.equals(obj.worldz)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByZone(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.zone.equals(obj.zone)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByIncludeUmaData(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.includeUmaData.equals(obj.includeUmaData)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByVitalsType(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.vitalsType.equals(obj.vitalsType)) {
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
            	                	                	message.authenticated = input.readBool();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.authtoken = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.passwordHash = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.gameId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.role = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.locked = input.readBool();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.ip = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.ipChangedAt = input.readInt64();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 12:
            	            		if(message.characters == null)
                        message.characters = new ArrayList<Character>();
                                        message.characters.add(input.mergeObject(null, Character.getSchema()));
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
    	    	
    	            	
    	    	
    	    	    	if(message.authenticated != null)
            output.writeBool(2, message.authenticated, false);
    	    	
    	            	
    	    	
    	    	    	if(message.authtoken != null)
            output.writeInt32(3, message.authtoken, false);
    	    	
    	            	
    	    	
    	    	    	if(message.passwordHash != null)
            output.writeString(4, message.passwordHash, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gameId != null)
            output.writeString(5, message.gameId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(6, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.role != null)
            output.writeString(7, message.role, false);
    	    	
    	            	
    	    	
    	    	    	if(message.locked != null)
            output.writeBool(8, message.locked, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ip != null)
            output.writeInt32(9, message.ip, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ipChangedAt != null)
            output.writeInt64(10, message.ipChangedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(11, message.characterId, false);
    	    	
    	            	
    	    	
    	    	if(message.characters != null)
        {
            for(Character characters : message.characters)
            {
                if(characters != null) {
                   	    				output.writeObject(12, characters, Character.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "authenticated";
        	        	case 3: return "authtoken";
        	        	case 4: return "passwordHash";
        	        	case 5: return "gameId";
        	        	case 6: return "recordId";
        	        	case 7: return "role";
        	        	case 8: return "locked";
        	        	case 9: return "ip";
        	        	case 10: return "ipChangedAt";
        	        	case 11: return "characterId";
        	        	case 12: return "characters";
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
    	    	__fieldMap.put("authenticated", 2);
    	    	__fieldMap.put("authtoken", 3);
    	    	__fieldMap.put("passwordHash", 4);
    	    	__fieldMap.put("gameId", 5);
    	    	__fieldMap.put("recordId", 6);
    	    	__fieldMap.put("role", 7);
    	    	__fieldMap.put("locked", 8);
    	    	__fieldMap.put("ip", 9);
    	    	__fieldMap.put("ipChangedAt", 10);
    	    	__fieldMap.put("characterId", 11);
    	    	__fieldMap.put("characters", 12);
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
	ProtobufIOUtil.mergeFrom(bytes, message, Player.getSchema());
	return message;
}

public static Player parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Player message = new Player();
	JsonIOUtil.mergeFrom(bytes, message, Player.getSchema(), false);
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

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Player.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Player> schema = Player.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Player.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
