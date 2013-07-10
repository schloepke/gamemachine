
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

public final class EchoTest  implements Externalizable, Message<EchoTest>, Schema<EchoTest>
{




    public static Schema<EchoTest> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static EchoTest getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final EchoTest DEFAULT_INSTANCE = new EchoTest();



    public String message;


    


    public EchoTest()
    {
        
    }






    

	public String getMessage() {
		return message;
	}
	
	public EchoTest setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public Boolean hasMessage()  {
        return message == null ? false : true;
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

    public Schema<EchoTest> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public EchoTest newMessage()
    {
        return new EchoTest();
    }

    public Class<EchoTest> typeClass()
    {
        return EchoTest.class;
    }

    public String messageName()
    {
        return EchoTest.class.getSimpleName();
    }

    public String messageFullName()
    {
        return EchoTest.class.getName();
    }

    public boolean isInitialized(EchoTest message)
    {
        return true;
    }

    public void mergeFrom(Input input, EchoTest message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.message = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, EchoTest message) throws IOException
    {

    	

    	if(message.message == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.message != null)
            output.writeString(1, message.message, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "message";

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

    	__fieldMap.put("message", 1);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = EchoTest.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static EchoTest parseFrom(byte[] bytes) {
	EchoTest message = new EchoTest();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(EchoTest.class));
	return message;
}

public EchoTest clone() {
	byte[] bytes = this.toByteArray();
	EchoTest echoTest = EchoTest.parseFrom(bytes);
	return echoTest;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, EchoTest.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(EchoTest.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
