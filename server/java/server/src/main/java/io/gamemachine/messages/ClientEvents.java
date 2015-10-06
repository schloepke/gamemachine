
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
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class ClientEvents implements Externalizable, Message<ClientEvents>, Schema<ClientEvents>{

private static final Logger logger = LoggerFactory.getLogger(ClientEvents.class);



    public static Schema<ClientEvents> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientEvents getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientEvents DEFAULT_INSTANCE = new ClientEvents();
    static final String defaultScope = ClientEvents.class.getSimpleName();

        public List<ClientEvent> clientEvent;
	    


    public ClientEvents()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static ClientEvents fromModel(Model model) {
		boolean hasFields = false;
    	ClientEvents message = new ClientEvents();
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public List<ClientEvent> getClientEventList() {
		if(this.clientEvent == null)
            this.clientEvent = new ArrayList<ClientEvent>();
		return clientEvent;
	}

	public ClientEvents setClientEventList(List<ClientEvent> clientEvent) {
		this.clientEvent = clientEvent;
		return this;
	}

	public ClientEvent getClientEvent(int index)  {
        return clientEvent == null ? null : clientEvent.get(index);
    }

    public int getClientEventCount()  {
        return clientEvent == null ? 0 : clientEvent.size();
    }

    public ClientEvents addClientEvent(ClientEvent clientEvent)  {
        if(this.clientEvent == null)
            this.clientEvent = new ArrayList<ClientEvent>();
        this.clientEvent.add(clientEvent);
        return this;
    }
            	    	    	    	
    public ClientEvents removeClientEventByEvent(ClientEvent clientEvent)  {
    	if(this.clientEvent == null)
           return this;
            
       	Iterator<ClientEvent> itr = this.clientEvent.iterator();
       	while (itr.hasNext()) {
    	ClientEvent obj = itr.next();
    	
    	    		if (clientEvent.event.equals(obj.event)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ClientEvents removeClientEventByClientId(ClientEvent clientEvent)  {
    	if(this.clientEvent == null)
           return this;
            
       	Iterator<ClientEvent> itr = this.clientEvent.iterator();
       	while (itr.hasNext()) {
    	ClientEvent obj = itr.next();
    	
    	    		if (clientEvent.clientId.equals(obj.clientId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ClientEvents removeClientEventBySenderId(ClientEvent clientEvent)  {
    	if(this.clientEvent == null)
           return this;
            
       	Iterator<ClientEvent> itr = this.clientEvent.iterator();
       	while (itr.hasNext()) {
    	ClientEvent obj = itr.next();
    	
    	    		if (clientEvent.senderId.equals(obj.senderId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ClientEvents removeClientEventByPlayerId(ClientEvent clientEvent)  {
    	if(this.clientEvent == null)
           return this;
            
       	Iterator<ClientEvent> itr = this.clientEvent.iterator();
       	while (itr.hasNext()) {
    	ClientEvent obj = itr.next();
    	
    	    		if (clientEvent.playerId.equals(obj.playerId)) {
    	      			itr.remove();
    		}
		}
        return this;
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

    public Schema<ClientEvents> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientEvents newMessage()
    {
        return new ClientEvents();
    }

    public Class<ClientEvents> typeClass()
    {
        return ClientEvents.class;
    }

    public String messageName()
    {
        return ClientEvents.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientEvents.class.getName();
    }

    public boolean isInitialized(ClientEvents message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientEvents message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.clientEvent == null)
                        message.clientEvent = new ArrayList<ClientEvent>();
                                        message.clientEvent.add(input.mergeObject(null, ClientEvent.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ClientEvents message) throws IOException
    {
    	    	
    	    	
    	    	if(message.clientEvent != null)
        {
            for(ClientEvent clientEvent : message.clientEvent)
            {
                if( (ClientEvent) clientEvent != null) {
                   	    				output.writeObject(1, clientEvent, ClientEvent.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ClientEvents");
    	    	//if(this.clientEvent != null) {
    		System.out.println("clientEvent="+this.clientEvent);
    	//}
    	    	System.out.println("END ClientEvents");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "clientEvent";
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
    	    	__fieldMap.put("clientEvent", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientEvents.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientEvents parseFrom(byte[] bytes) {
	ClientEvents message = new ClientEvents();
	ProtobufIOUtil.mergeFrom(bytes, message, ClientEvents.getSchema());
	return message;
}

public static ClientEvents parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ClientEvents message = new ClientEvents();
	JsonIOUtil.mergeFrom(bytes, message, ClientEvents.getSchema(), false);
	return message;
}

public ClientEvents clone() {
	byte[] bytes = this.toByteArray();
	ClientEvents clientEvents = ClientEvents.parseFrom(bytes);
	return clientEvents;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientEvents.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientEvents> schema = ClientEvents.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ClientEvents.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ClientEvents.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
