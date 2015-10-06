
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
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class Name implements Externalizable, Message<Name>, Schema<Name>{

private static final Logger logger = LoggerFactory.getLogger(Name.class);



    public static Schema<Name> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Name getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Name DEFAULT_INSTANCE = new Name();
    static final String defaultScope = Name.class.getSimpleName();

    	
							    public String value= null;
		    			    
		
    
        


    public Name()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("name_value",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (value != null) {
    	       	    	model.setString("name_value",value);
    	        		
    	//}
    	    	    }
    
	public static Name fromModel(Model model) {
		boolean hasFields = false;
    	Name message = new Name();
    	    	    	    	    	
    	    			String valueTestField = model.getString("name_value");
		if (valueTestField != null) {
			String valueField = valueTestField;
			message.setValue(valueField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public String getValue() {
		return value;
	}
	
	public Name setValue(String value) {
		this.value = value;
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

    public Schema<Name> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Name newMessage()
    {
        return new Name();
    }

    public Class<Name> typeClass()
    {
        return Name.class;
    }

    public String messageName()
    {
        return Name.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Name.class.getName();
    }

    public boolean isInitialized(Name message)
    {
        return true;
    }

    public void mergeFrom(Input input, Name message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.value = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Name message) throws IOException
    {
    	    	
    	    	//if(message.value == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.value != null) {
            output.writeString(1, message.value, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Name");
    	    	//if(this.value != null) {
    		System.out.println("value="+this.value);
    	//}
    	    	System.out.println("END Name");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "value";
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
    	    	__fieldMap.put("value", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Name.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Name parseFrom(byte[] bytes) {
	Name message = new Name();
	ProtobufIOUtil.mergeFrom(bytes, message, Name.getSchema());
	return message;
}

public static Name parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Name message = new Name();
	JsonIOUtil.mergeFrom(bytes, message, Name.getSchema(), false);
	return message;
}

public Name clone() {
	byte[] bytes = this.toByteArray();
	Name name = Name.parseFrom(bytes);
	return name;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Name.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Name> schema = Name.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Name.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Name.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
