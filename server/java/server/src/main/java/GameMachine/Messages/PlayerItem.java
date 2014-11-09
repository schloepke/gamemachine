
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
public final class PlayerItem implements Externalizable, Message<PlayerItem>, Schema<PlayerItem>, PersistableMessage
{

    public static Schema<PlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerItem DEFAULT_INSTANCE = new PlayerItem();
    static final String defaultScope = PlayerItem.class.getSimpleName();

		public String id;

		public String name;

		public Integer quantity;

		public String color;

		public Weapon weapon;

		public Consumable consumable;

		public Cost cost;

		public String playerId;

		public Integer recordId;

	public static PlayerItemCache cache() {
		return PlayerItemCache.getInstance();
	}
	
	public static PlayerItemStore store() {
		return PlayerItemStore.getInstance();
	}

    public PlayerItem()
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
		
		public PlayerItem result(int timeout) {
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
	
	public static class PlayerItemCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, PlayerItem> cache = new Cache<String, PlayerItem>(120, 5000);
		
		private PlayerItemCache() {
		}
		
		private static class LazyHolder {
			private static final PlayerItemCache INSTANCE = new PlayerItemCache();
		}
	
		public static PlayerItemCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, PlayerItem>(expiration, size);
		}
	
		public Cache<String, PlayerItem> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(PlayerItemCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(PlayerItem message) {
			CacheUpdate cacheUpdate = new CacheUpdate(PlayerItemCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public PlayerItem get(String id, int timeout) {
			PlayerItem message = cache.get(id);
			if (message == null) {
				message = PlayerItem.store().get(id, timeout);
			}
			return message;
		}
			
		public static PlayerItem setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			PlayerItem message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (PlayerItem) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				PlayerItem.store().set(message);
			} else {
				message = PlayerItem.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, PlayerItem.class.getField(field));
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
				PlayerItem.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class PlayerItemStore {
	
		private PlayerItemStore() {
		}
		
		private static class LazyHolder {
			private static final PlayerItemStore INSTANCE = new PlayerItemStore();
		}
	
		public static PlayerItemStore getInstance() {
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
		
	    public void set(PlayerItem message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public PlayerItem get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, PlayerItem message) {
	    	PlayerItem clone = message.clone();
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
			
		public PlayerItem get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("PlayerItem");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			PlayerItem message;
			Future<Object> future = askable.ask(get,t);
			try {
				message = (PlayerItem) Await.result(future, t.duration());
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

	public static PlayerItemDb db() {
		return PlayerItemDb.getInstance();
	}
	
	public interface PlayerItemAsyncDb {
		void save(PlayerItem message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class PlayerItemAsyncDbImpl implements PlayerItemAsyncDb {
	
		public void save(PlayerItem message) {
			PlayerItem.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	PlayerItem.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	PlayerItem.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class PlayerItemDb {
	
		public Errors dbErrors;
		private PlayerItemAsyncDb asyncDb = null;
		
		private PlayerItemDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<PlayerItemAsyncDbImpl>(PlayerItemAsyncDb.class, PlayerItemAsyncDbImpl.class));
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
			private static final PlayerItemDb INSTANCE = new PlayerItemDb();
		}
	
		public static PlayerItemDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(PlayerItem message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(PlayerItem message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(PlayerItem message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.PlayerItem.open();
	    	}
	    	
	    	com.game_machine.orm.models.PlayerItem model = null;
	    	if (message.hasRecordId()) {
	    		model = com.game_machine.orm.models.PlayerItem.findFirst("id = ?", message.recordId);
	    	}
	    	
	    	if (model == null) {
	    		model = new com.game_machine.orm.models.PlayerItem();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
	    	}
	    	
	    	if (message.hasWeapon()) {
	    		message.weapon.toModel(model);
	    	} else {
	    		Weapon.clearModel(model);
	    	}
	    	
	    	if (message.hasConsumable()) {
	    		message.consumable.toModel(model);
	    	} else {
	    		Consumable.clearModel(model);
	    	}
	    	
	    	if (message.hasCost()) {
	    		message.cost.toModel(model);
	    	} else {
	    		Cost.clearModel(model);
	    	}

	    	Boolean res = model.save();
	    	if (res) {
	    		message.setRecordId(model.getInteger("id"));
	    	} else {
	    		dbErrors = model.errors();
	    	}
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.PlayerItem.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	com.game_machine.orm.models.PlayerItem.open();
	    	int deleted = com.game_machine.orm.models.PlayerItem.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	com.game_machine.orm.models.PlayerItem.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	com.game_machine.orm.models.PlayerItem.open();
	    	int deleted = com.game_machine.orm.models.PlayerItem.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	com.game_machine.orm.models.PlayerItem.close();
	    	return result;
	    }
	    
	    public PlayerItem find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public PlayerItem find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.PlayerItem.open();
	    	}
	    	
	    	com.game_machine.orm.models.PlayerItem model = com.game_machine.orm.models.PlayerItem.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.PlayerItem.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		PlayerItem playerItem = fromModel(model);
	    		
	    		playerItem.weapon = Weapon.fromModel(model);
	    		
	    		playerItem.consumable = Consumable.fromModel(model);
	    		
	    		playerItem.cost = Cost.fromModel(model);
	    		
	    		return playerItem;
	    	}
	    }
	    
	    public PlayerItem findFirst(String query, Object ... params) {
	    	com.game_machine.orm.models.PlayerItem.open();
	    	com.game_machine.orm.models.PlayerItem model = com.game_machine.orm.models.PlayerItem.findFirst(query, params);
	    	com.game_machine.orm.models.PlayerItem.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		PlayerItem playerItem = fromModel(model);
	    		
	    		playerItem.weapon = Weapon.fromModel(model);
	    		
	    		playerItem.consumable = Consumable.fromModel(model);
	    		
	    		playerItem.cost = Cost.fromModel(model);
	    		
	    		return playerItem;
	    	}
	    }
	    
	    public List<PlayerItem> findAll() {
	    	com.game_machine.orm.models.PlayerItem.open();
	    	List<com.game_machine.orm.models.PlayerItem> models = com.game_machine.orm.models.PlayerItem.findAll();
	    	List<PlayerItem> messages = new ArrayList<PlayerItem>();
	    	for (com.game_machine.orm.models.PlayerItem model : models) {
	    		PlayerItem playerItem = fromModel(model);
	    		
	    		playerItem.weapon = Weapon.fromModel(model);
	    		
	    		playerItem.consumable = Consumable.fromModel(model);
	    		
	    		playerItem.cost = Cost.fromModel(model);
	    		
	    		messages.add(playerItem);
	    	}
	    	com.game_machine.orm.models.PlayerItem.close();
	    	return messages;
	    }
	    
	    public List<PlayerItem> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<PlayerItem> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.PlayerItem.open();
	    	}
	    	List<com.game_machine.orm.models.PlayerItem> models = com.game_machine.orm.models.PlayerItem.where(query, params);
	    	List<PlayerItem> messages = new ArrayList<PlayerItem>();
	    	for (com.game_machine.orm.models.PlayerItem model : models) {
	    		PlayerItem playerItem = fromModel(model);
	    		
	    		playerItem.weapon = Weapon.fromModel(model);
	    		
	    		playerItem.consumable = Consumable.fromModel(model);
	    		
	    		playerItem.cost = Cost.fromModel(model);
	    		
	    		messages.add(playerItem);
	    	}
	    	if (!inTransaction) {
	    		com.game_machine.orm.models.PlayerItem.close();
	    	}
	    	return messages;
	    }
    }

	public static void clearModel(Model model) {

    	model.set("player_item_id",null);

    	model.set("player_item_name",null);

    	model.set("player_item_quantity",null);

    	model.set("player_item_color",null);

    	model.set("player_item_player_id",null);

    }
    
	public void toModel(Model model) {

    	if (id != null) {
    		model.setString("player_item_id",id);
    	}

    	if (name != null) {
    		model.setString("player_item_name",name);
    	}

    	if (quantity != null) {
    		model.setInteger("player_item_quantity",quantity);
    	}

    	if (color != null) {
    		model.setString("player_item_color",color);
    	}

    	if (playerId != null) {
    		model.setString("player_item_player_id",playerId);
    	}

    	model.setInteger("id",recordId);

    }
    
	public static PlayerItem fromModel(Model model) {
		boolean hasFields = false;
    	PlayerItem message = new PlayerItem();

    	String idField = model.getString("player_item_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}

    	String nameField = model.getString("player_item_name");
    	if (nameField != null) {
    		message.setName(nameField);
    		hasFields = true;
    	}

    	Integer quantityField = model.getInteger("player_item_quantity");
    	if (quantityField != null) {
    		message.setQuantity(quantityField);
    		hasFields = true;
    	}

    	String colorField = model.getString("player_item_color");
    	if (colorField != null) {
    		message.setColor(colorField);
    		hasFields = true;
    	}

    	String playerIdField = model.getString("player_item_player_id");
    	if (playerIdField != null) {
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}

    	if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
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
	
	public PlayerItem setId(String id) {
		this.id = id;
		return this;
	}

    public Boolean hasName()  {
        return name == null ? false : true;
    }

	public String getName() {
		return name;
	}
	
	public PlayerItem setName(String name) {
		this.name = name;
		return this;
	}

    public Boolean hasQuantity()  {
        return quantity == null ? false : true;
    }

	public Integer getQuantity() {
		return quantity;
	}
	
	public PlayerItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

    public Boolean hasColor()  {
        return color == null ? false : true;
    }

	public String getColor() {
		return color;
	}
	
	public PlayerItem setColor(String color) {
		this.color = color;
		return this;
	}

    public Boolean hasWeapon()  {
        return weapon == null ? false : true;
    }

	public Weapon getWeapon() {
		return weapon;
	}
	
	public PlayerItem setWeapon(Weapon weapon) {
		this.weapon = weapon;
		return this;
	}

    public Boolean hasConsumable()  {
        return consumable == null ? false : true;
    }

	public Consumable getConsumable() {
		return consumable;
	}
	
	public PlayerItem setConsumable(Consumable consumable) {
		this.consumable = consumable;
		return this;
	}

    public Boolean hasCost()  {
        return cost == null ? false : true;
    }

	public Cost getCost() {
		return cost;
	}
	
	public PlayerItem setCost(Cost cost) {
		this.cost = cost;
		return this;
	}

    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }

	public String getPlayerId() {
		return playerId;
	}
	
	public PlayerItem setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;
	}

    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }

	public Integer getRecordId() {
		return recordId;
	}
	
	public PlayerItem setRecordId(Integer recordId) {
		this.recordId = recordId;
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

    public Schema<PlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerItem newMessage()
    {
        return new PlayerItem();
    }

    public Class<PlayerItem> typeClass()
    {
        return PlayerItem.class;
    }

    public String messageName()
    {
        return PlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerItem.class.getName();
    }

    public boolean isInitialized(PlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerItem message) throws IOException
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

                	message.quantity = input.readInt32();
                	break;

            	case 4:

                	message.color = input.readString();
                	break;

            	case 5:

                	message.weapon = input.mergeObject(message.weapon, Weapon.getSchema());
                    break;

            	case 6:

                	message.consumable = input.mergeObject(message.consumable, Consumable.getSchema());
                    break;

            	case 8:

                	message.cost = input.mergeObject(message.cost, Cost.getSchema());
                    break;

            	case 9:

                	message.playerId = input.readString();
                	break;

            	case 10:

                	message.recordId = input.readInt32();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, PlayerItem message) throws IOException
    {

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	if(message.id != null)
            output.writeString(1, message.id, false);

    	if(message.name == null)
            throw new UninitializedMessageException(message);

    	if(message.name != null)
            output.writeString(2, message.name, false);

    	if(message.quantity == null)
            throw new UninitializedMessageException(message);

    	if(message.quantity != null)
            output.writeInt32(3, message.quantity, false);

    	if(message.color != null)
            output.writeString(4, message.color, false);

    	if(message.weapon != null)
    		output.writeObject(5, message.weapon, Weapon.getSchema(), false);

    	if(message.consumable != null)
    		output.writeObject(6, message.consumable, Consumable.getSchema(), false);

    	if(message.cost != null)
    		output.writeObject(8, message.cost, Cost.getSchema(), false);

    	if(message.playerId != null)
            output.writeString(9, message.playerId, false);

    	if(message.recordId != null)
            output.writeInt32(10, message.recordId, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "id";
        	
        	case 2: return "name";
        	
        	case 3: return "quantity";
        	
        	case 4: return "color";
        	
        	case 5: return "weapon";
        	
        	case 6: return "consumable";
        	
        	case 8: return "cost";
        	
        	case 9: return "playerId";
        	
        	case 10: return "recordId";
        	
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
    	
    	__fieldMap.put("quantity", 3);
    	
    	__fieldMap.put("color", 4);
    	
    	__fieldMap.put("weapon", 5);
    	
    	__fieldMap.put("consumable", 6);
    	
    	__fieldMap.put("cost", 8);
    	
    	__fieldMap.put("playerId", 9);
    	
    	__fieldMap.put("recordId", 10);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerItem parseFrom(byte[] bytes) {
	PlayerItem message = new PlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerItem.getSchema());
	return message;
}

public static PlayerItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerItem message = new PlayerItem();
	JsonIOUtil.mergeFrom(bytes, message, PlayerItem.getSchema(), false);
	return message;
}

public PlayerItem clone() {
	byte[] bytes = this.toByteArray();
	PlayerItem playerItem = PlayerItem.parseFrom(bytes);
	return playerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerItem> schema = PlayerItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerItem.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, PlayerItem.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
