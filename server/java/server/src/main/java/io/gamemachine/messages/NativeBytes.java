
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
public final class NativeBytes implements Externalizable, Message<NativeBytes>, Schema<NativeBytes>{



    public static Schema<NativeBytes> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static NativeBytes getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final NativeBytes DEFAULT_INSTANCE = new NativeBytes();
    static final String defaultScope = NativeBytes.class.getSimpleName();

    	
							    public ByteString bytes;
		    			    
		
    
            public List<ByteString> byteArray;
	    


    public NativeBytes()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("native_bytes_bytes",null);
    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (bytes != null) {
    	       	    	model.set("native_bytes_bytes",bytes.toByteArray());
    	        		
    	//}
    	    	    	    	    }
    
	public static NativeBytes fromModel(Model model) {
		boolean hasFields = false;
    	NativeBytes message = new NativeBytes();
    	    	    	    	    	
    	    	    	ByteString bytesField = null;
    	Object bytesValue = model.get("native_bytes_bytes");
    	if (bytesValue != null) {
    		byte[] bytesBytes = Convert.toBytes(bytesValue);
    		bytesField = ByteString.copyFrom(bytesBytes);
    	}
    	    	
    	    	
    	    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public ByteString getBytes() {
		return bytes;
	}
	
	public NativeBytes setBytes(ByteString bytes) {
		this.bytes = bytes;
		return this;	}
	
		            
		public List<ByteString> getByteArrayList() {
		if(this.byteArray == null)
            this.byteArray = new ArrayList<ByteString>();
		return byteArray;
	}

	public NativeBytes setByteArrayList(List<ByteString> byteArray) {
		this.byteArray = byteArray;
		return this;
	}

	public ByteString getByteArray(int index)  {
        return byteArray == null ? null : byteArray.get(index);
    }

    public int getByteArrayCount()  {
        return byteArray == null ? 0 : byteArray.size();
    }

    public NativeBytes addByteArray(ByteString byteArray)  {
        if(this.byteArray == null)
            this.byteArray = new ArrayList<ByteString>();
        this.byteArray.add(byteArray);
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

    public Schema<NativeBytes> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public NativeBytes newMessage()
    {
        return new NativeBytes();
    }

    public Class<NativeBytes> typeClass()
    {
        return NativeBytes.class;
    }

    public String messageName()
    {
        return NativeBytes.class.getSimpleName();
    }

    public String messageFullName()
    {
        return NativeBytes.class.getName();
    }

    public boolean isInitialized(NativeBytes message)
    {
        return true;
    }

    public void mergeFrom(Input input, NativeBytes message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.bytes = input.readBytes();
                	break;
                	                	
                            	            	case 2:
            	            		if(message.byteArray == null)
                        message.byteArray = new ArrayList<ByteString>();
                                    	message.byteArray.add(input.readBytes());
                	                    break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, NativeBytes message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (ByteString)message.bytes != null) {
            output.writeBytes(1, message.bytes, false);
        }
    	    	
    	            	
    	    	
    	    	if(message.byteArray != null)
        {
            for(ByteString byteArray : message.byteArray)
            {
                if( (ByteString) byteArray != null) {
                   	            		output.writeBytes(2, byteArray, true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START NativeBytes");
    	    	//if(this.bytes != null) {
    		System.out.println("bytes="+this.bytes);
    	//}
    	    	//if(this.byteArray != null) {
    		System.out.println("byteArray="+this.byteArray);
    	//}
    	    	System.out.println("END NativeBytes");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "bytes";
        	        	case 2: return "byteArray";
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
    	    	__fieldMap.put("bytes", 1);
    	    	__fieldMap.put("byteArray", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = NativeBytes.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static NativeBytes parseFrom(byte[] bytes) {
	NativeBytes message = new NativeBytes();
	ProtobufIOUtil.mergeFrom(bytes, message, NativeBytes.getSchema());
	return message;
}

public static NativeBytes parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	NativeBytes message = new NativeBytes();
	JsonIOUtil.mergeFrom(bytes, message, NativeBytes.getSchema(), false);
	return message;
}

public NativeBytes clone() {
	byte[] bytes = this.toByteArray();
	NativeBytes nativeBytes = NativeBytes.parseFrom(bytes);
	return nativeBytes;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, NativeBytes.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<NativeBytes> schema = NativeBytes.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, NativeBytes.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, NativeBytes.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
