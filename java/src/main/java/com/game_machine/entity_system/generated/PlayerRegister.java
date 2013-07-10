
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

public final class PlayerRegister  implements Externalizable, Message<PlayerRegister>, Schema<PlayerRegister>
{




    public static Schema<PlayerRegister> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerRegister getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerRegister DEFAULT_INSTANCE = new PlayerRegister();



    public String playerId;



    public ClientConnection clientConnection;



    public String observer;


    


    public PlayerRegister()
    {
        
    }






    

	public String getPlayerId() {
		return playerId;
	}
	
	public PlayerRegister setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;
	}
	
	public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }



    

	public ClientConnection getClientConnection() {
		return clientConnection;
	}
	
	public PlayerRegister setClientConnection(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
		return this;
	}
	
	public Boolean hasClientConnection()  {
        return clientConnection == null ? false : true;
    }



    

	public String getObserver() {
		return observer;
	}
	
	public PlayerRegister setObserver(String observer) {
		this.observer = observer;
		return this;
	}
	
	public Boolean hasObserver()  {
        return observer == null ? false : true;
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

    public Schema<PlayerRegister> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerRegister newMessage()
    {
        return new PlayerRegister();
    }

    public Class<PlayerRegister> typeClass()
    {
        return PlayerRegister.class;
    }

    public String messageName()
    {
        return PlayerRegister.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerRegister.class.getName();
    }

    public boolean isInitialized(PlayerRegister message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerRegister message) throws IOException
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


                	message.clientConnection = input.mergeObject(message.clientConnection, ClientConnection.getSchema());
                    break;

                	


            	case 3:


                	message.observer = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerRegister message) throws IOException
    {

    	

    	if(message.playerId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.playerId != null)
            output.writeString(1, message.playerId, false);

    	


    	

    	if(message.clientConnection == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.clientConnection != null)
    		output.writeObject(2, message.clientConnection, ClientConnection.getSchema(), false);

    	


    	

    	


    	if(message.observer != null)
            output.writeString(3, message.observer, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "playerId";

        	case 2: return "clientConnection";

        	case 3: return "observer";

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

    	__fieldMap.put("clientConnection", 2);

    	__fieldMap.put("observer", 3);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerRegister.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerRegister parseFrom(byte[] bytes) {
	PlayerRegister message = new PlayerRegister();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(PlayerRegister.class));
	return message;
}

public PlayerRegister clone() {
	byte[] bytes = this.toByteArray();
	PlayerRegister playerRegister = PlayerRegister.parseFrom(bytes);
	return playerRegister;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerRegister.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(PlayerRegister.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
