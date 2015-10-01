
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
public final class EntityList implements Externalizable, Message<EntityList>, Schema<EntityList>{



    public static Schema<EntityList> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static EntityList getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final EntityList DEFAULT_INSTANCE = new EntityList();

        public List<Entity> entity;
	  
    public EntityList()
    {
        
    }


	

	    
    public Boolean hasEntity()  {
        return entity == null ? false : true;
    }
        
		public List<Entity> getEntityList() {
		if(this.entity == null)
            this.entity = new ArrayList<Entity>();
		return entity;
	}

	public EntityList setEntityList(List<Entity> entity) {
		this.entity = entity;
		return this;
	}

	public Entity getEntity(int index)  {
        return entity == null ? null : entity.get(index);
    }

    public int getEntityCount()  {
        return entity == null ? 0 : entity.size();
    }

    public EntityList addEntity(Entity entity)  {
        if(this.entity == null)
            this.entity = new ArrayList<Entity>();
        this.entity.add(entity);
        return this;
    }
            	    	    	    	    	    	    	    	
    public EntityList removeEntityById(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	
    public EntityList removeEntityByPublished(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.published.equals(obj.published)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public EntityList removeEntityByEntityType(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.entityType.equals(obj.entityType)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	
    public EntityList removeEntityBySendToPlayer(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.sendToPlayer.equals(obj.sendToPlayer)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public EntityList removeEntityBySave(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.save.equals(obj.save)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	    	    	
    public EntityList removeEntityByDestination(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.destination.equals(obj.destination)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public EntityList removeEntityByJson(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.json.equals(obj.json)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public EntityList removeEntityByParams(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.params.equals(obj.params)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	    	    	    	    	    	    	    	    	
    public EntityList removeEntityByFastpath(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.fastpath.equals(obj.fastpath)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public EntityList removeEntityBySenderId(Entity entity)  {
    	if(this.entity == null)
           return this;
            
       	Iterator<Entity> itr = this.entity.iterator();
       	while (itr.hasNext()) {
    	Entity obj = itr.next();
    	
    	    		if (entity.senderId.equals(obj.senderId)) {
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

    public Schema<EntityList> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public EntityList newMessage()
    {
        return new EntityList();
    }

    public Class<EntityList> typeClass()
    {
        return EntityList.class;
    }

    public String messageName()
    {
        return EntityList.class.getSimpleName();
    }

    public String messageFullName()
    {
        return EntityList.class.getName();
    }

    public boolean isInitialized(EntityList message)
    {
        return true;
    }

    public void mergeFrom(Input input, EntityList message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.entity == null)
                        message.entity = new ArrayList<Entity>();
                                        message.entity.add(input.mergeObject(null, Entity.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, EntityList message) throws IOException
    {
    	    	
    	    	
    	    	if(message.entity != null)
        {
            for(Entity entity : message.entity)
            {
                if(entity != null) {
                   	    				output.writeObject(1, entity, Entity.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "entity";
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
    	    	__fieldMap.put("entity", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = EntityList.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static EntityList parseFrom(byte[] bytes) {
	EntityList message = new EntityList();
	ProtobufIOUtil.mergeFrom(bytes, message, EntityList.getSchema());
	return message;
}

public static EntityList parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	EntityList message = new EntityList();
	JsonIOUtil.mergeFrom(bytes, message, EntityList.getSchema(), false);
	return message;
}

public EntityList clone() {
	byte[] bytes = this.toByteArray();
	EntityList entityList = EntityList.parseFrom(bytes);
	return entityList;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, EntityList.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<EntityList> schema = EntityList.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, EntityList.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
