
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

public final class ObjectdbGet  implements Externalizable, Message<ObjectdbGet>, Schema<ObjectdbGet>
{




    public static Schema<ObjectdbGet> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbGet getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbGet DEFAULT_INSTANCE = new ObjectdbGet();



    private String entityId;


    


    public ObjectdbGet()
    {
        
    }







	
    

	public String getEntityId() {
		return entityId;
	}
	
	public ObjectdbGet setEntityId(String entityId) {
		this.entityId = entityId;
		return this;
	}
	
	public Boolean hasEntityId()  {
        return entityId == null ? false : true;
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

    public Schema<ObjectdbGet> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbGet newMessage()
    {
        return new ObjectdbGet();
    }

    public Class<ObjectdbGet> typeClass()
    {
        return ObjectdbGet.class;
    }

    public String messageName()
    {
        return ObjectdbGet.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbGet.class.getName();
    }

    public boolean isInitialized(ObjectdbGet message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbGet message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.entityId = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ObjectdbGet message) throws IOException
    {

    	

    	if(message.entityId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.entityId != null)
            output.writeString(1, message.entityId, false);

    	


    	
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
		fieldName = ObjectdbGet.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbGet parseFrom(byte[] bytes) {
	ObjectdbGet message = new ObjectdbGet();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ObjectdbGet.class));
	return message;
}

public ObjectdbGet clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbGet objectdbGet = ObjectdbGet.parseFrom(bytes);
	return objectdbGet;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbGet.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ObjectdbGet.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
