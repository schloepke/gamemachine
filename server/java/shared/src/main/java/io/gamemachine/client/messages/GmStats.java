
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
public final class GmStats implements Externalizable, Message<GmStats>, Schema<GmStats>{



    public static Schema<GmStats> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GmStats getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GmStats DEFAULT_INSTANCE = new GmStats();

    			public String id;
	    
        			public String action;
	    
        			public Long messageCountIn;
	    
        			public Long messageCountOut;
	    
        			public Long messageCountInOut;
	    
        			public Long bytesOut;
	    
        			public Integer connectionCount;
	    
        			public Long playerBytesOut;
	    
        			public String playerId;
	    
        			public Long bytesPerMessageOut;
	    
      
    public GmStats()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public GmStats setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasAction()  {
        return action == null ? false : true;
    }
        
		public String getAction() {
		return action;
	}
	
	public GmStats setAction(String action) {
		this.action = action;
		return this;	}
	
		    
    public Boolean hasMessageCountIn()  {
        return messageCountIn == null ? false : true;
    }
        
		public Long getMessageCountIn() {
		return messageCountIn;
	}
	
	public GmStats setMessageCountIn(Long messageCountIn) {
		this.messageCountIn = messageCountIn;
		return this;	}
	
		    
    public Boolean hasMessageCountOut()  {
        return messageCountOut == null ? false : true;
    }
        
		public Long getMessageCountOut() {
		return messageCountOut;
	}
	
	public GmStats setMessageCountOut(Long messageCountOut) {
		this.messageCountOut = messageCountOut;
		return this;	}
	
		    
    public Boolean hasMessageCountInOut()  {
        return messageCountInOut == null ? false : true;
    }
        
		public Long getMessageCountInOut() {
		return messageCountInOut;
	}
	
	public GmStats setMessageCountInOut(Long messageCountInOut) {
		this.messageCountInOut = messageCountInOut;
		return this;	}
	
		    
    public Boolean hasBytesOut()  {
        return bytesOut == null ? false : true;
    }
        
		public Long getBytesOut() {
		return bytesOut;
	}
	
	public GmStats setBytesOut(Long bytesOut) {
		this.bytesOut = bytesOut;
		return this;	}
	
		    
    public Boolean hasConnectionCount()  {
        return connectionCount == null ? false : true;
    }
        
		public Integer getConnectionCount() {
		return connectionCount;
	}
	
	public GmStats setConnectionCount(Integer connectionCount) {
		this.connectionCount = connectionCount;
		return this;	}
	
		    
    public Boolean hasPlayerBytesOut()  {
        return playerBytesOut == null ? false : true;
    }
        
		public Long getPlayerBytesOut() {
		return playerBytesOut;
	}
	
	public GmStats setPlayerBytesOut(Long playerBytesOut) {
		this.playerBytesOut = playerBytesOut;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public GmStats setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		    
    public Boolean hasBytesPerMessageOut()  {
        return bytesPerMessageOut == null ? false : true;
    }
        
		public Long getBytesPerMessageOut() {
		return bytesPerMessageOut;
	}
	
	public GmStats setBytesPerMessageOut(Long bytesPerMessageOut) {
		this.bytesPerMessageOut = bytesPerMessageOut;
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

    public Schema<GmStats> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GmStats newMessage()
    {
        return new GmStats();
    }

    public Class<GmStats> typeClass()
    {
        return GmStats.class;
    }

    public String messageName()
    {
        return GmStats.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GmStats.class.getName();
    }

    public boolean isInitialized(GmStats message)
    {
        return true;
    }

    public void mergeFrom(Input input, GmStats message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.action = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.messageCountIn = input.readInt64();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.messageCountOut = input.readInt64();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.messageCountInOut = input.readInt64();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.bytesOut = input.readInt64();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.connectionCount = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.playerBytesOut = input.readInt64();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.bytesPerMessageOut = input.readInt64();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GmStats message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.action != null)
            output.writeString(2, message.action, false);
    	    	
    	            	
    	    	
    	    	    	if(message.messageCountIn != null)
            output.writeInt64(3, message.messageCountIn, false);
    	    	
    	            	
    	    	
    	    	    	if(message.messageCountOut != null)
            output.writeInt64(4, message.messageCountOut, false);
    	    	
    	            	
    	    	
    	    	    	if(message.messageCountInOut != null)
            output.writeInt64(5, message.messageCountInOut, false);
    	    	
    	            	
    	    	
    	    	    	if(message.bytesOut != null)
            output.writeInt64(6, message.bytesOut, false);
    	    	
    	            	
    	    	
    	    	    	if(message.connectionCount != null)
            output.writeInt32(7, message.connectionCount, false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerBytesOut != null)
            output.writeInt64(8, message.playerBytesOut, false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(9, message.playerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.bytesPerMessageOut != null)
            output.writeInt64(10, message.bytesPerMessageOut, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "action";
        	        	case 3: return "messageCountIn";
        	        	case 4: return "messageCountOut";
        	        	case 5: return "messageCountInOut";
        	        	case 6: return "bytesOut";
        	        	case 7: return "connectionCount";
        	        	case 8: return "playerBytesOut";
        	        	case 9: return "playerId";
        	        	case 10: return "bytesPerMessageOut";
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
    	    	__fieldMap.put("id", 1);
    	    	__fieldMap.put("action", 2);
    	    	__fieldMap.put("messageCountIn", 3);
    	    	__fieldMap.put("messageCountOut", 4);
    	    	__fieldMap.put("messageCountInOut", 5);
    	    	__fieldMap.put("bytesOut", 6);
    	    	__fieldMap.put("connectionCount", 7);
    	    	__fieldMap.put("playerBytesOut", 8);
    	    	__fieldMap.put("playerId", 9);
    	    	__fieldMap.put("bytesPerMessageOut", 10);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GmStats.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GmStats parseFrom(byte[] bytes) {
	GmStats message = new GmStats();
	ProtobufIOUtil.mergeFrom(bytes, message, GmStats.getSchema());
	return message;
}

public static GmStats parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GmStats message = new GmStats();
	JsonIOUtil.mergeFrom(bytes, message, GmStats.getSchema(), false);
	return message;
}

public GmStats clone() {
	byte[] bytes = this.toByteArray();
	GmStats gmStats = GmStats.parseFrom(bytes);
	return gmStats;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GmStats.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GmStats> schema = GmStats.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GmStats.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
