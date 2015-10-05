
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
public final class SlotInfo implements Externalizable, Message<SlotInfo>, Schema<SlotInfo>{



    public static Schema<SlotInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static SlotInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final SlotInfo DEFAULT_INSTANCE = new SlotInfo();
    static final String defaultScope = SlotInfo.class.getSimpleName();

    	
	    	    public String placedBlockId= null;
	    		
    
        	
	    	    public String slotId= null;
	    		
    
        


    public SlotInfo()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("slot_info_placed_block_id",null);
    	    	    	    	    	    	model.set("slot_info_slot_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (placedBlockId != null) {
    	       	    	model.setString("slot_info_placed_block_id",placedBlockId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (slotId != null) {
    	       	    	model.setString("slot_info_slot_id",slotId);
    	        		
    	//}
    	    	    }
    
	public static SlotInfo fromModel(Model model) {
		boolean hasFields = false;
    	SlotInfo message = new SlotInfo();
    	    	    	    	    	
    	    	    	String placedBlockIdTestField = model.getString("slot_info_placed_block_id");
    	if (placedBlockIdTestField != null) {
    		String placedBlockIdField = placedBlockIdTestField;
    		message.setPlacedBlockId(placedBlockIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String slotIdTestField = model.getString("slot_info_slot_id");
    	if (slotIdTestField != null) {
    		String slotIdField = slotIdTestField;
    		message.setSlotId(slotIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getPlacedBlockId() {
		return placedBlockId;
	}
	
	public SlotInfo setPlacedBlockId(String placedBlockId) {
		this.placedBlockId = placedBlockId;
		return this;	}
	
		            
		public String getSlotId() {
		return slotId;
	}
	
	public SlotInfo setSlotId(String slotId) {
		this.slotId = slotId;
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

    public Schema<SlotInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public SlotInfo newMessage()
    {
        return new SlotInfo();
    }

    public Class<SlotInfo> typeClass()
    {
        return SlotInfo.class;
    }

    public String messageName()
    {
        return SlotInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return SlotInfo.class.getName();
    }

    public boolean isInitialized(SlotInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, SlotInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.placedBlockId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.slotId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, SlotInfo message) throws IOException
    {
    	    	
    	    	//if(message.placedBlockId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.placedBlockId != null) {
            output.writeString(1, message.placedBlockId, false);
        }
    	    	
    	            	
    	    	//if(message.slotId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.slotId != null) {
            output.writeString(2, message.slotId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START SlotInfo");
    	    	//if(this.placedBlockId != null) {
    		System.out.println("placedBlockId="+this.placedBlockId);
    	//}
    	    	//if(this.slotId != null) {
    		System.out.println("slotId="+this.slotId);
    	//}
    	    	System.out.println("END SlotInfo");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "placedBlockId";
        	        	case 2: return "slotId";
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
    	    	__fieldMap.put("placedBlockId", 1);
    	    	__fieldMap.put("slotId", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = SlotInfo.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static SlotInfo parseFrom(byte[] bytes) {
	SlotInfo message = new SlotInfo();
	ProtobufIOUtil.mergeFrom(bytes, message, SlotInfo.getSchema());
	return message;
}

public static SlotInfo parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	SlotInfo message = new SlotInfo();
	JsonIOUtil.mergeFrom(bytes, message, SlotInfo.getSchema(), false);
	return message;
}

public SlotInfo clone() {
	byte[] bytes = this.toByteArray();
	SlotInfo slotInfo = SlotInfo.parseFrom(bytes);
	return slotInfo;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, SlotInfo.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<SlotInfo> schema = SlotInfo.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, SlotInfo.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, SlotInfo.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
