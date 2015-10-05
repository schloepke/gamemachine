
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
public final class Waypoint implements Externalizable, Message<Waypoint>, Schema<Waypoint>, PersistableMessage{



    public static Schema<Waypoint> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Waypoint getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Waypoint DEFAULT_INSTANCE = new Waypoint();
    static final String defaultScope = Waypoint.class.getSimpleName();

    	
	    	    public String id= null;
	    		
    
            public List<GmVector3> position;
	    
	public static WaypointCache cache() {
		return WaypointCache.getInstance();
	}
	
	public static WaypointStore store() {
		return WaypointStore.getInstance();
	}


    public Waypoint()
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
		
		public Waypoint result(int timeout) {
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
	
	public static class WaypointCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Waypoint> cache = new Cache<String, Waypoint>(120, 5000);
		
		private WaypointCache() {
		}
		
		private static class LazyHolder {
			private static final WaypointCache INSTANCE = new WaypointCache();
		}
	
		public static WaypointCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Waypoint>(expiration, size);
		}
	
		public Cache<String, Waypoint> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(WaypointCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Waypoint message) {
			CacheUpdate cacheUpdate = new CacheUpdate(WaypointCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Waypoint get(String id, int timeout) {
			Waypoint message = cache.get(id);
			if (message == null) {
				message = Waypoint.store().get(id, timeout);
			}
			return message;
		}
			
		public static Waypoint setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Waypoint message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Waypoint) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Waypoint.store().set(message);
			} else {
				message = Waypoint.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Waypoint.class.getField(field));
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
				Waypoint.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class WaypointStore {
	
		private WaypointStore() {
		}
		
		private static class LazyHolder {
			private static final WaypointStore INSTANCE = new WaypointStore();
		}
	
		public static WaypointStore getInstance() {
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
		
	    public void set(Waypoint message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Waypoint get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Waypoint message) {
	    	Waypoint clone = message.clone();
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
			
		public Waypoint get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Waypoint");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Waypoint message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Waypoint) {
					message = (Waypoint)result;
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
    	    	    	    	    	    	model.set("waypoint_id",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("waypoint_id",id);
    	        		
    	//}
    	    	    	    }
    
	public static Waypoint fromModel(Model model) {
		boolean hasFields = false;
    	Waypoint message = new Waypoint();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("waypoint_id");
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
	
	public Waypoint setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public List<GmVector3> getPositionList() {
		if(this.position == null)
            this.position = new ArrayList<GmVector3>();
		return position;
	}

	public Waypoint setPositionList(List<GmVector3> position) {
		this.position = position;
		return this;
	}

	public GmVector3 getPosition(int index)  {
        return position == null ? null : position.get(index);
    }

    public int getPositionCount()  {
        return position == null ? 0 : position.size();
    }

    public Waypoint addPosition(GmVector3 position)  {
        if(this.position == null)
            this.position = new ArrayList<GmVector3>();
        this.position.add(position);
        return this;
    }
            	    	    	    	
    public Waypoint removePositionByX(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.x == obj.x) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByY(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.y == obj.y) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByZ(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.z == obj.z) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByXi(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.xi == obj.xi) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByYi(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.yi == obj.yi) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByZi(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.zi == obj.zi) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Waypoint removePositionByVertice(GmVector3 position)  {
    	if(this.position == null)
           return this;
            
       	Iterator<GmVector3> itr = this.position.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (position.vertice == obj.vertice) {
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

    public Schema<Waypoint> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Waypoint newMessage()
    {
        return new Waypoint();
    }

    public Class<Waypoint> typeClass()
    {
        return Waypoint.class;
    }

    public String messageName()
    {
        return Waypoint.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Waypoint.class.getName();
    }

    public boolean isInitialized(Waypoint message)
    {
        return true;
    }

    public void mergeFrom(Input input, Waypoint message) throws IOException
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
            	            		if(message.position == null)
                        message.position = new ArrayList<GmVector3>();
                                        message.position.add(input.mergeObject(null, GmVector3.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Waypoint message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	if(message.position != null)
        {
            for(GmVector3 position : message.position)
            {
                if( (GmVector3) position != null) {
                   	    				output.writeObject(2, position, GmVector3.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Waypoint");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.position != null) {
    		System.out.println("position="+this.position);
    	//}
    	    	System.out.println("END Waypoint");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "position";
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
    	    	__fieldMap.put("position", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Waypoint.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Waypoint parseFrom(byte[] bytes) {
	Waypoint message = new Waypoint();
	ProtobufIOUtil.mergeFrom(bytes, message, Waypoint.getSchema());
	return message;
}

public static Waypoint parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Waypoint message = new Waypoint();
	JsonIOUtil.mergeFrom(bytes, message, Waypoint.getSchema(), false);
	return message;
}

public Waypoint clone() {
	byte[] bytes = this.toByteArray();
	Waypoint waypoint = Waypoint.parseFrom(bytes);
	return waypoint;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Waypoint.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Waypoint> schema = Waypoint.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Waypoint.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Waypoint.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
