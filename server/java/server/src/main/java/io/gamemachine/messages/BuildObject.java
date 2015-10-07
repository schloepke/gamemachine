
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

import io.gamemachine.core.GameMachineLoader;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.actor.ActorSystem;
import org.javalite.activejdbc.Errors;


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
public final class BuildObject implements Externalizable, Message<BuildObject>, Schema<BuildObject>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(BuildObject.class);



    public static Schema<BuildObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildObject DEFAULT_INSTANCE = new BuildObject();
    static final String defaultScope = BuildObject.class.getSimpleName();

    	
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
		    			    
		
    
        	
							    public int health= 0;
		    			    
		
    
        	
							    public int templateId= 0;
		    			    
		
    
        	
							    public String grid= null;
		    			    
		
    
        	
							    public long updatedAt= 0L;
		    			    
		
    
        	
							    public int state= 0;
		    			    
		
    
        	
							    public int updateId= 0;
		    			    
		
    
        	
							    public boolean isFloor= false;
		    			    
		
    
        	
							    public boolean isDestructable= false;
		    			    
		
    
        	
							    public boolean hasDoor= false;
		    			    
		
    
        	
							    public int doorStatus= 0;
		    			    
		
    
        	
							    public ByteString groundBlockObject;
		    			    
		
    
        	
							    public boolean isGroundBlock= false;
		    			    
		
    
        	
							    public int chunk= 0;
		    			    
		
    
        	
							    public ByteString terrainEdit;
		    			    
		
    
        	
							    public boolean isTerrainEdit= false;
		    			    
		
    
        	
							    public ByteString customBytes;
		    			    
		
    
        	
							    public String customString= null;
		    			    
		
    
        	
							    public String textureId= null;
		    			    
		
    
        	
							    public ByteString slots;
		    			    
		
    
        	
							    public long placedAt= 0L;
		    			    
		
    
        	
					public SlotInfo slotInfo = null;
			    
		
    
        	
							    public int zone= 0;
		    			    
		
    
        
	public static BuildObjectCache cache() {
		return BuildObjectCache.getInstance();
	}
	
	public static BuildObjectStore store() {
		return BuildObjectStore.getInstance();
	}


    public BuildObject()
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
		
		public BuildObject result(int timeout) {
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
	
	public static class BuildObjectCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, BuildObject> cache = new Cache<String, BuildObject>(120, 5000);
		
		private BuildObjectCache() {
		}
		
		private static class LazyHolder {
			private static final BuildObjectCache INSTANCE = new BuildObjectCache();
		}
	
		public static BuildObjectCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, BuildObject>(expiration, size);
		}
	
		public Cache<String, BuildObject> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(BuildObjectCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(BuildObject message) {
			CacheUpdate cacheUpdate = new CacheUpdate(BuildObjectCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public BuildObject get(String id, int timeout) {
			BuildObject message = cache.get(id);
			if (message == null) {
				message = BuildObject.store().get(id, timeout);
			}
			return message;
		}
			
		public static BuildObject setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			BuildObject message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (BuildObject) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				BuildObject.store().set(message);
			} else {
				message = BuildObject.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, BuildObject.class.getField(field));
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
				BuildObject.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class BuildObjectStore {
	
		private BuildObjectStore() {
		}
		
		private static class LazyHolder {
			private static final BuildObjectStore INSTANCE = new BuildObjectStore();
		}
	
		public static BuildObjectStore getInstance() {
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
		
	    public void set(BuildObject message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public BuildObject get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, BuildObject message) {
	    	BuildObject clone = message.clone();
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
			
		public BuildObject get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("BuildObject");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			BuildObject message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof BuildObject) {
					message = (BuildObject)result;
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
	

	

	public static BuildObjectDb db() {
		return BuildObjectDb.getInstance();
	}
	
	public interface BuildObjectAsyncDb {
		void save(BuildObject message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class BuildObjectAsyncDbImpl implements BuildObjectAsyncDb {
	
		public void save(BuildObject message) {
			BuildObject.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	BuildObject.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	BuildObject.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class BuildObjectDb {
	
		public Errors dbErrors;
		private BuildObjectAsyncDb asyncDb = null;
		
		private BuildObjectDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<BuildObjectAsyncDbImpl>(BuildObjectAsyncDb.class, BuildObjectAsyncDbImpl.class));
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
			private static final BuildObjectDb INSTANCE = new BuildObjectDb();
		}
	
		public static BuildObjectDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(BuildObject message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(BuildObject message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(BuildObject message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildObject.open();
	    	}
	    	
	    	io.gamemachine.orm.models.BuildObject model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.BuildObject.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.BuildObject();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
	    	}
	    		    	if (message.slotInfo != null) {
	    		message.slotInfo.toModel(model);
	    	} else {
	    		SlotInfo.clearModel(model);
	    	}
	    		    	
	    	Boolean res = model.save();
	    	if (res) {
	    		message.setRecordId(model.getInteger("id"));
	    	} else {
	    		if (model.hasErrors()) {
	    			logger.warn("Save has errors");
	    			dbErrors = model.errors();
		    		Map<String, String> errors = dbErrors;
		    		for (String key : errors.keySet()) {
		    			logger.warn(key+": "+errors.get(key));
		    		}
	    		} else {
	    			logger.warn("Save failed unknown reason");
	    		}
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildObject.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.BuildObject.open();
	    	int deleted = io.gamemachine.orm.models.BuildObject.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.BuildObject.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.BuildObject.open();
	    	int deleted = io.gamemachine.orm.models.BuildObject.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.BuildObject.close();
	    	return result;
	    }
	    
	    public BuildObject find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public BuildObject find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildObject.open();
	    	}
	    	
	    	io.gamemachine.orm.models.BuildObject model = io.gamemachine.orm.models.BuildObject.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildObject.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		BuildObject buildObject = fromModel(model);
	    			    		buildObject.slotInfo = SlotInfo.fromModel(model);
	    			    		return buildObject;
	    	}
	    }
	    
	    public BuildObject findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.BuildObject.open();
	    	io.gamemachine.orm.models.BuildObject model = io.gamemachine.orm.models.BuildObject.findFirst(query, params);
	    	io.gamemachine.orm.models.BuildObject.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		BuildObject buildObject = fromModel(model);
	    			    		buildObject.slotInfo = SlotInfo.fromModel(model);
	    			    		return buildObject;
	    	}
	    }
	    
	    public List<BuildObject> findAll() {
	    	io.gamemachine.orm.models.BuildObject.open();
	    	List<io.gamemachine.orm.models.BuildObject> models = io.gamemachine.orm.models.BuildObject.findAll();
	    	List<BuildObject> messages = new ArrayList<BuildObject>();
	    	for (io.gamemachine.orm.models.BuildObject model : models) {
	    		BuildObject buildObject = fromModel(model);
	    			    		buildObject.slotInfo = SlotInfo.fromModel(model);
	    			    		messages.add(buildObject);
	    	}
	    	io.gamemachine.orm.models.BuildObject.close();
	    	return messages;
	    }
	    
	    public List<BuildObject> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<BuildObject> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildObject.open();
	    	}
	    	List<io.gamemachine.orm.models.BuildObject> models = io.gamemachine.orm.models.BuildObject.where(query, params);
	    	List<BuildObject> messages = new ArrayList<BuildObject>();
	    	for (io.gamemachine.orm.models.BuildObject model : models) {
	    		BuildObject buildObject = fromModel(model);
	    			    		buildObject.slotInfo = SlotInfo.fromModel(model);
	    			    		messages.add(buildObject);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildObject.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("build_object_player_item_id",null);
    	    	    	    	    	    	model.set("build_object_action",null);
    	    	    	    	    	    	model.set("build_object_id",null);
    	    	    	    	    	    	    	    	    	model.set("build_object_owner_id",null);
    	    	    	    	    	    	model.set("build_object_x",null);
    	    	    	    	    	    	model.set("build_object_y",null);
    	    	    	    	    	    	model.set("build_object_z",null);
    	    	    	    	    	    	model.set("build_object_rx",null);
    	    	    	    	    	    	model.set("build_object_ry",null);
    	    	    	    	    	    	model.set("build_object_rz",null);
    	    	    	    	    	    	model.set("build_object_rw",null);
    	    	    	    	    	    	model.set("build_object_health",null);
    	    	    	    	    	    	model.set("build_object_template_id",null);
    	    	    	    	    	    	model.set("build_object_grid",null);
    	    	    	    	    	    	model.set("build_object_updated_at",null);
    	    	    	    	    	    	model.set("build_object_state",null);
    	    	    	    	    	    	model.set("build_object_update_id",null);
    	    	    	    	    	    	model.set("build_object_is_floor",null);
    	    	    	    	    	    	model.set("build_object_is_destructable",null);
    	    	    	    	    	    	model.set("build_object_has_door",null);
    	    	    	    	    	    	model.set("build_object_door_status",null);
    	    	    	    	    	    	model.set("build_object_ground_block_object",null);
    	    	    	    	    	    	model.set("build_object_is_ground_block",null);
    	    	    	    	    	    	model.set("build_object_chunk",null);
    	    	    	    	    	    	model.set("build_object_terrain_edit",null);
    	    	    	    	    	    	model.set("build_object_is_terrain_edit",null);
    	    	    	    	    	    	model.set("build_object_custom_bytes",null);
    	    	    	    	    	    	model.set("build_object_custom_string",null);
    	    	    	    	    	    	model.set("build_object_texture_id",null);
    	    	    	    	    	    	model.set("build_object_slots",null);
    	    	    	    	    	    	model.set("build_object_placed_at",null);
    	    	    	    	    	    	    	model.set("build_object_zone",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (playerItemId != null) {
    	       	    	model.setString("build_object_player_item_id",playerItemId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (action != null) {
    	       	    	model.setInteger("build_object_action",action);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("build_object_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//model.setInteger("id",recordId);
    	
    	    	    	    	    	
    	    	    	//if (ownerId != null) {
    	       	    	model.setString("build_object_owner_id",ownerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (x != null) {
    	       	    	model.setInteger("build_object_x",x);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (y != null) {
    	       	    	model.setInteger("build_object_y",y);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (z != null) {
    	       	    	model.setInteger("build_object_z",z);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rx != null) {
    	       	    	model.setInteger("build_object_rx",rx);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (ry != null) {
    	       	    	model.setInteger("build_object_ry",ry);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rz != null) {
    	       	    	model.setInteger("build_object_rz",rz);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rw != null) {
    	       	    	model.setInteger("build_object_rw",rw);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (health != null) {
    	       	    	model.setInteger("build_object_health",health);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (templateId != null) {
    	       	    	model.setInteger("build_object_template_id",templateId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (grid != null) {
    	       	    	model.setString("build_object_grid",grid);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (updatedAt != null) {
    	       	    	model.setLong("build_object_updated_at",updatedAt);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (state != null) {
    	       	    	model.setInteger("build_object_state",state);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (updateId != null) {
    	       	    	model.setInteger("build_object_update_id",updateId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (isFloor != null) {
    	       	    	model.setBoolean("build_object_is_floor",isFloor);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (isDestructable != null) {
    	       	    	model.setBoolean("build_object_is_destructable",isDestructable);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (hasDoor != null) {
    	       	    	model.setBoolean("build_object_has_door",hasDoor);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (doorStatus != null) {
    	       	    	model.setInteger("build_object_door_status",doorStatus);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (groundBlockObject != null) {
    	       	        if (groundBlockObject != null) {
    	    		model.set("build_object_ground_block_object",groundBlockObject.toByteArray());
    	    	}
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (isGroundBlock != null) {
    	       	    	model.setBoolean("build_object_is_ground_block",isGroundBlock);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (chunk != null) {
    	       	    	model.setInteger("build_object_chunk",chunk);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (terrainEdit != null) {
    	       	        if (terrainEdit != null) {
    	    		model.set("build_object_terrain_edit",terrainEdit.toByteArray());
    	    	}
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (isTerrainEdit != null) {
    	       	    	model.setBoolean("build_object_is_terrain_edit",isTerrainEdit);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (customBytes != null) {
    	       	        if (customBytes != null) {
    	    		model.set("build_object_custom_bytes",customBytes.toByteArray());
    	    	}
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (customString != null) {
    	       	    	model.setString("build_object_custom_string",customString);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (textureId != null) {
    	       	    	model.setString("build_object_texture_id",textureId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (slots != null) {
    	       	        if (slots != null) {
    	    		model.set("build_object_slots",slots.toByteArray());
    	    	}
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (placedAt != null) {
    	       	    	model.setLong("build_object_placed_at",placedAt);
    	        		
    	//}
    	    	    	    	    	    	
    	    	    	//if (zone != null) {
    	       	    	model.setInteger("build_object_zone",zone);
    	        		
    	//}
    	    	    }
    
	public static BuildObject fromModel(Model model) {
		boolean hasFields = false;
    	BuildObject message = new BuildObject();
    	    	    	    	    	
    	    			String playerItemIdTestField = model.getString("build_object_player_item_id");
		if (playerItemIdTestField != null) {
			String playerItemIdField = playerItemIdTestField;
			message.setPlayerItemId(playerItemIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer actionTestField = model.getInteger("build_object_action");
		if (actionTestField != null) {
			int actionField = actionTestField;
			message.setAction(actionField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String idTestField = model.getString("build_object_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    			String ownerIdTestField = model.getString("build_object_owner_id");
		if (ownerIdTestField != null) {
			String ownerIdField = ownerIdTestField;
			message.setOwnerId(ownerIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer xTestField = model.getInteger("build_object_x");
		if (xTestField != null) {
			int xField = xTestField;
			message.setX(xField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer yTestField = model.getInteger("build_object_y");
		if (yTestField != null) {
			int yField = yTestField;
			message.setY(yField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer zTestField = model.getInteger("build_object_z");
		if (zTestField != null) {
			int zField = zTestField;
			message.setZ(zField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer rxTestField = model.getInteger("build_object_rx");
		if (rxTestField != null) {
			int rxField = rxTestField;
			message.setRx(rxField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer ryTestField = model.getInteger("build_object_ry");
		if (ryTestField != null) {
			int ryField = ryTestField;
			message.setRy(ryField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer rzTestField = model.getInteger("build_object_rz");
		if (rzTestField != null) {
			int rzField = rzTestField;
			message.setRz(rzField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer rwTestField = model.getInteger("build_object_rw");
		if (rwTestField != null) {
			int rwField = rwTestField;
			message.setRw(rwField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer healthTestField = model.getInteger("build_object_health");
		if (healthTestField != null) {
			int healthField = healthTestField;
			message.setHealth(healthField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer templateIdTestField = model.getInteger("build_object_template_id");
		if (templateIdTestField != null) {
			int templateIdField = templateIdTestField;
			message.setTemplateId(templateIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String gridTestField = model.getString("build_object_grid");
		if (gridTestField != null) {
			String gridField = gridTestField;
			message.setGrid(gridField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Long updatedAtTestField = model.getLong("build_object_updated_at");
		if (updatedAtTestField != null) {
			long updatedAtField = updatedAtTestField;
			message.setUpdatedAt(updatedAtField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer stateTestField = model.getInteger("build_object_state");
		if (stateTestField != null) {
			int stateField = stateTestField;
			message.setState(stateField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer updateIdTestField = model.getInteger("build_object_update_id");
		if (updateIdTestField != null) {
			int updateIdField = updateIdTestField;
			message.setUpdateId(updateIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Boolean isFloorTestField = model.getBoolean("build_object_is_floor");
		if (isFloorTestField != null) {
			boolean isFloorField = isFloorTestField;
			message.setIsFloor(isFloorField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Boolean isDestructableTestField = model.getBoolean("build_object_is_destructable");
		if (isDestructableTestField != null) {
			boolean isDestructableField = isDestructableTestField;
			message.setIsDestructable(isDestructableField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Boolean hasDoorTestField = model.getBoolean("build_object_has_door");
		if (hasDoorTestField != null) {
			boolean hasDoorField = hasDoorTestField;
			message.setHasDoor(hasDoorField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer doorStatusTestField = model.getInteger("build_object_door_status");
		if (doorStatusTestField != null) {
			int doorStatusField = doorStatusTestField;
			message.setDoorStatus(doorStatusField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    	    	
    	ByteString groundBlockObjectField = null;
		Object groundBlockObjectValue = model.get("build_object_ground_block_object");
		if (groundBlockObjectValue != null) {
			byte[] groundBlockObjectBytes = Convert.toBytes(groundBlockObjectValue);
			groundBlockObjectField = ByteString.copyFrom(groundBlockObjectBytes);
		}
    	    	
    	    	
    	    	    	    	    	    	
    	    			Boolean isGroundBlockTestField = model.getBoolean("build_object_is_ground_block");
		if (isGroundBlockTestField != null) {
			boolean isGroundBlockField = isGroundBlockTestField;
			message.setIsGroundBlock(isGroundBlockField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer chunkTestField = model.getInteger("build_object_chunk");
		if (chunkTestField != null) {
			int chunkField = chunkTestField;
			message.setChunk(chunkField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    	    	
    	ByteString terrainEditField = null;
		Object terrainEditValue = model.get("build_object_terrain_edit");
		if (terrainEditValue != null) {
			byte[] terrainEditBytes = Convert.toBytes(terrainEditValue);
			terrainEditField = ByteString.copyFrom(terrainEditBytes);
		}
    	    	
    	    	
    	    	    	    	    	    	
    	    			Boolean isTerrainEditTestField = model.getBoolean("build_object_is_terrain_edit");
		if (isTerrainEditTestField != null) {
			boolean isTerrainEditField = isTerrainEditTestField;
			message.setIsTerrainEdit(isTerrainEditField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    	    	
    	ByteString customBytesField = null;
		Object customBytesValue = model.get("build_object_custom_bytes");
		if (customBytesValue != null) {
			byte[] customBytesBytes = Convert.toBytes(customBytesValue);
			customBytesField = ByteString.copyFrom(customBytesBytes);
		}
    	    	
    	    	
    	    	    	    	    	    	
    	    			String customStringTestField = model.getString("build_object_custom_string");
		if (customStringTestField != null) {
			String customStringField = customStringTestField;
			message.setCustomString(customStringField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String textureIdTestField = model.getString("build_object_texture_id");
		if (textureIdTestField != null) {
			String textureIdField = textureIdTestField;
			message.setTextureId(textureIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    	    	
    	ByteString slotsField = null;
		Object slotsValue = model.get("build_object_slots");
		if (slotsValue != null) {
			byte[] slotsBytes = Convert.toBytes(slotsValue);
			slotsField = ByteString.copyFrom(slotsBytes);
		}
    	    	
    	    	
    	    	    	    	    	    	
    	    			Long placedAtTestField = model.getLong("build_object_placed_at");
		if (placedAtTestField != null) {
			long placedAtField = placedAtTestField;
			message.setPlacedAt(placedAtField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	
    	    			Integer zoneTestField = model.getInteger("build_object_zone");
		if (zoneTestField != null) {
			int zoneField = zoneTestField;
			message.setZone(zoneField);
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
	
	public BuildObject setPlayerItemId(String playerItemId) {
		this.playerItemId = playerItemId;
		return this;	}
	
		            
		public int getAction() {
		return action;
	}
	
	public BuildObject setAction(int action) {
		this.action = action;
		return this;	}
	
		            
		public String getId() {
		return id;
	}
	
	public BuildObject setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public BuildObject setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public String getOwnerId() {
		return ownerId;
	}
	
	public BuildObject setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		            
		public int getX() {
		return x;
	}
	
	public BuildObject setX(int x) {
		this.x = x;
		return this;	}
	
		            
		public int getY() {
		return y;
	}
	
	public BuildObject setY(int y) {
		this.y = y;
		return this;	}
	
		            
		public int getZ() {
		return z;
	}
	
	public BuildObject setZ(int z) {
		this.z = z;
		return this;	}
	
		            
		public int getRx() {
		return rx;
	}
	
	public BuildObject setRx(int rx) {
		this.rx = rx;
		return this;	}
	
		            
		public int getRy() {
		return ry;
	}
	
	public BuildObject setRy(int ry) {
		this.ry = ry;
		return this;	}
	
		            
		public int getRz() {
		return rz;
	}
	
	public BuildObject setRz(int rz) {
		this.rz = rz;
		return this;	}
	
		            
		public int getRw() {
		return rw;
	}
	
	public BuildObject setRw(int rw) {
		this.rw = rw;
		return this;	}
	
		            
		public int getHealth() {
		return health;
	}
	
	public BuildObject setHealth(int health) {
		this.health = health;
		return this;	}
	
		            
		public int getTemplateId() {
		return templateId;
	}
	
	public BuildObject setTemplateId(int templateId) {
		this.templateId = templateId;
		return this;	}
	
		            
		public String getGrid() {
		return grid;
	}
	
	public BuildObject setGrid(String grid) {
		this.grid = grid;
		return this;	}
	
		            
		public long getUpdatedAt() {
		return updatedAt;
	}
	
	public BuildObject setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
		return this;	}
	
		            
		public int getState() {
		return state;
	}
	
	public BuildObject setState(int state) {
		this.state = state;
		return this;	}
	
		            
		public int getUpdateId() {
		return updateId;
	}
	
	public BuildObject setUpdateId(int updateId) {
		this.updateId = updateId;
		return this;	}
	
		            
		public boolean getIsFloor() {
		return isFloor;
	}
	
	public BuildObject setIsFloor(boolean isFloor) {
		this.isFloor = isFloor;
		return this;	}
	
		            
		public boolean getIsDestructable() {
		return isDestructable;
	}
	
	public BuildObject setIsDestructable(boolean isDestructable) {
		this.isDestructable = isDestructable;
		return this;	}
	
		            
		public boolean getHasDoor() {
		return hasDoor;
	}
	
	public BuildObject setHasDoor(boolean hasDoor) {
		this.hasDoor = hasDoor;
		return this;	}
	
		            
		public int getDoorStatus() {
		return doorStatus;
	}
	
	public BuildObject setDoorStatus(int doorStatus) {
		this.doorStatus = doorStatus;
		return this;	}
	
		            
		public ByteString getGroundBlockObject() {
		return groundBlockObject;
	}
	
	public BuildObject setGroundBlockObject(ByteString groundBlockObject) {
		this.groundBlockObject = groundBlockObject;
		return this;	}
	
		            
		public boolean getIsGroundBlock() {
		return isGroundBlock;
	}
	
	public BuildObject setIsGroundBlock(boolean isGroundBlock) {
		this.isGroundBlock = isGroundBlock;
		return this;	}
	
		            
		public int getChunk() {
		return chunk;
	}
	
	public BuildObject setChunk(int chunk) {
		this.chunk = chunk;
		return this;	}
	
		            
		public ByteString getTerrainEdit() {
		return terrainEdit;
	}
	
	public BuildObject setTerrainEdit(ByteString terrainEdit) {
		this.terrainEdit = terrainEdit;
		return this;	}
	
		            
		public boolean getIsTerrainEdit() {
		return isTerrainEdit;
	}
	
	public BuildObject setIsTerrainEdit(boolean isTerrainEdit) {
		this.isTerrainEdit = isTerrainEdit;
		return this;	}
	
		            
		public ByteString getCustomBytes() {
		return customBytes;
	}
	
	public BuildObject setCustomBytes(ByteString customBytes) {
		this.customBytes = customBytes;
		return this;	}
	
		            
		public String getCustomString() {
		return customString;
	}
	
	public BuildObject setCustomString(String customString) {
		this.customString = customString;
		return this;	}
	
		            
		public String getTextureId() {
		return textureId;
	}
	
	public BuildObject setTextureId(String textureId) {
		this.textureId = textureId;
		return this;	}
	
		            
		public ByteString getSlots() {
		return slots;
	}
	
	public BuildObject setSlots(ByteString slots) {
		this.slots = slots;
		return this;	}
	
		            
		public long getPlacedAt() {
		return placedAt;
	}
	
	public BuildObject setPlacedAt(long placedAt) {
		this.placedAt = placedAt;
		return this;	}
	
		            
		public SlotInfo getSlotInfo() {
		return slotInfo;
	}
	
	public BuildObject setSlotInfo(SlotInfo slotInfo) {
		this.slotInfo = slotInfo;
		return this;	}
	
		            
		public int getZone() {
		return zone;
	}
	
	public BuildObject setZone(int zone) {
		this.zone = zone;
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

    public Schema<BuildObject> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildObject newMessage()
    {
        return new BuildObject();
    }

    public Class<BuildObject> typeClass()
    {
        return BuildObject.class;
    }

    public String messageName()
    {
        return BuildObject.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildObject.class.getName();
    }

    public boolean isInitialized(BuildObject message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildObject message) throws IOException
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
                	                	
                            	            	case 14:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.templateId = input.readInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.grid = input.readString();
                	break;
                	                	
                            	            	case 22:
            	                	                	message.updatedAt = input.readInt64();
                	break;
                	                	
                            	            	case 23:
            	                	                	message.state = input.readInt32();
                	break;
                	                	
                            	            	case 24:
            	                	                	message.updateId = input.readInt32();
                	break;
                	                	
                            	            	case 25:
            	                	                	message.isFloor = input.readBool();
                	break;
                	                	
                            	            	case 26:
            	                	                	message.isDestructable = input.readBool();
                	break;
                	                	
                            	            	case 27:
            	                	                	message.hasDoor = input.readBool();
                	break;
                	                	
                            	            	case 28:
            	                	                	message.doorStatus = input.readInt32();
                	break;
                	                	
                            	            	case 29:
            	                	                	message.groundBlockObject = input.readBytes();
                	break;
                	                	
                            	            	case 30:
            	                	                	message.isGroundBlock = input.readBool();
                	break;
                	                	
                            	            	case 31:
            	                	                	message.chunk = input.readInt32();
                	break;
                	                	
                            	            	case 32:
            	                	                	message.terrainEdit = input.readBytes();
                	break;
                	                	
                            	            	case 33:
            	                	                	message.isTerrainEdit = input.readBool();
                	break;
                	                	
                            	            	case 34:
            	                	                	message.customBytes = input.readBytes();
                	break;
                	                	
                            	            	case 35:
            	                	                	message.customString = input.readString();
                	break;
                	                	
                            	            	case 36:
            	                	                	message.textureId = input.readString();
                	break;
                	                	
                            	            	case 37:
            	                	                	message.slots = input.readBytes();
                	break;
                	                	
                            	            	case 38:
            	                	                	message.placedAt = input.readInt64();
                	break;
                	                	
                            	            	case 40:
            	                	                	message.slotInfo = input.mergeObject(message.slotInfo, SlotInfo.getSchema());
                    break;
                                    	
                            	            	case 41:
            	                	                	message.zone = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildObject message) throws IOException
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
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.health != null) {
            output.writeInt32(14, message.health, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.templateId != null) {
            output.writeInt32(18, message.templateId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.grid != null) {
            output.writeString(19, message.grid, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.updatedAt != null) {
            output.writeInt64(22, message.updatedAt, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.state != null) {
            output.writeInt32(23, message.state, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.updateId != null) {
            output.writeInt32(24, message.updateId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.isFloor != null) {
            output.writeBool(25, message.isFloor, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.isDestructable != null) {
            output.writeBool(26, message.isDestructable, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.hasDoor != null) {
            output.writeBool(27, message.hasDoor, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.doorStatus != null) {
            output.writeInt32(28, message.doorStatus, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (ByteString)message.groundBlockObject != null) {
            output.writeBytes(29, message.groundBlockObject, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.isGroundBlock != null) {
            output.writeBool(30, message.isGroundBlock, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.chunk != null) {
            output.writeInt32(31, message.chunk, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (ByteString)message.terrainEdit != null) {
            output.writeBytes(32, message.terrainEdit, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.isTerrainEdit != null) {
            output.writeBool(33, message.isTerrainEdit, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (ByteString)message.customBytes != null) {
            output.writeBytes(34, message.customBytes, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.customString != null) {
            output.writeString(35, message.customString, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.textureId != null) {
            output.writeString(36, message.textureId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (ByteString)message.slots != null) {
            output.writeBytes(37, message.slots, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.placedAt != null) {
            output.writeInt64(38, message.placedAt, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.slotInfo != null)
    		output.writeObject(40, message.slotInfo, SlotInfo.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.zone != null) {
            output.writeInt32(41, message.zone, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START BuildObject");
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
    	    	//if(this.health != null) {
    		System.out.println("health="+this.health);
    	//}
    	    	//if(this.templateId != null) {
    		System.out.println("templateId="+this.templateId);
    	//}
    	    	//if(this.grid != null) {
    		System.out.println("grid="+this.grid);
    	//}
    	    	//if(this.updatedAt != null) {
    		System.out.println("updatedAt="+this.updatedAt);
    	//}
    	    	//if(this.state != null) {
    		System.out.println("state="+this.state);
    	//}
    	    	//if(this.updateId != null) {
    		System.out.println("updateId="+this.updateId);
    	//}
    	    	//if(this.isFloor != null) {
    		System.out.println("isFloor="+this.isFloor);
    	//}
    	    	//if(this.isDestructable != null) {
    		System.out.println("isDestructable="+this.isDestructable);
    	//}
    	    	//if(this.hasDoor != null) {
    		System.out.println("hasDoor="+this.hasDoor);
    	//}
    	    	//if(this.doorStatus != null) {
    		System.out.println("doorStatus="+this.doorStatus);
    	//}
    	    	//if(this.groundBlockObject != null) {
    		System.out.println("groundBlockObject="+this.groundBlockObject);
    	//}
    	    	//if(this.isGroundBlock != null) {
    		System.out.println("isGroundBlock="+this.isGroundBlock);
    	//}
    	    	//if(this.chunk != null) {
    		System.out.println("chunk="+this.chunk);
    	//}
    	    	//if(this.terrainEdit != null) {
    		System.out.println("terrainEdit="+this.terrainEdit);
    	//}
    	    	//if(this.isTerrainEdit != null) {
    		System.out.println("isTerrainEdit="+this.isTerrainEdit);
    	//}
    	    	//if(this.customBytes != null) {
    		System.out.println("customBytes="+this.customBytes);
    	//}
    	    	//if(this.customString != null) {
    		System.out.println("customString="+this.customString);
    	//}
    	    	//if(this.textureId != null) {
    		System.out.println("textureId="+this.textureId);
    	//}
    	    	//if(this.slots != null) {
    		System.out.println("slots="+this.slots);
    	//}
    	    	//if(this.placedAt != null) {
    		System.out.println("placedAt="+this.placedAt);
    	//}
    	    	//if(this.slotInfo != null) {
    		System.out.println("slotInfo="+this.slotInfo);
    	//}
    	    	//if(this.zone != null) {
    		System.out.println("zone="+this.zone);
    	//}
    	    	System.out.println("END BuildObject");
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
        	        	case 14: return "health";
        	        	case 18: return "templateId";
        	        	case 19: return "grid";
        	        	case 22: return "updatedAt";
        	        	case 23: return "state";
        	        	case 24: return "updateId";
        	        	case 25: return "isFloor";
        	        	case 26: return "isDestructable";
        	        	case 27: return "hasDoor";
        	        	case 28: return "doorStatus";
        	        	case 29: return "groundBlockObject";
        	        	case 30: return "isGroundBlock";
        	        	case 31: return "chunk";
        	        	case 32: return "terrainEdit";
        	        	case 33: return "isTerrainEdit";
        	        	case 34: return "customBytes";
        	        	case 35: return "customString";
        	        	case 36: return "textureId";
        	        	case 37: return "slots";
        	        	case 38: return "placedAt";
        	        	case 40: return "slotInfo";
        	        	case 41: return "zone";
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
    	    	__fieldMap.put("health", 14);
    	    	__fieldMap.put("templateId", 18);
    	    	__fieldMap.put("grid", 19);
    	    	__fieldMap.put("updatedAt", 22);
    	    	__fieldMap.put("state", 23);
    	    	__fieldMap.put("updateId", 24);
    	    	__fieldMap.put("isFloor", 25);
    	    	__fieldMap.put("isDestructable", 26);
    	    	__fieldMap.put("hasDoor", 27);
    	    	__fieldMap.put("doorStatus", 28);
    	    	__fieldMap.put("groundBlockObject", 29);
    	    	__fieldMap.put("isGroundBlock", 30);
    	    	__fieldMap.put("chunk", 31);
    	    	__fieldMap.put("terrainEdit", 32);
    	    	__fieldMap.put("isTerrainEdit", 33);
    	    	__fieldMap.put("customBytes", 34);
    	    	__fieldMap.put("customString", 35);
    	    	__fieldMap.put("textureId", 36);
    	    	__fieldMap.put("slots", 37);
    	    	__fieldMap.put("placedAt", 38);
    	    	__fieldMap.put("slotInfo", 40);
    	    	__fieldMap.put("zone", 41);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildObject.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildObject parseFrom(byte[] bytes) {
	BuildObject message = new BuildObject();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildObject.getSchema());
	return message;
}

public static BuildObject parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildObject message = new BuildObject();
	JsonIOUtil.mergeFrom(bytes, message, BuildObject.getSchema(), false);
	return message;
}

public BuildObject clone() {
	byte[] bytes = this.toByteArray();
	BuildObject buildObject = BuildObject.parseFrom(bytes);
	return buildObject;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildObject> schema = BuildObject.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildObject.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, BuildObject.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
