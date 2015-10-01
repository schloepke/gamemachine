
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
public final class AddPlayerItem implements Externalizable, Message<AddPlayerItem>, Schema<AddPlayerItem>{



    public static Schema<AddPlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static AddPlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final AddPlayerItem DEFAULT_INSTANCE = new AddPlayerItem();

    			public String id;
	    
        			public Integer quantity;
	    
        			public Integer result;
	    
        			public String characterId;
	    
        			public PlayerItem playerItem;
	    
        			public String containerId;
	    
      
    public AddPlayerItem()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public AddPlayerItem setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasQuantity()  {
        return quantity == null ? false : true;
    }
        
		public Integer getQuantity() {
		return quantity;
	}
	
	public AddPlayerItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;	}
	
		    
    public Boolean hasResult()  {
        return result == null ? false : true;
    }
        
		public Integer getResult() {
		return result;
	}
	
	public AddPlayerItem setResult(Integer result) {
		this.result = result;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public AddPlayerItem setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasPlayerItem()  {
        return playerItem == null ? false : true;
    }
        
		public PlayerItem getPlayerItem() {
		return playerItem;
	}
	
	public AddPlayerItem setPlayerItem(PlayerItem playerItem) {
		this.playerItem = playerItem;
		return this;	}
	
		    
    public Boolean hasContainerId()  {
        return containerId == null ? false : true;
    }
        
		public String getContainerId() {
		return containerId;
	}
	
	public AddPlayerItem setContainerId(String containerId) {
		this.containerId = containerId;
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

    public Schema<AddPlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public AddPlayerItem newMessage()
    {
        return new AddPlayerItem();
    }

    public Class<AddPlayerItem> typeClass()
    {
        return AddPlayerItem.class;
    }

    public String messageName()
    {
        return AddPlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return AddPlayerItem.class.getName();
    }

    public boolean isInitialized(AddPlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, AddPlayerItem message) throws IOException
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
            	                	                	message.quantity = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.result = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.playerItem = input.mergeObject(message.playerItem, PlayerItem.getSchema());
                    break;
                                    	
                            	            	case 6:
            	                	                	message.containerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, AddPlayerItem message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.quantity != null)
            output.writeInt32(2, message.quantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.result != null)
            output.writeInt32(3, message.result, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(4, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerItem != null)
    		output.writeObject(5, message.playerItem, PlayerItem.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.containerId != null)
            output.writeString(6, message.containerId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "quantity";
        	        	case 3: return "result";
        	        	case 4: return "characterId";
        	        	case 5: return "playerItem";
        	        	case 6: return "containerId";
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
    	    	__fieldMap.put("quantity", 2);
    	    	__fieldMap.put("result", 3);
    	    	__fieldMap.put("characterId", 4);
    	    	__fieldMap.put("playerItem", 5);
    	    	__fieldMap.put("containerId", 6);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = AddPlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static AddPlayerItem parseFrom(byte[] bytes) {
	AddPlayerItem message = new AddPlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, AddPlayerItem.getSchema());
	return message;
}

public static AddPlayerItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	AddPlayerItem message = new AddPlayerItem();
	JsonIOUtil.mergeFrom(bytes, message, AddPlayerItem.getSchema(), false);
	return message;
}

public AddPlayerItem clone() {
	byte[] bytes = this.toByteArray();
	AddPlayerItem addPlayerItem = AddPlayerItem.parseFrom(bytes);
	return addPlayerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, AddPlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<AddPlayerItem> schema = AddPlayerItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, AddPlayerItem.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
