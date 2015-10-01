
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
public final class WorldObject implements Externalizable, Message<WorldObject>, Schema<WorldObject>{



    public static Schema<WorldObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static WorldObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final WorldObject DEFAULT_INSTANCE = new WorldObject();

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
	    
        			public Integer maxHealth;
	    
        			public Integer health;
	    
        			public String parentId;
	    
        			public Boolean destructable;
	    
        			public String prefab;
	    
        			public Integer type;
	    
        			public String grid;
	    
        			public String currentUser;
	    
        			public Integer state;
	    
      
    public WorldObject()
    {
        
    }


	

	    
    public Boolean hasPlayerItemId()  {
        return playerItemId == null ? false : true;
    }
        
		public String getPlayerItemId() {
		return playerItemId;
	}
	
	public WorldObject setPlayerItemId(String playerItemId) {
		this.playerItemId = playerItemId;
		return this;	}
	
		    
    public Boolean hasAction()  {
        return action == null ? false : true;
    }
        
		public Integer getAction() {
		return action;
	}
	
	public WorldObject setAction(Integer action) {
		this.action = action;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public WorldObject setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public WorldObject setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasOwnerId()  {
        return ownerId == null ? false : true;
    }
        
		public String getOwnerId() {
		return ownerId;
	}
	
	public WorldObject setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		    
    public Boolean hasX()  {
        return x == null ? false : true;
    }
        
		public Integer getX() {
		return x;
	}
	
	public WorldObject setX(Integer x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Integer getY() {
		return y;
	}
	
	public WorldObject setY(Integer y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasZ()  {
        return z == null ? false : true;
    }
        
		public Integer getZ() {
		return z;
	}
	
	public WorldObject setZ(Integer z) {
		this.z = z;
		return this;	}
	
		    
    public Boolean hasRx()  {
        return rx == null ? false : true;
    }
        
		public Integer getRx() {
		return rx;
	}
	
	public WorldObject setRx(Integer rx) {
		this.rx = rx;
		return this;	}
	
		    
    public Boolean hasRy()  {
        return ry == null ? false : true;
    }
        
		public Integer getRy() {
		return ry;
	}
	
	public WorldObject setRy(Integer ry) {
		this.ry = ry;
		return this;	}
	
		    
    public Boolean hasRz()  {
        return rz == null ? false : true;
    }
        
		public Integer getRz() {
		return rz;
	}
	
	public WorldObject setRz(Integer rz) {
		this.rz = rz;
		return this;	}
	
		    
    public Boolean hasRw()  {
        return rw == null ? false : true;
    }
        
		public Integer getRw() {
		return rw;
	}
	
	public WorldObject setRw(Integer rw) {
		this.rw = rw;
		return this;	}
	
		    
    public Boolean hasMaxHealth()  {
        return maxHealth == null ? false : true;
    }
        
		public Integer getMaxHealth() {
		return maxHealth;
	}
	
	public WorldObject setMaxHealth(Integer maxHealth) {
		this.maxHealth = maxHealth;
		return this;	}
	
		    
    public Boolean hasHealth()  {
        return health == null ? false : true;
    }
        
		public Integer getHealth() {
		return health;
	}
	
	public WorldObject setHealth(Integer health) {
		this.health = health;
		return this;	}
	
		    
    public Boolean hasParentId()  {
        return parentId == null ? false : true;
    }
        
		public String getParentId() {
		return parentId;
	}
	
	public WorldObject setParentId(String parentId) {
		this.parentId = parentId;
		return this;	}
	
		    
    public Boolean hasDestructable()  {
        return destructable == null ? false : true;
    }
        
		public Boolean getDestructable() {
		return destructable;
	}
	
	public WorldObject setDestructable(Boolean destructable) {
		this.destructable = destructable;
		return this;	}
	
		    
    public Boolean hasPrefab()  {
        return prefab == null ? false : true;
    }
        
		public String getPrefab() {
		return prefab;
	}
	
	public WorldObject setPrefab(String prefab) {
		this.prefab = prefab;
		return this;	}
	
		    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public Integer getType() {
		return type;
	}
	
	public WorldObject setType(Integer type) {
		this.type = type;
		return this;	}
	
		    
    public Boolean hasGrid()  {
        return grid == null ? false : true;
    }
        
		public String getGrid() {
		return grid;
	}
	
	public WorldObject setGrid(String grid) {
		this.grid = grid;
		return this;	}
	
		    
    public Boolean hasCurrentUser()  {
        return currentUser == null ? false : true;
    }
        
		public String getCurrentUser() {
		return currentUser;
	}
	
	public WorldObject setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
		return this;	}
	
		    
    public Boolean hasState()  {
        return state == null ? false : true;
    }
        
		public Integer getState() {
		return state;
	}
	
	public WorldObject setState(Integer state) {
		this.state = state;
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

    public Schema<WorldObject> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public WorldObject newMessage()
    {
        return new WorldObject();
    }

    public Class<WorldObject> typeClass()
    {
        return WorldObject.class;
    }

    public String messageName()
    {
        return WorldObject.class.getSimpleName();
    }

    public String messageFullName()
    {
        return WorldObject.class.getName();
    }

    public boolean isInitialized(WorldObject message)
    {
        return true;
    }

    public void mergeFrom(Input input, WorldObject message) throws IOException
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
                	                	
                            	            	case 13:
            	                	                	message.maxHealth = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.parentId = input.readString();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.destructable = input.readBool();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.prefab = input.readString();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.type = input.readInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.grid = input.readString();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.currentUser = input.readString();
                	break;
                	                	
                            	            	case 21:
            	                	                	message.state = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, WorldObject message) throws IOException
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
    	    	
    	            	
    	    	
    	    	    	if(message.maxHealth != null)
            output.writeInt32(13, message.maxHealth, false);
    	    	
    	            	
    	    	
    	    	    	if(message.health != null)
            output.writeInt32(14, message.health, false);
    	    	
    	            	
    	    	
    	    	    	if(message.parentId != null)
            output.writeString(15, message.parentId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.destructable != null)
            output.writeBool(16, message.destructable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.prefab != null)
            output.writeString(17, message.prefab, false);
    	    	
    	            	
    	    	
    	    	    	if(message.type != null)
            output.writeInt32(18, message.type, false);
    	    	
    	            	
    	    	
    	    	    	if(message.grid != null)
            output.writeString(19, message.grid, false);
    	    	
    	            	
    	    	
    	    	    	if(message.currentUser != null)
            output.writeString(20, message.currentUser, false);
    	    	
    	            	
    	    	
    	    	    	if(message.state != null)
            output.writeInt32(21, message.state, false);
    	    	
    	            	
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
        	        	case 13: return "maxHealth";
        	        	case 14: return "health";
        	        	case 15: return "parentId";
        	        	case 16: return "destructable";
        	        	case 17: return "prefab";
        	        	case 18: return "type";
        	        	case 19: return "grid";
        	        	case 20: return "currentUser";
        	        	case 21: return "state";
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
    	    	__fieldMap.put("maxHealth", 13);
    	    	__fieldMap.put("health", 14);
    	    	__fieldMap.put("parentId", 15);
    	    	__fieldMap.put("destructable", 16);
    	    	__fieldMap.put("prefab", 17);
    	    	__fieldMap.put("type", 18);
    	    	__fieldMap.put("grid", 19);
    	    	__fieldMap.put("currentUser", 20);
    	    	__fieldMap.put("state", 21);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = WorldObject.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static WorldObject parseFrom(byte[] bytes) {
	WorldObject message = new WorldObject();
	ProtobufIOUtil.mergeFrom(bytes, message, WorldObject.getSchema());
	return message;
}

public static WorldObject parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	WorldObject message = new WorldObject();
	JsonIOUtil.mergeFrom(bytes, message, WorldObject.getSchema(), false);
	return message;
}

public WorldObject clone() {
	byte[] bytes = this.toByteArray();
	WorldObject worldObject = WorldObject.parseFrom(bytes);
	return worldObject;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, WorldObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<WorldObject> schema = WorldObject.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, WorldObject.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
