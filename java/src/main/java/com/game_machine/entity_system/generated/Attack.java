
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

public final class Attack  implements Externalizable, Message<Attack>, Schema<Attack>
{




    public static Schema<Attack> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Attack getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Attack DEFAULT_INSTANCE = new Attack();



    public String attackerId;



    public Integer attackerType;



    public List<String> npcId;



    public List<String> playerId;



    public Integer combatAbilityId;


    


    public Attack()
    {
        
    }






    

	public String getAttackerId() {
		return attackerId;
	}
	
	public Attack setAttackerId(String attackerId) {
		this.attackerId = attackerId;
		return this;
	}
	
	public Boolean hasAttackerId()  {
        return attackerId == null ? false : true;
    }



    

	public Integer getAttackerType() {
		return attackerType;
	}
	
	public Attack setAttackerType(Integer attackerType) {
		this.attackerType = attackerType;
		return this;
	}
	
	public Boolean hasAttackerType()  {
        return attackerType == null ? false : true;
    }



    

	public List<String> getNpcIdList() {
		return npcId;
	}

	public Attack setNpcIdList(List<String> npcId) {
		this.npcId = npcId;
		return this;
	}

	public String getNpcId(int index)  {
        return npcId == null ? null : npcId.get(index);
    }

    public int getNpcIdCount()  {
        return npcId == null ? 0 : npcId.size();
    }

    public Attack addNpcId(String npcId)  {
        if(this.npcId == null)
            this.npcId = new ArrayList<String>();
        this.npcId.add(npcId);
        return this;
    }
    



    

	public List<String> getPlayerIdList() {
		return playerId;
	}

	public Attack setPlayerIdList(List<String> playerId) {
		this.playerId = playerId;
		return this;
	}

	public String getPlayerId(int index)  {
        return playerId == null ? null : playerId.get(index);
    }

    public int getPlayerIdCount()  {
        return playerId == null ? 0 : playerId.size();
    }

    public Attack addPlayerId(String playerId)  {
        if(this.playerId == null)
            this.playerId = new ArrayList<String>();
        this.playerId.add(playerId);
        return this;
    }
    



    

	public Integer getCombatAbilityId() {
		return combatAbilityId;
	}
	
	public Attack setCombatAbilityId(Integer combatAbilityId) {
		this.combatAbilityId = combatAbilityId;
		return this;
	}
	
	public Boolean hasCombatAbilityId()  {
        return combatAbilityId == null ? false : true;
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

    public Schema<Attack> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Attack newMessage()
    {
        return new Attack();
    }

    public Class<Attack> typeClass()
    {
        return Attack.class;
    }

    public String messageName()
    {
        return Attack.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Attack.class.getName();
    }

    public boolean isInitialized(Attack message)
    {
        return true;
    }

    public void mergeFrom(Input input, Attack message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.attackerId = input.readString();
                	break;

                	


            	case 2:


                	message.attackerType = input.readInt32();
                	break;

                	


            	case 3:

            		if(message.npcId == null)
                        message.npcId = new ArrayList<String>();

                	message.npcId.add(input.readString());

                    break;


            	case 4:

            		if(message.playerId == null)
                        message.playerId = new ArrayList<String>();

                	message.playerId.add(input.readString());

                    break;


            	case 5:


                	message.combatAbilityId = input.readInt32();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Attack message) throws IOException
    {

    	

    	if(message.attackerId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.attackerId != null)
            output.writeString(1, message.attackerId, false);

    	


    	

    	if(message.attackerType == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.attackerType != null)
            output.writeInt32(2, message.attackerType, false);

    	


    	

    	

    	if(message.npcId != null)
        {
            for(String npcId : message.npcId)
            {
                if(npcId != null) {

            		output.writeString(3, npcId, true);

    			}
            }
        }


    	

    	

    	if(message.playerId != null)
        {
            for(String playerId : message.playerId)
            {
                if(playerId != null) {

            		output.writeString(4, playerId, true);

    			}
            }
        }


    	

    	if(message.combatAbilityId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.combatAbilityId != null)
            output.writeInt32(5, message.combatAbilityId, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "attackerId";

        	case 2: return "attackerType";

        	case 3: return "npcId";

        	case 4: return "playerId";

        	case 5: return "combatAbilityId";

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

    	__fieldMap.put("attackerId", 1);

    	__fieldMap.put("attackerType", 2);

    	__fieldMap.put("npcId", 3);

    	__fieldMap.put("playerId", 4);

    	__fieldMap.put("combatAbilityId", 5);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Attack.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Attack parseFrom(byte[] bytes) {
	Attack message = new Attack();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Attack.class));
	return message;
}

public Attack clone() {
	byte[] bytes = this.toByteArray();
	Attack attack = Attack.parseFrom(bytes);
	return attack;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Attack.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Attack.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
