
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

public final class DestroySingleton  implements Externalizable, Message<DestroySingleton>, Schema<DestroySingleton>
{




    public static Schema<DestroySingleton> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static DestroySingleton getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final DestroySingleton DEFAULT_INSTANCE = new DestroySingleton();



    public String id;


    


    public DestroySingleton()
    {
        
    }






    

	public String getId() {
		return id;
	}
	
	public DestroySingleton setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
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

    public Schema<DestroySingleton> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public DestroySingleton newMessage()
    {
        return new DestroySingleton();
    }

    public Class<DestroySingleton> typeClass()
    {
        return DestroySingleton.class;
    }

    public String messageName()
    {
        return DestroySingleton.class.getSimpleName();
    }

    public String messageFullName()
    {
        return DestroySingleton.class.getName();
    }

    public boolean isInitialized(DestroySingleton message)
    {
        return true;
    }

    public void mergeFrom(Input input, DestroySingleton message) throws IOException
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

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, DestroySingleton message) throws IOException
    {

    	

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.id != null)
            output.writeString(1, message.id, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "id";

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

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = DestroySingleton.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static DestroySingleton parseFrom(byte[] bytes) {
	DestroySingleton message = new DestroySingleton();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(DestroySingleton.class));
	return message;
}

public DestroySingleton clone() {
	byte[] bytes = this.toByteArray();
	DestroySingleton destroySingleton = DestroySingleton.parseFrom(bytes);
	return destroySingleton;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, DestroySingleton.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(DestroySingleton.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
