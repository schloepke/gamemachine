
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
public final class CraftingElement implements Externalizable, Message<CraftingElement>, Schema<CraftingElement>, PersistableMessage{



    public static Schema<CraftingElement> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftingElement getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftingElement DEFAULT_INSTANCE = new CraftingElement();
    static final String defaultScope = CraftingElement.class.getSimpleName();

    	
	    	    public String id= null;
	    		
    
        	
	    	    public int quantity= 0;
	    		
    
        	
	    	    public int level= 0;
	    		
    
        	
	    	    public int order= 0;
	    		
    
        
	public static CraftingElementCache cache() {
		return CraftingElementCache.getInstance();
	}
	
	public static CraftingElementStore store() {
		return CraftingElementStore.getInstance();
	}


    public CraftingElement()
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
		
		public CraftingElement result(int timeout) {
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
	
	public static class CraftingElementCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, CraftingElement> cache = new Cache<String, CraftingElement>(120, 5000);
		
		private CraftingElementCache() {
		}
		
		private static class LazyHolder {
			private static final CraftingElementCache INSTANCE = new CraftingElementCache();
		}
	
		public static CraftingElementCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, CraftingElement>(expiration, size);
		}
	
		public Cache<String, CraftingElement> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(CraftingElementCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(CraftingElement message) {
			CacheUpdate cacheUpdate = new CacheUpdate(CraftingElementCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public CraftingElement get(String id, int timeout) {
			CraftingElement message = cache.get(id);
			if (message == null) {
				message = CraftingElement.store().get(id, timeout);
			}
			return message;
		}
			
		public static CraftingElement setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			CraftingElement message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (CraftingElement) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				CraftingElement.store().set(message);
			} else {
				message = CraftingElement.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, CraftingElement.class.getField(field));
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
				CraftingElement.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class CraftingElementStore {
	
		private CraftingElementStore() {
		}
		
		private static class LazyHolder {
			private static final CraftingElementStore INSTANCE = new CraftingElementStore();
		}
	
		public static CraftingElementStore getInstance() {
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
		
	    public void set(CraftingElement message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public CraftingElement get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, CraftingElement message) {
	    	CraftingElement clone = message.clone();
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
			
		public CraftingElement get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("CraftingElement");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			CraftingElement message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof CraftingElement) {
					message = (CraftingElement)result;
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
    	    	    	    	    	    	model.set("crafting_element_id",null);
    	    	    	    	    	    	model.set("crafting_element_quantity",null);
    	    	    	    	    	    	model.set("crafting_element_level",null);
    	    	    	    	    	    	model.set("crafting_element_order",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("crafting_element_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (quantity != null) {
    	       	    	model.setInteger("crafting_element_quantity",quantity);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (level != null) {
    	       	    	model.setInteger("crafting_element_level",level);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (order != null) {
    	       	    	model.setInteger("crafting_element_order",order);
    	        		
    	//}
    	    	    }
    
	public static CraftingElement fromModel(Model model) {
		boolean hasFields = false;
    	CraftingElement message = new CraftingElement();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("crafting_element_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer quantityTestField = model.getInteger("crafting_element_quantity");
    	if (quantityTestField != null) {
    		int quantityField = quantityTestField;
    		message.setQuantity(quantityField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer levelTestField = model.getInteger("crafting_element_level");
    	if (levelTestField != null) {
    		int levelField = levelTestField;
    		message.setLevel(levelField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer orderTestField = model.getInteger("crafting_element_order");
    	if (orderTestField != null) {
    		int orderField = orderTestField;
    		message.setOrder(orderField);
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
	
	public CraftingElement setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public int getQuantity() {
		return quantity;
	}
	
	public CraftingElement setQuantity(int quantity) {
		this.quantity = quantity;
		return this;	}
	
		            
		public int getLevel() {
		return level;
	}
	
	public CraftingElement setLevel(int level) {
		this.level = level;
		return this;	}
	
		            
		public int getOrder() {
		return order;
	}
	
	public CraftingElement setOrder(int order) {
		this.order = order;
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

    public Schema<CraftingElement> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftingElement newMessage()
    {
        return new CraftingElement();
    }

    public Class<CraftingElement> typeClass()
    {
        return CraftingElement.class;
    }

    public String messageName()
    {
        return CraftingElement.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftingElement.class.getName();
    }

    public boolean isInitialized(CraftingElement message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftingElement message) throws IOException
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
            	                	                	message.level = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.order = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftingElement message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	//if(message.quantity == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.quantity != null) {
            output.writeInt32(2, message.quantity, false);
        }
    	    	
    	            	
    	    	//if(message.level == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.level != null) {
            output.writeInt32(3, message.level, false);
        }
    	    	
    	            	
    	    	//if(message.order == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.order != null) {
            output.writeInt32(4, message.order, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START CraftingElement");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.quantity != null) {
    		System.out.println("quantity="+this.quantity);
    	//}
    	    	//if(this.level != null) {
    		System.out.println("level="+this.level);
    	//}
    	    	//if(this.order != null) {
    		System.out.println("order="+this.order);
    	//}
    	    	System.out.println("END CraftingElement");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "quantity";
        	        	case 3: return "level";
        	        	case 4: return "order";
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
    	    	__fieldMap.put("level", 3);
    	    	__fieldMap.put("order", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftingElement.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftingElement parseFrom(byte[] bytes) {
	CraftingElement message = new CraftingElement();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftingElement.getSchema());
	return message;
}

public static CraftingElement parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftingElement message = new CraftingElement();
	JsonIOUtil.mergeFrom(bytes, message, CraftingElement.getSchema(), false);
	return message;
}

public CraftingElement clone() {
	byte[] bytes = this.toByteArray();
	CraftingElement craftingElement = CraftingElement.parseFrom(bytes);
	return craftingElement;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftingElement.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftingElement> schema = CraftingElement.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftingElement.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, CraftingElement.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
