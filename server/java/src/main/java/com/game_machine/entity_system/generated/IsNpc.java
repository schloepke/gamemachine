
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

public final class IsNpc  implements Externalizable, Message<IsNpc>, Schema<IsNpc>
{




    public static Schema<IsNpc> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static IsNpc getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final IsNpc DEFAULT_INSTANCE = new IsNpc();



    public Boolean enabled;


    


    public IsNpc()
    {
        
    }






    

	public Boolean getEnabled() {
		return enabled;
	}
	
	public IsNpc setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	
	public Boolean hasEnabled()  {
        return enabled == null ? false : true;
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

    public Schema<IsNpc> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public IsNpc newMessage()
    {
        return new IsNpc();
    }

    public Class<IsNpc> typeClass()
    {
        return IsNpc.class;
    }

    public String messageName()
    {
        return IsNpc.class.getSimpleName();
    }

    public String messageFullName()
    {
        return IsNpc.class.getName();
    }

    public boolean isInitialized(IsNpc message)
    {
        return true;
    }

    public void mergeFrom(Input input, IsNpc message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.enabled = input.readBool();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, IsNpc message) throws IOException
    {

    	

    	if(message.enabled == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.enabled != null)
            output.writeBool(1, message.enabled, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "enabled";

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

    	__fieldMap.put("enabled", 1);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = IsNpc.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static IsNpc parseFrom(byte[] bytes) {
	IsNpc message = new IsNpc();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(IsNpc.class));
	return message;
}

public IsNpc clone() {
	byte[] bytes = this.toByteArray();
	IsNpc isNpc = IsNpc.parseFrom(bytes);
	return isNpc;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, IsNpc.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(IsNpc.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
