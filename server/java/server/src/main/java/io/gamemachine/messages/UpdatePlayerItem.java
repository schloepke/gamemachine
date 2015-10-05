
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
public final class UpdatePlayerItem implements Externalizable, Message<UpdatePlayerItem>, Schema<UpdatePlayerItem>{



    public static Schema<UpdatePlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static UpdatePlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final UpdatePlayerItem DEFAULT_INSTANCE = new UpdatePlayerItem();
    static final String defaultScope = UpdatePlayerItem.class.getSimpleName();

    	
							    public int result= 0;
		    			    
		
    
        	
					public PlayerItem playerItem = null;
			    
		
    
        


    public UpdatePlayerItem()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("update_player_item_result",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (result != null) {
    	       	    	model.setInteger("update_player_item_result",result);
    	        		
    	//}
    	    	    	    }
    
	public static UpdatePlayerItem fromModel(Model model) {
		boolean hasFields = false;
    	UpdatePlayerItem message = new UpdatePlayerItem();
    	    	    	    	    	
    	    	    	Integer resultTestField = model.getInteger("update_player_item_result");
    	if (resultTestField != null) {
    		int resultField = resultTestField;
    		message.setResult(resultField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public int getResult() {
		return result;
	}
	
	public UpdatePlayerItem setResult(int result) {
		this.result = result;
		return this;	}
	
		            
		public PlayerItem getPlayerItem() {
		return playerItem;
	}
	
	public UpdatePlayerItem setPlayerItem(PlayerItem playerItem) {
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

    public Schema<UpdatePlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public UpdatePlayerItem newMessage()
    {
        return new UpdatePlayerItem();
    }

    public Class<UpdatePlayerItem> typeClass()
    {
        return UpdatePlayerItem.class;
    }

    public String messageName()
    {
        return UpdatePlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return UpdatePlayerItem.class.getName();
    }

    public boolean isInitialized(UpdatePlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, UpdatePlayerItem message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.result = input.readInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.playerItem = input.mergeObject(message.playerItem, PlayerItem.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, UpdatePlayerItem message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (Integer)message.result != null) {
            output.writeInt32(1, message.result, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.playerItem != null)
    		output.writeObject(2, message.playerItem, PlayerItem.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START UpdatePlayerItem");
    	    	//if(this.result != null) {
    		System.out.println("result="+this.result);
    	//}
    	    	//if(this.playerItem != null) {
    		System.out.println("playerItem="+this.playerItem);
    	//}
    	    	System.out.println("END UpdatePlayerItem");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "result";
        	        	case 2: return "playerItem";
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
    	    	__fieldMap.put("result", 1);
    	    	__fieldMap.put("playerItem", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = UpdatePlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static UpdatePlayerItem parseFrom(byte[] bytes) {
	UpdatePlayerItem message = new UpdatePlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, UpdatePlayerItem.getSchema());
	return message;
}

public static UpdatePlayerItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	UpdatePlayerItem message = new UpdatePlayerItem();
	JsonIOUtil.mergeFrom(bytes, message, UpdatePlayerItem.getSchema(), false);
	return message;
}

public UpdatePlayerItem clone() {
	byte[] bytes = this.toByteArray();
	UpdatePlayerItem updatePlayerItem = UpdatePlayerItem.parseFrom(bytes);
	return updatePlayerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, UpdatePlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<UpdatePlayerItem> schema = UpdatePlayerItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, UpdatePlayerItem.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, UpdatePlayerItem.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
