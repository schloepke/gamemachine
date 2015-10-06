
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
public final class ObjectdbGet implements Externalizable, Message<ObjectdbGet>, Schema<ObjectdbGet>{

private static final Logger logger = LoggerFactory.getLogger(ObjectdbGet.class);



    public static Schema<ObjectdbGet> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ObjectdbGet getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ObjectdbGet DEFAULT_INSTANCE = new ObjectdbGet();
    static final String defaultScope = ObjectdbGet.class.getSimpleName();

    	
							    public String entityId= null;
		    			    
		
    
        	
							    public String playerId= null;
		    			    
		
    
        	
							    public String klass= null;
		    			    
		
    
        


    public ObjectdbGet()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("objectdb_get_entity_id",null);
    	    	    	    	    	    	model.set("objectdb_get_player_id",null);
    	    	    	    	    	    	model.set("objectdb_get_klass",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (entityId != null) {
    	       	    	model.setString("objectdb_get_entity_id",entityId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("objectdb_get_player_id",playerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (klass != null) {
    	       	    	model.setString("objectdb_get_klass",klass);
    	        		
    	//}
    	    	    }
    
	public static ObjectdbGet fromModel(Model model) {
		boolean hasFields = false;
    	ObjectdbGet message = new ObjectdbGet();
    	    	    	    	    	
    	    			String entityIdTestField = model.getString("objectdb_get_entity_id");
		if (entityIdTestField != null) {
			String entityIdField = entityIdTestField;
			message.setEntityId(entityIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String playerIdTestField = model.getString("objectdb_get_player_id");
		if (playerIdTestField != null) {
			String playerIdField = playerIdTestField;
			message.setPlayerId(playerIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String klassTestField = model.getString("objectdb_get_klass");
		if (klassTestField != null) {
			String klassField = klassTestField;
			message.setKlass(klassField);
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
	
	public ObjectdbGet setEntityId(String entityId) {
		this.entityId = entityId;
		return this;	}
	
		            
		public String getPlayerId() {
		return playerId;
	}
	
	public ObjectdbGet setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		            
		public String getKlass() {
		return klass;
	}
	
	public ObjectdbGet setKlass(String klass) {
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

    public Schema<ObjectdbGet> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ObjectdbGet newMessage()
    {
        return new ObjectdbGet();
    }

    public Class<ObjectdbGet> typeClass()
    {
        return ObjectdbGet.class;
    }

    public String messageName()
    {
        return ObjectdbGet.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ObjectdbGet.class.getName();
    }

    public boolean isInitialized(ObjectdbGet message)
    {
        return true;
    }

    public void mergeFrom(Input input, ObjectdbGet message) throws IOException
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
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.klass = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ObjectdbGet message) throws IOException
    {
    	    	
    	    	//if(message.entityId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.entityId != null) {
            output.writeString(1, message.entityId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(2, message.playerId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.klass != null) {
            output.writeString(3, message.klass, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ObjectdbGet");
    	    	//if(this.entityId != null) {
    		System.out.println("entityId="+this.entityId);
    	//}
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	//if(this.klass != null) {
    		System.out.println("klass="+this.klass);
    	//}
    	    	System.out.println("END ObjectdbGet");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "entityId";
        	        	case 2: return "playerId";
        	        	case 3: return "klass";
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
    	    	__fieldMap.put("playerId", 2);
    	    	__fieldMap.put("klass", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ObjectdbGet.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ObjectdbGet parseFrom(byte[] bytes) {
	ObjectdbGet message = new ObjectdbGet();
	ProtobufIOUtil.mergeFrom(bytes, message, ObjectdbGet.getSchema());
	return message;
}

public static ObjectdbGet parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ObjectdbGet message = new ObjectdbGet();
	JsonIOUtil.mergeFrom(bytes, message, ObjectdbGet.getSchema(), false);
	return message;
}

public ObjectdbGet clone() {
	byte[] bytes = this.toByteArray();
	ObjectdbGet objectdbGet = ObjectdbGet.parseFrom(bytes);
	return objectdbGet;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ObjectdbGet.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ObjectdbGet> schema = ObjectdbGet.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ObjectdbGet.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ObjectdbGet.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
