
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


import java.lang.reflect.Field;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.pattern.AskableActorSelection;

import akka.util.Timeout;
import java.util.concurrent.TimeUnit;


import io.gamemachine.core.ActorUtil;

import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;

import io.gamemachine.core.PersistableMessage;


import io.gamemachine.objectdb.Cache;
import io.gamemachine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class Characters implements Externalizable, Message<Characters>, Schema<Characters>, PersistableMessage{



    public static Schema<Characters> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Characters getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Characters DEFAULT_INSTANCE = new Characters();
    static final String defaultScope = Characters.class.getSimpleName();

        public List<Character> characters;
	    	
							    public String id= null;
		    			    
		
    
        
	public static CharactersCache cache() {
		return CharactersCache.getInstance();
	}
	
	public static CharactersStore store() {
		return CharactersStore.getInstance();
	}


    public Characters()
    {
        
    }

	static class CacheRef {
	
		private final CacheUpdate cacheUpdate;
		private final String id;
		
		public CacheRef(CacheUpdate cacheUpdate, String id) {
			this.cacheUpdate = cacheUpdate;
			this.id = id;
		}
		
		public void send() {
			ActorSelection sel = ActorUtil.findLocalDistributed("cacheUpdateHandler", id);
			sel.tell(cacheUpdate,null);
		}
		
		public Characters result(int timeout) {
			ActorSelection sel = ActorUtil.findLocalDistributed("cacheUpdateHandler", id);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Future<Object> future = askable.ask(cacheUpdate, t);
			try {
				Await.result(future, t.duration());
				return cache().getCache().get(id);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static class CharactersCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Characters> cache = new Cache<String, Characters>(120, 5000);
		
		private CharactersCache() {
		}
		
		private static class LazyHolder {
			private static final CharactersCache INSTANCE = new CharactersCache();
		}
	
		public static CharactersCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Characters>(expiration, size);
		}
	
		public Cache<String, Characters> getCache() {
			return cache;
		}
		
		public CacheRef setField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.SET);
		}
		
		public CacheRef incrementField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.INCREMENT);
		}
		
		public CacheRef decrementField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.DECREMENT);
		}
		
		private CacheRef updateField(String id, String field, Object value, int updateType) {
			CacheUpdate cacheUpdate = new CacheUpdate(CharactersCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Characters message) {
			CacheUpdate cacheUpdate = new CacheUpdate(CharactersCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Characters get(String id, int timeout) {
			Characters message = cache.get(id);
			if (message == null) {
				message = Characters.store().get(id, timeout);
			}
			return message;
		}
			
		public static Characters setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Characters message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Characters) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Characters.store().set(message);
			} else {
				message = Characters.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Characters.class.getField(field));
					} catch (NoSuchFieldException e) {
						throw new RuntimeException("No such field "+field);
					} catch (SecurityException e) {
						throw new RuntimeException("Security Exception accessing field "+field);
					}
	        	}
				Field f = cachefields.get(field);
				Class<?> klass = f.getType();
				if (cacheUpdate.getUpdateType() == CacheUpdate.SET) {
					f.set(message, klass.cast(cacheUpdate.getFieldValue()));
				} else {
					int updateType = cacheUpdate.getUpdateType();
					Object value = cacheUpdate.getFieldValue();
					if (klass == Integer.TYPE || klass == Integer.class) {
						Integer i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Integer)f.get(message) + (Integer) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Integer)f.get(message) - (Integer) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Long.TYPE || klass == Long.class) {
						Long i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Long)f.get(message) + (Long) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Long)f.get(message) - (Long) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Double.TYPE || klass == Double.class) {
						Double i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Double)f.get(message) + (Double) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Double)f.get(message) - (Double) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Float.TYPE || klass == Float.class) {
						Float i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Float)f.get(message) + (Float) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Float)f.get(message) - (Float) value;
							f.set(message, klass.cast(i));
						}
					}
				}
				cache.set(message.id, message);
				Characters.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class CharactersStore {
	
		private CharactersStore() {
		}
		
		private static class LazyHolder {
			private static final CharactersStore INSTANCE = new CharactersStore();
		}
	
		public static CharactersStore getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public static String scopeId(String playerId, String id) {
    		return playerId + "##" + id;
    	}
    
	    public static String unscopeId(String id) {
	    	if (id.contains("##")) {
	    		String[] parts = id.split("##");
	        	return parts[1];
	    	} else {
	    		throw new RuntimeException("Expected "+id+" to contain ##");
	    	}
	    }
	    
	    public static String defaultScope() {
	    	return defaultScope;
	    }
		
	    public void set(Characters message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Characters get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Characters message) {
	    	Characters clone = message.clone();
			clone.id = scopeId(scope,message.id);
			ActorSelection sel = ActorUtil.findDistributed("object_store", clone.id);
			sel.tell(clone, null);
		}
			
		public void delete(String scope, String id) {
			String scopedId = scopeId(scope,id);
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			ObjectdbDel del = new ObjectdbDel().setEntityId(scopedId);
			sel.tell(del, null);
		}
			
		public Characters get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Characters");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Characters message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Characters) {
					message = (Characters)result;
				} else if (result instanceof ObjectdbStatus) {
					return null;
				}
			} catch (Exception e) {
				throw new RuntimeException("Operation timed out");
			}
			if (message == null) {
				return null;
			}
			message.id = unscopeId(message.id);
			return message;
		}
		
	}
	

	


	public static void clearModel(Model model) {
    	    	    	    	    	    	    	model.set("characters_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("characters_id",id);
    	        		
    	//}
    	    	    }
    
	public static Characters fromModel(Model model) {
		boolean hasFields = false;
    	Characters message = new Characters();
    	    	    	    	    	    	
    	    	    	String idTestField = model.getString("characters_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public List<Character> getCharactersList() {
		if(this.characters == null)
            this.characters = new ArrayList<Character>();
		return characters;
	}

	public Characters setCharactersList(List<Character> characters) {
		this.characters = characters;
		return this;
	}

	public Character getCharacters(int index)  {
        return characters == null ? null : characters.get(index);
    }

    public int getCharactersCount()  {
        return characters == null ? 0 : characters.size();
    }

    public Characters addCharacters(Character characters)  {
        if(this.characters == null)
            this.characters = new ArrayList<Character>();
        this.characters.add(characters);
        return this;
    }
            	    	    	    	
    public Characters removeCharactersById(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByUmaData(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.umaData.equals(obj.umaData)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByHealth(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.health == obj.health) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByRecordId(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.recordId == obj.recordId) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByPlayerId(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.playerId.equals(obj.playerId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByPart(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.part == obj.part) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByParts(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.parts == obj.parts) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByWorldx(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldx == obj.worldx) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByWorldy(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldy == obj.worldy) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByWorldz(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldz == obj.worldz) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByZone(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.zone == obj.zone) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByStamina(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.stamina == obj.stamina) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByMagic(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.magic == obj.magic) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByIncludeUmaData(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.includeUmaData == obj.includeUmaData) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		            
		public String getId() {
		return id;
	}
	
	public Characters setId(String id) {
		this.id = id;
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

    public Schema<Characters> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Characters newMessage()
    {
        return new Characters();
    }

    public Class<Characters> typeClass()
    {
        return Characters.class;
    }

    public String messageName()
    {
        return Characters.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Characters.class.getName();
    }

    public boolean isInitialized(Characters message)
    {
        return true;
    }

    public void mergeFrom(Input input, Characters message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.characters == null)
                        message.characters = new ArrayList<Character>();
                                        message.characters.add(input.mergeObject(null, Character.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Characters message) throws IOException
    {
    	    	
    	    	
    	    	if(message.characters != null)
        {
            for(Character characters : message.characters)
            {
                if( (Character) characters != null) {
                   	    				output.writeObject(1, characters, Character.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(2, message.id, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Characters");
    	    	//if(this.characters != null) {
    		System.out.println("characters="+this.characters);
    	//}
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	System.out.println("END Characters");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "characters";
        	        	case 2: return "id";
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
    	    	__fieldMap.put("characters", 1);
    	    	__fieldMap.put("id", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Characters.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Characters parseFrom(byte[] bytes) {
	Characters message = new Characters();
	ProtobufIOUtil.mergeFrom(bytes, message, Characters.getSchema());
	return message;
}

public static Characters parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Characters message = new Characters();
	JsonIOUtil.mergeFrom(bytes, message, Characters.getSchema(), false);
	return message;
}

public Characters clone() {
	byte[] bytes = this.toByteArray();
	Characters characters = Characters.parseFrom(bytes);
	return characters;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Characters.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Characters> schema = Characters.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Characters.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Characters.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
