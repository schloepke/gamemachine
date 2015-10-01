
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
public final class MonoMessage implements Externalizable, Message<MonoMessage>, Schema<MonoMessage>{



    public static Schema<MonoMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MonoMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MonoMessage DEFAULT_INSTANCE = new MonoMessage();
    static final String defaultScope = MonoMessage.class.getSimpleName();

    			public Entity entity;
	    
        			public String klass;
	    
        


    public MonoMessage()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	    	model.set("mono_message_klass",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	
    	    	    	if (klass != null) {
    	       	    	model.setString("mono_message_klass",klass);
    	        		
    	}
    	    	    }
    
	public static MonoMessage fromModel(Model model) {
		boolean hasFields = false;
    	MonoMessage message = new MonoMessage();
    	    	    	    	    	    	
    	    	    	String klassField = model.getString("mono_message_klass");
    	    	
    	if (klassField != null) {
    		message.setKlass(klassField);
    		hasFields = true;
    	}
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
	
	public MonoMessage setEntity(Entity entity) {
		this.entity = entity;
		return this;	}
	
		    
    public Boolean hasKlass()  {
        return klass == null ? false : true;
    }
        
		public String getKlass() {
		return klass;
	}
	
	public MonoMessage setKlass(String klass) {
		this.klass = klass;
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

    public Schema<MonoMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MonoMessage newMessage()
    {
        return new MonoMessage();
    }

    public Class<MonoMessage> typeClass()
    {
        return MonoMessage.class;
    }

    public String messageName()
    {
        return MonoMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MonoMessage.class.getName();
    }

    public boolean isInitialized(MonoMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, MonoMessage message) throws IOException
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
                                    	
                            	            	case 2:
            	                	                	message.klass = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MonoMessage message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.entity != null)
    		output.writeObject(1, message.entity, Entity.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.klass != null)
            output.writeString(2, message.klass, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START MonoMessage");
    	    	if(this.entity != null) {
    		System.out.println("entity="+this.entity);
    	}
    	    	if(this.klass != null) {
    		System.out.println("klass="+this.klass);
    	}
    	    	System.out.println("END MonoMessage");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "entity";
        	        	case 2: return "klass";
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
    	    	__fieldMap.put("klass", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = MonoMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static MonoMessage parseFrom(byte[] bytes) {
	MonoMessage message = new MonoMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, MonoMessage.getSchema());
	return message;
}

public static MonoMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	MonoMessage message = new MonoMessage();
	JsonIOUtil.mergeFrom(bytes, message, MonoMessage.getSchema(), false);
	return message;
}

public MonoMessage clone() {
	byte[] bytes = this.toByteArray();
	MonoMessage monoMessage = MonoMessage.parseFrom(bytes);
	return monoMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, MonoMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<MonoMessage> schema = MonoMessage.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, MonoMessage.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, MonoMessage.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
