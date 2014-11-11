
package io.gamemachine.client.messages;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class ClientManagerEvent implements Externalizable, Message<ClientManagerEvent>, Schema<ClientManagerEvent>{



    public static Schema<ClientManagerEvent> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientManagerEvent getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientManagerEvent DEFAULT_INSTANCE = new ClientManagerEvent();

    			public String client_id;
	    
        			public String player_id;
	    
        			public String event;
	    
      
    public ClientManagerEvent()
    {
        
    }


	

	    
    public Boolean hasClient_id()  {
        return client_id == null ? false : true;
    }
        
		public String getClient_id() {
		return client_id;
	}
	
	public ClientManagerEvent setClient_id(String client_id) {
		this.client_id = client_id;
		return this;	}
	
		    
    public Boolean hasPlayer_id()  {
        return player_id == null ? false : true;
    }
        
		public String getPlayer_id() {
		return player_id;
	}
	
	public ClientManagerEvent setPlayer_id(String player_id) {
		this.player_id = player_id;
		return this;	}
	
		    
    public Boolean hasEvent()  {
        return event == null ? false : true;
    }
        
		public String getEvent() {
		return event;
	}
	
	public ClientManagerEvent setEvent(String event) {
		this.event = event;
		return this;	}
	
	
  
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

    public Schema<ClientManagerEvent> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientManagerEvent newMessage()
    {
        return new ClientManagerEvent();
    }

    public Class<ClientManagerEvent> typeClass()
    {
        return ClientManagerEvent.class;
    }

    public String messageName()
    {
        return ClientManagerEvent.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientManagerEvent.class.getName();
    }

    public boolean isInitialized(ClientManagerEvent message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientManagerEvent message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.client_id = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.player_id = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.event = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ClientManagerEvent message) throws IOException
    {
    	    	
    	    	if(message.client_id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.client_id != null)
            output.writeString(1, message.client_id, false);
    	    	
    	            	
    	    	if(message.player_id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.player_id != null)
            output.writeString(2, message.player_id, false);
    	    	
    	            	
    	    	if(message.event == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.event != null)
            output.writeString(3, message.event, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "client_id";
        	        	case 2: return "player_id";
        	        	case 3: return "event";
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
    	    	__fieldMap.put("client_id", 1);
    	    	__fieldMap.put("player_id", 2);
    	    	__fieldMap.put("event", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientManagerEvent.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientManagerEvent parseFrom(byte[] bytes) {
	ClientManagerEvent message = new ClientManagerEvent();
	ProtobufIOUtil.mergeFrom(bytes, message, ClientManagerEvent.getSchema());
	return message;
}

public static ClientManagerEvent parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ClientManagerEvent message = new ClientManagerEvent();
	JsonIOUtil.mergeFrom(bytes, message, ClientManagerEvent.getSchema(), false);
	return message;
}

public ClientManagerEvent clone() {
	byte[] bytes = this.toByteArray();
	ClientManagerEvent clientManagerEvent = ClientManagerEvent.parseFrom(bytes);
	return clientManagerEvent;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientManagerEvent.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientManagerEvent> schema = ClientManagerEvent.getSchema();
    
	final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ProtobufOutput output = new ProtobufOutput(buffer);
    try
    {
    	schema.writeTo(output, this);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        assert size == msgSize;
        
        buffer.clear();
        return out.toByteArray();
    }
    catch (IOException e)
    {
        throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                "(should never happen).", e);
    }
 
}

public byte[] toProtobuf() {
	LinkedBuffer buffer = LocalLinkedBuffer.get();
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, ClientManagerEvent.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
