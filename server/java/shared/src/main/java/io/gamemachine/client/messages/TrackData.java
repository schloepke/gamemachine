
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
public final class TrackData implements Externalizable, Message<TrackData>, Schema<TrackData>{

	public enum EntityType implements com.dyuproject.protostuff.EnumLite<EntityType>
    {
    	
    	    	PLAYER(0),    	    	NPC(1),    	    	OTHER(2),    	    	ALL(3);    	        
        public final int number;
        
        private EntityType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static EntityType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (PLAYER);
    			    			case 1: return (NPC);
    			    			case 2: return (OTHER);
    			    			case 3: return (ALL);
    			                default: return null;
            }
        }
    }


    public static Schema<TrackData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackData DEFAULT_INSTANCE = new TrackData();

    			public String id;
	    
        			public Float x;
	    
        			public Float y;
	    
        			public Float z;
	    
        			public DynamicMessage dynamicMessage;
	    
        			public String gridName;
	    
        			public Integer getNeighbors;
	    
        			public EntityType neighborEntityType; // = PLAYER:0;
	    
        			public EntityType entityType; // = PLAYER:0;
	    
      
    public TrackData()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public TrackData setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasX()  {
        return x == null ? false : true;
    }
        
		public Float getX() {
		return x;
	}
	
	public TrackData setX(Float x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Float getY() {
		return y;
	}
	
	public TrackData setY(Float y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasZ()  {
        return z == null ? false : true;
    }
        
		public Float getZ() {
		return z;
	}
	
	public TrackData setZ(Float z) {
		this.z = z;
		return this;	}
	
		    
    public Boolean hasDynamicMessage()  {
        return dynamicMessage == null ? false : true;
    }
        
		public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public TrackData setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
		return this;	}
	
		    
    public Boolean hasGridName()  {
        return gridName == null ? false : true;
    }
        
		public String getGridName() {
		return gridName;
	}
	
	public TrackData setGridName(String gridName) {
		this.gridName = gridName;
		return this;	}
	
		    
    public Boolean hasGetNeighbors()  {
        return getNeighbors == null ? false : true;
    }
        
		public Integer getGetNeighbors() {
		return getNeighbors;
	}
	
	public TrackData setGetNeighbors(Integer getNeighbors) {
		this.getNeighbors = getNeighbors;
		return this;	}
	
		    
    public Boolean hasNeighborEntityType()  {
        return neighborEntityType == null ? false : true;
    }
        
		public EntityType getNeighborEntityType() {
		return neighborEntityType;
	}
	
	public TrackData setNeighborEntityType(EntityType neighborEntityType) {
		this.neighborEntityType = neighborEntityType;
		return this;	}
	
		    
    public Boolean hasEntityType()  {
        return entityType == null ? false : true;
    }
        
		public EntityType getEntityType() {
		return entityType;
	}
	
	public TrackData setEntityType(EntityType entityType) {
		this.entityType = entityType;
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

    public Schema<TrackData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackData newMessage()
    {
        return new TrackData();
    }

    public Class<TrackData> typeClass()
    {
        return TrackData.class;
    }

    public String messageName()
    {
        return TrackData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackData.class.getName();
    }

    public boolean isInitialized(TrackData message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 5:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.x = input.readFloat();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.y = input.readFloat();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.z = input.readFloat();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;
                                    	
                            	            	case 14:
            	                	                	message.gridName = input.readString();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.getNeighbors = input.readInt32();
                	break;
                	                	
                            	            	case 16:
            	                	                    message.neighborEntityType = EntityType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 17:
            	                	                    message.entityType = EntityType.valueOf(input.readEnum());
                    break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackData message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.id != null)
            output.writeString(5, message.id, false);
    	    	
    	            	
    	    	if(message.x == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.x != null)
            output.writeFloat(6, message.x, false);
    	    	
    	            	
    	    	if(message.y == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.y != null)
            output.writeFloat(7, message.y, false);
    	    	
    	            	
    	    	
    	    	    	if(message.z != null)
            output.writeFloat(8, message.z, false);
    	    	
    	            	
    	    	
    	    	    	if(message.dynamicMessage != null)
    		output.writeObject(13, message.dynamicMessage, DynamicMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.gridName != null)
            output.writeString(14, message.gridName, false);
    	    	
    	            	
    	    	if(message.getNeighbors == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.getNeighbors != null)
            output.writeInt32(15, message.getNeighbors, false);
    	    	
    	            	
    	    	
    	    	    	if(message.neighborEntityType != null)
    	 	output.writeEnum(16, message.neighborEntityType.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.entityType != null)
    	 	output.writeEnum(17, message.entityType.number, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 5: return "id";
        	        	case 6: return "x";
        	        	case 7: return "y";
        	        	case 8: return "z";
        	        	case 13: return "dynamicMessage";
        	        	case 14: return "gridName";
        	        	case 15: return "getNeighbors";
        	        	case 16: return "neighborEntityType";
        	        	case 17: return "entityType";
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
    	    	__fieldMap.put("id", 5);
    	    	__fieldMap.put("x", 6);
    	    	__fieldMap.put("y", 7);
    	    	__fieldMap.put("z", 8);
    	    	__fieldMap.put("dynamicMessage", 13);
    	    	__fieldMap.put("gridName", 14);
    	    	__fieldMap.put("getNeighbors", 15);
    	    	__fieldMap.put("neighborEntityType", 16);
    	    	__fieldMap.put("entityType", 17);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackData parseFrom(byte[] bytes) {
	TrackData message = new TrackData();
	ProtobufIOUtil.mergeFrom(bytes, message, TrackData.getSchema());
	return message;
}

public static TrackData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TrackData message = new TrackData();
	JsonIOUtil.mergeFrom(bytes, message, TrackData.getSchema(), false);
	return message;
}

public TrackData clone() {
	byte[] bytes = this.toByteArray();
	TrackData trackData = TrackData.parseFrom(bytes);
	return trackData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TrackData> schema = TrackData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TrackData.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
