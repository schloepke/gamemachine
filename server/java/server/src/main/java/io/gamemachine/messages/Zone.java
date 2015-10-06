
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
public final class Zone implements Externalizable, Message<Zone>, Schema<Zone>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(Zone.class);

	public enum Status implements io.protostuff.EnumLite<Status>
    {
    	
    	    	NONE(0),    	    	UP(1),    	    	DOWN(2),    	    	REQUEST_UP(3),    	    	REQUEST_DOWN(4);    	        
        public final int number;
        
        private Status (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Status defaultValue() {
        	return (NONE);
        }
        
        public static Status valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (UP);
    			    			case 2: return (DOWN);
    			    			case 3: return (REQUEST_UP);
    			    			case 4: return (REQUEST_DOWN);
    			                default: return null;
            }
        }
    }


    public static Schema<Zone> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Zone getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Zone DEFAULT_INSTANCE = new Zone();
    static final String defaultScope = Zone.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
					public Status status = Status.defaultValue();
			    
		
    
        	
							    public String name= null;
		    			    
		
    
        	
							    public String unityClient= null;
		    			    
		
    
        
	public static ZoneCache cache() {
		return ZoneCache.getInstance();
	}
	
	public static ZoneStore store() {
		return ZoneStore.getInstance();
	}


    public Zone()
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
		
		public Zone result(int timeout) {
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
	
	public static class ZoneCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Zone> cache = new Cache<String, Zone>(120, 5000);
		
		private ZoneCache() {
		}
		
		private static class LazyHolder {
			private static final ZoneCache INSTANCE = new ZoneCache();
		}
	
		public static ZoneCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Zone>(expiration, size);
		}
	
		public Cache<String, Zone> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(ZoneCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Zone message) {
			CacheUpdate cacheUpdate = new CacheUpdate(ZoneCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Zone get(String id, int timeout) {
			Zone message = cache.get(id);
			if (message == null) {
				message = Zone.store().get(id, timeout);
			}
			return message;
		}
			
		public static Zone setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Zone message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Zone) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Zone.store().set(message);
			} else {
				message = Zone.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Zone.class.getField(field));
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
				Zone.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class ZoneStore {
	
		private ZoneStore() {
		}
		
		private static class LazyHolder {
			private static final ZoneStore INSTANCE = new ZoneStore();
		}
	
		public static ZoneStore getInstance() {
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
		
	    public void set(Zone message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Zone get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Zone message) {
	    	Zone clone = message.clone();
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
			
		public Zone get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Zone");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Zone message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Zone) {
					message = (Zone)result;
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
    	    	    	    	    	    	model.set("zone_id",null);
    	    	    	    	    	    	    	model.set("zone_name",null);
    	    	    	    	    	    	model.set("zone_unity_client",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("zone_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (status != null) {
    	       	    	model.setInteger("zone_status",status.number);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (name != null) {
    	       	    	model.setString("zone_name",name);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (unityClient != null) {
    	       	    	model.setString("zone_unity_client",unityClient);
    	        		
    	//}
    	    	    }
    
	public static Zone fromModel(Model model) {
		boolean hasFields = false;
    	Zone message = new Zone();
    	    	    	    	    	
    	    			String idTestField = model.getString("zone_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    				message.setStatus(Status.valueOf(model.getInteger("zone_status")));
    	    	    	    	    	    	
    	    			String nameTestField = model.getString("zone_name");
		if (nameTestField != null) {
			String nameField = nameTestField;
			message.setName(nameField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String unityClientTestField = model.getString("zone_unity_client");
		if (unityClientTestField != null) {
			String unityClientField = unityClientTestField;
			message.setUnityClient(unityClientField);
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
	
	public Zone setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public Status getStatus() {
		return status;
	}
	
	public Zone setStatus(Status status) {
		this.status = status;
		return this;	}
	
		            
		public String getName() {
		return name;
	}
	
	public Zone setName(String name) {
		this.name = name;
		return this;	}
	
		            
		public String getUnityClient() {
		return unityClient;
	}
	
	public Zone setUnityClient(String unityClient) {
		this.unityClient = unityClient;
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

    public Schema<Zone> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Zone newMessage()
    {
        return new Zone();
    }

    public Class<Zone> typeClass()
    {
        return Zone.class;
    }

    public String messageName()
    {
        return Zone.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Zone.class.getName();
    }

    public boolean isInitialized(Zone message)
    {
        return true;
    }

    public void mergeFrom(Input input, Zone message) throws IOException
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
            	                	                    message.status = Status.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 3:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.unityClient = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Zone message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.status != null)
    	 	output.writeEnum(2, message.status.number, false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.name != null) {
            output.writeString(3, message.name, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.unityClient != null) {
            output.writeString(4, message.unityClient, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Zone");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.status != null) {
    		System.out.println("status="+this.status);
    	//}
    	    	//if(this.name != null) {
    		System.out.println("name="+this.name);
    	//}
    	    	//if(this.unityClient != null) {
    		System.out.println("unityClient="+this.unityClient);
    	//}
    	    	System.out.println("END Zone");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "status";
        	        	case 3: return "name";
        	        	case 4: return "unityClient";
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
    	    	__fieldMap.put("status", 2);
    	    	__fieldMap.put("name", 3);
    	    	__fieldMap.put("unityClient", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Zone.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Zone parseFrom(byte[] bytes) {
	Zone message = new Zone();
	ProtobufIOUtil.mergeFrom(bytes, message, Zone.getSchema());
	return message;
}

public static Zone parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Zone message = new Zone();
	JsonIOUtil.mergeFrom(bytes, message, Zone.getSchema(), false);
	return message;
}

public Zone clone() {
	byte[] bytes = this.toByteArray();
	Zone zone = Zone.parseFrom(bytes);
	return zone;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Zone.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Zone> schema = Zone.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Zone.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Zone.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
