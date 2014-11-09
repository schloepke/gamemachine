
package GameMachine.Messages;

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

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import com.game_machine.util.LocalLinkedBuffer;

import java.nio.charset.Charset;

import java.lang.reflect.Field;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.pattern.AskableActorSelection;

import akka.util.Timeout;
import java.util.concurrent.TimeUnit;

import com.game_machine.core.GameMachineLoader;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.actor.ActorSystem;
import org.javalite.activejdbc.Errors;

import com.game_machine.core.ActorUtil;

import org.javalite.activejdbc.Model;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

import com.game_machine.core.PersistableMessage;

import com.game_machine.objectdb.Cache;
import com.game_machine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class Player implements Externalizable, Message<Player>, Schema<Player>, PersistableMessage
{

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

		public String authtoken;

		public String passwordHash;

		public String gameId;

		public Integer recordId;

		public String role;

		public Boolean locked;

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
			Player message;
			Future<Object> future = askable.ask(get,t);
			try {
				message = (Player) Await.result(future, t.duration());
			} catch (Exception e) {
				return null;
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
	    		com.game_machine.orm.models.Player.open();
	    	}
	    	
	    	com.game_machine.orm.models.Player model = null;
	    	if (message.hasRecordId()) {
	    		model = com.game_machine.orm.models.Player.findFirst("id = ?", message.recordId);
	    	}
	    	
	    	if (model == null) {
	    		model = new com.game_machine.orm.models.Player();
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
	    		com.game_machine.orm.models.Player.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	com.game_machine.orm.models.Player.open();
	    	int deleted = com.game_machine.orm.models.Player.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	com.game_machine.orm.models.Player.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	com.game_machine.orm.models.Player.open();
	    	int deleted = com.game_machine.orm.models.Player.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	com.game_machine.orm.models.Player.close();
	    	return result;
	    }
	    
	    public Player find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public Player find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.Player.open();
	    	}
	    	
	    	com.game_machine.orm.models.Player model = com.game_machine.orm.models.Player.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.Player.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Player player = fromModel(model);
	    		
	    		return player;
	    	}
	    }
	    
	    public Player findFirst(String query, Object ... params) {
	    	com.game_machine.orm.models.Player.open();
	    	com.game_machine.orm.models.Player model = com.game_machine.orm.models.Player.findFirst(query, params);
	    	com.game_machine.orm.models.Player.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		Player player = fromModel(model);
	    		
	    		return player;
	    	}
	    }
	    
	    public List<Player> findAll() {
	    	com.game_machine.orm.models.Player.open();
	    	List<com.game_machine.orm.models.Player> models = com.game_machine.orm.models.Player.findAll();
	    	List<Player> messages = new ArrayList<Player>();
	    	for (com.game_machine.orm.models.Player model : models) {
	    		Player player = fromModel(model);
	    		
	    		messages.add(player);
	    	}
	    	com.game_machine.orm.models.Player.close();
	    	return messages;
	    }
	    
	    public List<Player> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<Player> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.Player.open();
	    	}
	    	List<com.game_machine.orm.models.Player> models = com.game_machine.orm.models.Player.where(query, params);
	    	List<Player> messages = new ArrayList<Player>();
	    	for (com.game_machine.orm.models.Player model : models) {
	    		Player player = fromModel(model);
	    		
	    		messages.add(player);
	    	}
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.Player.close();
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
    	
    }
    
	public void toModel(Model model) {

    	if (id != null) {
    		model.setString("player_id",id);
    	}

    	if (authenticated != null) {
    		model.setBoolean("player_authenticated",authenticated);
    	}

    	if (authtoken != null) {
    		model.setString("player_authtoken",authtoken);
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

    	String authtokenField = model.getString("player_authtoken");
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
		return this;
	}

    public Boolean hasAuthenticated()  {
        return authenticated == null ? false : true;
    }

	public Boolean getAuthenticated() {
		return authenticated;
	}
	
	public Player setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
		return this;
	}

    public Boolean hasAuthtoken()  {
        return authtoken == null ? false : true;
    }

	public String getAuthtoken() {
		return authtoken;
	}
	
	public Player setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
		return this;
	}

    public Boolean hasPasswordHash()  {
        return passwordHash == null ? false : true;
    }

	public String getPasswordHash() {
		return passwordHash;
	}
	
	public Player setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		return this;
	}

    public Boolean hasGameId()  {
        return gameId == null ? false : true;
    }

	public String getGameId() {
		return gameId;
	}
	
	public Player setGameId(String gameId) {
		this.gameId = gameId;
		return this;
	}

    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }

	public Integer getRecordId() {
		return recordId;
	}
	
	public Player setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;
	}

    public Boolean hasRole()  {
        return role == null ? false : true;
    }

	public String getRole() {
		return role;
	}
	
	public Player setRole(String role) {
		this.role = role;
		return this;
	}

    public Boolean hasLocked()  {
        return locked == null ? false : true;
    }

	public Boolean getLocked() {
		return locked;
	}
	
	public Player setLocked(Boolean locked) {
		this.locked = locked;
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

                	message.authtoken = input.readString();
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
            output.writeString(3, message.authtoken, false);

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
		throw new RuntimeException("Protobuf encoding failed");
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
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
