
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

public final class Neighbors  implements Externalizable, Message<Neighbors>, Schema<Neighbors>
{




    public static Schema<Neighbors> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Neighbors getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Neighbors DEFAULT_INSTANCE = new Neighbors();



    public List<Entity> entity;


    


    public Neighbors()
    {
        
    }






    

	public List<Entity> getEntityList() {
		return entity;
	}

	public Neighbors setEntityList(List<Entity> entity) {
		this.entity = entity;
		return this;
	}

	public Entity getEntity(int index)  {
        return entity == null ? null : entity.get(index);
    }

    public int getEntityCount()  {
        return entity == null ? 0 : entity.size();
    }

    public Neighbors addEntity(Entity entity)  {
        if(this.entity == null)
            this.entity = new ArrayList<Entity>();
        this.entity.add(entity);
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

    public Schema<Neighbors> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Neighbors newMessage()
    {
        return new Neighbors();
    }

    public Class<Neighbors> typeClass()
    {
        return Neighbors.class;
    }

    public String messageName()
    {
        return Neighbors.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Neighbors.class.getName();
    }

    public boolean isInitialized(Neighbors message)
    {
        return true;
    }

    public void mergeFrom(Input input, Neighbors message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:

            		if(message.entity == null)
                        message.entity = new ArrayList<Entity>();

                    message.entity.add(input.mergeObject(null, Entity.getSchema()));

                    break;


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Neighbors message) throws IOException
    {

    	

    	

    	if(message.entity != null)
        {
            for(Entity entity : message.entity)
            {
                if(entity != null) {

    				output.writeObject(1, entity, Entity.getSchema(), true);

    			}
            }
        }


    	
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
		fieldName = Neighbors.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Neighbors parseFrom(byte[] bytes) {
	Neighbors message = new Neighbors();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Neighbors.class));
	return message;
}

public Neighbors clone() {
	byte[] bytes = this.toByteArray();
	Neighbors neighbors = Neighbors.parseFrom(bytes);
	return neighbors;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Neighbors.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Neighbors.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
