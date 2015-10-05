
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
public final class SiegeCommand implements Externalizable, Message<SiegeCommand>, Schema<SiegeCommand>{



    public static Schema<SiegeCommand> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static SiegeCommand getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final SiegeCommand DEFAULT_INSTANCE = new SiegeCommand();

    			public int startLoad;
	    
        			public int fire;
	    
        			public float force;
	    
        			public String id;
	    
        			public String hitId;
	    
        			public GmVector3 hit;
	    
        			public String skillId;
	    
        			public int startUse;
	    
        			public int endUse;
	    
        			public String playerId;
	    
        			public String targetType;
	    
        			public String targetId;
	    
      
    public SiegeCommand()
    {
        
    }


	

	    
    public Boolean hasStartLoad()  {
        return startLoad == null ? false : true;
    }
        
		public int getStartLoad() {
		return startLoad;
	}
	
	public SiegeCommand setStartLoad(int startLoad) {
		this.startLoad = startLoad;
		return this;	}
	
		    
    public Boolean hasFire()  {
        return fire == null ? false : true;
    }
        
		public int getFire() {
		return fire;
	}
	
	public SiegeCommand setFire(int fire) {
		this.fire = fire;
		return this;	}
	
		    
    public Boolean hasForce()  {
        return force == null ? false : true;
    }
        
		public float getForce() {
		return force;
	}
	
	public SiegeCommand setForce(float force) {
		this.force = force;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public SiegeCommand setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasHitId()  {
        return hitId == null ? false : true;
    }
        
		public String getHitId() {
		return hitId;
	}
	
	public SiegeCommand setHitId(String hitId) {
		this.hitId = hitId;
		return this;	}
	
		    
    public Boolean hasHit()  {
        return hit == null ? false : true;
    }
        
		public GmVector3 getHit() {
		return hit;
	}
	
	public SiegeCommand setHit(GmVector3 hit) {
		this.hit = hit;
		return this;	}
	
		    
    public Boolean hasSkillId()  {
        return skillId == null ? false : true;
    }
        
		public String getSkillId() {
		return skillId;
	}
	
	public SiegeCommand setSkillId(String skillId) {
		this.skillId = skillId;
		return this;	}
	
		    
    public Boolean hasStartUse()  {
        return startUse == null ? false : true;
    }
        
		public int getStartUse() {
		return startUse;
	}
	
	public SiegeCommand setStartUse(int startUse) {
		this.startUse = startUse;
		return this;	}
	
		    
    public Boolean hasEndUse()  {
        return endUse == null ? false : true;
    }
        
		public int getEndUse() {
		return endUse;
	}
	
	public SiegeCommand setEndUse(int endUse) {
		this.endUse = endUse;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public SiegeCommand setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		    
    public Boolean hasTargetType()  {
        return targetType == null ? false : true;
    }
        
		public String getTargetType() {
		return targetType;
	}
	
	public SiegeCommand setTargetType(String targetType) {
		this.targetType = targetType;
		return this;	}
	
		    
    public Boolean hasTargetId()  {
        return targetId == null ? false : true;
    }
        
		public String getTargetId() {
		return targetId;
	}
	
	public SiegeCommand setTargetId(String targetId) {
		this.targetId = targetId;
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

    public Schema<SiegeCommand> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public SiegeCommand newMessage()
    {
        return new SiegeCommand();
    }

    public Class<SiegeCommand> typeClass()
    {
        return SiegeCommand.class;
    }

    public String messageName()
    {
        return SiegeCommand.class.getSimpleName();
    }

    public String messageFullName()
    {
        return SiegeCommand.class.getName();
    }

    public boolean isInitialized(SiegeCommand message)
    {
        return true;
    }

    public void mergeFrom(Input input, SiegeCommand message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.startLoad = input.readInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.fire = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.force = input.readFloat();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.hitId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.hit = input.mergeObject(message.hit, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 7:
            	                	                	message.skillId = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.startUse = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.endUse = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.targetType = input.readString();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.targetId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, SiegeCommand message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.startLoad != null)
            output.writeInt32(1, message.startLoad, false);
    	    	
    	            	
    	    	
    	    	    	if(message.fire != null)
            output.writeInt32(2, message.fire, false);
    	    	
    	            	
    	    	
    	    	    	if(message.force != null)
            output.writeFloat(3, message.force, false);
    	    	
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(4, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.hitId != null)
            output.writeString(5, message.hitId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.hit != null)
    		output.writeObject(6, message.hit, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.skillId != null)
            output.writeString(7, message.skillId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.startUse != null)
            output.writeInt32(8, message.startUse, false);
    	    	
    	            	
    	    	
    	    	    	if(message.endUse != null)
            output.writeInt32(9, message.endUse, false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(10, message.playerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.targetType != null)
            output.writeString(11, message.targetType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.targetId != null)
            output.writeString(12, message.targetId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "startLoad";
        	        	case 2: return "fire";
        	        	case 3: return "force";
        	        	case 4: return "id";
        	        	case 5: return "hitId";
        	        	case 6: return "hit";
        	        	case 7: return "skillId";
        	        	case 8: return "startUse";
        	        	case 9: return "endUse";
        	        	case 10: return "playerId";
        	        	case 11: return "targetType";
        	        	case 12: return "targetId";
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
    	    	__fieldMap.put("startLoad", 1);
    	    	__fieldMap.put("fire", 2);
    	    	__fieldMap.put("force", 3);
    	    	__fieldMap.put("id", 4);
    	    	__fieldMap.put("hitId", 5);
    	    	__fieldMap.put("hit", 6);
    	    	__fieldMap.put("skillId", 7);
    	    	__fieldMap.put("startUse", 8);
    	    	__fieldMap.put("endUse", 9);
    	    	__fieldMap.put("playerId", 10);
    	    	__fieldMap.put("targetType", 11);
    	    	__fieldMap.put("targetId", 12);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = SiegeCommand.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static SiegeCommand parseFrom(byte[] bytes) {
	SiegeCommand message = new SiegeCommand();
	ProtobufIOUtil.mergeFrom(bytes, message, SiegeCommand.getSchema());
	return message;
}

public static SiegeCommand parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	SiegeCommand message = new SiegeCommand();
	JsonIOUtil.mergeFrom(bytes, message, SiegeCommand.getSchema(), false);
	return message;
}

public SiegeCommand clone() {
	byte[] bytes = this.toByteArray();
	SiegeCommand siegeCommand = SiegeCommand.parseFrom(bytes);
	return siegeCommand;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, SiegeCommand.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<SiegeCommand> schema = SiegeCommand.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, SiegeCommand.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
