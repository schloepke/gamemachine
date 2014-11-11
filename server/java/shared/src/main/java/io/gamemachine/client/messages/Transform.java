
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
public final class Transform implements Externalizable, Message<Transform>, Schema<Transform>{



    public static Schema<Transform> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Transform getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Transform DEFAULT_INSTANCE = new Transform();

    			public Vector3 vector3;
	    
        			public Quaternion quaternion;
	    
      
    public Transform()
    {
        
    }


	

	    
    public Boolean hasVector3()  {
        return vector3 == null ? false : true;
    }
        
		public Vector3 getVector3() {
		return vector3;
	}
	
	public Transform setVector3(Vector3 vector3) {
		this.vector3 = vector3;
		return this;	}
	
		    
    public Boolean hasQuaternion()  {
        return quaternion == null ? false : true;
    }
        
		public Quaternion getQuaternion() {
		return quaternion;
	}
	
	public Transform setQuaternion(Quaternion quaternion) {
		this.quaternion = quaternion;
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

    public Schema<Transform> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Transform newMessage()
    {
        return new Transform();
    }

    public Class<Transform> typeClass()
    {
        return Transform.class;
    }

    public String messageName()
    {
        return Transform.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Transform.class.getName();
    }

    public boolean isInitialized(Transform message)
    {
        return true;
    }

    public void mergeFrom(Input input, Transform message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.vector3 = input.mergeObject(message.vector3, Vector3.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.quaternion = input.mergeObject(message.quaternion, Quaternion.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Transform message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.vector3 != null)
    		output.writeObject(1, message.vector3, Vector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.quaternion != null)
    		output.writeObject(2, message.quaternion, Quaternion.getSchema(), false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "vector3";
        	        	case 2: return "quaternion";
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
    	    	__fieldMap.put("vector3", 1);
    	    	__fieldMap.put("quaternion", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Transform.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Transform parseFrom(byte[] bytes) {
	Transform message = new Transform();
	ProtobufIOUtil.mergeFrom(bytes, message, Transform.getSchema());
	return message;
}

public static Transform parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Transform message = new Transform();
	JsonIOUtil.mergeFrom(bytes, message, Transform.getSchema(), false);
	return message;
}

public Transform clone() {
	byte[] bytes = this.toByteArray();
	Transform transform = Transform.parseFrom(bytes);
	return transform;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Transform.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Transform> schema = Transform.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Transform.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
