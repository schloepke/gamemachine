
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

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;


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
	    
        			public Weapon weapon;
	    
        			public Consumable consumable;
	    
        			public Cost cost;
	    
        			public String playerId;
	    
        			public Integer recordId;
	    
      
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
        
		public Weapon getWeapon() {
		return weapon;
	}
	
	public PlayerItem setWeapon(Weapon weapon) {
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
            	                	                	message.weapon = input.mergeObject(message.weapon, Weapon.getSchema());
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
    		output.writeObject(5, message.weapon, Weapon.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.consumable != null)
    		output.writeObject(6, message.consumable, Consumable.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.cost != null)
    		output.writeObject(8, message.cost, Cost.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(9, message.playerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(10, message.recordId, false);
    	    	
    	            	
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
