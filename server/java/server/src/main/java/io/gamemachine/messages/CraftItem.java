
package io.gamemachine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
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





import org.javalite.common.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class CraftItem implements Externalizable, Message<CraftItem>, Schema<CraftItem>{

private static final Logger logger = LoggerFactory.getLogger(CraftItem.class);



    public static Schema<CraftItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftItem DEFAULT_INSTANCE = new CraftItem();
    static final String defaultScope = CraftItem.class.getSimpleName();

    	
					public CraftableItem craftableItem = null;
			    
		
    
        	
							    public int result= 0;
		    			    
		
    
        	
							    public String characterId= null;
		    			    
		
    
        	
							    public int craftedQuantity= 0;
		    			    
		
    
        	
							    public String craftedId= null;
		    			    
		
    
        


    public CraftItem()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	    	model.set("craft_item_result",null);
    	    	    	    	    	    	model.set("craft_item_character_id",null);
    	    	    	    	    	    	model.set("craft_item_crafted_quantity",null);
    	    	    	    	    	    	model.set("craft_item_crafted_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	
    	    	    	//if (result != null) {
    	       	    	model.setInteger("craft_item_result",result);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (characterId != null) {
    	       	    	model.setString("craft_item_character_id",characterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (craftedQuantity != null) {
    	       	    	model.setInteger("craft_item_crafted_quantity",craftedQuantity);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (craftedId != null) {
    	       	    	model.setString("craft_item_crafted_id",craftedId);
    	        		
    	//}
    	    	    }
    
	public static CraftItem fromModel(Model model) {
		boolean hasFields = false;
    	CraftItem message = new CraftItem();
    	    	    	    	    	    	
    	    			Integer resultTestField = model.getInteger("craft_item_result");
		if (resultTestField != null) {
			int resultField = resultTestField;
			message.setResult(resultField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String characterIdTestField = model.getString("craft_item_character_id");
		if (characterIdTestField != null) {
			String characterIdField = characterIdTestField;
			message.setCharacterId(characterIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer craftedQuantityTestField = model.getInteger("craft_item_crafted_quantity");
		if (craftedQuantityTestField != null) {
			int craftedQuantityField = craftedQuantityTestField;
			message.setCraftedQuantity(craftedQuantityField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String craftedIdTestField = model.getString("craft_item_crafted_id");
		if (craftedIdTestField != null) {
			String craftedIdField = craftedIdTestField;
			message.setCraftedId(craftedIdField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public CraftableItem getCraftableItem() {
		return craftableItem;
	}
	
	public CraftItem setCraftableItem(CraftableItem craftableItem) {
		this.craftableItem = craftableItem;
		return this;	}
	
		            
		public int getResult() {
		return result;
	}
	
	public CraftItem setResult(int result) {
		this.result = result;
		return this;	}
	
		            
		public String getCharacterId() {
		return characterId;
	}
	
	public CraftItem setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		            
		public int getCraftedQuantity() {
		return craftedQuantity;
	}
	
	public CraftItem setCraftedQuantity(int craftedQuantity) {
		this.craftedQuantity = craftedQuantity;
		return this;	}
	
		            
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
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.result != null) {
            output.writeInt32(2, message.result, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.characterId != null) {
            output.writeString(3, message.characterId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.craftedQuantity != null) {
            output.writeInt32(4, message.craftedQuantity, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.craftedId != null) {
            output.writeString(5, message.craftedId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START CraftItem");
    	    	//if(this.craftableItem != null) {
    		System.out.println("craftableItem="+this.craftableItem);
    	//}
    	    	//if(this.result != null) {
    		System.out.println("result="+this.result);
    	//}
    	    	//if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	//}
    	    	//if(this.craftedQuantity != null) {
    		System.out.println("craftedQuantity="+this.craftedQuantity);
    	//}
    	    	//if(this.craftedId != null) {
    		System.out.println("craftedId="+this.craftedId);
    	//}
    	    	System.out.println("END CraftItem");
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
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, CraftItem.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
