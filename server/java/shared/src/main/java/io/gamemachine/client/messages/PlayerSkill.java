
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
public final class PlayerSkill implements Externalizable, Message<PlayerSkill>, Schema<PlayerSkill>{

	public enum DamageType implements io.protostuff.EnumLite<DamageType>
    {
    	
    	    	Aoe(0),    	    	SingleTarget(1),    	    	Pbaoe(2),    	    	SelfAoe(3),    	    	Self(4);    	        
        public final int number;
        
        private DamageType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static DamageType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (Aoe);
    			    			case 1: return (SingleTarget);
    			    			case 2: return (Pbaoe);
    			    			case 3: return (SelfAoe);
    			    			case 4: return (Self);
    			                default: return null;
            }
        }
    }
	public enum SkillType implements io.protostuff.EnumLite<SkillType>
    {
    	
    	    	Active(0),    	    	Passive(1);    	        
        public final int number;
        
        private SkillType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static SkillType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (Active);
    			    			case 1: return (Passive);
    			                default: return null;
            }
        }
    }
	public enum Resource implements io.protostuff.EnumLite<Resource>
    {
    	
    	    	NoResource(0),    	    	Magic(1),    	    	Stamina(2);    	        
        public final int number;
        
        private Resource (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Resource valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NoResource);
    			    			case 1: return (Magic);
    			    			case 2: return (Stamina);
    			                default: return null;
            }
        }
    }
	public enum Category implements io.protostuff.EnumLite<Category>
    {
    	
    	    	Weapon(0),    	    	Crafting(1),    	    	CategoryOther(2);    	        
        public final int number;
        
        private Category (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Category valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (Weapon);
    			    			case 1: return (Crafting);
    			    			case 2: return (CategoryOther);
    			                default: return null;
            }
        }
    }
	public enum WeaponType implements io.protostuff.EnumLite<WeaponType>
    {
    	
    	    	Bow(0),    	    	Sword2h(1),    	    	Sword1h(2),    	    	Staff(3),    	    	Gun(4),    	    	Siege(5),    	    	WeaponTypeOther(6);    	        
        public final int number;
        
        private WeaponType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static WeaponType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (Bow);
    			    			case 1: return (Sword2h);
    			    			case 2: return (Sword1h);
    			    			case 3: return (Staff);
    			    			case 4: return (Gun);
    			    			case 5: return (Siege);
    			    			case 6: return (WeaponTypeOther);
    			                default: return null;
            }
        }
    }


    public static Schema<PlayerSkill> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerSkill getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerSkill DEFAULT_INSTANCE = new PlayerSkill();

    			public String id;
	    
        			public String name;
	    
        			public int recordId;
	    
        			public String category;
	    
        			public String damageType;
	    
        			public String icon_path;
	    
        			public String description;
	    
        			public String resource;
	    
        			public int resourceCost;
	    
        			public String characterId;
	    
        			public String weaponType;
	    
        			public int range;
	    
        			public String statusEffectId;
	    
        			public int level;
	    
        			public int resourceCostPerTick;
	    
        			public int isComboPart;
	    
        			public int isPassive;
	    
        			public String skillType;
	    
        			public String icon_uuid;
	    
        			public String statusEffects;
	    
      
    public PlayerSkill()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public PlayerSkill setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public PlayerSkill setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public int getRecordId() {
		return recordId;
	}
	
	public PlayerSkill setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasCategory()  {
        return category == null ? false : true;
    }
        
		public String getCategory() {
		return category;
	}
	
	public PlayerSkill setCategory(String category) {
		this.category = category;
		return this;	}
	
		    
    public Boolean hasDamageType()  {
        return damageType == null ? false : true;
    }
        
		public String getDamageType() {
		return damageType;
	}
	
	public PlayerSkill setDamageType(String damageType) {
		this.damageType = damageType;
		return this;	}
	
		    
    public Boolean hasIcon_path()  {
        return icon_path == null ? false : true;
    }
        
		public String getIcon_path() {
		return icon_path;
	}
	
	public PlayerSkill setIcon_path(String icon_path) {
		this.icon_path = icon_path;
		return this;	}
	
		    
    public Boolean hasDescription()  {
        return description == null ? false : true;
    }
        
		public String getDescription() {
		return description;
	}
	
	public PlayerSkill setDescription(String description) {
		this.description = description;
		return this;	}
	
		    
    public Boolean hasResource()  {
        return resource == null ? false : true;
    }
        
		public String getResource() {
		return resource;
	}
	
	public PlayerSkill setResource(String resource) {
		this.resource = resource;
		return this;	}
	
		    
    public Boolean hasResourceCost()  {
        return resourceCost == null ? false : true;
    }
        
		public int getResourceCost() {
		return resourceCost;
	}
	
	public PlayerSkill setResourceCost(int resourceCost) {
		this.resourceCost = resourceCost;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public PlayerSkill setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasWeaponType()  {
        return weaponType == null ? false : true;
    }
        
		public String getWeaponType() {
		return weaponType;
	}
	
	public PlayerSkill setWeaponType(String weaponType) {
		this.weaponType = weaponType;
		return this;	}
	
		    
    public Boolean hasRange()  {
        return range == null ? false : true;
    }
        
		public int getRange() {
		return range;
	}
	
	public PlayerSkill setRange(int range) {
		this.range = range;
		return this;	}
	
		    
    public Boolean hasStatusEffectId()  {
        return statusEffectId == null ? false : true;
    }
        
		public String getStatusEffectId() {
		return statusEffectId;
	}
	
	public PlayerSkill setStatusEffectId(String statusEffectId) {
		this.statusEffectId = statusEffectId;
		return this;	}
	
		    
    public Boolean hasLevel()  {
        return level == null ? false : true;
    }
        
		public int getLevel() {
		return level;
	}
	
	public PlayerSkill setLevel(int level) {
		this.level = level;
		return this;	}
	
		    
    public Boolean hasResourceCostPerTick()  {
        return resourceCostPerTick == null ? false : true;
    }
        
		public int getResourceCostPerTick() {
		return resourceCostPerTick;
	}
	
	public PlayerSkill setResourceCostPerTick(int resourceCostPerTick) {
		this.resourceCostPerTick = resourceCostPerTick;
		return this;	}
	
		    
    public Boolean hasIsComboPart()  {
        return isComboPart == null ? false : true;
    }
        
		public int getIsComboPart() {
		return isComboPart;
	}
	
	public PlayerSkill setIsComboPart(int isComboPart) {
		this.isComboPart = isComboPart;
		return this;	}
	
		    
    public Boolean hasIsPassive()  {
        return isPassive == null ? false : true;
    }
        
		public int getIsPassive() {
		return isPassive;
	}
	
	public PlayerSkill setIsPassive(int isPassive) {
		this.isPassive = isPassive;
		return this;	}
	
		    
    public Boolean hasSkillType()  {
        return skillType == null ? false : true;
    }
        
		public String getSkillType() {
		return skillType;
	}
	
	public PlayerSkill setSkillType(String skillType) {
		this.skillType = skillType;
		return this;	}
	
		    
    public Boolean hasIcon_uuid()  {
        return icon_uuid == null ? false : true;
    }
        
		public String getIcon_uuid() {
		return icon_uuid;
	}
	
	public PlayerSkill setIcon_uuid(String icon_uuid) {
		this.icon_uuid = icon_uuid;
		return this;	}
	
		    
    public Boolean hasStatusEffects()  {
        return statusEffects == null ? false : true;
    }
        
		public String getStatusEffects() {
		return statusEffects;
	}
	
	public PlayerSkill setStatusEffects(String statusEffects) {
		this.statusEffects = statusEffects;
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

    public Schema<PlayerSkill> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerSkill newMessage()
    {
        return new PlayerSkill();
    }

    public Class<PlayerSkill> typeClass()
    {
        return PlayerSkill.class;
    }

    public String messageName()
    {
        return PlayerSkill.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerSkill.class.getName();
    }

    public boolean isInitialized(PlayerSkill message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerSkill message) throws IOException
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
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.category = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.damageType = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.icon_path = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.description = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.resource = input.readString();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.resourceCost = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.weaponType = input.readString();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.range = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.statusEffectId = input.readString();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.level = input.readInt32();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.resourceCostPerTick = input.readInt32();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.isComboPart = input.readInt32();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.isPassive = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.skillType = input.readString();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.icon_uuid = input.readString();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.statusEffects = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerSkill message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.name != null)
            output.writeString(2, message.name, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(3, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.category != null)
            output.writeString(4, message.category, false);
    	    	
    	            	
    	    	
    	    	    	if(message.damageType != null)
            output.writeString(5, message.damageType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_path != null)
            output.writeString(6, message.icon_path, false);
    	    	
    	            	
    	    	
    	    	    	if(message.description != null)
            output.writeString(7, message.description, false);
    	    	
    	            	
    	    	
    	    	    	if(message.resource != null)
            output.writeString(8, message.resource, false);
    	    	
    	            	
    	    	
    	    	    	if(message.resourceCost != null)
            output.writeInt32(9, message.resourceCost, false);
    	    	
    	            	
    	    	if(message.characterId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(10, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.weaponType != null)
            output.writeString(11, message.weaponType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.range != null)
            output.writeInt32(12, message.range, false);
    	    	
    	            	
    	    	
    	    	    	if(message.statusEffectId != null)
            output.writeString(13, message.statusEffectId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.level != null)
            output.writeInt32(14, message.level, false);
    	    	
    	            	
    	    	
    	    	    	if(message.resourceCostPerTick != null)
            output.writeInt32(15, message.resourceCostPerTick, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isComboPart != null)
            output.writeInt32(16, message.isComboPart, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isPassive != null)
            output.writeInt32(17, message.isPassive, false);
    	    	
    	            	
    	    	
    	    	    	if(message.skillType != null)
            output.writeString(18, message.skillType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_uuid != null)
            output.writeString(19, message.icon_uuid, false);
    	    	
    	            	
    	    	
    	    	    	if(message.statusEffects != null)
            output.writeString(20, message.statusEffects, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "name";
        	        	case 3: return "recordId";
        	        	case 4: return "category";
        	        	case 5: return "damageType";
        	        	case 6: return "icon_path";
        	        	case 7: return "description";
        	        	case 8: return "resource";
        	        	case 9: return "resourceCost";
        	        	case 10: return "characterId";
        	        	case 11: return "weaponType";
        	        	case 12: return "range";
        	        	case 13: return "statusEffectId";
        	        	case 14: return "level";
        	        	case 15: return "resourceCostPerTick";
        	        	case 16: return "isComboPart";
        	        	case 17: return "isPassive";
        	        	case 18: return "skillType";
        	        	case 19: return "icon_uuid";
        	        	case 20: return "statusEffects";
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
    	    	__fieldMap.put("recordId", 3);
    	    	__fieldMap.put("category", 4);
    	    	__fieldMap.put("damageType", 5);
    	    	__fieldMap.put("icon_path", 6);
    	    	__fieldMap.put("description", 7);
    	    	__fieldMap.put("resource", 8);
    	    	__fieldMap.put("resourceCost", 9);
    	    	__fieldMap.put("characterId", 10);
    	    	__fieldMap.put("weaponType", 11);
    	    	__fieldMap.put("range", 12);
    	    	__fieldMap.put("statusEffectId", 13);
    	    	__fieldMap.put("level", 14);
    	    	__fieldMap.put("resourceCostPerTick", 15);
    	    	__fieldMap.put("isComboPart", 16);
    	    	__fieldMap.put("isPassive", 17);
    	    	__fieldMap.put("skillType", 18);
    	    	__fieldMap.put("icon_uuid", 19);
    	    	__fieldMap.put("statusEffects", 20);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerSkill.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerSkill parseFrom(byte[] bytes) {
	PlayerSkill message = new PlayerSkill();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerSkill.getSchema());
	return message;
}

public static PlayerSkill parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerSkill message = new PlayerSkill();
	JsonIOUtil.mergeFrom(bytes, message, PlayerSkill.getSchema(), false);
	return message;
}

public PlayerSkill clone() {
	byte[] bytes = this.toByteArray();
	PlayerSkill playerSkill = PlayerSkill.parseFrom(bytes);
	return playerSkill;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerSkill.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerSkill> schema = PlayerSkill.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerSkill.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
