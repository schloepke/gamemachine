
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

public final class Unsubscribe  implements Externalizable, Message<Unsubscribe>, Schema<Unsubscribe>
{




    public static Schema<Unsubscribe> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Unsubscribe getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Unsubscribe DEFAULT_INSTANCE = new Unsubscribe();



    public String topic;


    


    public Unsubscribe()
    {
        
    }






    

	public String getTopic() {
		return topic;
	}
	
	public Unsubscribe setTopic(String topic) {
		this.topic = topic;
		return this;
	}
	
	public Boolean hasTopic()  {
        return topic == null ? false : true;
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

    public Schema<Unsubscribe> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Unsubscribe newMessage()
    {
        return new Unsubscribe();
    }

    public Class<Unsubscribe> typeClass()
    {
        return Unsubscribe.class;
    }

    public String messageName()
    {
        return Unsubscribe.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Unsubscribe.class.getName();
    }

    public boolean isInitialized(Unsubscribe message)
    {
        return true;
    }

    public void mergeFrom(Input input, Unsubscribe message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.topic = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Unsubscribe message) throws IOException
    {

    	

    	if(message.topic == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.topic != null)
            output.writeString(1, message.topic, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "topic";

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

    	__fieldMap.put("topic", 1);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Unsubscribe.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Unsubscribe parseFrom(byte[] bytes) {
	Unsubscribe message = new Unsubscribe();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Unsubscribe.class));
	return message;
}

public Unsubscribe clone() {
	byte[] bytes = this.toByteArray();
	Unsubscribe unsubscribe = Unsubscribe.parseFrom(bytes);
	return unsubscribe;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Unsubscribe.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Unsubscribe.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
