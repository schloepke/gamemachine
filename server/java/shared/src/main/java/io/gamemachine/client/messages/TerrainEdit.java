
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
public final class TerrainEdit implements Externalizable, Message<TerrainEdit>, Schema<TerrainEdit>{

	public enum Type implements io.protostuff.EnumLite<Type>
    {
    	
    	    	NONE(0),    	    	DETAIL(1),    	    	ALPHA(2),    	    	HEIGHT(3),    	    	TREE(4);    	        
        public final int number;
        
        private Type (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Type valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (DETAIL);
    			    			case 2: return (ALPHA);
    			    			case 3: return (HEIGHT);
    			    			case 4: return (TREE);
    			                default: return null;
            }
        }
    }


    public static Schema<TerrainEdit> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TerrainEdit getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TerrainEdit DEFAULT_INSTANCE = new TerrainEdit();

    			public Integer x;
	    
        			public Integer y;
	    
        			public Integer detailLayer;
	    
        			public Integer value;
	    
        			public Type type; // = NONE:0;
	    
        			public String id;
	    
        			public Integer recordId;
	    
        			public Integer texture;
	    
        			public Float height;
	    
        			public Long createdAt;
	    
        			public String terrain;
	    
      
    public TerrainEdit()
    {
        
    }


	

	    
    public Boolean hasX()  {
        return x == null ? false : true;
    }
        
		public Integer getX() {
		return x;
	}
	
	public TerrainEdit setX(Integer x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Integer getY() {
		return y;
	}
	
	public TerrainEdit setY(Integer y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasDetailLayer()  {
        return detailLayer == null ? false : true;
    }
        
		public Integer getDetailLayer() {
		return detailLayer;
	}
	
	public TerrainEdit setDetailLayer(Integer detailLayer) {
		this.detailLayer = detailLayer;
		return this;	}
	
		    
    public Boolean hasValue()  {
        return value == null ? false : true;
    }
        
		public Integer getValue() {
		return value;
	}
	
	public TerrainEdit setValue(Integer value) {
		this.value = value;
		return this;	}
	
		    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public Type getType() {
		return type;
	}
	
	public TerrainEdit setType(Type type) {
		this.type = type;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public TerrainEdit setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public TerrainEdit setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasTexture()  {
        return texture == null ? false : true;
    }
        
		public Integer getTexture() {
		return texture;
	}
	
	public TerrainEdit setTexture(Integer texture) {
		this.texture = texture;
		return this;	}
	
		    
    public Boolean hasHeight()  {
        return height == null ? false : true;
    }
        
		public Float getHeight() {
		return height;
	}
	
	public TerrainEdit setHeight(Float height) {
		this.height = height;
		return this;	}
	
		    
    public Boolean hasCreatedAt()  {
        return createdAt == null ? false : true;
    }
        
		public Long getCreatedAt() {
		return createdAt;
	}
	
	public TerrainEdit setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
		return this;	}
	
		    
    public Boolean hasTerrain()  {
        return terrain == null ? false : true;
    }
        
		public String getTerrain() {
		return terrain;
	}
	
	public TerrainEdit setTerrain(String terrain) {
		this.terrain = terrain;
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

    public Schema<TerrainEdit> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TerrainEdit newMessage()
    {
        return new TerrainEdit();
    }

    public Class<TerrainEdit> typeClass()
    {
        return TerrainEdit.class;
    }

    public String messageName()
    {
        return TerrainEdit.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TerrainEdit.class.getName();
    }

    public boolean isInitialized(TerrainEdit message)
    {
        return true;
    }

    public void mergeFrom(Input input, TerrainEdit message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.x = input.readInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.y = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.detailLayer = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.value = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                    message.type = Type.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 6:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.texture = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.height = input.readFloat();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.createdAt = input.readInt64();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.terrain = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TerrainEdit message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.x != null)
            output.writeInt32(1, message.x, false);
    	    	
    	            	
    	    	
    	    	    	if(message.y != null)
            output.writeInt32(2, message.y, false);
    	    	
    	            	
    	    	
    	    	    	if(message.detailLayer != null)
            output.writeInt32(3, message.detailLayer, false);
    	    	
    	            	
    	    	
    	    	    	if(message.value != null)
            output.writeInt32(4, message.value, false);
    	    	
    	            	
    	    	
    	    	    	if(message.type != null)
    	 	output.writeEnum(5, message.type.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(6, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(7, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.texture != null)
            output.writeInt32(8, message.texture, false);
    	    	
    	            	
    	    	
    	    	    	if(message.height != null)
            output.writeFloat(9, message.height, false);
    	    	
    	            	
    	    	
    	    	    	if(message.createdAt != null)
            output.writeInt64(10, message.createdAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.terrain != null)
            output.writeString(11, message.terrain, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "x";
        	        	case 2: return "y";
        	        	case 3: return "detailLayer";
        	        	case 4: return "value";
        	        	case 5: return "type";
        	        	case 6: return "id";
        	        	case 7: return "recordId";
        	        	case 8: return "texture";
        	        	case 9: return "height";
        	        	case 10: return "createdAt";
        	        	case 11: return "terrain";
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
    	    	__fieldMap.put("x", 1);
    	    	__fieldMap.put("y", 2);
    	    	__fieldMap.put("detailLayer", 3);
    	    	__fieldMap.put("value", 4);
    	    	__fieldMap.put("type", 5);
    	    	__fieldMap.put("id", 6);
    	    	__fieldMap.put("recordId", 7);
    	    	__fieldMap.put("texture", 8);
    	    	__fieldMap.put("height", 9);
    	    	__fieldMap.put("createdAt", 10);
    	    	__fieldMap.put("terrain", 11);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TerrainEdit.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TerrainEdit parseFrom(byte[] bytes) {
	TerrainEdit message = new TerrainEdit();
	ProtobufIOUtil.mergeFrom(bytes, message, TerrainEdit.getSchema());
	return message;
}

public static TerrainEdit parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TerrainEdit message = new TerrainEdit();
	JsonIOUtil.mergeFrom(bytes, message, TerrainEdit.getSchema(), false);
	return message;
}

public TerrainEdit clone() {
	byte[] bytes = this.toByteArray();
	TerrainEdit terrainEdit = TerrainEdit.parseFrom(bytes);
	return terrainEdit;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TerrainEdit.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TerrainEdit> schema = TerrainEdit.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TerrainEdit.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
