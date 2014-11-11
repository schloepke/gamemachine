
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

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class Agent implements Externalizable, Message<Agent>, Schema<Agent>{



    public static Schema<Agent> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Agent getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Agent DEFAULT_INSTANCE = new Agent();

    			public String id;
	    
        			public String code;
	    
        			public String classname;
	    
        			public Boolean remove;
	    
        			public String compileResult;
	    
        			public Integer concurrency;
	    
      
    public Agent()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Agent setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasCode()  {
        return code == null ? false : true;
    }
        
		public String getCode() {
		return code;
	}
	
	public Agent setCode(String code) {
		this.code = code;
		return this;	}
	
		    
    public Boolean hasClassname()  {
        return classname == null ? false : true;
    }
        
		public String getClassname() {
		return classname;
	}
	
	public Agent setClassname(String classname) {
		this.classname = classname;
		return this;	}
	
		    
    public Boolean hasRemove()  {
        return remove == null ? false : true;
    }
        
		public Boolean getRemove() {
		return remove;
	}
	
	public Agent setRemove(Boolean remove) {
		this.remove = remove;
		return this;	}
	
		    
    public Boolean hasCompileResult()  {
        return compileResult == null ? false : true;
    }
        
		public String getCompileResult() {
		return compileResult;
	}
	
	public Agent setCompileResult(String compileResult) {
		this.compileResult = compileResult;
		return this;	}
	
		    
    public Boolean hasConcurrency()  {
        return concurrency == null ? false : true;
    }
        
		public Integer getConcurrency() {
		return concurrency;
	}
	
	public Agent setConcurrency(Integer concurrency) {
		this.concurrency = concurrency;
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

    public Schema<Agent> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Agent newMessage()
    {
        return new Agent();
    }

    public Class<Agent> typeClass()
    {
        return Agent.class;
    }

    public String messageName()
    {
        return Agent.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Agent.class.getName();
    }

    public boolean isInitialized(Agent message)
    {
        return true;
    }

    public void mergeFrom(Input input, Agent message) throws IOException
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
            	                	                	message.code = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.classname = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.remove = input.readBool();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.compileResult = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.concurrency = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Agent message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	if(message.code == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.code != null)
            output.writeString(2, message.code, false);
    	    	
    	            	
    	    	if(message.classname == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.classname != null)
            output.writeString(3, message.classname, false);
    	    	
    	            	
    	    	
    	    	    	if(message.remove != null)
            output.writeBool(5, message.remove, false);
    	    	
    	            	
    	    	if(message.compileResult == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.compileResult != null)
            output.writeString(6, message.compileResult, false);
    	    	
    	            	
    	    	if(message.concurrency == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.concurrency != null)
            output.writeInt32(7, message.concurrency, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "code";
        	        	case 3: return "classname";
        	        	case 5: return "remove";
        	        	case 6: return "compileResult";
        	        	case 7: return "concurrency";
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
    	    	__fieldMap.put("code", 2);
    	    	__fieldMap.put("classname", 3);
    	    	__fieldMap.put("remove", 5);
    	    	__fieldMap.put("compileResult", 6);
    	    	__fieldMap.put("concurrency", 7);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Agent.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Agent parseFrom(byte[] bytes) {
	Agent message = new Agent();
	ProtobufIOUtil.mergeFrom(bytes, message, Agent.getSchema());
	return message;
}

public static Agent parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Agent message = new Agent();
	JsonIOUtil.mergeFrom(bytes, message, Agent.getSchema(), false);
	return message;
}

public Agent clone() {
	byte[] bytes = this.toByteArray();
	Agent agent = Agent.parseFrom(bytes);
	return agent;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Agent.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Agent> schema = Agent.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Agent.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
