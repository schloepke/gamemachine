
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

public final class PlayersAroundMe  implements Externalizable, Message<PlayersAroundMe>, Schema<PlayersAroundMe>
{




    public static Schema<PlayersAroundMe> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayersAroundMe getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayersAroundMe DEFAULT_INSTANCE = new PlayersAroundMe();



    public List<Player> player;


    


    public PlayersAroundMe()
    {
        
    }






    

	public List<Player> getPlayerList() {
		return player;
	}

	public PlayersAroundMe setPlayerList(List<Player> player) {
		this.player = player;
		return this;
	}

	public Player getPlayer(int index)  {
        return player == null ? null : player.get(index);
    }

    public int getPlayerCount()  {
        return player == null ? 0 : player.size();
    }

    public PlayersAroundMe addPlayer(Player player)  {
        if(this.player == null)
            this.player = new ArrayList<Player>();
        this.player.add(player);
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

    public Schema<PlayersAroundMe> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayersAroundMe newMessage()
    {
        return new PlayersAroundMe();
    }

    public Class<PlayersAroundMe> typeClass()
    {
        return PlayersAroundMe.class;
    }

    public String messageName()
    {
        return PlayersAroundMe.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayersAroundMe.class.getName();
    }

    public boolean isInitialized(PlayersAroundMe message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayersAroundMe message) throws IOException
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


    public void writeTo(Output output, PlayersAroundMe message) throws IOException
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
		fieldName = PlayersAroundMe.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayersAroundMe parseFrom(byte[] bytes) {
	PlayersAroundMe message = new PlayersAroundMe();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(PlayersAroundMe.class));
	return message;
}

public PlayersAroundMe clone() {
	byte[] bytes = this.toByteArray();
	PlayersAroundMe playersAroundMe = PlayersAroundMe.parseFrom(bytes);
	return playersAroundMe;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayersAroundMe.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(PlayersAroundMe.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
