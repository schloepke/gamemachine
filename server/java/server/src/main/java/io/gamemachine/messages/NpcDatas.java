
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
public final class NpcDatas implements Externalizable, Message<NpcDatas>, Schema<NpcDatas>{



    public static Schema<NpcDatas> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static NpcDatas getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final NpcDatas DEFAULT_INSTANCE = new NpcDatas();
    static final String defaultScope = NpcDatas.class.getSimpleName();

        public List<NpcData> npcData;
	    


    public NpcDatas()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static NpcDatas fromModel(Model model) {
		boolean hasFields = false;
    	NpcDatas message = new NpcDatas();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasNpcData()  {
        return npcData == null ? false : true;
    }
        
		public List<NpcData> getNpcDataList() {
		if(this.npcData == null)
            this.npcData = new ArrayList<NpcData>();
		return npcData;
	}

	public NpcDatas setNpcDataList(List<NpcData> npcData) {
		this.npcData = npcData;
		return this;
	}

	public NpcData getNpcData(int index)  {
        return npcData == null ? null : npcData.get(index);
    }

    public int getNpcDataCount()  {
        return npcData == null ? 0 : npcData.size();
    }

    public NpcDatas addNpcData(NpcData npcData)  {
        if(this.npcData == null)
            this.npcData = new ArrayList<NpcData>();
        this.npcData.add(npcData);
        return this;
    }
            	    	    	    	
    public NpcDatas removeNpcDataById(NpcData npcData)  {
    	if(this.npcData == null)
           return this;
            
       	Iterator<NpcData> itr = this.npcData.iterator();
       	while (itr.hasNext()) {
    	NpcData obj = itr.next();
    	
    	    		if (npcData.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public NpcDatas removeNpcDataByLeader(NpcData npcData)  {
    	if(this.npcData == null)
           return this;
            
       	Iterator<NpcData> itr = this.npcData.iterator();
       	while (itr.hasNext()) {
    	NpcData obj = itr.next();
    	
    	    		if (npcData.leader.equals(obj.leader)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	
    public NpcDatas removeNpcDataByPatrolRoute(NpcData npcData)  {
    	if(this.npcData == null)
           return this;
            
       	Iterator<NpcData> itr = this.npcData.iterator();
       	while (itr.hasNext()) {
    	NpcData obj = itr.next();
    	
    	    		if (npcData.patrolRoute.equals(obj.patrolRoute)) {
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

    public Schema<NpcDatas> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public NpcDatas newMessage()
    {
        return new NpcDatas();
    }

    public Class<NpcDatas> typeClass()
    {
        return NpcDatas.class;
    }

    public String messageName()
    {
        return NpcDatas.class.getSimpleName();
    }

    public String messageFullName()
    {
        return NpcDatas.class.getName();
    }

    public boolean isInitialized(NpcDatas message)
    {
        return true;
    }

    public void mergeFrom(Input input, NpcDatas message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.npcData == null)
                        message.npcData = new ArrayList<NpcData>();
                                        message.npcData.add(input.mergeObject(null, NpcData.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, NpcDatas message) throws IOException
    {
    	    	
    	    	
    	    	if(message.npcData != null)
        {
            for(NpcData npcData : message.npcData)
            {
                if(npcData != null) {
                   	    				output.writeObject(1, npcData, NpcData.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START NpcDatas");
    	    	if(this.npcData != null) {
    		System.out.println("npcData="+this.npcData);
    	}
    	    	System.out.println("END NpcDatas");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "npcData";
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
    	    	__fieldMap.put("npcData", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = NpcDatas.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static NpcDatas parseFrom(byte[] bytes) {
	NpcDatas message = new NpcDatas();
	ProtobufIOUtil.mergeFrom(bytes, message, NpcDatas.getSchema());
	return message;
}

public static NpcDatas parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	NpcDatas message = new NpcDatas();
	JsonIOUtil.mergeFrom(bytes, message, NpcDatas.getSchema(), false);
	return message;
}

public NpcDatas clone() {
	byte[] bytes = this.toByteArray();
	NpcDatas npcDatas = NpcDatas.parseFrom(bytes);
	return npcDatas;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, NpcDatas.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<NpcDatas> schema = NpcDatas.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, NpcDatas.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, NpcDatas.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
