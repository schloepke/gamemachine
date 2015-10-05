
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
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class CraftingRecipes implements Externalizable, Message<CraftingRecipes>, Schema<CraftingRecipes>{



    public static Schema<CraftingRecipes> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftingRecipes getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftingRecipes DEFAULT_INSTANCE = new CraftingRecipes();
    static final String defaultScope = CraftingRecipes.class.getSimpleName();

        public List<CraftingRecipe> craftingRecipes;
	    	
	    	    public String itemCatalog= null;
	    		
    
        


    public CraftingRecipes()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	    	model.set("crafting_recipes_item_catalog",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	
    	    	    	//if (itemCatalog != null) {
    	       	    	model.setString("crafting_recipes_item_catalog",itemCatalog);
    	        		
    	//}
    	    	    }
    
	public static CraftingRecipes fromModel(Model model) {
		boolean hasFields = false;
    	CraftingRecipes message = new CraftingRecipes();
    	    	    	    	    	    	
    	    	    	String itemCatalogTestField = model.getString("crafting_recipes_item_catalog");
    	if (itemCatalogTestField != null) {
    		String itemCatalogField = itemCatalogTestField;
    		message.setItemCatalog(itemCatalogField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public List<CraftingRecipe> getCraftingRecipesList() {
		if(this.craftingRecipes == null)
            this.craftingRecipes = new ArrayList<CraftingRecipe>();
		return craftingRecipes;
	}

	public CraftingRecipes setCraftingRecipesList(List<CraftingRecipe> craftingRecipes) {
		this.craftingRecipes = craftingRecipes;
		return this;
	}

	public CraftingRecipe getCraftingRecipes(int index)  {
        return craftingRecipes == null ? null : craftingRecipes.get(index);
    }

    public int getCraftingRecipesCount()  {
        return craftingRecipes == null ? 0 : craftingRecipes.size();
    }

    public CraftingRecipes addCraftingRecipes(CraftingRecipe craftingRecipes)  {
        if(this.craftingRecipes == null)
            this.craftingRecipes = new ArrayList<CraftingRecipe>();
        this.craftingRecipes.add(craftingRecipes);
        return this;
    }
            	    	    	    	
    public CraftingRecipes removeCraftingRecipesById(CraftingRecipe craftingRecipes)  {
    	if(this.craftingRecipes == null)
           return this;
            
       	Iterator<CraftingRecipe> itr = this.craftingRecipes.iterator();
       	while (itr.hasNext()) {
    	CraftingRecipe obj = itr.next();
    	
    	    		if (craftingRecipes.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	
    public CraftingRecipes removeCraftingRecipesByOrderMatters(CraftingRecipe craftingRecipes)  {
    	if(this.craftingRecipes == null)
           return this;
            
       	Iterator<CraftingRecipe> itr = this.craftingRecipes.iterator();
       	while (itr.hasNext()) {
    	CraftingRecipe obj = itr.next();
    	
    	    		if (craftingRecipes.orderMatters == obj.orderMatters) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipes removeCraftingRecipesByTimeToComplete(CraftingRecipe craftingRecipes)  {
    	if(this.craftingRecipes == null)
           return this;
            
       	Iterator<CraftingRecipe> itr = this.craftingRecipes.iterator();
       	while (itr.hasNext()) {
    	CraftingRecipe obj = itr.next();
    	
    	    		if (craftingRecipes.timeToComplete == obj.timeToComplete) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public CraftingRecipes removeCraftingRecipesByMaxUses(CraftingRecipe craftingRecipes)  {
    	if(this.craftingRecipes == null)
           return this;
            
       	Iterator<CraftingRecipe> itr = this.craftingRecipes.iterator();
       	while (itr.hasNext()) {
    	CraftingRecipe obj = itr.next();
    	
    	    		if (craftingRecipes.maxUses == obj.maxUses) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipes removeCraftingRecipesByActive(CraftingRecipe craftingRecipes)  {
    	if(this.craftingRecipes == null)
           return this;
            
       	Iterator<CraftingRecipe> itr = this.craftingRecipes.iterator();
       	while (itr.hasNext()) {
    	CraftingRecipe obj = itr.next();
    	
    	    		if (craftingRecipes.active == obj.active) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		            
		public String getItemCatalog() {
		return itemCatalog;
	}
	
	public CraftingRecipes setItemCatalog(String itemCatalog) {
		this.itemCatalog = itemCatalog;
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

    public Schema<CraftingRecipes> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftingRecipes newMessage()
    {
        return new CraftingRecipes();
    }

    public Class<CraftingRecipes> typeClass()
    {
        return CraftingRecipes.class;
    }

    public String messageName()
    {
        return CraftingRecipes.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftingRecipes.class.getName();
    }

    public boolean isInitialized(CraftingRecipes message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftingRecipes message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.craftingRecipes == null)
                        message.craftingRecipes = new ArrayList<CraftingRecipe>();
                                        message.craftingRecipes.add(input.mergeObject(null, CraftingRecipe.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.itemCatalog = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftingRecipes message) throws IOException
    {
    	    	
    	    	
    	    	if(message.craftingRecipes != null)
        {
            for(CraftingRecipe craftingRecipes : message.craftingRecipes)
            {
                if( (CraftingRecipe) craftingRecipes != null) {
                   	    				output.writeObject(1, craftingRecipes, CraftingRecipe.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	//if(message.itemCatalog == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.itemCatalog != null) {
            output.writeString(2, message.itemCatalog, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START CraftingRecipes");
    	    	//if(this.craftingRecipes != null) {
    		System.out.println("craftingRecipes="+this.craftingRecipes);
    	//}
    	    	//if(this.itemCatalog != null) {
    		System.out.println("itemCatalog="+this.itemCatalog);
    	//}
    	    	System.out.println("END CraftingRecipes");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "craftingRecipes";
        	        	case 2: return "itemCatalog";
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
    	    	__fieldMap.put("craftingRecipes", 1);
    	    	__fieldMap.put("itemCatalog", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftingRecipes.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftingRecipes parseFrom(byte[] bytes) {
	CraftingRecipes message = new CraftingRecipes();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftingRecipes.getSchema());
	return message;
}

public static CraftingRecipes parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftingRecipes message = new CraftingRecipes();
	JsonIOUtil.mergeFrom(bytes, message, CraftingRecipes.getSchema(), false);
	return message;
}

public CraftingRecipes clone() {
	byte[] bytes = this.toByteArray();
	CraftingRecipes craftingRecipes = CraftingRecipes.parseFrom(bytes);
	return craftingRecipes;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftingRecipes.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftingRecipes> schema = CraftingRecipes.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftingRecipes.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, CraftingRecipes.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
