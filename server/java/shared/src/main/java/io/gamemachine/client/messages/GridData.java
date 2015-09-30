
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
public final class GridData implements Externalizable, Message<GridData>, Schema<GridData>{



    public static Schema<GridData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GridData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GridData DEFAULT_INSTANCE = new GridData();

        public List<GridNode> nodes;
	    			public Integer w;
	    
        			public Integer h;
	    
            public List<GridVerticle> gridVerticles;
	  
    public GridData()
    {
        
    }


	

	    
    public Boolean hasNodes()  {
        return nodes == null ? false : true;
    }
        
		public List<GridNode> getNodesList() {
		if(this.nodes == null)
            this.nodes = new ArrayList<GridNode>();
		return nodes;
	}

	public GridData setNodesList(List<GridNode> nodes) {
		this.nodes = nodes;
		return this;
	}

	public GridNode getNodes(int index)  {
        return nodes == null ? null : nodes.get(index);
    }

    public int getNodesCount()  {
        return nodes == null ? 0 : nodes.size();
    }

    public GridData addNodes(GridNode nodes)  {
        if(this.nodes == null)
            this.nodes = new ArrayList<GridNode>();
        this.nodes.add(nodes);
        return this;
    }
            	    	    	    	    	
    public GridData removeNodesBySlope(GridNode nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GridNode> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GridNode obj = itr.next();
    	
    	    		if (nodes.slope.equals(obj.slope)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		    
    public Boolean hasW()  {
        return w == null ? false : true;
    }
        
		public Integer getW() {
		return w;
	}
	
	public GridData setW(Integer w) {
		this.w = w;
		return this;	}
	
		    
    public Boolean hasH()  {
        return h == null ? false : true;
    }
        
		public Integer getH() {
		return h;
	}
	
	public GridData setH(Integer h) {
		this.h = h;
		return this;	}
	
		    
    public Boolean hasGridVerticles()  {
        return gridVerticles == null ? false : true;
    }
        
		public List<GridVerticle> getGridVerticlesList() {
		if(this.gridVerticles == null)
            this.gridVerticles = new ArrayList<GridVerticle>();
		return gridVerticles;
	}

	public GridData setGridVerticlesList(List<GridVerticle> gridVerticles) {
		this.gridVerticles = gridVerticles;
		return this;
	}

	public GridVerticle getGridVerticles(int index)  {
        return gridVerticles == null ? null : gridVerticles.get(index);
    }

    public int getGridVerticlesCount()  {
        return gridVerticles == null ? 0 : gridVerticles.size();
    }

    public GridData addGridVerticles(GridVerticle gridVerticles)  {
        if(this.gridVerticles == null)
            this.gridVerticles = new ArrayList<GridVerticle>();
        this.gridVerticles.add(gridVerticles);
        return this;
    }
            	    	    	    	
    public GridData removeGridVerticlesByX(GridVerticle gridVerticles)  {
    	if(this.gridVerticles == null)
           return this;
            
       	Iterator<GridVerticle> itr = this.gridVerticles.iterator();
       	while (itr.hasNext()) {
    	GridVerticle obj = itr.next();
    	
    	    		if (gridVerticles.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GridData removeGridVerticlesByY(GridVerticle gridVerticles)  {
    	if(this.gridVerticles == null)
           return this;
            
       	Iterator<GridVerticle> itr = this.gridVerticles.iterator();
       	while (itr.hasNext()) {
    	GridVerticle obj = itr.next();
    	
    	    		if (gridVerticles.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	        	
    
    
    
	
  
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

    public Schema<GridData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GridData newMessage()
    {
        return new GridData();
    }

    public Class<GridData> typeClass()
    {
        return GridData.class;
    }

    public String messageName()
    {
        return GridData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GridData.class.getName();
    }

    public boolean isInitialized(GridData message)
    {
        return true;
    }

    public void mergeFrom(Input input, GridData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.nodes == null)
                        message.nodes = new ArrayList<GridNode>();
                                        message.nodes.add(input.mergeObject(null, GridNode.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.w = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.h = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	            		if(message.gridVerticles == null)
                        message.gridVerticles = new ArrayList<GridVerticle>();
                                        message.gridVerticles.add(input.mergeObject(null, GridVerticle.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GridData message) throws IOException
    {
    	    	
    	    	
    	    	if(message.nodes != null)
        {
            for(GridNode nodes : message.nodes)
            {
                if(nodes != null) {
                   	    				output.writeObject(1, nodes, GridNode.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	if(message.w == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.w != null)
            output.writeInt32(2, message.w, false);
    	    	
    	            	
    	    	if(message.h == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.h != null)
            output.writeInt32(3, message.h, false);
    	    	
    	            	
    	    	
    	    	if(message.gridVerticles != null)
        {
            for(GridVerticle gridVerticles : message.gridVerticles)
            {
                if(gridVerticles != null) {
                   	    				output.writeObject(4, gridVerticles, GridVerticle.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "nodes";
        	        	case 2: return "w";
        	        	case 3: return "h";
        	        	case 4: return "gridVerticles";
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
    	    	__fieldMap.put("nodes", 1);
    	    	__fieldMap.put("w", 2);
    	    	__fieldMap.put("h", 3);
    	    	__fieldMap.put("gridVerticles", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GridData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GridData parseFrom(byte[] bytes) {
	GridData message = new GridData();
	ProtobufIOUtil.mergeFrom(bytes, message, GridData.getSchema());
	return message;
}

public static GridData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GridData message = new GridData();
	JsonIOUtil.mergeFrom(bytes, message, GridData.getSchema(), false);
	return message;
}

public GridData clone() {
	byte[] bytes = this.toByteArray();
	GridData gridData = GridData.parseFrom(bytes);
	return gridData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GridData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GridData> schema = GridData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GridData.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
