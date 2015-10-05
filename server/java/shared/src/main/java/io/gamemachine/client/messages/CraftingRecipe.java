
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
public final class CraftingRecipe implements Externalizable, Message<CraftingRecipe>, Schema<CraftingRecipe>{



    public static Schema<CraftingRecipe> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftingRecipe getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftingRecipe DEFAULT_INSTANCE = new CraftingRecipe();

    			public String id;
	    
            public List<CraftingElement> elements;
	    			public CraftingTool tool;
	    
        			public boolean orderMatters;
	    
        			public float timeToComplete;
	    
            public List<CraftingReward> rewards;
	    			public int maxUses;
	    
        			public boolean active;
	    
      
    public CraftingRecipe()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public CraftingRecipe setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasElements()  {
        return elements == null ? false : true;
    }
        
		public List<CraftingElement> getElementsList() {
		if(this.elements == null)
            this.elements = new ArrayList<CraftingElement>();
		return elements;
	}

	public CraftingRecipe setElementsList(List<CraftingElement> elements) {
		this.elements = elements;
		return this;
	}

	public CraftingElement getElements(int index)  {
        return elements == null ? null : elements.get(index);
    }

    public int getElementsCount()  {
        return elements == null ? 0 : elements.size();
    }

    public CraftingRecipe addElements(CraftingElement elements)  {
        if(this.elements == null)
            this.elements = new ArrayList<CraftingElement>();
        this.elements.add(elements);
        return this;
    }
            	    	    	    	
    public CraftingRecipe removeElementsById(CraftingElement elements)  {
    	if(this.elements == null)
           return this;
            
       	Iterator<CraftingElement> itr = this.elements.iterator();
       	while (itr.hasNext()) {
    	CraftingElement obj = itr.next();
    	
    	    		if (elements.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeElementsByQuantity(CraftingElement elements)  {
    	if(this.elements == null)
           return this;
            
       	Iterator<CraftingElement> itr = this.elements.iterator();
       	while (itr.hasNext()) {
    	CraftingElement obj = itr.next();
    	
    	    		if (elements.quantity.equals(obj.quantity)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeElementsByLevel(CraftingElement elements)  {
    	if(this.elements == null)
           return this;
            
       	Iterator<CraftingElement> itr = this.elements.iterator();
       	while (itr.hasNext()) {
    	CraftingElement obj = itr.next();
    	
    	    		if (elements.level.equals(obj.level)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeElementsByOrder(CraftingElement elements)  {
    	if(this.elements == null)
           return this;
            
       	Iterator<CraftingElement> itr = this.elements.iterator();
       	while (itr.hasNext()) {
    	CraftingElement obj = itr.next();
    	
    	    		if (elements.order.equals(obj.order)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		    
    public Boolean hasTool()  {
        return tool == null ? false : true;
    }
        
		public CraftingTool getTool() {
		return tool;
	}
	
	public CraftingRecipe setTool(CraftingTool tool) {
		this.tool = tool;
		return this;	}
	
		    
    public Boolean hasOrderMatters()  {
        return orderMatters == null ? false : true;
    }
        
		public boolean getOrderMatters() {
		return orderMatters;
	}
	
	public CraftingRecipe setOrderMatters(boolean orderMatters) {
		this.orderMatters = orderMatters;
		return this;	}
	
		    
    public Boolean hasTimeToComplete()  {
        return timeToComplete == null ? false : true;
    }
        
		public float getTimeToComplete() {
		return timeToComplete;
	}
	
	public CraftingRecipe setTimeToComplete(float timeToComplete) {
		this.timeToComplete = timeToComplete;
		return this;	}
	
		    
    public Boolean hasRewards()  {
        return rewards == null ? false : true;
    }
        
		public List<CraftingReward> getRewardsList() {
		if(this.rewards == null)
            this.rewards = new ArrayList<CraftingReward>();
		return rewards;
	}

	public CraftingRecipe setRewardsList(List<CraftingReward> rewards) {
		this.rewards = rewards;
		return this;
	}

	public CraftingReward getRewards(int index)  {
        return rewards == null ? null : rewards.get(index);
    }

    public int getRewardsCount()  {
        return rewards == null ? 0 : rewards.size();
    }

    public CraftingRecipe addRewards(CraftingReward rewards)  {
        if(this.rewards == null)
            this.rewards = new ArrayList<CraftingReward>();
        this.rewards.add(rewards);
        return this;
    }
            	    	    	    	
    public CraftingRecipe removeRewardsById(CraftingReward rewards)  {
    	if(this.rewards == null)
           return this;
            
       	Iterator<CraftingReward> itr = this.rewards.iterator();
       	while (itr.hasNext()) {
    	CraftingReward obj = itr.next();
    	
    	    		if (rewards.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeRewardsByQuantity(CraftingReward rewards)  {
    	if(this.rewards == null)
           return this;
            
       	Iterator<CraftingReward> itr = this.rewards.iterator();
       	while (itr.hasNext()) {
    	CraftingReward obj = itr.next();
    	
    	    		if (rewards.quantity.equals(obj.quantity)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeRewardsByLevel(CraftingReward rewards)  {
    	if(this.rewards == null)
           return this;
            
       	Iterator<CraftingReward> itr = this.rewards.iterator();
       	while (itr.hasNext()) {
    	CraftingReward obj = itr.next();
    	
    	    		if (rewards.level.equals(obj.level)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		    
    public Boolean hasMaxUses()  {
        return maxUses == null ? false : true;
    }
        
		public int getMaxUses() {
		return maxUses;
	}
	
	public CraftingRecipe setMaxUses(int maxUses) {
		this.maxUses = maxUses;
		return this;	}
	
		    
    public Boolean hasActive()  {
        return active == null ? false : true;
    }
        
		public boolean getActive() {
		return active;
	}
	
	public CraftingRecipe setActive(boolean active) {
		this.active = active;
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

    public Schema<CraftingRecipe> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftingRecipe newMessage()
    {
        return new CraftingRecipe();
    }

    public Class<CraftingRecipe> typeClass()
    {
        return CraftingRecipe.class;
    }

    public String messageName()
    {
        return CraftingRecipe.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftingRecipe.class.getName();
    }

    public boolean isInitialized(CraftingRecipe message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftingRecipe message) throws IOException
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
            	            		if(message.elements == null)
                        message.elements = new ArrayList<CraftingElement>();
                                        message.elements.add(input.mergeObject(null, CraftingElement.getSchema()));
                                        break;
                            	            	case 3:
            	                	                	message.tool = input.mergeObject(message.tool, CraftingTool.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.orderMatters = input.readBool();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.timeToComplete = input.readFloat();
                	break;
                	                	
                            	            	case 6:
            	            		if(message.rewards == null)
                        message.rewards = new ArrayList<CraftingReward>();
                                        message.rewards.add(input.mergeObject(null, CraftingReward.getSchema()));
                                        break;
                            	            	case 7:
            	                	                	message.maxUses = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.active = input.readBool();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftingRecipe message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	if(message.elements != null)
        {
            for(CraftingElement elements : message.elements)
            {
                if(elements != null) {
                   	    				output.writeObject(2, elements, CraftingElement.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.tool != null)
    		output.writeObject(3, message.tool, CraftingTool.getSchema(), false);
    	    	
    	            	
    	    	if(message.orderMatters == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.orderMatters != null)
            output.writeBool(4, message.orderMatters, false);
    	    	
    	            	
    	    	if(message.timeToComplete == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.timeToComplete != null)
            output.writeFloat(5, message.timeToComplete, false);
    	    	
    	            	
    	    	
    	    	if(message.rewards != null)
        {
            for(CraftingReward rewards : message.rewards)
            {
                if(rewards != null) {
                   	    				output.writeObject(6, rewards, CraftingReward.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	if(message.maxUses == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.maxUses != null)
            output.writeInt32(7, message.maxUses, false);
    	    	
    	            	
    	    	if(message.active == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.active != null)
            output.writeBool(8, message.active, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "elements";
        	        	case 3: return "tool";
        	        	case 4: return "orderMatters";
        	        	case 5: return "timeToComplete";
        	        	case 6: return "rewards";
        	        	case 7: return "maxUses";
        	        	case 8: return "active";
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
    	    	__fieldMap.put("elements", 2);
    	    	__fieldMap.put("tool", 3);
    	    	__fieldMap.put("orderMatters", 4);
    	    	__fieldMap.put("timeToComplete", 5);
    	    	__fieldMap.put("rewards", 6);
    	    	__fieldMap.put("maxUses", 7);
    	    	__fieldMap.put("active", 8);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftingRecipe.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftingRecipe parseFrom(byte[] bytes) {
	CraftingRecipe message = new CraftingRecipe();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftingRecipe.getSchema());
	return message;
}

public static CraftingRecipe parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftingRecipe message = new CraftingRecipe();
	JsonIOUtil.mergeFrom(bytes, message, CraftingRecipe.getSchema(), false);
	return message;
}

public CraftingRecipe clone() {
	byte[] bytes = this.toByteArray();
	CraftingRecipe craftingRecipe = CraftingRecipe.parseFrom(bytes);
	return craftingRecipe;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftingRecipe.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftingRecipe> schema = CraftingRecipe.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftingRecipe.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
