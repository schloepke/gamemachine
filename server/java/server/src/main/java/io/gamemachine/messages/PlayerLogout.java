
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
public final class PlayerLogout implements Externalizable, Message<PlayerLogout>, Schema<PlayerLogout>{



    public static Schema<PlayerLogout> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerLogout getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerLogout DEFAULT_INSTANCE = new PlayerLogout();
    static final String defaultScope = PlayerLogout.class.getSimpleName();

    	
							    public String playerId= null;
		    			    
		
    
        	
							    public int authtoken= 0;
		    			    
		
    
        


    public PlayerLogout()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("player_logout_player_id",null);
    	    	    	    	    	    	model.set("player_logout_authtoken",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("player_logout_player_id",playerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (authtoken != null) {
    	       	    	model.setInteger("player_logout_authtoken",authtoken);
    	        		
    	//}
    	    	    }
    
	public static PlayerLogout fromModel(Model model) {
		boolean hasFields = false;
    	PlayerLogout message = new PlayerLogout();
    	    	    	    	    	
    	    	    	String playerIdTestField = model.getString("player_logout_player_id");
    	if (playerIdTestField != null) {
    		String playerIdField = playerIdTestField;
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer authtokenTestField = model.getInteger("player_logout_authtoken");
    	if (authtokenTestField != null) {
    		int authtokenField = authtokenTestField;
    		message.setAuthtoken(authtokenField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getPlayerId() {
		return playerId;
	}
	
	public PlayerLogout setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		            
		public int getAuthtoken() {
		return authtoken;
	}
	
	public PlayerLogout setAuthtoken(int authtoken) {
		this.authtoken = authtoken;
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

    public Schema<PlayerLogout> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerLogout newMessage()
    {
        return new PlayerLogout();
    }

    public Class<PlayerLogout> typeClass()
    {
        return PlayerLogout.class;
    }

    public String messageName()
    {
        return PlayerLogout.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerLogout.class.getName();
    }

    public boolean isInitialized(PlayerLogout message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerLogout message) throws IOException
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
            	                	                	message.authtoken = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerLogout message) throws IOException
    {
    	    	
    	    	//if(message.playerId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(1, message.playerId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.authtoken != null) {
            output.writeInt32(2, message.authtoken, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START PlayerLogout");
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	//if(this.authtoken != null) {
    		System.out.println("authtoken="+this.authtoken);
    	//}
    	    	System.out.println("END PlayerLogout");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerId";
        	        	case 2: return "authtoken";
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
    	    	__fieldMap.put("authtoken", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerLogout.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerLogout parseFrom(byte[] bytes) {
	PlayerLogout message = new PlayerLogout();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerLogout.getSchema());
	return message;
}

public static PlayerLogout parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerLogout message = new PlayerLogout();
	JsonIOUtil.mergeFrom(bytes, message, PlayerLogout.getSchema(), false);
	return message;
}

public PlayerLogout clone() {
	byte[] bytes = this.toByteArray();
	PlayerLogout playerLogout = PlayerLogout.parseFrom(bytes);
	return playerLogout;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerLogout.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerLogout> schema = PlayerLogout.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerLogout.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, PlayerLogout.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
