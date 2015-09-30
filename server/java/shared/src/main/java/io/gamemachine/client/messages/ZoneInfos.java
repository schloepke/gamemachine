
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
public final class ZoneInfos implements Externalizable, Message<ZoneInfos>, Schema<ZoneInfos>{



    public static Schema<ZoneInfos> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ZoneInfos getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ZoneInfos DEFAULT_INSTANCE = new ZoneInfos();

        public List<ZoneInfo> zoneInfo;
	    			public String id;
	    
      
    public ZoneInfos()
    {
        
    }


	

	    
    public Boolean hasZoneInfo()  {
        return zoneInfo == null ? false : true;
    }
        
		public List<ZoneInfo> getZoneInfoList() {
		if(this.zoneInfo == null)
            this.zoneInfo = new ArrayList<ZoneInfo>();
		return zoneInfo;
	}

	public ZoneInfos setZoneInfoList(List<ZoneInfo> zoneInfo) {
		this.zoneInfo = zoneInfo;
		return this;
	}

	public ZoneInfo getZoneInfo(int index)  {
        return zoneInfo == null ? null : zoneInfo.get(index);
    }

    public int getZoneInfoCount()  {
        return zoneInfo == null ? 0 : zoneInfo.size();
    }

    public ZoneInfos addZoneInfo(ZoneInfo zoneInfo)  {
        if(this.zoneInfo == null)
            this.zoneInfo = new ArrayList<ZoneInfo>();
        this.zoneInfo.add(zoneInfo);
        return this;
    }
            	    	    	    	
    public ZoneInfos removeZoneInfoById(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByRecordId(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByNode(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.node.equals(obj.node)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByActorName(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.actorName.equals(obj.actorName)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByAssigned(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.assigned.equals(obj.assigned)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByNumber(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.number.equals(obj.number)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByHostname(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.hostname.equals(obj.hostname)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByCurrent(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.current.equals(obj.current)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public ZoneInfos setId(String id) {
		this.id = id;
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

    public Schema<ZoneInfos> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ZoneInfos newMessage()
    {
        return new ZoneInfos();
    }

    public Class<ZoneInfos> typeClass()
    {
        return ZoneInfos.class;
    }

    public String messageName()
    {
        return ZoneInfos.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ZoneInfos.class.getName();
    }

    public boolean isInitialized(ZoneInfos message)
    {
        return true;
    }

    public void mergeFrom(Input input, ZoneInfos message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.zoneInfo == null)
                        message.zoneInfo = new ArrayList<ZoneInfo>();
                                        message.zoneInfo.add(input.mergeObject(null, ZoneInfo.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ZoneInfos message) throws IOException
    {
    	    	
    	    	
    	    	if(message.zoneInfo != null)
        {
            for(ZoneInfo zoneInfo : message.zoneInfo)
            {
                if(zoneInfo != null) {
                   	    				output.writeObject(1, zoneInfo, ZoneInfo.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(2, message.id, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "zoneInfo";
        	        	case 2: return "id";
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
    	    	__fieldMap.put("zoneInfo", 1);
    	    	__fieldMap.put("id", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ZoneInfos.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ZoneInfos parseFrom(byte[] bytes) {
	ZoneInfos message = new ZoneInfos();
	ProtobufIOUtil.mergeFrom(bytes, message, ZoneInfos.getSchema());
	return message;
}

public static ZoneInfos parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ZoneInfos message = new ZoneInfos();
	JsonIOUtil.mergeFrom(bytes, message, ZoneInfos.getSchema(), false);
	return message;
}

public ZoneInfos clone() {
	byte[] bytes = this.toByteArray();
	ZoneInfos zoneInfos = ZoneInfos.parseFrom(bytes);
	return zoneInfos;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ZoneInfos.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ZoneInfos> schema = ZoneInfos.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ZoneInfos.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
