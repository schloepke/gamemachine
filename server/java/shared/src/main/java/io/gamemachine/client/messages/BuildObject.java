
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
public final class BuildObject implements Externalizable, Message<BuildObject>, Schema<BuildObject>{



    public static Schema<BuildObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildObject DEFAULT_INSTANCE = new BuildObject();

    			public String playerItemId;
	    
        			public Integer action;
	    
        			public String id;
	    
        			public Integer recordId;
	    
        			public String ownerId;
	    
        			public Integer x;
	    
        			public Integer y;
	    
        			public Integer z;
	    
        			public Integer rx;
	    
        			public Integer ry;
	    
        			public Integer rz;
	    
        			public Integer rw;
	    
        			public Integer health;
	    
        			public Integer templateId;
	    
        			public String grid;
	    
        			public Long updatedAt;
	    
        			public Integer state;
	    
        			public Integer updateId;
	    
        			public Boolean isFloor;
	    
        			public Boolean isDestructable;
	    
        			public Boolean hasDoor;
	    
        			public Integer doorStatus;
	    
        			public ByteString groundBlockObject;
	    
        			public Boolean isGroundBlock;
	    
        			public Integer chunk;
	    
        			public ByteString terrainEdit;
	    
        			public Boolean isTerrainEdit;
	    
        			public ByteString customBytes;
	    
        			public String customString;
	    
        			public String textureId;
	    
        			public ByteString slots;
	    
        			public Long placedAt;
	    
        			public SlotInfo slotInfo;
	    
      
    public BuildObject()
    {
        
    }


	

	    
    public Boolean hasPlayerItemId()  {
        return playerItemId == null ? false : true;
    }
        
		public String getPlayerItemId() {
		return playerItemId;
	}
	
	public BuildObject setPlayerItemId(String playerItemId) {
		this.playerItemId = playerItemId;
		return this;	}
	
		    
    public Boolean hasAction()  {
        return action == null ? false : true;
    }
        
		public Integer getAction() {
		return action;
	}
	
	public BuildObject setAction(Integer action) {
		this.action = action;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public BuildObject setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public BuildObject setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasOwnerId()  {
        return ownerId == null ? false : true;
    }
        
		public String getOwnerId() {
		return ownerId;
	}
	
	public BuildObject setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		    
    public Boolean hasX()  {
        return x == null ? false : true;
    }
        
		public Integer getX() {
		return x;
	}
	
	public BuildObject setX(Integer x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Integer getY() {
		return y;
	}
	
	public BuildObject setY(Integer y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasZ()  {
        return z == null ? false : true;
    }
        
		public Integer getZ() {
		return z;
	}
	
	public BuildObject setZ(Integer z) {
		this.z = z;
		return this;	}
	
		    
    public Boolean hasRx()  {
        return rx == null ? false : true;
    }
        
		public Integer getRx() {
		return rx;
	}
	
	public BuildObject setRx(Integer rx) {
		this.rx = rx;
		return this;	}
	
		    
    public Boolean hasRy()  {
        return ry == null ? false : true;
    }
        
		public Integer getRy() {
		return ry;
	}
	
	public BuildObject setRy(Integer ry) {
		this.ry = ry;
		return this;	}
	
		    
    public Boolean hasRz()  {
        return rz == null ? false : true;
    }
        
		public Integer getRz() {
		return rz;
	}
	
	public BuildObject setRz(Integer rz) {
		this.rz = rz;
		return this;	}
	
		    
    public Boolean hasRw()  {
        return rw == null ? false : true;
    }
        
		public Integer getRw() {
		return rw;
	}
	
	public BuildObject setRw(Integer rw) {
		this.rw = rw;
		return this;	}
	
		    
    public Boolean hasHealth()  {
        return health == null ? false : true;
    }
        
		public Integer getHealth() {
		return health;
	}
	
	public BuildObject setHealth(Integer health) {
		this.health = health;
		return this;	}
	
		    
    public Boolean hasTemplateId()  {
        return templateId == null ? false : true;
    }
        
		public Integer getTemplateId() {
		return templateId;
	}
	
	public BuildObject setTemplateId(Integer templateId) {
		this.templateId = templateId;
		return this;	}
	
		    
    public Boolean hasGrid()  {
        return grid == null ? false : true;
    }
        
		public String getGrid() {
		return grid;
	}
	
	public BuildObject setGrid(String grid) {
		this.grid = grid;
		return this;	}
	
		    
    public Boolean hasUpdatedAt()  {
        return updatedAt == null ? false : true;
    }
        
		public Long getUpdatedAt() {
		return updatedAt;
	}
	
	public BuildObject setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
		return this;	}
	
		    
    public Boolean hasState()  {
        return state == null ? false : true;
    }
        
		public Integer getState() {
		return state;
	}
	
	public BuildObject setState(Integer state) {
		this.state = state;
		return this;	}
	
		    
    public Boolean hasUpdateId()  {
        return updateId == null ? false : true;
    }
        
		public Integer getUpdateId() {
		return updateId;
	}
	
	public BuildObject setUpdateId(Integer updateId) {
		this.updateId = updateId;
		return this;	}
	
		    
    public Boolean hasIsFloor()  {
        return isFloor == null ? false : true;
    }
        
		public Boolean getIsFloor() {
		return isFloor;
	}
	
	public BuildObject setIsFloor(Boolean isFloor) {
		this.isFloor = isFloor;
		return this;	}
	
		    
    public Boolean hasIsDestructable()  {
        return isDestructable == null ? false : true;
    }
        
		public Boolean getIsDestructable() {
		return isDestructable;
	}
	
	public BuildObject setIsDestructable(Boolean isDestructable) {
		this.isDestructable = isDestructable;
		return this;	}
	
		    
    public Boolean hasHasDoor()  {
        return hasDoor == null ? false : true;
    }
        
		public Boolean getHasDoor() {
		return hasDoor;
	}
	
	public BuildObject setHasDoor(Boolean hasDoor) {
		this.hasDoor = hasDoor;
		return this;	}
	
		    
    public Boolean hasDoorStatus()  {
        return doorStatus == null ? false : true;
    }
        
		public Integer getDoorStatus() {
		return doorStatus;
	}
	
	public BuildObject setDoorStatus(Integer doorStatus) {
		this.doorStatus = doorStatus;
		return this;	}
	
		    
    public Boolean hasGroundBlockObject()  {
        return groundBlockObject == null ? false : true;
    }
        
		public ByteString getGroundBlockObject() {
		return groundBlockObject;
	}
	
	public BuildObject setGroundBlockObject(ByteString groundBlockObject) {
		this.groundBlockObject = groundBlockObject;
		return this;	}
	
		    
    public Boolean hasIsGroundBlock()  {
        return isGroundBlock == null ? false : true;
    }
        
		public Boolean getIsGroundBlock() {
		return isGroundBlock;
	}
	
	public BuildObject setIsGroundBlock(Boolean isGroundBlock) {
		this.isGroundBlock = isGroundBlock;
		return this;	}
	
		    
    public Boolean hasChunk()  {
        return chunk == null ? false : true;
    }
        
		public Integer getChunk() {
		return chunk;
	}
	
	public BuildObject setChunk(Integer chunk) {
		this.chunk = chunk;
		return this;	}
	
		    
    public Boolean hasTerrainEdit()  {
        return terrainEdit == null ? false : true;
    }
        
		public ByteString getTerrainEdit() {
		return terrainEdit;
	}
	
	public BuildObject setTerrainEdit(ByteString terrainEdit) {
		this.terrainEdit = terrainEdit;
		return this;	}
	
		    
    public Boolean hasIsTerrainEdit()  {
        return isTerrainEdit == null ? false : true;
    }
        
		public Boolean getIsTerrainEdit() {
		return isTerrainEdit;
	}
	
	public BuildObject setIsTerrainEdit(Boolean isTerrainEdit) {
		this.isTerrainEdit = isTerrainEdit;
		return this;	}
	
		    
    public Boolean hasCustomBytes()  {
        return customBytes == null ? false : true;
    }
        
		public ByteString getCustomBytes() {
		return customBytes;
	}
	
	public BuildObject setCustomBytes(ByteString customBytes) {
		this.customBytes = customBytes;
		return this;	}
	
		    
    public Boolean hasCustomString()  {
        return customString == null ? false : true;
    }
        
		public String getCustomString() {
		return customString;
	}
	
	public BuildObject setCustomString(String customString) {
		this.customString = customString;
		return this;	}
	
		    
    public Boolean hasTextureId()  {
        return textureId == null ? false : true;
    }
        
		public String getTextureId() {
		return textureId;
	}
	
	public BuildObject setTextureId(String textureId) {
		this.textureId = textureId;
		return this;	}
	
		    
    public Boolean hasSlots()  {
        return slots == null ? false : true;
    }
        
		public ByteString getSlots() {
		return slots;
	}
	
	public BuildObject setSlots(ByteString slots) {
		this.slots = slots;
		return this;	}
	
		    
    public Boolean hasPlacedAt()  {
        return placedAt == null ? false : true;
    }
        
		public Long getPlacedAt() {
		return placedAt;
	}
	
	public BuildObject setPlacedAt(Long placedAt) {
		this.placedAt = placedAt;
		return this;	}
	
		    
    public Boolean hasSlotInfo()  {
        return slotInfo == null ? false : true;
    }
        
		public SlotInfo getSlotInfo() {
		return slotInfo;
	}
	
	public BuildObject setSlotInfo(SlotInfo slotInfo) {
		this.slotInfo = slotInfo;
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

    public Schema<BuildObject> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildObject newMessage()
    {
        return new BuildObject();
    }

    public Class<BuildObject> typeClass()
    {
        return BuildObject.class;
    }

    public String messageName()
    {
        return BuildObject.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildObject.class.getName();
    }

    public boolean isInitialized(BuildObject message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildObject message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.playerItemId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.action = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.ownerId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.x = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.y = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.z = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.rx = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.ry = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.rz = input.readInt32();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.rw = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.templateId = input.readInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.grid = input.readString();
                	break;
                	                	
                            	            	case 22:
            	                	                	message.updatedAt = input.readInt64();
                	break;
                	                	
                            	            	case 23:
            	                	                	message.state = input.readInt32();
                	break;
                	                	
                            	            	case 24:
            	                	                	message.updateId = input.readInt32();
                	break;
                	                	
                            	            	case 25:
            	                	                	message.isFloor = input.readBool();
                	break;
                	                	
                            	            	case 26:
            	                	                	message.isDestructable = input.readBool();
                	break;
                	                	
                            	            	case 27:
            	                	                	message.hasDoor = input.readBool();
                	break;
                	                	
                            	            	case 28:
            	                	                	message.doorStatus = input.readInt32();
                	break;
                	                	
                            	            	case 29:
            	                	                	message.groundBlockObject = input.readBytes();
                	break;
                	                	
                            	            	case 30:
            	                	                	message.isGroundBlock = input.readBool();
                	break;
                	                	
                            	            	case 31:
            	                	                	message.chunk = input.readInt32();
                	break;
                	                	
                            	            	case 32:
            	                	                	message.terrainEdit = input.readBytes();
                	break;
                	                	
                            	            	case 33:
            	                	                	message.isTerrainEdit = input.readBool();
                	break;
                	                	
                            	            	case 34:
            	                	                	message.customBytes = input.readBytes();
                	break;
                	                	
                            	            	case 35:
            	                	                	message.customString = input.readString();
                	break;
                	                	
                            	            	case 36:
            	                	                	message.textureId = input.readString();
                	break;
                	                	
                            	            	case 37:
            	                	                	message.slots = input.readBytes();
                	break;
                	                	
                            	            	case 38:
            	                	                	message.placedAt = input.readInt64();
                	break;
                	                	
                            	            	case 40:
            	                	                	message.slotInfo = input.mergeObject(message.slotInfo, SlotInfo.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildObject message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.playerItemId != null)
            output.writeString(1, message.playerItemId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.action != null)
            output.writeInt32(2, message.action, false);
    	    	
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(3, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(4, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ownerId != null)
            output.writeString(5, message.ownerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.x != null)
            output.writeInt32(6, message.x, false);
    	    	
    	            	
    	    	
    	    	    	if(message.y != null)
            output.writeInt32(7, message.y, false);
    	    	
    	            	
    	    	
    	    	    	if(message.z != null)
            output.writeInt32(8, message.z, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rx != null)
            output.writeInt32(9, message.rx, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ry != null)
            output.writeInt32(10, message.ry, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rz != null)
            output.writeInt32(11, message.rz, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rw != null)
            output.writeInt32(12, message.rw, false);
    	    	
    	            	
    	    	
    	    	    	if(message.health != null)
            output.writeInt32(14, message.health, false);
    	    	
    	            	
    	    	
    	    	    	if(message.templateId != null)
            output.writeInt32(18, message.templateId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.grid != null)
            output.writeString(19, message.grid, false);
    	    	
    	            	
    	    	
    	    	    	if(message.updatedAt != null)
            output.writeInt64(22, message.updatedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.state != null)
            output.writeInt32(23, message.state, false);
    	    	
    	            	
    	    	
    	    	    	if(message.updateId != null)
            output.writeInt32(24, message.updateId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isFloor != null)
            output.writeBool(25, message.isFloor, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isDestructable != null)
            output.writeBool(26, message.isDestructable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.hasDoor != null)
            output.writeBool(27, message.hasDoor, false);
    	    	
    	            	
    	    	
    	    	    	if(message.doorStatus != null)
            output.writeInt32(28, message.doorStatus, false);
    	    	
    	            	
    	    	
    	    	    	if(message.groundBlockObject != null)
            output.writeBytes(29, message.groundBlockObject, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isGroundBlock != null)
            output.writeBool(30, message.isGroundBlock, false);
    	    	
    	            	
    	    	
    	    	    	if(message.chunk != null)
            output.writeInt32(31, message.chunk, false);
    	    	
    	            	
    	    	
    	    	    	if(message.terrainEdit != null)
            output.writeBytes(32, message.terrainEdit, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isTerrainEdit != null)
            output.writeBool(33, message.isTerrainEdit, false);
    	    	
    	            	
    	    	
    	    	    	if(message.customBytes != null)
            output.writeBytes(34, message.customBytes, false);
    	    	
    	            	
    	    	
    	    	    	if(message.customString != null)
            output.writeString(35, message.customString, false);
    	    	
    	            	
    	    	
    	    	    	if(message.textureId != null)
            output.writeString(36, message.textureId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.slots != null)
            output.writeBytes(37, message.slots, false);
    	    	
    	            	
    	    	
    	    	    	if(message.placedAt != null)
            output.writeInt64(38, message.placedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.slotInfo != null)
    		output.writeObject(40, message.slotInfo, SlotInfo.getSchema(), false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerItemId";
        	        	case 2: return "action";
        	        	case 3: return "id";
        	        	case 4: return "recordId";
        	        	case 5: return "ownerId";
        	        	case 6: return "x";
        	        	case 7: return "y";
        	        	case 8: return "z";
        	        	case 9: return "rx";
        	        	case 10: return "ry";
        	        	case 11: return "rz";
        	        	case 12: return "rw";
        	        	case 14: return "health";
        	        	case 18: return "templateId";
        	        	case 19: return "grid";
        	        	case 22: return "updatedAt";
        	        	case 23: return "state";
        	        	case 24: return "updateId";
        	        	case 25: return "isFloor";
        	        	case 26: return "isDestructable";
        	        	case 27: return "hasDoor";
        	        	case 28: return "doorStatus";
        	        	case 29: return "groundBlockObject";
        	        	case 30: return "isGroundBlock";
        	        	case 31: return "chunk";
        	        	case 32: return "terrainEdit";
        	        	case 33: return "isTerrainEdit";
        	        	case 34: return "customBytes";
        	        	case 35: return "customString";
        	        	case 36: return "textureId";
        	        	case 37: return "slots";
        	        	case 38: return "placedAt";
        	        	case 40: return "slotInfo";
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
    	    	__fieldMap.put("playerItemId", 1);
    	    	__fieldMap.put("action", 2);
    	    	__fieldMap.put("id", 3);
    	    	__fieldMap.put("recordId", 4);
    	    	__fieldMap.put("ownerId", 5);
    	    	__fieldMap.put("x", 6);
    	    	__fieldMap.put("y", 7);
    	    	__fieldMap.put("z", 8);
    	    	__fieldMap.put("rx", 9);
    	    	__fieldMap.put("ry", 10);
    	    	__fieldMap.put("rz", 11);
    	    	__fieldMap.put("rw", 12);
    	    	__fieldMap.put("health", 14);
    	    	__fieldMap.put("templateId", 18);
    	    	__fieldMap.put("grid", 19);
    	    	__fieldMap.put("updatedAt", 22);
    	    	__fieldMap.put("state", 23);
    	    	__fieldMap.put("updateId", 24);
    	    	__fieldMap.put("isFloor", 25);
    	    	__fieldMap.put("isDestructable", 26);
    	    	__fieldMap.put("hasDoor", 27);
    	    	__fieldMap.put("doorStatus", 28);
    	    	__fieldMap.put("groundBlockObject", 29);
    	    	__fieldMap.put("isGroundBlock", 30);
    	    	__fieldMap.put("chunk", 31);
    	    	__fieldMap.put("terrainEdit", 32);
    	    	__fieldMap.put("isTerrainEdit", 33);
    	    	__fieldMap.put("customBytes", 34);
    	    	__fieldMap.put("customString", 35);
    	    	__fieldMap.put("textureId", 36);
    	    	__fieldMap.put("slots", 37);
    	    	__fieldMap.put("placedAt", 38);
    	    	__fieldMap.put("slotInfo", 40);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildObject.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildObject parseFrom(byte[] bytes) {
	BuildObject message = new BuildObject();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildObject.getSchema());
	return message;
}

public static BuildObject parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildObject message = new BuildObject();
	JsonIOUtil.mergeFrom(bytes, message, BuildObject.getSchema(), false);
	return message;
}

public BuildObject clone() {
	byte[] bytes = this.toByteArray();
	BuildObject buildObject = BuildObject.parseFrom(bytes);
	return buildObject;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildObject> schema = BuildObject.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildObject.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
