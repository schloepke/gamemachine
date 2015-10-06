
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
public final class Character implements Externalizable, Message<Character>, Schema<Character>{



    public static Schema<Character> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Character getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Character DEFAULT_INSTANCE = new Character();

    			public String id;
	    
        			public String umaData;
	    
        			public int recordId;
	    
        			public String playerId;
	    
        			public int part;
	    
        			public int parts;
	    
        			public int worldx;
	    
        			public int worldy;
	    
        			public int worldz;
	    
        			public int zone;
	    
        			public boolean includeUmaData;
	    
        			public int vitalsType;
	    
      
    public Character()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Character setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasUmaData()  {
        return umaData == null ? false : true;
    }
        
		public String getUmaData() {
		return umaData;
	}
	
	public Character setUmaData(String umaData) {
		this.umaData = umaData;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public int getRecordId() {
		return recordId;
	}
	
	public Character setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public Character setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		    
    public Boolean hasPart()  {
        return part == null ? false : true;
    }
        
		public int getPart() {
		return part;
	}
	
	public Character setPart(int part) {
		this.part = part;
		return this;	}
	
		    
    public Boolean hasParts()  {
        return parts == null ? false : true;
    }
        
		public int getParts() {
		return parts;
	}
	
	public Character setParts(int parts) {
		this.parts = parts;
		return this;	}
	
		    
    public Boolean hasWorldx()  {
        return worldx == null ? false : true;
    }
        
		public int getWorldx() {
		return worldx;
	}
	
	public Character setWorldx(int worldx) {
		this.worldx = worldx;
		return this;	}
	
		    
    public Boolean hasWorldy()  {
        return worldy == null ? false : true;
    }
        
		public int getWorldy() {
		return worldy;
	}
	
	public Character setWorldy(int worldy) {
		this.worldy = worldy;
		return this;	}
	
		    
    public Boolean hasWorldz()  {
        return worldz == null ? false : true;
    }
        
		public int getWorldz() {
		return worldz;
	}
	
	public Character setWorldz(int worldz) {
		this.worldz = worldz;
		return this;	}
	
		    
    public Boolean hasZone()  {
        return zone == null ? false : true;
    }
        
		public int getZone() {
		return zone;
	}
	
	public Character setZone(int zone) {
		this.zone = zone;
		return this;	}
	
		    
    public Boolean hasIncludeUmaData()  {
        return includeUmaData == null ? false : true;
    }
        
		public boolean getIncludeUmaData() {
		return includeUmaData;
	}
	
	public Character setIncludeUmaData(boolean includeUmaData) {
		this.includeUmaData = includeUmaData;
		return this;	}
	
		    
    public Boolean hasVitalsType()  {
        return vitalsType == null ? false : true;
    }
        
		public int getVitalsType() {
		return vitalsType;
	}
	
	public Character setVitalsType(int vitalsType) {
		this.vitalsType = vitalsType;
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

    public Schema<Character> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Character newMessage()
    {
        return new Character();
    }

    public Class<Character> typeClass()
    {
        return Character.class;
    }

    public String messageName()
    {
        return Character.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Character.class.getName();
    }

    public boolean isInitialized(Character message)
    {
        return true;
    }

    public void mergeFrom(Input input, Character message) throws IOException
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
            	                	                	message.umaData = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.part = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.parts = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.worldx = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.worldy = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.worldz = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.zone = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.includeUmaData = input.readBool();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.vitalsType = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Character message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.umaData != null)
            output.writeString(2, message.umaData, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(4, message.recordId, false);
    	    	
    	            	
    	    	if(message.playerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(5, message.playerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.part != null)
            output.writeInt32(6, message.part, false);
    	    	
    	            	
    	    	
    	    	    	if(message.parts != null)
            output.writeInt32(7, message.parts, false);
    	    	
    	            	
    	    	
    	    	    	if(message.worldx != null)
            output.writeInt32(8, message.worldx, false);
    	    	
    	            	
    	    	
    	    	    	if(message.worldy != null)
            output.writeInt32(9, message.worldy, false);
    	    	
    	            	
    	    	
    	    	    	if(message.worldz != null)
            output.writeInt32(10, message.worldz, false);
    	    	
    	            	
    	    	
    	    	    	if(message.zone != null)
            output.writeInt32(11, message.zone, false);
    	    	
    	            	
    	    	
    	    	    	if(message.includeUmaData != null)
            output.writeBool(14, message.includeUmaData, false);
    	    	
    	            	
    	    	
    	    	    	if(message.vitalsType != null)
            output.writeInt32(15, message.vitalsType, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "umaData";
        	        	case 4: return "recordId";
        	        	case 5: return "playerId";
        	        	case 6: return "part";
        	        	case 7: return "parts";
        	        	case 8: return "worldx";
        	        	case 9: return "worldy";
        	        	case 10: return "worldz";
        	        	case 11: return "zone";
        	        	case 14: return "includeUmaData";
        	        	case 15: return "vitalsType";
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
    	    	__fieldMap.put("umaData", 2);
    	    	__fieldMap.put("recordId", 4);
    	    	__fieldMap.put("playerId", 5);
    	    	__fieldMap.put("part", 6);
    	    	__fieldMap.put("parts", 7);
    	    	__fieldMap.put("worldx", 8);
    	    	__fieldMap.put("worldy", 9);
    	    	__fieldMap.put("worldz", 10);
    	    	__fieldMap.put("zone", 11);
    	    	__fieldMap.put("includeUmaData", 14);
    	    	__fieldMap.put("vitalsType", 15);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Character.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Character parseFrom(byte[] bytes) {
	Character message = new Character();
	ProtobufIOUtil.mergeFrom(bytes, message, Character.getSchema());
	return message;
}

public static Character parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Character message = new Character();
	JsonIOUtil.mergeFrom(bytes, message, Character.getSchema(), false);
	return message;
}

public Character clone() {
	byte[] bytes = this.toByteArray();
	Character character = Character.parseFrom(bytes);
	return character;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Character.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Character> schema = Character.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Character.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
