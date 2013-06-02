
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
import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.Component;

import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

public final class ChatMessage extends com.game_machine.entity_system.Component implements Externalizable, Message<ChatMessage>, Schema<ChatMessage>
{




    public static Schema<ChatMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ChatMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ChatMessage DEFAULT_INSTANCE = new ChatMessage();



    private String text;



    private String target;



    private Integer entityId;


    

    public ChatMessage()
    {
        
    }




	public String getText() {
		return text;
	}
	
	public ChatMessage setText(String text) {
		this.text = text;
		return this;
	}




	public String getTarget() {
		return target;
	}
	
	public ChatMessage setTarget(String target) {
		this.target = target;
		return this;
	}




	public Integer getEntityId() {
		return entityId;
	}
	
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
		
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

    public Schema<ChatMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ChatMessage newMessage()
    {
        return new ChatMessage();
    }

    public Class<ChatMessage> typeClass()
    {
        return ChatMessage.class;
    }

    public String messageName()
    {
        return ChatMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ChatMessage.class.getName();
    }

    public boolean isInitialized(ChatMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, ChatMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.text = input.readString();
                	break;

                	


            	case 2:


                	message.target = input.readString();
                	break;

                	


            	case 3:


                	message.entityId = input.readInt32();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ChatMessage message) throws IOException
    {

    	

    	if(message.text == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.text != null)
            output.writeString(1, message.text, false);

    	


    	

    	if(message.target == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.target != null)
            output.writeString(2, message.target, false);

    	


    	

    	


    	if(message.entityId != null)
            output.writeInt32(3, message.entityId, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "text";

        	case 2: return "target";

        	case 3: return "entityId";

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

    	__fieldMap.put("text", 1);

    	__fieldMap.put("target", 2);

    	__fieldMap.put("entityId", 3);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ChatMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ChatMessage parseFrom(byte[] bytes) {
	ChatMessage message = new ChatMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ChatMessage.class));
	return message;
}

public ChatMessage clone() {
	byte[] bytes = this.toByteArray();
	ChatMessage chatMessage = ChatMessage.parseFrom(bytes);
	chatMessage.setEntityId(null);
	return chatMessage;
}
	
public byte[] toByteArray() {
	if (Entities.encoding.equals("protobuf")) {
		return toProtobuf();
	} else if (Entities.encoding.equals("json")) {
		return toJson();
	} else {
		throw new RuntimeException("No encoding specified");
	}
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ChatMessage.getSchema(), numeric);
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ChatMessage.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

 
    

}
