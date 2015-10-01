
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
public final class RemovePlayerItem implements Externalizable, Message<RemovePlayerItem>, Schema<RemovePlayerItem>, PersistableMessage{



    public static Schema<RemovePlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static RemovePlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final RemovePlayerItem DEFAULT_INSTANCE = new RemovePlayerItem();
    static final String defaultScope = RemovePlayerItem.class.getSimpleName();

    			public String id;
	    
        			public Integer quantity;
	    
        			public Integer result;
	    
        			public String characterId;
	    
        
	public static RemovePlayerItemCache cache() {
		return RemovePlayerItemCache.getInstance();
	}
	
	public static RemovePlayerItemStore store() {
		return RemovePlayerItemStore.getInstance();
	}


    public RemovePlayerItem()
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
		
		public RemovePlayerItem result(int timeout) {
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
	
	public static class RemovePlayerItemCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, RemovePlayerItem> cache = new Cache<String, RemovePlayerItem>(120, 5000);
		
		private RemovePlayerItemCache() {
		}
		
		private static class LazyHolder {
			private static final RemovePlayerItemCache INSTANCE = new RemovePlayerItemCache();
		}
	
		public static RemovePlayerItemCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, RemovePlayerItem>(expiration, size);
		}
	
		public Cache<String, RemovePlayerItem> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(RemovePlayerItemCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(RemovePlayerItem message) {
			CacheUpdate cacheUpdate = new CacheUpdate(RemovePlayerItemCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public RemovePlayerItem get(String id, int timeout) {
			RemovePlayerItem message = cache.get(id);
			if (message == null) {
				message = RemovePlayerItem.store().get(id, timeout);
			}
			return message;
		}
			
		public static RemovePlayerItem setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			RemovePlayerItem message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (RemovePlayerItem) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				RemovePlayerItem.store().set(message);
			} else {
				message = RemovePlayerItem.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, RemovePlayerItem.class.getField(field));
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
				RemovePlayerItem.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class RemovePlayerItemStore {
	
		private RemovePlayerItemStore() {
		}
		
		private static class LazyHolder {
			private static final RemovePlayerItemStore INSTANCE = new RemovePlayerItemStore();
		}
	
		public static RemovePlayerItemStore getInstance() {
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
		
	    public void set(RemovePlayerItem message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public RemovePlayerItem get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, RemovePlayerItem message) {
	    	RemovePlayerItem clone = message.clone();
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
			
		public RemovePlayerItem get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("RemovePlayerItem");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			RemovePlayerItem message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof RemovePlayerItem) {
					message = (RemovePlayerItem)result;
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
    	    	    	    	    	    	model.set("remove_player_item_id",null);
    	    	    	    	    	    	model.set("remove_player_item_quantity",null);
    	    	    	    	    	    	model.set("remove_player_item_result",null);
    	    	    	    	    	    	model.set("remove_player_item_character_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("remove_player_item_id",id);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (quantity != null) {
    	       	    	model.setInteger("remove_player_item_quantity",quantity);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (result != null) {
    	       	    	model.setInteger("remove_player_item_result",result);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (characterId != null) {
    	       	    	model.setString("remove_player_item_character_id",characterId);
    	        		
    	}
    	    	    }
    
	public static RemovePlayerItem fromModel(Model model) {
		boolean hasFields = false;
    	RemovePlayerItem message = new RemovePlayerItem();
    	    	    	    	    	
    	    	    	String idField = model.getString("remove_player_item_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer quantityField = model.getInteger("remove_player_item_quantity");
    	    	
    	if (quantityField != null) {
    		message.setQuantity(quantityField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer resultField = model.getInteger("remove_player_item_result");
    	    	
    	if (resultField != null) {
    		message.setResult(resultField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String characterIdField = model.getString("remove_player_item_character_id");
    	    	
    	if (characterIdField != null) {
    		message.setCharacterId(characterIdField);
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
	
	public RemovePlayerItem setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasQuantity()  {
        return quantity == null ? false : true;
    }
        
		public Integer getQuantity() {
		return quantity;
	}
	
	public RemovePlayerItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;	}
	
		    
    public Boolean hasResult()  {
        return result == null ? false : true;
    }
        
		public Integer getResult() {
		return result;
	}
	
	public RemovePlayerItem setResult(Integer result) {
		this.result = result;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public RemovePlayerItem setCharacterId(String characterId) {
		this.characterId = characterId;
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

    public Schema<RemovePlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public RemovePlayerItem newMessage()
    {
        return new RemovePlayerItem();
    }

    public Class<RemovePlayerItem> typeClass()
    {
        return RemovePlayerItem.class;
    }

    public String messageName()
    {
        return RemovePlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return RemovePlayerItem.class.getName();
    }

    public boolean isInitialized(RemovePlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, RemovePlayerItem message) throws IOException
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
            	                	                	message.quantity = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.result = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, RemovePlayerItem message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	if(message.quantity == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.quantity != null)
            output.writeInt32(2, message.quantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.result != null)
            output.writeInt32(3, message.result, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(4, message.characterId, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START RemovePlayerItem");
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.quantity != null) {
    		System.out.println("quantity="+this.quantity);
    	}
    	    	if(this.result != null) {
    		System.out.println("result="+this.result);
    	}
    	    	if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	}
    	    	System.out.println("END RemovePlayerItem");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "quantity";
        	        	case 3: return "result";
        	        	case 4: return "characterId";
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
    	    	__fieldMap.put("quantity", 2);
    	    	__fieldMap.put("result", 3);
    	    	__fieldMap.put("characterId", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = RemovePlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static RemovePlayerItem parseFrom(byte[] bytes) {
	RemovePlayerItem message = new RemovePlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, RemovePlayerItem.getSchema());
	return message;
}

public static RemovePlayerItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	RemovePlayerItem message = new RemovePlayerItem();
	JsonIOUtil.mergeFrom(bytes, message, RemovePlayerItem.getSchema(), false);
	return message;
}

public RemovePlayerItem clone() {
	byte[] bytes = this.toByteArray();
	RemovePlayerItem removePlayerItem = RemovePlayerItem.parseFrom(bytes);
	return removePlayerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, RemovePlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<RemovePlayerItem> schema = RemovePlayerItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RemovePlayerItem.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RemovePlayerItem.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
