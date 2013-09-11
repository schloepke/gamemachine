
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

public final class Disconnected  implements Externalizable, Message<Disconnected>, Schema<Disconnected>
{




    public static Schema<Disconnected> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Disconnected getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Disconnected DEFAULT_INSTANCE = new Disconnected();



    public String playerId;



    public String clientId;


    


    public Disconnected()
    {
        
    }






    

	public String getPlayerId() {
		return playerId;
	}
	
	public Disconnected setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;
	}
	
	public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }



    

	public String getClientId() {
		return clientId;
	}
	
	public Disconnected setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}
	
	public Boolean hasClientId()  {
        return clientId == null ? false : true;
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

    public Schema<Disconnected> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Disconnected newMessage()
    {
        return new Disconnected();
    }

    public Class<Disconnected> typeClass()
    {
        return Disconnected.class;
    }

    public String messageName()
    {
        return Disconnected.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Disconnected.class.getName();
    }

    public boolean isInitialized(Disconnected message)
    {
        return true;
    }

    public void mergeFrom(Input input, Disconnected message) throws IOException
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


                	message.clientId = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Disconnected message) throws IOException
    {

    	

    	if(message.playerId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.playerId != null)
            output.writeString(1, message.playerId, false);

    	


    	

    	if(message.clientId == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.clientId != null)
            output.writeString(2, message.clientId, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "playerId";

        	case 2: return "clientId";

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

    	__fieldMap.put("clientId", 2);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Disconnected.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Disconnected parseFrom(byte[] bytes) {
	Disconnected message = new Disconnected();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Disconnected.class));
	return message;
}

public Disconnected clone() {
	byte[] bytes = this.toByteArray();
	Disconnected disconnected = Disconnected.parseFrom(bytes);
	return disconnected;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Disconnected.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Disconnected.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
