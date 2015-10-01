
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
public final class Waypoint implements Externalizable, Message<Waypoint>, Schema<Waypoint>{



    public static Schema<Waypoint> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Waypoint getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Waypoint DEFAULT_INSTANCE = new Waypoint();

    			public String id;
	    
            public List<GmVector3> position;
	  
    public Waypoint()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Waypoint setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasPosition()  {
        return position == null ? false : true;
    }
        
		public List<GmVector3> getPositionList() {
		if(this.position == null)
            this.position = new ArrayList<GmVector3>();
		return position;
	}

	public Waypoint setPositionList(List<GmVector3> position) {
		this.position = position;
		return this;
	}

	public GmVector3 getPosition(int index)  {
        return position == null ? null : position.get(index);
    }

    public int getPositionCount()  {
        return position == null ? 0 : position.size();
    }

    public Waypoint addPosition(GmVector3 position)  {
        if(this.position == null)
            this.position = new ArrayList<GmVector3>();
        this.position.add(position);
        return this;
    }
            	    	    	    	
    public Waypoint removePositionByX(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByY(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByZ(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.z.equals(obj.z)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByXi(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.xi.equals(obj.xi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByYi(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.yi.equals(obj.yi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByZi(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.zi.equals(obj.zi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByVertice(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.vertice.equals(obj.vertice)) {
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

    public Schema<Waypoint> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Waypoint newMessage()
    {
        return new Waypoint();
    }

    public Class<Waypoint> typeClass()
    {
        return Waypoint.class;
    }

    public String messageName()
    {
        return Waypoint.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Waypoint.class.getName();
    }

    public boolean isInitialized(Waypoint message)
    {
        return true;
    }

    public void mergeFrom(Input input, Waypoint message) throws IOException
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
            	            		if(message.position == null)
                        message.position = new ArrayList<GmVector3>();
                                        message.position.add(input.mergeObject(null, GmVector3.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Waypoint message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	if(message.position != null)
        {
            for(GmVector3 position : message.position)
            {
                if(position != null) {
                   	    				output.writeObject(2, position, GmVector3.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "position";
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
    	    	__fieldMap.put("position", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Waypoint.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Waypoint parseFrom(byte[] bytes) {
	Waypoint message = new Waypoint();
	ProtobufIOUtil.mergeFrom(bytes, message, Waypoint.getSchema());
	return message;
}

public static Waypoint parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Waypoint message = new Waypoint();
	JsonIOUtil.mergeFrom(bytes, message, Waypoint.getSchema(), false);
	return message;
}

public Waypoint clone() {
	byte[] bytes = this.toByteArray();
	Waypoint waypoint = Waypoint.parseFrom(bytes);
	return waypoint;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Waypoint.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Waypoint> schema = Waypoint.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Waypoint.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
