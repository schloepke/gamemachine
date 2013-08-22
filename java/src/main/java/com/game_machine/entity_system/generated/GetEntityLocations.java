
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

public final class GetEntityLocations  implements Externalizable, Message<GetEntityLocations>, Schema<GetEntityLocations>
{




    public static Schema<GetEntityLocations> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GetEntityLocations getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GetEntityLocations DEFAULT_INSTANCE = new GetEntityLocations();



    public List<String> entityId;


    


    public GetEntityLocations()
    {
        
    }






    

	public List<String> getEntityIdList() {
		return entityId;
	}

	public GetEntityLocations setEntityIdList(List<String> entityId) {
		this.entityId = entityId;
		return this;
	}

	public String getEntityId(int index)  {
        return entityId == null ? null : entityId.get(index);
    }

    public int getEntityIdCount()  {
        return entityId == null ? 0 : entityId.size();
    }

    public GetEntityLocations addEntityId(String entityId)  {
        if(this.entityId == null)
            this.entityId = new ArrayList<String>();
        this.entityId.add(entityId);
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

    public Schema<GetEntityLocations> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GetEntityLocations newMessage()
    {
        return new GetEntityLocations();
    }

    public Class<GetEntityLocations> typeClass()
    {
        return GetEntityLocations.class;
    }

    public String messageName()
    {
        return GetEntityLocations.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GetEntityLocations.class.getName();
    }

    public boolean isInitialized(GetEntityLocations message)
    {
        return true;
    }

    public void mergeFrom(Input input, GetEntityLocations message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:

            		if(message.entityId == null)
                        message.entityId = new ArrayList<String>();

                	message.entityId.add(input.readString());

                    break;


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GetEntityLocations message) throws IOException
    {

    	

    	

    	if(message.entityId != null)
        {
            for(String entityId : message.entityId)
            {
                if(entityId != null) {

            		output.writeString(1, entityId, true);

    			}
            }
        }


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "entityId";

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

    	__fieldMap.put("entityId", 1);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GetEntityLocations.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GetEntityLocations parseFrom(byte[] bytes) {
	GetEntityLocations message = new GetEntityLocations();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(GetEntityLocations.class));
	return message;
}

public GetEntityLocations clone() {
	byte[] bytes = this.toByteArray();
	GetEntityLocations getEntityLocations = GetEntityLocations.parseFrom(bytes);
	return getEntityLocations;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GetEntityLocations.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(GetEntityLocations.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
