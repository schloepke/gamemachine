
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
public final class StatusEffect implements Externalizable, Message<StatusEffect>, Schema<StatusEffect>{

	public enum Type implements io.protostuff.EnumLite<Type>
    {
    	
    	    	StatusEffectNone(0),    	    	AttributeDecrease(1),    	    	AttributeIncrease(2),    	    	Heal(3),    	    	Slow(4),    	    	Stun(5),    	    	Root(6),    	    	Death(7),    	    	ArmorIncrease(8),    	    	SpellResistIncrease(9),    	    	ElementalResistIncrease(10),    	    	SpellPenetrationIncrease(11),    	    	MagicRegenIncrease(12),    	    	HealthRegenIncrease(13),    	    	StaminaRegenIncrease(14),    	    	ArmorDecrease(15),    	    	SpellResistDecrease(16),    	    	ElementalResistDecrease(17),    	    	MagicRegenDecrease(18),    	    	HealthRegenDecrease(19),    	    	StaminaRegenDecrease(20),    	    	Speed(21);    	        
        public final int number;
        
        private Type (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Type valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (StatusEffectNone);
    			    			case 1: return (AttributeDecrease);
    			    			case 2: return (AttributeIncrease);
    			    			case 3: return (Heal);
    			    			case 4: return (Slow);
    			    			case 5: return (Stun);
    			    			case 6: return (Root);
    			    			case 7: return (Death);
    			    			case 8: return (ArmorIncrease);
    			    			case 9: return (SpellResistIncrease);
    			    			case 10: return (ElementalResistIncrease);
    			    			case 11: return (SpellPenetrationIncrease);
    			    			case 12: return (MagicRegenIncrease);
    			    			case 13: return (HealthRegenIncrease);
    			    			case 14: return (StaminaRegenIncrease);
    			    			case 15: return (ArmorDecrease);
    			    			case 16: return (SpellResistDecrease);
    			    			case 17: return (ElementalResistDecrease);
    			    			case 18: return (MagicRegenDecrease);
    			    			case 19: return (HealthRegenDecrease);
    			    			case 20: return (StaminaRegenDecrease);
    			    			case 21: return (Speed);
    			                default: return null;
            }
        }
    }
	public enum DamageType implements io.protostuff.EnumLite<DamageType>
    {
    	
    	    	DamageTypeNone(0),    	    	Physical(1),    	    	Magic(2),    	    	Elemental(3);    	        
        public final int number;
        
        private DamageType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static DamageType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (DamageTypeNone);
    			    			case 1: return (Physical);
    			    			case 2: return (Magic);
    			    			case 3: return (Elemental);
    			                default: return null;
            }
        }
    }


    public static Schema<StatusEffect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static StatusEffect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final StatusEffect DEFAULT_INSTANCE = new StatusEffect();

    			public Type type; // = StatusEffectNone:0;
	    
        			public String id;
	    
        			public int duration;
	    
        			public int ticks;
	    
        			public String attribute;
	    
        			public int minValue;
	    
        			public int maxValue;
	    
        			public String particleEffect;
	    
        			public DamageType damageType; // = DamageTypeNone:0;
	    
        			public String icon_path;
	    
        			public String icon_uuid;
	    
      
    public StatusEffect()
    {
        
    }


	

	    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public Type getType() {
		return type;
	}
	
	public StatusEffect setType(Type type) {
		this.type = type;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public StatusEffect setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasDuration()  {
        return duration == null ? false : true;
    }
        
		public int getDuration() {
		return duration;
	}
	
	public StatusEffect setDuration(int duration) {
		this.duration = duration;
		return this;	}
	
		    
    public Boolean hasTicks()  {
        return ticks == null ? false : true;
    }
        
		public int getTicks() {
		return ticks;
	}
	
	public StatusEffect setTicks(int ticks) {
		this.ticks = ticks;
		return this;	}
	
		    
    public Boolean hasAttribute()  {
        return attribute == null ? false : true;
    }
        
		public String getAttribute() {
		return attribute;
	}
	
	public StatusEffect setAttribute(String attribute) {
		this.attribute = attribute;
		return this;	}
	
		    
    public Boolean hasMinValue()  {
        return minValue == null ? false : true;
    }
        
		public int getMinValue() {
		return minValue;
	}
	
	public StatusEffect setMinValue(int minValue) {
		this.minValue = minValue;
		return this;	}
	
		    
    public Boolean hasMaxValue()  {
        return maxValue == null ? false : true;
    }
        
		public int getMaxValue() {
		return maxValue;
	}
	
	public StatusEffect setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		return this;	}
	
		    
    public Boolean hasParticleEffect()  {
        return particleEffect == null ? false : true;
    }
        
		public String getParticleEffect() {
		return particleEffect;
	}
	
	public StatusEffect setParticleEffect(String particleEffect) {
		this.particleEffect = particleEffect;
		return this;	}
	
		    
    public Boolean hasDamageType()  {
        return damageType == null ? false : true;
    }
        
		public DamageType getDamageType() {
		return damageType;
	}
	
	public StatusEffect setDamageType(DamageType damageType) {
		this.damageType = damageType;
		return this;	}
	
		    
    public Boolean hasIcon_path()  {
        return icon_path == null ? false : true;
    }
        
		public String getIcon_path() {
		return icon_path;
	}
	
	public StatusEffect setIcon_path(String icon_path) {
		this.icon_path = icon_path;
		return this;	}
	
		    
    public Boolean hasIcon_uuid()  {
        return icon_uuid == null ? false : true;
    }
        
		public String getIcon_uuid() {
		return icon_uuid;
	}
	
	public StatusEffect setIcon_uuid(String icon_uuid) {
		this.icon_uuid = icon_uuid;
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

    public Schema<StatusEffect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public StatusEffect newMessage()
    {
        return new StatusEffect();
    }

    public Class<StatusEffect> typeClass()
    {
        return StatusEffect.class;
    }

    public String messageName()
    {
        return StatusEffect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return StatusEffect.class.getName();
    }

    public boolean isInitialized(StatusEffect message)
    {
        return true;
    }

    public void mergeFrom(Input input, StatusEffect message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                    message.type = Type.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 2:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.duration = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.ticks = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.attribute = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.minValue = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.maxValue = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.particleEffect = input.readString();
                	break;
                	                	
                            	            	case 9:
            	                	                    message.damageType = DamageType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 10:
            	                	                	message.icon_path = input.readString();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.icon_uuid = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, StatusEffect message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.type != null)
    	 	output.writeEnum(1, message.type.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(2, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.duration != null)
            output.writeInt32(3, message.duration, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ticks != null)
            output.writeInt32(4, message.ticks, false);
    	    	
    	            	
    	    	
    	    	    	if(message.attribute != null)
            output.writeString(5, message.attribute, false);
    	    	
    	            	
    	    	
    	    	    	if(message.minValue != null)
            output.writeInt32(6, message.minValue, false);
    	    	
    	            	
    	    	
    	    	    	if(message.maxValue != null)
            output.writeInt32(7, message.maxValue, false);
    	    	
    	            	
    	    	
    	    	    	if(message.particleEffect != null)
            output.writeString(8, message.particleEffect, false);
    	    	
    	            	
    	    	
    	    	    	if(message.damageType != null)
    	 	output.writeEnum(9, message.damageType.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_path != null)
            output.writeString(10, message.icon_path, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_uuid != null)
            output.writeString(11, message.icon_uuid, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "type";
        	        	case 2: return "id";
        	        	case 3: return "duration";
        	        	case 4: return "ticks";
        	        	case 5: return "attribute";
        	        	case 6: return "minValue";
        	        	case 7: return "maxValue";
        	        	case 8: return "particleEffect";
        	        	case 9: return "damageType";
        	        	case 10: return "icon_path";
        	        	case 11: return "icon_uuid";
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
    	    	__fieldMap.put("type", 1);
    	    	__fieldMap.put("id", 2);
    	    	__fieldMap.put("duration", 3);
    	    	__fieldMap.put("ticks", 4);
    	    	__fieldMap.put("attribute", 5);
    	    	__fieldMap.put("minValue", 6);
    	    	__fieldMap.put("maxValue", 7);
    	    	__fieldMap.put("particleEffect", 8);
    	    	__fieldMap.put("damageType", 9);
    	    	__fieldMap.put("icon_path", 10);
    	    	__fieldMap.put("icon_uuid", 11);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = StatusEffect.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static StatusEffect parseFrom(byte[] bytes) {
	StatusEffect message = new StatusEffect();
	ProtobufIOUtil.mergeFrom(bytes, message, StatusEffect.getSchema());
	return message;
}

public static StatusEffect parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	StatusEffect message = new StatusEffect();
	JsonIOUtil.mergeFrom(bytes, message, StatusEffect.getSchema(), false);
	return message;
}

public StatusEffect clone() {
	byte[] bytes = this.toByteArray();
	StatusEffect statusEffect = StatusEffect.parseFrom(bytes);
	return statusEffect;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, StatusEffect.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<StatusEffect> schema = StatusEffect.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, StatusEffect.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
