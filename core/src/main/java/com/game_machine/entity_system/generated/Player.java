
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

public final class Player  implements Externalizable, Message<Player>, Schema<Player>
{




    public static Schema<Player> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Player getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Player DEFAULT_INSTANCE = new Player();



    private Integer x;



    private Integer y;



    private Integer z;



    private String id;



    private String name;


    


    public Player()
    {
        
    }







	
    

	public Integer getX() {
		return x;
	}
	
	public Player setX(Integer x) {
		this.x = x;
		return this;
	}
	
	public Boolean hasX()  {
        return x == null ? false : true;
    }




	
    

	public Integer getY() {
		return y;
	}
	
	public Player setY(Integer y) {
		this.y = y;
		return this;
	}
	
	public Boolean hasY()  {
        return y == null ? false : true;
    }




	
    

	public Integer getZ() {
		return z;
	}
	
	public Player setZ(Integer z) {
		this.z = z;
		return this;
	}
	
	public Boolean hasZ()  {
        return z == null ? false : true;
    }




	
    

	public String getId() {
		return id;
	}
	
	public Player setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }




	
    

	public String getName() {
		return name;
	}
	
	public Player setName(String name) {
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

    public Schema<Player> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Player newMessage()
    {
        return new Player();
    }

    public Class<Player> typeClass()
    {
        return Player.class;
    }

    public String messageName()
    {
        return Player.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Player.class.getName();
    }

    public boolean isInitialized(Player message)
    {
        return true;
    }

    public void mergeFrom(Input input, Player message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.x = input.readInt32();
                	break;

                	


            	case 2:


                	message.y = input.readInt32();
                	break;

                	


            	case 3:


                	message.z = input.readInt32();
                	break;

                	


            	case 4:


                	message.id = input.readString();
                	break;

                	


            	case 5:


                	message.name = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Player message) throws IOException
    {

    	

    	


    	if(message.x != null)
            output.writeInt32(1, message.x, false);

    	


    	

    	


    	if(message.y != null)
            output.writeInt32(2, message.y, false);

    	


    	

    	


    	if(message.z != null)
            output.writeInt32(3, message.z, false);

    	


    	

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.id != null)
            output.writeString(4, message.id, false);

    	


    	

    	


    	if(message.name != null)
            output.writeString(5, message.name, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "x";

        	case 2: return "y";

        	case 3: return "z";

        	case 4: return "id";

        	case 5: return "name";

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

    	__fieldMap.put("z", 3);

    	__fieldMap.put("id", 4);

    	__fieldMap.put("name", 5);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Player.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Player parseFrom(byte[] bytes) {
	Player message = new Player();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Player.class));
	return message;
}

public Player clone() {
	byte[] bytes = this.toByteArray();
	Player player = Player.parseFrom(bytes);
	return player;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Player.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}
		
public byte[] toProtobuf() {
	LinkedBuffer buffer = LinkedBuffer.allocate(1024);
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Player.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
