
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
public final class NodeStatus implements Externalizable, Message<NodeStatus>, Schema<NodeStatus>{



    public static Schema<NodeStatus> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static NodeStatus getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final NodeStatus DEFAULT_INSTANCE = new NodeStatus();

    			public Integer containerId;
	    
        			public String clusterName;
	    
        			public Integer lastUpdated;
	    
        			public String hostname;
	    
        			public Integer port;
	    
        			public Integer clientCount;
	    
        			public Double loadAverage;
	    
        			public Integer heapUsed;
	    
        			public Statistics statistics;
	    
      
    public NodeStatus()
    {
        
    }


	

	    
    public Boolean hasContainerId()  {
        return containerId == null ? false : true;
    }
        
		public Integer getContainerId() {
		return containerId;
	}
	
	public NodeStatus setContainerId(Integer containerId) {
		this.containerId = containerId;
		return this;	}
	
		    
    public Boolean hasClusterName()  {
        return clusterName == null ? false : true;
    }
        
		public String getClusterName() {
		return clusterName;
	}
	
	public NodeStatus setClusterName(String clusterName) {
		this.clusterName = clusterName;
		return this;	}
	
		    
    public Boolean hasLastUpdated()  {
        return lastUpdated == null ? false : true;
    }
        
		public Integer getLastUpdated() {
		return lastUpdated;
	}
	
	public NodeStatus setLastUpdated(Integer lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;	}
	
		    
    public Boolean hasHostname()  {
        return hostname == null ? false : true;
    }
        
		public String getHostname() {
		return hostname;
	}
	
	public NodeStatus setHostname(String hostname) {
		this.hostname = hostname;
		return this;	}
	
		    
    public Boolean hasPort()  {
        return port == null ? false : true;
    }
        
		public Integer getPort() {
		return port;
	}
	
	public NodeStatus setPort(Integer port) {
		this.port = port;
		return this;	}
	
		    
    public Boolean hasClientCount()  {
        return clientCount == null ? false : true;
    }
        
		public Integer getClientCount() {
		return clientCount;
	}
	
	public NodeStatus setClientCount(Integer clientCount) {
		this.clientCount = clientCount;
		return this;	}
	
		    
    public Boolean hasLoadAverage()  {
        return loadAverage == null ? false : true;
    }
        
		public Double getLoadAverage() {
		return loadAverage;
	}
	
	public NodeStatus setLoadAverage(Double loadAverage) {
		this.loadAverage = loadAverage;
		return this;	}
	
		    
    public Boolean hasHeapUsed()  {
        return heapUsed == null ? false : true;
    }
        
		public Integer getHeapUsed() {
		return heapUsed;
	}
	
	public NodeStatus setHeapUsed(Integer heapUsed) {
		this.heapUsed = heapUsed;
		return this;	}
	
		    
    public Boolean hasStatistics()  {
        return statistics == null ? false : true;
    }
        
		public Statistics getStatistics() {
		return statistics;
	}
	
	public NodeStatus setStatistics(Statistics statistics) {
		this.statistics = statistics;
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

    public Schema<NodeStatus> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public NodeStatus newMessage()
    {
        return new NodeStatus();
    }

    public Class<NodeStatus> typeClass()
    {
        return NodeStatus.class;
    }

    public String messageName()
    {
        return NodeStatus.class.getSimpleName();
    }

    public String messageFullName()
    {
        return NodeStatus.class.getName();
    }

    public boolean isInitialized(NodeStatus message)
    {
        return true;
    }

    public void mergeFrom(Input input, NodeStatus message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.containerId = input.readInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.clusterName = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.lastUpdated = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.hostname = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.port = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.clientCount = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.loadAverage = input.readDouble();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.heapUsed = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.statistics = input.mergeObject(message.statistics, Statistics.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, NodeStatus message) throws IOException
    {
    	    	
    	    	if(message.containerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.containerId != null)
            output.writeInt32(1, message.containerId, false);
    	    	
    	            	
    	    	if(message.clusterName == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.clusterName != null)
            output.writeString(2, message.clusterName, false);
    	    	
    	            	
    	    	if(message.lastUpdated == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.lastUpdated != null)
            output.writeInt32(3, message.lastUpdated, false);
    	    	
    	            	
    	    	if(message.hostname == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.hostname != null)
            output.writeString(4, message.hostname, false);
    	    	
    	            	
    	    	if(message.port == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.port != null)
            output.writeInt32(5, message.port, false);
    	    	
    	            	
    	    	if(message.clientCount == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.clientCount != null)
            output.writeInt32(6, message.clientCount, false);
    	    	
    	            	
    	    	if(message.loadAverage == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.loadAverage != null)
            output.writeDouble(7, message.loadAverage, false);
    	    	
    	            	
    	    	if(message.heapUsed == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.heapUsed != null)
            output.writeInt32(8, message.heapUsed, false);
    	    	
    	            	
    	    	
    	    	    	if(message.statistics != null)
    		output.writeObject(9, message.statistics, Statistics.getSchema(), false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "containerId";
        	        	case 2: return "clusterName";
        	        	case 3: return "lastUpdated";
        	        	case 4: return "hostname";
        	        	case 5: return "port";
        	        	case 6: return "clientCount";
        	        	case 7: return "loadAverage";
        	        	case 8: return "heapUsed";
        	        	case 9: return "statistics";
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
    	    	__fieldMap.put("containerId", 1);
    	    	__fieldMap.put("clusterName", 2);
    	    	__fieldMap.put("lastUpdated", 3);
    	    	__fieldMap.put("hostname", 4);
    	    	__fieldMap.put("port", 5);
    	    	__fieldMap.put("clientCount", 6);
    	    	__fieldMap.put("loadAverage", 7);
    	    	__fieldMap.put("heapUsed", 8);
    	    	__fieldMap.put("statistics", 9);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = NodeStatus.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static NodeStatus parseFrom(byte[] bytes) {
	NodeStatus message = new NodeStatus();
	ProtobufIOUtil.mergeFrom(bytes, message, NodeStatus.getSchema());
	return message;
}

public static NodeStatus parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	NodeStatus message = new NodeStatus();
	JsonIOUtil.mergeFrom(bytes, message, NodeStatus.getSchema(), false);
	return message;
}

public NodeStatus clone() {
	byte[] bytes = this.toByteArray();
	NodeStatus nodeStatus = NodeStatus.parseFrom(bytes);
	return nodeStatus;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, NodeStatus.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<NodeStatus> schema = NodeStatus.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, NodeStatus.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
