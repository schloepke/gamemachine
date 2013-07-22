
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

public final class PlayerAttack  implements Externalizable, Message<PlayerAttack>, Schema<PlayerAttack>
{




    public static Schema<PlayerAttack> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerAttack getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerAttack DEFAULT_INSTANCE = new PlayerAttack();



    public Target target;



    public String ability;


    


    public PlayerAttack()
    {
        
    }






    

	public Target getTarget() {
		return target;
	}
	
	public PlayerAttack setTarget(Target target) {
		this.target = target;
		return this;
	}
	
	public Boolean hasTarget()  {
        return target == null ? false : true;
    }



    

	public String getAbility() {
		return ability;
	}
	
	public PlayerAttack setAbility(String ability) {
		this.ability = ability;
		return this;
	}
	
	public Boolean hasAbility()  {
        return ability == null ? false : true;
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

    public Schema<PlayerAttack> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerAttack newMessage()
    {
        return new PlayerAttack();
    }

    public Class<PlayerAttack> typeClass()
    {
        return PlayerAttack.class;
    }

    public String messageName()
    {
        return PlayerAttack.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerAttack.class.getName();
    }

    public boolean isInitialized(PlayerAttack message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerAttack message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.target = input.mergeObject(message.target, Target.getSchema());
                    break;

                	


            	case 2:


                	message.ability = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerAttack message) throws IOException
    {

    	

    	if(message.target == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.target != null)
    		output.writeObject(1, message.target, Target.getSchema(), false);

    	


    	

    	if(message.ability == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.ability != null)
            output.writeString(2, message.ability, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "target";

        	case 2: return "ability";

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

    	__fieldMap.put("target", 1);

    	__fieldMap.put("ability", 2);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerAttack.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerAttack parseFrom(byte[] bytes) {
	PlayerAttack message = new PlayerAttack();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(PlayerAttack.class));
	return message;
}

public PlayerAttack clone() {
	byte[] bytes = this.toByteArray();
	PlayerAttack playerAttack = PlayerAttack.parseFrom(bytes);
	return playerAttack;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerAttack.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(PlayerAttack.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
