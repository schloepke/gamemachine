
package GameMachine.Messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import GameMachine.Messages.Entity;
import com.game_machine.core.LocalLinkedBuffer;

import org.javalite.activejdbc.Model;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

public final class ClientEvents  implements Externalizable, Message<ClientEvents>, Schema<ClientEvents>

{

    public static Schema<ClientEvents> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientEvents getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientEvents DEFAULT_INSTANCE = new ClientEvents();

    public List<ClientEvent> clientEvent;

    public ClientEvents()
    {
        
    }

	public static void clearModel(Model model) {

    }
    
	public void toModel(Model model, String playerId) {

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
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
                if(clientEvent != null) {
                   	
    				output.writeObject(1, clientEvent, ClientEvent.getSchema(), true);
    				
    			}
            }
        }

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
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ClientEvents.class));
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

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientEvents.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientEvents> schema = RuntimeSchema.getSchema(ClientEvents.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(ClientEvents.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(ClientEvents.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
