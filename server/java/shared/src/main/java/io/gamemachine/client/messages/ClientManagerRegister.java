
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
public final class ClientManagerRegister implements Externalizable, Message<ClientManagerRegister>, Schema<ClientManagerRegister>{



    public static Schema<ClientManagerRegister> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientManagerRegister getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientManagerRegister DEFAULT_INSTANCE = new ClientManagerRegister();

    			public String events;
	    
        			public String registerType;
	    
        			public String name;
	    
      
    public ClientManagerRegister()
    {
        
    }


	

	    
    public Boolean hasEvents()  {
        return events == null ? false : true;
    }
        
		public String getEvents() {
		return events;
	}
	
	public ClientManagerRegister setEvents(String events) {
		this.events = events;
		return this;	}
	
		    
    public Boolean hasRegisterType()  {
        return registerType == null ? false : true;
    }
        
		public String getRegisterType() {
		return registerType;
	}
	
	public ClientManagerRegister setRegisterType(String registerType) {
		this.registerType = registerType;
		return this;	}
	
		    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public ClientManagerRegister setName(String name) {
		this.name = name;
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

    public Schema<ClientManagerRegister> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientManagerRegister newMessage()
    {
        return new ClientManagerRegister();
    }

    public Class<ClientManagerRegister> typeClass()
    {
        return ClientManagerRegister.class;
    }

    public String messageName()
    {
        return ClientManagerRegister.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientManagerRegister.class.getName();
    }

    public boolean isInitialized(ClientManagerRegister message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientManagerRegister message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.events = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.registerType = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ClientManagerRegister message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.events != null)
            output.writeString(1, message.events, false);
    	    	
    	            	
    	    	
    	    	    	if(message.registerType != null)
            output.writeString(2, message.registerType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.name != null)
            output.writeString(3, message.name, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "events";
        	        	case 2: return "registerType";
        	        	case 3: return "name";
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
    	    	__fieldMap.put("events", 1);
    	    	__fieldMap.put("registerType", 2);
    	    	__fieldMap.put("name", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientManagerRegister.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientManagerRegister parseFrom(byte[] bytes) {
	ClientManagerRegister message = new ClientManagerRegister();
	ProtobufIOUtil.mergeFrom(bytes, message, ClientManagerRegister.getSchema());
	return message;
}

public static ClientManagerRegister parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ClientManagerRegister message = new ClientManagerRegister();
	JsonIOUtil.mergeFrom(bytes, message, ClientManagerRegister.getSchema(), false);
	return message;
}

public ClientManagerRegister clone() {
	byte[] bytes = this.toByteArray();
	ClientManagerRegister clientManagerRegister = ClientManagerRegister.parseFrom(bytes);
	return clientManagerRegister;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientManagerRegister.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientManagerRegister> schema = ClientManagerRegister.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ClientManagerRegister.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
