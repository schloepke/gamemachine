
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
public final class VisualEffect implements Externalizable, Message<VisualEffect>, Schema<VisualEffect>{



    public static Schema<VisualEffect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static VisualEffect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final VisualEffect DEFAULT_INSTANCE = new VisualEffect();

    			public String id;
	    
        			public String prefab;
	    
        			public GmVector3 startPosition;
	    
        			public GmVector3 endPosition;
	    
        			public String type;
	    
        			public Integer duration;
	    
      
    public VisualEffect()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public VisualEffect setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasPrefab()  {
        return prefab == null ? false : true;
    }
        
		public String getPrefab() {
		return prefab;
	}
	
	public VisualEffect setPrefab(String prefab) {
		this.prefab = prefab;
		return this;	}
	
		    
    public Boolean hasStartPosition()  {
        return startPosition == null ? false : true;
    }
        
		public GmVector3 getStartPosition() {
		return startPosition;
	}
	
	public VisualEffect setStartPosition(GmVector3 startPosition) {
		this.startPosition = startPosition;
		return this;	}
	
		    
    public Boolean hasEndPosition()  {
        return endPosition == null ? false : true;
    }
        
		public GmVector3 getEndPosition() {
		return endPosition;
	}
	
	public VisualEffect setEndPosition(GmVector3 endPosition) {
		this.endPosition = endPosition;
		return this;	}
	
		    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public String getType() {
		return type;
	}
	
	public VisualEffect setType(String type) {
		this.type = type;
		return this;	}
	
		    
    public Boolean hasDuration()  {
        return duration == null ? false : true;
    }
        
		public Integer getDuration() {
		return duration;
	}
	
	public VisualEffect setDuration(Integer duration) {
		this.duration = duration;
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

    public Schema<VisualEffect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public VisualEffect newMessage()
    {
        return new VisualEffect();
    }

    public Class<VisualEffect> typeClass()
    {
        return VisualEffect.class;
    }

    public String messageName()
    {
        return VisualEffect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return VisualEffect.class.getName();
    }

    public boolean isInitialized(VisualEffect message)
    {
        return true;
    }

    public void mergeFrom(Input input, VisualEffect message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.prefab = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.startPosition = input.mergeObject(message.startPosition, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.endPosition = input.mergeObject(message.endPosition, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 5:
            	                	                	message.type = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.duration = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, VisualEffect message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.prefab != null)
            output.writeString(2, message.prefab, false);
    	    	
    	            	
    	    	
    	    	    	if(message.startPosition != null)
    		output.writeObject(3, message.startPosition, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.endPosition != null)
    		output.writeObject(4, message.endPosition, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.type != null)
            output.writeString(5, message.type, false);
    	    	
    	            	
    	    	
    	    	    	if(message.duration != null)
            output.writeInt32(6, message.duration, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "prefab";
        	        	case 3: return "startPosition";
        	        	case 4: return "endPosition";
        	        	case 5: return "type";
        	        	case 6: return "duration";
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
    	    	__fieldMap.put("id", 1);
    	    	__fieldMap.put("prefab", 2);
    	    	__fieldMap.put("startPosition", 3);
    	    	__fieldMap.put("endPosition", 4);
    	    	__fieldMap.put("type", 5);
    	    	__fieldMap.put("duration", 6);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = VisualEffect.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static VisualEffect parseFrom(byte[] bytes) {
	VisualEffect message = new VisualEffect();
	ProtobufIOUtil.mergeFrom(bytes, message, VisualEffect.getSchema());
	return message;
}

public static VisualEffect parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	VisualEffect message = new VisualEffect();
	JsonIOUtil.mergeFrom(bytes, message, VisualEffect.getSchema(), false);
	return message;
}

public VisualEffect clone() {
	byte[] bytes = this.toByteArray();
	VisualEffect visualEffect = VisualEffect.parseFrom(bytes);
	return visualEffect;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, VisualEffect.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<VisualEffect> schema = VisualEffect.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, VisualEffect.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
