
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
public final class StatusEffects implements Externalizable, Message<StatusEffects>, Schema<StatusEffects>{

private static final Logger logger = LoggerFactory.getLogger(StatusEffects.class);



    public static Schema<StatusEffects> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static StatusEffects getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final StatusEffects DEFAULT_INSTANCE = new StatusEffects();
    static final String defaultScope = StatusEffects.class.getSimpleName();

        public List<StatusEffect> statusEffect;
	    


    public StatusEffects()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static StatusEffects fromModel(Model model) {
		boolean hasFields = false;
    	StatusEffects message = new StatusEffects();
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public List<StatusEffect> getStatusEffectList() {
		if(this.statusEffect == null)
            this.statusEffect = new ArrayList<StatusEffect>();
		return statusEffect;
	}

	public StatusEffects setStatusEffectList(List<StatusEffect> statusEffect) {
		this.statusEffect = statusEffect;
		return this;
	}

	public StatusEffect getStatusEffect(int index)  {
        return statusEffect == null ? null : statusEffect.get(index);
    }

    public int getStatusEffectCount()  {
        return statusEffect == null ? 0 : statusEffect.size();
    }

    public StatusEffects addStatusEffect(StatusEffect statusEffect)  {
        if(this.statusEffect == null)
            this.statusEffect = new ArrayList<StatusEffect>();
        this.statusEffect.add(statusEffect);
        return this;
    }
            	    	    	    	    	
    public StatusEffects removeStatusEffectById(StatusEffect statusEffect)  {
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
    
        	    	    	    	
    public StatusEffects removeStatusEffectByDuration(StatusEffect statusEffect)  {
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
    
        	    	    	    	
    public StatusEffects removeStatusEffectByTicks(StatusEffect statusEffect)  {
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
    
        	    	    	    	
    public StatusEffects removeStatusEffectByAttribute(StatusEffect statusEffect)  {
    	if(this.statusEffect == null)
           return this;
            
       	Iterator<StatusEffect> itr = this.statusEffect.iterator();
       	while (itr.hasNext()) {
    	StatusEffect obj = itr.next();
    	
    	    		if (statusEffect.attribute.equals(obj.attribute)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public StatusEffects removeStatusEffectByMinValue(StatusEffect statusEffect)  {
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
    
        	    	    	    	
    public StatusEffects removeStatusEffectByMaxValue(StatusEffect statusEffect)  {
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
    
        	    	    	    	
    public StatusEffects removeStatusEffectByParticleEffect(StatusEffect statusEffect)  {
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
    
        	    	    	    	    	
    public StatusEffects removeStatusEffectByIcon_path(StatusEffect statusEffect)  {
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
    
        	    	    	    	
    public StatusEffects removeStatusEffectByIcon_uuid(StatusEffect statusEffect)  {
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
    
        	    	    	    	
    public StatusEffects removeStatusEffectByTicksPerformed(StatusEffect statusEffect)  {
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
    
        	    	    	    	    	
    public StatusEffects removeStatusEffectByResourceCost(StatusEffect statusEffect)  {
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
    
        	    	    	    	
    public StatusEffects removeStatusEffectByRange(StatusEffect statusEffect)  {
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

    public Schema<StatusEffects> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public StatusEffects newMessage()
    {
        return new StatusEffects();
    }

    public Class<StatusEffects> typeClass()
    {
        return StatusEffects.class;
    }

    public String messageName()
    {
        return StatusEffects.class.getSimpleName();
    }

    public String messageFullName()
    {
        return StatusEffects.class.getName();
    }

    public boolean isInitialized(StatusEffects message)
    {
        return true;
    }

    public void mergeFrom(Input input, StatusEffects message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.statusEffect == null)
                        message.statusEffect = new ArrayList<StatusEffect>();
                                        message.statusEffect.add(input.mergeObject(null, StatusEffect.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, StatusEffects message) throws IOException
    {
    	    	
    	    	
    	    	if(message.statusEffect != null)
        {
            for(StatusEffect statusEffect : message.statusEffect)
            {
                if( (StatusEffect) statusEffect != null) {
                   	    				output.writeObject(1, statusEffect, StatusEffect.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START StatusEffects");
    	    	//if(this.statusEffect != null) {
    		System.out.println("statusEffect="+this.statusEffect);
    	//}
    	    	System.out.println("END StatusEffects");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "statusEffect";
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
    	    	__fieldMap.put("statusEffect", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = StatusEffects.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static StatusEffects parseFrom(byte[] bytes) {
	StatusEffects message = new StatusEffects();
	ProtobufIOUtil.mergeFrom(bytes, message, StatusEffects.getSchema());
	return message;
}

public static StatusEffects parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	StatusEffects message = new StatusEffects();
	JsonIOUtil.mergeFrom(bytes, message, StatusEffects.getSchema(), false);
	return message;
}

public StatusEffects clone() {
	byte[] bytes = this.toByteArray();
	StatusEffects statusEffects = StatusEffects.parseFrom(bytes);
	return statusEffects;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, StatusEffects.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<StatusEffects> schema = StatusEffects.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, StatusEffects.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, StatusEffects.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
