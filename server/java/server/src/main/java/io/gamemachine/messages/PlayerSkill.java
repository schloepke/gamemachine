
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
public final class PlayerSkill implements Externalizable, Message<PlayerSkill>, Schema<PlayerSkill>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(PlayerSkill.class);

	public enum DamageType implements io.protostuff.EnumLite<DamageType>
    {
    	
    	    	DamageTypeNone(0),    	    	Aoe(1),    	    	SingleTarget(2),    	    	Pbaoe(3),    	    	Self(4);    	        
        public final int number;
        
        private DamageType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static DamageType defaultValue() {
        	return (DamageTypeNone);
        }
        
        public static DamageType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (DamageTypeNone);
    			    			case 1: return (Aoe);
    			    			case 2: return (SingleTarget);
    			    			case 3: return (Pbaoe);
    			    			case 4: return (Self);
    			                default: return null;
            }
        }
    }
	public enum SkillType implements io.protostuff.EnumLite<SkillType>
    {
    	
    	    	SkillTypeNone(0),    	    	Active(1),    	    	Passive(2);    	        
        public final int number;
        
        private SkillType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static SkillType defaultValue() {
        	return (SkillTypeNone);
        }
        
        public static SkillType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (SkillTypeNone);
    			    			case 1: return (Active);
    			    			case 2: return (Passive);
    			                default: return null;
            }
        }
    }
	public enum Category implements io.protostuff.EnumLite<Category>
    {
    	
    	    	CategoryNone(0),    	    	Weapon(1),    	    	Crafting(2),    	    	CategoryOther(3);    	        
        public final int number;
        
        private Category (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Category defaultValue() {
        	return (CategoryNone);
        }
        
        public static Category valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (CategoryNone);
    			    			case 1: return (Weapon);
    			    			case 2: return (Crafting);
    			    			case 3: return (CategoryOther);
    			                default: return null;
            }
        }
    }
	public enum WeaponType implements io.protostuff.EnumLite<WeaponType>
    {
    	
    	    	WeaponTypeNone(0),    	    	Bow(1),    	    	Sword2h(2),    	    	Sword1h(3),    	    	Staff(4),    	    	Gun(5),    	    	Siege(6),    	    	WeaponTypeOther(7);    	        
        public final int number;
        
        private WeaponType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static WeaponType defaultValue() {
        	return (WeaponTypeNone);
        }
        
        public static WeaponType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (WeaponTypeNone);
    			    			case 1: return (Bow);
    			    			case 2: return (Sword2h);
    			    			case 3: return (Sword1h);
    			    			case 4: return (Staff);
    			    			case 5: return (Gun);
    			    			case 6: return (Siege);
    			    			case 7: return (WeaponTypeOther);
    			                default: return null;
            }
        }
    }


    public static Schema<PlayerSkill> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerSkill getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerSkill DEFAULT_INSTANCE = new PlayerSkill();
    static final String defaultScope = PlayerSkill.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
							    public String name= null;
		    			    
		
    
        	
							    public int recordId= 0;
		    			    
		
    
        	
							    public String category= null;
		    			    
		
    
        	
							    public String damageType= null;
		    			    
		
    
        	
							    public String icon_path= null;
		    			    
		
    
        	
							    public String description= null;
		    			    
		
    
        	
							    public String characterId= null;
		    			    
		
    
        	
							    public String weaponType= null;
		    			    
		
    
        	
							    public String statusEffectId= null;
		    			    
		
    
        	
							    public int level= 0;
		    			    
		
    
        	
							    public int isComboPart= 0;
		    			    
		
    
        	
							    public int isPassive= 0;
		    			    
		
    
        	
							    public String skillType= null;
		    			    
		
    
        	
							    public String icon_uuid= null;
		    			    
		
    
        	
							    public String statusEffects= null;
		    			    
		
    
        
	public static PlayerSkillCache cache() {
		return PlayerSkillCache.getInstance();
	}
	
	public static PlayerSkillStore store() {
		return PlayerSkillStore.getInstance();
	}


    public PlayerSkill()
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
		
		public PlayerSkill result(int timeout) {
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
	
	public static class PlayerSkillCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, PlayerSkill> cache = new Cache<String, PlayerSkill>(120, 5000);
		
		private PlayerSkillCache() {
		}
		
		private static class LazyHolder {
			private static final PlayerSkillCache INSTANCE = new PlayerSkillCache();
		}
	
		public static PlayerSkillCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, PlayerSkill>(expiration, size);
		}
	
		public Cache<String, PlayerSkill> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(PlayerSkillCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(PlayerSkill message) {
			CacheUpdate cacheUpdate = new CacheUpdate(PlayerSkillCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public PlayerSkill get(String id, int timeout) {
			PlayerSkill message = cache.get(id);
			if (message == null) {
				message = PlayerSkill.store().get(id, timeout);
			}
			return message;
		}
			
		public static PlayerSkill setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			PlayerSkill message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (PlayerSkill) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				PlayerSkill.store().set(message);
			} else {
				message = PlayerSkill.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, PlayerSkill.class.getField(field));
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
				PlayerSkill.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class PlayerSkillStore {
	
		private PlayerSkillStore() {
		}
		
		private static class LazyHolder {
			private static final PlayerSkillStore INSTANCE = new PlayerSkillStore();
		}
	
		public static PlayerSkillStore getInstance() {
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
		
	    public void set(PlayerSkill message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public PlayerSkill get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, PlayerSkill message) {
	    	PlayerSkill clone = message.clone();
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
			
		public PlayerSkill get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("PlayerSkill");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			PlayerSkill message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof PlayerSkill) {
					message = (PlayerSkill)result;
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
	

	

	public static PlayerSkillDb db() {
		return PlayerSkillDb.getInstance();
	}
	
	public interface PlayerSkillAsyncDb {
		void save(PlayerSkill message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class PlayerSkillAsyncDbImpl implements PlayerSkillAsyncDb {
	
		public void save(PlayerSkill message) {
			PlayerSkill.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	PlayerSkill.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	PlayerSkill.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class PlayerSkillDb {
	
		public Errors dbErrors;
		private PlayerSkillAsyncDb asyncDb = null;
		
		private PlayerSkillDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<PlayerSkillAsyncDbImpl>(PlayerSkillAsyncDb.class, PlayerSkillAsyncDbImpl.class));
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
			private static final PlayerSkillDb INSTANCE = new PlayerSkillDb();
		}
	
		public static PlayerSkillDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(PlayerSkill message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(PlayerSkill message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(PlayerSkill message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerSkill.open();
	    	}
	    	
	    	io.gamemachine.orm.models.PlayerSkill model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.PlayerSkill.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.PlayerSkill();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
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
	    		io.gamemachine.orm.models.PlayerSkill.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.PlayerSkill.open();
	    	int deleted = io.gamemachine.orm.models.PlayerSkill.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.PlayerSkill.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.PlayerSkill.open();
	    	int deleted = io.gamemachine.orm.models.PlayerSkill.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.PlayerSkill.close();
	    	return result;
	    }
	    
	    public PlayerSkill find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public PlayerSkill find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerSkill.open();
	    	}
	    	
	    	io.gamemachine.orm.models.PlayerSkill model = io.gamemachine.orm.models.PlayerSkill.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerSkill.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		PlayerSkill playerSkill = fromModel(model);
	    			    		return playerSkill;
	    	}
	    }
	    
	    public PlayerSkill findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.PlayerSkill.open();
	    	io.gamemachine.orm.models.PlayerSkill model = io.gamemachine.orm.models.PlayerSkill.findFirst(query, params);
	    	io.gamemachine.orm.models.PlayerSkill.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		PlayerSkill playerSkill = fromModel(model);
	    			    		return playerSkill;
	    	}
	    }
	    
	    public List<PlayerSkill> findAll() {
	    	io.gamemachine.orm.models.PlayerSkill.open();
	    	List<io.gamemachine.orm.models.PlayerSkill> models = io.gamemachine.orm.models.PlayerSkill.findAll();
	    	List<PlayerSkill> messages = new ArrayList<PlayerSkill>();
	    	for (io.gamemachine.orm.models.PlayerSkill model : models) {
	    		PlayerSkill playerSkill = fromModel(model);
	    			    		messages.add(playerSkill);
	    	}
	    	io.gamemachine.orm.models.PlayerSkill.close();
	    	return messages;
	    }
	    
	    public List<PlayerSkill> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<PlayerSkill> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerSkill.open();
	    	}
	    	List<io.gamemachine.orm.models.PlayerSkill> models = io.gamemachine.orm.models.PlayerSkill.where(query, params);
	    	List<PlayerSkill> messages = new ArrayList<PlayerSkill>();
	    	for (io.gamemachine.orm.models.PlayerSkill model : models) {
	    		PlayerSkill playerSkill = fromModel(model);
	    			    		messages.add(playerSkill);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerSkill.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("player_skill_id",null);
    	    	    	    	    	    	model.set("player_skill_name",null);
    	    	    	    	    	    	    	    	    	model.set("player_skill_category",null);
    	    	    	    	    	    	model.set("player_skill_damage_type",null);
    	    	    	    	    	    	model.set("player_skill_icon_path",null);
    	    	    	    	    	    	model.set("player_skill_description",null);
    	    	    	    	    	    	model.set("player_skill_character_id",null);
    	    	    	    	    	    	model.set("player_skill_weapon_type",null);
    	    	    	    	    	    	model.set("player_skill_status_effect_id",null);
    	    	    	    	    	    	model.set("player_skill_level",null);
    	    	    	    	    	    	model.set("player_skill_is_combo_part",null);
    	    	    	    	    	    	model.set("player_skill_is_passive",null);
    	    	    	    	    	    	model.set("player_skill_skill_type",null);
    	    	    	    	    	    	model.set("player_skill_icon_uuid",null);
    	    	    	    	    	    	model.set("player_skill_status_effects",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("player_skill_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (name != null) {
    	       	    	model.setString("player_skill_name",name);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//model.setInteger("id",recordId);
    	
    	    	    	    	    	
    	    	    	//if (category != null) {
    	       	    	model.setString("player_skill_category",category);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (damageType != null) {
    	       	    	model.setString("player_skill_damage_type",damageType);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (icon_path != null) {
    	       	    	model.setString("player_skill_icon_path",icon_path);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (description != null) {
    	       	    	model.setString("player_skill_description",description);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (characterId != null) {
    	       	    	model.setString("player_skill_character_id",characterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (weaponType != null) {
    	       	    	model.setString("player_skill_weapon_type",weaponType);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (statusEffectId != null) {
    	       	    	model.setString("player_skill_status_effect_id",statusEffectId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (level != null) {
    	       	    	model.setInteger("player_skill_level",level);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (isComboPart != null) {
    	       	    	model.setInteger("player_skill_is_combo_part",isComboPart);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (isPassive != null) {
    	       	    	model.setInteger("player_skill_is_passive",isPassive);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (skillType != null) {
    	       	    	model.setString("player_skill_skill_type",skillType);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (icon_uuid != null) {
    	       	    	model.setString("player_skill_icon_uuid",icon_uuid);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (statusEffects != null) {
    	       	    	model.setString("player_skill_status_effects",statusEffects);
    	        		
    	//}
    	    	    }
    
	public static PlayerSkill fromModel(Model model) {
		boolean hasFields = false;
    	PlayerSkill message = new PlayerSkill();
    	    	    	    	    	
    	    			String idTestField = model.getString("player_skill_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String nameTestField = model.getString("player_skill_name");
		if (nameTestField != null) {
			String nameField = nameTestField;
			message.setName(nameField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    			String categoryTestField = model.getString("player_skill_category");
		if (categoryTestField != null) {
			String categoryField = categoryTestField;
			message.setCategory(categoryField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String damageTypeTestField = model.getString("player_skill_damage_type");
		if (damageTypeTestField != null) {
			String damageTypeField = damageTypeTestField;
			message.setDamageType(damageTypeField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String icon_pathTestField = model.getString("player_skill_icon_path");
		if (icon_pathTestField != null) {
			String icon_pathField = icon_pathTestField;
			message.setIcon_path(icon_pathField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String descriptionTestField = model.getString("player_skill_description");
		if (descriptionTestField != null) {
			String descriptionField = descriptionTestField;
			message.setDescription(descriptionField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String characterIdTestField = model.getString("player_skill_character_id");
		if (characterIdTestField != null) {
			String characterIdField = characterIdTestField;
			message.setCharacterId(characterIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String weaponTypeTestField = model.getString("player_skill_weapon_type");
		if (weaponTypeTestField != null) {
			String weaponTypeField = weaponTypeTestField;
			message.setWeaponType(weaponTypeField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String statusEffectIdTestField = model.getString("player_skill_status_effect_id");
		if (statusEffectIdTestField != null) {
			String statusEffectIdField = statusEffectIdTestField;
			message.setStatusEffectId(statusEffectIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer levelTestField = model.getInteger("player_skill_level");
		if (levelTestField != null) {
			int levelField = levelTestField;
			message.setLevel(levelField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer isComboPartTestField = model.getInteger("player_skill_is_combo_part");
		if (isComboPartTestField != null) {
			int isComboPartField = isComboPartTestField;
			message.setIsComboPart(isComboPartField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer isPassiveTestField = model.getInteger("player_skill_is_passive");
		if (isPassiveTestField != null) {
			int isPassiveField = isPassiveTestField;
			message.setIsPassive(isPassiveField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String skillTypeTestField = model.getString("player_skill_skill_type");
		if (skillTypeTestField != null) {
			String skillTypeField = skillTypeTestField;
			message.setSkillType(skillTypeField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String icon_uuidTestField = model.getString("player_skill_icon_uuid");
		if (icon_uuidTestField != null) {
			String icon_uuidField = icon_uuidTestField;
			message.setIcon_uuid(icon_uuidField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String statusEffectsTestField = model.getString("player_skill_status_effects");
		if (statusEffectsTestField != null) {
			String statusEffectsField = statusEffectsTestField;
			message.setStatusEffects(statusEffectsField);
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
	
	public PlayerSkill setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getName() {
		return name;
	}
	
	public PlayerSkill setName(String name) {
		this.name = name;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public PlayerSkill setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public String getCategory() {
		return category;
	}
	
	public PlayerSkill setCategory(String category) {
		this.category = category;
		return this;	}
	
		            
		public String getDamageType() {
		return damageType;
	}
	
	public PlayerSkill setDamageType(String damageType) {
		this.damageType = damageType;
		return this;	}
	
		            
		public String getIcon_path() {
		return icon_path;
	}
	
	public PlayerSkill setIcon_path(String icon_path) {
		this.icon_path = icon_path;
		return this;	}
	
		            
		public String getDescription() {
		return description;
	}
	
	public PlayerSkill setDescription(String description) {
		this.description = description;
		return this;	}
	
		            
		public String getCharacterId() {
		return characterId;
	}
	
	public PlayerSkill setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		            
		public String getWeaponType() {
		return weaponType;
	}
	
	public PlayerSkill setWeaponType(String weaponType) {
		this.weaponType = weaponType;
		return this;	}
	
		            
		public String getStatusEffectId() {
		return statusEffectId;
	}
	
	public PlayerSkill setStatusEffectId(String statusEffectId) {
		this.statusEffectId = statusEffectId;
		return this;	}
	
		            
		public int getLevel() {
		return level;
	}
	
	public PlayerSkill setLevel(int level) {
		this.level = level;
		return this;	}
	
		            
		public int getIsComboPart() {
		return isComboPart;
	}
	
	public PlayerSkill setIsComboPart(int isComboPart) {
		this.isComboPart = isComboPart;
		return this;	}
	
		            
		public int getIsPassive() {
		return isPassive;
	}
	
	public PlayerSkill setIsPassive(int isPassive) {
		this.isPassive = isPassive;
		return this;	}
	
		            
		public String getSkillType() {
		return skillType;
	}
	
	public PlayerSkill setSkillType(String skillType) {
		this.skillType = skillType;
		return this;	}
	
		            
		public String getIcon_uuid() {
		return icon_uuid;
	}
	
	public PlayerSkill setIcon_uuid(String icon_uuid) {
		this.icon_uuid = icon_uuid;
		return this;	}
	
		            
		public String getStatusEffects() {
		return statusEffects;
	}
	
	public PlayerSkill setStatusEffects(String statusEffects) {
		this.statusEffects = statusEffects;
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

    public Schema<PlayerSkill> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerSkill newMessage()
    {
        return new PlayerSkill();
    }

    public Class<PlayerSkill> typeClass()
    {
        return PlayerSkill.class;
    }

    public String messageName()
    {
        return PlayerSkill.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerSkill.class.getName();
    }

    public boolean isInitialized(PlayerSkill message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerSkill message) throws IOException
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
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.category = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.damageType = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.icon_path = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.description = input.readString();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.weaponType = input.readString();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.statusEffectId = input.readString();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.level = input.readInt32();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.isComboPart = input.readInt32();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.isPassive = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.skillType = input.readString();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.icon_uuid = input.readString();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.statusEffects = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerSkill message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.name != null) {
            output.writeString(2, message.name, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(3, message.recordId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.category != null) {
            output.writeString(4, message.category, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.damageType != null) {
            output.writeString(5, message.damageType, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.icon_path != null) {
            output.writeString(6, message.icon_path, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.description != null) {
            output.writeString(7, message.description, false);
        }
    	    	
    	            	
    	    	//if(message.characterId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.characterId != null) {
            output.writeString(10, message.characterId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.weaponType != null) {
            output.writeString(11, message.weaponType, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.statusEffectId != null) {
            output.writeString(13, message.statusEffectId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.level != null) {
            output.writeInt32(14, message.level, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.isComboPart != null) {
            output.writeInt32(16, message.isComboPart, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.isPassive != null) {
            output.writeInt32(17, message.isPassive, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.skillType != null) {
            output.writeString(18, message.skillType, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.icon_uuid != null) {
            output.writeString(19, message.icon_uuid, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.statusEffects != null) {
            output.writeString(20, message.statusEffects, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START PlayerSkill");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.name != null) {
    		System.out.println("name="+this.name);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	//if(this.category != null) {
    		System.out.println("category="+this.category);
    	//}
    	    	//if(this.damageType != null) {
    		System.out.println("damageType="+this.damageType);
    	//}
    	    	//if(this.icon_path != null) {
    		System.out.println("icon_path="+this.icon_path);
    	//}
    	    	//if(this.description != null) {
    		System.out.println("description="+this.description);
    	//}
    	    	//if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	//}
    	    	//if(this.weaponType != null) {
    		System.out.println("weaponType="+this.weaponType);
    	//}
    	    	//if(this.statusEffectId != null) {
    		System.out.println("statusEffectId="+this.statusEffectId);
    	//}
    	    	//if(this.level != null) {
    		System.out.println("level="+this.level);
    	//}
    	    	//if(this.isComboPart != null) {
    		System.out.println("isComboPart="+this.isComboPart);
    	//}
    	    	//if(this.isPassive != null) {
    		System.out.println("isPassive="+this.isPassive);
    	//}
    	    	//if(this.skillType != null) {
    		System.out.println("skillType="+this.skillType);
    	//}
    	    	//if(this.icon_uuid != null) {
    		System.out.println("icon_uuid="+this.icon_uuid);
    	//}
    	    	//if(this.statusEffects != null) {
    		System.out.println("statusEffects="+this.statusEffects);
    	//}
    	    	System.out.println("END PlayerSkill");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "name";
        	        	case 3: return "recordId";
        	        	case 4: return "category";
        	        	case 5: return "damageType";
        	        	case 6: return "icon_path";
        	        	case 7: return "description";
        	        	case 10: return "characterId";
        	        	case 11: return "weaponType";
        	        	case 13: return "statusEffectId";
        	        	case 14: return "level";
        	        	case 16: return "isComboPart";
        	        	case 17: return "isPassive";
        	        	case 18: return "skillType";
        	        	case 19: return "icon_uuid";
        	        	case 20: return "statusEffects";
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
    	    	__fieldMap.put("name", 2);
    	    	__fieldMap.put("recordId", 3);
    	    	__fieldMap.put("category", 4);
    	    	__fieldMap.put("damageType", 5);
    	    	__fieldMap.put("icon_path", 6);
    	    	__fieldMap.put("description", 7);
    	    	__fieldMap.put("characterId", 10);
    	    	__fieldMap.put("weaponType", 11);
    	    	__fieldMap.put("statusEffectId", 13);
    	    	__fieldMap.put("level", 14);
    	    	__fieldMap.put("isComboPart", 16);
    	    	__fieldMap.put("isPassive", 17);
    	    	__fieldMap.put("skillType", 18);
    	    	__fieldMap.put("icon_uuid", 19);
    	    	__fieldMap.put("statusEffects", 20);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerSkill.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerSkill parseFrom(byte[] bytes) {
	PlayerSkill message = new PlayerSkill();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerSkill.getSchema());
	return message;
}

public static PlayerSkill parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerSkill message = new PlayerSkill();
	JsonIOUtil.mergeFrom(bytes, message, PlayerSkill.getSchema(), false);
	return message;
}

public PlayerSkill clone() {
	byte[] bytes = this.toByteArray();
	PlayerSkill playerSkill = PlayerSkill.parseFrom(bytes);
	return playerSkill;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerSkill.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerSkill> schema = PlayerSkill.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerSkill.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, PlayerSkill.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
