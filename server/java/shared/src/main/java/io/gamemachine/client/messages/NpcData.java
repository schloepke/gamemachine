
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
public final class NpcData implements Externalizable, Message<NpcData>, Schema<NpcData>{

	public enum NpcType implements io.protostuff.EnumLite<NpcType>
    {
    	
    	    	None(0),    	    	Guard(1),    	    	Bandit(2),    	    	Animal(3),    	    	Civilian(4);    	        
        public final int number;
        
        private NpcType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static NpcType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (None);
    			    			case 1: return (Guard);
    			    			case 2: return (Bandit);
    			    			case 3: return (Animal);
    			    			case 4: return (Civilian);
    			                default: return null;
            }
        }
    }


    public static Schema<NpcData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static NpcData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final NpcData DEFAULT_INSTANCE = new NpcData();

    			public String id;
	    
        			public GmVector3 spawnpoint;
	    
        			public String leader;
	    
            public List<String> followers;
	    			public String patrolRoute;
	    
        			public Waypoint waypoint;
	    
        			public NpcType npcType; // = None:0;
	    
      
    public NpcData()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public NpcData setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasSpawnpoint()  {
        return spawnpoint == null ? false : true;
    }
        
		public GmVector3 getSpawnpoint() {
		return spawnpoint;
	}
	
	public NpcData setSpawnpoint(GmVector3 spawnpoint) {
		this.spawnpoint = spawnpoint;
		return this;	}
	
		    
    public Boolean hasLeader()  {
        return leader == null ? false : true;
    }
        
		public String getLeader() {
		return leader;
	}
	
	public NpcData setLeader(String leader) {
		this.leader = leader;
		return this;	}
	
		    
    public Boolean hasFollowers()  {
        return followers == null ? false : true;
    }
        
		public List<String> getFollowersList() {
		if(this.followers == null)
            this.followers = new ArrayList<String>();
		return followers;
	}

	public NpcData setFollowersList(List<String> followers) {
		this.followers = followers;
		return this;
	}

	public String getFollowers(int index)  {
        return followers == null ? null : followers.get(index);
    }

    public int getFollowersCount()  {
        return followers == null ? 0 : followers.size();
    }

    public NpcData addFollowers(String followers)  {
        if(this.followers == null)
            this.followers = new ArrayList<String>();
        this.followers.add(followers);
        return this;
    }
        	
    
    
    
		    
    public Boolean hasPatrolRoute()  {
        return patrolRoute == null ? false : true;
    }
        
		public String getPatrolRoute() {
		return patrolRoute;
	}
	
	public NpcData setPatrolRoute(String patrolRoute) {
		this.patrolRoute = patrolRoute;
		return this;	}
	
		    
    public Boolean hasWaypoint()  {
        return waypoint == null ? false : true;
    }
        
		public Waypoint getWaypoint() {
		return waypoint;
	}
	
	public NpcData setWaypoint(Waypoint waypoint) {
		this.waypoint = waypoint;
		return this;	}
	
		    
    public Boolean hasNpcType()  {
        return npcType == null ? false : true;
    }
        
		public NpcType getNpcType() {
		return npcType;
	}
	
	public NpcData setNpcType(NpcType npcType) {
		this.npcType = npcType;
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

    public Schema<NpcData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public NpcData newMessage()
    {
        return new NpcData();
    }

    public Class<NpcData> typeClass()
    {
        return NpcData.class;
    }

    public String messageName()
    {
        return NpcData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return NpcData.class.getName();
    }

    public boolean isInitialized(NpcData message)
    {
        return true;
    }

    public void mergeFrom(Input input, NpcData message) throws IOException
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
            	                	                	message.spawnpoint = input.mergeObject(message.spawnpoint, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.leader = input.readString();
                	break;
                	                	
                            	            	case 4:
            	            		if(message.followers == null)
                        message.followers = new ArrayList<String>();
                                    	message.followers.add(input.readString());
                	                    break;
                            	            	case 5:
            	                	                	message.patrolRoute = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.waypoint = input.mergeObject(message.waypoint, Waypoint.getSchema());
                    break;
                                    	
                            	            	case 7:
            	                	                    message.npcType = NpcType.valueOf(input.readEnum());
                    break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, NpcData message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.spawnpoint != null)
    		output.writeObject(2, message.spawnpoint, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.leader != null)
            output.writeString(3, message.leader, false);
    	    	
    	            	
    	    	
    	    	if(message.followers != null)
        {
            for(String followers : message.followers)
            {
                if(followers != null) {
                   	            		output.writeString(4, followers, true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.patrolRoute != null)
            output.writeString(5, message.patrolRoute, false);
    	    	
    	            	
    	    	
    	    	    	if(message.waypoint != null)
    		output.writeObject(6, message.waypoint, Waypoint.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.npcType != null)
    	 	output.writeEnum(7, message.npcType.number, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "spawnpoint";
        	        	case 3: return "leader";
        	        	case 4: return "followers";
        	        	case 5: return "patrolRoute";
        	        	case 6: return "waypoint";
        	        	case 7: return "npcType";
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
    	    	__fieldMap.put("spawnpoint", 2);
    	    	__fieldMap.put("leader", 3);
    	    	__fieldMap.put("followers", 4);
    	    	__fieldMap.put("patrolRoute", 5);
    	    	__fieldMap.put("waypoint", 6);
    	    	__fieldMap.put("npcType", 7);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = NpcData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static NpcData parseFrom(byte[] bytes) {
	NpcData message = new NpcData();
	ProtobufIOUtil.mergeFrom(bytes, message, NpcData.getSchema());
	return message;
}

public static NpcData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	NpcData message = new NpcData();
	JsonIOUtil.mergeFrom(bytes, message, NpcData.getSchema(), false);
	return message;
}

public NpcData clone() {
	byte[] bytes = this.toByteArray();
	NpcData npcData = NpcData.parseFrom(bytes);
	return npcData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, NpcData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<NpcData> schema = NpcData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, NpcData.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
