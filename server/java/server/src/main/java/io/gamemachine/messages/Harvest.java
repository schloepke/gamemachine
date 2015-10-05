
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
public final class Harvest implements Externalizable, Message<Harvest>, Schema<Harvest>, PersistableMessage{



    public static Schema<Harvest> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Harvest getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Harvest DEFAULT_INSTANCE = new Harvest();
    static final String defaultScope = Harvest.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
							    public int result= 0;
		    			    
		
    
        	
							    public long harvestedAt= 0L;
		    			    
		
    
        	
							    public String characterId= null;
		    			    
		
    
        	
							    public String itemId= null;
		    			    
		
    
        
	public static HarvestCache cache() {
		return HarvestCache.getInstance();
	}
	
	public static HarvestStore store() {
		return HarvestStore.getInstance();
	}


    public Harvest()
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
		
		public Harvest result(int timeout) {
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
	
	public static class HarvestCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Harvest> cache = new Cache<String, Harvest>(120, 5000);
		
		private HarvestCache() {
		}
		
		private static class LazyHolder {
			private static final HarvestCache INSTANCE = new HarvestCache();
		}
	
		public static HarvestCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Harvest>(expiration, size);
		}
	
		public Cache<String, Harvest> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(HarvestCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Harvest message) {
			CacheUpdate cacheUpdate = new CacheUpdate(HarvestCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Harvest get(String id, int timeout) {
			Harvest message = cache.get(id);
			if (message == null) {
				message = Harvest.store().get(id, timeout);
			}
			return message;
		}
			
		public static Harvest setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Harvest message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Harvest) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Harvest.store().set(message);
			} else {
				message = Harvest.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Harvest.class.getField(field));
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
				Harvest.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class HarvestStore {
	
		private HarvestStore() {
		}
		
		private static class LazyHolder {
			private static final HarvestStore INSTANCE = new HarvestStore();
		}
	
		public static HarvestStore getInstance() {
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
		
	    public void set(Harvest message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Harvest get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Harvest message) {
	    	Harvest clone = message.clone();
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
			
		public Harvest get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Harvest");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Harvest message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Harvest) {
					message = (Harvest)result;
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
    	    	    	    	    	    	model.set("harvest_id",null);
    	    	    	    	    	    	model.set("harvest_result",null);
    	    	    	    	    	    	model.set("harvest_harvested_at",null);
    	    	    	    	    	    	model.set("harvest_character_id",null);
    	    	    	    	    	    	model.set("harvest_item_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("harvest_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (result != null) {
    	       	    	model.setInteger("harvest_result",result);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (harvestedAt != null) {
    	       	    	model.setLong("harvest_harvested_at",harvestedAt);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (characterId != null) {
    	       	    	model.setString("harvest_character_id",characterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (itemId != null) {
    	       	    	model.setString("harvest_item_id",itemId);
    	        		
    	//}
    	    	    }
    
	public static Harvest fromModel(Model model) {
		boolean hasFields = false;
    	Harvest message = new Harvest();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("harvest_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer resultTestField = model.getInteger("harvest_result");
    	if (resultTestField != null) {
    		int resultField = resultTestField;
    		message.setResult(resultField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Long harvestedAtTestField = model.getLong("harvest_harvested_at");
    	if (harvestedAtTestField != null) {
    		long harvestedAtField = harvestedAtTestField;
    		message.setHarvestedAt(harvestedAtField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String characterIdTestField = model.getString("harvest_character_id");
    	if (characterIdTestField != null) {
    		String characterIdField = characterIdTestField;
    		message.setCharacterId(characterIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String itemIdTestField = model.getString("harvest_item_id");
    	if (itemIdTestField != null) {
    		String itemIdField = itemIdTestField;
    		message.setItemId(itemIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getId() {
		return id;
	}
	
	public Harvest setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public int getResult() {
		return result;
	}
	
	public Harvest setResult(int result) {
		this.result = result;
		return this;	}
	
		            
		public long getHarvestedAt() {
		return harvestedAt;
	}
	
	public Harvest setHarvestedAt(long harvestedAt) {
		this.harvestedAt = harvestedAt;
		return this;	}
	
		            
		public String getCharacterId() {
		return characterId;
	}
	
	public Harvest setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		            
		public String getItemId() {
		return itemId;
	}
	
	public Harvest setItemId(String itemId) {
		this.itemId = itemId;
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

    public Schema<Harvest> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Harvest newMessage()
    {
        return new Harvest();
    }

    public Class<Harvest> typeClass()
    {
        return Harvest.class;
    }

    public String messageName()
    {
        return Harvest.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Harvest.class.getName();
    }

    public boolean isInitialized(Harvest message)
    {
        return true;
    }

    public void mergeFrom(Input input, Harvest message) throws IOException
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
            	                	                	message.result = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.harvestedAt = input.readInt64();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.itemId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Harvest message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.result != null) {
            output.writeInt32(2, message.result, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.harvestedAt != null) {
            output.writeInt64(3, message.harvestedAt, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.characterId != null) {
            output.writeString(4, message.characterId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.itemId != null) {
            output.writeString(5, message.itemId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Harvest");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.result != null) {
    		System.out.println("result="+this.result);
    	//}
    	    	//if(this.harvestedAt != null) {
    		System.out.println("harvestedAt="+this.harvestedAt);
    	//}
    	    	//if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	//}
    	    	//if(this.itemId != null) {
    		System.out.println("itemId="+this.itemId);
    	//}
    	    	System.out.println("END Harvest");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "result";
        	        	case 3: return "harvestedAt";
        	        	case 4: return "characterId";
        	        	case 5: return "itemId";
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
    	    	__fieldMap.put("result", 2);
    	    	__fieldMap.put("harvestedAt", 3);
    	    	__fieldMap.put("characterId", 4);
    	    	__fieldMap.put("itemId", 5);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Harvest.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Harvest parseFrom(byte[] bytes) {
	Harvest message = new Harvest();
	ProtobufIOUtil.mergeFrom(bytes, message, Harvest.getSchema());
	return message;
}

public static Harvest parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Harvest message = new Harvest();
	JsonIOUtil.mergeFrom(bytes, message, Harvest.getSchema(), false);
	return message;
}

public Harvest clone() {
	byte[] bytes = this.toByteArray();
	Harvest harvest = Harvest.parseFrom(bytes);
	return harvest;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Harvest.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Harvest> schema = Harvest.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Harvest.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Harvest.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
