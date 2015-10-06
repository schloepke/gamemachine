
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
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class NodeStatuses implements Externalizable, Message<NodeStatuses>, Schema<NodeStatuses>{

private static final Logger logger = LoggerFactory.getLogger(NodeStatuses.class);



    public static Schema<NodeStatuses> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static NodeStatuses getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final NodeStatuses DEFAULT_INSTANCE = new NodeStatuses();
    static final String defaultScope = NodeStatuses.class.getSimpleName();

        public List<NodeStatus> nodeStatus;
	    


    public NodeStatuses()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static NodeStatuses fromModel(Model model) {
		boolean hasFields = false;
    	NodeStatuses message = new NodeStatuses();
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public List<NodeStatus> getNodeStatusList() {
		if(this.nodeStatus == null)
            this.nodeStatus = new ArrayList<NodeStatus>();
		return nodeStatus;
	}

	public NodeStatuses setNodeStatusList(List<NodeStatus> nodeStatus) {
		this.nodeStatus = nodeStatus;
		return this;
	}

	public NodeStatus getNodeStatus(int index)  {
        return nodeStatus == null ? null : nodeStatus.get(index);
    }

    public int getNodeStatusCount()  {
        return nodeStatus == null ? 0 : nodeStatus.size();
    }

    public NodeStatuses addNodeStatus(NodeStatus nodeStatus)  {
        if(this.nodeStatus == null)
            this.nodeStatus = new ArrayList<NodeStatus>();
        this.nodeStatus.add(nodeStatus);
        return this;
    }
            	    	    	    	
    public NodeStatuses removeNodeStatusByContainerId(NodeStatus nodeStatus)  {
    	if(this.nodeStatus == null)
           return this;
            
       	Iterator<NodeStatus> itr = this.nodeStatus.iterator();
       	while (itr.hasNext()) {
    	NodeStatus obj = itr.next();
    	
    	    		if (nodeStatus.containerId == obj.containerId) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public NodeStatuses removeNodeStatusByClusterName(NodeStatus nodeStatus)  {
    	if(this.nodeStatus == null)
           return this;
            
       	Iterator<NodeStatus> itr = this.nodeStatus.iterator();
       	while (itr.hasNext()) {
    	NodeStatus obj = itr.next();
    	
    	    		if (nodeStatus.clusterName.equals(obj.clusterName)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public NodeStatuses removeNodeStatusByLastUpdated(NodeStatus nodeStatus)  {
    	if(this.nodeStatus == null)
           return this;
            
       	Iterator<NodeStatus> itr = this.nodeStatus.iterator();
       	while (itr.hasNext()) {
    	NodeStatus obj = itr.next();
    	
    	    		if (nodeStatus.lastUpdated == obj.lastUpdated) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public NodeStatuses removeNodeStatusByHostname(NodeStatus nodeStatus)  {
    	if(this.nodeStatus == null)
           return this;
            
       	Iterator<NodeStatus> itr = this.nodeStatus.iterator();
       	while (itr.hasNext()) {
    	NodeStatus obj = itr.next();
    	
    	    		if (nodeStatus.hostname.equals(obj.hostname)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public NodeStatuses removeNodeStatusByPort(NodeStatus nodeStatus)  {
    	if(this.nodeStatus == null)
           return this;
            
       	Iterator<NodeStatus> itr = this.nodeStatus.iterator();
       	while (itr.hasNext()) {
    	NodeStatus obj = itr.next();
    	
    	    		if (nodeStatus.port == obj.port) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public NodeStatuses removeNodeStatusByClientCount(NodeStatus nodeStatus)  {
    	if(this.nodeStatus == null)
           return this;
            
       	Iterator<NodeStatus> itr = this.nodeStatus.iterator();
       	while (itr.hasNext()) {
    	NodeStatus obj = itr.next();
    	
    	    		if (nodeStatus.clientCount == obj.clientCount) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public NodeStatuses removeNodeStatusByLoadAverage(NodeStatus nodeStatus)  {
    	if(this.nodeStatus == null)
           return this;
            
       	Iterator<NodeStatus> itr = this.nodeStatus.iterator();
       	while (itr.hasNext()) {
    	NodeStatus obj = itr.next();
    	
    	    		if (nodeStatus.loadAverage == obj.loadAverage) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public NodeStatuses removeNodeStatusByHeapUsed(NodeStatus nodeStatus)  {
    	if(this.nodeStatus == null)
           return this;
            
       	Iterator<NodeStatus> itr = this.nodeStatus.iterator();
       	while (itr.hasNext()) {
    	NodeStatus obj = itr.next();
    	
    	    		if (nodeStatus.heapUsed == obj.heapUsed) {
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

    public Schema<NodeStatuses> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public NodeStatuses newMessage()
    {
        return new NodeStatuses();
    }

    public Class<NodeStatuses> typeClass()
    {
        return NodeStatuses.class;
    }

    public String messageName()
    {
        return NodeStatuses.class.getSimpleName();
    }

    public String messageFullName()
    {
        return NodeStatuses.class.getName();
    }

    public boolean isInitialized(NodeStatuses message)
    {
        return true;
    }

    public void mergeFrom(Input input, NodeStatuses message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.nodeStatus == null)
                        message.nodeStatus = new ArrayList<NodeStatus>();
                                        message.nodeStatus.add(input.mergeObject(null, NodeStatus.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, NodeStatuses message) throws IOException
    {
    	    	
    	    	
    	    	if(message.nodeStatus != null)
        {
            for(NodeStatus nodeStatus : message.nodeStatus)
            {
                if( (NodeStatus) nodeStatus != null) {
                   	    				output.writeObject(1, nodeStatus, NodeStatus.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START NodeStatuses");
    	    	//if(this.nodeStatus != null) {
    		System.out.println("nodeStatus="+this.nodeStatus);
    	//}
    	    	System.out.println("END NodeStatuses");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "nodeStatus";
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
    	    	__fieldMap.put("nodeStatus", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = NodeStatuses.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static NodeStatuses parseFrom(byte[] bytes) {
	NodeStatuses message = new NodeStatuses();
	ProtobufIOUtil.mergeFrom(bytes, message, NodeStatuses.getSchema());
	return message;
}

public static NodeStatuses parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	NodeStatuses message = new NodeStatuses();
	JsonIOUtil.mergeFrom(bytes, message, NodeStatuses.getSchema(), false);
	return message;
}

public NodeStatuses clone() {
	byte[] bytes = this.toByteArray();
	NodeStatuses nodeStatuses = NodeStatuses.parseFrom(bytes);
	return nodeStatuses;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, NodeStatuses.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<NodeStatuses> schema = NodeStatuses.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, NodeStatuses.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, NodeStatuses.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
