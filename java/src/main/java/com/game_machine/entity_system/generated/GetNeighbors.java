
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

public final class GetNeighbors  implements Externalizable, Message<GetNeighbors>, Schema<GetNeighbors>
{




    public static Schema<GetNeighbors> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GetNeighbors getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GetNeighbors DEFAULT_INSTANCE = new GetNeighbors();



    public Boolean value;



    public Integer search_radius;



    public Vector3 vector3;


    


    public GetNeighbors()
    {
        
    }






    

	public Boolean getValue() {
		return value;
	}
	
	public GetNeighbors setValue(Boolean value) {
		this.value = value;
		return this;
	}
	
	public Boolean hasValue()  {
        return value == null ? false : true;
    }



    

	public Integer getSearch_radius() {
		return search_radius;
	}
	
	public GetNeighbors setSearch_radius(Integer search_radius) {
		this.search_radius = search_radius;
		return this;
	}
	
	public Boolean hasSearch_radius()  {
        return search_radius == null ? false : true;
    }



    

	public Vector3 getVector3() {
		return vector3;
	}
	
	public GetNeighbors setVector3(Vector3 vector3) {
		this.vector3 = vector3;
		return this;
	}
	
	public Boolean hasVector3()  {
        return vector3 == null ? false : true;
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

    public Schema<GetNeighbors> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GetNeighbors newMessage()
    {
        return new GetNeighbors();
    }

    public Class<GetNeighbors> typeClass()
    {
        return GetNeighbors.class;
    }

    public String messageName()
    {
        return GetNeighbors.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GetNeighbors.class.getName();
    }

    public boolean isInitialized(GetNeighbors message)
    {
        return true;
    }

    public void mergeFrom(Input input, GetNeighbors message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.value = input.readBool();
                	break;

                	


            	case 2:


                	message.search_radius = input.readUInt32();
                	break;

                	


            	case 3:


                	message.vector3 = input.mergeObject(message.vector3, Vector3.getSchema());
                    break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GetNeighbors message) throws IOException
    {

    	

    	if(message.value == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.value != null)
            output.writeBool(1, message.value, false);

    	


    	

    	


    	if(message.search_radius != null)
            output.writeUInt32(2, message.search_radius, false);

    	


    	

    	if(message.vector3 == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.vector3 != null)
    		output.writeObject(3, message.vector3, Vector3.getSchema(), false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "value";

        	case 2: return "search_radius";

        	case 3: return "vector3";

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

    	__fieldMap.put("value", 1);

    	__fieldMap.put("search_radius", 2);

    	__fieldMap.put("vector3", 3);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GetNeighbors.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GetNeighbors parseFrom(byte[] bytes) {
	GetNeighbors message = new GetNeighbors();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(GetNeighbors.class));
	return message;
}

public GetNeighbors clone() {
	byte[] bytes = this.toByteArray();
	GetNeighbors getNeighbors = GetNeighbors.parseFrom(bytes);
	return getNeighbors;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GetNeighbors.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(GetNeighbors.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
