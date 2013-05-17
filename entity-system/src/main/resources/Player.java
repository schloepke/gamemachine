
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

public final class Player extends com.game_machine.entity_system.Component implements Externalizable, Message<Player>, Schema<Player>
{

    public static Schema<Player> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Player getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Player DEFAULT_INSTANCE = new Player();

    private int x;
    private int y;
    private int z;
    private int id;
    private String name;
    private int entityId;
    

    public Player()
    {
        
    }

	public int getint() {
		return x;
	}
	public void setint(int x) {
		this.x = x;
	}
	public int getint() {
		return y;
	}
	public void setint(int y) {
		this.y = y;
	}
	public int getint() {
		return z;
	}
	public void setint(int z) {
		this.z = z;
	}
	public int getint() {
		return id;
	}
	public void setint(int id) {
		this.id = id;
	}
	public String getString() {
		return name;
	}
	public void setString(String name) {
		this.name = name;
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
                	message.x = input.readInt32();
                    break;
                	message.x = input.readInt32();
                    break;
                	message.x = input.readInt32();
                    break;
                	message.x = input.readInt32();
                    break;
                	message.x = input.readString();
                    break;
                	message.x = input.readInt32();
                    break;
            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Player message) throws IOException
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
    
    static final Pipe.Schema<Player> PIPE_SCHEMA = new Pipe.Schema<Player>(DEFAULT_INSTANCE)
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

    public static Pipe.Schema<Player> getPipeSchema()
    {
        return PIPE_SCHEMA;
    }

    

}
