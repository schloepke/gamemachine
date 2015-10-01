
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
public final class BuildableAreas implements Externalizable, Message<BuildableAreas>, Schema<BuildableAreas>{



    public static Schema<BuildableAreas> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildableAreas getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildableAreas DEFAULT_INSTANCE = new BuildableAreas();
    static final String defaultScope = BuildableAreas.class.getSimpleName();

        public List<BuildableArea> buildableArea;
	    


    public BuildableAreas()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static BuildableAreas fromModel(Model model) {
		boolean hasFields = false;
    	BuildableAreas message = new BuildableAreas();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasBuildableArea()  {
        return buildableArea == null ? false : true;
    }
        
		public List<BuildableArea> getBuildableAreaList() {
		if(this.buildableArea == null)
            this.buildableArea = new ArrayList<BuildableArea>();
		return buildableArea;
	}

	public BuildableAreas setBuildableAreaList(List<BuildableArea> buildableArea) {
		this.buildableArea = buildableArea;
		return this;
	}

	public BuildableArea getBuildableArea(int index)  {
        return buildableArea == null ? null : buildableArea.get(index);
    }

    public int getBuildableAreaCount()  {
        return buildableArea == null ? 0 : buildableArea.size();
    }

    public BuildableAreas addBuildableArea(BuildableArea buildableArea)  {
        if(this.buildableArea == null)
            this.buildableArea = new ArrayList<BuildableArea>();
        this.buildableArea.add(buildableArea);
        return this;
    }
            	    	    	    	
    public BuildableAreas removeBuildableAreaByOwnerId(BuildableArea buildableArea)  {
    	if(this.buildableArea == null)
           return this;
            
       	Iterator<BuildableArea> itr = this.buildableArea.iterator();
       	while (itr.hasNext()) {
    	BuildableArea obj = itr.next();
    	
    	    		if (buildableArea.ownerId.equals(obj.ownerId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildableAreas removeBuildableAreaByPx(BuildableArea buildableArea)  {
    	if(this.buildableArea == null)
           return this;
            
       	Iterator<BuildableArea> itr = this.buildableArea.iterator();
       	while (itr.hasNext()) {
    	BuildableArea obj = itr.next();
    	
    	    		if (buildableArea.px.equals(obj.px)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildableAreas removeBuildableAreaByPy(BuildableArea buildableArea)  {
    	if(this.buildableArea == null)
           return this;
            
       	Iterator<BuildableArea> itr = this.buildableArea.iterator();
       	while (itr.hasNext()) {
    	BuildableArea obj = itr.next();
    	
    	    		if (buildableArea.py.equals(obj.py)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildableAreas removeBuildableAreaByPz(BuildableArea buildableArea)  {
    	if(this.buildableArea == null)
           return this;
            
       	Iterator<BuildableArea> itr = this.buildableArea.iterator();
       	while (itr.hasNext()) {
    	BuildableArea obj = itr.next();
    	
    	    		if (buildableArea.pz.equals(obj.pz)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildableAreas removeBuildableAreaBySx(BuildableArea buildableArea)  {
    	if(this.buildableArea == null)
           return this;
            
       	Iterator<BuildableArea> itr = this.buildableArea.iterator();
       	while (itr.hasNext()) {
    	BuildableArea obj = itr.next();
    	
    	    		if (buildableArea.sx.equals(obj.sx)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildableAreas removeBuildableAreaBySy(BuildableArea buildableArea)  {
    	if(this.buildableArea == null)
           return this;
            
       	Iterator<BuildableArea> itr = this.buildableArea.iterator();
       	while (itr.hasNext()) {
    	BuildableArea obj = itr.next();
    	
    	    		if (buildableArea.sy.equals(obj.sy)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildableAreas removeBuildableAreaBySz(BuildableArea buildableArea)  {
    	if(this.buildableArea == null)
           return this;
            
       	Iterator<BuildableArea> itr = this.buildableArea.iterator();
       	while (itr.hasNext()) {
    	BuildableArea obj = itr.next();
    	
    	    		if (buildableArea.sz.equals(obj.sz)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildableAreas removeBuildableAreaByRecordId(BuildableArea buildableArea)  {
    	if(this.buildableArea == null)
           return this;
            
       	Iterator<BuildableArea> itr = this.buildableArea.iterator();
       	while (itr.hasNext()) {
    	BuildableArea obj = itr.next();
    	
    	    		if (buildableArea.recordId.equals(obj.recordId)) {
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

    public Schema<BuildableAreas> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildableAreas newMessage()
    {
        return new BuildableAreas();
    }

    public Class<BuildableAreas> typeClass()
    {
        return BuildableAreas.class;
    }

    public String messageName()
    {
        return BuildableAreas.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildableAreas.class.getName();
    }

    public boolean isInitialized(BuildableAreas message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildableAreas message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.buildableArea == null)
                        message.buildableArea = new ArrayList<BuildableArea>();
                                        message.buildableArea.add(input.mergeObject(null, BuildableArea.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildableAreas message) throws IOException
    {
    	    	
    	    	
    	    	if(message.buildableArea != null)
        {
            for(BuildableArea buildableArea : message.buildableArea)
            {
                if(buildableArea != null) {
                   	    				output.writeObject(1, buildableArea, BuildableArea.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START BuildableAreas");
    	    	if(this.buildableArea != null) {
    		System.out.println("buildableArea="+this.buildableArea);
    	}
    	    	System.out.println("END BuildableAreas");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "buildableArea";
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
    	    	__fieldMap.put("buildableArea", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildableAreas.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildableAreas parseFrom(byte[] bytes) {
	BuildableAreas message = new BuildableAreas();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildableAreas.getSchema());
	return message;
}

public static BuildableAreas parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildableAreas message = new BuildableAreas();
	JsonIOUtil.mergeFrom(bytes, message, BuildableAreas.getSchema(), false);
	return message;
}

public BuildableAreas clone() {
	byte[] bytes = this.toByteArray();
	BuildableAreas buildableAreas = BuildableAreas.parseFrom(bytes);
	return buildableAreas;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildableAreas.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildableAreas> schema = BuildableAreas.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildableAreas.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, BuildableAreas.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
