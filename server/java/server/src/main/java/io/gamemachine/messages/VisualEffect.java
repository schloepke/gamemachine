
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
public final class VisualEffect implements Externalizable, Message<VisualEffect>, Schema<VisualEffect>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(VisualEffect.class);



    public static Schema<VisualEffect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static VisualEffect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final VisualEffect DEFAULT_INSTANCE = new VisualEffect();
    static final String defaultScope = VisualEffect.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
							    public String prefab= null;
		    			    
		
    
        	
					public GmVector3 startPosition = null;
			    
		
    
        	
					public GmVector3 endPosition = null;
			    
		
    
        	
							    public String type= null;
		    			    
		
    
        	
							    public int duration= 0;
		    			    
		
    
        
	public static VisualEffectCache cache() {
		return VisualEffectCache.getInstance();
	}
	
	public static VisualEffectStore store() {
		return VisualEffectStore.getInstance();
	}


    public VisualEffect()
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
		
		public VisualEffect result(int timeout) {
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
	
	public static class VisualEffectCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, VisualEffect> cache = new Cache<String, VisualEffect>(120, 5000);
		
		private VisualEffectCache() {
		}
		
		private static class LazyHolder {
			private static final VisualEffectCache INSTANCE = new VisualEffectCache();
		}
	
		public static VisualEffectCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, VisualEffect>(expiration, size);
		}
	
		public Cache<String, VisualEffect> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(VisualEffectCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(VisualEffect message) {
			CacheUpdate cacheUpdate = new CacheUpdate(VisualEffectCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public VisualEffect get(String id, int timeout) {
			VisualEffect message = cache.get(id);
			if (message == null) {
				message = VisualEffect.store().get(id, timeout);
			}
			return message;
		}
			
		public static VisualEffect setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			VisualEffect message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (VisualEffect) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				VisualEffect.store().set(message);
			} else {
				message = VisualEffect.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, VisualEffect.class.getField(field));
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
				VisualEffect.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class VisualEffectStore {
	
		private VisualEffectStore() {
		}
		
		private static class LazyHolder {
			private static final VisualEffectStore INSTANCE = new VisualEffectStore();
		}
	
		public static VisualEffectStore getInstance() {
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
		
	    public void set(VisualEffect message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public VisualEffect get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, VisualEffect message) {
	    	VisualEffect clone = message.clone();
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
			
		public VisualEffect get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("VisualEffect");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			VisualEffect message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof VisualEffect) {
					message = (VisualEffect)result;
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
    	    	    	    	    	    	model.set("visual_effect_id",null);
    	    	    	    	    	    	model.set("visual_effect_prefab",null);
    	    	    	    	    	    	    	    	model.set("visual_effect_type",null);
    	    	    	    	    	    	model.set("visual_effect_duration",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("visual_effect_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (prefab != null) {
    	       	    	model.setString("visual_effect_prefab",prefab);
    	        		
    	//}
    	    	    	    	    	    	    	
    	    	    	//if (type != null) {
    	       	    	model.setString("visual_effect_type",type);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (duration != null) {
    	       	    	model.setInteger("visual_effect_duration",duration);
    	        		
    	//}
    	    	    }
    
	public static VisualEffect fromModel(Model model) {
		boolean hasFields = false;
    	VisualEffect message = new VisualEffect();
    	    	    	    	    	
    	    			String idTestField = model.getString("visual_effect_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String prefabTestField = model.getString("visual_effect_prefab");
		if (prefabTestField != null) {
			String prefabField = prefabTestField;
			message.setPrefab(prefabField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	    	
    	    			String typeTestField = model.getString("visual_effect_type");
		if (typeTestField != null) {
			String typeField = typeTestField;
			message.setType(typeField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer durationTestField = model.getInteger("visual_effect_duration");
		if (durationTestField != null) {
			int durationField = durationTestField;
			message.setDuration(durationField);
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
	
	public VisualEffect setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getPrefab() {
		return prefab;
	}
	
	public VisualEffect setPrefab(String prefab) {
		this.prefab = prefab;
		return this;	}
	
		            
		public GmVector3 getStartPosition() {
		return startPosition;
	}
	
	public VisualEffect setStartPosition(GmVector3 startPosition) {
		this.startPosition = startPosition;
		return this;	}
	
		            
		public GmVector3 getEndPosition() {
		return endPosition;
	}
	
	public VisualEffect setEndPosition(GmVector3 endPosition) {
		this.endPosition = endPosition;
		return this;	}
	
		            
		public String getType() {
		return type;
	}
	
	public VisualEffect setType(String type) {
		this.type = type;
		return this;	}
	
		            
		public int getDuration() {
		return duration;
	}
	
	public VisualEffect setDuration(int duration) {
		this.duration = duration;
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

    public Schema<VisualEffect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public VisualEffect newMessage()
    {
        return new VisualEffect();
    }

    public Class<VisualEffect> typeClass()
    {
        return VisualEffect.class;
    }

    public String messageName()
    {
        return VisualEffect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return VisualEffect.class.getName();
    }

    public boolean isInitialized(VisualEffect message)
    {
        return true;
    }

    public void mergeFrom(Input input, VisualEffect message) throws IOException
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
            	                	                	message.prefab = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.startPosition = input.mergeObject(message.startPosition, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.endPosition = input.mergeObject(message.endPosition, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 5:
            	                	                	message.type = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.duration = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, VisualEffect message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.prefab != null) {
            output.writeString(2, message.prefab, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.startPosition != null)
    		output.writeObject(3, message.startPosition, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.endPosition != null)
    		output.writeObject(4, message.endPosition, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.type != null) {
            output.writeString(5, message.type, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.duration != null) {
            output.writeInt32(6, message.duration, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START VisualEffect");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.prefab != null) {
    		System.out.println("prefab="+this.prefab);
    	//}
    	    	//if(this.startPosition != null) {
    		System.out.println("startPosition="+this.startPosition);
    	//}
    	    	//if(this.endPosition != null) {
    		System.out.println("endPosition="+this.endPosition);
    	//}
    	    	//if(this.type != null) {
    		System.out.println("type="+this.type);
    	//}
    	    	//if(this.duration != null) {
    		System.out.println("duration="+this.duration);
    	//}
    	    	System.out.println("END VisualEffect");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "prefab";
        	        	case 3: return "startPosition";
        	        	case 4: return "endPosition";
        	        	case 5: return "type";
        	        	case 6: return "duration";
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
    	    	__fieldMap.put("prefab", 2);
    	    	__fieldMap.put("startPosition", 3);
    	    	__fieldMap.put("endPosition", 4);
    	    	__fieldMap.put("type", 5);
    	    	__fieldMap.put("duration", 6);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = VisualEffect.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static VisualEffect parseFrom(byte[] bytes) {
	VisualEffect message = new VisualEffect();
	ProtobufIOUtil.mergeFrom(bytes, message, VisualEffect.getSchema());
	return message;
}

public static VisualEffect parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	VisualEffect message = new VisualEffect();
	JsonIOUtil.mergeFrom(bytes, message, VisualEffect.getSchema(), false);
	return message;
}

public VisualEffect clone() {
	byte[] bytes = this.toByteArray();
	VisualEffect visualEffect = VisualEffect.parseFrom(bytes);
	return visualEffect;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, VisualEffect.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<VisualEffect> schema = VisualEffect.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, VisualEffect.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, VisualEffect.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
