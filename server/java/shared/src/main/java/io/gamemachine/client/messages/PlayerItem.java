
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
public final class PlayerItem implements Externalizable, Message<PlayerItem>, Schema<PlayerItem>{



    public static Schema<PlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerItem DEFAULT_INSTANCE = new PlayerItem();

    			public String id;
	    
        			public String name;
	    
        			public Integer quantity;
	    
        			public String color;
	    
        			public Boolean weapon;
	    
        			public Consumable consumable;
	    
        			public Cost cost;
	    
        			public String playerId;
	    
        			public Integer recordId;
	    
        			public String icon;
	    
        			public Integer harvestable;
	    
        			public Integer craftingResource;
	    
        			public Integer craftable;
	    
        			public ModelInfo modelInfo;
	    
        			public Boolean isConsumable;
	    
        			public Integer type;
	    
        			public Integer maxHealth;
	    
        			public Integer health;
	    
        			public Integer level;
	    
        			public String characterId;
	    
        			public String containerId;
	    
        			public Integer updatedAt;
	    
        			public String category;
	    
        			public String locationId;
	    
        			public Integer slotCount;
	    
        			public Boolean stackable;
	    
        			public String locationType;
	    
        			public Integer stackMax;
	    
        			public Integer containerSlot;
	    
        			public String icon_uuid;
	    
        			public String icon_path;
	    
        			public String referenceId;
	    
        			public Boolean hidden;
	    
        			public Integer maxQuantity;
	    
        			public Boolean active;
	    
        			public Float weight;
	    
        			public Integer templateBlockId;
	    
      
    public PlayerItem()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public PlayerItem setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public PlayerItem setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasQuantity()  {
        return quantity == null ? false : true;
    }
        
		public Integer getQuantity() {
		return quantity;
	}
	
	public PlayerItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;	}
	
		    
    public Boolean hasColor()  {
        return color == null ? false : true;
    }
        
		public String getColor() {
		return color;
	}
	
	public PlayerItem setColor(String color) {
		this.color = color;
		return this;	}
	
		    
    public Boolean hasWeapon()  {
        return weapon == null ? false : true;
    }
        
		public Boolean getWeapon() {
		return weapon;
	}
	
	public PlayerItem setWeapon(Boolean weapon) {
		this.weapon = weapon;
		return this;	}
	
		    
    public Boolean hasConsumable()  {
        return consumable == null ? false : true;
    }
        
		public Consumable getConsumable() {
		return consumable;
	}
	
	public PlayerItem setConsumable(Consumable consumable) {
		this.consumable = consumable;
		return this;	}
	
		    
    public Boolean hasCost()  {
        return cost == null ? false : true;
    }
        
		public Cost getCost() {
		return cost;
	}
	
	public PlayerItem setCost(Cost cost) {
		this.cost = cost;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public PlayerItem setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public PlayerItem setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasIcon()  {
        return icon == null ? false : true;
    }
        
		public String getIcon() {
		return icon;
	}
	
	public PlayerItem setIcon(String icon) {
		this.icon = icon;
		return this;	}
	
		    
    public Boolean hasHarvestable()  {
        return harvestable == null ? false : true;
    }
        
		public Integer getHarvestable() {
		return harvestable;
	}
	
	public PlayerItem setHarvestable(Integer harvestable) {
		this.harvestable = harvestable;
		return this;	}
	
		    
    public Boolean hasCraftingResource()  {
        return craftingResource == null ? false : true;
    }
        
		public Integer getCraftingResource() {
		return craftingResource;
	}
	
	public PlayerItem setCraftingResource(Integer craftingResource) {
		this.craftingResource = craftingResource;
		return this;	}
	
		    
    public Boolean hasCraftable()  {
        return craftable == null ? false : true;
    }
        
		public Integer getCraftable() {
		return craftable;
	}
	
	public PlayerItem setCraftable(Integer craftable) {
		this.craftable = craftable;
		return this;	}
	
		    
    public Boolean hasModelInfo()  {
        return modelInfo == null ? false : true;
    }
        
		public ModelInfo getModelInfo() {
		return modelInfo;
	}
	
	public PlayerItem setModelInfo(ModelInfo modelInfo) {
		this.modelInfo = modelInfo;
		return this;	}
	
		    
    public Boolean hasIsConsumable()  {
        return isConsumable == null ? false : true;
    }
        
		public Boolean getIsConsumable() {
		return isConsumable;
	}
	
	public PlayerItem setIsConsumable(Boolean isConsumable) {
		this.isConsumable = isConsumable;
		return this;	}
	
		    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public Integer getType() {
		return type;
	}
	
	public PlayerItem setType(Integer type) {
		this.type = type;
		return this;	}
	
		    
    public Boolean hasMaxHealth()  {
        return maxHealth == null ? false : true;
    }
        
		public Integer getMaxHealth() {
		return maxHealth;
	}
	
	public PlayerItem setMaxHealth(Integer maxHealth) {
		this.maxHealth = maxHealth;
		return this;	}
	
		    
    public Boolean hasHealth()  {
        return health == null ? false : true;
    }
        
		public Integer getHealth() {
		return health;
	}
	
	public PlayerItem setHealth(Integer health) {
		this.health = health;
		return this;	}
	
		    
    public Boolean hasLevel()  {
        return level == null ? false : true;
    }
        
		public Integer getLevel() {
		return level;
	}
	
	public PlayerItem setLevel(Integer level) {
		this.level = level;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public PlayerItem setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasContainerId()  {
        return containerId == null ? false : true;
    }
        
		public String getContainerId() {
		return containerId;
	}
	
	public PlayerItem setContainerId(String containerId) {
		this.containerId = containerId;
		return this;	}
	
		    
    public Boolean hasUpdatedAt()  {
        return updatedAt == null ? false : true;
    }
        
		public Integer getUpdatedAt() {
		return updatedAt;
	}
	
	public PlayerItem setUpdatedAt(Integer updatedAt) {
		this.updatedAt = updatedAt;
		return this;	}
	
		    
    public Boolean hasCategory()  {
        return category == null ? false : true;
    }
        
		public String getCategory() {
		return category;
	}
	
	public PlayerItem setCategory(String category) {
		this.category = category;
		return this;	}
	
		    
    public Boolean hasLocationId()  {
        return locationId == null ? false : true;
    }
        
		public String getLocationId() {
		return locationId;
	}
	
	public PlayerItem setLocationId(String locationId) {
		this.locationId = locationId;
		return this;	}
	
		    
    public Boolean hasSlotCount()  {
        return slotCount == null ? false : true;
    }
        
		public Integer getSlotCount() {
		return slotCount;
	}
	
	public PlayerItem setSlotCount(Integer slotCount) {
		this.slotCount = slotCount;
		return this;	}
	
		    
    public Boolean hasStackable()  {
        return stackable == null ? false : true;
    }
        
		public Boolean getStackable() {
		return stackable;
	}
	
	public PlayerItem setStackable(Boolean stackable) {
		this.stackable = stackable;
		return this;	}
	
		    
    public Boolean hasLocationType()  {
        return locationType == null ? false : true;
    }
        
		public String getLocationType() {
		return locationType;
	}
	
	public PlayerItem setLocationType(String locationType) {
		this.locationType = locationType;
		return this;	}
	
		    
    public Boolean hasStackMax()  {
        return stackMax == null ? false : true;
    }
        
		public Integer getStackMax() {
		return stackMax;
	}
	
	public PlayerItem setStackMax(Integer stackMax) {
		this.stackMax = stackMax;
		return this;	}
	
		    
    public Boolean hasContainerSlot()  {
        return containerSlot == null ? false : true;
    }
        
		public Integer getContainerSlot() {
		return containerSlot;
	}
	
	public PlayerItem setContainerSlot(Integer containerSlot) {
		this.containerSlot = containerSlot;
		return this;	}
	
		    
    public Boolean hasIcon_uuid()  {
        return icon_uuid == null ? false : true;
    }
        
		public String getIcon_uuid() {
		return icon_uuid;
	}
	
	public PlayerItem setIcon_uuid(String icon_uuid) {
		this.icon_uuid = icon_uuid;
		return this;	}
	
		    
    public Boolean hasIcon_path()  {
        return icon_path == null ? false : true;
    }
        
		public String getIcon_path() {
		return icon_path;
	}
	
	public PlayerItem setIcon_path(String icon_path) {
		this.icon_path = icon_path;
		return this;	}
	
		    
    public Boolean hasReferenceId()  {
        return referenceId == null ? false : true;
    }
        
		public String getReferenceId() {
		return referenceId;
	}
	
	public PlayerItem setReferenceId(String referenceId) {
		this.referenceId = referenceId;
		return this;	}
	
		    
    public Boolean hasHidden()  {
        return hidden == null ? false : true;
    }
        
		public Boolean getHidden() {
		return hidden;
	}
	
	public PlayerItem setHidden(Boolean hidden) {
		this.hidden = hidden;
		return this;	}
	
		    
    public Boolean hasMaxQuantity()  {
        return maxQuantity == null ? false : true;
    }
        
		public Integer getMaxQuantity() {
		return maxQuantity;
	}
	
	public PlayerItem setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
		return this;	}
	
		    
    public Boolean hasActive()  {
        return active == null ? false : true;
    }
        
		public Boolean getActive() {
		return active;
	}
	
	public PlayerItem setActive(Boolean active) {
		this.active = active;
		return this;	}
	
		    
    public Boolean hasWeight()  {
        return weight == null ? false : true;
    }
        
		public Float getWeight() {
		return weight;
	}
	
	public PlayerItem setWeight(Float weight) {
		this.weight = weight;
		return this;	}
	
		    
    public Boolean hasTemplateBlockId()  {
        return templateBlockId == null ? false : true;
    }
        
		public Integer getTemplateBlockId() {
		return templateBlockId;
	}
	
	public PlayerItem setTemplateBlockId(Integer templateBlockId) {
		this.templateBlockId = templateBlockId;
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

    public Schema<PlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerItem newMessage()
    {
        return new PlayerItem();
    }

    public Class<PlayerItem> typeClass()
    {
        return PlayerItem.class;
    }

    public String messageName()
    {
        return PlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerItem.class.getName();
    }

    public boolean isInitialized(PlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerItem message) throws IOException
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
            	                	                	message.quantity = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.color = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.weapon = input.readBool();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.consumable = input.mergeObject(message.consumable, Consumable.getSchema());
                    break;
                                    	
                            	            	case 8:
            	                	                	message.cost = input.mergeObject(message.cost, Cost.getSchema());
                    break;
                                    	
                            	            	case 9:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.icon = input.readString();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.harvestable = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.craftingResource = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.craftable = input.readInt32();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.modelInfo = input.mergeObject(message.modelInfo, ModelInfo.getSchema());
                    break;
                                    	
                            	            	case 16:
            	                	                	message.isConsumable = input.readBool();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.type = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.maxHealth = input.readInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.level = input.readInt32();
                	break;
                	                	
                            	            	case 21:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 22:
            	                	                	message.containerId = input.readString();
                	break;
                	                	
                            	            	case 23:
            	                	                	message.updatedAt = input.readInt32();
                	break;
                	                	
                            	            	case 24:
            	                	                	message.category = input.readString();
                	break;
                	                	
                            	            	case 25:
            	                	                	message.locationId = input.readString();
                	break;
                	                	
                            	            	case 26:
            	                	                	message.slotCount = input.readInt32();
                	break;
                	                	
                            	            	case 27:
            	                	                	message.stackable = input.readBool();
                	break;
                	                	
                            	            	case 28:
            	                	                	message.locationType = input.readString();
                	break;
                	                	
                            	            	case 29:
            	                	                	message.stackMax = input.readInt32();
                	break;
                	                	
                            	            	case 30:
            	                	                	message.containerSlot = input.readInt32();
                	break;
                	                	
                            	            	case 31:
            	                	                	message.icon_uuid = input.readString();
                	break;
                	                	
                            	            	case 32:
            	                	                	message.icon_path = input.readString();
                	break;
                	                	
                            	            	case 33:
            	                	                	message.referenceId = input.readString();
                	break;
                	                	
                            	            	case 34:
            	                	                	message.hidden = input.readBool();
                	break;
                	                	
                            	            	case 35:
            	                	                	message.maxQuantity = input.readInt32();
                	break;
                	                	
                            	            	case 36:
            	                	                	message.active = input.readBool();
                	break;
                	                	
                            	            	case 37:
            	                	                	message.weight = input.readFloat();
                	break;
                	                	
                            	            	case 38:
            	                	                	message.templateBlockId = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerItem message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	if(message.name == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.name != null)
            output.writeString(2, message.name, false);
    	    	
    	            	
    	    	if(message.quantity == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.quantity != null)
            output.writeInt32(3, message.quantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.color != null)
            output.writeString(4, message.color, false);
    	    	
    	            	
    	    	
    	    	    	if(message.weapon != null)
            output.writeBool(5, message.weapon, false);
    	    	
    	            	
    	    	
    	    	    	if(message.consumable != null)
    		output.writeObject(6, message.consumable, Consumable.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.cost != null)
    		output.writeObject(8, message.cost, Cost.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(9, message.playerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(10, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon != null)
            output.writeString(11, message.icon, false);
    	    	
    	            	
    	    	
    	    	    	if(message.harvestable != null)
            output.writeInt32(12, message.harvestable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.craftingResource != null)
            output.writeInt32(13, message.craftingResource, false);
    	    	
    	            	
    	    	
    	    	    	if(message.craftable != null)
            output.writeInt32(14, message.craftable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.modelInfo != null)
    		output.writeObject(15, message.modelInfo, ModelInfo.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.isConsumable != null)
            output.writeBool(16, message.isConsumable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.type != null)
            output.writeInt32(17, message.type, false);
    	    	
    	            	
    	    	
    	    	    	if(message.maxHealth != null)
            output.writeInt32(18, message.maxHealth, false);
    	    	
    	            	
    	    	
    	    	    	if(message.health != null)
            output.writeInt32(19, message.health, false);
    	    	
    	            	
    	    	
    	    	    	if(message.level != null)
            output.writeInt32(20, message.level, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(21, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.containerId != null)
            output.writeString(22, message.containerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.updatedAt != null)
            output.writeInt32(23, message.updatedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.category != null)
            output.writeString(24, message.category, false);
    	    	
    	            	
    	    	
    	    	    	if(message.locationId != null)
            output.writeString(25, message.locationId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.slotCount != null)
            output.writeInt32(26, message.slotCount, false);
    	    	
    	            	
    	    	if(message.stackable == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.stackable != null)
            output.writeBool(27, message.stackable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.locationType != null)
            output.writeString(28, message.locationType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.stackMax != null)
            output.writeInt32(29, message.stackMax, false);
    	    	
    	            	
    	    	
    	    	    	if(message.containerSlot != null)
            output.writeInt32(30, message.containerSlot, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_uuid != null)
            output.writeString(31, message.icon_uuid, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_path != null)
            output.writeString(32, message.icon_path, false);
    	    	
    	            	
    	    	
    	    	    	if(message.referenceId != null)
            output.writeString(33, message.referenceId, false);
    	    	
    	            	
    	    	if(message.hidden == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.hidden != null)
            output.writeBool(34, message.hidden, false);
    	    	
    	            	
    	    	
    	    	    	if(message.maxQuantity != null)
            output.writeInt32(35, message.maxQuantity, false);
    	    	
    	            	
    	    	if(message.active == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.active != null)
            output.writeBool(36, message.active, false);
    	    	
    	            	
    	    	if(message.weight == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.weight != null)
            output.writeFloat(37, message.weight, false);
    	    	
    	            	
    	    	
    	    	    	if(message.templateBlockId != null)
            output.writeInt32(38, message.templateBlockId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "name";
        	        	case 3: return "quantity";
        	        	case 4: return "color";
        	        	case 5: return "weapon";
        	        	case 6: return "consumable";
        	        	case 8: return "cost";
        	        	case 9: return "playerId";
        	        	case 10: return "recordId";
        	        	case 11: return "icon";
        	        	case 12: return "harvestable";
        	        	case 13: return "craftingResource";
        	        	case 14: return "craftable";
        	        	case 15: return "modelInfo";
        	        	case 16: return "isConsumable";
        	        	case 17: return "type";
        	        	case 18: return "maxHealth";
        	        	case 19: return "health";
        	        	case 20: return "level";
        	        	case 21: return "characterId";
        	        	case 22: return "containerId";
        	        	case 23: return "updatedAt";
        	        	case 24: return "category";
        	        	case 25: return "locationId";
        	        	case 26: return "slotCount";
        	        	case 27: return "stackable";
        	        	case 28: return "locationType";
        	        	case 29: return "stackMax";
        	        	case 30: return "containerSlot";
        	        	case 31: return "icon_uuid";
        	        	case 32: return "icon_path";
        	        	case 33: return "referenceId";
        	        	case 34: return "hidden";
        	        	case 35: return "maxQuantity";
        	        	case 36: return "active";
        	        	case 37: return "weight";
        	        	case 38: return "templateBlockId";
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
    	    	__fieldMap.put("quantity", 3);
    	    	__fieldMap.put("color", 4);
    	    	__fieldMap.put("weapon", 5);
    	    	__fieldMap.put("consumable", 6);
    	    	__fieldMap.put("cost", 8);
    	    	__fieldMap.put("playerId", 9);
    	    	__fieldMap.put("recordId", 10);
    	    	__fieldMap.put("icon", 11);
    	    	__fieldMap.put("harvestable", 12);
    	    	__fieldMap.put("craftingResource", 13);
    	    	__fieldMap.put("craftable", 14);
    	    	__fieldMap.put("modelInfo", 15);
    	    	__fieldMap.put("isConsumable", 16);
    	    	__fieldMap.put("type", 17);
    	    	__fieldMap.put("maxHealth", 18);
    	    	__fieldMap.put("health", 19);
    	    	__fieldMap.put("level", 20);
    	    	__fieldMap.put("characterId", 21);
    	    	__fieldMap.put("containerId", 22);
    	    	__fieldMap.put("updatedAt", 23);
    	    	__fieldMap.put("category", 24);
    	    	__fieldMap.put("locationId", 25);
    	    	__fieldMap.put("slotCount", 26);
    	    	__fieldMap.put("stackable", 27);
    	    	__fieldMap.put("locationType", 28);
    	    	__fieldMap.put("stackMax", 29);
    	    	__fieldMap.put("containerSlot", 30);
    	    	__fieldMap.put("icon_uuid", 31);
    	    	__fieldMap.put("icon_path", 32);
    	    	__fieldMap.put("referenceId", 33);
    	    	__fieldMap.put("hidden", 34);
    	    	__fieldMap.put("maxQuantity", 35);
    	    	__fieldMap.put("active", 36);
    	    	__fieldMap.put("weight", 37);
    	    	__fieldMap.put("templateBlockId", 38);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerItem parseFrom(byte[] bytes) {
	PlayerItem message = new PlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerItem.getSchema());
	return message;
}

public static PlayerItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerItem message = new PlayerItem();
	JsonIOUtil.mergeFrom(bytes, message, PlayerItem.getSchema(), false);
	return message;
}

public PlayerItem clone() {
	byte[] bytes = this.toByteArray();
	PlayerItem playerItem = PlayerItem.parseFrom(bytes);
	return playerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerItem> schema = PlayerItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerItem.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
