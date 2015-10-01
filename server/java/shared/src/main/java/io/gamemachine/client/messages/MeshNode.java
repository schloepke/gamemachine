
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
public final class MeshNode implements Externalizable, Message<MeshNode>, Schema<MeshNode>{



    public static Schema<MeshNode> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MeshNode getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MeshNode DEFAULT_INSTANCE = new MeshNode();

    			public GmVector3 position;
	    
            public List<GmVector3> connections;
	  
    public MeshNode()
    {
        
    }


	

	    
    public Boolean hasPosition()  {
        return position == null ? false : true;
    }
        
		public GmVector3 getPosition() {
		return position;
	}
	
	public MeshNode setPosition(GmVector3 position) {
		this.position = position;
		return this;	}
	
		    
    public Boolean hasConnections()  {
        return connections == null ? false : true;
    }
        
		public List<GmVector3> getConnectionsList() {
		if(this.connections == null)
            this.connections = new ArrayList<GmVector3>();
		return connections;
	}

	public MeshNode setConnectionsList(List<GmVector3> connections) {
		this.connections = connections;
		return this;
	}

	public GmVector3 getConnections(int index)  {
        return connections == null ? null : connections.get(index);
    }

    public int getConnectionsCount()  {
        return connections == null ? 0 : connections.size();
    }

    public MeshNode addConnections(GmVector3 connections)  {
        if(this.connections == null)
            this.connections = new ArrayList<GmVector3>();
        this.connections.add(connections);
        return this;
    }
            	    	    	    	
    public MeshNode removeConnectionsByX(GmVector3 connections)  {
    	if(this.connections == null)
           return this;
            
       	Iterator<GmVector3> itr = this.connections.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (connections.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public MeshNode removeConnectionsByY(GmVector3 connections)  {
    	if(this.connections == null)
           return this;
            
       	Iterator<GmVector3> itr = this.connections.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (connections.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public MeshNode removeConnectionsByZ(GmVector3 connections)  {
    	if(this.connections == null)
           return this;
            
       	Iterator<GmVector3> itr = this.connections.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (connections.z.equals(obj.z)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public MeshNode removeConnectionsByXi(GmVector3 connections)  {
    	if(this.connections == null)
           return this;
            
       	Iterator<GmVector3> itr = this.connections.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (connections.xi.equals(obj.xi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public MeshNode removeConnectionsByYi(GmVector3 connections)  {
    	if(this.connections == null)
           return this;
            
       	Iterator<GmVector3> itr = this.connections.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (connections.yi.equals(obj.yi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public MeshNode removeConnectionsByZi(GmVector3 connections)  {
    	if(this.connections == null)
           return this;
            
       	Iterator<GmVector3> itr = this.connections.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (connections.zi.equals(obj.zi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public MeshNode removeConnectionsByVertice(GmVector3 connections)  {
    	if(this.connections == null)
           return this;
            
       	Iterator<GmVector3> itr = this.connections.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (connections.vertice.equals(obj.vertice)) {
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

    public Schema<MeshNode> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MeshNode newMessage()
    {
        return new MeshNode();
    }

    public Class<MeshNode> typeClass()
    {
        return MeshNode.class;
    }

    public String messageName()
    {
        return MeshNode.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MeshNode.class.getName();
    }

    public boolean isInitialized(MeshNode message)
    {
        return true;
    }

    public void mergeFrom(Input input, MeshNode message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.position = input.mergeObject(message.position, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 2:
            	            		if(message.connections == null)
                        message.connections = new ArrayList<GmVector3>();
                                        message.connections.add(input.mergeObject(null, GmVector3.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MeshNode message) throws IOException
    {
    	    	
    	    	if(message.position == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.position != null)
    		output.writeObject(1, message.position, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	if(message.connections != null)
        {
            for(GmVector3 connections : message.connections)
            {
                if(connections != null) {
                   	    				output.writeObject(2, connections, GmVector3.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "position";
        	        	case 2: return "connections";
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
    	    	__fieldMap.put("position", 1);
    	    	__fieldMap.put("connections", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = MeshNode.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static MeshNode parseFrom(byte[] bytes) {
	MeshNode message = new MeshNode();
	ProtobufIOUtil.mergeFrom(bytes, message, MeshNode.getSchema());
	return message;
}

public static MeshNode parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	MeshNode message = new MeshNode();
	JsonIOUtil.mergeFrom(bytes, message, MeshNode.getSchema(), false);
	return message;
}

public MeshNode clone() {
	byte[] bytes = this.toByteArray();
	MeshNode meshNode = MeshNode.parseFrom(bytes);
	return meshNode;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, MeshNode.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<MeshNode> schema = MeshNode.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, MeshNode.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
