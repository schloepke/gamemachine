
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
public final class Territories implements Externalizable, Message<Territories>, Schema<Territories>{



    public static Schema<Territories> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Territories getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Territories DEFAULT_INSTANCE = new Territories();
    static final String defaultScope = Territories.class.getSimpleName();

        public List<Territory> territories;
	    


    public Territories()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static Territories fromModel(Model model) {
		boolean hasFields = false;
    	Territories message = new Territories();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public List<Territory> getTerritoriesList() {
		if(this.territories == null)
            this.territories = new ArrayList<Territory>();
		return territories;
	}

	public Territories setTerritoriesList(List<Territory> territories) {
		this.territories = territories;
		return this;
	}

	public Territory getTerritories(int index)  {
        return territories == null ? null : territories.get(index);
    }

    public int getTerritoriesCount()  {
        return territories == null ? 0 : territories.size();
    }

    public Territories addTerritories(Territory territories)  {
        if(this.territories == null)
            this.territories = new ArrayList<Territory>();
        this.territories.add(territories);
        return this;
    }
            	    	    	    	
    public Territories removeTerritoriesById(Territory territories)  {
    	if(this.territories == null)
           return this;
            
       	Iterator<Territory> itr = this.territories.iterator();
       	while (itr.hasNext()) {
    	Territory obj = itr.next();
    	
    	    		if (territories.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Territories removeTerritoriesByOwner(Territory territories)  {
    	if(this.territories == null)
           return this;
            
       	Iterator<Territory> itr = this.territories.iterator();
       	while (itr.hasNext()) {
    	Territory obj = itr.next();
    	
    	    		if (territories.owner.equals(obj.owner)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Territories removeTerritoriesByRecordId(Territory territories)  {
    	if(this.territories == null)
           return this;
            
       	Iterator<Territory> itr = this.territories.iterator();
       	while (itr.hasNext()) {
    	Territory obj = itr.next();
    	
    	    		if (territories.recordId == obj.recordId) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Territories removeTerritoriesByKeep(Territory territories)  {
    	if(this.territories == null)
           return this;
            
       	Iterator<Territory> itr = this.territories.iterator();
       	while (itr.hasNext()) {
    	Territory obj = itr.next();
    	
    	    		if (territories.keep.equals(obj.keep)) {
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

    public Schema<Territories> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Territories newMessage()
    {
        return new Territories();
    }

    public Class<Territories> typeClass()
    {
        return Territories.class;
    }

    public String messageName()
    {
        return Territories.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Territories.class.getName();
    }

    public boolean isInitialized(Territories message)
    {
        return true;
    }

    public void mergeFrom(Input input, Territories message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.territories == null)
                        message.territories = new ArrayList<Territory>();
                                        message.territories.add(input.mergeObject(null, Territory.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Territories message) throws IOException
    {
    	    	
    	    	
    	    	if(message.territories != null)
        {
            for(Territory territories : message.territories)
            {
                if( (Territory) territories != null) {
                   	    				output.writeObject(1, territories, Territory.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Territories");
    	    	//if(this.territories != null) {
    		System.out.println("territories="+this.territories);
    	//}
    	    	System.out.println("END Territories");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "territories";
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
    	    	__fieldMap.put("territories", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Territories.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Territories parseFrom(byte[] bytes) {
	Territories message = new Territories();
	ProtobufIOUtil.mergeFrom(bytes, message, Territories.getSchema());
	return message;
}

public static Territories parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Territories message = new Territories();
	JsonIOUtil.mergeFrom(bytes, message, Territories.getSchema(), false);
	return message;
}

public Territories clone() {
	byte[] bytes = this.toByteArray();
	Territories territories = Territories.parseFrom(bytes);
	return territories;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Territories.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Territories> schema = Territories.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Territories.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Territories.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
