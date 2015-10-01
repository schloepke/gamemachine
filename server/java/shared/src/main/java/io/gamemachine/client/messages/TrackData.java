
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
public final class TrackData implements Externalizable, Message<TrackData>, Schema<TrackData>{

	public enum EntityType implements io.protostuff.EnumLite<EntityType>
    {
    	
    	    	NONE(0),    	    	PLAYER(1),    	    	NPC(2),    	    	OTHER(3),    	    	ALL(4),    	    	SHIP(5);    	        
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
            	    			case 0: return (NONE);
    			    			case 1: return (PLAYER);
    			    			case 2: return (NPC);
    			    			case 3: return (OTHER);
    			    			case 4: return (ALL);
    			    			case 5: return (SHIP);
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

    			public Integer ix;
	    
        			public Integer iy;
	    
        			public Integer iz;
	    
        			public String id;
	    
        			public Integer x;
	    
        			public Integer y;
	    
        			public Integer z;
	    
        			public DynamicMessage dynamicMessage;
	    
        			public String gridName;
	    
        			public Integer getNeighbors;
	    
        			public EntityType neighborEntityType; // = NONE:0;
	    
        			public EntityType entityType; // = NONE:0;
	    
        			public Integer shortId;
	    
        			public UserDefinedData userDefinedData;
	    
        			public Integer broadcast;
	    
        			public String characterId;
	    
        			public Integer rx;
	    
        			public Integer ry;
	    
        			public Integer rz;
	    
        			public Integer rw;
	    
        			public Integer vaxis;
	    
        			public Integer haxis;
	    
        			public Integer speed;
	    
        			public Float velX;
	    
        			public Float velZ;
	    
        			public Integer zone;
	    
        			public Integer hidden;
	    
        			public Integer yaxis;
	    
      
    public TrackData()
    {
        
    }


	

	    
    public Boolean hasIx()  {
        return ix == null ? false : true;
    }
        
		public Integer getIx() {
		return ix;
	}
	
	public TrackData setIx(Integer ix) {
		this.ix = ix;
		return this;	}
	
		    
    public Boolean hasIy()  {
        return iy == null ? false : true;
    }
        
		public Integer getIy() {
		return iy;
	}
	
	public TrackData setIy(Integer iy) {
		this.iy = iy;
		return this;	}
	
		    
    public Boolean hasIz()  {
        return iz == null ? false : true;
    }
        
		public Integer getIz() {
		return iz;
	}
	
	public TrackData setIz(Integer iz) {
		this.iz = iz;
		return this;	}
	
		    
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
        
		public Integer getX() {
		return x;
	}
	
	public TrackData setX(Integer x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Integer getY() {
		return y;
	}
	
	public TrackData setY(Integer y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasZ()  {
        return z == null ? false : true;
    }
        
		public Integer getZ() {
		return z;
	}
	
	public TrackData setZ(Integer z) {
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
	
		    
    public Boolean hasShortId()  {
        return shortId == null ? false : true;
    }
        
		public Integer getShortId() {
		return shortId;
	}
	
	public TrackData setShortId(Integer shortId) {
		this.shortId = shortId;
		return this;	}
	
		    
    public Boolean hasUserDefinedData()  {
        return userDefinedData == null ? false : true;
    }
        
		public UserDefinedData getUserDefinedData() {
		return userDefinedData;
	}
	
	public TrackData setUserDefinedData(UserDefinedData userDefinedData) {
		this.userDefinedData = userDefinedData;
		return this;	}
	
		    
    public Boolean hasBroadcast()  {
        return broadcast == null ? false : true;
    }
        
		public Integer getBroadcast() {
		return broadcast;
	}
	
	public TrackData setBroadcast(Integer broadcast) {
		this.broadcast = broadcast;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public TrackData setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasRx()  {
        return rx == null ? false : true;
    }
        
		public Integer getRx() {
		return rx;
	}
	
	public TrackData setRx(Integer rx) {
		this.rx = rx;
		return this;	}
	
		    
    public Boolean hasRy()  {
        return ry == null ? false : true;
    }
        
		public Integer getRy() {
		return ry;
	}
	
	public TrackData setRy(Integer ry) {
		this.ry = ry;
		return this;	}
	
		    
    public Boolean hasRz()  {
        return rz == null ? false : true;
    }
        
		public Integer getRz() {
		return rz;
	}
	
	public TrackData setRz(Integer rz) {
		this.rz = rz;
		return this;	}
	
		    
    public Boolean hasRw()  {
        return rw == null ? false : true;
    }
        
		public Integer getRw() {
		return rw;
	}
	
	public TrackData setRw(Integer rw) {
		this.rw = rw;
		return this;	}
	
		    
    public Boolean hasVaxis()  {
        return vaxis == null ? false : true;
    }
        
		public Integer getVaxis() {
		return vaxis;
	}
	
	public TrackData setVaxis(Integer vaxis) {
		this.vaxis = vaxis;
		return this;	}
	
		    
    public Boolean hasHaxis()  {
        return haxis == null ? false : true;
    }
        
		public Integer getHaxis() {
		return haxis;
	}
	
	public TrackData setHaxis(Integer haxis) {
		this.haxis = haxis;
		return this;	}
	
		    
    public Boolean hasSpeed()  {
        return speed == null ? false : true;
    }
        
		public Integer getSpeed() {
		return speed;
	}
	
	public TrackData setSpeed(Integer speed) {
		this.speed = speed;
		return this;	}
	
		    
    public Boolean hasVelX()  {
        return velX == null ? false : true;
    }
        
		public Float getVelX() {
		return velX;
	}
	
	public TrackData setVelX(Float velX) {
		this.velX = velX;
		return this;	}
	
		    
    public Boolean hasVelZ()  {
        return velZ == null ? false : true;
    }
        
		public Float getVelZ() {
		return velZ;
	}
	
	public TrackData setVelZ(Float velZ) {
		this.velZ = velZ;
		return this;	}
	
		    
    public Boolean hasZone()  {
        return zone == null ? false : true;
    }
        
		public Integer getZone() {
		return zone;
	}
	
	public TrackData setZone(Integer zone) {
		this.zone = zone;
		return this;	}
	
		    
    public Boolean hasHidden()  {
        return hidden == null ? false : true;
    }
        
		public Integer getHidden() {
		return hidden;
	}
	
	public TrackData setHidden(Integer hidden) {
		this.hidden = hidden;
		return this;	}
	
		    
    public Boolean hasYaxis()  {
        return yaxis == null ? false : true;
    }
        
		public Integer getYaxis() {
		return yaxis;
	}
	
	public TrackData setYaxis(Integer yaxis) {
		this.yaxis = yaxis;
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
                            	case 1:
            	                	                	message.ix = input.readSInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.iy = input.readSInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.iz = input.readSInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.x = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.y = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.z = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;
                                    	
                            	            	case 9:
            	                	                	message.gridName = input.readString();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.getNeighbors = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                    message.neighborEntityType = EntityType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 12:
            	                	                    message.entityType = EntityType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 13:
            	                	                	message.shortId = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.userDefinedData = input.mergeObject(message.userDefinedData, UserDefinedData.getSchema());
                    break;
                                    	
                            	            	case 15:
            	                	                	message.broadcast = input.readInt32();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.rx = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.ry = input.readInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.rz = input.readInt32();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.rw = input.readInt32();
                	break;
                	                	
                            	            	case 21:
            	                	                	message.vaxis = input.readInt32();
                	break;
                	                	
                            	            	case 22:
            	                	                	message.haxis = input.readInt32();
                	break;
                	                	
                            	            	case 23:
            	                	                	message.speed = input.readInt32();
                	break;
                	                	
                            	            	case 24:
            	                	                	message.velX = input.readFloat();
                	break;
                	                	
                            	            	case 25:
            	                	                	message.velZ = input.readFloat();
                	break;
                	                	
                            	            	case 26:
            	                	                	message.zone = input.readInt32();
                	break;
                	                	
                            	            	case 27:
            	                	                	message.hidden = input.readInt32();
                	break;
                	                	
                            	            	case 28:
            	                	                	message.yaxis = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackData message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.ix != null)
            output.writeSInt32(1, message.ix, false);
    	    	
    	            	
    	    	
    	    	    	if(message.iy != null)
            output.writeSInt32(2, message.iy, false);
    	    	
    	            	
    	    	
    	    	    	if(message.iz != null)
            output.writeSInt32(3, message.iz, false);
    	    	
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(4, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.x != null)
            output.writeInt32(5, message.x, false);
    	    	
    	            	
    	    	
    	    	    	if(message.y != null)
            output.writeInt32(6, message.y, false);
    	    	
    	            	
    	    	
    	    	    	if(message.z != null)
            output.writeInt32(7, message.z, false);
    	    	
    	            	
    	    	
    	    	    	if(message.dynamicMessage != null)
    		output.writeObject(8, message.dynamicMessage, DynamicMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.gridName != null)
            output.writeString(9, message.gridName, false);
    	    	
    	            	
    	    	
    	    	    	if(message.getNeighbors != null)
            output.writeInt32(10, message.getNeighbors, false);
    	    	
    	            	
    	    	
    	    	    	if(message.neighborEntityType != null)
    	 	output.writeEnum(11, message.neighborEntityType.number, false);
    	    	
    	            	
    	    	if(message.entityType == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.entityType != null)
    	 	output.writeEnum(12, message.entityType.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.shortId != null)
            output.writeInt32(13, message.shortId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userDefinedData != null)
    		output.writeObject(14, message.userDefinedData, UserDefinedData.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.broadcast != null)
            output.writeInt32(15, message.broadcast, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(16, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rx != null)
            output.writeInt32(17, message.rx, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ry != null)
            output.writeInt32(18, message.ry, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rz != null)
            output.writeInt32(19, message.rz, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rw != null)
            output.writeInt32(20, message.rw, false);
    	    	
    	            	
    	    	
    	    	    	if(message.vaxis != null)
            output.writeInt32(21, message.vaxis, false);
    	    	
    	            	
    	    	
    	    	    	if(message.haxis != null)
            output.writeInt32(22, message.haxis, false);
    	    	
    	            	
    	    	
    	    	    	if(message.speed != null)
            output.writeInt32(23, message.speed, false);
    	    	
    	            	
    	    	
    	    	    	if(message.velX != null)
            output.writeFloat(24, message.velX, false);
    	    	
    	            	
    	    	
    	    	    	if(message.velZ != null)
            output.writeFloat(25, message.velZ, false);
    	    	
    	            	
    	    	
    	    	    	if(message.zone != null)
            output.writeInt32(26, message.zone, false);
    	    	
    	            	
    	    	
    	    	    	if(message.hidden != null)
            output.writeInt32(27, message.hidden, false);
    	    	
    	            	
    	    	
    	    	    	if(message.yaxis != null)
            output.writeInt32(28, message.yaxis, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "ix";
        	        	case 2: return "iy";
        	        	case 3: return "iz";
        	        	case 4: return "id";
        	        	case 5: return "x";
        	        	case 6: return "y";
        	        	case 7: return "z";
        	        	case 8: return "dynamicMessage";
        	        	case 9: return "gridName";
        	        	case 10: return "getNeighbors";
        	        	case 11: return "neighborEntityType";
        	        	case 12: return "entityType";
        	        	case 13: return "shortId";
        	        	case 14: return "userDefinedData";
        	        	case 15: return "broadcast";
        	        	case 16: return "characterId";
        	        	case 17: return "rx";
        	        	case 18: return "ry";
        	        	case 19: return "rz";
        	        	case 20: return "rw";
        	        	case 21: return "vaxis";
        	        	case 22: return "haxis";
        	        	case 23: return "speed";
        	        	case 24: return "velX";
        	        	case 25: return "velZ";
        	        	case 26: return "zone";
        	        	case 27: return "hidden";
        	        	case 28: return "yaxis";
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
    	    	__fieldMap.put("ix", 1);
    	    	__fieldMap.put("iy", 2);
    	    	__fieldMap.put("iz", 3);
    	    	__fieldMap.put("id", 4);
    	    	__fieldMap.put("x", 5);
    	    	__fieldMap.put("y", 6);
    	    	__fieldMap.put("z", 7);
    	    	__fieldMap.put("dynamicMessage", 8);
    	    	__fieldMap.put("gridName", 9);
    	    	__fieldMap.put("getNeighbors", 10);
    	    	__fieldMap.put("neighborEntityType", 11);
    	    	__fieldMap.put("entityType", 12);
    	    	__fieldMap.put("shortId", 13);
    	    	__fieldMap.put("userDefinedData", 14);
    	    	__fieldMap.put("broadcast", 15);
    	    	__fieldMap.put("characterId", 16);
    	    	__fieldMap.put("rx", 17);
    	    	__fieldMap.put("ry", 18);
    	    	__fieldMap.put("rz", 19);
    	    	__fieldMap.put("rw", 20);
    	    	__fieldMap.put("vaxis", 21);
    	    	__fieldMap.put("haxis", 22);
    	    	__fieldMap.put("speed", 23);
    	    	__fieldMap.put("velX", 24);
    	    	__fieldMap.put("velZ", 25);
    	    	__fieldMap.put("zone", 26);
    	    	__fieldMap.put("hidden", 27);
    	    	__fieldMap.put("yaxis", 28);
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
