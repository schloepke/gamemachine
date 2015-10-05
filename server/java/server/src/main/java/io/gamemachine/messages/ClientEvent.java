
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
public final class ClientEvent implements Externalizable, Message<ClientEvent>, Schema<ClientEvent>{



    public static Schema<ClientEvent> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientEvent getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientEvent DEFAULT_INSTANCE = new ClientEvent();
    static final String defaultScope = ClientEvent.class.getSimpleName();

    	
							    public String event= null;
		    			    
		
    
        	
							    public String clientId= null;
		    			    
		
    
        	
							    public String senderId= null;
		    			    
		
    
        	
							    public String playerId= null;
		    			    
		
    
        


    public ClientEvent()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("client_event_event",null);
    	    	    	    	    	    	model.set("client_event_client_id",null);
    	    	    	    	    	    	model.set("client_event_sender_id",null);
    	    	    	    	    	    	model.set("client_event_player_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (event != null) {
    	       	    	model.setString("client_event_event",event);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (clientId != null) {
    	       	    	model.setString("client_event_client_id",clientId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (senderId != null) {
    	       	    	model.setString("client_event_sender_id",senderId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("client_event_player_id",playerId);
    	        		
    	//}
    	    	    }
    
	public static ClientEvent fromModel(Model model) {
		boolean hasFields = false;
    	ClientEvent message = new ClientEvent();
    	    	    	    	    	
    	    	    	String eventTestField = model.getString("client_event_event");
    	if (eventTestField != null) {
    		String eventField = eventTestField;
    		message.setEvent(eventField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String clientIdTestField = model.getString("client_event_client_id");
    	if (clientIdTestField != null) {
    		String clientIdField = clientIdTestField;
    		message.setClientId(clientIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String senderIdTestField = model.getString("client_event_sender_id");
    	if (senderIdTestField != null) {
    		String senderIdField = senderIdTestField;
    		message.setSenderId(senderIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String playerIdTestField = model.getString("client_event_player_id");
    	if (playerIdTestField != null) {
    		String playerIdField = playerIdTestField;
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getEvent() {
		return event;
	}
	
	public ClientEvent setEvent(String event) {
		this.event = event;
		return this;	}
	
		            
		public String getClientId() {
		return clientId;
	}
	
	public ClientEvent setClientId(String clientId) {
		this.clientId = clientId;
		return this;	}
	
		            
		public String getSenderId() {
		return senderId;
	}
	
	public ClientEvent setSenderId(String senderId) {
		this.senderId = senderId;
		return this;	}
	
		            
		public String getPlayerId() {
		return playerId;
	}
	
	public ClientEvent setPlayerId(String playerId) {
		this.playerId = playerId;
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

    public Schema<ClientEvent> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientEvent newMessage()
    {
        return new ClientEvent();
    }

    public Class<ClientEvent> typeClass()
    {
        return ClientEvent.class;
    }

    public String messageName()
    {
        return ClientEvent.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientEvent.class.getName();
    }

    public boolean isInitialized(ClientEvent message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientEvent message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.event = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.clientId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.senderId = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ClientEvent message) throws IOException
    {
    	    	
    	    	//if(message.event == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.event != null) {
            output.writeString(1, message.event, false);
        }
    	    	
    	            	
    	    	//if(message.clientId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.clientId != null) {
            output.writeString(2, message.clientId, false);
        }
    	    	
    	            	
    	    	//if(message.senderId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.senderId != null) {
            output.writeString(3, message.senderId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(4, message.playerId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ClientEvent");
    	    	//if(this.event != null) {
    		System.out.println("event="+this.event);
    	//}
    	    	//if(this.clientId != null) {
    		System.out.println("clientId="+this.clientId);
    	//}
    	    	//if(this.senderId != null) {
    		System.out.println("senderId="+this.senderId);
    	//}
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	System.out.println("END ClientEvent");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "event";
        	        	case 2: return "clientId";
        	        	case 3: return "senderId";
        	        	case 4: return "playerId";
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
    	    	__fieldMap.put("event", 1);
    	    	__fieldMap.put("clientId", 2);
    	    	__fieldMap.put("senderId", 3);
    	    	__fieldMap.put("playerId", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientEvent.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientEvent parseFrom(byte[] bytes) {
	ClientEvent message = new ClientEvent();
	ProtobufIOUtil.mergeFrom(bytes, message, ClientEvent.getSchema());
	return message;
}

public static ClientEvent parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ClientEvent message = new ClientEvent();
	JsonIOUtil.mergeFrom(bytes, message, ClientEvent.getSchema(), false);
	return message;
}

public ClientEvent clone() {
	byte[] bytes = this.toByteArray();
	ClientEvent clientEvent = ClientEvent.parseFrom(bytes);
	return clientEvent;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientEvent.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientEvent> schema = ClientEvent.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ClientEvent.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ClientEvent.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
