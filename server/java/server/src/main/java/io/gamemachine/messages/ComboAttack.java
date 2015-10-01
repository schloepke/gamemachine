
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
public final class ComboAttack implements Externalizable, Message<ComboAttack>, Schema<ComboAttack>, PersistableMessage{



    public static Schema<ComboAttack> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ComboAttack getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ComboAttack DEFAULT_INSTANCE = new ComboAttack();
    static final String defaultScope = ComboAttack.class.getSimpleName();

    			public String id;
	    
            public List<Attack> attack;
	    
	public static ComboAttackCache cache() {
		return ComboAttackCache.getInstance();
	}
	
	public static ComboAttackStore store() {
		return ComboAttackStore.getInstance();
	}


    public ComboAttack()
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
		
		public ComboAttack result(int timeout) {
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
	
	public static class ComboAttackCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, ComboAttack> cache = new Cache<String, ComboAttack>(120, 5000);
		
		private ComboAttackCache() {
		}
		
		private static class LazyHolder {
			private static final ComboAttackCache INSTANCE = new ComboAttackCache();
		}
	
		public static ComboAttackCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, ComboAttack>(expiration, size);
		}
	
		public Cache<String, ComboAttack> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(ComboAttackCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(ComboAttack message) {
			CacheUpdate cacheUpdate = new CacheUpdate(ComboAttackCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public ComboAttack get(String id, int timeout) {
			ComboAttack message = cache.get(id);
			if (message == null) {
				message = ComboAttack.store().get(id, timeout);
			}
			return message;
		}
			
		public static ComboAttack setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			ComboAttack message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (ComboAttack) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				ComboAttack.store().set(message);
			} else {
				message = ComboAttack.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, ComboAttack.class.getField(field));
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
				ComboAttack.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class ComboAttackStore {
	
		private ComboAttackStore() {
		}
		
		private static class LazyHolder {
			private static final ComboAttackStore INSTANCE = new ComboAttackStore();
		}
	
		public static ComboAttackStore getInstance() {
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
		
	    public void set(ComboAttack message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public ComboAttack get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, ComboAttack message) {
	    	ComboAttack clone = message.clone();
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
			
		public ComboAttack get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("ComboAttack");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			ComboAttack message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof ComboAttack) {
					message = (ComboAttack)result;
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
    	    	    	    	    	    	model.set("combo_attack_id",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("combo_attack_id",id);
    	        		
    	}
    	    	    	    }
    
	public static ComboAttack fromModel(Model model) {
		boolean hasFields = false;
    	ComboAttack message = new ComboAttack();
    	    	    	    	    	
    	    	    	String idField = model.getString("combo_attack_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public ComboAttack setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasAttack()  {
        return attack == null ? false : true;
    }
        
		public List<Attack> getAttackList() {
		if(this.attack == null)
            this.attack = new ArrayList<Attack>();
		return attack;
	}

	public ComboAttack setAttackList(List<Attack> attack) {
		this.attack = attack;
		return this;
	}

	public Attack getAttack(int index)  {
        return attack == null ? null : attack.get(index);
    }

    public int getAttackCount()  {
        return attack == null ? 0 : attack.size();
    }

    public ComboAttack addAttack(Attack attack)  {
        if(this.attack == null)
            this.attack = new ArrayList<Attack>();
        this.attack.add(attack);
        return this;
    }
            	    	    	    	
    public ComboAttack removeAttackByAttacker(Attack attack)  {
    	if(this.attack == null)
           return this;
            
       	Iterator<Attack> itr = this.attack.iterator();
       	while (itr.hasNext()) {
    	Attack obj = itr.next();
    	
    	    		if (attack.attacker.equals(obj.attacker)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ComboAttack removeAttackByTarget(Attack attack)  {
    	if(this.attack == null)
           return this;
            
       	Iterator<Attack> itr = this.attack.iterator();
       	while (itr.hasNext()) {
    	Attack obj = itr.next();
    	
    	    		if (attack.target.equals(obj.target)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ComboAttack removeAttackBySkill(Attack attack)  {
    	if(this.attack == null)
           return this;
            
       	Iterator<Attack> itr = this.attack.iterator();
       	while (itr.hasNext()) {
    	Attack obj = itr.next();
    	
    	    		if (attack.skill.equals(obj.skill)) {
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

    public Schema<ComboAttack> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ComboAttack newMessage()
    {
        return new ComboAttack();
    }

    public Class<ComboAttack> typeClass()
    {
        return ComboAttack.class;
    }

    public String messageName()
    {
        return ComboAttack.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ComboAttack.class.getName();
    }

    public boolean isInitialized(ComboAttack message)
    {
        return true;
    }

    public void mergeFrom(Input input, ComboAttack message) throws IOException
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
            	            		if(message.attack == null)
                        message.attack = new ArrayList<Attack>();
                                        message.attack.add(input.mergeObject(null, Attack.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ComboAttack message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	if(message.attack != null)
        {
            for(Attack attack : message.attack)
            {
                if(attack != null) {
                   	    				output.writeObject(2, attack, Attack.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ComboAttack");
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.attack != null) {
    		System.out.println("attack="+this.attack);
    	}
    	    	System.out.println("END ComboAttack");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "attack";
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
    	    	__fieldMap.put("attack", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ComboAttack.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ComboAttack parseFrom(byte[] bytes) {
	ComboAttack message = new ComboAttack();
	ProtobufIOUtil.mergeFrom(bytes, message, ComboAttack.getSchema());
	return message;
}

public static ComboAttack parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ComboAttack message = new ComboAttack();
	JsonIOUtil.mergeFrom(bytes, message, ComboAttack.getSchema(), false);
	return message;
}

public ComboAttack clone() {
	byte[] bytes = this.toByteArray();
	ComboAttack comboAttack = ComboAttack.parseFrom(bytes);
	return comboAttack;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ComboAttack.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ComboAttack> schema = ComboAttack.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ComboAttack.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ComboAttack.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
