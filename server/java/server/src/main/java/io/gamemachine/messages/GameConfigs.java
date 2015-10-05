
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
public final class GameConfigs implements Externalizable, Message<GameConfigs>, Schema<GameConfigs>{



    public static Schema<GameConfigs> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GameConfigs getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GameConfigs DEFAULT_INSTANCE = new GameConfigs();
    static final String defaultScope = GameConfigs.class.getSimpleName();

        public List<GameConfig> gameConfig;
	    


    public GameConfigs()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static GameConfigs fromModel(Model model) {
		boolean hasFields = false;
    	GameConfigs message = new GameConfigs();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public List<GameConfig> getGameConfigList() {
		if(this.gameConfig == null)
            this.gameConfig = new ArrayList<GameConfig>();
		return gameConfig;
	}

	public GameConfigs setGameConfigList(List<GameConfig> gameConfig) {
		this.gameConfig = gameConfig;
		return this;
	}

	public GameConfig getGameConfig(int index)  {
        return gameConfig == null ? null : gameConfig.get(index);
    }

    public int getGameConfigCount()  {
        return gameConfig == null ? 0 : gameConfig.size();
    }

    public GameConfigs addGameConfig(GameConfig gameConfig)  {
        if(this.gameConfig == null)
            this.gameConfig = new ArrayList<GameConfig>();
        this.gameConfig.add(gameConfig);
        return this;
    }
            	    	    	    	
    public GameConfigs removeGameConfigByConfig(GameConfig gameConfig)  {
    	if(this.gameConfig == null)
           return this;
            
       	Iterator<GameConfig> itr = this.gameConfig.iterator();
       	while (itr.hasNext()) {
    	GameConfig obj = itr.next();
    	
    	    		if (gameConfig.config.equals(obj.config)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GameConfigs removeGameConfigByVersion(GameConfig gameConfig)  {
    	if(this.gameConfig == null)
           return this;
            
       	Iterator<GameConfig> itr = this.gameConfig.iterator();
       	while (itr.hasNext()) {
    	GameConfig obj = itr.next();
    	
    	    		if (gameConfig.version == obj.version) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GameConfigs removeGameConfigByGameId(GameConfig gameConfig)  {
    	if(this.gameConfig == null)
           return this;
            
       	Iterator<GameConfig> itr = this.gameConfig.iterator();
       	while (itr.hasNext()) {
    	GameConfig obj = itr.next();
    	
    	    		if (gameConfig.gameId.equals(obj.gameId)) {
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

    public Schema<GameConfigs> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GameConfigs newMessage()
    {
        return new GameConfigs();
    }

    public Class<GameConfigs> typeClass()
    {
        return GameConfigs.class;
    }

    public String messageName()
    {
        return GameConfigs.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GameConfigs.class.getName();
    }

    public boolean isInitialized(GameConfigs message)
    {
        return true;
    }

    public void mergeFrom(Input input, GameConfigs message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.gameConfig == null)
                        message.gameConfig = new ArrayList<GameConfig>();
                                        message.gameConfig.add(input.mergeObject(null, GameConfig.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GameConfigs message) throws IOException
    {
    	    	
    	    	
    	    	if(message.gameConfig != null)
        {
            for(GameConfig gameConfig : message.gameConfig)
            {
                if( (GameConfig) gameConfig != null) {
                   	    				output.writeObject(1, gameConfig, GameConfig.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GameConfigs");
    	    	//if(this.gameConfig != null) {
    		System.out.println("gameConfig="+this.gameConfig);
    	//}
    	    	System.out.println("END GameConfigs");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "gameConfig";
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
    	    	__fieldMap.put("gameConfig", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GameConfigs.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GameConfigs parseFrom(byte[] bytes) {
	GameConfigs message = new GameConfigs();
	ProtobufIOUtil.mergeFrom(bytes, message, GameConfigs.getSchema());
	return message;
}

public static GameConfigs parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GameConfigs message = new GameConfigs();
	JsonIOUtil.mergeFrom(bytes, message, GameConfigs.getSchema(), false);
	return message;
}

public GameConfigs clone() {
	byte[] bytes = this.toByteArray();
	GameConfigs gameConfigs = GameConfigs.parseFrom(bytes);
	return gameConfigs;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GameConfigs.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GameConfigs> schema = GameConfigs.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GameConfigs.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GameConfigs.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
