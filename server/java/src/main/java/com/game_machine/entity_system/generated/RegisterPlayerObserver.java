
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

public final class RegisterPlayerObserver  implements Externalizable, Message<RegisterPlayerObserver>, Schema<RegisterPlayerObserver>
{




    public static Schema<RegisterPlayerObserver> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static RegisterPlayerObserver getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final RegisterPlayerObserver DEFAULT_INSTANCE = new RegisterPlayerObserver();



    public String playerId;



    public String event;


    


    public RegisterPlayerObserver()
    {
        
    }






    

	public String getPlayerId() {
		return playerId;
	}
	
	public RegisterPlayerObserver setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;
	}
	
	public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }



    

	public String getEvent() {
		return event;
	}
	
	public RegisterPlayerObserver setEvent(String event) {
		this.event = event;
		return this;
	}
	
	public Boolean hasEvent()  {
        return event == null ? false : true;
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

    public Schema<RegisterPlayerObserver> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public RegisterPlayerObserver newMessage()
    {
        return new RegisterPlayerObserver();
    }

    public Class<RegisterPlayerObserver> typeClass()
    {
        return RegisterPlayerObserver.class;
    }

    public String messageName()
    {
        return RegisterPlayerObserver.class.getSimpleName();
    }

    public String messageFullName()
    {
        return RegisterPlayerObserver.class.getName();
    }

    public boolean isInitialized(RegisterPlayerObserver message)
    {
        return true;
    }

    public void mergeFrom(Input input, RegisterPlayerObserver message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.playerId = input.readString();
                	break;

                	


            	case 2:


                	message.event = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, RegisterPlayerObserver message) throws IOException
    {

    	

    	if(message.playerId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.playerId != null)
            output.writeString(1, message.playerId, false);

    	


    	

    	if(message.event == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.event != null)
            output.writeString(2, message.event, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "playerId";

        	case 2: return "event";

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

    	__fieldMap.put("playerId", 1);

    	__fieldMap.put("event", 2);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = RegisterPlayerObserver.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static RegisterPlayerObserver parseFrom(byte[] bytes) {
	RegisterPlayerObserver message = new RegisterPlayerObserver();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(RegisterPlayerObserver.class));
	return message;
}

public RegisterPlayerObserver clone() {
	byte[] bytes = this.toByteArray();
	RegisterPlayerObserver registerPlayerObserver = RegisterPlayerObserver.parseFrom(bytes);
	return registerPlayerObserver;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, RegisterPlayerObserver.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(RegisterPlayerObserver.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
