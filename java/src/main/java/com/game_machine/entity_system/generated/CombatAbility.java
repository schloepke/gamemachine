
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

public final class CombatAbility  implements Externalizable, Message<CombatAbility>, Schema<CombatAbility>
{




    public static Schema<CombatAbility> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CombatAbility getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CombatAbility DEFAULT_INSTANCE = new CombatAbility();



    public Integer id;



    public String name;



    public Integer damage;



    public Integer hitChance;



    public Integer range;



    public String type;


    


    public CombatAbility()
    {
        
    }






    

	public Integer getId() {
		return id;
	}
	
	public CombatAbility setId(Integer id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }



    

	public String getName() {
		return name;
	}
	
	public CombatAbility setName(String name) {
		this.name = name;
		return this;
	}
	
	public Boolean hasName()  {
        return name == null ? false : true;
    }



    

	public Integer getDamage() {
		return damage;
	}
	
	public CombatAbility setDamage(Integer damage) {
		this.damage = damage;
		return this;
	}
	
	public Boolean hasDamage()  {
        return damage == null ? false : true;
    }



    

	public Integer getHitChance() {
		return hitChance;
	}
	
	public CombatAbility setHitChance(Integer hitChance) {
		this.hitChance = hitChance;
		return this;
	}
	
	public Boolean hasHitChance()  {
        return hitChance == null ? false : true;
    }



    

	public Integer getRange() {
		return range;
	}
	
	public CombatAbility setRange(Integer range) {
		this.range = range;
		return this;
	}
	
	public Boolean hasRange()  {
        return range == null ? false : true;
    }



    

	public String getType() {
		return type;
	}
	
	public CombatAbility setType(String type) {
		this.type = type;
		return this;
	}
	
	public Boolean hasType()  {
        return type == null ? false : true;
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

    public Schema<CombatAbility> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CombatAbility newMessage()
    {
        return new CombatAbility();
    }

    public Class<CombatAbility> typeClass()
    {
        return CombatAbility.class;
    }

    public String messageName()
    {
        return CombatAbility.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CombatAbility.class.getName();
    }

    public boolean isInitialized(CombatAbility message)
    {
        return true;
    }

    public void mergeFrom(Input input, CombatAbility message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.id = input.readInt32();
                	break;

                	


            	case 2:


                	message.name = input.readString();
                	break;

                	


            	case 3:


                	message.damage = input.readInt32();
                	break;

                	


            	case 4:


                	message.hitChance = input.readInt32();
                	break;

                	


            	case 5:


                	message.range = input.readInt32();
                	break;

                	


            	case 6:


                	message.type = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CombatAbility message) throws IOException
    {

    	

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.id != null)
            output.writeInt32(1, message.id, false);

    	


    	

    	if(message.name == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.name != null)
            output.writeString(2, message.name, false);

    	


    	

    	if(message.damage == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.damage != null)
            output.writeInt32(3, message.damage, false);

    	


    	

    	


    	if(message.hitChance != null)
            output.writeInt32(4, message.hitChance, false);

    	


    	

    	if(message.range == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.range != null)
            output.writeInt32(5, message.range, false);

    	


    	

    	if(message.type == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.type != null)
            output.writeString(6, message.type, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "id";

        	case 2: return "name";

        	case 3: return "damage";

        	case 4: return "hitChance";

        	case 5: return "range";

        	case 6: return "type";

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

    	__fieldMap.put("id", 1);

    	__fieldMap.put("name", 2);

    	__fieldMap.put("damage", 3);

    	__fieldMap.put("hitChance", 4);

    	__fieldMap.put("range", 5);

    	__fieldMap.put("type", 6);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CombatAbility.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CombatAbility parseFrom(byte[] bytes) {
	CombatAbility message = new CombatAbility();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(CombatAbility.class));
	return message;
}

public CombatAbility clone() {
	byte[] bytes = this.toByteArray();
	CombatAbility combatAbility = CombatAbility.parseFrom(bytes);
	return combatAbility;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CombatAbility.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(CombatAbility.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
