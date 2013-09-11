
package com.game_machine.entity_system.generated;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.game_machine.entity_system.generated.Entity;

import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

public final class PlayerAuthenticated  implements Externalizable, Message<PlayerAuthenticated>, Schema<PlayerAuthenticated>
{




    public static Schema<PlayerAuthenticated> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerAuthenticated getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerAuthenticated DEFAULT_INSTANCE = new PlayerAuthenticated();



    public String playerId;


    


    public PlayerAuthenticated()
    {
        
    }






    

	public String getPlayerId() {
		return playerId;
	}
	
	public PlayerAuthenticated setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;
	}
	
	public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
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

    public Schema<PlayerAuthenticated> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerAuthenticated newMessage()
    {
        return new PlayerAuthenticated();
    }

    public Class<PlayerAuthenticated> typeClass()
    {
        return PlayerAuthenticated.class;
    }

    public String messageName()
    {
        return PlayerAuthenticated.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerAuthenticated.class.getName();
    }

    public boolean isInitialized(PlayerAuthenticated message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerAuthenticated message) throws IOException
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

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerAuthenticated message) throws IOException
    {

    	

    	if(message.playerId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.playerId != null)
            output.writeString(1, message.playerId, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "playerId";

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

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerAuthenticated.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerAuthenticated parseFrom(byte[] bytes) {
	PlayerAuthenticated message = new PlayerAuthenticated();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(PlayerAuthenticated.class));
	return message;
}

public PlayerAuthenticated clone() {
	byte[] bytes = this.toByteArray();
	PlayerAuthenticated playerAuthenticated = PlayerAuthenticated.parseFrom(bytes);
	return playerAuthenticated;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerAuthenticated.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}
		
public byte[] toProtobuf() {
	LinkedBuffer buffer = LinkedBuffer.allocate(8024);
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(PlayerAuthenticated.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
