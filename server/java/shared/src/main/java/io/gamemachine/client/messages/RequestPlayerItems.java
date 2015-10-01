
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
public final class RequestPlayerItems implements Externalizable, Message<RequestPlayerItems>, Schema<RequestPlayerItems>{



    public static Schema<RequestPlayerItems> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static RequestPlayerItems getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final RequestPlayerItems DEFAULT_INSTANCE = new RequestPlayerItems();

    			public Boolean catalog;
	    
        			public String characterId;
	    
        			public Integer result;
	    
        			public PlayerItems playerItems;
	    
        			public String query;
	    
      
    public RequestPlayerItems()
    {
        
    }


	

	    
    public Boolean hasCatalog()  {
        return catalog == null ? false : true;
    }
        
		public Boolean getCatalog() {
		return catalog;
	}
	
	public RequestPlayerItems setCatalog(Boolean catalog) {
		this.catalog = catalog;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public RequestPlayerItems setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasResult()  {
        return result == null ? false : true;
    }
        
		public Integer getResult() {
		return result;
	}
	
	public RequestPlayerItems setResult(Integer result) {
		this.result = result;
		return this;	}
	
		    
    public Boolean hasPlayerItems()  {
        return playerItems == null ? false : true;
    }
        
		public PlayerItems getPlayerItems() {
		return playerItems;
	}
	
	public RequestPlayerItems setPlayerItems(PlayerItems playerItems) {
		this.playerItems = playerItems;
		return this;	}
	
		    
    public Boolean hasQuery()  {
        return query == null ? false : true;
    }
        
		public String getQuery() {
		return query;
	}
	
	public RequestPlayerItems setQuery(String query) {
		this.query = query;
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

    public Schema<RequestPlayerItems> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public RequestPlayerItems newMessage()
    {
        return new RequestPlayerItems();
    }

    public Class<RequestPlayerItems> typeClass()
    {
        return RequestPlayerItems.class;
    }

    public String messageName()
    {
        return RequestPlayerItems.class.getSimpleName();
    }

    public String messageFullName()
    {
        return RequestPlayerItems.class.getName();
    }

    public boolean isInitialized(RequestPlayerItems message)
    {
        return true;
    }

    public void mergeFrom(Input input, RequestPlayerItems message) throws IOException
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
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.result = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.playerItems = input.mergeObject(message.playerItems, PlayerItems.getSchema());
                    break;
                                    	
                            	            	case 5:
            	                	                	message.query = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, RequestPlayerItems message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.catalog != null)
            output.writeBool(1, message.catalog, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(2, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.result != null)
            output.writeInt32(3, message.result, false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerItems != null)
    		output.writeObject(4, message.playerItems, PlayerItems.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.query != null)
            output.writeString(5, message.query, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "catalog";
        	        	case 2: return "characterId";
        	        	case 3: return "result";
        	        	case 4: return "playerItems";
        	        	case 5: return "query";
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
    	    	__fieldMap.put("characterId", 2);
    	    	__fieldMap.put("result", 3);
    	    	__fieldMap.put("playerItems", 4);
    	    	__fieldMap.put("query", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = RequestPlayerItems.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static RequestPlayerItems parseFrom(byte[] bytes) {
	RequestPlayerItems message = new RequestPlayerItems();
	ProtobufIOUtil.mergeFrom(bytes, message, RequestPlayerItems.getSchema());
	return message;
}

public static RequestPlayerItems parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	RequestPlayerItems message = new RequestPlayerItems();
	JsonIOUtil.mergeFrom(bytes, message, RequestPlayerItems.getSchema(), false);
	return message;
}

public RequestPlayerItems clone() {
	byte[] bytes = this.toByteArray();
	RequestPlayerItems requestPlayerItems = RequestPlayerItems.parseFrom(bytes);
	return requestPlayerItems;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, RequestPlayerItems.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<RequestPlayerItems> schema = RequestPlayerItems.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RequestPlayerItems.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
