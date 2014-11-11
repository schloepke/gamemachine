
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
public final class PlayerItems implements Externalizable, Message<PlayerItems>, Schema<PlayerItems>{



    public static Schema<PlayerItems> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerItems getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerItems DEFAULT_INSTANCE = new PlayerItems();

    			public Boolean catalog;
	    
            public List<PlayerItem> playerItem;
	  
    public PlayerItems()
    {
        
    }


	

	    
    public Boolean hasCatalog()  {
        return catalog == null ? false : true;
    }
        
		public Boolean getCatalog() {
		return catalog;
	}
	
	public PlayerItems setCatalog(Boolean catalog) {
		this.catalog = catalog;
		return this;	}
	
		    
    public Boolean hasPlayerItem()  {
        return playerItem == null ? false : true;
    }
        
		public List<PlayerItem> getPlayerItemList() {
		if(this.playerItem == null)
            this.playerItem = new ArrayList<PlayerItem>();
		return playerItem;
	}

	public PlayerItems setPlayerItemList(List<PlayerItem> playerItem) {
		this.playerItem = playerItem;
		return this;
	}

	public PlayerItem getPlayerItem(int index)  {
        return playerItem == null ? null : playerItem.get(index);
    }

    public int getPlayerItemCount()  {
        return playerItem == null ? 0 : playerItem.size();
    }

    public PlayerItems addPlayerItem(PlayerItem playerItem)  {
        if(this.playerItem == null)
            this.playerItem = new ArrayList<PlayerItem>();
        this.playerItem.add(playerItem);
        return this;
    }
            	    	    	    	
    public PlayerItems removePlayerItemById(PlayerItem playerItem)  {
    	if(this.playerItem == null)
           return this;
            
       	Iterator<PlayerItem> itr = this.playerItem.iterator();
       	while (itr.hasNext()) {
    	PlayerItem obj = itr.next();
    	
    	    		if (playerItem.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerItems removePlayerItemByName(PlayerItem playerItem)  {
    	if(this.playerItem == null)
           return this;
            
       	Iterator<PlayerItem> itr = this.playerItem.iterator();
       	while (itr.hasNext()) {
    	PlayerItem obj = itr.next();
    	
    	    		if (playerItem.name.equals(obj.name)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerItems removePlayerItemByQuantity(PlayerItem playerItem)  {
    	if(this.playerItem == null)
           return this;
            
       	Iterator<PlayerItem> itr = this.playerItem.iterator();
       	while (itr.hasNext()) {
    	PlayerItem obj = itr.next();
    	
    	    		if (playerItem.quantity.equals(obj.quantity)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerItems removePlayerItemByColor(PlayerItem playerItem)  {
    	if(this.playerItem == null)
           return this;
            
       	Iterator<PlayerItem> itr = this.playerItem.iterator();
       	while (itr.hasNext()) {
    	PlayerItem obj = itr.next();
    	
    	    		if (playerItem.color.equals(obj.color)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	    	
    public PlayerItems removePlayerItemByPlayerId(PlayerItem playerItem)  {
    	if(this.playerItem == null)
           return this;
            
       	Iterator<PlayerItem> itr = this.playerItem.iterator();
       	while (itr.hasNext()) {
    	PlayerItem obj = itr.next();
    	
    	    		if (playerItem.playerId.equals(obj.playerId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerItems removePlayerItemByRecordId(PlayerItem playerItem)  {
    	if(this.playerItem == null)
           return this;
            
       	Iterator<PlayerItem> itr = this.playerItem.iterator();
       	while (itr.hasNext()) {
    	PlayerItem obj = itr.next();
    	
    	    		if (playerItem.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
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

    public Schema<PlayerItems> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerItems newMessage()
    {
        return new PlayerItems();
    }

    public Class<PlayerItems> typeClass()
    {
        return PlayerItems.class;
    }

    public String messageName()
    {
        return PlayerItems.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerItems.class.getName();
    }

    public boolean isInitialized(PlayerItems message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerItems message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.catalog = input.readBool();
                	break;
                	                	
                            	            	case 2:
            	            		if(message.playerItem == null)
                        message.playerItem = new ArrayList<PlayerItem>();
                                        message.playerItem.add(input.mergeObject(null, PlayerItem.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerItems message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.catalog != null)
            output.writeBool(1, message.catalog, false);
    	    	
    	            	
    	    	
    	    	if(message.playerItem != null)
        {
            for(PlayerItem playerItem : message.playerItem)
            {
                if(playerItem != null) {
                   	    				output.writeObject(2, playerItem, PlayerItem.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "catalog";
        	        	case 2: return "playerItem";
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
    	    	__fieldMap.put("catalog", 1);
    	    	__fieldMap.put("playerItem", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerItems.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerItems parseFrom(byte[] bytes) {
	PlayerItems message = new PlayerItems();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerItems.getSchema());
	return message;
}

public static PlayerItems parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerItems message = new PlayerItems();
	JsonIOUtil.mergeFrom(bytes, message, PlayerItems.getSchema(), false);
	return message;
}

public PlayerItems clone() {
	byte[] bytes = this.toByteArray();
	PlayerItems playerItems = PlayerItems.parseFrom(bytes);
	return playerItems;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerItems.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerItems> schema = PlayerItems.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerItems.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
