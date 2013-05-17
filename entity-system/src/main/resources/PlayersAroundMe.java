
package com.game_machine.entity_system.generated;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

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
import com.game_machine.entity_system.Entity;
import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.Component;

import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;

public final class PlayersAroundMe extends com.game_machine.entity_system.Component implements Externalizable, Message<PlayersAroundMe>, Schema<PlayersAroundMe>
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

    private List<Player> player;
    private int entityId;
    

    public PlayersAroundMe()
    {
        
    }

	public List<Player> getPlayerList() {
		return player;
	}

	public void setPlayerList(List<Player> player) {
		this.player = player;
	}

	public Player getPlayer(int index)  {
        return player == null ? null : player.get(index);
    }

    public int getPlayerCount()  {
        return player == null ? 0 : player.size();
    }

    public void addPlayer(Player player)  {
        if(this.player == null)
            this.player = new ArrayList<Player>();
        this.player.add(player);
    }
	public int getint() {
		return entityId;
	}
	public void setint(int entityId) {
		this.entityId = entityId;
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
                	message.x = input.readInt32();
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
                if(player != null)
                    output.writeObject(1, player, Player.getSchema(), true);
            }
        }


        if(message.playersAroundMe != null)
        {
            for(PlayersAroundMe playersAroundMe : message.playersAroundMe)
            {
                if(playersAroundMe != null)
                    output.writeObject(2, playersAroundMe, PlayersAroundMe.getSchema(), true);
            }
        }


        if(message.gameCommand != null)
        {
            for(GameCommand gameCommand : message.gameCommand)
            {
                if(gameCommand != null)
                    output.writeObject(3, gameCommand, GameCommand.getSchema(), true);
            }
        }

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
            case 1: return "player";
            case 2: return "playersAroundMe";
            case 3: return "gameCommand";
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
        __fieldMap.put("playersAroundMe", 2);
        __fieldMap.put("gameCommand", 3);
    }
    
    static final Pipe.Schema<PlayersAroundMe> PIPE_SCHEMA = new Pipe.Schema<PlayersAroundMe>(DEFAULT_INSTANCE)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(wrappedSchema);; number = input.readFieldNumber(wrappedSchema))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, Player.getPipeSchema(), true);
                        break;

                    case 2:
                        output.writeObject(number, pipe, PlayersAroundMe.getPipeSchema(), true);
                        break;

                    case 3:
                        output.writeObject(number, pipe, GameCommand.getPipeSchema(), true);
                        break;

                    default:
                        input.handleUnknownField(number, wrappedSchema);
                }
            }
        }
    };

    public static Pipe.Schema<PlayersAroundMe> getPipeSchema()
    {
        return PIPE_SCHEMA;
    }

    

}
