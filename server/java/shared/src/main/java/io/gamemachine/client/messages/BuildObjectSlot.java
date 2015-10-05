
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
public final class BuildObjectSlot implements Externalizable, Message<BuildObjectSlot>, Schema<BuildObjectSlot>{



    public static Schema<BuildObjectSlot> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildObjectSlot getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildObjectSlot DEFAULT_INSTANCE = new BuildObjectSlot();

    			public String slotId;
	    
        			public String buildObjectId;
	    
        			public long placedAt;
	    
        			public boolean buildOverTime;
	    
        			public int buildTime;
	    
      
    public BuildObjectSlot()
    {
        
    }


	

	    
    public Boolean hasSlotId()  {
        return slotId == null ? false : true;
    }
        
		public String getSlotId() {
		return slotId;
	}
	
	public BuildObjectSlot setSlotId(String slotId) {
		this.slotId = slotId;
		return this;	}
	
		    
    public Boolean hasBuildObjectId()  {
        return buildObjectId == null ? false : true;
    }
        
		public String getBuildObjectId() {
		return buildObjectId;
	}
	
	public BuildObjectSlot setBuildObjectId(String buildObjectId) {
		this.buildObjectId = buildObjectId;
		return this;	}
	
		    
    public Boolean hasPlacedAt()  {
        return placedAt == null ? false : true;
    }
        
		public long getPlacedAt() {
		return placedAt;
	}
	
	public BuildObjectSlot setPlacedAt(long placedAt) {
		this.placedAt = placedAt;
		return this;	}
	
		    
    public Boolean hasBuildOverTime()  {
        return buildOverTime == null ? false : true;
    }
        
		public boolean getBuildOverTime() {
		return buildOverTime;
	}
	
	public BuildObjectSlot setBuildOverTime(boolean buildOverTime) {
		this.buildOverTime = buildOverTime;
		return this;	}
	
		    
    public Boolean hasBuildTime()  {
        return buildTime == null ? false : true;
    }
        
		public int getBuildTime() {
		return buildTime;
	}
	
	public BuildObjectSlot setBuildTime(int buildTime) {
		this.buildTime = buildTime;
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

    public Schema<BuildObjectSlot> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildObjectSlot newMessage()
    {
        return new BuildObjectSlot();
    }

    public Class<BuildObjectSlot> typeClass()
    {
        return BuildObjectSlot.class;
    }

    public String messageName()
    {
        return BuildObjectSlot.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildObjectSlot.class.getName();
    }

    public boolean isInitialized(BuildObjectSlot message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildObjectSlot message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.slotId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.buildObjectId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.placedAt = input.readInt64();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.buildOverTime = input.readBool();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.buildTime = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildObjectSlot message) throws IOException
    {
    	    	
    	    	if(message.slotId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.slotId != null)
            output.writeString(1, message.slotId, false);
    	    	
    	            	
    	    	if(message.buildObjectId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.buildObjectId != null)
            output.writeString(2, message.buildObjectId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.placedAt != null)
            output.writeInt64(3, message.placedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.buildOverTime != null)
            output.writeBool(4, message.buildOverTime, false);
    	    	
    	            	
    	    	
    	    	    	if(message.buildTime != null)
            output.writeInt32(5, message.buildTime, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "slotId";
        	        	case 2: return "buildObjectId";
        	        	case 3: return "placedAt";
        	        	case 4: return "buildOverTime";
        	        	case 5: return "buildTime";
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
    	    	__fieldMap.put("slotId", 1);
    	    	__fieldMap.put("buildObjectId", 2);
    	    	__fieldMap.put("placedAt", 3);
    	    	__fieldMap.put("buildOverTime", 4);
    	    	__fieldMap.put("buildTime", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildObjectSlot.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildObjectSlot parseFrom(byte[] bytes) {
	BuildObjectSlot message = new BuildObjectSlot();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildObjectSlot.getSchema());
	return message;
}

public static BuildObjectSlot parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildObjectSlot message = new BuildObjectSlot();
	JsonIOUtil.mergeFrom(bytes, message, BuildObjectSlot.getSchema(), false);
	return message;
}

public BuildObjectSlot clone() {
	byte[] bytes = this.toByteArray();
	BuildObjectSlot buildObjectSlot = BuildObjectSlot.parseFrom(bytes);
	return buildObjectSlot;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildObjectSlot.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildObjectSlot> schema = BuildObjectSlot.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildObjectSlot.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
