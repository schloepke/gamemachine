
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



    public String attacker;



    public String target;



    public Integer combatAbilityId;


    


    public Attack()
    {
        
    }






    

	public String getAttacker() {
		return attacker;
	}
	
	public Attack setAttacker(String attacker) {
		this.attacker = attacker;
		return this;
	}
	
	public Boolean hasAttacker()  {
        return attacker == null ? false : true;
    }



    

	public String getTarget() {
		return target;
	}
	
	public Attack setTarget(String target) {
		this.target = target;
		return this;
	}
	
	public Boolean hasTarget()  {
        return target == null ? false : true;
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


                	message.attacker = input.readString();
                	break;

                	


            	case 2:


                	message.target = input.readString();
                	break;

                	


            	case 3:


                	message.combatAbilityId = input.readInt32();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Attack message) throws IOException
    {

    	

    	if(message.attacker == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.attacker != null)
            output.writeString(1, message.attacker, false);

    	


    	

    	if(message.target == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.target != null)
            output.writeString(2, message.target, false);

    	


    	

    	if(message.combatAbilityId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.combatAbilityId != null)
            output.writeInt32(3, message.combatAbilityId, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "attacker";

        	case 2: return "target";

        	case 3: return "combatAbilityId";

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

    	__fieldMap.put("attacker", 1);

    	__fieldMap.put("target", 2);

    	__fieldMap.put("combatAbilityId", 3);

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
