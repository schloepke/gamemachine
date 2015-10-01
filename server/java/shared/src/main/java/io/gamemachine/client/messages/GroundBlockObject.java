
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
public final class GroundBlockObject implements Externalizable, Message<GroundBlockObject>, Schema<GroundBlockObject>{

	public enum Status implements io.protostuff.EnumLite<Status>
    {
    	
    	    	NONE(0),    	    	PLACED(1),    	    	CLEARED(2),    	    	TOP(3);    	        
        public final int number;
        
        private Status (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Status valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (PLACED);
    			    			case 2: return (CLEARED);
    			    			case 3: return (TOP);
    			                default: return null;
            }
        }
    }


    public static Schema<GroundBlockObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GroundBlockObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GroundBlockObject DEFAULT_INSTANCE = new GroundBlockObject();

        public List<GmVector3> vertices;
	    			public GmVector3 position;
	    
        			public GmQuaternion rotation;
	    
        			public String id;
	    
        			public Integer verticeCount;
	    
        			public String tag;
	    
        			public String layer;
	    
        			public Integer gbLayer;
	    
        			public Boolean molded;
	    
        			public Status status; // = NONE:0;
	    
        			public Boolean canRemove;
	    
        			public Boolean isTop;
	    
        			public Integer gbType;
	    
      
    public GroundBlockObject()
    {
        
    }


	

	    
    public Boolean hasVertices()  {
        return vertices == null ? false : true;
    }
        
		public List<GmVector3> getVerticesList() {
		if(this.vertices == null)
            this.vertices = new ArrayList<GmVector3>();
		return vertices;
	}

	public GroundBlockObject setVerticesList(List<GmVector3> vertices) {
		this.vertices = vertices;
		return this;
	}

	public GmVector3 getVertices(int index)  {
        return vertices == null ? null : vertices.get(index);
    }

    public int getVerticesCount()  {
        return vertices == null ? 0 : vertices.size();
    }

    public GroundBlockObject addVertices(GmVector3 vertices)  {
        if(this.vertices == null)
            this.vertices = new ArrayList<GmVector3>();
        this.vertices.add(vertices);
        return this;
    }
            	    	    	    	
    public GroundBlockObject removeVerticesByX(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByY(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByZ(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.z.equals(obj.z)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByXi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.xi.equals(obj.xi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByYi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.yi.equals(obj.yi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByZi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.zi.equals(obj.zi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByVertice(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.vertice.equals(obj.vertice)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		    
    public Boolean hasPosition()  {
        return position == null ? false : true;
    }
        
		public GmVector3 getPosition() {
		return position;
	}
	
	public GroundBlockObject setPosition(GmVector3 position) {
		this.position = position;
		return this;	}
	
		    
    public Boolean hasRotation()  {
        return rotation == null ? false : true;
    }
        
		public GmQuaternion getRotation() {
		return rotation;
	}
	
	public GroundBlockObject setRotation(GmQuaternion rotation) {
		this.rotation = rotation;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public GroundBlockObject setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasVerticeCount()  {
        return verticeCount == null ? false : true;
    }
        
		public Integer getVerticeCount() {
		return verticeCount;
	}
	
	public GroundBlockObject setVerticeCount(Integer verticeCount) {
		this.verticeCount = verticeCount;
		return this;	}
	
		    
    public Boolean hasTag()  {
        return tag == null ? false : true;
    }
        
		public String getTag() {
		return tag;
	}
	
	public GroundBlockObject setTag(String tag) {
		this.tag = tag;
		return this;	}
	
		    
    public Boolean hasLayer()  {
        return layer == null ? false : true;
    }
        
		public String getLayer() {
		return layer;
	}
	
	public GroundBlockObject setLayer(String layer) {
		this.layer = layer;
		return this;	}
	
		    
    public Boolean hasGbLayer()  {
        return gbLayer == null ? false : true;
    }
        
		public Integer getGbLayer() {
		return gbLayer;
	}
	
	public GroundBlockObject setGbLayer(Integer gbLayer) {
		this.gbLayer = gbLayer;
		return this;	}
	
		    
    public Boolean hasMolded()  {
        return molded == null ? false : true;
    }
        
		public Boolean getMolded() {
		return molded;
	}
	
	public GroundBlockObject setMolded(Boolean molded) {
		this.molded = molded;
		return this;	}
	
		    
    public Boolean hasStatus()  {
        return status == null ? false : true;
    }
        
		public Status getStatus() {
		return status;
	}
	
	public GroundBlockObject setStatus(Status status) {
		this.status = status;
		return this;	}
	
		    
    public Boolean hasCanRemove()  {
        return canRemove == null ? false : true;
    }
        
		public Boolean getCanRemove() {
		return canRemove;
	}
	
	public GroundBlockObject setCanRemove(Boolean canRemove) {
		this.canRemove = canRemove;
		return this;	}
	
		    
    public Boolean hasIsTop()  {
        return isTop == null ? false : true;
    }
        
		public Boolean getIsTop() {
		return isTop;
	}
	
	public GroundBlockObject setIsTop(Boolean isTop) {
		this.isTop = isTop;
		return this;	}
	
		    
    public Boolean hasGbType()  {
        return gbType == null ? false : true;
    }
        
		public Integer getGbType() {
		return gbType;
	}
	
	public GroundBlockObject setGbType(Integer gbType) {
		this.gbType = gbType;
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

    public Schema<GroundBlockObject> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GroundBlockObject newMessage()
    {
        return new GroundBlockObject();
    }

    public Class<GroundBlockObject> typeClass()
    {
        return GroundBlockObject.class;
    }

    public String messageName()
    {
        return GroundBlockObject.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GroundBlockObject.class.getName();
    }

    public boolean isInitialized(GroundBlockObject message)
    {
        return true;
    }

    public void mergeFrom(Input input, GroundBlockObject message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.vertices == null)
                        message.vertices = new ArrayList<GmVector3>();
                                        message.vertices.add(input.mergeObject(null, GmVector3.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.position = input.mergeObject(message.position, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.rotation = input.mergeObject(message.rotation, GmQuaternion.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.verticeCount = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.tag = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.layer = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.gbLayer = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.molded = input.readBool();
                	break;
                	                	
                            	            	case 10:
            	                	                    message.status = Status.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 11:
            	                	                	message.canRemove = input.readBool();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.isTop = input.readBool();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.gbType = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GroundBlockObject message) throws IOException
    {
    	    	
    	    	
    	    	if(message.vertices != null)
        {
            for(GmVector3 vertices : message.vertices)
            {
                if(vertices != null) {
                   	    				output.writeObject(1, vertices, GmVector3.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.position != null)
    		output.writeObject(2, message.position, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.rotation != null)
    		output.writeObject(3, message.rotation, GmQuaternion.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(4, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.verticeCount != null)
            output.writeInt32(5, message.verticeCount, false);
    	    	
    	            	
    	    	
    	    	    	if(message.tag != null)
            output.writeString(6, message.tag, false);
    	    	
    	            	
    	    	
    	    	    	if(message.layer != null)
            output.writeString(7, message.layer, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gbLayer != null)
            output.writeInt32(8, message.gbLayer, false);
    	    	
    	            	
    	    	
    	    	    	if(message.molded != null)
            output.writeBool(9, message.molded, false);
    	    	
    	            	
    	    	
    	    	    	if(message.status != null)
    	 	output.writeEnum(10, message.status.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.canRemove != null)
            output.writeBool(11, message.canRemove, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isTop != null)
            output.writeBool(12, message.isTop, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gbType != null)
            output.writeInt32(13, message.gbType, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "vertices";
        	        	case 2: return "position";
        	        	case 3: return "rotation";
        	        	case 4: return "id";
        	        	case 5: return "verticeCount";
        	        	case 6: return "tag";
        	        	case 7: return "layer";
        	        	case 8: return "gbLayer";
        	        	case 9: return "molded";
        	        	case 10: return "status";
        	        	case 11: return "canRemove";
        	        	case 12: return "isTop";
        	        	case 13: return "gbType";
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
    	    	__fieldMap.put("vertices", 1);
    	    	__fieldMap.put("position", 2);
    	    	__fieldMap.put("rotation", 3);
    	    	__fieldMap.put("id", 4);
    	    	__fieldMap.put("verticeCount", 5);
    	    	__fieldMap.put("tag", 6);
    	    	__fieldMap.put("layer", 7);
    	    	__fieldMap.put("gbLayer", 8);
    	    	__fieldMap.put("molded", 9);
    	    	__fieldMap.put("status", 10);
    	    	__fieldMap.put("canRemove", 11);
    	    	__fieldMap.put("isTop", 12);
    	    	__fieldMap.put("gbType", 13);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GroundBlockObject.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GroundBlockObject parseFrom(byte[] bytes) {
	GroundBlockObject message = new GroundBlockObject();
	ProtobufIOUtil.mergeFrom(bytes, message, GroundBlockObject.getSchema());
	return message;
}

public static GroundBlockObject parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GroundBlockObject message = new GroundBlockObject();
	JsonIOUtil.mergeFrom(bytes, message, GroundBlockObject.getSchema(), false);
	return message;
}

public GroundBlockObject clone() {
	byte[] bytes = this.toByteArray();
	GroundBlockObject groundBlockObject = GroundBlockObject.parseFrom(bytes);
	return groundBlockObject;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GroundBlockObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GroundBlockObject> schema = GroundBlockObject.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GroundBlockObject.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
