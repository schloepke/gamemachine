
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
public final class ObjectdbPut implements Externalizable, Message<ObjectdbPut>, Schema<ObjectdbPut>{



    public static Schema<ObjectdbPut> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbPut getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbPut DEFAULT_INSTANCE = new ObjectdbPut();
    static final String defaultScope = ObjectdbPut.class.getSimpleName();

    			public Entity entity;
	    
        


    public ObjectdbPut()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static ObjectdbPut fromModel(Model model) {
		boolean hasFields = false;
    	ObjectdbPut message = new ObjectdbPut();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasEntity()  {
        return entity == null ? false : true;
    }
        
		public Entity getEntity() {
		return entity;
	}
	
	public ObjectdbPut setEntity(Entity entity) {
		this.entity = entity;
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

    public Schema<ObjectdbPut> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbPut newMessage()
    {
        return new ObjectdbPut();
    }

    public Class<ObjectdbPut> typeClass()
    {
        return ObjectdbPut.class;
    }

    public String messageName()
    {
        return ObjectdbPut.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbPut.class.getName();
    }

    public boolean isInitialized(ObjectdbPut message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbPut message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.entity = input.mergeObject(message.entity, Entity.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ObjectdbPut message) throws IOException
    {
    	    	
    	    	if(message.entity == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.entity != null)
    		output.writeObject(1, message.entity, Entity.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ObjectdbPut");
    	    	if(this.entity != null) {
    		System.out.println("entity="+this.entity);
    	}
    	    	System.out.println("END ObjectdbPut");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "entity";
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
    	    	__fieldMap.put("entity", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbPut.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbPut parseFrom(byte[] bytes) {
	ObjectdbPut message = new ObjectdbPut();
	ProtobufIOUtil.mergeFrom(bytes, message, ObjectdbPut.getSchema());
	return message;
}

public static ObjectdbPut parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ObjectdbPut message = new ObjectdbPut();
	JsonIOUtil.mergeFrom(bytes, message, ObjectdbPut.getSchema(), false);
	return message;
}

public ObjectdbPut clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbPut objectdbPut = ObjectdbPut.parseFrom(bytes);
	return objectdbPut;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbPut.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ObjectdbPut> schema = ObjectdbPut.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ObjectdbPut.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ObjectdbPut.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
