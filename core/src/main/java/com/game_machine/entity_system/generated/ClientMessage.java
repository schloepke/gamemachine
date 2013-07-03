
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

public final class ClientMessage  implements Externalizable, Message<ClientMessage>, Schema<ClientMessage>
{




    public static Schema<ClientMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientMessage DEFAULT_INSTANCE = new ClientMessage();



    public List<Entity> entity;



    public Player player;



    public PlayerLogin playerLogin;



    public ClientConnection clientConnection;


    


    public ClientMessage()
    {
        
    }






    

	public List<Entity> getEntityList() {
		return entity;
	}

	public ClientMessage setEntityList(List<Entity> entity) {
		this.entity = entity;
		return this;
	}

	public Entity getEntity(int index)  {
        return entity == null ? null : entity.get(index);
    }

    public int getEntityCount()  {
        return entity == null ? 0 : entity.size();
    }

    public ClientMessage addEntity(Entity entity)  {
        if(this.entity == null)
            this.entity = new ArrayList<Entity>();
        this.entity.add(entity);
        return this;
    }
    



    

	public Player getPlayer() {
		return player;
	}
	
	public ClientMessage setPlayer(Player player) {
		this.player = player;
		return this;
	}
	
	public Boolean hasPlayer()  {
        return player == null ? false : true;
    }



    

	public PlayerLogin getPlayerLogin() {
		return playerLogin;
	}
	
	public ClientMessage setPlayerLogin(PlayerLogin playerLogin) {
		this.playerLogin = playerLogin;
		return this;
	}
	
	public Boolean hasPlayerLogin()  {
        return playerLogin == null ? false : true;
    }



    

	public ClientConnection getClientConnection() {
		return clientConnection;
	}
	
	public ClientMessage setClientConnection(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
		return this;
	}
	
	public Boolean hasClientConnection()  {
        return clientConnection == null ? false : true;
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

    public Schema<ClientMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientMessage newMessage()
    {
        return new ClientMessage();
    }

    public Class<ClientMessage> typeClass()
    {
        return ClientMessage.class;
    }

    public String messageName()
    {
        return ClientMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientMessage.class.getName();
    }

    public boolean isInitialized(ClientMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:

            		if(message.entity == null)
                        message.entity = new ArrayList<Entity>();

                    message.entity.add(input.mergeObject(null, Entity.getSchema()));

                    break;


            	case 2:


                	message.player = input.mergeObject(message.player, Player.getSchema());
                    break;

                	


            	case 3:


                	message.playerLogin = input.mergeObject(message.playerLogin, PlayerLogin.getSchema());
                    break;

                	


            	case 4:


                	message.clientConnection = input.mergeObject(message.clientConnection, ClientConnection.getSchema());
                    break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ClientMessage message) throws IOException
    {

    	

    	

    	if(message.entity != null)
        {
            for(Entity entity : message.entity)
            {
                if(entity != null) {

    				output.writeObject(1, entity, Entity.getSchema(), true);

    			}
            }
        }


    	

    	


    	if(message.player != null)
    		output.writeObject(2, message.player, Player.getSchema(), false);

    	


    	

    	


    	if(message.playerLogin != null)
    		output.writeObject(3, message.playerLogin, PlayerLogin.getSchema(), false);

    	


    	

    	


    	if(message.clientConnection != null)
    		output.writeObject(4, message.clientConnection, ClientConnection.getSchema(), false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "entity";

        	case 2: return "player";

        	case 3: return "playerLogin";

        	case 4: return "clientConnection";

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

    	__fieldMap.put("entity", 1);

    	__fieldMap.put("player", 2);

    	__fieldMap.put("playerLogin", 3);

    	__fieldMap.put("clientConnection", 4);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientMessage parseFrom(byte[] bytes) {
	ClientMessage message = new ClientMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ClientMessage.class));
	return message;
}

public ClientMessage clone() {
	byte[] bytes = this.toByteArray();
	ClientMessage clientMessage = ClientMessage.parseFrom(bytes);
	return clientMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientMessage.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ClientMessage.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
