
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

public final class ObjectdbPut  implements Externalizable, Message<ObjectdbPut>, Schema<ObjectdbPut>
{




    public static Schema<ObjectdbPut> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbPut getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbPut DEFAULT_INSTANCE = new ObjectdbPut();



    private Entity entity;


    


    public ObjectdbPut()
    {
        
    }







	
    

	public Entity getEntity() {
		return entity;
	}
	
	public ObjectdbPut setEntity(Entity entity) {
		this.entity = entity;
		return this;
	}
	
	public Boolean hasEntity()  {
        return entity == null ? false : true;
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

    public Schema<ObjectdbPut> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbPut newMessage()
    {
        return new ObjectdbPut();
    }

    public Class<ObjectdbPut> typeClass()
    {
        return ObjectdbPut.class;
    }

    public String messageName()
    {
        return ObjectdbPut.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbPut.class.getName();
    }

    public boolean isInitialized(ObjectdbPut message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbPut message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.entity = input.mergeObject(message.entity, Entity.getSchema());
                    break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ObjectdbPut message) throws IOException
    {

    	

    	if(message.entity == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.entity != null)
    		output.writeObject(1, message.entity, Entity.getSchema(), false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "entity";

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

    	__fieldMap.put("entity", 1);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbPut.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbPut parseFrom(byte[] bytes) {
	ObjectdbPut message = new ObjectdbPut();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ObjectdbPut.class));
	return message;
}

public ObjectdbPut clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbPut objectdbPut = ObjectdbPut.parseFrom(bytes);
	return objectdbPut;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbPut.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}
		
public byte[] toProtobuf() {
	LinkedBuffer buffer = LinkedBuffer.allocate(1024);
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ObjectdbPut.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
