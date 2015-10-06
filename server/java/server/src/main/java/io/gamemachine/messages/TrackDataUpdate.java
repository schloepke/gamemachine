
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
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;

import io.gamemachine.core.PersistableMessage;


import io.gamemachine.objectdb.Cache;
import io.gamemachine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class TrackDataUpdate implements Externalizable, Message<TrackDataUpdate>, Schema<TrackDataUpdate>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(TrackDataUpdate.class);



    public static Schema<TrackDataUpdate> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackDataUpdate getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackDataUpdate DEFAULT_INSTANCE = new TrackDataUpdate();
    static final String defaultScope = TrackDataUpdate.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
					public DynamicMessage dynamicMessage = null;
			    
		
    
        
	public static TrackDataUpdateCache cache() {
		return TrackDataUpdateCache.getInstance();
	}
	
	public static TrackDataUpdateStore store() {
		return TrackDataUpdateStore.getInstance();
	}


    public TrackDataUpdate()
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
		
		public TrackDataUpdate result(int timeout) {
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
	
	public static class TrackDataUpdateCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, TrackDataUpdate> cache = new Cache<String, TrackDataUpdate>(120, 5000);
		
		private TrackDataUpdateCache() {
		}
		
		private static class LazyHolder {
			private static final TrackDataUpdateCache INSTANCE = new TrackDataUpdateCache();
		}
	
		public static TrackDataUpdateCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, TrackDataUpdate>(expiration, size);
		}
	
		public Cache<String, TrackDataUpdate> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(TrackDataUpdateCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(TrackDataUpdate message) {
			CacheUpdate cacheUpdate = new CacheUpdate(TrackDataUpdateCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public TrackDataUpdate get(String id, int timeout) {
			TrackDataUpdate message = cache.get(id);
			if (message == null) {
				message = TrackDataUpdate.store().get(id, timeout);
			}
			return message;
		}
			
		public static TrackDataUpdate setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			TrackDataUpdate message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (TrackDataUpdate) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				TrackDataUpdate.store().set(message);
			} else {
				message = TrackDataUpdate.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, TrackDataUpdate.class.getField(field));
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
				TrackDataUpdate.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class TrackDataUpdateStore {
	
		private TrackDataUpdateStore() {
		}
		
		private static class LazyHolder {
			private static final TrackDataUpdateStore INSTANCE = new TrackDataUpdateStore();
		}
	
		public static TrackDataUpdateStore getInstance() {
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
		
	    public void set(TrackDataUpdate message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public TrackDataUpdate get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, TrackDataUpdate message) {
	    	TrackDataUpdate clone = message.clone();
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
			
		public TrackDataUpdate get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("TrackDataUpdate");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			TrackDataUpdate message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof TrackDataUpdate) {
					message = (TrackDataUpdate)result;
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
    	    	    	    	    	    	model.set("track_data_update_id",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("track_data_update_id",id);
    	        		
    	//}
    	    	    	    }
    
	public static TrackDataUpdate fromModel(Model model) {
		boolean hasFields = false;
    	TrackDataUpdate message = new TrackDataUpdate();
    	    	    	    	    	
    	    			String idTestField = model.getString("track_data_update_id");
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


	            
		public String getId() {
		return id;
	}
	
	public TrackDataUpdate setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public TrackDataUpdate setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
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

    public Schema<TrackDataUpdate> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackDataUpdate newMessage()
    {
        return new TrackDataUpdate();
    }

    public Class<TrackDataUpdate> typeClass()
    {
        return TrackDataUpdate.class;
    }

    public String messageName()
    {
        return TrackDataUpdate.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackDataUpdate.class.getName();
    }

    public boolean isInitialized(TrackDataUpdate message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackDataUpdate message) throws IOException
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
            	                	                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackDataUpdate message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	//if(message.dynamicMessage == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.dynamicMessage != null)
    		output.writeObject(2, message.dynamicMessage, DynamicMessage.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START TrackDataUpdate");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.dynamicMessage != null) {
    		System.out.println("dynamicMessage="+this.dynamicMessage);
    	//}
    	    	System.out.println("END TrackDataUpdate");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "dynamicMessage";
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
    	    	__fieldMap.put("dynamicMessage", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackDataUpdate.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackDataUpdate parseFrom(byte[] bytes) {
	TrackDataUpdate message = new TrackDataUpdate();
	ProtobufIOUtil.mergeFrom(bytes, message, TrackDataUpdate.getSchema());
	return message;
}

public static TrackDataUpdate parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TrackDataUpdate message = new TrackDataUpdate();
	JsonIOUtil.mergeFrom(bytes, message, TrackDataUpdate.getSchema(), false);
	return message;
}

public TrackDataUpdate clone() {
	byte[] bytes = this.toByteArray();
	TrackDataUpdate trackDataUpdate = TrackDataUpdate.parseFrom(bytes);
	return trackDataUpdate;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackDataUpdate.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TrackDataUpdate> schema = TrackDataUpdate.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TrackDataUpdate.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, TrackDataUpdate.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
