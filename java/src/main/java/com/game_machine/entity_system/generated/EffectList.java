
package com.game_machine.entity_system.generated;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.game_machine.entity_system.generated.Entity;

import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

public final class EffectList  implements Externalizable, Message<EffectList>, Schema<EffectList>
{




    public static Schema<EffectList> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static EffectList getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final EffectList DEFAULT_INSTANCE = new EffectList();



    public List<Effect> effect;


    


    public EffectList()
    {
        
    }






    

	public List<Effect> getEffectList() {
		return effect;
	}

	public EffectList setEffectList(List<Effect> effect) {
		this.effect = effect;
		return this;
	}

	public Effect getEffect(int index)  {
        return effect == null ? null : effect.get(index);
    }

    public int getEffectCount()  {
        return effect == null ? 0 : effect.size();
    }

    public EffectList addEffect(Effect effect)  {
        if(this.effect == null)
            this.effect = new ArrayList<Effect>();
        this.effect.add(effect);
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

    public Schema<EffectList> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public EffectList newMessage()
    {
        return new EffectList();
    }

    public Class<EffectList> typeClass()
    {
        return EffectList.class;
    }

    public String messageName()
    {
        return EffectList.class.getSimpleName();
    }

    public String messageFullName()
    {
        return EffectList.class.getName();
    }

    public boolean isInitialized(EffectList message)
    {
        return true;
    }

    public void mergeFrom(Input input, EffectList message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:

            		if(message.effect == null)
                        message.effect = new ArrayList<Effect>();

                    message.effect.add(input.mergeObject(null, Effect.getSchema()));

                    break;


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, EffectList message) throws IOException
    {

    	

    	

    	if(message.effect != null)
        {
            for(Effect effect : message.effect)
            {
                if(effect != null) {

    				output.writeObject(1, effect, Effect.getSchema(), true);

    			}
            }
        }


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "effect";

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

    	__fieldMap.put("effect", 1);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = EffectList.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static EffectList parseFrom(byte[] bytes) {
	EffectList message = new EffectList();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(EffectList.class));
	return message;
}

public EffectList clone() {
	byte[] bytes = this.toByteArray();
	EffectList effectList = EffectList.parseFrom(bytes);
	return effectList;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, EffectList.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}
		
public byte[] toProtobuf() {
	LinkedBuffer buffer = LinkedBuffer.allocate(8024);
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(EffectList.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
