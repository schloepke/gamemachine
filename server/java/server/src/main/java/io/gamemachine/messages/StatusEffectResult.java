
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
public final class StatusEffectResult implements Externalizable, Message<StatusEffectResult>, Schema<StatusEffectResult>{

private static final Logger logger = LoggerFactory.getLogger(StatusEffectResult.class);



    public static Schema<StatusEffectResult> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static StatusEffectResult getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final StatusEffectResult DEFAULT_INSTANCE = new StatusEffectResult();
    static final String defaultScope = StatusEffectResult.class.getSimpleName();

    	
							    public String targetCharacterId= null;
		    			    
		
    
        	
							    public String originCharacterId= null;
		    			    
		
    
        	
							    public int value= 0;
		    			    
		
    
        	
							    public String statusEffectId= null;
		    			    
		
    
        	
							    public String targetEntityId= null;
		    			    
		
    
        


    public StatusEffectResult()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("status_effect_result_target_character_id",null);
    	    	    	    	    	    	model.set("status_effect_result_origin_character_id",null);
    	    	    	    	    	    	model.set("status_effect_result_value",null);
    	    	    	    	    	    	model.set("status_effect_result_status_effect_id",null);
    	    	    	    	    	    	model.set("status_effect_result_target_entity_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (targetCharacterId != null) {
    	       	    	model.setString("status_effect_result_target_character_id",targetCharacterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (originCharacterId != null) {
    	       	    	model.setString("status_effect_result_origin_character_id",originCharacterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (value != null) {
    	       	    	model.setInteger("status_effect_result_value",value);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (statusEffectId != null) {
    	       	    	model.setString("status_effect_result_status_effect_id",statusEffectId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (targetEntityId != null) {
    	       	    	model.setString("status_effect_result_target_entity_id",targetEntityId);
    	        		
    	//}
    	    	    }
    
	public static StatusEffectResult fromModel(Model model) {
		boolean hasFields = false;
    	StatusEffectResult message = new StatusEffectResult();
    	    	    	    	    	
    	    			String targetCharacterIdTestField = model.getString("status_effect_result_target_character_id");
		if (targetCharacterIdTestField != null) {
			String targetCharacterIdField = targetCharacterIdTestField;
			message.setTargetCharacterId(targetCharacterIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String originCharacterIdTestField = model.getString("status_effect_result_origin_character_id");
		if (originCharacterIdTestField != null) {
			String originCharacterIdField = originCharacterIdTestField;
			message.setOriginCharacterId(originCharacterIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer valueTestField = model.getInteger("status_effect_result_value");
		if (valueTestField != null) {
			int valueField = valueTestField;
			message.setValue(valueField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String statusEffectIdTestField = model.getString("status_effect_result_status_effect_id");
		if (statusEffectIdTestField != null) {
			String statusEffectIdField = statusEffectIdTestField;
			message.setStatusEffectId(statusEffectIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String targetEntityIdTestField = model.getString("status_effect_result_target_entity_id");
		if (targetEntityIdTestField != null) {
			String targetEntityIdField = targetEntityIdTestField;
			message.setTargetEntityId(targetEntityIdField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public String getTargetCharacterId() {
		return targetCharacterId;
	}
	
	public StatusEffectResult setTargetCharacterId(String targetCharacterId) {
		this.targetCharacterId = targetCharacterId;
		return this;	}
	
		            
		public String getOriginCharacterId() {
		return originCharacterId;
	}
	
	public StatusEffectResult setOriginCharacterId(String originCharacterId) {
		this.originCharacterId = originCharacterId;
		return this;	}
	
		            
		public int getValue() {
		return value;
	}
	
	public StatusEffectResult setValue(int value) {
		this.value = value;
		return this;	}
	
		            
		public String getStatusEffectId() {
		return statusEffectId;
	}
	
	public StatusEffectResult setStatusEffectId(String statusEffectId) {
		this.statusEffectId = statusEffectId;
		return this;	}
	
		            
		public String getTargetEntityId() {
		return targetEntityId;
	}
	
	public StatusEffectResult setTargetEntityId(String targetEntityId) {
		this.targetEntityId = targetEntityId;
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

    public Schema<StatusEffectResult> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public StatusEffectResult newMessage()
    {
        return new StatusEffectResult();
    }

    public Class<StatusEffectResult> typeClass()
    {
        return StatusEffectResult.class;
    }

    public String messageName()
    {
        return StatusEffectResult.class.getSimpleName();
    }

    public String messageFullName()
    {
        return StatusEffectResult.class.getName();
    }

    public boolean isInitialized(StatusEffectResult message)
    {
        return true;
    }

    public void mergeFrom(Input input, StatusEffectResult message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.targetCharacterId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.originCharacterId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.value = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.statusEffectId = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.targetEntityId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, StatusEffectResult message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.targetCharacterId != null) {
            output.writeString(1, message.targetCharacterId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.originCharacterId != null) {
            output.writeString(2, message.originCharacterId, false);
        }
    	    	
    	            	
    	    	//if(message.value == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.value != null) {
            output.writeInt32(3, message.value, false);
        }
    	    	
    	            	
    	    	//if(message.statusEffectId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.statusEffectId != null) {
            output.writeString(4, message.statusEffectId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.targetEntityId != null) {
            output.writeString(5, message.targetEntityId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START StatusEffectResult");
    	    	//if(this.targetCharacterId != null) {
    		System.out.println("targetCharacterId="+this.targetCharacterId);
    	//}
    	    	//if(this.originCharacterId != null) {
    		System.out.println("originCharacterId="+this.originCharacterId);
    	//}
    	    	//if(this.value != null) {
    		System.out.println("value="+this.value);
    	//}
    	    	//if(this.statusEffectId != null) {
    		System.out.println("statusEffectId="+this.statusEffectId);
    	//}
    	    	//if(this.targetEntityId != null) {
    		System.out.println("targetEntityId="+this.targetEntityId);
    	//}
    	    	System.out.println("END StatusEffectResult");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "targetCharacterId";
        	        	case 2: return "originCharacterId";
        	        	case 3: return "value";
        	        	case 4: return "statusEffectId";
        	        	case 5: return "targetEntityId";
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
    	    	__fieldMap.put("targetCharacterId", 1);
    	    	__fieldMap.put("originCharacterId", 2);
    	    	__fieldMap.put("value", 3);
    	    	__fieldMap.put("statusEffectId", 4);
    	    	__fieldMap.put("targetEntityId", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = StatusEffectResult.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static StatusEffectResult parseFrom(byte[] bytes) {
	StatusEffectResult message = new StatusEffectResult();
	ProtobufIOUtil.mergeFrom(bytes, message, StatusEffectResult.getSchema());
	return message;
}

public static StatusEffectResult parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	StatusEffectResult message = new StatusEffectResult();
	JsonIOUtil.mergeFrom(bytes, message, StatusEffectResult.getSchema(), false);
	return message;
}

public StatusEffectResult clone() {
	byte[] bytes = this.toByteArray();
	StatusEffectResult statusEffectResult = StatusEffectResult.parseFrom(bytes);
	return statusEffectResult;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, StatusEffectResult.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<StatusEffectResult> schema = StatusEffectResult.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, StatusEffectResult.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, StatusEffectResult.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
