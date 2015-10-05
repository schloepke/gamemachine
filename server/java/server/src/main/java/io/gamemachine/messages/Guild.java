
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
public final class Guild implements Externalizable, Message<Guild>, Schema<Guild>, PersistableMessage{



    public static Schema<Guild> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Guild getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Guild DEFAULT_INSTANCE = new Guild();
    static final String defaultScope = Guild.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
							    public String ownerId= null;
		    			    
		
    
        	
							    public int recordId= 0;
		    			    
		
    
        	
							    public String name= null;
		    			    
		
    
        
	public static GuildCache cache() {
		return GuildCache.getInstance();
	}
	
	public static GuildStore store() {
		return GuildStore.getInstance();
	}


    public Guild()
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
		
		public Guild result(int timeout) {
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
	
	public static class GuildCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Guild> cache = new Cache<String, Guild>(120, 5000);
		
		private GuildCache() {
		}
		
		private static class LazyHolder {
			private static final GuildCache INSTANCE = new GuildCache();
		}
	
		public static GuildCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Guild>(expiration, size);
		}
	
		public Cache<String, Guild> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(GuildCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Guild message) {
			CacheUpdate cacheUpdate = new CacheUpdate(GuildCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Guild get(String id, int timeout) {
			Guild message = cache.get(id);
			if (message == null) {
				message = Guild.store().get(id, timeout);
			}
			return message;
		}
			
		public static Guild setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Guild message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Guild) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Guild.store().set(message);
			} else {
				message = Guild.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Guild.class.getField(field));
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
				Guild.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class GuildStore {
	
		private GuildStore() {
		}
		
		private static class LazyHolder {
			private static final GuildStore INSTANCE = new GuildStore();
		}
	
		public static GuildStore getInstance() {
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
		
	    public void set(Guild message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Guild get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Guild message) {
	    	Guild clone = message.clone();
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
			
		public Guild get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Guild");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Guild message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Guild) {
					message = (Guild)result;
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
	

	

	public static GuildDb db() {
		return GuildDb.getInstance();
	}
	
	public interface GuildAsyncDb {
		void save(Guild message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class GuildAsyncDbImpl implements GuildAsyncDb {
	
		public void save(Guild message) {
			Guild.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	Guild.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	Guild.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class GuildDb {
	
		public Errors dbErrors;
		private GuildAsyncDb asyncDb = null;
		
		private GuildDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<GuildAsyncDbImpl>(GuildAsyncDb.class, GuildAsyncDbImpl.class));
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
			private static final GuildDb INSTANCE = new GuildDb();
		}
	
		public static GuildDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(Guild message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(Guild message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(Guild message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Guild.open();
	    	}
	    	
	    	io.gamemachine.orm.models.Guild model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.Guild.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.Guild();
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
	    		io.gamemachine.orm.models.Guild.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.Guild.open();
	    	int deleted = io.gamemachine.orm.models.Guild.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.Guild.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.Guild.open();
	    	int deleted = io.gamemachine.orm.models.Guild.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.Guild.close();
	    	return result;
	    }
	    
	    public Guild find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public Guild find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Guild.open();
	    	}
	    	
	    	io.gamemachine.orm.models.Guild model = io.gamemachine.orm.models.Guild.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Guild.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Guild guild = fromModel(model);
	    			    		return guild;
	    	}
	    }
	    
	    public Guild findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.Guild.open();
	    	io.gamemachine.orm.models.Guild model = io.gamemachine.orm.models.Guild.findFirst(query, params);
	    	io.gamemachine.orm.models.Guild.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Guild guild = fromModel(model);
	    			    		return guild;
	    	}
	    }
	    
	    public List<Guild> findAll() {
	    	io.gamemachine.orm.models.Guild.open();
	    	List<io.gamemachine.orm.models.Guild> models = io.gamemachine.orm.models.Guild.findAll();
	    	List<Guild> messages = new ArrayList<Guild>();
	    	for (io.gamemachine.orm.models.Guild model : models) {
	    		Guild guild = fromModel(model);
	    			    		messages.add(guild);
	    	}
	    	io.gamemachine.orm.models.Guild.close();
	    	return messages;
	    }
	    
	    public List<Guild> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<Guild> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Guild.open();
	    	}
	    	List<io.gamemachine.orm.models.Guild> models = io.gamemachine.orm.models.Guild.where(query, params);
	    	List<Guild> messages = new ArrayList<Guild>();
	    	for (io.gamemachine.orm.models.Guild model : models) {
	    		Guild guild = fromModel(model);
	    			    		messages.add(guild);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Guild.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("guild_id",null);
    	    	    	    	    	    	model.set("guild_owner_id",null);
    	    	    	    	    	    	    	    	    	model.set("guild_name",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("guild_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (ownerId != null) {
    	       	    	model.setString("guild_owner_id",ownerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	//if (name != null) {
    	       	    	model.setString("guild_name",name);
    	        		
    	//}
    	    	    }
    
	public static Guild fromModel(Model model) {
		boolean hasFields = false;
    	Guild message = new Guild();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("guild_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String ownerIdTestField = model.getString("guild_owner_id");
    	if (ownerIdTestField != null) {
    		String ownerIdField = ownerIdTestField;
    		message.setOwnerId(ownerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    	    	String nameTestField = model.getString("guild_name");
    	if (nameTestField != null) {
    		String nameField = nameTestField;
    		message.setName(nameField);
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
	
	public Guild setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getOwnerId() {
		return ownerId;
	}
	
	public Guild setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public Guild setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public String getName() {
		return name;
	}
	
	public Guild setName(String name) {
		this.name = name;
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

    public Schema<Guild> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Guild newMessage()
    {
        return new Guild();
    }

    public Class<Guild> typeClass()
    {
        return Guild.class;
    }

    public String messageName()
    {
        return Guild.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Guild.class.getName();
    }

    public boolean isInitialized(Guild message)
    {
        return true;
    }

    public void mergeFrom(Input input, Guild message) throws IOException
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
                	                	
                            	            	case 4:
            	                	                	message.ownerId = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Guild message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	//if(message.ownerId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.ownerId != null) {
            output.writeString(4, message.ownerId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(5, message.recordId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.name != null) {
            output.writeString(6, message.name, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Guild");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.ownerId != null) {
    		System.out.println("ownerId="+this.ownerId);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	//if(this.name != null) {
    		System.out.println("name="+this.name);
    	//}
    	    	System.out.println("END Guild");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 4: return "ownerId";
        	        	case 5: return "recordId";
        	        	case 6: return "name";
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
    	    	__fieldMap.put("ownerId", 4);
    	    	__fieldMap.put("recordId", 5);
    	    	__fieldMap.put("name", 6);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Guild.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Guild parseFrom(byte[] bytes) {
	Guild message = new Guild();
	ProtobufIOUtil.mergeFrom(bytes, message, Guild.getSchema());
	return message;
}

public static Guild parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Guild message = new Guild();
	JsonIOUtil.mergeFrom(bytes, message, Guild.getSchema(), false);
	return message;
}

public Guild clone() {
	byte[] bytes = this.toByteArray();
	Guild guild = Guild.parseFrom(bytes);
	return guild;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Guild.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Guild> schema = Guild.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Guild.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Guild.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
