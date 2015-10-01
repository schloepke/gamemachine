
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
public final class UseItem implements Externalizable, Message<UseItem>, Schema<UseItem>{



    public static Schema<UseItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static UseItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final UseItem DEFAULT_INSTANCE = new UseItem();

    			public String id;
	    
        			public String targetId;
	    
        			public String action;
	    
        			public String playerId;
	    
      
    public UseItem()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public UseItem setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasTargetId()  {
        return targetId == null ? false : true;
    }
        
		public String getTargetId() {
		return targetId;
	}
	
	public UseItem setTargetId(String targetId) {
		this.targetId = targetId;
		return this;	}
	
		    
    public Boolean hasAction()  {
        return action == null ? false : true;
    }
        
		public String getAction() {
		return action;
	}
	
	public UseItem setAction(String action) {
		this.action = action;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public UseItem setPlayerId(String playerId) {
		this.playerId = playerId;
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

    public Schema<UseItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public UseItem newMessage()
    {
        return new UseItem();
    }

    public Class<UseItem> typeClass()
    {
        return UseItem.class;
    }

    public String messageName()
    {
        return UseItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return UseItem.class.getName();
    }

    public boolean isInitialized(UseItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, UseItem message) throws IOException
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
            	                	                	message.targetId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.action = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, UseItem message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.targetId != null)
            output.writeString(2, message.targetId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.action != null)
            output.writeString(3, message.action, false);
    	    	
    	            	
    	    	if(message.playerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(4, message.playerId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "targetId";
        	        	case 3: return "action";
        	        	case 4: return "playerId";
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
    	    	__fieldMap.put("targetId", 2);
    	    	__fieldMap.put("action", 3);
    	    	__fieldMap.put("playerId", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = UseItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static UseItem parseFrom(byte[] bytes) {
	UseItem message = new UseItem();
	ProtobufIOUtil.mergeFrom(bytes, message, UseItem.getSchema());
	return message;
}

public static UseItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	UseItem message = new UseItem();
	JsonIOUtil.mergeFrom(bytes, message, UseItem.getSchema(), false);
	return message;
}

public UseItem clone() {
	byte[] bytes = this.toByteArray();
	UseItem useItem = UseItem.parseFrom(bytes);
	return useItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, UseItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<UseItem> schema = UseItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, UseItem.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
