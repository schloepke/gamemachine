
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
public final class Cost implements Externalizable, Message<Cost>, Schema<Cost>{



    public static Schema<Cost> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Cost getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Cost DEFAULT_INSTANCE = new Cost();

    			public Float amount;
	    
        			public String currency;
	    
      
    public Cost()
    {
        
    }


	

	    
    public Boolean hasAmount()  {
        return amount == null ? false : true;
    }
        
		public Float getAmount() {
		return amount;
	}
	
	public Cost setAmount(Float amount) {
		this.amount = amount;
		return this;	}
	
		    
    public Boolean hasCurrency()  {
        return currency == null ? false : true;
    }
        
		public String getCurrency() {
		return currency;
	}
	
	public Cost setCurrency(String currency) {
		this.currency = currency;
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

    public Schema<Cost> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Cost newMessage()
    {
        return new Cost();
    }

    public Class<Cost> typeClass()
    {
        return Cost.class;
    }

    public String messageName()
    {
        return Cost.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Cost.class.getName();
    }

    public boolean isInitialized(Cost message)
    {
        return true;
    }

    public void mergeFrom(Input input, Cost message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.amount = input.readFloat();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.currency = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Cost message) throws IOException
    {
    	    	
    	    	if(message.amount == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.amount != null)
            output.writeFloat(1, message.amount, false);
    	    	
    	            	
    	    	
    	    	    	if(message.currency != null)
            output.writeString(2, message.currency, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "amount";
        	        	case 2: return "currency";
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
    	    	__fieldMap.put("amount", 1);
    	    	__fieldMap.put("currency", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Cost.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Cost parseFrom(byte[] bytes) {
	Cost message = new Cost();
	ProtobufIOUtil.mergeFrom(bytes, message, Cost.getSchema());
	return message;
}

public static Cost parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Cost message = new Cost();
	JsonIOUtil.mergeFrom(bytes, message, Cost.getSchema(), false);
	return message;
}

public Cost clone() {
	byte[] bytes = this.toByteArray();
	Cost cost = Cost.parseFrom(bytes);
	return cost;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Cost.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Cost> schema = Cost.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Cost.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
