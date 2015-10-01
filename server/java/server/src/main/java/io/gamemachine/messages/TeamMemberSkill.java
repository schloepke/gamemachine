
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
public final class TeamMemberSkill implements Externalizable, Message<TeamMemberSkill>, Schema<TeamMemberSkill>{



    public static Schema<TeamMemberSkill> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TeamMemberSkill getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TeamMemberSkill DEFAULT_INSTANCE = new TeamMemberSkill();
    static final String defaultScope = TeamMemberSkill.class.getSimpleName();

    			public String name;
	    
        			public Integer value;
	    
        			public String playerId;
	    
        


    public TeamMemberSkill()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("team_member_skill_name",null);
    	    	    	    	    	    	model.set("team_member_skill_value",null);
    	    	    	    	    	    	model.set("team_member_skill_player_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (name != null) {
    	       	    	model.setString("team_member_skill_name",name);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (value != null) {
    	       	    	model.setInteger("team_member_skill_value",value);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (playerId != null) {
    	       	    	model.setString("team_member_skill_player_id",playerId);
    	        		
    	}
    	    	    }
    
	public static TeamMemberSkill fromModel(Model model) {
		boolean hasFields = false;
    	TeamMemberSkill message = new TeamMemberSkill();
    	    	    	    	    	
    	    	    	String nameField = model.getString("team_member_skill_name");
    	    	
    	if (nameField != null) {
    		message.setName(nameField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer valueField = model.getInteger("team_member_skill_value");
    	    	
    	if (valueField != null) {
    		message.setValue(valueField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String playerIdField = model.getString("team_member_skill_player_id");
    	    	
    	if (playerIdField != null) {
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public TeamMemberSkill setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasValue()  {
        return value == null ? false : true;
    }
        
		public Integer getValue() {
		return value;
	}
	
	public TeamMemberSkill setValue(Integer value) {
		this.value = value;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public TeamMemberSkill setPlayerId(String playerId) {
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

    public Schema<TeamMemberSkill> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TeamMemberSkill newMessage()
    {
        return new TeamMemberSkill();
    }

    public Class<TeamMemberSkill> typeClass()
    {
        return TeamMemberSkill.class;
    }

    public String messageName()
    {
        return TeamMemberSkill.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TeamMemberSkill.class.getName();
    }

    public boolean isInitialized(TeamMemberSkill message)
    {
        return true;
    }

    public void mergeFrom(Input input, TeamMemberSkill message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.value = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TeamMemberSkill message) throws IOException
    {
    	    	
    	    	if(message.name == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.name != null)
            output.writeString(1, message.name, false);
    	    	
    	            	
    	    	if(message.value == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.value != null)
            output.writeInt32(2, message.value, false);
    	    	
    	            	
    	    	if(message.playerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(3, message.playerId, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START TeamMemberSkill");
    	    	if(this.name != null) {
    		System.out.println("name="+this.name);
    	}
    	    	if(this.value != null) {
    		System.out.println("value="+this.value);
    	}
    	    	if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	}
    	    	System.out.println("END TeamMemberSkill");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "name";
        	        	case 2: return "value";
        	        	case 3: return "playerId";
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
    	    	__fieldMap.put("name", 1);
    	    	__fieldMap.put("value", 2);
    	    	__fieldMap.put("playerId", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TeamMemberSkill.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TeamMemberSkill parseFrom(byte[] bytes) {
	TeamMemberSkill message = new TeamMemberSkill();
	ProtobufIOUtil.mergeFrom(bytes, message, TeamMemberSkill.getSchema());
	return message;
}

public static TeamMemberSkill parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TeamMemberSkill message = new TeamMemberSkill();
	JsonIOUtil.mergeFrom(bytes, message, TeamMemberSkill.getSchema(), false);
	return message;
}

public TeamMemberSkill clone() {
	byte[] bytes = this.toByteArray();
	TeamMemberSkill teamMemberSkill = TeamMemberSkill.parseFrom(bytes);
	return teamMemberSkill;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TeamMemberSkill.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TeamMemberSkill> schema = TeamMemberSkill.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TeamMemberSkill.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, TeamMemberSkill.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
