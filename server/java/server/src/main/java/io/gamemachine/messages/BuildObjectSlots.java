
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
public final class BuildObjectSlots implements Externalizable, Message<BuildObjectSlots>, Schema<BuildObjectSlots>{



    public static Schema<BuildObjectSlots> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildObjectSlots getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildObjectSlots DEFAULT_INSTANCE = new BuildObjectSlots();
    static final String defaultScope = BuildObjectSlots.class.getSimpleName();

        public List<BuildObjectSlot> buildObjectSlot;
	    


    public BuildObjectSlots()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static BuildObjectSlots fromModel(Model model) {
		boolean hasFields = false;
    	BuildObjectSlots message = new BuildObjectSlots();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasBuildObjectSlot()  {
        return buildObjectSlot == null ? false : true;
    }
        
		public List<BuildObjectSlot> getBuildObjectSlotList() {
		if(this.buildObjectSlot == null)
            this.buildObjectSlot = new ArrayList<BuildObjectSlot>();
		return buildObjectSlot;
	}

	public BuildObjectSlots setBuildObjectSlotList(List<BuildObjectSlot> buildObjectSlot) {
		this.buildObjectSlot = buildObjectSlot;
		return this;
	}

	public BuildObjectSlot getBuildObjectSlot(int index)  {
        return buildObjectSlot == null ? null : buildObjectSlot.get(index);
    }

    public int getBuildObjectSlotCount()  {
        return buildObjectSlot == null ? 0 : buildObjectSlot.size();
    }

    public BuildObjectSlots addBuildObjectSlot(BuildObjectSlot buildObjectSlot)  {
        if(this.buildObjectSlot == null)
            this.buildObjectSlot = new ArrayList<BuildObjectSlot>();
        this.buildObjectSlot.add(buildObjectSlot);
        return this;
    }
            	    	    	    	
    public BuildObjectSlots removeBuildObjectSlotBySlotId(BuildObjectSlot buildObjectSlot)  {
    	if(this.buildObjectSlot == null)
           return this;
            
       	Iterator<BuildObjectSlot> itr = this.buildObjectSlot.iterator();
       	while (itr.hasNext()) {
    	BuildObjectSlot obj = itr.next();
    	
    	    		if (buildObjectSlot.slotId.equals(obj.slotId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjectSlots removeBuildObjectSlotByBuildObjectId(BuildObjectSlot buildObjectSlot)  {
    	if(this.buildObjectSlot == null)
           return this;
            
       	Iterator<BuildObjectSlot> itr = this.buildObjectSlot.iterator();
       	while (itr.hasNext()) {
    	BuildObjectSlot obj = itr.next();
    	
    	    		if (buildObjectSlot.buildObjectId.equals(obj.buildObjectId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjectSlots removeBuildObjectSlotByPlacedAt(BuildObjectSlot buildObjectSlot)  {
    	if(this.buildObjectSlot == null)
           return this;
            
       	Iterator<BuildObjectSlot> itr = this.buildObjectSlot.iterator();
       	while (itr.hasNext()) {
    	BuildObjectSlot obj = itr.next();
    	
    	    		if (buildObjectSlot.placedAt.equals(obj.placedAt)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjectSlots removeBuildObjectSlotByBuildOverTime(BuildObjectSlot buildObjectSlot)  {
    	if(this.buildObjectSlot == null)
           return this;
            
       	Iterator<BuildObjectSlot> itr = this.buildObjectSlot.iterator();
       	while (itr.hasNext()) {
    	BuildObjectSlot obj = itr.next();
    	
    	    		if (buildObjectSlot.buildOverTime.equals(obj.buildOverTime)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjectSlots removeBuildObjectSlotByBuildTime(BuildObjectSlot buildObjectSlot)  {
    	if(this.buildObjectSlot == null)
           return this;
            
       	Iterator<BuildObjectSlot> itr = this.buildObjectSlot.iterator();
       	while (itr.hasNext()) {
    	BuildObjectSlot obj = itr.next();
    	
    	    		if (buildObjectSlot.buildTime.equals(obj.buildTime)) {
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

    public Schema<BuildObjectSlots> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildObjectSlots newMessage()
    {
        return new BuildObjectSlots();
    }

    public Class<BuildObjectSlots> typeClass()
    {
        return BuildObjectSlots.class;
    }

    public String messageName()
    {
        return BuildObjectSlots.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildObjectSlots.class.getName();
    }

    public boolean isInitialized(BuildObjectSlots message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildObjectSlots message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.buildObjectSlot == null)
                        message.buildObjectSlot = new ArrayList<BuildObjectSlot>();
                                        message.buildObjectSlot.add(input.mergeObject(null, BuildObjectSlot.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildObjectSlots message) throws IOException
    {
    	    	
    	    	
    	    	if(message.buildObjectSlot != null)
        {
            for(BuildObjectSlot buildObjectSlot : message.buildObjectSlot)
            {
                if(buildObjectSlot != null) {
                   	    				output.writeObject(1, buildObjectSlot, BuildObjectSlot.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START BuildObjectSlots");
    	    	if(this.buildObjectSlot != null) {
    		System.out.println("buildObjectSlot="+this.buildObjectSlot);
    	}
    	    	System.out.println("END BuildObjectSlots");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "buildObjectSlot";
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
    	    	__fieldMap.put("buildObjectSlot", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildObjectSlots.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildObjectSlots parseFrom(byte[] bytes) {
	BuildObjectSlots message = new BuildObjectSlots();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildObjectSlots.getSchema());
	return message;
}

public static BuildObjectSlots parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildObjectSlots message = new BuildObjectSlots();
	JsonIOUtil.mergeFrom(bytes, message, BuildObjectSlots.getSchema(), false);
	return message;
}

public BuildObjectSlots clone() {
	byte[] bytes = this.toByteArray();
	BuildObjectSlots buildObjectSlots = BuildObjectSlots.parseFrom(bytes);
	return buildObjectSlots;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildObjectSlots.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildObjectSlots> schema = BuildObjectSlots.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildObjectSlots.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, BuildObjectSlots.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
