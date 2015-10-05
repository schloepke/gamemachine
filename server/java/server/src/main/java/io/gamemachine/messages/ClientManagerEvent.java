
package io.gamemachine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;





import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



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
    static final String defaultScope = ClientManagerEvent.class.getSimpleName();

    	
	    	    public String client_id= null;
	    		
    
        	
	    	    public String player_id= null;
	    		
    
        	
	    	    public String event= null;
	    		
    
        


    public ClientManagerEvent()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("client_manager_event_client_id",null);
    	    	    	    	    	    	model.set("client_manager_event_player_id",null);
    	    	    	    	    	    	model.set("client_manager_event_event",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (client_id != null) {
    	       	    	model.setString("client_manager_event_client_id",client_id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (player_id != null) {
    	       	    	model.setString("client_manager_event_player_id",player_id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (event != null) {
    	       	    	model.setString("client_manager_event_event",event);
    	        		
    	//}
    	    	    }
    
	public static ClientManagerEvent fromModel(Model model) {
		boolean hasFields = false;
    	ClientManagerEvent message = new ClientManagerEvent();
    	    	    	    	    	
    	    	    	String client_idTestField = model.getString("client_manager_event_client_id");
    	if (client_idTestField != null) {
    		String client_idField = client_idTestField;
    		message.setClient_id(client_idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String player_idTestField = model.getString("client_manager_event_player_id");
    	if (player_idTestField != null) {
    		String player_idField = player_idTestField;
    		message.setPlayer_id(player_idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String eventTestField = model.getString("client_manager_event_event");
    	if (eventTestField != null) {
    		String eventField = eventTestField;
    		message.setEvent(eventField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getClient_id() {
		return client_id;
	}
	
	public ClientManagerEvent setClient_id(String client_id) {
		this.client_id = client_id;
		return this;	}
	
		            
		public String getPlayer_id() {
		return player_id;
	}
	
	public ClientManagerEvent setPlayer_id(String player_id) {
		this.player_id = player_id;
		return this;	}
	
		            
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
    	    	
    	    	//if(message.client_id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.client_id != null) {
            output.writeString(1, message.client_id, false);
        }
    	    	
    	            	
    	    	//if(message.player_id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.player_id != null) {
            output.writeString(2, message.player_id, false);
        }
    	    	
    	            	
    	    	//if(message.event == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.event != null) {
            output.writeString(3, message.event, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ClientManagerEvent");
    	    	//if(this.client_id != null) {
    		System.out.println("client_id="+this.client_id);
    	//}
    	    	//if(this.player_id != null) {
    		System.out.println("player_id="+this.player_id);
    	//}
    	    	//if(this.event != null) {
    		System.out.println("event="+this.event);
    	//}
    	    	System.out.println("END ClientManagerEvent");
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
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, ClientManagerEvent.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
