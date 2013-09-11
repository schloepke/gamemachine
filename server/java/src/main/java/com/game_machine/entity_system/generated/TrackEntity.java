
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

public final class TrackEntity  implements Externalizable, Message<TrackEntity>, Schema<TrackEntity>
{




    public static Schema<TrackEntity> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackEntity getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackEntity DEFAULT_INSTANCE = new TrackEntity();



    public Boolean value;



    public Boolean internal;


    


    public TrackEntity()
    {
        
    }






    

	public Boolean getValue() {
		return value;
	}
	
	public TrackEntity setValue(Boolean value) {
		this.value = value;
		return this;
	}
	
	public Boolean hasValue()  {
        return value == null ? false : true;
    }



    

	public Boolean getInternal() {
		return internal;
	}
	
	public TrackEntity setInternal(Boolean internal) {
		this.internal = internal;
		return this;
	}
	
	public Boolean hasInternal()  {
        return internal == null ? false : true;
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

    public Schema<TrackEntity> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackEntity newMessage()
    {
        return new TrackEntity();
    }

    public Class<TrackEntity> typeClass()
    {
        return TrackEntity.class;
    }

    public String messageName()
    {
        return TrackEntity.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackEntity.class.getName();
    }

    public boolean isInitialized(TrackEntity message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackEntity message) throws IOException
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

                	


            	case 2:


                	message.internal = input.readBool();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackEntity message) throws IOException
    {

    	

    	if(message.value == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.value != null)
            output.writeBool(1, message.value, false);

    	


    	

    	


    	if(message.internal != null)
            output.writeBool(2, message.internal, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "value";

        	case 2: return "internal";

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

    	__fieldMap.put("internal", 2);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackEntity.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackEntity parseFrom(byte[] bytes) {
	TrackEntity message = new TrackEntity();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(TrackEntity.class));
	return message;
}

public TrackEntity clone() {
	byte[] bytes = this.toByteArray();
	TrackEntity trackEntity = TrackEntity.parseFrom(bytes);
	return trackEntity;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackEntity.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(TrackEntity.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
