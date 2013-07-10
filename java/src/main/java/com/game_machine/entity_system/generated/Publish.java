
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

public final class Publish  implements Externalizable, Message<Publish>, Schema<Publish>
{




    public static Schema<Publish> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Publish getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Publish DEFAULT_INSTANCE = new Publish();



    public String topic;



    public Entity message;


    


    public Publish()
    {
        
    }






    

	public String getTopic() {
		return topic;
	}
	
	public Publish setTopic(String topic) {
		this.topic = topic;
		return this;
	}
	
	public Boolean hasTopic()  {
        return topic == null ? false : true;
    }



    

	public Entity getMessage() {
		return message;
	}
	
	public Publish setMessage(Entity message) {
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

    public Schema<Publish> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Publish newMessage()
    {
        return new Publish();
    }

    public Class<Publish> typeClass()
    {
        return Publish.class;
    }

    public String messageName()
    {
        return Publish.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Publish.class.getName();
    }

    public boolean isInitialized(Publish message)
    {
        return true;
    }

    public void mergeFrom(Input input, Publish message) throws IOException
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

                	


            	case 2:


                	message.message = input.mergeObject(message.message, Entity.getSchema());
                    break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Publish message) throws IOException
    {

    	

    	if(message.topic == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.topic != null)
            output.writeString(1, message.topic, false);

    	


    	

    	if(message.message == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.message != null)
    		output.writeObject(2, message.message, Entity.getSchema(), false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "topic";

        	case 2: return "message";

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

    	__fieldMap.put("message", 2);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Publish.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Publish parseFrom(byte[] bytes) {
	Publish message = new Publish();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Publish.class));
	return message;
}

public Publish clone() {
	byte[] bytes = this.toByteArray();
	Publish publish = Publish.parseFrom(bytes);
	return publish;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Publish.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Publish.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
