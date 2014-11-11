
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
public final class Neighbor implements Externalizable, Message<Neighbor>, Schema<Neighbor>{



    public static Schema<Neighbor> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Neighbor getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Neighbor DEFAULT_INSTANCE = new Neighbor();

    			public TrackData trackData;
	    
        			public Vector3 location;
	    
        			public String id;
	    
      
    public Neighbor()
    {
        
    }


	

	    
    public Boolean hasTrackData()  {
        return trackData == null ? false : true;
    }
        
		public TrackData getTrackData() {
		return trackData;
	}
	
	public Neighbor setTrackData(TrackData trackData) {
		this.trackData = trackData;
		return this;	}
	
		    
    public Boolean hasLocation()  {
        return location == null ? false : true;
    }
        
		public Vector3 getLocation() {
		return location;
	}
	
	public Neighbor setLocation(Vector3 location) {
		this.location = location;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Neighbor setId(String id) {
		this.id = id;
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

    public Schema<Neighbor> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Neighbor newMessage()
    {
        return new Neighbor();
    }

    public Class<Neighbor> typeClass()
    {
        return Neighbor.class;
    }

    public String messageName()
    {
        return Neighbor.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Neighbor.class.getName();
    }

    public boolean isInitialized(Neighbor message)
    {
        return true;
    }

    public void mergeFrom(Input input, Neighbor message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.trackData = input.mergeObject(message.trackData, TrackData.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.location = input.mergeObject(message.location, Vector3.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Neighbor message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.trackData != null)
    		output.writeObject(1, message.trackData, TrackData.getSchema(), false);
    	    	
    	            	
    	    	if(message.location == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.location != null)
    		output.writeObject(2, message.location, Vector3.getSchema(), false);
    	    	
    	            	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(3, message.id, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "trackData";
        	        	case 2: return "location";
        	        	case 3: return "id";
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
    	    	__fieldMap.put("trackData", 1);
    	    	__fieldMap.put("location", 2);
    	    	__fieldMap.put("id", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Neighbor.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Neighbor parseFrom(byte[] bytes) {
	Neighbor message = new Neighbor();
	ProtobufIOUtil.mergeFrom(bytes, message, Neighbor.getSchema());
	return message;
}

public static Neighbor parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Neighbor message = new Neighbor();
	JsonIOUtil.mergeFrom(bytes, message, Neighbor.getSchema(), false);
	return message;
}

public Neighbor clone() {
	byte[] bytes = this.toByteArray();
	Neighbor neighbor = Neighbor.parseFrom(bytes);
	return neighbor;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Neighbor.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Neighbor> schema = Neighbor.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Neighbor.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
