
package io.gamemachine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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





import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
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
    static final String defaultScope = NodeStatus.class.getSimpleName();

    	
							    public int containerId= 0;
		    			    
		
    
        	
							    public String clusterName= null;
		    			    
		
    
        	
							    public int lastUpdated= 0;
		    			    
		
    
        	
							    public String hostname= null;
		    			    
		
    
        	
							    public int port= 0;
		    			    
		
    
        	
							    public int clientCount= 0;
		    			    
		
    
        	
							    public double loadAverage= 0D;
		    			    
		
    
        	
							    public int heapUsed= 0;
		    			    
		
    
        	
					public Statistics statistics = null;
			    
		
    
        


    public NodeStatus()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("node_status_container_id",null);
    	    	    	    	    	    	model.set("node_status_cluster_name",null);
    	    	    	    	    	    	model.set("node_status_last_updated",null);
    	    	    	    	    	    	model.set("node_status_hostname",null);
    	    	    	    	    	    	model.set("node_status_port",null);
    	    	    	    	    	    	model.set("node_status_client_count",null);
    	    	    	    	    	    	model.set("node_status_load_average",null);
    	    	    	    	    	    	model.set("node_status_heap_used",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (containerId != null) {
    	       	    	model.setInteger("node_status_container_id",containerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (clusterName != null) {
    	       	    	model.setString("node_status_cluster_name",clusterName);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (lastUpdated != null) {
    	       	    	model.setInteger("node_status_last_updated",lastUpdated);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (hostname != null) {
    	       	    	model.setString("node_status_hostname",hostname);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (port != null) {
    	       	    	model.setInteger("node_status_port",port);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (clientCount != null) {
    	       	    	model.setInteger("node_status_client_count",clientCount);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (loadAverage != null) {
    	       	    	model.setDouble("node_status_load_average",loadAverage);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (heapUsed != null) {
    	       	    	model.setInteger("node_status_heap_used",heapUsed);
    	        		
    	//}
    	    	    	    }
    
	public static NodeStatus fromModel(Model model) {
		boolean hasFields = false;
    	NodeStatus message = new NodeStatus();
    	    	    	    	    	
    	    	    	Integer containerIdTestField = model.getInteger("node_status_container_id");
    	if (containerIdTestField != null) {
    		int containerIdField = containerIdTestField;
    		message.setContainerId(containerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String clusterNameTestField = model.getString("node_status_cluster_name");
    	if (clusterNameTestField != null) {
    		String clusterNameField = clusterNameTestField;
    		message.setClusterName(clusterNameField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer lastUpdatedTestField = model.getInteger("node_status_last_updated");
    	if (lastUpdatedTestField != null) {
    		int lastUpdatedField = lastUpdatedTestField;
    		message.setLastUpdated(lastUpdatedField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String hostnameTestField = model.getString("node_status_hostname");
    	if (hostnameTestField != null) {
    		String hostnameField = hostnameTestField;
    		message.setHostname(hostnameField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer portTestField = model.getInteger("node_status_port");
    	if (portTestField != null) {
    		int portField = portTestField;
    		message.setPort(portField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer clientCountTestField = model.getInteger("node_status_client_count");
    	if (clientCountTestField != null) {
    		int clientCountField = clientCountTestField;
    		message.setClientCount(clientCountField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Double loadAverageTestField = model.getDouble("node_status_load_average");
    	if (loadAverageTestField != null) {
    		double loadAverageField = loadAverageTestField;
    		message.setLoadAverage(loadAverageField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer heapUsedTestField = model.getInteger("node_status_heap_used");
    	if (heapUsedTestField != null) {
    		int heapUsedField = heapUsedTestField;
    		message.setHeapUsed(heapUsedField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public int getContainerId() {
		return containerId;
	}
	
	public NodeStatus setContainerId(int containerId) {
		this.containerId = containerId;
		return this;	}
	
		            
		public String getClusterName() {
		return clusterName;
	}
	
	public NodeStatus setClusterName(String clusterName) {
		this.clusterName = clusterName;
		return this;	}
	
		            
		public int getLastUpdated() {
		return lastUpdated;
	}
	
	public NodeStatus setLastUpdated(int lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;	}
	
		            
		public String getHostname() {
		return hostname;
	}
	
	public NodeStatus setHostname(String hostname) {
		this.hostname = hostname;
		return this;	}
	
		            
		public int getPort() {
		return port;
	}
	
	public NodeStatus setPort(int port) {
		this.port = port;
		return this;	}
	
		            
		public int getClientCount() {
		return clientCount;
	}
	
	public NodeStatus setClientCount(int clientCount) {
		this.clientCount = clientCount;
		return this;	}
	
		            
		public double getLoadAverage() {
		return loadAverage;
	}
	
	public NodeStatus setLoadAverage(double loadAverage) {
		this.loadAverage = loadAverage;
		return this;	}
	
		            
		public int getHeapUsed() {
		return heapUsed;
	}
	
	public NodeStatus setHeapUsed(int heapUsed) {
		this.heapUsed = heapUsed;
		return this;	}
	
		            
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
    	    	
    	    	//if(message.containerId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.containerId != null) {
            output.writeInt32(1, message.containerId, false);
        }
    	    	
    	            	
    	    	//if(message.clusterName == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.clusterName != null) {
            output.writeString(2, message.clusterName, false);
        }
    	    	
    	            	
    	    	//if(message.lastUpdated == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.lastUpdated != null) {
            output.writeInt32(3, message.lastUpdated, false);
        }
    	    	
    	            	
    	    	//if(message.hostname == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.hostname != null) {
            output.writeString(4, message.hostname, false);
        }
    	    	
    	            	
    	    	//if(message.port == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.port != null) {
            output.writeInt32(5, message.port, false);
        }
    	    	
    	            	
    	    	//if(message.clientCount == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.clientCount != null) {
            output.writeInt32(6, message.clientCount, false);
        }
    	    	
    	            	
    	    	//if(message.loadAverage == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Double)message.loadAverage != null) {
            output.writeDouble(7, message.loadAverage, false);
        }
    	    	
    	            	
    	    	//if(message.heapUsed == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.heapUsed != null) {
            output.writeInt32(8, message.heapUsed, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.statistics != null)
    		output.writeObject(9, message.statistics, Statistics.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START NodeStatus");
    	    	//if(this.containerId != null) {
    		System.out.println("containerId="+this.containerId);
    	//}
    	    	//if(this.clusterName != null) {
    		System.out.println("clusterName="+this.clusterName);
    	//}
    	    	//if(this.lastUpdated != null) {
    		System.out.println("lastUpdated="+this.lastUpdated);
    	//}
    	    	//if(this.hostname != null) {
    		System.out.println("hostname="+this.hostname);
    	//}
    	    	//if(this.port != null) {
    		System.out.println("port="+this.port);
    	//}
    	    	//if(this.clientCount != null) {
    		System.out.println("clientCount="+this.clientCount);
    	//}
    	    	//if(this.loadAverage != null) {
    		System.out.println("loadAverage="+this.loadAverage);
    	//}
    	    	//if(this.heapUsed != null) {
    		System.out.println("heapUsed="+this.heapUsed);
    	//}
    	    	//if(this.statistics != null) {
    		System.out.println("statistics="+this.statistics);
    	//}
    	    	System.out.println("END NodeStatus");
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
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, NodeStatus.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
