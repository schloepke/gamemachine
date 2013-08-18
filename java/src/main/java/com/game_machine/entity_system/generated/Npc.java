
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

public final class Npc  implements Externalizable, Message<Npc>, Schema<Npc>
{




    public static Schema<Npc> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Npc getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Npc DEFAULT_INSTANCE = new Npc();



    public String id;



    public String name;


    


    public Npc()
    {
        
    }






    

	public String getId() {
		return id;
	}
	
	public Npc setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }



    

	public String getName() {
		return name;
	}
	
	public Npc setName(String name) {
		this.name = name;
		return this;
	}
	
	public Boolean hasName()  {
        return name == null ? false : true;
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

    public Schema<Npc> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Npc newMessage()
    {
        return new Npc();
    }

    public Class<Npc> typeClass()
    {
        return Npc.class;
    }

    public String messageName()
    {
        return Npc.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Npc.class.getName();
    }

    public boolean isInitialized(Npc message)
    {
        return true;
    }

    public void mergeFrom(Input input, Npc message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.id = input.readString();
                	break;

                	


            	case 2:


                	message.name = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Npc message) throws IOException
    {

    	

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.id != null)
            output.writeString(1, message.id, false);

    	


    	

    	


    	if(message.name != null)
            output.writeString(2, message.name, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "id";

        	case 2: return "name";

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

    	__fieldMap.put("id", 1);

    	__fieldMap.put("name", 2);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Npc.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Npc parseFrom(byte[] bytes) {
	Npc message = new Npc();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Npc.class));
	return message;
}

public Npc clone() {
	byte[] bytes = this.toByteArray();
	Npc npc = Npc.parseFrom(bytes);
	return npc;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Npc.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Npc.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
