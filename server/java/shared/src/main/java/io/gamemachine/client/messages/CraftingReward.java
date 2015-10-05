
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
public final class CraftingReward implements Externalizable, Message<CraftingReward>, Schema<CraftingReward>{



    public static Schema<CraftingReward> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftingReward getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftingReward DEFAULT_INSTANCE = new CraftingReward();

    			public String id;
	    
        			public int quantity;
	    
        			public int level;
	    
      
    public CraftingReward()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public CraftingReward setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasQuantity()  {
        return quantity == null ? false : true;
    }
        
		public int getQuantity() {
		return quantity;
	}
	
	public CraftingReward setQuantity(int quantity) {
		this.quantity = quantity;
		return this;	}
	
		    
    public Boolean hasLevel()  {
        return level == null ? false : true;
    }
        
		public int getLevel() {
		return level;
	}
	
	public CraftingReward setLevel(int level) {
		this.level = level;
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

    public Schema<CraftingReward> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftingReward newMessage()
    {
        return new CraftingReward();
    }

    public Class<CraftingReward> typeClass()
    {
        return CraftingReward.class;
    }

    public String messageName()
    {
        return CraftingReward.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftingReward.class.getName();
    }

    public boolean isInitialized(CraftingReward message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftingReward message) throws IOException
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
            	                	                	message.level = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftingReward message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	if(message.quantity == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.quantity != null)
            output.writeInt32(2, message.quantity, false);
    	    	
    	            	
    	    	if(message.level == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.level != null)
            output.writeInt32(3, message.level, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "quantity";
        	        	case 3: return "level";
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
    	    	__fieldMap.put("level", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftingReward.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftingReward parseFrom(byte[] bytes) {
	CraftingReward message = new CraftingReward();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftingReward.getSchema());
	return message;
}

public static CraftingReward parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftingReward message = new CraftingReward();
	JsonIOUtil.mergeFrom(bytes, message, CraftingReward.getSchema(), false);
	return message;
}

public CraftingReward clone() {
	byte[] bytes = this.toByteArray();
	CraftingReward craftingReward = CraftingReward.parseFrom(bytes);
	return craftingReward;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftingReward.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftingReward> schema = CraftingReward.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftingReward.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
