
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
public final class ObjectdbStatus implements Externalizable, Message<ObjectdbStatus>, Schema<ObjectdbStatus>{



    public static Schema<ObjectdbStatus> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbStatus getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbStatus DEFAULT_INSTANCE = new ObjectdbStatus();
    static final String defaultScope = ObjectdbStatus.class.getSimpleName();

    	
							    public String entityId= null;
		    			    
		
    
        	
							    public String status= null;
		    			    
		
    
        


    public ObjectdbStatus()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("objectdb_status_entity_id",null);
    	    	    	    	    	    	model.set("objectdb_status_status",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (entityId != null) {
    	       	    	model.setString("objectdb_status_entity_id",entityId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (status != null) {
    	       	    	model.setString("objectdb_status_status",status);
    	        		
    	//}
    	    	    }
    
	public static ObjectdbStatus fromModel(Model model) {
		boolean hasFields = false;
    	ObjectdbStatus message = new ObjectdbStatus();
    	    	    	    	    	
    	    	    	String entityIdTestField = model.getString("objectdb_status_entity_id");
    	if (entityIdTestField != null) {
    		String entityIdField = entityIdTestField;
    		message.setEntityId(entityIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String statusTestField = model.getString("objectdb_status_status");
    	if (statusTestField != null) {
    		String statusField = statusTestField;
    		message.setStatus(statusField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getEntityId() {
		return entityId;
	}
	
	public ObjectdbStatus setEntityId(String entityId) {
		this.entityId = entityId;
		return this;	}
	
		            
		public String getStatus() {
		return status;
	}
	
	public ObjectdbStatus setStatus(String status) {
		this.status = status;
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

    public Schema<ObjectdbStatus> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbStatus newMessage()
    {
        return new ObjectdbStatus();
    }

    public Class<ObjectdbStatus> typeClass()
    {
        return ObjectdbStatus.class;
    }

    public String messageName()
    {
        return ObjectdbStatus.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbStatus.class.getName();
    }

    public boolean isInitialized(ObjectdbStatus message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbStatus message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.entityId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.status = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ObjectdbStatus message) throws IOException
    {
    	    	
    	    	//if(message.entityId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.entityId != null) {
            output.writeString(1, message.entityId, false);
        }
    	    	
    	            	
    	    	//if(message.status == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.status != null) {
            output.writeString(2, message.status, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ObjectdbStatus");
    	    	//if(this.entityId != null) {
    		System.out.println("entityId="+this.entityId);
    	//}
    	    	//if(this.status != null) {
    		System.out.println("status="+this.status);
    	//}
    	    	System.out.println("END ObjectdbStatus");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "entityId";
        	        	case 2: return "status";
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
    	    	__fieldMap.put("entityId", 1);
    	    	__fieldMap.put("status", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbStatus.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbStatus parseFrom(byte[] bytes) {
	ObjectdbStatus message = new ObjectdbStatus();
	ProtobufIOUtil.mergeFrom(bytes, message, ObjectdbStatus.getSchema());
	return message;
}

public static ObjectdbStatus parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ObjectdbStatus message = new ObjectdbStatus();
	JsonIOUtil.mergeFrom(bytes, message, ObjectdbStatus.getSchema(), false);
	return message;
}

public ObjectdbStatus clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbStatus objectdbStatus = ObjectdbStatus.parseFrom(bytes);
	return objectdbStatus;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbStatus.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ObjectdbStatus> schema = ObjectdbStatus.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ObjectdbStatus.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ObjectdbStatus.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
