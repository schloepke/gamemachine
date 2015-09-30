
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
public final class GameConfig implements Externalizable, Message<GameConfig>, Schema<GameConfig>{



    public static Schema<GameConfig> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GameConfig getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GameConfig DEFAULT_INSTANCE = new GameConfig();

    			public String config;
	    
        			public Integer version;
	    
        			public String gameId;
	    
      
    public GameConfig()
    {
        
    }


	

	    
    public Boolean hasConfig()  {
        return config == null ? false : true;
    }
        
		public String getConfig() {
		return config;
	}
	
	public GameConfig setConfig(String config) {
		this.config = config;
		return this;	}
	
		    
    public Boolean hasVersion()  {
        return version == null ? false : true;
    }
        
		public Integer getVersion() {
		return version;
	}
	
	public GameConfig setVersion(Integer version) {
		this.version = version;
		return this;	}
	
		    
    public Boolean hasGameId()  {
        return gameId == null ? false : true;
    }
        
		public String getGameId() {
		return gameId;
	}
	
	public GameConfig setGameId(String gameId) {
		this.gameId = gameId;
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

    public Schema<GameConfig> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GameConfig newMessage()
    {
        return new GameConfig();
    }

    public Class<GameConfig> typeClass()
    {
        return GameConfig.class;
    }

    public String messageName()
    {
        return GameConfig.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GameConfig.class.getName();
    }

    public boolean isInitialized(GameConfig message)
    {
        return true;
    }

    public void mergeFrom(Input input, GameConfig message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.config = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.version = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.gameId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GameConfig message) throws IOException
    {
    	    	
    	    	if(message.config == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.config != null)
            output.writeString(1, message.config, false);
    	    	
    	            	
    	    	if(message.version == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.version != null)
            output.writeInt32(2, message.version, false);
    	    	
    	            	
    	    	if(message.gameId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.gameId != null)
            output.writeString(3, message.gameId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "config";
        	        	case 2: return "version";
        	        	case 3: return "gameId";
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
    	    	__fieldMap.put("config", 1);
    	    	__fieldMap.put("version", 2);
    	    	__fieldMap.put("gameId", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GameConfig.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GameConfig parseFrom(byte[] bytes) {
	GameConfig message = new GameConfig();
	ProtobufIOUtil.mergeFrom(bytes, message, GameConfig.getSchema());
	return message;
}

public static GameConfig parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GameConfig message = new GameConfig();
	JsonIOUtil.mergeFrom(bytes, message, GameConfig.getSchema(), false);
	return message;
}

public GameConfig clone() {
	byte[] bytes = this.toByteArray();
	GameConfig gameConfig = GameConfig.parseFrom(bytes);
	return gameConfig;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GameConfig.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GameConfig> schema = GameConfig.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GameConfig.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
