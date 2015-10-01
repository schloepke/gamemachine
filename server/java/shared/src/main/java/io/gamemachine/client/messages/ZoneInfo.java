
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
public final class ZoneInfo implements Externalizable, Message<ZoneInfo>, Schema<ZoneInfo>{



    public static Schema<ZoneInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ZoneInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ZoneInfo DEFAULT_INSTANCE = new ZoneInfo();

    			public String id;
	    
        			public Integer recordId;
	    
        			public String node;
	    
        			public String actorName;
	    
        			public Boolean assigned;
	    
        			public Integer number;
	    
        			public String hostname;
	    
        			public Boolean current;
	    
      
    public ZoneInfo()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public ZoneInfo setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public ZoneInfo setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasNode()  {
        return node == null ? false : true;
    }
        
		public String getNode() {
		return node;
	}
	
	public ZoneInfo setNode(String node) {
		this.node = node;
		return this;	}
	
		    
    public Boolean hasActorName()  {
        return actorName == null ? false : true;
    }
        
		public String getActorName() {
		return actorName;
	}
	
	public ZoneInfo setActorName(String actorName) {
		this.actorName = actorName;
		return this;	}
	
		    
    public Boolean hasAssigned()  {
        return assigned == null ? false : true;
    }
        
		public Boolean getAssigned() {
		return assigned;
	}
	
	public ZoneInfo setAssigned(Boolean assigned) {
		this.assigned = assigned;
		return this;	}
	
		    
    public Boolean hasNumber()  {
        return number == null ? false : true;
    }
        
		public Integer getNumber() {
		return number;
	}
	
	public ZoneInfo setNumber(Integer number) {
		this.number = number;
		return this;	}
	
		    
    public Boolean hasHostname()  {
        return hostname == null ? false : true;
    }
        
		public String getHostname() {
		return hostname;
	}
	
	public ZoneInfo setHostname(String hostname) {
		this.hostname = hostname;
		return this;	}
	
		    
    public Boolean hasCurrent()  {
        return current == null ? false : true;
    }
        
		public Boolean getCurrent() {
		return current;
	}
	
	public ZoneInfo setCurrent(Boolean current) {
		this.current = current;
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

    public Schema<ZoneInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ZoneInfo newMessage()
    {
        return new ZoneInfo();
    }

    public Class<ZoneInfo> typeClass()
    {
        return ZoneInfo.class;
    }

    public String messageName()
    {
        return ZoneInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ZoneInfo.class.getName();
    }

    public boolean isInitialized(ZoneInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ZoneInfo message) throws IOException
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
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.node = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.actorName = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.assigned = input.readBool();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.number = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.hostname = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.current = input.readBool();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ZoneInfo message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(2, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.node != null)
            output.writeString(3, message.node, false);
    	    	
    	            	
    	    	if(message.actorName == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.actorName != null)
            output.writeString(4, message.actorName, false);
    	    	
    	            	
    	    	
    	    	    	if(message.assigned != null)
            output.writeBool(5, message.assigned, false);
    	    	
    	            	
    	    	
    	    	    	if(message.number != null)
            output.writeInt32(6, message.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.hostname != null)
            output.writeString(7, message.hostname, false);
    	    	
    	            	
    	    	
    	    	    	if(message.current != null)
            output.writeBool(8, message.current, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "recordId";
        	        	case 3: return "node";
        	        	case 4: return "actorName";
        	        	case 5: return "assigned";
        	        	case 6: return "number";
        	        	case 7: return "hostname";
        	        	case 8: return "current";
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
    	    	__fieldMap.put("recordId", 2);
    	    	__fieldMap.put("node", 3);
    	    	__fieldMap.put("actorName", 4);
    	    	__fieldMap.put("assigned", 5);
    	    	__fieldMap.put("number", 6);
    	    	__fieldMap.put("hostname", 7);
    	    	__fieldMap.put("current", 8);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ZoneInfo.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ZoneInfo parseFrom(byte[] bytes) {
	ZoneInfo message = new ZoneInfo();
	ProtobufIOUtil.mergeFrom(bytes, message, ZoneInfo.getSchema());
	return message;
}

public static ZoneInfo parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ZoneInfo message = new ZoneInfo();
	JsonIOUtil.mergeFrom(bytes, message, ZoneInfo.getSchema(), false);
	return message;
}

public ZoneInfo clone() {
	byte[] bytes = this.toByteArray();
	ZoneInfo zoneInfo = ZoneInfo.parseFrom(bytes);
	return zoneInfo;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ZoneInfo.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ZoneInfo> schema = ZoneInfo.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ZoneInfo.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
