
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
public final class PlayerSkill implements Externalizable, Message<PlayerSkill>, Schema<PlayerSkill>, PersistableMessage{

	public enum DamageType implements io.protostuff.EnumLite<DamageType>
    {
    	
    	    	Aoe(0),    	    	SingleTarget(1),    	    	Pbaoe(2),    	    	SelfAoe(3);    	        
        public final int number;
        
        private DamageType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static DamageType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (Aoe);
    			    			case 1: return (SingleTarget);
    			    			case 2: return (Pbaoe);
    			    			case 3: return (SelfAoe);
    			                default: return null;
            }
        }
    }
	public enum SkillType implements io.protostuff.EnumLite<SkillType>
    {
    	
    	    	Active(0),    	    	Passive(1);    	        
        public final int number;
        
        private SkillType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static SkillType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (Active);
    			    			case 1: return (Passive);
    			                default: return null;
            }
        }
    }
	public enum Resource implements io.protostuff.EnumLite<Resource>
    {
    	
    	    	NoResource(0),    	    	Magic(1),    	    	Stamina(2);    	        
        public final int number;
        
        private Resource (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Resource valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NoResource);
    			    			case 1: return (Magic);
    			    			case 2: return (Stamina);
    			                default: return null;
            }
        }
    }
	public enum Category implements io.protostuff.EnumLite<Category>
    {
    	
    	    	Weapon(0),    	    	Crafting(1),    	    	CategoryOther(2);    	        
        public final int number;
        
        private Category (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Category valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (Weapon);
    			    			case 1: return (Crafting);
    			    			case 2: return (CategoryOther);
    			                default: return null;
            }
        }
    }
	public enum WeaponType implements io.protostuff.EnumLite<WeaponType>
    {
    	
    	    	Bow(0),    	    	Sword2h(1),    	    	Sword1h(2),    	    	Staff(3),    	    	Gun(4),    	    	Siege(5),    	    	WeaponTypeOther(6);    	        
        public final int number;
        
        private WeaponType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static WeaponType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (Bow);
    			    			case 1: return (Sword2h);
    			    			case 2: return (Sword1h);
    			    			case 3: return (Staff);
    			    			case 4: return (Gun);
    			    			case 5: return (Siege);
    			    			case 6: return (WeaponTypeOther);
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

    			public String id;
	    
        			public String name;
	    
        			public Integer recordId;
	    
        			public String category;
	    
        			public String damageType;
	    
        			public String icon_path;
	    
        			public String description;
	    
        			public String resource;
	    
        			public Integer resourceCost;
	    
        			public String characterId;
	    
        			public String weaponType;
	    
        			public Integer range;
	    
        			public String statusEffectId;
	    
        			public Integer level;
	    
        			public Integer resourceCostPerTick;
	    
        			public Integer isComboPart;
	    
        			public Integer isPassive;
	    
        			public String skillType;
	    
        			public String icon_uuid;
	    
        
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
	    	if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.PlayerSkill.findFirst("id = ?", message.recordId);
	    	}
	    	
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
	    		dbErrors = model.errors();
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
    	    	    	    	    	    	model.set("player_skill_resource",null);
    	    	    	    	    	    	model.set("player_skill_resource_cost",null);
    	    	    	    	    	    	model.set("player_skill_character_id",null);
    	    	    	    	    	    	model.set("player_skill_weapon_type",null);
    	    	    	    	    	    	model.set("player_skill_range",null);
    	    	    	    	    	    	model.set("player_skill_status_effect_id",null);
    	    	    	    	    	    	model.set("player_skill_level",null);
    	    	    	    	    	    	model.set("player_skill_resource_cost_per_tick",null);
    	    	    	    	    	    	model.set("player_skill_is_combo_part",null);
    	    	    	    	    	    	model.set("player_skill_is_passive",null);
    	    	    	    	    	    	model.set("player_skill_skill_type",null);
    	    	    	    	    	    	model.set("player_skill_icon_uuid",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("player_skill_id",id);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (name != null) {
    	       	    	model.setString("player_skill_name",name);
    	        		
    	}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	if (category != null) {
    	       	    	model.setString("player_skill_category",category);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (damageType != null) {
    	       	    	model.setString("player_skill_damage_type",damageType);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (icon_path != null) {
    	       	    	model.setString("player_skill_icon_path",icon_path);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (description != null) {
    	       	    	model.setString("player_skill_description",description);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (resource != null) {
    	       	    	model.setString("player_skill_resource",resource);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (resourceCost != null) {
    	       	    	model.setInteger("player_skill_resource_cost",resourceCost);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (characterId != null) {
    	       	    	model.setString("player_skill_character_id",characterId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (weaponType != null) {
    	       	    	model.setString("player_skill_weapon_type",weaponType);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (range != null) {
    	       	    	model.setInteger("player_skill_range",range);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (statusEffectId != null) {
    	       	    	model.setString("player_skill_status_effect_id",statusEffectId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (level != null) {
    	       	    	model.setInteger("player_skill_level",level);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (resourceCostPerTick != null) {
    	       	    	model.setInteger("player_skill_resource_cost_per_tick",resourceCostPerTick);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (isComboPart != null) {
    	       	    	model.setInteger("player_skill_is_combo_part",isComboPart);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (isPassive != null) {
    	       	    	model.setInteger("player_skill_is_passive",isPassive);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (skillType != null) {
    	       	    	model.setString("player_skill_skill_type",skillType);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (icon_uuid != null) {
    	       	    	model.setString("player_skill_icon_uuid",icon_uuid);
    	        		
    	}
    	    	    }
    
	public static PlayerSkill fromModel(Model model) {
		boolean hasFields = false;
    	PlayerSkill message = new PlayerSkill();
    	    	    	    	    	
    	    	    	String idField = model.getString("player_skill_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String nameField = model.getString("player_skill_name");
    	    	
    	if (nameField != null) {
    		message.setName(nameField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String categoryField = model.getString("player_skill_category");
    	    	
    	if (categoryField != null) {
    		message.setCategory(categoryField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String damageTypeField = model.getString("player_skill_damage_type");
    	    	
    	if (damageTypeField != null) {
    		message.setDamageType(damageTypeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String icon_pathField = model.getString("player_skill_icon_path");
    	    	
    	if (icon_pathField != null) {
    		message.setIcon_path(icon_pathField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String descriptionField = model.getString("player_skill_description");
    	    	
    	if (descriptionField != null) {
    		message.setDescription(descriptionField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String resourceField = model.getString("player_skill_resource");
    	    	
    	if (resourceField != null) {
    		message.setResource(resourceField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer resourceCostField = model.getInteger("player_skill_resource_cost");
    	    	
    	if (resourceCostField != null) {
    		message.setResourceCost(resourceCostField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String characterIdField = model.getString("player_skill_character_id");
    	    	
    	if (characterIdField != null) {
    		message.setCharacterId(characterIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String weaponTypeField = model.getString("player_skill_weapon_type");
    	    	
    	if (weaponTypeField != null) {
    		message.setWeaponType(weaponTypeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer rangeField = model.getInteger("player_skill_range");
    	    	
    	if (rangeField != null) {
    		message.setRange(rangeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String statusEffectIdField = model.getString("player_skill_status_effect_id");
    	    	
    	if (statusEffectIdField != null) {
    		message.setStatusEffectId(statusEffectIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer levelField = model.getInteger("player_skill_level");
    	    	
    	if (levelField != null) {
    		message.setLevel(levelField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer resourceCostPerTickField = model.getInteger("player_skill_resource_cost_per_tick");
    	    	
    	if (resourceCostPerTickField != null) {
    		message.setResourceCostPerTick(resourceCostPerTickField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer isComboPartField = model.getInteger("player_skill_is_combo_part");
    	    	
    	if (isComboPartField != null) {
    		message.setIsComboPart(isComboPartField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer isPassiveField = model.getInteger("player_skill_is_passive");
    	    	
    	if (isPassiveField != null) {
    		message.setIsPassive(isPassiveField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String skillTypeField = model.getString("player_skill_skill_type");
    	    	
    	if (skillTypeField != null) {
    		message.setSkillType(skillTypeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String icon_uuidField = model.getString("player_skill_icon_uuid");
    	    	
    	if (icon_uuidField != null) {
    		message.setIcon_uuid(icon_uuidField);
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
	
	public PlayerSkill setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public PlayerSkill setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public PlayerSkill setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasCategory()  {
        return category == null ? false : true;
    }
        
		public String getCategory() {
		return category;
	}
	
	public PlayerSkill setCategory(String category) {
		this.category = category;
		return this;	}
	
		    
    public Boolean hasDamageType()  {
        return damageType == null ? false : true;
    }
        
		public String getDamageType() {
		return damageType;
	}
	
	public PlayerSkill setDamageType(String damageType) {
		this.damageType = damageType;
		return this;	}
	
		    
    public Boolean hasIcon_path()  {
        return icon_path == null ? false : true;
    }
        
		public String getIcon_path() {
		return icon_path;
	}
	
	public PlayerSkill setIcon_path(String icon_path) {
		this.icon_path = icon_path;
		return this;	}
	
		    
    public Boolean hasDescription()  {
        return description == null ? false : true;
    }
        
		public String getDescription() {
		return description;
	}
	
	public PlayerSkill setDescription(String description) {
		this.description = description;
		return this;	}
	
		    
    public Boolean hasResource()  {
        return resource == null ? false : true;
    }
        
		public String getResource() {
		return resource;
	}
	
	public PlayerSkill setResource(String resource) {
		this.resource = resource;
		return this;	}
	
		    
    public Boolean hasResourceCost()  {
        return resourceCost == null ? false : true;
    }
        
		public Integer getResourceCost() {
		return resourceCost;
	}
	
	public PlayerSkill setResourceCost(Integer resourceCost) {
		this.resourceCost = resourceCost;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public PlayerSkill setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasWeaponType()  {
        return weaponType == null ? false : true;
    }
        
		public String getWeaponType() {
		return weaponType;
	}
	
	public PlayerSkill setWeaponType(String weaponType) {
		this.weaponType = weaponType;
		return this;	}
	
		    
    public Boolean hasRange()  {
        return range == null ? false : true;
    }
        
		public Integer getRange() {
		return range;
	}
	
	public PlayerSkill setRange(Integer range) {
		this.range = range;
		return this;	}
	
		    
    public Boolean hasStatusEffectId()  {
        return statusEffectId == null ? false : true;
    }
        
		public String getStatusEffectId() {
		return statusEffectId;
	}
	
	public PlayerSkill setStatusEffectId(String statusEffectId) {
		this.statusEffectId = statusEffectId;
		return this;	}
	
		    
    public Boolean hasLevel()  {
        return level == null ? false : true;
    }
        
		public Integer getLevel() {
		return level;
	}
	
	public PlayerSkill setLevel(Integer level) {
		this.level = level;
		return this;	}
	
		    
    public Boolean hasResourceCostPerTick()  {
        return resourceCostPerTick == null ? false : true;
    }
        
		public Integer getResourceCostPerTick() {
		return resourceCostPerTick;
	}
	
	public PlayerSkill setResourceCostPerTick(Integer resourceCostPerTick) {
		this.resourceCostPerTick = resourceCostPerTick;
		return this;	}
	
		    
    public Boolean hasIsComboPart()  {
        return isComboPart == null ? false : true;
    }
        
		public Integer getIsComboPart() {
		return isComboPart;
	}
	
	public PlayerSkill setIsComboPart(Integer isComboPart) {
		this.isComboPart = isComboPart;
		return this;	}
	
		    
    public Boolean hasIsPassive()  {
        return isPassive == null ? false : true;
    }
        
		public Integer getIsPassive() {
		return isPassive;
	}
	
	public PlayerSkill setIsPassive(Integer isPassive) {
		this.isPassive = isPassive;
		return this;	}
	
		    
    public Boolean hasSkillType()  {
        return skillType == null ? false : true;
    }
        
		public String getSkillType() {
		return skillType;
	}
	
	public PlayerSkill setSkillType(String skillType) {
		this.skillType = skillType;
		return this;	}
	
		    
    public Boolean hasIcon_uuid()  {
        return icon_uuid == null ? false : true;
    }
        
		public String getIcon_uuid() {
		return icon_uuid;
	}
	
	public PlayerSkill setIcon_uuid(String icon_uuid) {
		this.icon_uuid = icon_uuid;
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
                	                	
                            	            	case 8:
            	                	                	message.resource = input.readString();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.resourceCost = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.weaponType = input.readString();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.range = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.statusEffectId = input.readString();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.level = input.readInt32();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.resourceCostPerTick = input.readInt32();
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
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerSkill message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.name != null)
            output.writeString(2, message.name, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(3, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.category != null)
            output.writeString(4, message.category, false);
    	    	
    	            	
    	    	
    	    	    	if(message.damageType != null)
            output.writeString(5, message.damageType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_path != null)
            output.writeString(6, message.icon_path, false);
    	    	
    	            	
    	    	
    	    	    	if(message.description != null)
            output.writeString(7, message.description, false);
    	    	
    	            	
    	    	
    	    	    	if(message.resource != null)
            output.writeString(8, message.resource, false);
    	    	
    	            	
    	    	
    	    	    	if(message.resourceCost != null)
            output.writeInt32(9, message.resourceCost, false);
    	    	
    	            	
    	    	if(message.characterId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(10, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.weaponType != null)
            output.writeString(11, message.weaponType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.range != null)
            output.writeInt32(12, message.range, false);
    	    	
    	            	
    	    	
    	    	    	if(message.statusEffectId != null)
            output.writeString(13, message.statusEffectId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.level != null)
            output.writeInt32(14, message.level, false);
    	    	
    	            	
    	    	
    	    	    	if(message.resourceCostPerTick != null)
            output.writeInt32(15, message.resourceCostPerTick, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isComboPart != null)
            output.writeInt32(16, message.isComboPart, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isPassive != null)
            output.writeInt32(17, message.isPassive, false);
    	    	
    	            	
    	    	
    	    	    	if(message.skillType != null)
            output.writeString(18, message.skillType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_uuid != null)
            output.writeString(19, message.icon_uuid, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START PlayerSkill");
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.name != null) {
    		System.out.println("name="+this.name);
    	}
    	    	if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	}
    	    	if(this.category != null) {
    		System.out.println("category="+this.category);
    	}
    	    	if(this.damageType != null) {
    		System.out.println("damageType="+this.damageType);
    	}
    	    	if(this.icon_path != null) {
    		System.out.println("icon_path="+this.icon_path);
    	}
    	    	if(this.description != null) {
    		System.out.println("description="+this.description);
    	}
    	    	if(this.resource != null) {
    		System.out.println("resource="+this.resource);
    	}
    	    	if(this.resourceCost != null) {
    		System.out.println("resourceCost="+this.resourceCost);
    	}
    	    	if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	}
    	    	if(this.weaponType != null) {
    		System.out.println("weaponType="+this.weaponType);
    	}
    	    	if(this.range != null) {
    		System.out.println("range="+this.range);
    	}
    	    	if(this.statusEffectId != null) {
    		System.out.println("statusEffectId="+this.statusEffectId);
    	}
    	    	if(this.level != null) {
    		System.out.println("level="+this.level);
    	}
    	    	if(this.resourceCostPerTick != null) {
    		System.out.println("resourceCostPerTick="+this.resourceCostPerTick);
    	}
    	    	if(this.isComboPart != null) {
    		System.out.println("isComboPart="+this.isComboPart);
    	}
    	    	if(this.isPassive != null) {
    		System.out.println("isPassive="+this.isPassive);
    	}
    	    	if(this.skillType != null) {
    		System.out.println("skillType="+this.skillType);
    	}
    	    	if(this.icon_uuid != null) {
    		System.out.println("icon_uuid="+this.icon_uuid);
    	}
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
        	        	case 8: return "resource";
        	        	case 9: return "resourceCost";
        	        	case 10: return "characterId";
        	        	case 11: return "weaponType";
        	        	case 12: return "range";
        	        	case 13: return "statusEffectId";
        	        	case 14: return "level";
        	        	case 15: return "resourceCostPerTick";
        	        	case 16: return "isComboPart";
        	        	case 17: return "isPassive";
        	        	case 18: return "skillType";
        	        	case 19: return "icon_uuid";
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
    	    	__fieldMap.put("resource", 8);
    	    	__fieldMap.put("resourceCost", 9);
    	    	__fieldMap.put("characterId", 10);
    	    	__fieldMap.put("weaponType", 11);
    	    	__fieldMap.put("range", 12);
    	    	__fieldMap.put("statusEffectId", 13);
    	    	__fieldMap.put("level", 14);
    	    	__fieldMap.put("resourceCostPerTick", 15);
    	    	__fieldMap.put("isComboPart", 16);
    	    	__fieldMap.put("isPassive", 17);
    	    	__fieldMap.put("skillType", 18);
    	    	__fieldMap.put("icon_uuid", 19);
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
