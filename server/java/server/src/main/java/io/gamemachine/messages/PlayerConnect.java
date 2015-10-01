
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
public final class PlayerConnect implements Externalizable, Message<PlayerConnect>, Schema<PlayerConnect>{



    public static Schema<PlayerConnect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerConnect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerConnect DEFAULT_INSTANCE = new PlayerConnect();
    static final String defaultScope = PlayerConnect.class.getSimpleName();

    			public String playerId;
	    
        			public String password;
	    
        


    public PlayerConnect()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("player_connect_player_id",null);
    	    	    	    	    	    	model.set("player_connect_password",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (playerId != null) {
    	       	    	model.setString("player_connect_player_id",playerId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (password != null) {
    	       	    	model.setString("player_connect_password",password);
    	        		
    	}
    	    	    }
    
	public static PlayerConnect fromModel(Model model) {
		boolean hasFields = false;
    	PlayerConnect message = new PlayerConnect();
    	    	    	    	    	
    	    	    	String playerIdField = model.getString("player_connect_player_id");
    	    	
    	if (playerIdField != null) {
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String passwordField = model.getString("player_connect_password");
    	    	
    	if (passwordField != null) {
    		message.setPassword(passwordField);
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
	
	public PlayerConnect setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		    
    public Boolean hasPassword()  {
        return password == null ? false : true;
    }
        
		public String getPassword() {
		return password;
	}
	
	public PlayerConnect setPassword(String password) {
		this.password = password;
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

    public Schema<PlayerConnect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerConnect newMessage()
    {
        return new PlayerConnect();
    }

    public Class<PlayerConnect> typeClass()
    {
        return PlayerConnect.class;
    }

    public String messageName()
    {
        return PlayerConnect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerConnect.class.getName();
    }

    public boolean isInitialized(PlayerConnect message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerConnect message) throws IOException
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
            	                	                	message.password = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerConnect message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(1, message.playerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.password != null)
            output.writeString(2, message.password, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START PlayerConnect");
    	    	if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	}
    	    	if(this.password != null) {
    		System.out.println("password="+this.password);
    	}
    	    	System.out.println("END PlayerConnect");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerId";
        	        	case 2: return "password";
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
    	    	__fieldMap.put("password", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerConnect.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerConnect parseFrom(byte[] bytes) {
	PlayerConnect message = new PlayerConnect();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerConnect.getSchema());
	return message;
}

public static PlayerConnect parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerConnect message = new PlayerConnect();
	JsonIOUtil.mergeFrom(bytes, message, PlayerConnect.getSchema(), false);
	return message;
}

public PlayerConnect clone() {
	byte[] bytes = this.toByteArray();
	PlayerConnect playerConnect = PlayerConnect.parseFrom(bytes);
	return playerConnect;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerConnect.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerConnect> schema = PlayerConnect.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerConnect.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, PlayerConnect.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
