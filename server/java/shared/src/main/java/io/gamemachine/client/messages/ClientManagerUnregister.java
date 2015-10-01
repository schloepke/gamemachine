
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


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class ClientManagerUnregister implements Externalizable, Message<ClientManagerUnregister>, Schema<ClientManagerUnregister>{



    public static Schema<ClientManagerUnregister> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientManagerUnregister getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientManagerUnregister DEFAULT_INSTANCE = new ClientManagerUnregister();

    			public String registerType;
	    
        			public String name;
	    
      
    public ClientManagerUnregister()
    {
        
    }


	

	    
    public Boolean hasRegisterType()  {
        return registerType == null ? false : true;
    }
        
		public String getRegisterType() {
		return registerType;
	}
	
	public ClientManagerUnregister setRegisterType(String registerType) {
		this.registerType = registerType;
		return this;	}
	
		    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public ClientManagerUnregister setName(String name) {
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

    public Schema<ClientManagerUnregister> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientManagerUnregister newMessage()
    {
        return new ClientManagerUnregister();
    }

    public Class<ClientManagerUnregister> typeClass()
    {
        return ClientManagerUnregister.class;
    }

    public String messageName()
    {
        return ClientManagerUnregister.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientManagerUnregister.class.getName();
    }

    public boolean isInitialized(ClientManagerUnregister message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientManagerUnregister message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.registerType = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ClientManagerUnregister message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.registerType != null)
            output.writeString(1, message.registerType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.name != null)
            output.writeString(2, message.name, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "registerType";
        	        	case 2: return "name";
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
    	    	__fieldMap.put("registerType", 1);
    	    	__fieldMap.put("name", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientManagerUnregister.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientManagerUnregister parseFrom(byte[] bytes) {
	ClientManagerUnregister message = new ClientManagerUnregister();
	ProtobufIOUtil.mergeFrom(bytes, message, ClientManagerUnregister.getSchema());
	return message;
}

public static ClientManagerUnregister parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ClientManagerUnregister message = new ClientManagerUnregister();
	JsonIOUtil.mergeFrom(bytes, message, ClientManagerUnregister.getSchema(), false);
	return message;
}

public ClientManagerUnregister clone() {
	byte[] bytes = this.toByteArray();
	ClientManagerUnregister clientManagerUnregister = ClientManagerUnregister.parseFrom(bytes);
	return clientManagerUnregister;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientManagerUnregister.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientManagerUnregister> schema = ClientManagerUnregister.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ClientManagerUnregister.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
