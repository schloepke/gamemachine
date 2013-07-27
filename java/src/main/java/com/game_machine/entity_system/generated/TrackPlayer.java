
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

public final class TrackPlayer  implements Externalizable, Message<TrackPlayer>, Schema<TrackPlayer>
{




    public static Schema<TrackPlayer> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackPlayer getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackPlayer DEFAULT_INSTANCE = new TrackPlayer();



    public Boolean value;


    


    public TrackPlayer()
    {
        
    }






    

	public Boolean getValue() {
		return value;
	}
	
	public TrackPlayer setValue(Boolean value) {
		this.value = value;
		return this;
	}
	
	public Boolean hasValue()  {
        return value == null ? false : true;
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

    public Schema<TrackPlayer> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackPlayer newMessage()
    {
        return new TrackPlayer();
    }

    public Class<TrackPlayer> typeClass()
    {
        return TrackPlayer.class;
    }

    public String messageName()
    {
        return TrackPlayer.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackPlayer.class.getName();
    }

    public boolean isInitialized(TrackPlayer message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackPlayer message) throws IOException
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

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackPlayer message) throws IOException
    {

    	

    	if(message.value == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.value != null)
            output.writeBool(1, message.value, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "value";

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

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackPlayer.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackPlayer parseFrom(byte[] bytes) {
	TrackPlayer message = new TrackPlayer();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(TrackPlayer.class));
	return message;
}

public TrackPlayer clone() {
	byte[] bytes = this.toByteArray();
	TrackPlayer trackPlayer = TrackPlayer.parseFrom(bytes);
	return trackPlayer;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackPlayer.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(TrackPlayer.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
