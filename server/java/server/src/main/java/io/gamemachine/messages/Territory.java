
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

import io.gamemachine.core.GameMachineLoader;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.actor.ActorSystem;
import org.javalite.activejdbc.Errors;


import io.gamemachine.core.ActorUtil;

import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;

import io.gamemachine.core.PersistableMessage;


import io.gamemachine.objectdb.Cache;
import io.gamemachine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class Territory implements Externalizable, Message<Territory>, Schema<Territory>, PersistableMessage{



    public static Schema<Territory> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Territory getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Territory DEFAULT_INSTANCE = new Territory();
    static final String defaultScope = Territory.class.getSimpleName();

    			public String id;
	    
        			public String owner;
	    
        			public Integer recordId;
	    
        			public String keep;
	    
        
	public static TerritoryCache cache() {
		return TerritoryCache.getInstance();
	}
	
	public static TerritoryStore store() {
		return TerritoryStore.getInstance();
	}


    public Territory()
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
		
		public Territory result(int timeout) {
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
	
	public static class TerritoryCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Territory> cache = new Cache<String, Territory>(120, 5000);
		
		private TerritoryCache() {
		}
		
		private static class LazyHolder {
			private static final TerritoryCache INSTANCE = new TerritoryCache();
		}
	
		public static TerritoryCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Territory>(expiration, size);
		}
	
		public Cache<String, Territory> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(TerritoryCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Territory message) {
			CacheUpdate cacheUpdate = new CacheUpdate(TerritoryCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Territory get(String id, int timeout) {
			Territory message = cache.get(id);
			if (message == null) {
				message = Territory.store().get(id, timeout);
			}
			return message;
		}
			
		public static Territory setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Territory message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Territory) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Territory.store().set(message);
			} else {
				message = Territory.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Territory.class.getField(field));
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
				Territory.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class TerritoryStore {
	
		private TerritoryStore() {
		}
		
		private static class LazyHolder {
			private static final TerritoryStore INSTANCE = new TerritoryStore();
		}
	
		public static TerritoryStore getInstance() {
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
		
	    public void set(Territory message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Territory get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Territory message) {
	    	Territory clone = message.clone();
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
			
		public Territory get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Territory");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Territory message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Territory) {
					message = (Territory)result;
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
	

	

	public static TerritoryDb db() {
		return TerritoryDb.getInstance();
	}
	
	public interface TerritoryAsyncDb {
		void save(Territory message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class TerritoryAsyncDbImpl implements TerritoryAsyncDb {
	
		public void save(Territory message) {
			Territory.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	Territory.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	Territory.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class TerritoryDb {
	
		public Errors dbErrors;
		private TerritoryAsyncDb asyncDb = null;
		
		private TerritoryDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<TerritoryAsyncDbImpl>(TerritoryAsyncDb.class, TerritoryAsyncDbImpl.class));
			}
		}
		
		public void stop() {
			if (asyncDb != null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				TypedActor.get(system).stop(asyncDb);
				asyncDb = null;
			}
		}
		
		private static class LazyHolder {
			private static final TerritoryDb INSTANCE = new TerritoryDb();
		}
	
		public static TerritoryDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(Territory message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(Territory message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(Territory message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Territory.open();
	    	}
	    	
	    	io.gamemachine.orm.models.Territory model = null;
	    	if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.Territory.findFirst("id = ?", message.recordId);
	    	}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.Territory();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
	    	}
	    		    	
	    	Boolean res = model.save();
	    	if (res) {
	    		message.setRecordId(model.getInteger("id"));
	    	} else {
	    		dbErrors = model.errors();
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Territory.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.Territory.open();
	    	int deleted = io.gamemachine.orm.models.Territory.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.Territory.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.Territory.open();
	    	int deleted = io.gamemachine.orm.models.Territory.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.Territory.close();
	    	return result;
	    }
	    
	    public Territory find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public Territory find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Territory.open();
	    	}
	    	
	    	io.gamemachine.orm.models.Territory model = io.gamemachine.orm.models.Territory.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Territory.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Territory territory = fromModel(model);
	    			    		return territory;
	    	}
	    }
	    
	    public Territory findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.Territory.open();
	    	io.gamemachine.orm.models.Territory model = io.gamemachine.orm.models.Territory.findFirst(query, params);
	    	io.gamemachine.orm.models.Territory.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Territory territory = fromModel(model);
	    			    		return territory;
	    	}
	    }
	    
	    public List<Territory> findAll() {
	    	io.gamemachine.orm.models.Territory.open();
	    	List<io.gamemachine.orm.models.Territory> models = io.gamemachine.orm.models.Territory.findAll();
	    	List<Territory> messages = new ArrayList<Territory>();
	    	for (io.gamemachine.orm.models.Territory model : models) {
	    		Territory territory = fromModel(model);
	    			    		messages.add(territory);
	    	}
	    	io.gamemachine.orm.models.Territory.close();
	    	return messages;
	    }
	    
	    public List<Territory> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<Territory> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Territory.open();
	    	}
	    	List<io.gamemachine.orm.models.Territory> models = io.gamemachine.orm.models.Territory.where(query, params);
	    	List<Territory> messages = new ArrayList<Territory>();
	    	for (io.gamemachine.orm.models.Territory model : models) {
	    		Territory territory = fromModel(model);
	    			    		messages.add(territory);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Territory.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("territory_id",null);
    	    	    	    	    	    	model.set("territory_owner",null);
    	    	    	    	    	    	    	    	    	model.set("territory_keep",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("territory_id",id);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (owner != null) {
    	       	    	model.setString("territory_owner",owner);
    	        		
    	}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	if (keep != null) {
    	       	    	model.setString("territory_keep",keep);
    	        		
    	}
    	    	    }
    
	public static Territory fromModel(Model model) {
		boolean hasFields = false;
    	Territory message = new Territory();
    	    	    	    	    	
    	    	    	String idField = model.getString("territory_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String ownerField = model.getString("territory_owner");
    	    	
    	if (ownerField != null) {
    		message.setOwner(ownerField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String keepField = model.getString("territory_keep");
    	    	
    	if (keepField != null) {
    		message.setKeep(keepField);
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
	
	public Territory setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasOwner()  {
        return owner == null ? false : true;
    }
        
		public String getOwner() {
		return owner;
	}
	
	public Territory setOwner(String owner) {
		this.owner = owner;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public Territory setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasKeep()  {
        return keep == null ? false : true;
    }
        
		public String getKeep() {
		return keep;
	}
	
	public Territory setKeep(String keep) {
		this.keep = keep;
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

    public Schema<Territory> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Territory newMessage()
    {
        return new Territory();
    }

    public Class<Territory> typeClass()
    {
        return Territory.class;
    }

    public String messageName()
    {
        return Territory.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Territory.class.getName();
    }

    public boolean isInitialized(Territory message)
    {
        return true;
    }

    public void mergeFrom(Input input, Territory message) throws IOException
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
            	                	                	message.owner = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.keep = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Territory message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	if(message.owner == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.owner != null)
            output.writeString(2, message.owner, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(3, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.keep != null)
            output.writeString(4, message.keep, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Territory");
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.owner != null) {
    		System.out.println("owner="+this.owner);
    	}
    	    	if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	}
    	    	if(this.keep != null) {
    		System.out.println("keep="+this.keep);
    	}
    	    	System.out.println("END Territory");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "owner";
        	        	case 3: return "recordId";
        	        	case 4: return "keep";
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
    	    	__fieldMap.put("owner", 2);
    	    	__fieldMap.put("recordId", 3);
    	    	__fieldMap.put("keep", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Territory.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Territory parseFrom(byte[] bytes) {
	Territory message = new Territory();
	ProtobufIOUtil.mergeFrom(bytes, message, Territory.getSchema());
	return message;
}

public static Territory parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Territory message = new Territory();
	JsonIOUtil.mergeFrom(bytes, message, Territory.getSchema(), false);
	return message;
}

public Territory clone() {
	byte[] bytes = this.toByteArray();
	Territory territory = Territory.parseFrom(bytes);
	return territory;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Territory.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Territory> schema = Territory.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Territory.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Territory.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
