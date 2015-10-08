
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
public final class StatusEffectTarget implements Externalizable, Message<StatusEffectTarget>, Schema<StatusEffectTarget>{

private static final Logger logger = LoggerFactory.getLogger(StatusEffectTarget.class);

	public enum Action implements io.protostuff.EnumLite<Action>
    {
    	
    	    	None(0),    	    	Apply(1),    	    	Remove(2);    	        
        public final int number;
        
        private Action (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Action defaultValue() {
        	return (None);
        }
        
        public static Action valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (None);
    			    			case 1: return (Apply);
    			    			case 2: return (Remove);
    			                default: return null;
            }
        }
    }
	public enum PassiveFlag implements io.protostuff.EnumLite<PassiveFlag>
    {
    	
    	    	NA(0),    	    	AutoRemove(1),    	    	ManualRemove(2);    	        
        public final int number;
        
        private PassiveFlag (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static PassiveFlag defaultValue() {
        	return (NA);
        }
        
        public static PassiveFlag valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NA);
    			    			case 1: return (AutoRemove);
    			    			case 2: return (ManualRemove);
    			                default: return null;
            }
        }
    }


    public static Schema<StatusEffectTarget> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static StatusEffectTarget getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final StatusEffectTarget DEFAULT_INSTANCE = new StatusEffectTarget();
    static final String defaultScope = StatusEffectTarget.class.getSimpleName();

    	
							    public String targetEntityId= null;
		    			    
		
    
        	
					public GmVector3 location = null;
			    
		
    
            public List<StatusEffect> statusEffect;
	    	
							    public String originCharacterId= null;
		    			    
		
    
        	
							    public long activeId= 0L;
		    			    
		
    
        	
							    public long lastTick= 0L;
		    			    
		
    
        	
					public Action action = Action.defaultValue();
			    
		
    
        	
					public PassiveFlag passiveFlag = PassiveFlag.defaultValue();
			    
		
    
        	
							    public String originEntityId= null;
		    			    
		
    
        	
					public Attack attack = null;
			    
		
    
        


    public StatusEffectTarget()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("status_effect_target_target_entity_id",null);
    	    	    	    	    	    	    	    	model.set("status_effect_target_origin_character_id",null);
    	    	    	    	    	    	model.set("status_effect_target_active_id",null);
    	    	    	    	    	    	model.set("status_effect_target_last_tick",null);
    	    	    	    	    	    	    	    	model.set("status_effect_target_origin_entity_id",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (targetEntityId != null) {
    	       	    	model.setString("status_effect_target_target_entity_id",targetEntityId);
    	        		
    	//}
    	    	    	    	    	    	    	
    	    	    	//if (originCharacterId != null) {
    	       	    	model.setString("status_effect_target_origin_character_id",originCharacterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (activeId != null) {
    	       	    	model.setLong("status_effect_target_active_id",activeId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (lastTick != null) {
    	       	    	model.setLong("status_effect_target_last_tick",lastTick);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (action != null) {
    	       	    	model.setInteger("status_effect_target_action",action.number);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (passiveFlag != null) {
    	       	    	model.setInteger("status_effect_target_passive_flag",passiveFlag.number);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (originEntityId != null) {
    	       	    	model.setString("status_effect_target_origin_entity_id",originEntityId);
    	        		
    	//}
    	    	    	    }
    
	public static StatusEffectTarget fromModel(Model model) {
		boolean hasFields = false;
    	StatusEffectTarget message = new StatusEffectTarget();
    	    	    	    	    	
    	    			String targetEntityIdTestField = model.getString("status_effect_target_target_entity_id");
		if (targetEntityIdTestField != null) {
			String targetEntityIdField = targetEntityIdTestField;
			message.setTargetEntityId(targetEntityIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	    	
    	    			String originCharacterIdTestField = model.getString("status_effect_target_origin_character_id");
		if (originCharacterIdTestField != null) {
			String originCharacterIdField = originCharacterIdTestField;
			message.setOriginCharacterId(originCharacterIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Long activeIdTestField = model.getLong("status_effect_target_active_id");
		if (activeIdTestField != null) {
			long activeIdField = activeIdTestField;
			message.setActiveId(activeIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Long lastTickTestField = model.getLong("status_effect_target_last_tick");
		if (lastTickTestField != null) {
			long lastTickField = lastTickTestField;
			message.setLastTick(lastTickField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    				message.setAction(Action.valueOf(model.getInteger("status_effect_target_action")));
    	    	    	    	    	    	
    				message.setPassiveFlag(PassiveFlag.valueOf(model.getInteger("status_effect_target_passive_flag")));
    	    	    	    	    	    	
    	    			String originEntityIdTestField = model.getString("status_effect_target_origin_entity_id");
		if (originEntityIdTestField != null) {
			String originEntityIdField = originEntityIdTestField;
			message.setOriginEntityId(originEntityIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public String getTargetEntityId() {
		return targetEntityId;
	}
	
	public StatusEffectTarget setTargetEntityId(String targetEntityId) {
		this.targetEntityId = targetEntityId;
		return this;	}
	
		            
		public GmVector3 getLocation() {
		return location;
	}
	
	public StatusEffectTarget setLocation(GmVector3 location) {
		this.location = location;
		return this;	}
	
		            
		public List<StatusEffect> getStatusEffectList() {
		if(this.statusEffect == null)
            this.statusEffect = new ArrayList<StatusEffect>();
		return statusEffect;
	}

	public StatusEffectTarget setStatusEffectList(List<StatusEffect> statusEffect) {
		this.statusEffect = statusEffect;
		return this;
	}

	public StatusEffect getStatusEffect(int index)  {
        return statusEffect == null ? null : statusEffect.get(index);
    }

    public int getStatusEffectCount()  {
        return statusEffect == null ? 0 : statusEffect.size();
    }

    public StatusEffectTarget addStatusEffect(StatusEffect statusEffect)  {
        if(this.statusEffect == null)
            this.statusEffect = new ArrayList<StatusEffect>();
        this.statusEffect.add(statusEffect);
        return this;
    }
            	    	    	    	    	
    public StatusEffectTarget removeStatusEffectById(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByDuration(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.duration == obj.duration) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByTicks(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.ticks == obj.ticks) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByMinValue(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.minValue == obj.minValue) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByMaxValue(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.maxValue == obj.maxValue) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByParticleEffect(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.particleEffect.equals(obj.particleEffect)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public StatusEffectTarget removeStatusEffectByIcon_path(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.icon_path.equals(obj.icon_path)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByIcon_uuid(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.icon_uuid.equals(obj.icon_uuid)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByTicksPerformed(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.ticksPerformed == obj.ticksPerformed) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public StatusEffectTarget removeStatusEffectByResourceCost(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.resourceCost == obj.resourceCost) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByRange(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.range == obj.range) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffectTarget removeStatusEffectByAttribute(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.attribute == obj.attribute) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		            
		public String getOriginCharacterId() {
		return originCharacterId;
	}
	
	public StatusEffectTarget setOriginCharacterId(String originCharacterId) {
		this.originCharacterId = originCharacterId;
		return this;	}
	
		            
		public long getActiveId() {
		return activeId;
	}
	
	public StatusEffectTarget setActiveId(long activeId) {
		this.activeId = activeId;
		return this;	}
	
		            
		public long getLastTick() {
		return lastTick;
	}
	
	public StatusEffectTarget setLastTick(long lastTick) {
		this.lastTick = lastTick;
		return this;	}
	
		            
		public Action getAction() {
		return action;
	}
	
	public StatusEffectTarget setAction(Action action) {
		this.action = action;
		return this;	}
	
		            
		public PassiveFlag getPassiveFlag() {
		return passiveFlag;
	}
	
	public StatusEffectTarget setPassiveFlag(PassiveFlag passiveFlag) {
		this.passiveFlag = passiveFlag;
		return this;	}
	
		            
		public String getOriginEntityId() {
		return originEntityId;
	}
	
	public StatusEffectTarget setOriginEntityId(String originEntityId) {
		this.originEntityId = originEntityId;
		return this;	}
	
		            
		public Attack getAttack() {
		return attack;
	}
	
	public StatusEffectTarget setAttack(Attack attack) {
		this.attack = attack;
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

    public Schema<StatusEffectTarget> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public StatusEffectTarget newMessage()
    {
        return new StatusEffectTarget();
    }

    public Class<StatusEffectTarget> typeClass()
    {
        return StatusEffectTarget.class;
    }

    public String messageName()
    {
        return StatusEffectTarget.class.getSimpleName();
    }

    public String messageFullName()
    {
        return StatusEffectTarget.class.getName();
    }

    public boolean isInitialized(StatusEffectTarget message)
    {
        return true;
    }

    public void mergeFrom(Input input, StatusEffectTarget message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.targetEntityId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.location = input.mergeObject(message.location, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 5:
            	            		if(message.statusEffect == null)
                        message.statusEffect = new ArrayList<StatusEffect>();
                                        message.statusEffect.add(input.mergeObject(null, StatusEffect.getSchema()));
                                        break;
                            	            	case 6:
            	                	                	message.originCharacterId = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.activeId = input.readInt64();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.lastTick = input.readInt64();
                	break;
                	                	
                            	            	case 10:
            	                	                    message.action = Action.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 11:
            	                	                    message.passiveFlag = PassiveFlag.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 12:
            	                	                	message.originEntityId = input.readString();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.attack = input.mergeObject(message.attack, Attack.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, StatusEffectTarget message) throws IOException
    {
    	    	
    	    	//if(message.targetEntityId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.targetEntityId != null) {
            output.writeString(1, message.targetEntityId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.location != null)
    		output.writeObject(3, message.location, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	if(message.statusEffect != null)
        {
            for(StatusEffect statusEffect : message.statusEffect)
            {
                if( (StatusEffect) statusEffect != null) {
                   	    				output.writeObject(5, statusEffect, StatusEffect.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	//if(message.originCharacterId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.originCharacterId != null) {
            output.writeString(6, message.originCharacterId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.activeId != null) {
            output.writeInt64(8, message.activeId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.lastTick != null) {
            output.writeInt64(9, message.lastTick, false);
        }
    	    	
    	            	
    	    	//if(message.action == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.action != null)
    	 	output.writeEnum(10, message.action.number, false);
    	    	
    	            	
    	    	//if(message.passiveFlag == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.passiveFlag != null)
    	 	output.writeEnum(11, message.passiveFlag.number, false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.originEntityId != null) {
            output.writeString(12, message.originEntityId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.attack != null)
    		output.writeObject(13, message.attack, Attack.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START StatusEffectTarget");
    	    	//if(this.targetEntityId != null) {
    		System.out.println("targetEntityId="+this.targetEntityId);
    	//}
    	    	//if(this.location != null) {
    		System.out.println("location="+this.location);
    	//}
    	    	//if(this.statusEffect != null) {
    		System.out.println("statusEffect="+this.statusEffect);
    	//}
    	    	//if(this.originCharacterId != null) {
    		System.out.println("originCharacterId="+this.originCharacterId);
    	//}
    	    	//if(this.activeId != null) {
    		System.out.println("activeId="+this.activeId);
    	//}
    	    	//if(this.lastTick != null) {
    		System.out.println("lastTick="+this.lastTick);
    	//}
    	    	//if(this.action != null) {
    		System.out.println("action="+this.action);
    	//}
    	    	//if(this.passiveFlag != null) {
    		System.out.println("passiveFlag="+this.passiveFlag);
    	//}
    	    	//if(this.originEntityId != null) {
    		System.out.println("originEntityId="+this.originEntityId);
    	//}
    	    	//if(this.attack != null) {
    		System.out.println("attack="+this.attack);
    	//}
    	    	System.out.println("END StatusEffectTarget");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "targetEntityId";
        	        	case 3: return "location";
        	        	case 5: return "statusEffect";
        	        	case 6: return "originCharacterId";
        	        	case 8: return "activeId";
        	        	case 9: return "lastTick";
        	        	case 10: return "action";
        	        	case 11: return "passiveFlag";
        	        	case 12: return "originEntityId";
        	        	case 13: return "attack";
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
    	    	__fieldMap.put("targetEntityId", 1);
    	    	__fieldMap.put("location", 3);
    	    	__fieldMap.put("statusEffect", 5);
    	    	__fieldMap.put("originCharacterId", 6);
    	    	__fieldMap.put("activeId", 8);
    	    	__fieldMap.put("lastTick", 9);
    	    	__fieldMap.put("action", 10);
    	    	__fieldMap.put("passiveFlag", 11);
    	    	__fieldMap.put("originEntityId", 12);
    	    	__fieldMap.put("attack", 13);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = StatusEffectTarget.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static StatusEffectTarget parseFrom(byte[] bytes) {
	StatusEffectTarget message = new StatusEffectTarget();
	ProtobufIOUtil.mergeFrom(bytes, message, StatusEffectTarget.getSchema());
	return message;
}

public static StatusEffectTarget parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	StatusEffectTarget message = new StatusEffectTarget();
	JsonIOUtil.mergeFrom(bytes, message, StatusEffectTarget.getSchema(), false);
	return message;
}

public StatusEffectTarget clone() {
	byte[] bytes = this.toByteArray();
	StatusEffectTarget statusEffectTarget = StatusEffectTarget.parseFrom(bytes);
	return statusEffectTarget;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, StatusEffectTarget.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<StatusEffectTarget> schema = StatusEffectTarget.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, StatusEffectTarget.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, StatusEffectTarget.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
