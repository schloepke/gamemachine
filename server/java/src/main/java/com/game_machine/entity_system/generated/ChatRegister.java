
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

public final class ChatRegister  implements Externalizable, Message<ChatRegister>, Schema<ChatRegister>
{




    public static Schema<ChatRegister> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatRegister getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatRegister DEFAULT_INSTANCE = new ChatRegister();


    


    public ChatRegister()
    {
        
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

    public Schema<ChatRegister> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatRegister newMessage()
    {
        return new ChatRegister();
    }

    public Class<ChatRegister> typeClass()
    {
        return ChatRegister.class;
    }

    public String messageName()
    {
        return ChatRegister.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatRegister.class.getName();
    }

    public boolean isInitialized(ChatRegister message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatRegister message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ChatRegister message) throws IOException
    {

    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

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

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatRegister.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatRegister parseFrom(byte[] bytes) {
	ChatRegister message = new ChatRegister();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ChatRegister.class));
	return message;
}

public ChatRegister clone() {
	byte[] bytes = this.toByteArray();
	ChatRegister chatRegister = ChatRegister.parseFrom(bytes);
	return chatRegister;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatRegister.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ChatRegister.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
