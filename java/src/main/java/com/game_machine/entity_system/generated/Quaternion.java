
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

public final class Quaternion  implements Externalizable, Message<Quaternion>, Schema<Quaternion>
{




    public static Schema<Quaternion> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Quaternion getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Quaternion DEFAULT_INSTANCE = new Quaternion();



    public Float w;



    public Float x;



    public Float y;



    public Float z;


    


    public Quaternion()
    {
        
    }






    

	public Float getW() {
		return w;
	}
	
	public Quaternion setW(Float w) {
		this.w = w;
		return this;
	}
	
	public Boolean hasW()  {
        return w == null ? false : true;
    }



    

	public Float getX() {
		return x;
	}
	
	public Quaternion setX(Float x) {
		this.x = x;
		return this;
	}
	
	public Boolean hasX()  {
        return x == null ? false : true;
    }



    

	public Float getY() {
		return y;
	}
	
	public Quaternion setY(Float y) {
		this.y = y;
		return this;
	}
	
	public Boolean hasY()  {
        return y == null ? false : true;
    }



    

	public Float getZ() {
		return z;
	}
	
	public Quaternion setZ(Float z) {
		this.z = z;
		return this;
	}
	
	public Boolean hasZ()  {
        return z == null ? false : true;
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

    public Schema<Quaternion> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Quaternion newMessage()
    {
        return new Quaternion();
    }

    public Class<Quaternion> typeClass()
    {
        return Quaternion.class;
    }

    public String messageName()
    {
        return Quaternion.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Quaternion.class.getName();
    }

    public boolean isInitialized(Quaternion message)
    {
        return true;
    }

    public void mergeFrom(Input input, Quaternion message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.w = input.readFloat();
                	break;

                	


            	case 2:


                	message.x = input.readFloat();
                	break;

                	


            	case 3:


                	message.y = input.readFloat();
                	break;

                	


            	case 4:


                	message.z = input.readFloat();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Quaternion message) throws IOException
    {

    	

    	


    	if(message.w != null)
            output.writeFloat(1, message.w, false);

    	


    	

    	


    	if(message.x != null)
            output.writeFloat(2, message.x, false);

    	


    	

    	


    	if(message.y != null)
            output.writeFloat(3, message.y, false);

    	


    	

    	


    	if(message.z != null)
            output.writeFloat(4, message.z, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "w";

        	case 2: return "x";

        	case 3: return "y";

        	case 4: return "z";

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

    	__fieldMap.put("w", 1);

    	__fieldMap.put("x", 2);

    	__fieldMap.put("y", 3);

    	__fieldMap.put("z", 4);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Quaternion.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Quaternion parseFrom(byte[] bytes) {
	Quaternion message = new Quaternion();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Quaternion.class));
	return message;
}

public Quaternion clone() {
	byte[] bytes = this.toByteArray();
	Quaternion quaternion = Quaternion.parseFrom(bytes);
	return quaternion;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Quaternion.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Quaternion.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
