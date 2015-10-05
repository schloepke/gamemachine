
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
public final class DataRequest implements Externalizable, Message<DataRequest>, Schema<DataRequest>{



    public static Schema<DataRequest> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static DataRequest getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final DataRequest DEFAULT_INSTANCE = new DataRequest();
    static final String defaultScope = DataRequest.class.getSimpleName();

    	
	    	    public String requester= null;
	    		
    
        	
	    	    public String name= null;
	    		
    
        


    public DataRequest()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("data_request_requester",null);
    	    	    	    	    	    	model.set("data_request_name",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (requester != null) {
    	       	    	model.setString("data_request_requester",requester);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (name != null) {
    	       	    	model.setString("data_request_name",name);
    	        		
    	//}
    	    	    }
    
	public static DataRequest fromModel(Model model) {
		boolean hasFields = false;
    	DataRequest message = new DataRequest();
    	    	    	    	    	
    	    	    	String requesterTestField = model.getString("data_request_requester");
    	if (requesterTestField != null) {
    		String requesterField = requesterTestField;
    		message.setRequester(requesterField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String nameTestField = model.getString("data_request_name");
    	if (nameTestField != null) {
    		String nameField = nameTestField;
    		message.setName(nameField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getRequester() {
		return requester;
	}
	
	public DataRequest setRequester(String requester) {
		this.requester = requester;
		return this;	}
	
		            
		public String getName() {
		return name;
	}
	
	public DataRequest setName(String name) {
		this.name = name;
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

    public Schema<DataRequest> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public DataRequest newMessage()
    {
        return new DataRequest();
    }

    public Class<DataRequest> typeClass()
    {
        return DataRequest.class;
    }

    public String messageName()
    {
        return DataRequest.class.getSimpleName();
    }

    public String messageFullName()
    {
        return DataRequest.class.getName();
    }

    public boolean isInitialized(DataRequest message)
    {
        return true;
    }

    public void mergeFrom(Input input, DataRequest message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.requester = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, DataRequest message) throws IOException
    {
    	    	
    	    	//if(message.requester == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.requester != null) {
            output.writeString(1, message.requester, false);
        }
    	    	
    	            	
    	    	//if(message.name == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.name != null) {
            output.writeString(2, message.name, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START DataRequest");
    	    	//if(this.requester != null) {
    		System.out.println("requester="+this.requester);
    	//}
    	    	//if(this.name != null) {
    		System.out.println("name="+this.name);
    	//}
    	    	System.out.println("END DataRequest");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "requester";
        	        	case 2: return "name";
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
    	    	__fieldMap.put("requester", 1);
    	    	__fieldMap.put("name", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = DataRequest.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static DataRequest parseFrom(byte[] bytes) {
	DataRequest message = new DataRequest();
	ProtobufIOUtil.mergeFrom(bytes, message, DataRequest.getSchema());
	return message;
}

public static DataRequest parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	DataRequest message = new DataRequest();
	JsonIOUtil.mergeFrom(bytes, message, DataRequest.getSchema(), false);
	return message;
}

public DataRequest clone() {
	byte[] bytes = this.toByteArray();
	DataRequest dataRequest = DataRequest.parseFrom(bytes);
	return dataRequest;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, DataRequest.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<DataRequest> schema = DataRequest.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, DataRequest.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, DataRequest.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
