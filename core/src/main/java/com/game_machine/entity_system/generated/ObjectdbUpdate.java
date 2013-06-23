
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

public final class ObjectdbUpdate  implements Externalizable, Message<ObjectdbUpdate>, Schema<ObjectdbUpdate>
{




    public static Schema<ObjectdbUpdate> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbUpdate getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbUpdate DEFAULT_INSTANCE = new ObjectdbUpdate();



    private String entityId;



    private String updateClass;



    private String updateMethod;


    


    public ObjectdbUpdate()
    {
        
    }







	
    

	public String getEntityId() {
		return entityId;
	}
	
	public ObjectdbUpdate setEntityId(String entityId) {
		this.entityId = entityId;
		return this;
	}
	
	public Boolean hasEntityId()  {
        return entityId == null ? false : true;
    }




	
    

	public String getUpdateClass() {
		return updateClass;
	}
	
	public ObjectdbUpdate setUpdateClass(String updateClass) {
		this.updateClass = updateClass;
		return this;
	}
	
	public Boolean hasUpdateClass()  {
        return updateClass == null ? false : true;
    }




	
    

	public String getUpdateMethod() {
		return updateMethod;
	}
	
	public ObjectdbUpdate setUpdateMethod(String updateMethod) {
		this.updateMethod = updateMethod;
		return this;
	}
	
	public Boolean hasUpdateMethod()  {
        return updateMethod == null ? false : true;
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

    public Schema<ObjectdbUpdate> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbUpdate newMessage()
    {
        return new ObjectdbUpdate();
    }

    public Class<ObjectdbUpdate> typeClass()
    {
        return ObjectdbUpdate.class;
    }

    public String messageName()
    {
        return ObjectdbUpdate.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbUpdate.class.getName();
    }

    public boolean isInitialized(ObjectdbUpdate message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbUpdate message) throws IOException
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

                	


            	case 2:


                	message.updateClass = input.readString();
                	break;

                	


            	case 3:


                	message.updateMethod = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ObjectdbUpdate message) throws IOException
    {

    	

    	if(message.entityId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.entityId != null)
            output.writeString(1, message.entityId, false);

    	


    	

    	if(message.updateClass == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.updateClass != null)
            output.writeString(2, message.updateClass, false);

    	


    	

    	if(message.updateMethod == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.updateMethod != null)
            output.writeString(3, message.updateMethod, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "entityId";

        	case 2: return "updateClass";

        	case 3: return "updateMethod";

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

    	__fieldMap.put("updateClass", 2);

    	__fieldMap.put("updateMethod", 3);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbUpdate.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbUpdate parseFrom(byte[] bytes) {
	ObjectdbUpdate message = new ObjectdbUpdate();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ObjectdbUpdate.class));
	return message;
}

public ObjectdbUpdate clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbUpdate objectdbUpdate = ObjectdbUpdate.parseFrom(bytes);
	return objectdbUpdate;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbUpdate.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ObjectdbUpdate.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
