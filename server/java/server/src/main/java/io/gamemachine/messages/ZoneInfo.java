
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
public final class ZoneInfo implements Externalizable, Message<ZoneInfo>, Schema<ZoneInfo>, PersistableMessage{



    public static Schema<ZoneInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ZoneInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ZoneInfo DEFAULT_INSTANCE = new ZoneInfo();
    static final String defaultScope = ZoneInfo.class.getSimpleName();

    	
	    	    public String id= null;
	    		
    
        	
	    	    public int recordId= 0;
	    		
    
        	
	    	    public String node= null;
	    		
    
        	
	    	    public String actorName= null;
	    		
    
        	
	    	    public boolean assigned= false;
	    		
    
        	
	    	    public int number= 0;
	    		
    
        	
	    	    public String hostname= null;
	    		
    
        	
	    	    public boolean current= false;
	    		
    
        
	public static ZoneInfoCache cache() {
		return ZoneInfoCache.getInstance();
	}
	
	public static ZoneInfoStore store() {
		return ZoneInfoStore.getInstance();
	}


    public ZoneInfo()
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
		
		public ZoneInfo result(int timeout) {
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
	
	public static class ZoneInfoCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, ZoneInfo> cache = new Cache<String, ZoneInfo>(120, 5000);
		
		private ZoneInfoCache() {
		}
		
		private static class LazyHolder {
			private static final ZoneInfoCache INSTANCE = new ZoneInfoCache();
		}
	
		public static ZoneInfoCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, ZoneInfo>(expiration, size);
		}
	
		public Cache<String, ZoneInfo> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(ZoneInfoCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(ZoneInfo message) {
			CacheUpdate cacheUpdate = new CacheUpdate(ZoneInfoCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public ZoneInfo get(String id, int timeout) {
			ZoneInfo message = cache.get(id);
			if (message == null) {
				message = ZoneInfo.store().get(id, timeout);
			}
			return message;
		}
			
		public static ZoneInfo setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			ZoneInfo message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (ZoneInfo) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				ZoneInfo.store().set(message);
			} else {
				message = ZoneInfo.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, ZoneInfo.class.getField(field));
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
				ZoneInfo.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class ZoneInfoStore {
	
		private ZoneInfoStore() {
		}
		
		private static class LazyHolder {
			private static final ZoneInfoStore INSTANCE = new ZoneInfoStore();
		}
	
		public static ZoneInfoStore getInstance() {
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
		
	    public void set(ZoneInfo message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public ZoneInfo get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, ZoneInfo message) {
	    	ZoneInfo clone = message.clone();
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
			
		public ZoneInfo get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("ZoneInfo");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			ZoneInfo message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof ZoneInfo) {
					message = (ZoneInfo)result;
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
	

	

	public static ZoneInfoDb db() {
		return ZoneInfoDb.getInstance();
	}
	
	public interface ZoneInfoAsyncDb {
		void save(ZoneInfo message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class ZoneInfoAsyncDbImpl implements ZoneInfoAsyncDb {
	
		public void save(ZoneInfo message) {
			ZoneInfo.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	ZoneInfo.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	ZoneInfo.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class ZoneInfoDb {
	
		public Errors dbErrors;
		private ZoneInfoAsyncDb asyncDb = null;
		
		private ZoneInfoDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<ZoneInfoAsyncDbImpl>(ZoneInfoAsyncDb.class, ZoneInfoAsyncDbImpl.class));
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
			private static final ZoneInfoDb INSTANCE = new ZoneInfoDb();
		}
	
		public static ZoneInfoDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(ZoneInfo message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(ZoneInfo message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(ZoneInfo message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.ZoneInfo.open();
	    	}
	    	
	    	io.gamemachine.orm.models.ZoneInfo model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.ZoneInfo.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.ZoneInfo();
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
	    		io.gamemachine.orm.models.ZoneInfo.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.ZoneInfo.open();
	    	int deleted = io.gamemachine.orm.models.ZoneInfo.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.ZoneInfo.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.ZoneInfo.open();
	    	int deleted = io.gamemachine.orm.models.ZoneInfo.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.ZoneInfo.close();
	    	return result;
	    }
	    
	    public ZoneInfo find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public ZoneInfo find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.ZoneInfo.open();
	    	}
	    	
	    	io.gamemachine.orm.models.ZoneInfo model = io.gamemachine.orm.models.ZoneInfo.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.ZoneInfo.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		ZoneInfo zoneInfo = fromModel(model);
	    			    		return zoneInfo;
	    	}
	    }
	    
	    public ZoneInfo findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.ZoneInfo.open();
	    	io.gamemachine.orm.models.ZoneInfo model = io.gamemachine.orm.models.ZoneInfo.findFirst(query, params);
	    	io.gamemachine.orm.models.ZoneInfo.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		ZoneInfo zoneInfo = fromModel(model);
	    			    		return zoneInfo;
	    	}
	    }
	    
	    public List<ZoneInfo> findAll() {
	    	io.gamemachine.orm.models.ZoneInfo.open();
	    	List<io.gamemachine.orm.models.ZoneInfo> models = io.gamemachine.orm.models.ZoneInfo.findAll();
	    	List<ZoneInfo> messages = new ArrayList<ZoneInfo>();
	    	for (io.gamemachine.orm.models.ZoneInfo model : models) {
	    		ZoneInfo zoneInfo = fromModel(model);
	    			    		messages.add(zoneInfo);
	    	}
	    	io.gamemachine.orm.models.ZoneInfo.close();
	    	return messages;
	    }
	    
	    public List<ZoneInfo> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<ZoneInfo> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.ZoneInfo.open();
	    	}
	    	List<io.gamemachine.orm.models.ZoneInfo> models = io.gamemachine.orm.models.ZoneInfo.where(query, params);
	    	List<ZoneInfo> messages = new ArrayList<ZoneInfo>();
	    	for (io.gamemachine.orm.models.ZoneInfo model : models) {
	    		ZoneInfo zoneInfo = fromModel(model);
	    			    		messages.add(zoneInfo);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.ZoneInfo.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("zone_info_id",null);
    	    	    	    	    	    	    	    	    	model.set("zone_info_node",null);
    	    	    	    	    	    	model.set("zone_info_actor_name",null);
    	    	    	    	    	    	model.set("zone_info_assigned",null);
    	    	    	    	    	    	model.set("zone_info_number",null);
    	    	    	    	    	    	model.set("zone_info_hostname",null);
    	    	    	    	    	    	model.set("zone_info_current",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("zone_info_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	//if (node != null) {
    	       	    	model.setString("zone_info_node",node);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (actorName != null) {
    	       	    	model.setString("zone_info_actor_name",actorName);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (assigned != null) {
    	       	    	model.setBoolean("zone_info_assigned",assigned);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (number != null) {
    	       	    	model.setInteger("zone_info_number",number);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (hostname != null) {
    	       	    	model.setString("zone_info_hostname",hostname);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (current != null) {
    	       	    	model.setBoolean("zone_info_current",current);
    	        		
    	//}
    	    	    }
    
	public static ZoneInfo fromModel(Model model) {
		boolean hasFields = false;
    	ZoneInfo message = new ZoneInfo();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("zone_info_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    	    	String nodeTestField = model.getString("zone_info_node");
    	if (nodeTestField != null) {
    		String nodeField = nodeTestField;
    		message.setNode(nodeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String actorNameTestField = model.getString("zone_info_actor_name");
    	if (actorNameTestField != null) {
    		String actorNameField = actorNameTestField;
    		message.setActorName(actorNameField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Boolean assignedTestField = model.getBoolean("zone_info_assigned");
    	if (assignedTestField != null) {
    		boolean assignedField = assignedTestField;
    		message.setAssigned(assignedField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer numberTestField = model.getInteger("zone_info_number");
    	if (numberTestField != null) {
    		int numberField = numberTestField;
    		message.setNumber(numberField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String hostnameTestField = model.getString("zone_info_hostname");
    	if (hostnameTestField != null) {
    		String hostnameField = hostnameTestField;
    		message.setHostname(hostnameField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Boolean currentTestField = model.getBoolean("zone_info_current");
    	if (currentTestField != null) {
    		boolean currentField = currentTestField;
    		message.setCurrent(currentField);
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
	
	public ZoneInfo setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public ZoneInfo setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public String getNode() {
		return node;
	}
	
	public ZoneInfo setNode(String node) {
		this.node = node;
		return this;	}
	
		            
		public String getActorName() {
		return actorName;
	}
	
	public ZoneInfo setActorName(String actorName) {
		this.actorName = actorName;
		return this;	}
	
		            
		public boolean getAssigned() {
		return assigned;
	}
	
	public ZoneInfo setAssigned(boolean assigned) {
		this.assigned = assigned;
		return this;	}
	
		            
		public int getNumber() {
		return number;
	}
	
	public ZoneInfo setNumber(int number) {
		this.number = number;
		return this;	}
	
		            
		public String getHostname() {
		return hostname;
	}
	
	public ZoneInfo setHostname(String hostname) {
		this.hostname = hostname;
		return this;	}
	
		            
		public boolean getCurrent() {
		return current;
	}
	
	public ZoneInfo setCurrent(boolean current) {
		this.current = current;
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

    public Schema<ZoneInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ZoneInfo newMessage()
    {
        return new ZoneInfo();
    }

    public Class<ZoneInfo> typeClass()
    {
        return ZoneInfo.class;
    }

    public String messageName()
    {
        return ZoneInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ZoneInfo.class.getName();
    }

    public boolean isInitialized(ZoneInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ZoneInfo message) throws IOException
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
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.node = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.actorName = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.assigned = input.readBool();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.number = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.hostname = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.current = input.readBool();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ZoneInfo message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(2, message.recordId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.node != null) {
            output.writeString(3, message.node, false);
        }
    	    	
    	            	
    	    	//if(message.actorName == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.actorName != null) {
            output.writeString(4, message.actorName, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.assigned != null) {
            output.writeBool(5, message.assigned, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.number != null) {
            output.writeInt32(6, message.number, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.hostname != null) {
            output.writeString(7, message.hostname, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.current != null) {
            output.writeBool(8, message.current, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ZoneInfo");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	//if(this.node != null) {
    		System.out.println("node="+this.node);
    	//}
    	    	//if(this.actorName != null) {
    		System.out.println("actorName="+this.actorName);
    	//}
    	    	//if(this.assigned != null) {
    		System.out.println("assigned="+this.assigned);
    	//}
    	    	//if(this.number != null) {
    		System.out.println("number="+this.number);
    	//}
    	    	//if(this.hostname != null) {
    		System.out.println("hostname="+this.hostname);
    	//}
    	    	//if(this.current != null) {
    		System.out.println("current="+this.current);
    	//}
    	    	System.out.println("END ZoneInfo");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "recordId";
        	        	case 3: return "node";
        	        	case 4: return "actorName";
        	        	case 5: return "assigned";
        	        	case 6: return "number";
        	        	case 7: return "hostname";
        	        	case 8: return "current";
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
    	    	__fieldMap.put("recordId", 2);
    	    	__fieldMap.put("node", 3);
    	    	__fieldMap.put("actorName", 4);
    	    	__fieldMap.put("assigned", 5);
    	    	__fieldMap.put("number", 6);
    	    	__fieldMap.put("hostname", 7);
    	    	__fieldMap.put("current", 8);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ZoneInfo.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ZoneInfo parseFrom(byte[] bytes) {
	ZoneInfo message = new ZoneInfo();
	ProtobufIOUtil.mergeFrom(bytes, message, ZoneInfo.getSchema());
	return message;
}

public static ZoneInfo parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ZoneInfo message = new ZoneInfo();
	JsonIOUtil.mergeFrom(bytes, message, ZoneInfo.getSchema(), false);
	return message;
}

public ZoneInfo clone() {
	byte[] bytes = this.toByteArray();
	ZoneInfo zoneInfo = ZoneInfo.parseFrom(bytes);
	return zoneInfo;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ZoneInfo.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ZoneInfo> schema = ZoneInfo.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ZoneInfo.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ZoneInfo.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
