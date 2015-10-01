
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
public final class BuildObject implements Externalizable, Message<BuildObject>, Schema<BuildObject>, PersistableMessage{



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

    			public String playerItemId;
	    
        			public Integer action;
	    
        			public String id;
	    
        			public Integer recordId;
	    
        			public String ownerId;
	    
        			public Integer x;
	    
        			public Integer y;
	    
        			public Integer z;
	    
        			public Integer rx;
	    
        			public Integer ry;
	    
        			public Integer rz;
	    
        			public Integer rw;
	    
        			public Integer health;
	    
        			public Integer templateId;
	    
        			public String grid;
	    
        			public Long updatedAt;
	    
        			public Integer state;
	    
        			public Integer updateId;
	    
        			public Boolean isFloor;
	    
        			public Boolean isDestructable;
	    
        			public Boolean hasDoor;
	    
        			public Integer doorStatus;
	    
        			public ByteString groundBlockObject;
	    
        			public Boolean isGroundBlock;
	    
        			public Integer chunk;
	    
        			public ByteString terrainEdit;
	    
        			public Boolean isTerrainEdit;
	    
        			public ByteString customBytes;
	    
        			public String customString;
	    
        			public String textureId;
	    
        			public ByteString slots;
	    
        			public Long placedAt;
	    
        			public SlotInfo slotInfo;
	    
        
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
	    	if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.BuildObject.findFirst("id = ?", message.recordId);
	    	}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.BuildObject();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
	    	}
	    		    	if (message.hasSlotInfo()) {
	    		message.slotInfo.toModel(model);
	    	} else {
	    		SlotInfo.clearModel(model);
	    	}
	    		    	
	    	Boolean res = model.save();
	    	if (res) {
	    		message.setRecordId(model.getInteger("id"));
	    	} else {
	    		dbErrors = model.errors();
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
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (playerItemId != null) {
    	       	    	model.setString("build_object_player_item_id",playerItemId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (action != null) {
    	       	    	model.setInteger("build_object_action",action);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("build_object_id",id);
    	        		
    	}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	if (ownerId != null) {
    	       	    	model.setString("build_object_owner_id",ownerId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (x != null) {
    	       	    	model.setInteger("build_object_x",x);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (y != null) {
    	       	    	model.setInteger("build_object_y",y);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (z != null) {
    	       	    	model.setInteger("build_object_z",z);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (rx != null) {
    	       	    	model.setInteger("build_object_rx",rx);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (ry != null) {
    	       	    	model.setInteger("build_object_ry",ry);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (rz != null) {
    	       	    	model.setInteger("build_object_rz",rz);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (rw != null) {
    	       	    	model.setInteger("build_object_rw",rw);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (health != null) {
    	       	    	model.setInteger("build_object_health",health);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (templateId != null) {
    	       	    	model.setInteger("build_object_template_id",templateId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (grid != null) {
    	       	    	model.setString("build_object_grid",grid);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (updatedAt != null) {
    	       	    	model.setLong("build_object_updated_at",updatedAt);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (state != null) {
    	       	    	model.setInteger("build_object_state",state);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (updateId != null) {
    	       	    	model.setInteger("build_object_update_id",updateId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (isFloor != null) {
    	       	    	model.setBoolean("build_object_is_floor",isFloor);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (isDestructable != null) {
    	       	    	model.setBoolean("build_object_is_destructable",isDestructable);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (hasDoor != null) {
    	       	    	model.setBoolean("build_object_has_door",hasDoor);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (doorStatus != null) {
    	       	    	model.setInteger("build_object_door_status",doorStatus);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (groundBlockObject != null) {
    	       	    	model.set("build_object_ground_block_object",groundBlockObject.toByteArray());
    	        		
    	}
    	    	    	    	    	
    	    	    	if (isGroundBlock != null) {
    	       	    	model.setBoolean("build_object_is_ground_block",isGroundBlock);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (chunk != null) {
    	       	    	model.setInteger("build_object_chunk",chunk);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (terrainEdit != null) {
    	       	    	model.set("build_object_terrain_edit",terrainEdit.toByteArray());
    	        		
    	}
    	    	    	    	    	
    	    	    	if (isTerrainEdit != null) {
    	       	    	model.setBoolean("build_object_is_terrain_edit",isTerrainEdit);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (customBytes != null) {
    	       	    	model.set("build_object_custom_bytes",customBytes.toByteArray());
    	        		
    	}
    	    	    	    	    	
    	    	    	if (customString != null) {
    	       	    	model.setString("build_object_custom_string",customString);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (textureId != null) {
    	       	    	model.setString("build_object_texture_id",textureId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (slots != null) {
    	       	    	model.set("build_object_slots",slots.toByteArray());
    	        		
    	}
    	    	    	    	    	
    	    	    	if (placedAt != null) {
    	       	    	model.setLong("build_object_placed_at",placedAt);
    	        		
    	}
    	    	    	    }
    
	public static BuildObject fromModel(Model model) {
		boolean hasFields = false;
    	BuildObject message = new BuildObject();
    	    	    	    	    	
    	    	    	String playerItemIdField = model.getString("build_object_player_item_id");
    	    	
    	if (playerItemIdField != null) {
    		message.setPlayerItemId(playerItemIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer actionField = model.getInteger("build_object_action");
    	    	
    	if (actionField != null) {
    		message.setAction(actionField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String idField = model.getString("build_object_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String ownerIdField = model.getString("build_object_owner_id");
    	    	
    	if (ownerIdField != null) {
    		message.setOwnerId(ownerIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer xField = model.getInteger("build_object_x");
    	    	
    	if (xField != null) {
    		message.setX(xField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer yField = model.getInteger("build_object_y");
    	    	
    	if (yField != null) {
    		message.setY(yField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer zField = model.getInteger("build_object_z");
    	    	
    	if (zField != null) {
    		message.setZ(zField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer rxField = model.getInteger("build_object_rx");
    	    	
    	if (rxField != null) {
    		message.setRx(rxField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer ryField = model.getInteger("build_object_ry");
    	    	
    	if (ryField != null) {
    		message.setRy(ryField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer rzField = model.getInteger("build_object_rz");
    	    	
    	if (rzField != null) {
    		message.setRz(rzField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer rwField = model.getInteger("build_object_rw");
    	    	
    	if (rwField != null) {
    		message.setRw(rwField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer healthField = model.getInteger("build_object_health");
    	    	
    	if (healthField != null) {
    		message.setHealth(healthField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer templateIdField = model.getInteger("build_object_template_id");
    	    	
    	if (templateIdField != null) {
    		message.setTemplateId(templateIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String gridField = model.getString("build_object_grid");
    	    	
    	if (gridField != null) {
    		message.setGrid(gridField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Long updatedAtField = model.getLong("build_object_updated_at");
    	    	
    	if (updatedAtField != null) {
    		message.setUpdatedAt(updatedAtField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer stateField = model.getInteger("build_object_state");
    	    	
    	if (stateField != null) {
    		message.setState(stateField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer updateIdField = model.getInteger("build_object_update_id");
    	    	
    	if (updateIdField != null) {
    		message.setUpdateId(updateIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean isFloorField = model.getBoolean("build_object_is_floor");
    	    	
    	if (isFloorField != null) {
    		message.setIsFloor(isFloorField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean isDestructableField = model.getBoolean("build_object_is_destructable");
    	    	
    	if (isDestructableField != null) {
    		message.setIsDestructable(isDestructableField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean hasDoorField = model.getBoolean("build_object_has_door");
    	    	
    	if (hasDoorField != null) {
    		message.setHasDoor(hasDoorField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer doorStatusField = model.getInteger("build_object_door_status");
    	    	
    	if (doorStatusField != null) {
    		message.setDoorStatus(doorStatusField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	ByteString groundBlockObjectField = null;
    	Object groundBlockObjectValue = model.get("build_object_ground_block_object");
    	if (groundBlockObjectValue != null) {
    		byte[] groundBlockObjectBytes = Convert.toBytes(groundBlockObjectValue);
    		groundBlockObjectField = ByteString.copyFrom(groundBlockObjectBytes);
    	}
    	    	
    	    	
    	if (groundBlockObjectField != null) {
    		message.setGroundBlockObject(groundBlockObjectField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean isGroundBlockField = model.getBoolean("build_object_is_ground_block");
    	    	
    	if (isGroundBlockField != null) {
    		message.setIsGroundBlock(isGroundBlockField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer chunkField = model.getInteger("build_object_chunk");
    	    	
    	if (chunkField != null) {
    		message.setChunk(chunkField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	ByteString terrainEditField = null;
    	Object terrainEditValue = model.get("build_object_terrain_edit");
    	if (terrainEditValue != null) {
    		byte[] terrainEditBytes = Convert.toBytes(terrainEditValue);
    		terrainEditField = ByteString.copyFrom(terrainEditBytes);
    	}
    	    	
    	    	
    	if (terrainEditField != null) {
    		message.setTerrainEdit(terrainEditField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean isTerrainEditField = model.getBoolean("build_object_is_terrain_edit");
    	    	
    	if (isTerrainEditField != null) {
    		message.setIsTerrainEdit(isTerrainEditField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	ByteString customBytesField = null;
    	Object customBytesValue = model.get("build_object_custom_bytes");
    	if (customBytesValue != null) {
    		byte[] customBytesBytes = Convert.toBytes(customBytesValue);
    		customBytesField = ByteString.copyFrom(customBytesBytes);
    	}
    	    	
    	    	
    	if (customBytesField != null) {
    		message.setCustomBytes(customBytesField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String customStringField = model.getString("build_object_custom_string");
    	    	
    	if (customStringField != null) {
    		message.setCustomString(customStringField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String textureIdField = model.getString("build_object_texture_id");
    	    	
    	if (textureIdField != null) {
    		message.setTextureId(textureIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	ByteString slotsField = null;
    	Object slotsValue = model.get("build_object_slots");
    	if (slotsValue != null) {
    		byte[] slotsBytes = Convert.toBytes(slotsValue);
    		slotsField = ByteString.copyFrom(slotsBytes);
    	}
    	    	
    	    	
    	if (slotsField != null) {
    		message.setSlots(slotsField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Long placedAtField = model.getLong("build_object_placed_at");
    	    	
    	if (placedAtField != null) {
    		message.setPlacedAt(placedAtField);
    		hasFields = true;
    	}
    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasPlayerItemId()  {
        return playerItemId == null ? false : true;
    }
        
		public String getPlayerItemId() {
		return playerItemId;
	}
	
	public BuildObject setPlayerItemId(String playerItemId) {
		this.playerItemId = playerItemId;
		return this;	}
	
		    
    public Boolean hasAction()  {
        return action == null ? false : true;
    }
        
		public Integer getAction() {
		return action;
	}
	
	public BuildObject setAction(Integer action) {
		this.action = action;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public BuildObject setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public BuildObject setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasOwnerId()  {
        return ownerId == null ? false : true;
    }
        
		public String getOwnerId() {
		return ownerId;
	}
	
	public BuildObject setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		    
    public Boolean hasX()  {
        return x == null ? false : true;
    }
        
		public Integer getX() {
		return x;
	}
	
	public BuildObject setX(Integer x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Integer getY() {
		return y;
	}
	
	public BuildObject setY(Integer y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasZ()  {
        return z == null ? false : true;
    }
        
		public Integer getZ() {
		return z;
	}
	
	public BuildObject setZ(Integer z) {
		this.z = z;
		return this;	}
	
		    
    public Boolean hasRx()  {
        return rx == null ? false : true;
    }
        
		public Integer getRx() {
		return rx;
	}
	
	public BuildObject setRx(Integer rx) {
		this.rx = rx;
		return this;	}
	
		    
    public Boolean hasRy()  {
        return ry == null ? false : true;
    }
        
		public Integer getRy() {
		return ry;
	}
	
	public BuildObject setRy(Integer ry) {
		this.ry = ry;
		return this;	}
	
		    
    public Boolean hasRz()  {
        return rz == null ? false : true;
    }
        
		public Integer getRz() {
		return rz;
	}
	
	public BuildObject setRz(Integer rz) {
		this.rz = rz;
		return this;	}
	
		    
    public Boolean hasRw()  {
        return rw == null ? false : true;
    }
        
		public Integer getRw() {
		return rw;
	}
	
	public BuildObject setRw(Integer rw) {
		this.rw = rw;
		return this;	}
	
		    
    public Boolean hasHealth()  {
        return health == null ? false : true;
    }
        
		public Integer getHealth() {
		return health;
	}
	
	public BuildObject setHealth(Integer health) {
		this.health = health;
		return this;	}
	
		    
    public Boolean hasTemplateId()  {
        return templateId == null ? false : true;
    }
        
		public Integer getTemplateId() {
		return templateId;
	}
	
	public BuildObject setTemplateId(Integer templateId) {
		this.templateId = templateId;
		return this;	}
	
		    
    public Boolean hasGrid()  {
        return grid == null ? false : true;
    }
        
		public String getGrid() {
		return grid;
	}
	
	public BuildObject setGrid(String grid) {
		this.grid = grid;
		return this;	}
	
		    
    public Boolean hasUpdatedAt()  {
        return updatedAt == null ? false : true;
    }
        
		public Long getUpdatedAt() {
		return updatedAt;
	}
	
	public BuildObject setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
		return this;	}
	
		    
    public Boolean hasState()  {
        return state == null ? false : true;
    }
        
		public Integer getState() {
		return state;
	}
	
	public BuildObject setState(Integer state) {
		this.state = state;
		return this;	}
	
		    
    public Boolean hasUpdateId()  {
        return updateId == null ? false : true;
    }
        
		public Integer getUpdateId() {
		return updateId;
	}
	
	public BuildObject setUpdateId(Integer updateId) {
		this.updateId = updateId;
		return this;	}
	
		    
    public Boolean hasIsFloor()  {
        return isFloor == null ? false : true;
    }
        
		public Boolean getIsFloor() {
		return isFloor;
	}
	
	public BuildObject setIsFloor(Boolean isFloor) {
		this.isFloor = isFloor;
		return this;	}
	
		    
    public Boolean hasIsDestructable()  {
        return isDestructable == null ? false : true;
    }
        
		public Boolean getIsDestructable() {
		return isDestructable;
	}
	
	public BuildObject setIsDestructable(Boolean isDestructable) {
		this.isDestructable = isDestructable;
		return this;	}
	
		    
    public Boolean hasHasDoor()  {
        return hasDoor == null ? false : true;
    }
        
		public Boolean getHasDoor() {
		return hasDoor;
	}
	
	public BuildObject setHasDoor(Boolean hasDoor) {
		this.hasDoor = hasDoor;
		return this;	}
	
		    
    public Boolean hasDoorStatus()  {
        return doorStatus == null ? false : true;
    }
        
		public Integer getDoorStatus() {
		return doorStatus;
	}
	
	public BuildObject setDoorStatus(Integer doorStatus) {
		this.doorStatus = doorStatus;
		return this;	}
	
		    
    public Boolean hasGroundBlockObject()  {
        return groundBlockObject == null ? false : true;
    }
        
		public ByteString getGroundBlockObject() {
		return groundBlockObject;
	}
	
	public BuildObject setGroundBlockObject(ByteString groundBlockObject) {
		this.groundBlockObject = groundBlockObject;
		return this;	}
	
		    
    public Boolean hasIsGroundBlock()  {
        return isGroundBlock == null ? false : true;
    }
        
		public Boolean getIsGroundBlock() {
		return isGroundBlock;
	}
	
	public BuildObject setIsGroundBlock(Boolean isGroundBlock) {
		this.isGroundBlock = isGroundBlock;
		return this;	}
	
		    
    public Boolean hasChunk()  {
        return chunk == null ? false : true;
    }
        
		public Integer getChunk() {
		return chunk;
	}
	
	public BuildObject setChunk(Integer chunk) {
		this.chunk = chunk;
		return this;	}
	
		    
    public Boolean hasTerrainEdit()  {
        return terrainEdit == null ? false : true;
    }
        
		public ByteString getTerrainEdit() {
		return terrainEdit;
	}
	
	public BuildObject setTerrainEdit(ByteString terrainEdit) {
		this.terrainEdit = terrainEdit;
		return this;	}
	
		    
    public Boolean hasIsTerrainEdit()  {
        return isTerrainEdit == null ? false : true;
    }
        
		public Boolean getIsTerrainEdit() {
		return isTerrainEdit;
	}
	
	public BuildObject setIsTerrainEdit(Boolean isTerrainEdit) {
		this.isTerrainEdit = isTerrainEdit;
		return this;	}
	
		    
    public Boolean hasCustomBytes()  {
        return customBytes == null ? false : true;
    }
        
		public ByteString getCustomBytes() {
		return customBytes;
	}
	
	public BuildObject setCustomBytes(ByteString customBytes) {
		this.customBytes = customBytes;
		return this;	}
	
		    
    public Boolean hasCustomString()  {
        return customString == null ? false : true;
    }
        
		public String getCustomString() {
		return customString;
	}
	
	public BuildObject setCustomString(String customString) {
		this.customString = customString;
		return this;	}
	
		    
    public Boolean hasTextureId()  {
        return textureId == null ? false : true;
    }
        
		public String getTextureId() {
		return textureId;
	}
	
	public BuildObject setTextureId(String textureId) {
		this.textureId = textureId;
		return this;	}
	
		    
    public Boolean hasSlots()  {
        return slots == null ? false : true;
    }
        
		public ByteString getSlots() {
		return slots;
	}
	
	public BuildObject setSlots(ByteString slots) {
		this.slots = slots;
		return this;	}
	
		    
    public Boolean hasPlacedAt()  {
        return placedAt == null ? false : true;
    }
        
		public Long getPlacedAt() {
		return placedAt;
	}
	
	public BuildObject setPlacedAt(Long placedAt) {
		this.placedAt = placedAt;
		return this;	}
	
		    
    public Boolean hasSlotInfo()  {
        return slotInfo == null ? false : true;
    }
        
		public SlotInfo getSlotInfo() {
		return slotInfo;
	}
	
	public BuildObject setSlotInfo(SlotInfo slotInfo) {
		this.slotInfo = slotInfo;
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
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildObject message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.playerItemId != null)
            output.writeString(1, message.playerItemId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.action != null)
            output.writeInt32(2, message.action, false);
    	    	
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(3, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(4, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ownerId != null)
            output.writeString(5, message.ownerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.x != null)
            output.writeInt32(6, message.x, false);
    	    	
    	            	
    	    	
    	    	    	if(message.y != null)
            output.writeInt32(7, message.y, false);
    	    	
    	            	
    	    	
    	    	    	if(message.z != null)
            output.writeInt32(8, message.z, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rx != null)
            output.writeInt32(9, message.rx, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ry != null)
            output.writeInt32(10, message.ry, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rz != null)
            output.writeInt32(11, message.rz, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rw != null)
            output.writeInt32(12, message.rw, false);
    	    	
    	            	
    	    	
    	    	    	if(message.health != null)
            output.writeInt32(14, message.health, false);
    	    	
    	            	
    	    	
    	    	    	if(message.templateId != null)
            output.writeInt32(18, message.templateId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.grid != null)
            output.writeString(19, message.grid, false);
    	    	
    	            	
    	    	
    	    	    	if(message.updatedAt != null)
            output.writeInt64(22, message.updatedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.state != null)
            output.writeInt32(23, message.state, false);
    	    	
    	            	
    	    	
    	    	    	if(message.updateId != null)
            output.writeInt32(24, message.updateId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isFloor != null)
            output.writeBool(25, message.isFloor, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isDestructable != null)
            output.writeBool(26, message.isDestructable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.hasDoor != null)
            output.writeBool(27, message.hasDoor, false);
    	    	
    	            	
    	    	
    	    	    	if(message.doorStatus != null)
            output.writeInt32(28, message.doorStatus, false);
    	    	
    	            	
    	    	
    	    	    	if(message.groundBlockObject != null)
            output.writeBytes(29, message.groundBlockObject, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isGroundBlock != null)
            output.writeBool(30, message.isGroundBlock, false);
    	    	
    	            	
    	    	
    	    	    	if(message.chunk != null)
            output.writeInt32(31, message.chunk, false);
    	    	
    	            	
    	    	
    	    	    	if(message.terrainEdit != null)
            output.writeBytes(32, message.terrainEdit, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isTerrainEdit != null)
            output.writeBool(33, message.isTerrainEdit, false);
    	    	
    	            	
    	    	
    	    	    	if(message.customBytes != null)
            output.writeBytes(34, message.customBytes, false);
    	    	
    	            	
    	    	
    	    	    	if(message.customString != null)
            output.writeString(35, message.customString, false);
    	    	
    	            	
    	    	
    	    	    	if(message.textureId != null)
            output.writeString(36, message.textureId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.slots != null)
            output.writeBytes(37, message.slots, false);
    	    	
    	            	
    	    	
    	    	    	if(message.placedAt != null)
            output.writeInt64(38, message.placedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.slotInfo != null)
    		output.writeObject(40, message.slotInfo, SlotInfo.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START BuildObject");
    	    	if(this.playerItemId != null) {
    		System.out.println("playerItemId="+this.playerItemId);
    	}
    	    	if(this.action != null) {
    		System.out.println("action="+this.action);
    	}
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	}
    	    	if(this.ownerId != null) {
    		System.out.println("ownerId="+this.ownerId);
    	}
    	    	if(this.x != null) {
    		System.out.println("x="+this.x);
    	}
    	    	if(this.y != null) {
    		System.out.println("y="+this.y);
    	}
    	    	if(this.z != null) {
    		System.out.println("z="+this.z);
    	}
    	    	if(this.rx != null) {
    		System.out.println("rx="+this.rx);
    	}
    	    	if(this.ry != null) {
    		System.out.println("ry="+this.ry);
    	}
    	    	if(this.rz != null) {
    		System.out.println("rz="+this.rz);
    	}
    	    	if(this.rw != null) {
    		System.out.println("rw="+this.rw);
    	}
    	    	if(this.health != null) {
    		System.out.println("health="+this.health);
    	}
    	    	if(this.templateId != null) {
    		System.out.println("templateId="+this.templateId);
    	}
    	    	if(this.grid != null) {
    		System.out.println("grid="+this.grid);
    	}
    	    	if(this.updatedAt != null) {
    		System.out.println("updatedAt="+this.updatedAt);
    	}
    	    	if(this.state != null) {
    		System.out.println("state="+this.state);
    	}
    	    	if(this.updateId != null) {
    		System.out.println("updateId="+this.updateId);
    	}
    	    	if(this.isFloor != null) {
    		System.out.println("isFloor="+this.isFloor);
    	}
    	    	if(this.isDestructable != null) {
    		System.out.println("isDestructable="+this.isDestructable);
    	}
    	    	if(this.hasDoor != null) {
    		System.out.println("hasDoor="+this.hasDoor);
    	}
    	    	if(this.doorStatus != null) {
    		System.out.println("doorStatus="+this.doorStatus);
    	}
    	    	if(this.groundBlockObject != null) {
    		System.out.println("groundBlockObject="+this.groundBlockObject);
    	}
    	    	if(this.isGroundBlock != null) {
    		System.out.println("isGroundBlock="+this.isGroundBlock);
    	}
    	    	if(this.chunk != null) {
    		System.out.println("chunk="+this.chunk);
    	}
    	    	if(this.terrainEdit != null) {
    		System.out.println("terrainEdit="+this.terrainEdit);
    	}
    	    	if(this.isTerrainEdit != null) {
    		System.out.println("isTerrainEdit="+this.isTerrainEdit);
    	}
    	    	if(this.customBytes != null) {
    		System.out.println("customBytes="+this.customBytes);
    	}
    	    	if(this.customString != null) {
    		System.out.println("customString="+this.customString);
    	}
    	    	if(this.textureId != null) {
    		System.out.println("textureId="+this.textureId);
    	}
    	    	if(this.slots != null) {
    		System.out.println("slots="+this.slots);
    	}
    	    	if(this.placedAt != null) {
    		System.out.println("placedAt="+this.placedAt);
    	}
    	    	if(this.slotInfo != null) {
    		System.out.println("slotInfo="+this.slotInfo);
    	}
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
		throw new RuntimeException("Protobuf encoding failed");
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
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
