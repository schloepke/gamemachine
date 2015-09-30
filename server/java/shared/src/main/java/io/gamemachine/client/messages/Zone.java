
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
public final class Zone implements Externalizable, Message<Zone>, Schema<Zone>{

	public enum Status implements io.protostuff.EnumLite<Status>
    {
    	
    	    	NONE(0),    	    	UP(1),    	    	DOWN(2),    	    	REQUEST_UP(3),    	    	REQUEST_DOWN(4);    	        
        public final int number;
        
        private Status (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Status valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (UP);
    			    			case 2: return (DOWN);
    			    			case 3: return (REQUEST_UP);
    			    			case 4: return (REQUEST_DOWN);
    			                default: return null;
            }
        }
    }


    public static Schema<Zone> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Zone getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Zone DEFAULT_INSTANCE = new Zone();

    			public String id;
	    
        			public Status status; // = NONE:0;
	    
        			public String name;
	    
        			public String unityClient;
	    
      
    public Zone()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Zone setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasStatus()  {
        return status == null ? false : true;
    }
        
		public Status getStatus() {
		return status;
	}
	
	public Zone setStatus(Status status) {
		this.status = status;
		return this;	}
	
		    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public Zone setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasUnityClient()  {
        return unityClient == null ? false : true;
    }
        
		public String getUnityClient() {
		return unityClient;
	}
	
	public Zone setUnityClient(String unityClient) {
		this.unityClient = unityClient;
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

    public Schema<Zone> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Zone newMessage()
    {
        return new Zone();
    }

    public Class<Zone> typeClass()
    {
        return Zone.class;
    }

    public String messageName()
    {
        return Zone.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Zone.class.getName();
    }

    public boolean isInitialized(Zone message)
    {
        return true;
    }

    public void mergeFrom(Input input, Zone message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                    message.status = Status.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 3:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.unityClient = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Zone message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.status != null)
    	 	output.writeEnum(2, message.status.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.name != null)
            output.writeString(3, message.name, false);
    	    	
    	            	
    	    	
    	    	    	if(message.unityClient != null)
            output.writeString(4, message.unityClient, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "status";
        	        	case 3: return "name";
        	        	case 4: return "unityClient";
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
    	    	__fieldMap.put("id", 1);
    	    	__fieldMap.put("status", 2);
    	    	__fieldMap.put("name", 3);
    	    	__fieldMap.put("unityClient", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Zone.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Zone parseFrom(byte[] bytes) {
	Zone message = new Zone();
	ProtobufIOUtil.mergeFrom(bytes, message, Zone.getSchema());
	return message;
}

public static Zone parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Zone message = new Zone();
	JsonIOUtil.mergeFrom(bytes, message, Zone.getSchema(), false);
	return message;
}

public Zone clone() {
	byte[] bytes = this.toByteArray();
	Zone zone = Zone.parseFrom(bytes);
	return zone;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Zone.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Zone> schema = Zone.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Zone.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
