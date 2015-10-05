
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
public final class Character implements Externalizable, Message<Character>, Schema<Character>, PersistableMessage{



    public static Schema<Character> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Character getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Character DEFAULT_INSTANCE = new Character();
    static final String defaultScope = Character.class.getSimpleName();

    	
	    	    public String id= null;
	    		
    
        	
	    	    public String umaData= null;
	    		
    
        	
	    	    public int health= 0;
	    		
    
        	
	    	    public int recordId= 0;
	    		
    
        	
	    	    public String playerId= null;
	    		
    
        	
	    	    public int part= 0;
	    		
    
        	
	    	    public int parts= 0;
	    		
    
        	
	    	    public int worldx= 0;
	    		
    
        	
	    	    public int worldy= 0;
	    		
    
        	
	    	    public int worldz= 0;
	    		
    
        	
	    	    public int zone= 0;
	    		
    
        	
	    	    public int stamina= 0;
	    		
    
        	
	    	    public int magic= 0;
	    		
    
        	
	    	    public boolean includeUmaData= false;
	    		
    
        
	public static CharacterCache cache() {
		return CharacterCache.getInstance();
	}
	
	public static CharacterStore store() {
		return CharacterStore.getInstance();
	}


    public Character()
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
		
		public Character result(int timeout) {
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
	
	public static class CharacterCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Character> cache = new Cache<String, Character>(120, 5000);
		
		private CharacterCache() {
		}
		
		private static class LazyHolder {
			private static final CharacterCache INSTANCE = new CharacterCache();
		}
	
		public static CharacterCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Character>(expiration, size);
		}
	
		public Cache<String, Character> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(CharacterCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Character message) {
			CacheUpdate cacheUpdate = new CacheUpdate(CharacterCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Character get(String id, int timeout) {
			Character message = cache.get(id);
			if (message == null) {
				message = Character.store().get(id, timeout);
			}
			return message;
		}
			
		public static Character setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Character message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Character) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Character.store().set(message);
			} else {
				message = Character.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Character.class.getField(field));
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
				Character.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class CharacterStore {
	
		private CharacterStore() {
		}
		
		private static class LazyHolder {
			private static final CharacterStore INSTANCE = new CharacterStore();
		}
	
		public static CharacterStore getInstance() {
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
		
	    public void set(Character message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Character get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Character message) {
	    	Character clone = message.clone();
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
			
		public Character get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Character");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Character message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Character) {
					message = (Character)result;
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
	

	

	public static CharacterDb db() {
		return CharacterDb.getInstance();
	}
	
	public interface CharacterAsyncDb {
		void save(Character message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class CharacterAsyncDbImpl implements CharacterAsyncDb {
	
		public void save(Character message) {
			Character.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	Character.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	Character.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class CharacterDb {
	
		public Errors dbErrors;
		private CharacterAsyncDb asyncDb = null;
		
		private CharacterDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<CharacterAsyncDbImpl>(CharacterAsyncDb.class, CharacterAsyncDbImpl.class));
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
			private static final CharacterDb INSTANCE = new CharacterDb();
		}
	
		public static CharacterDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(Character message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(Character message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(Character message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Character.open();
	    	}
	    	
	    	io.gamemachine.orm.models.Character model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.Character.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.Character();
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
	    		io.gamemachine.orm.models.Character.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.Character.open();
	    	int deleted = io.gamemachine.orm.models.Character.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.Character.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.Character.open();
	    	int deleted = io.gamemachine.orm.models.Character.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.Character.close();
	    	return result;
	    }
	    
	    public Character find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public Character find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Character.open();
	    	}
	    	
	    	io.gamemachine.orm.models.Character model = io.gamemachine.orm.models.Character.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Character.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Character character = fromModel(model);
	    			    		return character;
	    	}
	    }
	    
	    public Character findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.Character.open();
	    	io.gamemachine.orm.models.Character model = io.gamemachine.orm.models.Character.findFirst(query, params);
	    	io.gamemachine.orm.models.Character.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Character character = fromModel(model);
	    			    		return character;
	    	}
	    }
	    
	    public List<Character> findAll() {
	    	io.gamemachine.orm.models.Character.open();
	    	List<io.gamemachine.orm.models.Character> models = io.gamemachine.orm.models.Character.findAll();
	    	List<Character> messages = new ArrayList<Character>();
	    	for (io.gamemachine.orm.models.Character model : models) {
	    		Character character = fromModel(model);
	    			    		messages.add(character);
	    	}
	    	io.gamemachine.orm.models.Character.close();
	    	return messages;
	    }
	    
	    public List<Character> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<Character> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Character.open();
	    	}
	    	List<io.gamemachine.orm.models.Character> models = io.gamemachine.orm.models.Character.where(query, params);
	    	List<Character> messages = new ArrayList<Character>();
	    	for (io.gamemachine.orm.models.Character model : models) {
	    		Character character = fromModel(model);
	    			    		messages.add(character);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Character.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("character_id",null);
    	    	    	    	    	    	model.set("character_uma_data",null);
    	    	    	    	    	    	model.set("character_health",null);
    	    	    	    	    	    	    	    	    	model.set("character_player_id",null);
    	    	    	    	    	    	model.set("character_part",null);
    	    	    	    	    	    	model.set("character_parts",null);
    	    	    	    	    	    	model.set("character_worldx",null);
    	    	    	    	    	    	model.set("character_worldy",null);
    	    	    	    	    	    	model.set("character_worldz",null);
    	    	    	    	    	    	model.set("character_zone",null);
    	    	    	    	    	    	model.set("character_stamina",null);
    	    	    	    	    	    	model.set("character_magic",null);
    	    	    	    	    	    	model.set("character_include_uma_data",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("character_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (umaData != null) {
    	       	    	model.setString("character_uma_data",umaData);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (health != null) {
    	       	    	model.setInteger("character_health",health);
    	        		
    	//}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("character_player_id",playerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (part != null) {
    	       	    	model.setInteger("character_part",part);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (parts != null) {
    	       	    	model.setInteger("character_parts",parts);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (worldx != null) {
    	       	    	model.setInteger("character_worldx",worldx);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (worldy != null) {
    	       	    	model.setInteger("character_worldy",worldy);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (worldz != null) {
    	       	    	model.setInteger("character_worldz",worldz);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (zone != null) {
    	       	    	model.setInteger("character_zone",zone);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (stamina != null) {
    	       	    	model.setInteger("character_stamina",stamina);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (magic != null) {
    	       	    	model.setInteger("character_magic",magic);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (includeUmaData != null) {
    	       	    	model.setBoolean("character_include_uma_data",includeUmaData);
    	        		
    	//}
    	    	    }
    
	public static Character fromModel(Model model) {
		boolean hasFields = false;
    	Character message = new Character();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("character_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String umaDataTestField = model.getString("character_uma_data");
    	if (umaDataTestField != null) {
    		String umaDataField = umaDataTestField;
    		message.setUmaData(umaDataField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer healthTestField = model.getInteger("character_health");
    	if (healthTestField != null) {
    		int healthField = healthTestField;
    		message.setHealth(healthField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    	    	String playerIdTestField = model.getString("character_player_id");
    	if (playerIdTestField != null) {
    		String playerIdField = playerIdTestField;
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer partTestField = model.getInteger("character_part");
    	if (partTestField != null) {
    		int partField = partTestField;
    		message.setPart(partField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer partsTestField = model.getInteger("character_parts");
    	if (partsTestField != null) {
    		int partsField = partsTestField;
    		message.setParts(partsField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer worldxTestField = model.getInteger("character_worldx");
    	if (worldxTestField != null) {
    		int worldxField = worldxTestField;
    		message.setWorldx(worldxField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer worldyTestField = model.getInteger("character_worldy");
    	if (worldyTestField != null) {
    		int worldyField = worldyTestField;
    		message.setWorldy(worldyField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer worldzTestField = model.getInteger("character_worldz");
    	if (worldzTestField != null) {
    		int worldzField = worldzTestField;
    		message.setWorldz(worldzField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer zoneTestField = model.getInteger("character_zone");
    	if (zoneTestField != null) {
    		int zoneField = zoneTestField;
    		message.setZone(zoneField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer staminaTestField = model.getInteger("character_stamina");
    	if (staminaTestField != null) {
    		int staminaField = staminaTestField;
    		message.setStamina(staminaField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer magicTestField = model.getInteger("character_magic");
    	if (magicTestField != null) {
    		int magicField = magicTestField;
    		message.setMagic(magicField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Boolean includeUmaDataTestField = model.getBoolean("character_include_uma_data");
    	if (includeUmaDataTestField != null) {
    		boolean includeUmaDataField = includeUmaDataTestField;
    		message.setIncludeUmaData(includeUmaDataField);
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
	
	public Character setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getUmaData() {
		return umaData;
	}
	
	public Character setUmaData(String umaData) {
		this.umaData = umaData;
		return this;	}
	
		            
		public int getHealth() {
		return health;
	}
	
	public Character setHealth(int health) {
		this.health = health;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public Character setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public String getPlayerId() {
		return playerId;
	}
	
	public Character setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		            
		public int getPart() {
		return part;
	}
	
	public Character setPart(int part) {
		this.part = part;
		return this;	}
	
		            
		public int getParts() {
		return parts;
	}
	
	public Character setParts(int parts) {
		this.parts = parts;
		return this;	}
	
		            
		public int getWorldx() {
		return worldx;
	}
	
	public Character setWorldx(int worldx) {
		this.worldx = worldx;
		return this;	}
	
		            
		public int getWorldy() {
		return worldy;
	}
	
	public Character setWorldy(int worldy) {
		this.worldy = worldy;
		return this;	}
	
		            
		public int getWorldz() {
		return worldz;
	}
	
	public Character setWorldz(int worldz) {
		this.worldz = worldz;
		return this;	}
	
		            
		public int getZone() {
		return zone;
	}
	
	public Character setZone(int zone) {
		this.zone = zone;
		return this;	}
	
		            
		public int getStamina() {
		return stamina;
	}
	
	public Character setStamina(int stamina) {
		this.stamina = stamina;
		return this;	}
	
		            
		public int getMagic() {
		return magic;
	}
	
	public Character setMagic(int magic) {
		this.magic = magic;
		return this;	}
	
		            
		public boolean getIncludeUmaData() {
		return includeUmaData;
	}
	
	public Character setIncludeUmaData(boolean includeUmaData) {
		this.includeUmaData = includeUmaData;
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

    public Schema<Character> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Character newMessage()
    {
        return new Character();
    }

    public Class<Character> typeClass()
    {
        return Character.class;
    }

    public String messageName()
    {
        return Character.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Character.class.getName();
    }

    public boolean isInitialized(Character message)
    {
        return true;
    }

    public void mergeFrom(Input input, Character message) throws IOException
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
            	                	                	message.umaData = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.part = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.parts = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.worldx = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.worldy = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.worldz = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.zone = input.readInt32();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.stamina = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.magic = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.includeUmaData = input.readBool();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Character message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.umaData != null) {
            output.writeString(2, message.umaData, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.health != null) {
            output.writeInt32(3, message.health, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(4, message.recordId, false);
        }
    	    	
    	            	
    	    	//if(message.playerId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(5, message.playerId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.part != null) {
            output.writeInt32(6, message.part, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.parts != null) {
            output.writeInt32(7, message.parts, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.worldx != null) {
            output.writeInt32(8, message.worldx, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.worldy != null) {
            output.writeInt32(9, message.worldy, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.worldz != null) {
            output.writeInt32(10, message.worldz, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.zone != null) {
            output.writeInt32(11, message.zone, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.stamina != null) {
            output.writeInt32(12, message.stamina, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.magic != null) {
            output.writeInt32(13, message.magic, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.includeUmaData != null) {
            output.writeBool(14, message.includeUmaData, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Character");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.umaData != null) {
    		System.out.println("umaData="+this.umaData);
    	//}
    	    	//if(this.health != null) {
    		System.out.println("health="+this.health);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	//if(this.part != null) {
    		System.out.println("part="+this.part);
    	//}
    	    	//if(this.parts != null) {
    		System.out.println("parts="+this.parts);
    	//}
    	    	//if(this.worldx != null) {
    		System.out.println("worldx="+this.worldx);
    	//}
    	    	//if(this.worldy != null) {
    		System.out.println("worldy="+this.worldy);
    	//}
    	    	//if(this.worldz != null) {
    		System.out.println("worldz="+this.worldz);
    	//}
    	    	//if(this.zone != null) {
    		System.out.println("zone="+this.zone);
    	//}
    	    	//if(this.stamina != null) {
    		System.out.println("stamina="+this.stamina);
    	//}
    	    	//if(this.magic != null) {
    		System.out.println("magic="+this.magic);
    	//}
    	    	//if(this.includeUmaData != null) {
    		System.out.println("includeUmaData="+this.includeUmaData);
    	//}
    	    	System.out.println("END Character");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "umaData";
        	        	case 3: return "health";
        	        	case 4: return "recordId";
        	        	case 5: return "playerId";
        	        	case 6: return "part";
        	        	case 7: return "parts";
        	        	case 8: return "worldx";
        	        	case 9: return "worldy";
        	        	case 10: return "worldz";
        	        	case 11: return "zone";
        	        	case 12: return "stamina";
        	        	case 13: return "magic";
        	        	case 14: return "includeUmaData";
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
    	    	__fieldMap.put("umaData", 2);
    	    	__fieldMap.put("health", 3);
    	    	__fieldMap.put("recordId", 4);
    	    	__fieldMap.put("playerId", 5);
    	    	__fieldMap.put("part", 6);
    	    	__fieldMap.put("parts", 7);
    	    	__fieldMap.put("worldx", 8);
    	    	__fieldMap.put("worldy", 9);
    	    	__fieldMap.put("worldz", 10);
    	    	__fieldMap.put("zone", 11);
    	    	__fieldMap.put("stamina", 12);
    	    	__fieldMap.put("magic", 13);
    	    	__fieldMap.put("includeUmaData", 14);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Character.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Character parseFrom(byte[] bytes) {
	Character message = new Character();
	ProtobufIOUtil.mergeFrom(bytes, message, Character.getSchema());
	return message;
}

public static Character parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Character message = new Character();
	JsonIOUtil.mergeFrom(bytes, message, Character.getSchema(), false);
	return message;
}

public Character clone() {
	byte[] bytes = this.toByteArray();
	Character character = Character.parseFrom(bytes);
	return character;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Character.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Character> schema = Character.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Character.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Character.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
