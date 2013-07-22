
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

public final class Target  implements Externalizable, Message<Target>, Schema<Target>
{




    public static Schema<Target> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Target getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Target DEFAULT_INSTANCE = new Target();



    public Long x;



    public Long y;


    


    public Target()
    {
        
    }






    

	public Long getX() {
		return x;
	}
	
	public Target setX(Long x) {
		this.x = x;
		return this;
	}
	
	public Boolean hasX()  {
        return x == null ? false : true;
    }



    

	public Long getY() {
		return y;
	}
	
	public Target setY(Long y) {
		this.y = y;
		return this;
	}
	
	public Boolean hasY()  {
        return y == null ? false : true;
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

    public Schema<Target> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Target newMessage()
    {
        return new Target();
    }

    public Class<Target> typeClass()
    {
        return Target.class;
    }

    public String messageName()
    {
        return Target.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Target.class.getName();
    }

    public boolean isInitialized(Target message)
    {
        return true;
    }

    public void mergeFrom(Input input, Target message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.x = input.readUInt64();
                	break;

                	


            	case 2:


                	message.y = input.readUInt64();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Target message) throws IOException
    {

    	

    	if(message.x == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.x != null)
            output.writeUInt64(1, message.x, false);

    	


    	

    	if(message.y == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.y != null)
            output.writeUInt64(2, message.y, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "x";

        	case 2: return "y";

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

    	__fieldMap.put("x", 1);

    	__fieldMap.put("y", 2);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Target.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Target parseFrom(byte[] bytes) {
	Target message = new Target();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Target.class));
	return message;
}

public Target clone() {
	byte[] bytes = this.toByteArray();
	Target target = Target.parseFrom(bytes);
	return target;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Target.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Target.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
