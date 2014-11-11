
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
public final class Mesh implements Externalizable, Message<Mesh>, Schema<Mesh>{



    public static Schema<Mesh> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Mesh getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Mesh DEFAULT_INSTANCE = new Mesh();

        public List<Polygon> polygon;
	  
    public Mesh()
    {
        
    }


	

	    
    public Boolean hasPolygon()  {
        return polygon == null ? false : true;
    }
        
		public List<Polygon> getPolygonList() {
		if(this.polygon == null)
            this.polygon = new ArrayList<Polygon>();
		return polygon;
	}

	public Mesh setPolygonList(List<Polygon> polygon) {
		this.polygon = polygon;
		return this;
	}

	public Polygon getPolygon(int index)  {
        return polygon == null ? null : polygon.get(index);
    }

    public int getPolygonCount()  {
        return polygon == null ? 0 : polygon.size();
    }

    public Mesh addPolygon(Polygon polygon)  {
        if(this.polygon == null)
            this.polygon = new ArrayList<Polygon>();
        this.polygon.add(polygon);
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

    public Schema<Mesh> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Mesh newMessage()
    {
        return new Mesh();
    }

    public Class<Mesh> typeClass()
    {
        return Mesh.class;
    }

    public String messageName()
    {
        return Mesh.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Mesh.class.getName();
    }

    public boolean isInitialized(Mesh message)
    {
        return true;
    }

    public void mergeFrom(Input input, Mesh message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.polygon == null)
                        message.polygon = new ArrayList<Polygon>();
                                        message.polygon.add(input.mergeObject(null, Polygon.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Mesh message) throws IOException
    {
    	    	
    	    	
    	    	if(message.polygon != null)
        {
            for(Polygon polygon : message.polygon)
            {
                if(polygon != null) {
                   	    				output.writeObject(1, polygon, Polygon.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "polygon";
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
    	    	__fieldMap.put("polygon", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Mesh.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Mesh parseFrom(byte[] bytes) {
	Mesh message = new Mesh();
	ProtobufIOUtil.mergeFrom(bytes, message, Mesh.getSchema());
	return message;
}

public static Mesh parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Mesh message = new Mesh();
	JsonIOUtil.mergeFrom(bytes, message, Mesh.getSchema(), false);
	return message;
}

public Mesh clone() {
	byte[] bytes = this.toByteArray();
	Mesh mesh = Mesh.parseFrom(bytes);
	return mesh;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Mesh.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Mesh> schema = Mesh.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Mesh.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
