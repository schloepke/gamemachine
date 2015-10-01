
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
	    
        			public Integer recordId;
	    
        			public String category;
	    
        			public String damageType;
	    
        			public String icon;
	    
        			public String description;
	    
        			public String resource;
	    
        			public Integer resourceCost;
	    
        			public String characterId;
	    
        			public String weaponType;
	    
        			public Integer range;
	    
        			public String statusEffectId;
	    
        			public Integer level;
	    
        			public Integer resourceCostPerTick;
	    
        			public Integer isComboPart;
	    
        			public Integer isPassive;
	    
      
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
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public PlayerSkill setRecordId(Integer recordId) {
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
	
		    
    public Boolean hasIcon()  {
        return icon == null ? false : true;
    }
        
		public String getIcon() {
		return icon;
	}
	
	public PlayerSkill setIcon(String icon) {
		this.icon = icon;
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
        
		public Integer getResourceCost() {
		return resourceCost;
	}
	
	public PlayerSkill setResourceCost(Integer resourceCost) {
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
        
		public Integer getRange() {
		return range;
	}
	
	public PlayerSkill setRange(Integer range) {
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
        
		public Integer getLevel() {
		return level;
	}
	
	public PlayerSkill setLevel(Integer level) {
		this.level = level;
		return this;	}
	
		    
    public Boolean hasResourceCostPerTick()  {
        return resourceCostPerTick == null ? false : true;
    }
        
		public Integer getResourceCostPerTick() {
		return resourceCostPerTick;
	}
	
	public PlayerSkill setResourceCostPerTick(Integer resourceCostPerTick) {
		this.resourceCostPerTick = resourceCostPerTick;
		return this;	}
	
		    
    public Boolean hasIsComboPart()  {
        return isComboPart == null ? false : true;
    }
        
		public Integer getIsComboPart() {
		return isComboPart;
	}
	
	public PlayerSkill setIsComboPart(Integer isComboPart) {
		this.isComboPart = isComboPart;
		return this;	}
	
		    
    public Boolean hasIsPassive()  {
        return isPassive == null ? false : true;
    }
        
		public Integer getIsPassive() {
		return isPassive;
	}
	
	public PlayerSkill setIsPassive(Integer isPassive) {
		this.isPassive = isPassive;
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
            	                	                	message.icon = input.readString();
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
    	    	
    	            	
    	    	if(message.name == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.name != null)
            output.writeString(2, message.name, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(3, message.recordId, false);
    	    	
    	            	
    	    	if(message.category == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.category != null)
            output.writeString(4, message.category, false);
    	    	
    	            	
    	    	
    	    	    	if(message.damageType != null)
            output.writeString(5, message.damageType, false);
    	    	
    	            	
    	    	if(message.icon == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.icon != null)
            output.writeString(6, message.icon, false);
    	    	
    	            	
    	    	
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
        	        	case 6: return "icon";
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
    	    	__fieldMap.put("icon", 6);
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
