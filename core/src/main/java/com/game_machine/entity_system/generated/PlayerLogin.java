
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

public final class PlayerLogin  implements Externalizable, Message<PlayerLogin>, Schema<PlayerLogin>
{




    public static Schema<PlayerLogin> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerLogin getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerLogin DEFAULT_INSTANCE = new PlayerLogin();



    public String username;



    public String password;


    


    public PlayerLogin()
    {
        
    }






    

	public String getUsername() {
		return username;
	}
	
	public PlayerLogin setUsername(String username) {
		this.username = username;
		return this;
	}
	
	public Boolean hasUsername()  {
        return username == null ? false : true;
    }



    

	public String getPassword() {
		return password;
	}
	
	public PlayerLogin setPassword(String password) {
		this.password = password;
		return this;
	}
	
	public Boolean hasPassword()  {
        return password == null ? false : true;
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

    public Schema<PlayerLogin> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerLogin newMessage()
    {
        return new PlayerLogin();
    }

    public Class<PlayerLogin> typeClass()
    {
        return PlayerLogin.class;
    }

    public String messageName()
    {
        return PlayerLogin.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerLogin.class.getName();
    }

    public boolean isInitialized(PlayerLogin message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerLogin message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.username = input.readString();
                	break;

                	


            	case 2:


                	message.password = input.readString();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerLogin message) throws IOException
    {

    	

    	if(message.username == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.username != null)
            output.writeString(1, message.username, false);

    	


    	

    	if(message.password == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.password != null)
            output.writeString(2, message.password, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "username";

        	case 2: return "password";

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

    	__fieldMap.put("username", 1);

    	__fieldMap.put("password", 2);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerLogin.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerLogin parseFrom(byte[] bytes) {
	PlayerLogin message = new PlayerLogin();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(PlayerLogin.class));
	return message;
}

public PlayerLogin clone() {
	byte[] bytes = this.toByteArray();
	PlayerLogin playerLogin = PlayerLogin.parseFrom(bytes);
	return playerLogin;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerLogin.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(PlayerLogin.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
