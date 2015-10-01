
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
public final class CraftItem implements Externalizable, Message<CraftItem>, Schema<CraftItem>{



    public static Schema<CraftItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftItem DEFAULT_INSTANCE = new CraftItem();

    			public CraftableItem craftableItem;
	    
        			public Integer result;
	    
        			public String characterId;
	    
        			public Integer craftedQuantity;
	    
        			public String craftedId;
	    
      
    public CraftItem()
    {
        
    }


	

	    
    public Boolean hasCraftableItem()  {
        return craftableItem == null ? false : true;
    }
        
		public CraftableItem getCraftableItem() {
		return craftableItem;
	}
	
	public CraftItem setCraftableItem(CraftableItem craftableItem) {
		this.craftableItem = craftableItem;
		return this;	}
	
		    
    public Boolean hasResult()  {
        return result == null ? false : true;
    }
        
		public Integer getResult() {
		return result;
	}
	
	public CraftItem setResult(Integer result) {
		this.result = result;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public CraftItem setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasCraftedQuantity()  {
        return craftedQuantity == null ? false : true;
    }
        
		public Integer getCraftedQuantity() {
		return craftedQuantity;
	}
	
	public CraftItem setCraftedQuantity(Integer craftedQuantity) {
		this.craftedQuantity = craftedQuantity;
		return this;	}
	
		    
    public Boolean hasCraftedId()  {
        return craftedId == null ? false : true;
    }
        
		public String getCraftedId() {
		return craftedId;
	}
	
	public CraftItem setCraftedId(String craftedId) {
		this.craftedId = craftedId;
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

    public Schema<CraftItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftItem newMessage()
    {
        return new CraftItem();
    }

    public Class<CraftItem> typeClass()
    {
        return CraftItem.class;
    }

    public String messageName()
    {
        return CraftItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftItem.class.getName();
    }

    public boolean isInitialized(CraftItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftItem message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.craftableItem = input.mergeObject(message.craftableItem, CraftableItem.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.result = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.craftedQuantity = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.craftedId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftItem message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.craftableItem != null)
    		output.writeObject(1, message.craftableItem, CraftableItem.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.result != null)
            output.writeInt32(2, message.result, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(3, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.craftedQuantity != null)
            output.writeInt32(4, message.craftedQuantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.craftedId != null)
            output.writeString(5, message.craftedId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "craftableItem";
        	        	case 2: return "result";
        	        	case 3: return "characterId";
        	        	case 4: return "craftedQuantity";
        	        	case 5: return "craftedId";
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
    	    	__fieldMap.put("craftableItem", 1);
    	    	__fieldMap.put("result", 2);
    	    	__fieldMap.put("characterId", 3);
    	    	__fieldMap.put("craftedQuantity", 4);
    	    	__fieldMap.put("craftedId", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftItem parseFrom(byte[] bytes) {
	CraftItem message = new CraftItem();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftItem.getSchema());
	return message;
}

public static CraftItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftItem message = new CraftItem();
	JsonIOUtil.mergeFrom(bytes, message, CraftItem.getSchema(), false);
	return message;
}

public CraftItem clone() {
	byte[] bytes = this.toByteArray();
	CraftItem craftItem = CraftItem.parseFrom(bytes);
	return craftItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftItem> schema = CraftItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftItem.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
