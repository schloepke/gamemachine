
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
public final class CraftingElement implements Externalizable, Message<CraftingElement>, Schema<CraftingElement>{



    public static Schema<CraftingElement> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftingElement getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftingElement DEFAULT_INSTANCE = new CraftingElement();

    			public String id;
	    
        			public int quantity;
	    
        			public int level;
	    
        			public int order;
	    
      
    public CraftingElement()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public CraftingElement setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasQuantity()  {
        return quantity == null ? false : true;
    }
        
		public int getQuantity() {
		return quantity;
	}
	
	public CraftingElement setQuantity(int quantity) {
		this.quantity = quantity;
		return this;	}
	
		    
    public Boolean hasLevel()  {
        return level == null ? false : true;
    }
        
		public int getLevel() {
		return level;
	}
	
	public CraftingElement setLevel(int level) {
		this.level = level;
		return this;	}
	
		    
    public Boolean hasOrder()  {
        return order == null ? false : true;
    }
        
		public int getOrder() {
		return order;
	}
	
	public CraftingElement setOrder(int order) {
		this.order = order;
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

    public Schema<CraftingElement> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftingElement newMessage()
    {
        return new CraftingElement();
    }

    public Class<CraftingElement> typeClass()
    {
        return CraftingElement.class;
    }

    public String messageName()
    {
        return CraftingElement.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftingElement.class.getName();
    }

    public boolean isInitialized(CraftingElement message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftingElement message) throws IOException
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
                	                	
                            	            	case 4:
            	                	                	message.order = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftingElement message) throws IOException
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
    	    	
    	            	
    	    	if(message.order == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.order != null)
            output.writeInt32(4, message.order, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "quantity";
        	        	case 3: return "level";
        	        	case 4: return "order";
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
    	    	__fieldMap.put("order", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftingElement.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftingElement parseFrom(byte[] bytes) {
	CraftingElement message = new CraftingElement();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftingElement.getSchema());
	return message;
}

public static CraftingElement parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftingElement message = new CraftingElement();
	JsonIOUtil.mergeFrom(bytes, message, CraftingElement.getSchema(), false);
	return message;
}

public CraftingElement clone() {
	byte[] bytes = this.toByteArray();
	CraftingElement craftingElement = CraftingElement.parseFrom(bytes);
	return craftingElement;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftingElement.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftingElement> schema = CraftingElement.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftingElement.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
