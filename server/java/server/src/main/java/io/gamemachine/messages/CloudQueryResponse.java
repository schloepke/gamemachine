
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
public final class CloudQueryResponse implements Externalizable, Message<CloudQueryResponse>, Schema<CloudQueryResponse>{



    public static Schema<CloudQueryResponse> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CloudQueryResponse getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CloudQueryResponse DEFAULT_INSTANCE = new CloudQueryResponse();
    static final String defaultScope = CloudQueryResponse.class.getSimpleName();

    			public String format;
	    
            public List<String> messageId;
	        public List<ByteString> byteMessage;
	        public List<String> jsonMessage;
	    


    public CloudQueryResponse()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("cloud_query_response_format",null);
    	    	    	    	    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (format != null) {
    	       	    	model.setString("cloud_query_response_format",format);
    	        		
    	}
    	    	    	    	    	    	    	    	    }
    
	public static CloudQueryResponse fromModel(Model model) {
		boolean hasFields = false;
    	CloudQueryResponse message = new CloudQueryResponse();
    	    	    	    	    	
    	    	    	String formatField = model.getString("cloud_query_response_format");
    	    	
    	if (formatField != null) {
    		message.setFormat(formatField);
    		hasFields = true;
    	}
    	    	    	    	    	    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasFormat()  {
        return format == null ? false : true;
    }
        
		public String getFormat() {
		return format;
	}
	
	public CloudQueryResponse setFormat(String format) {
		this.format = format;
		return this;	}
	
		    
    public Boolean hasMessageId()  {
        return messageId == null ? false : true;
    }
        
		public List<String> getMessageIdList() {
		if(this.messageId == null)
            this.messageId = new ArrayList<String>();
		return messageId;
	}

	public CloudQueryResponse setMessageIdList(List<String> messageId) {
		this.messageId = messageId;
		return this;
	}

	public String getMessageId(int index)  {
        return messageId == null ? null : messageId.get(index);
    }

    public int getMessageIdCount()  {
        return messageId == null ? 0 : messageId.size();
    }

    public CloudQueryResponse addMessageId(String messageId)  {
        if(this.messageId == null)
            this.messageId = new ArrayList<String>();
        this.messageId.add(messageId);
        return this;
    }
        	
    
    
    
		    
    public Boolean hasByteMessage()  {
        return byteMessage == null ? false : true;
    }
        
		public List<ByteString> getByteMessageList() {
		if(this.byteMessage == null)
            this.byteMessage = new ArrayList<ByteString>();
		return byteMessage;
	}

	public CloudQueryResponse setByteMessageList(List<ByteString> byteMessage) {
		this.byteMessage = byteMessage;
		return this;
	}

	public ByteString getByteMessage(int index)  {
        return byteMessage == null ? null : byteMessage.get(index);
    }

    public int getByteMessageCount()  {
        return byteMessage == null ? 0 : byteMessage.size();
    }

    public CloudQueryResponse addByteMessage(ByteString byteMessage)  {
        if(this.byteMessage == null)
            this.byteMessage = new ArrayList<ByteString>();
        this.byteMessage.add(byteMessage);
        return this;
    }
        	
    
    
    
		    
    public Boolean hasJsonMessage()  {
        return jsonMessage == null ? false : true;
    }
        
		public List<String> getJsonMessageList() {
		if(this.jsonMessage == null)
            this.jsonMessage = new ArrayList<String>();
		return jsonMessage;
	}

	public CloudQueryResponse setJsonMessageList(List<String> jsonMessage) {
		this.jsonMessage = jsonMessage;
		return this;
	}

	public String getJsonMessage(int index)  {
        return jsonMessage == null ? null : jsonMessage.get(index);
    }

    public int getJsonMessageCount()  {
        return jsonMessage == null ? 0 : jsonMessage.size();
    }

    public CloudQueryResponse addJsonMessage(String jsonMessage)  {
        if(this.jsonMessage == null)
            this.jsonMessage = new ArrayList<String>();
        this.jsonMessage.add(jsonMessage);
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

    public Schema<CloudQueryResponse> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CloudQueryResponse newMessage()
    {
        return new CloudQueryResponse();
    }

    public Class<CloudQueryResponse> typeClass()
    {
        return CloudQueryResponse.class;
    }

    public String messageName()
    {
        return CloudQueryResponse.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CloudQueryResponse.class.getName();
    }

    public boolean isInitialized(CloudQueryResponse message)
    {
        return true;
    }

    public void mergeFrom(Input input, CloudQueryResponse message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.format = input.readString();
                	break;
                	                	
                            	            	case 2:
            	            		if(message.messageId == null)
                        message.messageId = new ArrayList<String>();
                                    	message.messageId.add(input.readString());
                	                    break;
                            	            	case 3:
            	            		if(message.byteMessage == null)
                        message.byteMessage = new ArrayList<ByteString>();
                                    	message.byteMessage.add(input.readBytes());
                	                    break;
                            	            	case 4:
            	            		if(message.jsonMessage == null)
                        message.jsonMessage = new ArrayList<String>();
                                    	message.jsonMessage.add(input.readString());
                	                    break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CloudQueryResponse message) throws IOException
    {
    	    	
    	    	if(message.format == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.format != null)
            output.writeString(1, message.format, false);
    	    	
    	            	
    	    	
    	    	if(message.messageId != null)
        {
            for(String messageId : message.messageId)
            {
                if(messageId != null) {
                   	            		output.writeString(2, messageId, true);
    				    			}
            }
        }
    	            	
    	    	
    	    	if(message.byteMessage != null)
        {
            for(ByteString byteMessage : message.byteMessage)
            {
                if(byteMessage != null) {
                   	            		output.writeBytes(3, byteMessage, true);
    				    			}
            }
        }
    	            	
    	    	
    	    	if(message.jsonMessage != null)
        {
            for(String jsonMessage : message.jsonMessage)
            {
                if(jsonMessage != null) {
                   	            		output.writeString(4, jsonMessage, true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START CloudQueryResponse");
    	    	if(this.format != null) {
    		System.out.println("format="+this.format);
    	}
    	    	if(this.messageId != null) {
    		System.out.println("messageId="+this.messageId);
    	}
    	    	if(this.byteMessage != null) {
    		System.out.println("byteMessage="+this.byteMessage);
    	}
    	    	if(this.jsonMessage != null) {
    		System.out.println("jsonMessage="+this.jsonMessage);
    	}
    	    	System.out.println("END CloudQueryResponse");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "format";
        	        	case 2: return "messageId";
        	        	case 3: return "byteMessage";
        	        	case 4: return "jsonMessage";
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
    	    	__fieldMap.put("format", 1);
    	    	__fieldMap.put("messageId", 2);
    	    	__fieldMap.put("byteMessage", 3);
    	    	__fieldMap.put("jsonMessage", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CloudQueryResponse.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CloudQueryResponse parseFrom(byte[] bytes) {
	CloudQueryResponse message = new CloudQueryResponse();
	ProtobufIOUtil.mergeFrom(bytes, message, CloudQueryResponse.getSchema());
	return message;
}

public static CloudQueryResponse parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CloudQueryResponse message = new CloudQueryResponse();
	JsonIOUtil.mergeFrom(bytes, message, CloudQueryResponse.getSchema(), false);
	return message;
}

public CloudQueryResponse clone() {
	byte[] bytes = this.toByteArray();
	CloudQueryResponse cloudQueryResponse = CloudQueryResponse.parseFrom(bytes);
	return cloudQueryResponse;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CloudQueryResponse.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CloudQueryResponse> schema = CloudQueryResponse.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CloudQueryResponse.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, CloudQueryResponse.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
