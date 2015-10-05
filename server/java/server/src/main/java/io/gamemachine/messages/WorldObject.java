
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
public final class WorldObject implements Externalizable, Message<WorldObject>, Schema<WorldObject>, PersistableMessage{



    public static Schema<WorldObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static WorldObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final WorldObject DEFAULT_INSTANCE = new WorldObject();
    static final String defaultScope = WorldObject.class.getSimpleName();

    	
							    public String playerItemId= null;
		    			    
		
    
        	
							    public int action= 0;
		    			    
		
    
        	
							    public String id= null;
		    			    
		
    
        	
							    public int recordId= 0;
		    			    
		
    
        	
							    public String ownerId= null;
		    			    
		
    
        	
							    public int x= 0;
		    			    
		
    
        	
							    public int y= 0;
		    			    
		
    
        	
							    public int z= 0;
		    			    
		
    
        	
							    public int rx= 0;
		    			    
		
    
        	
							    public int ry= 0;
		    			    
		
    
        	
							    public int rz= 0;
		    			    
		
    
        	
							    public int rw= 0;
		    			    
		
    
        	
							    public int maxHealth= 0;
		    			    
		
    
        	
							    public int health= 0;
		    			    
		
    
        	
							    public String parentId= null;
		    			    
		
    
        	
							    public boolean destructable= false;
		    			    
		
    
        	
							    public String prefab= null;
		    			    
		
    
        	
							    public int type= 0;
		    			    
		
    
        	
							    public String grid= null;
		    			    
		
    
        	
							    public String currentUser= null;
		    			    
		
    
        	
							    public int state= 0;
		    			    
		
    
        
	public static WorldObjectCache cache() {
		return WorldObjectCache.getInstance();
	}
	
	public static WorldObjectStore store() {
		return WorldObjectStore.getInstance();
	}


    public WorldObject()
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
		
		public WorldObject result(int timeout) {
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
	
	public static class WorldObjectCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, WorldObject> cache = new Cache<String, WorldObject>(120, 5000);
		
		private WorldObjectCache() {
		}
		
		private static class LazyHolder {
			private static final WorldObjectCache INSTANCE = new WorldObjectCache();
		}
	
		public static WorldObjectCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, WorldObject>(expiration, size);
		}
	
		public Cache<String, WorldObject> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(WorldObjectCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(WorldObject message) {
			CacheUpdate cacheUpdate = new CacheUpdate(WorldObjectCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public WorldObject get(String id, int timeout) {
			WorldObject message = cache.get(id);
			if (message == null) {
				message = WorldObject.store().get(id, timeout);
			}
			return message;
		}
			
		public static WorldObject setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			WorldObject message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (WorldObject) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				WorldObject.store().set(message);
			} else {
				message = WorldObject.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, WorldObject.class.getField(field));
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
				WorldObject.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class WorldObjectStore {
	
		private WorldObjectStore() {
		}
		
		private static class LazyHolder {
			private static final WorldObjectStore INSTANCE = new WorldObjectStore();
		}
	
		public static WorldObjectStore getInstance() {
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
		
	    public void set(WorldObject message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public WorldObject get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, WorldObject message) {
	    	WorldObject clone = message.clone();
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
			
		public WorldObject get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("WorldObject");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			WorldObject message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof WorldObject) {
					message = (WorldObject)result;
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
	

	

	public static WorldObjectDb db() {
		return WorldObjectDb.getInstance();
	}
	
	public interface WorldObjectAsyncDb {
		void save(WorldObject message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class WorldObjectAsyncDbImpl implements WorldObjectAsyncDb {
	
		public void save(WorldObject message) {
			WorldObject.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	WorldObject.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	WorldObject.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class WorldObjectDb {
	
		public Errors dbErrors;
		private WorldObjectAsyncDb asyncDb = null;
		
		private WorldObjectDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<WorldObjectAsyncDbImpl>(WorldObjectAsyncDb.class, WorldObjectAsyncDbImpl.class));
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
			private static final WorldObjectDb INSTANCE = new WorldObjectDb();
		}
	
		public static WorldObjectDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(WorldObject message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(WorldObject message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(WorldObject message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.WorldObject.open();
	    	}
	    	
	    	io.gamemachine.orm.models.WorldObject model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.WorldObject.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.WorldObject();
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
	    		io.gamemachine.orm.models.WorldObject.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.WorldObject.open();
	    	int deleted = io.gamemachine.orm.models.WorldObject.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.WorldObject.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.WorldObject.open();
	    	int deleted = io.gamemachine.orm.models.WorldObject.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.WorldObject.close();
	    	return result;
	    }
	    
	    public WorldObject find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public WorldObject find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.WorldObject.open();
	    	}
	    	
	    	io.gamemachine.orm.models.WorldObject model = io.gamemachine.orm.models.WorldObject.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.WorldObject.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		WorldObject worldObject = fromModel(model);
	    			    		return worldObject;
	    	}
	    }
	    
	    public WorldObject findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.WorldObject.open();
	    	io.gamemachine.orm.models.WorldObject model = io.gamemachine.orm.models.WorldObject.findFirst(query, params);
	    	io.gamemachine.orm.models.WorldObject.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		WorldObject worldObject = fromModel(model);
	    			    		return worldObject;
	    	}
	    }
	    
	    public List<WorldObject> findAll() {
	    	io.gamemachine.orm.models.WorldObject.open();
	    	List<io.gamemachine.orm.models.WorldObject> models = io.gamemachine.orm.models.WorldObject.findAll();
	    	List<WorldObject> messages = new ArrayList<WorldObject>();
	    	for (io.gamemachine.orm.models.WorldObject model : models) {
	    		WorldObject worldObject = fromModel(model);
	    			    		messages.add(worldObject);
	    	}
	    	io.gamemachine.orm.models.WorldObject.close();
	    	return messages;
	    }
	    
	    public List<WorldObject> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<WorldObject> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.WorldObject.open();
	    	}
	    	List<io.gamemachine.orm.models.WorldObject> models = io.gamemachine.orm.models.WorldObject.where(query, params);
	    	List<WorldObject> messages = new ArrayList<WorldObject>();
	    	for (io.gamemachine.orm.models.WorldObject model : models) {
	    		WorldObject worldObject = fromModel(model);
	    			    		messages.add(worldObject);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.WorldObject.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("world_object_player_item_id",null);
    	    	    	    	    	    	model.set("world_object_action",null);
    	    	    	    	    	    	model.set("world_object_id",null);
    	    	    	    	    	    	    	    	    	model.set("world_object_owner_id",null);
    	    	    	    	    	    	model.set("world_object_x",null);
    	    	    	    	    	    	model.set("world_object_y",null);
    	    	    	    	    	    	model.set("world_object_z",null);
    	    	    	    	    	    	model.set("world_object_rx",null);
    	    	    	    	    	    	model.set("world_object_ry",null);
    	    	    	    	    	    	model.set("world_object_rz",null);
    	    	    	    	    	    	model.set("world_object_rw",null);
    	    	    	    	    	    	model.set("world_object_max_health",null);
    	    	    	    	    	    	model.set("world_object_health",null);
    	    	    	    	    	    	model.set("world_object_parent_id",null);
    	    	    	    	    	    	model.set("world_object_destructable",null);
    	    	    	    	    	    	model.set("world_object_prefab",null);
    	    	    	    	    	    	model.set("world_object_type",null);
    	    	    	    	    	    	model.set("world_object_grid",null);
    	    	    	    	    	    	model.set("world_object_current_user",null);
    	    	    	    	    	    	model.set("world_object_state",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (playerItemId != null) {
    	       	    	model.setString("world_object_player_item_id",playerItemId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (action != null) {
    	       	    	model.setInteger("world_object_action",action);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("world_object_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	//if (ownerId != null) {
    	       	    	model.setString("world_object_owner_id",ownerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (x != null) {
    	       	    	model.setInteger("world_object_x",x);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (y != null) {
    	       	    	model.setInteger("world_object_y",y);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (z != null) {
    	       	    	model.setInteger("world_object_z",z);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rx != null) {
    	       	    	model.setInteger("world_object_rx",rx);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (ry != null) {
    	       	    	model.setInteger("world_object_ry",ry);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rz != null) {
    	       	    	model.setInteger("world_object_rz",rz);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rw != null) {
    	       	    	model.setInteger("world_object_rw",rw);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (maxHealth != null) {
    	       	    	model.setInteger("world_object_max_health",maxHealth);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (health != null) {
    	       	    	model.setInteger("world_object_health",health);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (parentId != null) {
    	       	    	model.setString("world_object_parent_id",parentId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (destructable != null) {
    	       	    	model.setBoolean("world_object_destructable",destructable);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (prefab != null) {
    	       	    	model.setString("world_object_prefab",prefab);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (type != null) {
    	       	    	model.setInteger("world_object_type",type);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (grid != null) {
    	       	    	model.setString("world_object_grid",grid);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (currentUser != null) {
    	       	    	model.setString("world_object_current_user",currentUser);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (state != null) {
    	       	    	model.setInteger("world_object_state",state);
    	        		
    	//}
    	    	    }
    
	public static WorldObject fromModel(Model model) {
		boolean hasFields = false;
    	WorldObject message = new WorldObject();
    	    	    	    	    	
    	    	    	String playerItemIdTestField = model.getString("world_object_player_item_id");
    	if (playerItemIdTestField != null) {
    		String playerItemIdField = playerItemIdTestField;
    		message.setPlayerItemId(playerItemIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer actionTestField = model.getInteger("world_object_action");
    	if (actionTestField != null) {
    		int actionField = actionTestField;
    		message.setAction(actionField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String idTestField = model.getString("world_object_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    	    	String ownerIdTestField = model.getString("world_object_owner_id");
    	if (ownerIdTestField != null) {
    		String ownerIdField = ownerIdTestField;
    		message.setOwnerId(ownerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer xTestField = model.getInteger("world_object_x");
    	if (xTestField != null) {
    		int xField = xTestField;
    		message.setX(xField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer yTestField = model.getInteger("world_object_y");
    	if (yTestField != null) {
    		int yField = yTestField;
    		message.setY(yField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer zTestField = model.getInteger("world_object_z");
    	if (zTestField != null) {
    		int zField = zTestField;
    		message.setZ(zField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer rxTestField = model.getInteger("world_object_rx");
    	if (rxTestField != null) {
    		int rxField = rxTestField;
    		message.setRx(rxField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer ryTestField = model.getInteger("world_object_ry");
    	if (ryTestField != null) {
    		int ryField = ryTestField;
    		message.setRy(ryField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer rzTestField = model.getInteger("world_object_rz");
    	if (rzTestField != null) {
    		int rzField = rzTestField;
    		message.setRz(rzField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer rwTestField = model.getInteger("world_object_rw");
    	if (rwTestField != null) {
    		int rwField = rwTestField;
    		message.setRw(rwField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer maxHealthTestField = model.getInteger("world_object_max_health");
    	if (maxHealthTestField != null) {
    		int maxHealthField = maxHealthTestField;
    		message.setMaxHealth(maxHealthField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer healthTestField = model.getInteger("world_object_health");
    	if (healthTestField != null) {
    		int healthField = healthTestField;
    		message.setHealth(healthField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String parentIdTestField = model.getString("world_object_parent_id");
    	if (parentIdTestField != null) {
    		String parentIdField = parentIdTestField;
    		message.setParentId(parentIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Boolean destructableTestField = model.getBoolean("world_object_destructable");
    	if (destructableTestField != null) {
    		boolean destructableField = destructableTestField;
    		message.setDestructable(destructableField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String prefabTestField = model.getString("world_object_prefab");
    	if (prefabTestField != null) {
    		String prefabField = prefabTestField;
    		message.setPrefab(prefabField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer typeTestField = model.getInteger("world_object_type");
    	if (typeTestField != null) {
    		int typeField = typeTestField;
    		message.setType(typeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String gridTestField = model.getString("world_object_grid");
    	if (gridTestField != null) {
    		String gridField = gridTestField;
    		message.setGrid(gridField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String currentUserTestField = model.getString("world_object_current_user");
    	if (currentUserTestField != null) {
    		String currentUserField = currentUserTestField;
    		message.setCurrentUser(currentUserField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer stateTestField = model.getInteger("world_object_state");
    	if (stateTestField != null) {
    		int stateField = stateTestField;
    		message.setState(stateField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getPlayerItemId() {
		return playerItemId;
	}
	
	public WorldObject setPlayerItemId(String playerItemId) {
		this.playerItemId = playerItemId;
		return this;	}
	
		            
		public int getAction() {
		return action;
	}
	
	public WorldObject setAction(int action) {
		this.action = action;
		return this;	}
	
		            
		public String getId() {
		return id;
	}
	
	public WorldObject setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public WorldObject setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public String getOwnerId() {
		return ownerId;
	}
	
	public WorldObject setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		            
		public int getX() {
		return x;
	}
	
	public WorldObject setX(int x) {
		this.x = x;
		return this;	}
	
		            
		public int getY() {
		return y;
	}
	
	public WorldObject setY(int y) {
		this.y = y;
		return this;	}
	
		            
		public int getZ() {
		return z;
	}
	
	public WorldObject setZ(int z) {
		this.z = z;
		return this;	}
	
		            
		public int getRx() {
		return rx;
	}
	
	public WorldObject setRx(int rx) {
		this.rx = rx;
		return this;	}
	
		            
		public int getRy() {
		return ry;
	}
	
	public WorldObject setRy(int ry) {
		this.ry = ry;
		return this;	}
	
		            
		public int getRz() {
		return rz;
	}
	
	public WorldObject setRz(int rz) {
		this.rz = rz;
		return this;	}
	
		            
		public int getRw() {
		return rw;
	}
	
	public WorldObject setRw(int rw) {
		this.rw = rw;
		return this;	}
	
		            
		public int getMaxHealth() {
		return maxHealth;
	}
	
	public WorldObject setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		return this;	}
	
		            
		public int getHealth() {
		return health;
	}
	
	public WorldObject setHealth(int health) {
		this.health = health;
		return this;	}
	
		            
		public String getParentId() {
		return parentId;
	}
	
	public WorldObject setParentId(String parentId) {
		this.parentId = parentId;
		return this;	}
	
		            
		public boolean getDestructable() {
		return destructable;
	}
	
	public WorldObject setDestructable(boolean destructable) {
		this.destructable = destructable;
		return this;	}
	
		            
		public String getPrefab() {
		return prefab;
	}
	
	public WorldObject setPrefab(String prefab) {
		this.prefab = prefab;
		return this;	}
	
		            
		public int getType() {
		return type;
	}
	
	public WorldObject setType(int type) {
		this.type = type;
		return this;	}
	
		            
		public String getGrid() {
		return grid;
	}
	
	public WorldObject setGrid(String grid) {
		this.grid = grid;
		return this;	}
	
		            
		public String getCurrentUser() {
		return currentUser;
	}
	
	public WorldObject setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
		return this;	}
	
		            
		public int getState() {
		return state;
	}
	
	public WorldObject setState(int state) {
		this.state = state;
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

    public Schema<WorldObject> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public WorldObject newMessage()
    {
        return new WorldObject();
    }

    public Class<WorldObject> typeClass()
    {
        return WorldObject.class;
    }

    public String messageName()
    {
        return WorldObject.class.getSimpleName();
    }

    public String messageFullName()
    {
        return WorldObject.class.getName();
    }

    public boolean isInitialized(WorldObject message)
    {
        return true;
    }

    public void mergeFrom(Input input, WorldObject message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.playerItemId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.action = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.ownerId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.x = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.y = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.z = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.rx = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.ry = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.rz = input.readInt32();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.rw = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.maxHealth = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.parentId = input.readString();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.destructable = input.readBool();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.prefab = input.readString();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.type = input.readInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.grid = input.readString();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.currentUser = input.readString();
                	break;
                	                	
                            	            	case 21:
            	                	                	message.state = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, WorldObject message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.playerItemId != null) {
            output.writeString(1, message.playerItemId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.action != null) {
            output.writeInt32(2, message.action, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(3, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(4, message.recordId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.ownerId != null) {
            output.writeString(5, message.ownerId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.x != null) {
            output.writeInt32(6, message.x, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.y != null) {
            output.writeInt32(7, message.y, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.z != null) {
            output.writeInt32(8, message.z, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rx != null) {
            output.writeInt32(9, message.rx, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.ry != null) {
            output.writeInt32(10, message.ry, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rz != null) {
            output.writeInt32(11, message.rz, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rw != null) {
            output.writeInt32(12, message.rw, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.maxHealth != null) {
            output.writeInt32(13, message.maxHealth, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.health != null) {
            output.writeInt32(14, message.health, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.parentId != null) {
            output.writeString(15, message.parentId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.destructable != null) {
            output.writeBool(16, message.destructable, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.prefab != null) {
            output.writeString(17, message.prefab, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.type != null) {
            output.writeInt32(18, message.type, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.grid != null) {
            output.writeString(19, message.grid, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.currentUser != null) {
            output.writeString(20, message.currentUser, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.state != null) {
            output.writeInt32(21, message.state, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START WorldObject");
    	    	//if(this.playerItemId != null) {
    		System.out.println("playerItemId="+this.playerItemId);
    	//}
    	    	//if(this.action != null) {
    		System.out.println("action="+this.action);
    	//}
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	//if(this.ownerId != null) {
    		System.out.println("ownerId="+this.ownerId);
    	//}
    	    	//if(this.x != null) {
    		System.out.println("x="+this.x);
    	//}
    	    	//if(this.y != null) {
    		System.out.println("y="+this.y);
    	//}
    	    	//if(this.z != null) {
    		System.out.println("z="+this.z);
    	//}
    	    	//if(this.rx != null) {
    		System.out.println("rx="+this.rx);
    	//}
    	    	//if(this.ry != null) {
    		System.out.println("ry="+this.ry);
    	//}
    	    	//if(this.rz != null) {
    		System.out.println("rz="+this.rz);
    	//}
    	    	//if(this.rw != null) {
    		System.out.println("rw="+this.rw);
    	//}
    	    	//if(this.maxHealth != null) {
    		System.out.println("maxHealth="+this.maxHealth);
    	//}
    	    	//if(this.health != null) {
    		System.out.println("health="+this.health);
    	//}
    	    	//if(this.parentId != null) {
    		System.out.println("parentId="+this.parentId);
    	//}
    	    	//if(this.destructable != null) {
    		System.out.println("destructable="+this.destructable);
    	//}
    	    	//if(this.prefab != null) {
    		System.out.println("prefab="+this.prefab);
    	//}
    	    	//if(this.type != null) {
    		System.out.println("type="+this.type);
    	//}
    	    	//if(this.grid != null) {
    		System.out.println("grid="+this.grid);
    	//}
    	    	//if(this.currentUser != null) {
    		System.out.println("currentUser="+this.currentUser);
    	//}
    	    	//if(this.state != null) {
    		System.out.println("state="+this.state);
    	//}
    	    	System.out.println("END WorldObject");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerItemId";
        	        	case 2: return "action";
        	        	case 3: return "id";
        	        	case 4: return "recordId";
        	        	case 5: return "ownerId";
        	        	case 6: return "x";
        	        	case 7: return "y";
        	        	case 8: return "z";
        	        	case 9: return "rx";
        	        	case 10: return "ry";
        	        	case 11: return "rz";
        	        	case 12: return "rw";
        	        	case 13: return "maxHealth";
        	        	case 14: return "health";
        	        	case 15: return "parentId";
        	        	case 16: return "destructable";
        	        	case 17: return "prefab";
        	        	case 18: return "type";
        	        	case 19: return "grid";
        	        	case 20: return "currentUser";
        	        	case 21: return "state";
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
    	    	__fieldMap.put("playerItemId", 1);
    	    	__fieldMap.put("action", 2);
    	    	__fieldMap.put("id", 3);
    	    	__fieldMap.put("recordId", 4);
    	    	__fieldMap.put("ownerId", 5);
    	    	__fieldMap.put("x", 6);
    	    	__fieldMap.put("y", 7);
    	    	__fieldMap.put("z", 8);
    	    	__fieldMap.put("rx", 9);
    	    	__fieldMap.put("ry", 10);
    	    	__fieldMap.put("rz", 11);
    	    	__fieldMap.put("rw", 12);
    	    	__fieldMap.put("maxHealth", 13);
    	    	__fieldMap.put("health", 14);
    	    	__fieldMap.put("parentId", 15);
    	    	__fieldMap.put("destructable", 16);
    	    	__fieldMap.put("prefab", 17);
    	    	__fieldMap.put("type", 18);
    	    	__fieldMap.put("grid", 19);
    	    	__fieldMap.put("currentUser", 20);
    	    	__fieldMap.put("state", 21);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = WorldObject.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static WorldObject parseFrom(byte[] bytes) {
	WorldObject message = new WorldObject();
	ProtobufIOUtil.mergeFrom(bytes, message, WorldObject.getSchema());
	return message;
}

public static WorldObject parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	WorldObject message = new WorldObject();
	JsonIOUtil.mergeFrom(bytes, message, WorldObject.getSchema(), false);
	return message;
}

public WorldObject clone() {
	byte[] bytes = this.toByteArray();
	WorldObject worldObject = WorldObject.parseFrom(bytes);
	return worldObject;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, WorldObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<WorldObject> schema = WorldObject.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, WorldObject.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, WorldObject.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
