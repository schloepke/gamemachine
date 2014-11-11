
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
	    
        			public Boolean authenticated;
	    
        			public String authtoken;
	    
        			public String passwordHash;
	    
        			public String gameId;
	    
        			public Integer recordId;
	    
        			public String role;
	    
        			public Boolean locked;
	    
      
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
        
		public Boolean getAuthenticated() {
		return authenticated;
	}
	
	public Player setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
		return this;	}
	
		    
    public Boolean hasAuthtoken()  {
        return authtoken == null ? false : true;
    }
        
		public String getAuthtoken() {
		return authtoken;
	}
	
	public Player setAuthtoken(String authtoken) {
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
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public Player setRecordId(Integer recordId) {
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
        
		public Boolean getLocked() {
		return locked;
	}
	
	public Player setLocked(Boolean locked) {
		this.locked = locked;
		return this;	}
	
	
  
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
            	                	                	message.authtoken = input.readString();
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
            output.writeString(3, message.authtoken, false);
    	    	
    	            	
    	    	
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
