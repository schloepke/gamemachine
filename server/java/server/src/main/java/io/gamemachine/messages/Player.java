
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
public final class Player implements Externalizable, Message<Player>, Schema<Player>, PersistableMessage{



    public static Schema<Player> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Player getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Player DEFAULT_INSTANCE = new Player();
    static final String defaultScope = Player.class.getSimpleName();

    			public String id;
	    
        			public Boolean authenticated;
	    
        			public Integer authtoken;
	    
        			public String passwordHash;
	    
        			public String gameId;
	    
        			public Integer recordId;
	    
        			public String role;
	    
        			public Boolean locked;
	    
        			public Integer ip;
	    
        			public Long ipChangedAt;
	    
        			public String characterId;
	    
            public List<Character> characters;
	    
	public static PlayerCache cache() {
		return PlayerCache.getInstance();
	}
	
	public static PlayerStore store() {
		return PlayerStore.getInstance();
	}


    public Player()
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
		
		public Player result(int timeout) {
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
	
	public static class PlayerCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Player> cache = new Cache<String, Player>(120, 5000);
		
		private PlayerCache() {
		}
		
		private static class LazyHolder {
			private static final PlayerCache INSTANCE = new PlayerCache();
		}
	
		public static PlayerCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Player>(expiration, size);
		}
	
		public Cache<String, Player> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(PlayerCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Player message) {
			CacheUpdate cacheUpdate = new CacheUpdate(PlayerCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Player get(String id, int timeout) {
			Player message = cache.get(id);
			if (message == null) {
				message = Player.store().get(id, timeout);
			}
			return message;
		}
			
		public static Player setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Player message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Player) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Player.store().set(message);
			} else {
				message = Player.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Player.class.getField(field));
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
				Player.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class PlayerStore {
	
		private PlayerStore() {
		}
		
		private static class LazyHolder {
			private static final PlayerStore INSTANCE = new PlayerStore();
		}
	
		public static PlayerStore getInstance() {
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
		
	    public void set(Player message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Player get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Player message) {
	    	Player clone = message.clone();
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
			
		public Player get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Player");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Player message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Player) {
					message = (Player)result;
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
	

	

	public static PlayerDb db() {
		return PlayerDb.getInstance();
	}
	
	public interface PlayerAsyncDb {
		void save(Player message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class PlayerAsyncDbImpl implements PlayerAsyncDb {
	
		public void save(Player message) {
			Player.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	Player.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	Player.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class PlayerDb {
	
		public Errors dbErrors;
		private PlayerAsyncDb asyncDb = null;
		
		private PlayerDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<PlayerAsyncDbImpl>(PlayerAsyncDb.class, PlayerAsyncDbImpl.class));
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
			private static final PlayerDb INSTANCE = new PlayerDb();
		}
	
		public static PlayerDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(Player message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(Player message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(Player message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Player.open();
	    	}
	    	
	    	io.gamemachine.orm.models.Player model = null;
	    	if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.Player.findFirst("id = ?", message.recordId);
	    	}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.Player();
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
	    		io.gamemachine.orm.models.Player.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.Player.open();
	    	int deleted = io.gamemachine.orm.models.Player.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.Player.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.Player.open();
	    	int deleted = io.gamemachine.orm.models.Player.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.Player.close();
	    	return result;
	    }
	    
	    public Player find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public Player find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Player.open();
	    	}
	    	
	    	io.gamemachine.orm.models.Player model = io.gamemachine.orm.models.Player.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Player.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Player player = fromModel(model);
	    			    		return player;
	    	}
	    }
	    
	    public Player findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.Player.open();
	    	io.gamemachine.orm.models.Player model = io.gamemachine.orm.models.Player.findFirst(query, params);
	    	io.gamemachine.orm.models.Player.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Player player = fromModel(model);
	    			    		return player;
	    	}
	    }
	    
	    public List<Player> findAll() {
	    	io.gamemachine.orm.models.Player.open();
	    	List<io.gamemachine.orm.models.Player> models = io.gamemachine.orm.models.Player.findAll();
	    	List<Player> messages = new ArrayList<Player>();
	    	for (io.gamemachine.orm.models.Player model : models) {
	    		Player player = fromModel(model);
	    			    		messages.add(player);
	    	}
	    	io.gamemachine.orm.models.Player.close();
	    	return messages;
	    }
	    
	    public List<Player> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<Player> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Player.open();
	    	}
	    	List<io.gamemachine.orm.models.Player> models = io.gamemachine.orm.models.Player.where(query, params);
	    	List<Player> messages = new ArrayList<Player>();
	    	for (io.gamemachine.orm.models.Player model : models) {
	    		Player player = fromModel(model);
	    			    		messages.add(player);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.Player.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("player_id",null);
    	    	    	    	    	    	model.set("player_authenticated",null);
    	    	    	    	    	    	model.set("player_authtoken",null);
    	    	    	    	    	    	model.set("player_password_hash",null);
    	    	    	    	    	    	model.set("player_game_id",null);
    	    	    	    	    	    	    	    	    	model.set("player_role",null);
    	    	    	    	    	    	model.set("player_locked",null);
    	    	    	    	    	    	model.set("player_ip",null);
    	    	    	    	    	    	model.set("player_ip_changed_at",null);
    	    	    	    	    	    	model.set("player_character_id",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("player_id",id);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (authenticated != null) {
    	       	    	model.setBoolean("player_authenticated",authenticated);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (authtoken != null) {
    	       	    	model.setInteger("player_authtoken",authtoken);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (passwordHash != null) {
    	       	    	model.setString("player_password_hash",passwordHash);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (gameId != null) {
    	       	    	model.setString("player_game_id",gameId);
    	        		
    	}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	if (role != null) {
    	       	    	model.setString("player_role",role);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (locked != null) {
    	       	    	model.setBoolean("player_locked",locked);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (ip != null) {
    	       	    	model.setInteger("player_ip",ip);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (ipChangedAt != null) {
    	       	    	model.setLong("player_ip_changed_at",ipChangedAt);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (characterId != null) {
    	       	    	model.setString("player_character_id",characterId);
    	        		
    	}
    	    	    	    }
    
	public static Player fromModel(Model model) {
		boolean hasFields = false;
    	Player message = new Player();
    	    	    	    	    	
    	    	    	String idField = model.getString("player_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean authenticatedField = model.getBoolean("player_authenticated");
    	    	
    	if (authenticatedField != null) {
    		message.setAuthenticated(authenticatedField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer authtokenField = model.getInteger("player_authtoken");
    	    	
    	if (authtokenField != null) {
    		message.setAuthtoken(authtokenField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String passwordHashField = model.getString("player_password_hash");
    	    	
    	if (passwordHashField != null) {
    		message.setPasswordHash(passwordHashField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String gameIdField = model.getString("player_game_id");
    	    	
    	if (gameIdField != null) {
    		message.setGameId(gameIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String roleField = model.getString("player_role");
    	    	
    	if (roleField != null) {
    		message.setRole(roleField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean lockedField = model.getBoolean("player_locked");
    	    	
    	if (lockedField != null) {
    		message.setLocked(lockedField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer ipField = model.getInteger("player_ip");
    	    	
    	if (ipField != null) {
    		message.setIp(ipField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Long ipChangedAtField = model.getLong("player_ip_changed_at");
    	    	
    	if (ipChangedAtField != null) {
    		message.setIpChangedAt(ipChangedAtField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String characterIdField = model.getString("player_character_id");
    	    	
    	if (characterIdField != null) {
    		message.setCharacterId(characterIdField);
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
	
	public Player setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasAuthenticated()  {
        return authenticated == null ? false : true;
    }
        
		public Boolean getAuthenticated() {
		return authenticated;
	}
	
	public Player setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
		return this;	}
	
		    
    public Boolean hasAuthtoken()  {
        return authtoken == null ? false : true;
    }
        
		public Integer getAuthtoken() {
		return authtoken;
	}
	
	public Player setAuthtoken(Integer authtoken) {
		this.authtoken = authtoken;
		return this;	}
	
		    
    public Boolean hasPasswordHash()  {
        return passwordHash == null ? false : true;
    }
        
		public String getPasswordHash() {
		return passwordHash;
	}
	
	public Player setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		return this;	}
	
		    
    public Boolean hasGameId()  {
        return gameId == null ? false : true;
    }
        
		public String getGameId() {
		return gameId;
	}
	
	public Player setGameId(String gameId) {
		this.gameId = gameId;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public Player setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasRole()  {
        return role == null ? false : true;
    }
        
		public String getRole() {
		return role;
	}
	
	public Player setRole(String role) {
		this.role = role;
		return this;	}
	
		    
    public Boolean hasLocked()  {
        return locked == null ? false : true;
    }
        
		public Boolean getLocked() {
		return locked;
	}
	
	public Player setLocked(Boolean locked) {
		this.locked = locked;
		return this;	}
	
		    
    public Boolean hasIp()  {
        return ip == null ? false : true;
    }
        
		public Integer getIp() {
		return ip;
	}
	
	public Player setIp(Integer ip) {
		this.ip = ip;
		return this;	}
	
		    
    public Boolean hasIpChangedAt()  {
        return ipChangedAt == null ? false : true;
    }
        
		public Long getIpChangedAt() {
		return ipChangedAt;
	}
	
	public Player setIpChangedAt(Long ipChangedAt) {
		this.ipChangedAt = ipChangedAt;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public Player setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasCharacters()  {
        return characters == null ? false : true;
    }
        
		public List<Character> getCharactersList() {
		if(this.characters == null)
            this.characters = new ArrayList<Character>();
		return characters;
	}

	public Player setCharactersList(List<Character> characters) {
		this.characters = characters;
		return this;
	}

	public Character getCharacters(int index)  {
        return characters == null ? null : characters.get(index);
    }

    public int getCharactersCount()  {
        return characters == null ? 0 : characters.size();
    }

    public Player addCharacters(Character characters)  {
        if(this.characters == null)
            this.characters = new ArrayList<Character>();
        this.characters.add(characters);
        return this;
    }
            	    	    	    	
    public Player removeCharactersById(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByUmaData(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.umaData.equals(obj.umaData)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByHealth(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.health.equals(obj.health)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByRecordId(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByPlayerId(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.playerId.equals(obj.playerId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByPart(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.part.equals(obj.part)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByParts(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.parts.equals(obj.parts)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByWorldx(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldx.equals(obj.worldx)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByWorldy(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldy.equals(obj.worldy)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByWorldz(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldz.equals(obj.worldz)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByZone(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.zone.equals(obj.zone)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByStamina(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.stamina.equals(obj.stamina)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByMagic(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.magic.equals(obj.magic)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Player removeCharactersByIncludeUmaData(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.includeUmaData.equals(obj.includeUmaData)) {
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

    public Schema<Player> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Player newMessage()
    {
        return new Player();
    }

    public Class<Player> typeClass()
    {
        return Player.class;
    }

    public String messageName()
    {
        return Player.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Player.class.getName();
    }

    public boolean isInitialized(Player message)
    {
        return true;
    }

    public void mergeFrom(Input input, Player message) throws IOException
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
            	                	                	message.authenticated = input.readBool();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.authtoken = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.passwordHash = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.gameId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.role = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.locked = input.readBool();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.ip = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.ipChangedAt = input.readInt64();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 12:
            	            		if(message.characters == null)
                        message.characters = new ArrayList<Character>();
                                        message.characters.add(input.mergeObject(null, Character.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Player message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.authenticated != null)
            output.writeBool(2, message.authenticated, false);
    	    	
    	            	
    	    	
    	    	    	if(message.authtoken != null)
            output.writeInt32(3, message.authtoken, false);
    	    	
    	            	
    	    	
    	    	    	if(message.passwordHash != null)
            output.writeString(4, message.passwordHash, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gameId != null)
            output.writeString(5, message.gameId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(6, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.role != null)
            output.writeString(7, message.role, false);
    	    	
    	            	
    	    	
    	    	    	if(message.locked != null)
            output.writeBool(8, message.locked, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ip != null)
            output.writeInt32(9, message.ip, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ipChangedAt != null)
            output.writeInt64(10, message.ipChangedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(11, message.characterId, false);
    	    	
    	            	
    	    	
    	    	if(message.characters != null)
        {
            for(Character characters : message.characters)
            {
                if(characters != null) {
                   	    				output.writeObject(12, characters, Character.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Player");
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.authenticated != null) {
    		System.out.println("authenticated="+this.authenticated);
    	}
    	    	if(this.authtoken != null) {
    		System.out.println("authtoken="+this.authtoken);
    	}
    	    	if(this.passwordHash != null) {
    		System.out.println("passwordHash="+this.passwordHash);
    	}
    	    	if(this.gameId != null) {
    		System.out.println("gameId="+this.gameId);
    	}
    	    	if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	}
    	    	if(this.role != null) {
    		System.out.println("role="+this.role);
    	}
    	    	if(this.locked != null) {
    		System.out.println("locked="+this.locked);
    	}
    	    	if(this.ip != null) {
    		System.out.println("ip="+this.ip);
    	}
    	    	if(this.ipChangedAt != null) {
    		System.out.println("ipChangedAt="+this.ipChangedAt);
    	}
    	    	if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	}
    	    	if(this.characters != null) {
    		System.out.println("characters="+this.characters);
    	}
    	    	System.out.println("END Player");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "authenticated";
        	        	case 3: return "authtoken";
        	        	case 4: return "passwordHash";
        	        	case 5: return "gameId";
        	        	case 6: return "recordId";
        	        	case 7: return "role";
        	        	case 8: return "locked";
        	        	case 9: return "ip";
        	        	case 10: return "ipChangedAt";
        	        	case 11: return "characterId";
        	        	case 12: return "characters";
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
    	    	__fieldMap.put("authenticated", 2);
    	    	__fieldMap.put("authtoken", 3);
    	    	__fieldMap.put("passwordHash", 4);
    	    	__fieldMap.put("gameId", 5);
    	    	__fieldMap.put("recordId", 6);
    	    	__fieldMap.put("role", 7);
    	    	__fieldMap.put("locked", 8);
    	    	__fieldMap.put("ip", 9);
    	    	__fieldMap.put("ipChangedAt", 10);
    	    	__fieldMap.put("characterId", 11);
    	    	__fieldMap.put("characters", 12);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Player.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Player parseFrom(byte[] bytes) {
	Player message = new Player();
	ProtobufIOUtil.mergeFrom(bytes, message, Player.getSchema());
	return message;
}

public static Player parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Player message = new Player();
	JsonIOUtil.mergeFrom(bytes, message, Player.getSchema(), false);
	return message;
}

public Player clone() {
	byte[] bytes = this.toByteArray();
	Player player = Player.parseFrom(bytes);
	return player;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Player.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Player> schema = Player.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Player.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Player.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
