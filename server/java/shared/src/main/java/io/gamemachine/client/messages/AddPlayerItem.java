
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

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class AddPlayerItem implements Externalizable, Message<AddPlayerItem>, Schema<AddPlayerItem>{



    public static Schema<AddPlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static AddPlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final AddPlayerItem DEFAULT_INSTANCE = new AddPlayerItem();

    			public PlayerItem playerItem;
	    
      
    public AddPlayerItem()
    {
        
    }


	

	    
    public Boolean hasPlayerItem()  {
        return playerItem == null ? false : true;
    }
        
		public PlayerItem getPlayerItem() {
		return playerItem;
	}
	
	public AddPlayerItem setPlayerItem(PlayerItem playerItem) {
		this.playerItem = playerItem;
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

    public Schema<AddPlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public AddPlayerItem newMessage()
    {
        return new AddPlayerItem();
    }

    public Class<AddPlayerItem> typeClass()
    {
        return AddPlayerItem.class;
    }

    public String messageName()
    {
        return AddPlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return AddPlayerItem.class.getName();
    }

    public boolean isInitialized(AddPlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, AddPlayerItem message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.playerItem = input.mergeObject(message.playerItem, PlayerItem.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, AddPlayerItem message) throws IOException
    {
    	    	
    	    	if(message.playerItem == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerItem != null)
    		output.writeObject(1, message.playerItem, PlayerItem.getSchema(), false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerItem";
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
    	    	__fieldMap.put("playerItem", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = AddPlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static AddPlayerItem parseFrom(byte[] bytes) {
	AddPlayerItem message = new AddPlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, AddPlayerItem.getSchema());
	return message;
}

public static AddPlayerItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	AddPlayerItem message = new AddPlayerItem();
	JsonIOUtil.mergeFrom(bytes, message, AddPlayerItem.getSchema(), false);
	return message;
}

public AddPlayerItem clone() {
	byte[] bytes = this.toByteArray();
	AddPlayerItem addPlayerItem = AddPlayerItem.parseFrom(bytes);
	return addPlayerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, AddPlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<AddPlayerItem> schema = AddPlayerItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, AddPlayerItem.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
