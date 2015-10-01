
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
public final class Harvest implements Externalizable, Message<Harvest>, Schema<Harvest>{



    public static Schema<Harvest> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Harvest getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Harvest DEFAULT_INSTANCE = new Harvest();

    			public String id;
	    
        			public Integer result;
	    
        			public Long harvestedAt;
	    
        			public String characterId;
	    
        			public String itemId;
	    
      
    public Harvest()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Harvest setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasResult()  {
        return result == null ? false : true;
    }
        
		public Integer getResult() {
		return result;
	}
	
	public Harvest setResult(Integer result) {
		this.result = result;
		return this;	}
	
		    
    public Boolean hasHarvestedAt()  {
        return harvestedAt == null ? false : true;
    }
        
		public Long getHarvestedAt() {
		return harvestedAt;
	}
	
	public Harvest setHarvestedAt(Long harvestedAt) {
		this.harvestedAt = harvestedAt;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public Harvest setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasItemId()  {
        return itemId == null ? false : true;
    }
        
		public String getItemId() {
		return itemId;
	}
	
	public Harvest setItemId(String itemId) {
		this.itemId = itemId;
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

    public Schema<Harvest> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Harvest newMessage()
    {
        return new Harvest();
    }

    public Class<Harvest> typeClass()
    {
        return Harvest.class;
    }

    public String messageName()
    {
        return Harvest.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Harvest.class.getName();
    }

    public boolean isInitialized(Harvest message)
    {
        return true;
    }

    public void mergeFrom(Input input, Harvest message) throws IOException
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
            	                	                	message.result = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.harvestedAt = input.readInt64();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.itemId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Harvest message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.result != null)
            output.writeInt32(2, message.result, false);
    	    	
    	            	
    	    	
    	    	    	if(message.harvestedAt != null)
            output.writeInt64(3, message.harvestedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(4, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.itemId != null)
            output.writeString(5, message.itemId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "result";
        	        	case 3: return "harvestedAt";
        	        	case 4: return "characterId";
        	        	case 5: return "itemId";
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
    	    	__fieldMap.put("result", 2);
    	    	__fieldMap.put("harvestedAt", 3);
    	    	__fieldMap.put("characterId", 4);
    	    	__fieldMap.put("itemId", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Harvest.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Harvest parseFrom(byte[] bytes) {
	Harvest message = new Harvest();
	ProtobufIOUtil.mergeFrom(bytes, message, Harvest.getSchema());
	return message;
}

public static Harvest parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Harvest message = new Harvest();
	JsonIOUtil.mergeFrom(bytes, message, Harvest.getSchema(), false);
	return message;
}

public Harvest clone() {
	byte[] bytes = this.toByteArray();
	Harvest harvest = Harvest.parseFrom(bytes);
	return harvest;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Harvest.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Harvest> schema = Harvest.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Harvest.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
