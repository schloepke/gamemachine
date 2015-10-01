
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
public final class Subscribers implements Externalizable, Message<Subscribers>, Schema<Subscribers>{



    public static Schema<Subscribers> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Subscribers getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Subscribers DEFAULT_INSTANCE = new Subscribers();
    static final String defaultScope = Subscribers.class.getSimpleName();

        public List<String> subscriberId;
	    


    public Subscribers()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    }
    
	public static Subscribers fromModel(Model model) {
		boolean hasFields = false;
    	Subscribers message = new Subscribers();
    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasSubscriberId()  {
        return subscriberId == null ? false : true;
    }
        
		public List<String> getSubscriberIdList() {
		if(this.subscriberId == null)
            this.subscriberId = new ArrayList<String>();
		return subscriberId;
	}

	public Subscribers setSubscriberIdList(List<String> subscriberId) {
		this.subscriberId = subscriberId;
		return this;
	}

	public String getSubscriberId(int index)  {
        return subscriberId == null ? null : subscriberId.get(index);
    }

    public int getSubscriberIdCount()  {
        return subscriberId == null ? 0 : subscriberId.size();
    }

    public Subscribers addSubscriberId(String subscriberId)  {
        if(this.subscriberId == null)
            this.subscriberId = new ArrayList<String>();
        this.subscriberId.add(subscriberId);
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

    public Schema<Subscribers> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Subscribers newMessage()
    {
        return new Subscribers();
    }

    public Class<Subscribers> typeClass()
    {
        return Subscribers.class;
    }

    public String messageName()
    {
        return Subscribers.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Subscribers.class.getName();
    }

    public boolean isInitialized(Subscribers message)
    {
        return true;
    }

    public void mergeFrom(Input input, Subscribers message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.subscriberId == null)
                        message.subscriberId = new ArrayList<String>();
                                    	message.subscriberId.add(input.readString());
                	                    break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Subscribers message) throws IOException
    {
    	    	
    	    	
    	    	if(message.subscriberId != null)
        {
            for(String subscriberId : message.subscriberId)
            {
                if(subscriberId != null) {
                   	            		output.writeString(1, subscriberId, true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Subscribers");
    	    	if(this.subscriberId != null) {
    		System.out.println("subscriberId="+this.subscriberId);
    	}
    	    	System.out.println("END Subscribers");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "subscriberId";
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
    	    	__fieldMap.put("subscriberId", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Subscribers.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Subscribers parseFrom(byte[] bytes) {
	Subscribers message = new Subscribers();
	ProtobufIOUtil.mergeFrom(bytes, message, Subscribers.getSchema());
	return message;
}

public static Subscribers parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Subscribers message = new Subscribers();
	JsonIOUtil.mergeFrom(bytes, message, Subscribers.getSchema(), false);
	return message;
}

public Subscribers clone() {
	byte[] bytes = this.toByteArray();
	Subscribers subscribers = Subscribers.parseFrom(bytes);
	return subscribers;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Subscribers.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Subscribers> schema = Subscribers.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Subscribers.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Subscribers.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
