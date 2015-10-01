
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
public final class BuildableArea implements Externalizable, Message<BuildableArea>, Schema<BuildableArea>{



    public static Schema<BuildableArea> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildableArea getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildableArea DEFAULT_INSTANCE = new BuildableArea();

    			public String ownerId;
	    
        			public Float px;
	    
        			public Float py;
	    
        			public Float pz;
	    
        			public Integer sx;
	    
        			public Integer sy;
	    
        			public Integer sz;
	    
        			public Integer recordId;
	    
      
    public BuildableArea()
    {
        
    }


	

	    
    public Boolean hasOwnerId()  {
        return ownerId == null ? false : true;
    }
        
		public String getOwnerId() {
		return ownerId;
	}
	
	public BuildableArea setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		    
    public Boolean hasPx()  {
        return px == null ? false : true;
    }
        
		public Float getPx() {
		return px;
	}
	
	public BuildableArea setPx(Float px) {
		this.px = px;
		return this;	}
	
		    
    public Boolean hasPy()  {
        return py == null ? false : true;
    }
        
		public Float getPy() {
		return py;
	}
	
	public BuildableArea setPy(Float py) {
		this.py = py;
		return this;	}
	
		    
    public Boolean hasPz()  {
        return pz == null ? false : true;
    }
        
		public Float getPz() {
		return pz;
	}
	
	public BuildableArea setPz(Float pz) {
		this.pz = pz;
		return this;	}
	
		    
    public Boolean hasSx()  {
        return sx == null ? false : true;
    }
        
		public Integer getSx() {
		return sx;
	}
	
	public BuildableArea setSx(Integer sx) {
		this.sx = sx;
		return this;	}
	
		    
    public Boolean hasSy()  {
        return sy == null ? false : true;
    }
        
		public Integer getSy() {
		return sy;
	}
	
	public BuildableArea setSy(Integer sy) {
		this.sy = sy;
		return this;	}
	
		    
    public Boolean hasSz()  {
        return sz == null ? false : true;
    }
        
		public Integer getSz() {
		return sz;
	}
	
	public BuildableArea setSz(Integer sz) {
		this.sz = sz;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public BuildableArea setRecordId(Integer recordId) {
		this.recordId = recordId;
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

    public Schema<BuildableArea> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildableArea newMessage()
    {
        return new BuildableArea();
    }

    public Class<BuildableArea> typeClass()
    {
        return BuildableArea.class;
    }

    public String messageName()
    {
        return BuildableArea.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildableArea.class.getName();
    }

    public boolean isInitialized(BuildableArea message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildableArea message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.ownerId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.px = input.readFloat();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.py = input.readFloat();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.pz = input.readFloat();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.sx = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.sy = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.sz = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildableArea message) throws IOException
    {
    	    	
    	    	if(message.ownerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.ownerId != null)
            output.writeString(1, message.ownerId, false);
    	    	
    	            	
    	    	if(message.px == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.px != null)
            output.writeFloat(2, message.px, false);
    	    	
    	            	
    	    	if(message.py == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.py != null)
            output.writeFloat(3, message.py, false);
    	    	
    	            	
    	    	if(message.pz == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.pz != null)
            output.writeFloat(4, message.pz, false);
    	    	
    	            	
    	    	if(message.sx == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.sx != null)
            output.writeInt32(5, message.sx, false);
    	    	
    	            	
    	    	if(message.sy == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.sy != null)
            output.writeInt32(6, message.sy, false);
    	    	
    	            	
    	    	if(message.sz == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.sz != null)
            output.writeInt32(7, message.sz, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(8, message.recordId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "ownerId";
        	        	case 2: return "px";
        	        	case 3: return "py";
        	        	case 4: return "pz";
        	        	case 5: return "sx";
        	        	case 6: return "sy";
        	        	case 7: return "sz";
        	        	case 8: return "recordId";
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
    	    	__fieldMap.put("ownerId", 1);
    	    	__fieldMap.put("px", 2);
    	    	__fieldMap.put("py", 3);
    	    	__fieldMap.put("pz", 4);
    	    	__fieldMap.put("sx", 5);
    	    	__fieldMap.put("sy", 6);
    	    	__fieldMap.put("sz", 7);
    	    	__fieldMap.put("recordId", 8);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildableArea.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildableArea parseFrom(byte[] bytes) {
	BuildableArea message = new BuildableArea();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildableArea.getSchema());
	return message;
}

public static BuildableArea parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildableArea message = new BuildableArea();
	JsonIOUtil.mergeFrom(bytes, message, BuildableArea.getSchema(), false);
	return message;
}

public BuildableArea clone() {
	byte[] bytes = this.toByteArray();
	BuildableArea buildableArea = BuildableArea.parseFrom(bytes);
	return buildableArea;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildableArea.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildableArea> schema = BuildableArea.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildableArea.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
