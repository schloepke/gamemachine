
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
public final class TerrainEdits implements Externalizable, Message<TerrainEdits>, Schema<TerrainEdits>{



    public static Schema<TerrainEdits> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TerrainEdits getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TerrainEdits DEFAULT_INSTANCE = new TerrainEdits();

        public List<TerrainEdit> terrainEdit;
	  
    public TerrainEdits()
    {
        
    }


	

	    
    public Boolean hasTerrainEdit()  {
        return terrainEdit == null ? false : true;
    }
        
		public List<TerrainEdit> getTerrainEditList() {
		if(this.terrainEdit == null)
            this.terrainEdit = new ArrayList<TerrainEdit>();
		return terrainEdit;
	}

	public TerrainEdits setTerrainEditList(List<TerrainEdit> terrainEdit) {
		this.terrainEdit = terrainEdit;
		return this;
	}

	public TerrainEdit getTerrainEdit(int index)  {
        return terrainEdit == null ? null : terrainEdit.get(index);
    }

    public int getTerrainEditCount()  {
        return terrainEdit == null ? 0 : terrainEdit.size();
    }

    public TerrainEdits addTerrainEdit(TerrainEdit terrainEdit)  {
        if(this.terrainEdit == null)
            this.terrainEdit = new ArrayList<TerrainEdit>();
        this.terrainEdit.add(terrainEdit);
        return this;
    }
            	    	    	    	
    public TerrainEdits removeTerrainEditByX(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TerrainEdits removeTerrainEditByY(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TerrainEdits removeTerrainEditByDetailLayer(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.detailLayer.equals(obj.detailLayer)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TerrainEdits removeTerrainEditByValue(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.value.equals(obj.value)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public TerrainEdits removeTerrainEditById(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TerrainEdits removeTerrainEditByRecordId(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TerrainEdits removeTerrainEditByTexture(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.texture.equals(obj.texture)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TerrainEdits removeTerrainEditByHeight(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.height.equals(obj.height)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TerrainEdits removeTerrainEditByCreatedAt(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.createdAt.equals(obj.createdAt)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TerrainEdits removeTerrainEditByTerrain(TerrainEdit terrainEdit)  {
    	if(this.terrainEdit == null)
           return this;
            
       	Iterator<TerrainEdit> itr = this.terrainEdit.iterator();
       	while (itr.hasNext()) {
    	TerrainEdit obj = itr.next();
    	
    	    		if (terrainEdit.terrain.equals(obj.terrain)) {
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

    public Schema<TerrainEdits> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TerrainEdits newMessage()
    {
        return new TerrainEdits();
    }

    public Class<TerrainEdits> typeClass()
    {
        return TerrainEdits.class;
    }

    public String messageName()
    {
        return TerrainEdits.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TerrainEdits.class.getName();
    }

    public boolean isInitialized(TerrainEdits message)
    {
        return true;
    }

    public void mergeFrom(Input input, TerrainEdits message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.terrainEdit == null)
                        message.terrainEdit = new ArrayList<TerrainEdit>();
                                        message.terrainEdit.add(input.mergeObject(null, TerrainEdit.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TerrainEdits message) throws IOException
    {
    	    	
    	    	
    	    	if(message.terrainEdit != null)
        {
            for(TerrainEdit terrainEdit : message.terrainEdit)
            {
                if(terrainEdit != null) {
                   	    				output.writeObject(1, terrainEdit, TerrainEdit.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "terrainEdit";
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
    	    	__fieldMap.put("terrainEdit", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TerrainEdits.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TerrainEdits parseFrom(byte[] bytes) {
	TerrainEdits message = new TerrainEdits();
	ProtobufIOUtil.mergeFrom(bytes, message, TerrainEdits.getSchema());
	return message;
}

public static TerrainEdits parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TerrainEdits message = new TerrainEdits();
	JsonIOUtil.mergeFrom(bytes, message, TerrainEdits.getSchema(), false);
	return message;
}

public TerrainEdits clone() {
	byte[] bytes = this.toByteArray();
	TerrainEdits terrainEdits = TerrainEdits.parseFrom(bytes);
	return terrainEdits;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TerrainEdits.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TerrainEdits> schema = TerrainEdits.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TerrainEdits.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
