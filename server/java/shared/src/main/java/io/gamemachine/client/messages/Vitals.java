
package io.gamemachine.client.messages;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class Vitals implements Externalizable, Message<Vitals>, Schema<Vitals>{



    public static Schema<Vitals> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Vitals getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Vitals DEFAULT_INSTANCE = new Vitals();

    			public String id;
	    
        			public Integer health;
	    
        			public Integer stamina;
	    
        			public Integer magic;
	    
        			public Long lastCombat;
	    
        			public Integer dead;
	    
        			public Integer armor;
	    
        			public Integer spellResist;
	    
        			public Integer elementalResist;
	    
        			public Integer spellPenetration;
	    
        			public Integer magicRegen;
	    
        			public Integer healthRegen;
	    
        			public Integer staminaRegen;
	    
        			public Integer changed;
	    
        			public String grid;
	    
        			public Integer staminaDrain;
	    
      
    public Vitals()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Vitals setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasHealth()  {
        return health == null ? false : true;
    }
        
		public Integer getHealth() {
		return health;
	}
	
	public Vitals setHealth(Integer health) {
		this.health = health;
		return this;	}
	
		    
    public Boolean hasStamina()  {
        return stamina == null ? false : true;
    }
        
		public Integer getStamina() {
		return stamina;
	}
	
	public Vitals setStamina(Integer stamina) {
		this.stamina = stamina;
		return this;	}
	
		    
    public Boolean hasMagic()  {
        return magic == null ? false : true;
    }
        
		public Integer getMagic() {
		return magic;
	}
	
	public Vitals setMagic(Integer magic) {
		this.magic = magic;
		return this;	}
	
		    
    public Boolean hasLastCombat()  {
        return lastCombat == null ? false : true;
    }
        
		public Long getLastCombat() {
		return lastCombat;
	}
	
	public Vitals setLastCombat(Long lastCombat) {
		this.lastCombat = lastCombat;
		return this;	}
	
		    
    public Boolean hasDead()  {
        return dead == null ? false : true;
    }
        
		public Integer getDead() {
		return dead;
	}
	
	public Vitals setDead(Integer dead) {
		this.dead = dead;
		return this;	}
	
		    
    public Boolean hasArmor()  {
        return armor == null ? false : true;
    }
        
		public Integer getArmor() {
		return armor;
	}
	
	public Vitals setArmor(Integer armor) {
		this.armor = armor;
		return this;	}
	
		    
    public Boolean hasSpellResist()  {
        return spellResist == null ? false : true;
    }
        
		public Integer getSpellResist() {
		return spellResist;
	}
	
	public Vitals setSpellResist(Integer spellResist) {
		this.spellResist = spellResist;
		return this;	}
	
		    
    public Boolean hasElementalResist()  {
        return elementalResist == null ? false : true;
    }
        
		public Integer getElementalResist() {
		return elementalResist;
	}
	
	public Vitals setElementalResist(Integer elementalResist) {
		this.elementalResist = elementalResist;
		return this;	}
	
		    
    public Boolean hasSpellPenetration()  {
        return spellPenetration == null ? false : true;
    }
        
		public Integer getSpellPenetration() {
		return spellPenetration;
	}
	
	public Vitals setSpellPenetration(Integer spellPenetration) {
		this.spellPenetration = spellPenetration;
		return this;	}
	
		    
    public Boolean hasMagicRegen()  {
        return magicRegen == null ? false : true;
    }
        
		public Integer getMagicRegen() {
		return magicRegen;
	}
	
	public Vitals setMagicRegen(Integer magicRegen) {
		this.magicRegen = magicRegen;
		return this;	}
	
		    
    public Boolean hasHealthRegen()  {
        return healthRegen == null ? false : true;
    }
        
		public Integer getHealthRegen() {
		return healthRegen;
	}
	
	public Vitals setHealthRegen(Integer healthRegen) {
		this.healthRegen = healthRegen;
		return this;	}
	
		    
    public Boolean hasStaminaRegen()  {
        return staminaRegen == null ? false : true;
    }
        
		public Integer getStaminaRegen() {
		return staminaRegen;
	}
	
	public Vitals setStaminaRegen(Integer staminaRegen) {
		this.staminaRegen = staminaRegen;
		return this;	}
	
		    
    public Boolean hasChanged()  {
        return changed == null ? false : true;
    }
        
		public Integer getChanged() {
		return changed;
	}
	
	public Vitals setChanged(Integer changed) {
		this.changed = changed;
		return this;	}
	
		    
    public Boolean hasGrid()  {
        return grid == null ? false : true;
    }
        
		public String getGrid() {
		return grid;
	}
	
	public Vitals setGrid(String grid) {
		this.grid = grid;
		return this;	}
	
		    
    public Boolean hasStaminaDrain()  {
        return staminaDrain == null ? false : true;
    }
        
		public Integer getStaminaDrain() {
		return staminaDrain;
	}
	
	public Vitals setStaminaDrain(Integer staminaDrain) {
		this.staminaDrain = staminaDrain;
		return this;	}
	
	
  
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

    public Schema<Vitals> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Vitals newMessage()
    {
        return new Vitals();
    }

    public Class<Vitals> typeClass()
    {
        return Vitals.class;
    }

    public String messageName()
    {
        return Vitals.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Vitals.class.getName();
    }

    public boolean isInitialized(Vitals message)
    {
        return true;
    }

    public void mergeFrom(Input input, Vitals message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.stamina = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.magic = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.lastCombat = input.readInt64();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.dead = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.armor = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.spellResist = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.elementalResist = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.spellPenetration = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.magicRegen = input.readInt32();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.healthRegen = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.staminaRegen = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.changed = input.readInt32();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.grid = input.readString();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.staminaDrain = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Vitals message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.health != null)
            output.writeInt32(2, message.health, false);
    	    	
    	            	
    	    	
    	    	    	if(message.stamina != null)
            output.writeInt32(3, message.stamina, false);
    	    	
    	            	
    	    	
    	    	    	if(message.magic != null)
            output.writeInt32(4, message.magic, false);
    	    	
    	            	
    	    	
    	    	    	if(message.lastCombat != null)
            output.writeInt64(5, message.lastCombat, false);
    	    	
    	            	
    	    	
    	    	    	if(message.dead != null)
            output.writeInt32(6, message.dead, false);
    	    	
    	            	
    	    	
    	    	    	if(message.armor != null)
            output.writeInt32(7, message.armor, false);
    	    	
    	            	
    	    	
    	    	    	if(message.spellResist != null)
            output.writeInt32(8, message.spellResist, false);
    	    	
    	            	
    	    	
    	    	    	if(message.elementalResist != null)
            output.writeInt32(9, message.elementalResist, false);
    	    	
    	            	
    	    	
    	    	    	if(message.spellPenetration != null)
            output.writeInt32(10, message.spellPenetration, false);
    	    	
    	            	
    	    	
    	    	    	if(message.magicRegen != null)
            output.writeInt32(11, message.magicRegen, false);
    	    	
    	            	
    	    	
    	    	    	if(message.healthRegen != null)
            output.writeInt32(12, message.healthRegen, false);
    	    	
    	            	
    	    	
    	    	    	if(message.staminaRegen != null)
            output.writeInt32(13, message.staminaRegen, false);
    	    	
    	            	
    	    	
    	    	    	if(message.changed != null)
            output.writeInt32(14, message.changed, false);
    	    	
    	            	
    	    	
    	    	    	if(message.grid != null)
            output.writeString(15, message.grid, false);
    	    	
    	            	
    	    	
    	    	    	if(message.staminaDrain != null)
            output.writeInt32(16, message.staminaDrain, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "health";
        	        	case 3: return "stamina";
        	        	case 4: return "magic";
        	        	case 5: return "lastCombat";
        	        	case 6: return "dead";
        	        	case 7: return "armor";
        	        	case 8: return "spellResist";
        	        	case 9: return "elementalResist";
        	        	case 10: return "spellPenetration";
        	        	case 11: return "magicRegen";
        	        	case 12: return "healthRegen";
        	        	case 13: return "staminaRegen";
        	        	case 14: return "changed";
        	        	case 15: return "grid";
        	        	case 16: return "staminaDrain";
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
    	    	__fieldMap.put("health", 2);
    	    	__fieldMap.put("stamina", 3);
    	    	__fieldMap.put("magic", 4);
    	    	__fieldMap.put("lastCombat", 5);
    	    	__fieldMap.put("dead", 6);
    	    	__fieldMap.put("armor", 7);
    	    	__fieldMap.put("spellResist", 8);
    	    	__fieldMap.put("elementalResist", 9);
    	    	__fieldMap.put("spellPenetration", 10);
    	    	__fieldMap.put("magicRegen", 11);
    	    	__fieldMap.put("healthRegen", 12);
    	    	__fieldMap.put("staminaRegen", 13);
    	    	__fieldMap.put("changed", 14);
    	    	__fieldMap.put("grid", 15);
    	    	__fieldMap.put("staminaDrain", 16);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Vitals.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Vitals parseFrom(byte[] bytes) {
	Vitals message = new Vitals();
	ProtobufIOUtil.mergeFrom(bytes, message, Vitals.getSchema());
	return message;
}

public static Vitals parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Vitals message = new Vitals();
	JsonIOUtil.mergeFrom(bytes, message, Vitals.getSchema(), false);
	return message;
}

public Vitals clone() {
	byte[] bytes = this.toByteArray();
	Vitals vitals = Vitals.parseFrom(bytes);
	return vitals;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Vitals.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Vitals> schema = Vitals.getSchema();
    
	final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ProtobufOutput output = new ProtobufOutput(buffer);
    try
    {
    	schema.writeTo(output, this);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        assert size == msgSize;
        
        buffer.clear();
        return out.toByteArray();
    }
    catch (IOException e)
    {
        throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                "(should never happen).", e);
    }
 
}

public byte[] toProtobuf() {
	LinkedBuffer buffer = LocalLinkedBuffer.get();
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, Vitals.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
