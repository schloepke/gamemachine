
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
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class Attack implements Externalizable, Message<Attack>, Schema<Attack>{

private static final Logger logger = LoggerFactory.getLogger(Attack.class);

	public enum TargetType implements io.protostuff.EnumLite<TargetType>
    {
    	
    	    	None(0),    	    	Character(1),    	    	Object(2);    	        
        public final int number;
        
        private TargetType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static TargetType defaultValue() {
        	return (None);
        }
        
        public static TargetType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (None);
    			    			case 1: return (Character);
    			    			case 2: return (Object);
    			                default: return null;
            }
        }
    }


    public static Schema<Attack> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Attack getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Attack DEFAULT_INSTANCE = new Attack();
    static final String defaultScope = Attack.class.getSimpleName();

    	
							    public String attackerCharacterId= null;
		    			    
		
    
        	
							    public String targetId= null;
		    			    
		
    
        	
					public PlayerSkill playerSkill = null;
			    
		
    
        	
					public GmVector3 targetLocation = null;
			    
		
    
        	
					public TargetType targetType = TargetType.defaultValue();
			    
		
    
        


    public Attack()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("attack_attacker_character_id",null);
    	    	    	    	    	    	model.set("attack_target_id",null);
    	    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (attackerCharacterId != null) {
    	       	    	model.setString("attack_attacker_character_id",attackerCharacterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (targetId != null) {
    	       	    	model.setString("attack_target_id",targetId);
    	        		
    	//}
    	    	    	    	    	    	    	
    	    	    	//if (targetType != null) {
    	       	    	model.setInteger("attack_target_type",targetType.number);
    	        		
    	//}
    	    	    }
    
	public static Attack fromModel(Model model) {
		boolean hasFields = false;
    	Attack message = new Attack();
    	    	    	    	    	
    	    			String attackerCharacterIdTestField = model.getString("attack_attacker_character_id");
		if (attackerCharacterIdTestField != null) {
			String attackerCharacterIdField = attackerCharacterIdTestField;
			message.setAttackerCharacterId(attackerCharacterIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String targetIdTestField = model.getString("attack_target_id");
		if (targetIdTestField != null) {
			String targetIdField = targetIdTestField;
			message.setTargetId(targetIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	    	
    				message.setTargetType(TargetType.valueOf(model.getInteger("attack_target_type")));
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public String getAttackerCharacterId() {
		return attackerCharacterId;
	}
	
	public Attack setAttackerCharacterId(String attackerCharacterId) {
		this.attackerCharacterId = attackerCharacterId;
		return this;	}
	
		            
		public String getTargetId() {
		return targetId;
	}
	
	public Attack setTargetId(String targetId) {
		this.targetId = targetId;
		return this;	}
	
		            
		public PlayerSkill getPlayerSkill() {
		return playerSkill;
	}
	
	public Attack setPlayerSkill(PlayerSkill playerSkill) {
		this.playerSkill = playerSkill;
		return this;	}
	
		            
		public GmVector3 getTargetLocation() {
		return targetLocation;
	}
	
	public Attack setTargetLocation(GmVector3 targetLocation) {
		this.targetLocation = targetLocation;
		return this;	}
	
		            
		public TargetType getTargetType() {
		return targetType;
	}
	
	public Attack setTargetType(TargetType targetType) {
		this.targetType = targetType;
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

    public Schema<Attack> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Attack newMessage()
    {
        return new Attack();
    }

    public Class<Attack> typeClass()
    {
        return Attack.class;
    }

    public String messageName()
    {
        return Attack.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Attack.class.getName();
    }

    public boolean isInitialized(Attack message)
    {
        return true;
    }

    public void mergeFrom(Input input, Attack message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.attackerCharacterId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.targetId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.playerSkill = input.mergeObject(message.playerSkill, PlayerSkill.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.targetLocation = input.mergeObject(message.targetLocation, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 5:
            	                	                    message.targetType = TargetType.valueOf(input.readEnum());
                    break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Attack message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.attackerCharacterId != null) {
            output.writeString(1, message.attackerCharacterId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.targetId != null) {
            output.writeString(2, message.targetId, false);
        }
    	    	
    	            	
    	    	//if(message.playerSkill == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerSkill != null)
    		output.writeObject(3, message.playerSkill, PlayerSkill.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.targetLocation != null)
    		output.writeObject(4, message.targetLocation, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.targetType != null)
    	 	output.writeEnum(5, message.targetType.number, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Attack");
    	    	//if(this.attackerCharacterId != null) {
    		System.out.println("attackerCharacterId="+this.attackerCharacterId);
    	//}
    	    	//if(this.targetId != null) {
    		System.out.println("targetId="+this.targetId);
    	//}
    	    	//if(this.playerSkill != null) {
    		System.out.println("playerSkill="+this.playerSkill);
    	//}
    	    	//if(this.targetLocation != null) {
    		System.out.println("targetLocation="+this.targetLocation);
    	//}
    	    	//if(this.targetType != null) {
    		System.out.println("targetType="+this.targetType);
    	//}
    	    	System.out.println("END Attack");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "attackerCharacterId";
        	        	case 2: return "targetId";
        	        	case 3: return "playerSkill";
        	        	case 4: return "targetLocation";
        	        	case 5: return "targetType";
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
    	    	__fieldMap.put("attackerCharacterId", 1);
    	    	__fieldMap.put("targetId", 2);
    	    	__fieldMap.put("playerSkill", 3);
    	    	__fieldMap.put("targetLocation", 4);
    	    	__fieldMap.put("targetType", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Attack.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Attack parseFrom(byte[] bytes) {
	Attack message = new Attack();
	ProtobufIOUtil.mergeFrom(bytes, message, Attack.getSchema());
	return message;
}

public static Attack parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Attack message = new Attack();
	JsonIOUtil.mergeFrom(bytes, message, Attack.getSchema(), false);
	return message;
}

public Attack clone() {
	byte[] bytes = this.toByteArray();
	Attack attack = Attack.parseFrom(bytes);
	return attack;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Attack.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Attack> schema = Attack.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Attack.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Attack.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
