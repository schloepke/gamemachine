
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

public final class Effect  implements Externalizable, Message<Effect>, Schema<Effect>
{




    public static Schema<Effect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Effect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Effect DEFAULT_INSTANCE = new Effect();



    public String id;



    public Integer length;



    public String name;



    public Integer healthDiff;



    public Integer damageDiff;



    public Integer timePeriod;



    public String type;


    


    public Effect()
    {
        
    }






    

	public String getId() {
		return id;
	}
	
	public Effect setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }



    

	public Integer getLength() {
		return length;
	}
	
	public Effect setLength(Integer length) {
		this.length = length;
		return this;
	}
	
	public Boolean hasLength()  {
        return length == null ? false : true;
    }



    

	public String getName() {
		return name;
	}
	
	public Effect setName(String name) {
		this.name = name;
		return this;
	}
	
	public Boolean hasName()  {
        return name == null ? false : true;
    }



    

	public Integer getHealthDiff() {
		return healthDiff;
	}
	
	public Effect setHealthDiff(Integer healthDiff) {
		this.healthDiff = healthDiff;
		return this;
	}
	
	public Boolean hasHealthDiff()  {
        return healthDiff == null ? false : true;
    }



    

	public Integer getDamageDiff() {
		return damageDiff;
	}
	
	public Effect setDamageDiff(Integer damageDiff) {
		this.damageDiff = damageDiff;
		return this;
	}
	
	public Boolean hasDamageDiff()  {
        return damageDiff == null ? false : true;
    }



    

	public Integer getTimePeriod() {
		return timePeriod;
	}
	
	public Effect setTimePeriod(Integer timePeriod) {
		this.timePeriod = timePeriod;
		return this;
	}
	
	public Boolean hasTimePeriod()  {
        return timePeriod == null ? false : true;
    }



    

	public String getType() {
		return type;
	}
	
	public Effect setType(String type) {
		this.type = type;
		return this;
	}
	
	public Boolean hasType()  {
        return type == null ? false : true;
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

    public Schema<Effect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Effect newMessage()
    {
        return new Effect();
    }

    public Class<Effect> typeClass()
    {
        return Effect.class;
    }

    public String messageName()
    {
        return Effect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Effect.class.getName();
    }

    public boolean isInitialized(Effect message)
    {
        return true;
    }

    public void mergeFrom(Input input, Effect message) throws IOException
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


                	message.length = input.readInt32();
                	break;

                	


            	case 3:


                	message.name = input.readString();
                	break;

                	


            	case 4:


                	message.healthDiff = input.readInt32();
                	break;

                	


            	case 5:


                	message.damageDiff = input.readInt32();
                	break;

                	


            	case 6:


                	message.timePeriod = input.readInt32();
                	break;

                	


            	case 7:


                	message.type = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Effect message) throws IOException
    {

    	

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.id != null)
            output.writeString(1, message.id, false);

    	


    	

    	


    	if(message.length != null)
            output.writeInt32(2, message.length, false);

    	


    	

    	


    	if(message.name != null)
            output.writeString(3, message.name, false);

    	


    	

    	


    	if(message.healthDiff != null)
            output.writeInt32(4, message.healthDiff, false);

    	


    	

    	


    	if(message.damageDiff != null)
            output.writeInt32(5, message.damageDiff, false);

    	


    	

    	


    	if(message.timePeriod != null)
            output.writeInt32(6, message.timePeriod, false);

    	


    	

    	


    	if(message.type != null)
            output.writeString(7, message.type, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "id";

        	case 2: return "length";

        	case 3: return "name";

        	case 4: return "healthDiff";

        	case 5: return "damageDiff";

        	case 6: return "timePeriod";

        	case 7: return "type";

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

    	__fieldMap.put("length", 2);

    	__fieldMap.put("name", 3);

    	__fieldMap.put("healthDiff", 4);

    	__fieldMap.put("damageDiff", 5);

    	__fieldMap.put("timePeriod", 6);

    	__fieldMap.put("type", 7);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Effect.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Effect parseFrom(byte[] bytes) {
	Effect message = new Effect();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Effect.class));
	return message;
}

public Effect clone() {
	byte[] bytes = this.toByteArray();
	Effect effect = Effect.parseFrom(bytes);
	return effect;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Effect.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Effect.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
