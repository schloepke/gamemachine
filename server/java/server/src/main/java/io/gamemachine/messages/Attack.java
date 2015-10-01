
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
public final class Attack implements Externalizable, Message<Attack>, Schema<Attack>{



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

    			public String attacker;
	    
        			public String target;
	    
        			public String skill;
	    
        			public GmVector3 targetLocation;
	    
        


    public Attack()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("attack_attacker",null);
    	    	    	    	    	    	model.set("attack_target",null);
    	    	    	    	    	    	model.set("attack_skill",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (attacker != null) {
    	       	    	model.setString("attack_attacker",attacker);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (target != null) {
    	       	    	model.setString("attack_target",target);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (skill != null) {
    	       	    	model.setString("attack_skill",skill);
    	        		
    	}
    	    	    	    }
    
	public static Attack fromModel(Model model) {
		boolean hasFields = false;
    	Attack message = new Attack();
    	    	    	    	    	
    	    	    	String attackerField = model.getString("attack_attacker");
    	    	
    	if (attackerField != null) {
    		message.setAttacker(attackerField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String targetField = model.getString("attack_target");
    	    	
    	if (targetField != null) {
    		message.setTarget(targetField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String skillField = model.getString("attack_skill");
    	    	
    	if (skillField != null) {
    		message.setSkill(skillField);
    		hasFields = true;
    	}
    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasAttacker()  {
        return attacker == null ? false : true;
    }
        
		public String getAttacker() {
		return attacker;
	}
	
	public Attack setAttacker(String attacker) {
		this.attacker = attacker;
		return this;	}
	
		    
    public Boolean hasTarget()  {
        return target == null ? false : true;
    }
        
		public String getTarget() {
		return target;
	}
	
	public Attack setTarget(String target) {
		this.target = target;
		return this;	}
	
		    
    public Boolean hasSkill()  {
        return skill == null ? false : true;
    }
        
		public String getSkill() {
		return skill;
	}
	
	public Attack setSkill(String skill) {
		this.skill = skill;
		return this;	}
	
		    
    public Boolean hasTargetLocation()  {
        return targetLocation == null ? false : true;
    }
        
		public GmVector3 getTargetLocation() {
		return targetLocation;
	}
	
	public Attack setTargetLocation(GmVector3 targetLocation) {
		this.targetLocation = targetLocation;
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
            	                	                	message.attacker = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.target = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.skill = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.targetLocation = input.mergeObject(message.targetLocation, GmVector3.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Attack message) throws IOException
    {
    	    	
    	    	if(message.attacker == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.attacker != null)
            output.writeString(1, message.attacker, false);
    	    	
    	            	
    	    	if(message.target == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.target != null)
            output.writeString(2, message.target, false);
    	    	
    	            	
    	    	if(message.skill == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.skill != null)
            output.writeString(3, message.skill, false);
    	    	
    	            	
    	    	
    	    	    	if(message.targetLocation != null)
    		output.writeObject(4, message.targetLocation, GmVector3.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Attack");
    	    	if(this.attacker != null) {
    		System.out.println("attacker="+this.attacker);
    	}
    	    	if(this.target != null) {
    		System.out.println("target="+this.target);
    	}
    	    	if(this.skill != null) {
    		System.out.println("skill="+this.skill);
    	}
    	    	if(this.targetLocation != null) {
    		System.out.println("targetLocation="+this.targetLocation);
    	}
    	    	System.out.println("END Attack");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "attacker";
        	        	case 2: return "target";
        	        	case 3: return "skill";
        	        	case 4: return "targetLocation";
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
    	    	__fieldMap.put("attacker", 1);
    	    	__fieldMap.put("target", 2);
    	    	__fieldMap.put("skill", 3);
    	    	__fieldMap.put("targetLocation", 4);
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
