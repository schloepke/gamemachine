
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
public final class CraftableItem implements Externalizable, Message<CraftableItem>, Schema<CraftableItem>{



    public static Schema<CraftableItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftableItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftableItem DEFAULT_INSTANCE = new CraftableItem();

    			public String id;
	    
        			public String item1;
	    
        			public Integer item1_quantity;
	    
        			public String item2;
	    
        			public Integer item2_quantity;
	    
        			public String item3;
	    
        			public Integer item3_quantity;
	    
        			public String item4;
	    
        			public Integer item4_quantity;
	    
        			public Integer recordId;
	    
      
    public CraftableItem()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public CraftableItem setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasItem1()  {
        return item1 == null ? false : true;
    }
        
		public String getItem1() {
		return item1;
	}
	
	public CraftableItem setItem1(String item1) {
		this.item1 = item1;
		return this;	}
	
		    
    public Boolean hasItem1_quantity()  {
        return item1_quantity == null ? false : true;
    }
        
		public Integer getItem1_quantity() {
		return item1_quantity;
	}
	
	public CraftableItem setItem1_quantity(Integer item1_quantity) {
		this.item1_quantity = item1_quantity;
		return this;	}
	
		    
    public Boolean hasItem2()  {
        return item2 == null ? false : true;
    }
        
		public String getItem2() {
		return item2;
	}
	
	public CraftableItem setItem2(String item2) {
		this.item2 = item2;
		return this;	}
	
		    
    public Boolean hasItem2_quantity()  {
        return item2_quantity == null ? false : true;
    }
        
		public Integer getItem2_quantity() {
		return item2_quantity;
	}
	
	public CraftableItem setItem2_quantity(Integer item2_quantity) {
		this.item2_quantity = item2_quantity;
		return this;	}
	
		    
    public Boolean hasItem3()  {
        return item3 == null ? false : true;
    }
        
		public String getItem3() {
		return item3;
	}
	
	public CraftableItem setItem3(String item3) {
		this.item3 = item3;
		return this;	}
	
		    
    public Boolean hasItem3_quantity()  {
        return item3_quantity == null ? false : true;
    }
        
		public Integer getItem3_quantity() {
		return item3_quantity;
	}
	
	public CraftableItem setItem3_quantity(Integer item3_quantity) {
		this.item3_quantity = item3_quantity;
		return this;	}
	
		    
    public Boolean hasItem4()  {
        return item4 == null ? false : true;
    }
        
		public String getItem4() {
		return item4;
	}
	
	public CraftableItem setItem4(String item4) {
		this.item4 = item4;
		return this;	}
	
		    
    public Boolean hasItem4_quantity()  {
        return item4_quantity == null ? false : true;
    }
        
		public Integer getItem4_quantity() {
		return item4_quantity;
	}
	
	public CraftableItem setItem4_quantity(Integer item4_quantity) {
		this.item4_quantity = item4_quantity;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public CraftableItem setRecordId(Integer recordId) {
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

    public Schema<CraftableItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftableItem newMessage()
    {
        return new CraftableItem();
    }

    public Class<CraftableItem> typeClass()
    {
        return CraftableItem.class;
    }

    public String messageName()
    {
        return CraftableItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftableItem.class.getName();
    }

    public boolean isInitialized(CraftableItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftableItem message) throws IOException
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
            	                	                	message.item1 = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.item1_quantity = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.item2 = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.item2_quantity = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.item3 = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.item3_quantity = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.item4 = input.readString();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.item4_quantity = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftableItem message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.item1 != null)
            output.writeString(2, message.item1, false);
    	    	
    	            	
    	    	
    	    	    	if(message.item1_quantity != null)
            output.writeInt32(3, message.item1_quantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.item2 != null)
            output.writeString(4, message.item2, false);
    	    	
    	            	
    	    	
    	    	    	if(message.item2_quantity != null)
            output.writeInt32(5, message.item2_quantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.item3 != null)
            output.writeString(6, message.item3, false);
    	    	
    	            	
    	    	
    	    	    	if(message.item3_quantity != null)
            output.writeInt32(7, message.item3_quantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.item4 != null)
            output.writeString(8, message.item4, false);
    	    	
    	            	
    	    	
    	    	    	if(message.item4_quantity != null)
            output.writeInt32(9, message.item4_quantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(10, message.recordId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "item1";
        	        	case 3: return "item1_quantity";
        	        	case 4: return "item2";
        	        	case 5: return "item2_quantity";
        	        	case 6: return "item3";
        	        	case 7: return "item3_quantity";
        	        	case 8: return "item4";
        	        	case 9: return "item4_quantity";
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
    	    	__fieldMap.put("item1", 2);
    	    	__fieldMap.put("item1_quantity", 3);
    	    	__fieldMap.put("item2", 4);
    	    	__fieldMap.put("item2_quantity", 5);
    	    	__fieldMap.put("item3", 6);
    	    	__fieldMap.put("item3_quantity", 7);
    	    	__fieldMap.put("item4", 8);
    	    	__fieldMap.put("item4_quantity", 9);
    	    	__fieldMap.put("recordId", 10);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftableItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftableItem parseFrom(byte[] bytes) {
	CraftableItem message = new CraftableItem();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftableItem.getSchema());
	return message;
}

public static CraftableItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftableItem message = new CraftableItem();
	JsonIOUtil.mergeFrom(bytes, message, CraftableItem.getSchema(), false);
	return message;
}

public CraftableItem clone() {
	byte[] bytes = this.toByteArray();
	CraftableItem craftableItem = CraftableItem.parseFrom(bytes);
	return craftableItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftableItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftableItem> schema = CraftableItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftableItem.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
