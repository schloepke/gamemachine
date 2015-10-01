
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
public final class PathData implements Externalizable, Message<PathData>, Schema<PathData>{



    public static Schema<PathData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PathData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PathData DEFAULT_INSTANCE = new PathData();

    			public GmVector3 startPoint;
	    
        			public GmVector3 endPoint;
	    
            public List<GmVector3> nodes;
	    			public String id;
	    
      
    public PathData()
    {
        
    }


	

	    
    public Boolean hasStartPoint()  {
        return startPoint == null ? false : true;
    }
        
		public GmVector3 getStartPoint() {
		return startPoint;
	}
	
	public PathData setStartPoint(GmVector3 startPoint) {
		this.startPoint = startPoint;
		return this;	}
	
		    
    public Boolean hasEndPoint()  {
        return endPoint == null ? false : true;
    }
        
		public GmVector3 getEndPoint() {
		return endPoint;
	}
	
	public PathData setEndPoint(GmVector3 endPoint) {
		this.endPoint = endPoint;
		return this;	}
	
		    
    public Boolean hasNodes()  {
        return nodes == null ? false : true;
    }
        
		public List<GmVector3> getNodesList() {
		if(this.nodes == null)
            this.nodes = new ArrayList<GmVector3>();
		return nodes;
	}

	public PathData setNodesList(List<GmVector3> nodes) {
		this.nodes = nodes;
		return this;
	}

	public GmVector3 getNodes(int index)  {
        return nodes == null ? null : nodes.get(index);
    }

    public int getNodesCount()  {
        return nodes == null ? 0 : nodes.size();
    }

    public PathData addNodes(GmVector3 nodes)  {
        if(this.nodes == null)
            this.nodes = new ArrayList<GmVector3>();
        this.nodes.add(nodes);
        return this;
    }
            	    	    	    	
    public PathData removeNodesByX(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByY(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByZ(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.z.equals(obj.z)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByXi(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.xi.equals(obj.xi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByYi(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.yi.equals(obj.yi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByZi(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.zi.equals(obj.zi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByVertice(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.vertice.equals(obj.vertice)) {
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
	
	public PathData setId(String id) {
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

    public Schema<PathData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PathData newMessage()
    {
        return new PathData();
    }

    public Class<PathData> typeClass()
    {
        return PathData.class;
    }

    public String messageName()
    {
        return PathData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PathData.class.getName();
    }

    public boolean isInitialized(PathData message)
    {
        return true;
    }

    public void mergeFrom(Input input, PathData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.startPoint = input.mergeObject(message.startPoint, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.endPoint = input.mergeObject(message.endPoint, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 3:
            	            		if(message.nodes == null)
                        message.nodes = new ArrayList<GmVector3>();
                                        message.nodes.add(input.mergeObject(null, GmVector3.getSchema()));
                                        break;
                            	            	case 4:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PathData message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.startPoint != null)
    		output.writeObject(1, message.startPoint, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.endPoint != null)
    		output.writeObject(2, message.endPoint, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	if(message.nodes != null)
        {
            for(GmVector3 nodes : message.nodes)
            {
                if(nodes != null) {
                   	    				output.writeObject(3, nodes, GmVector3.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(4, message.id, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "startPoint";
        	        	case 2: return "endPoint";
        	        	case 3: return "nodes";
        	        	case 4: return "id";
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
    	    	__fieldMap.put("startPoint", 1);
    	    	__fieldMap.put("endPoint", 2);
    	    	__fieldMap.put("nodes", 3);
    	    	__fieldMap.put("id", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PathData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PathData parseFrom(byte[] bytes) {
	PathData message = new PathData();
	ProtobufIOUtil.mergeFrom(bytes, message, PathData.getSchema());
	return message;
}

public static PathData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PathData message = new PathData();
	JsonIOUtil.mergeFrom(bytes, message, PathData.getSchema(), false);
	return message;
}

public PathData clone() {
	byte[] bytes = this.toByteArray();
	PathData pathData = PathData.parseFrom(bytes);
	return pathData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PathData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PathData> schema = PathData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PathData.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
