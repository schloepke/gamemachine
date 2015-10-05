
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
public final class Neighbor implements Externalizable, Message<Neighbor>, Schema<Neighbor>, PersistableMessage{



    public static Schema<Neighbor> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Neighbor getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Neighbor DEFAULT_INSTANCE = new Neighbor();
    static final String defaultScope = Neighbor.class.getSimpleName();

    	
	    	    public TrackData trackData;
	    		
    
        	
	    	    public GmVector3 location;
	    		
    
        	
	    	    public String id= null;
	    		
    
        
	public static NeighborCache cache() {
		return NeighborCache.getInstance();
	}
	
	public static NeighborStore store() {
		return NeighborStore.getInstance();
	}


    public Neighbor()
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
		
		public Neighbor result(int timeout) {
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
	
	public static class NeighborCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Neighbor> cache = new Cache<String, Neighbor>(120, 5000);
		
		private NeighborCache() {
		}
		
		private static class LazyHolder {
			private static final NeighborCache INSTANCE = new NeighborCache();
		}
	
		public static NeighborCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Neighbor>(expiration, size);
		}
	
		public Cache<String, Neighbor> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(NeighborCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Neighbor message) {
			CacheUpdate cacheUpdate = new CacheUpdate(NeighborCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Neighbor get(String id, int timeout) {
			Neighbor message = cache.get(id);
			if (message == null) {
				message = Neighbor.store().get(id, timeout);
			}
			return message;
		}
			
		public static Neighbor setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Neighbor message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Neighbor) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Neighbor.store().set(message);
			} else {
				message = Neighbor.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Neighbor.class.getField(field));
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
				Neighbor.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class NeighborStore {
	
		private NeighborStore() {
		}
		
		private static class LazyHolder {
			private static final NeighborStore INSTANCE = new NeighborStore();
		}
	
		public static NeighborStore getInstance() {
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
		
	    public void set(Neighbor message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Neighbor get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Neighbor message) {
	    	Neighbor clone = message.clone();
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
			
		public Neighbor get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Neighbor");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Neighbor message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Neighbor) {
					message = (Neighbor)result;
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
    	    	    	    	    	    	    	    	model.set("neighbor_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("neighbor_id",id);
    	        		
    	//}
    	    	    }
    
	public static Neighbor fromModel(Model model) {
		boolean hasFields = false;
    	Neighbor message = new Neighbor();
    	    	    	    	    	    	    	
    	    	    	String idTestField = model.getString("neighbor_id");
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


	            
		public TrackData getTrackData() {
		return trackData;
	}
	
	public Neighbor setTrackData(TrackData trackData) {
		this.trackData = trackData;
		return this;	}
	
		            
		public GmVector3 getLocation() {
		return location;
	}
	
	public Neighbor setLocation(GmVector3 location) {
		this.location = location;
		return this;	}
	
		            
		public String getId() {
		return id;
	}
	
	public Neighbor setId(String id) {
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

    public Schema<Neighbor> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Neighbor newMessage()
    {
        return new Neighbor();
    }

    public Class<Neighbor> typeClass()
    {
        return Neighbor.class;
    }

    public String messageName()
    {
        return Neighbor.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Neighbor.class.getName();
    }

    public boolean isInitialized(Neighbor message)
    {
        return true;
    }

    public void mergeFrom(Input input, Neighbor message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.trackData = input.mergeObject(message.trackData, TrackData.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.location = input.mergeObject(message.location, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Neighbor message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.trackData != null)
    		output.writeObject(1, message.trackData, TrackData.getSchema(), false);
    	    	
    	            	
    	    	//if(message.location == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.location != null)
    		output.writeObject(2, message.location, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(3, message.id, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Neighbor");
    	    	//if(this.trackData != null) {
    		System.out.println("trackData="+this.trackData);
    	//}
    	    	//if(this.location != null) {
    		System.out.println("location="+this.location);
    	//}
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	System.out.println("END Neighbor");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "trackData";
        	        	case 2: return "location";
        	        	case 3: return "id";
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
    	    	__fieldMap.put("trackData", 1);
    	    	__fieldMap.put("location", 2);
    	    	__fieldMap.put("id", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Neighbor.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Neighbor parseFrom(byte[] bytes) {
	Neighbor message = new Neighbor();
	ProtobufIOUtil.mergeFrom(bytes, message, Neighbor.getSchema());
	return message;
}

public static Neighbor parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Neighbor message = new Neighbor();
	JsonIOUtil.mergeFrom(bytes, message, Neighbor.getSchema(), false);
	return message;
}

public Neighbor clone() {
	byte[] bytes = this.toByteArray();
	Neighbor neighbor = Neighbor.parseFrom(bytes);
	return neighbor;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Neighbor.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Neighbor> schema = Neighbor.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Neighbor.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Neighbor.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
