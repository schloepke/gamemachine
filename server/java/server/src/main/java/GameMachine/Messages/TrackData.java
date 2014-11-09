
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

import com.game_machine.core.ActorUtil;

import org.javalite.activejdbc.Model;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

import com.game_machine.core.PersistableMessage;

import com.game_machine.objectdb.Cache;
import com.game_machine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class TrackData implements Externalizable, Message<TrackData>, Schema<TrackData>, PersistableMessage
{

	public enum EntityType implements com.dyuproject.protostuff.EnumLite<EntityType>
    {

    	PLAYER(0),
    	
    	NPC(1),
    	
    	OTHER(2),
    	
    	ALL(3);

        public final int number;
        
        private EntityType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static EntityType valueOf(int number)
        {
            switch(number) 
            {
            	
    			case 0: return (PLAYER);
    			
    			case 1: return (NPC);
    			
    			case 2: return (OTHER);
    			
    			case 3: return (ALL);
    			
                default: return null;
            }
        }
    }

    public static Schema<TrackData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackData DEFAULT_INSTANCE = new TrackData();
    static final String defaultScope = TrackData.class.getSimpleName();

		public String id;

		public Float x;

		public Float y;

		public Float z;

		public DynamicMessage dynamicMessage;

		public String gridName;

		public Integer getNeighbors;

		public EntityType neighborEntityType; // = PLAYER:0;

		public EntityType entityType; // = PLAYER:0;

	public static TrackDataCache cache() {
		return TrackDataCache.getInstance();
	}
	
	public static TrackDataStore store() {
		return TrackDataStore.getInstance();
	}

    public TrackData()
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
		
		public TrackData result(int timeout) {
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
	
	public static class TrackDataCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, TrackData> cache = new Cache<String, TrackData>(120, 5000);
		
		private TrackDataCache() {
		}
		
		private static class LazyHolder {
			private static final TrackDataCache INSTANCE = new TrackDataCache();
		}
	
		public static TrackDataCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, TrackData>(expiration, size);
		}
	
		public Cache<String, TrackData> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(TrackDataCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(TrackData message) {
			CacheUpdate cacheUpdate = new CacheUpdate(TrackDataCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public TrackData get(String id, int timeout) {
			TrackData message = cache.get(id);
			if (message == null) {
				message = TrackData.store().get(id, timeout);
			}
			return message;
		}
			
		public static TrackData setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			TrackData message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (TrackData) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				TrackData.store().set(message);
			} else {
				message = TrackData.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, TrackData.class.getField(field));
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
				TrackData.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class TrackDataStore {
	
		private TrackDataStore() {
		}
		
		private static class LazyHolder {
			private static final TrackDataStore INSTANCE = new TrackDataStore();
		}
	
		public static TrackDataStore getInstance() {
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
		
	    public void set(TrackData message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public TrackData get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, TrackData message) {
	    	TrackData clone = message.clone();
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
			
		public TrackData get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("TrackData");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			TrackData message;
			Future<Object> future = askable.ask(get,t);
			try {
				message = (TrackData) Await.result(future, t.duration());
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

	public static void clearModel(Model model) {

    	model.set("track_data_id",null);

    	model.set("track_data_x",null);

    	model.set("track_data_y",null);

    	model.set("track_data_z",null);

    	model.set("track_data_grid_name",null);

    	model.set("track_data_get_neighbors",null);

    }
    
	public void toModel(Model model) {

    	if (id != null) {
    		model.setString("track_data_id",id);
    	}

    	if (x != null) {
    		model.setFloat("track_data_x",x);
    	}

    	if (y != null) {
    		model.setFloat("track_data_y",y);
    	}

    	if (z != null) {
    		model.setFloat("track_data_z",z);
    	}

    	if (gridName != null) {
    		model.setString("track_data_grid_name",gridName);
    	}

    	if (getNeighbors != null) {
    		model.setInteger("track_data_get_neighbors",getNeighbors);
    	}

    }
    
	public static TrackData fromModel(Model model) {
		boolean hasFields = false;
    	TrackData message = new TrackData();

    	String idField = model.getString("track_data_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}

    	Float xField = model.getFloat("track_data_x");
    	if (xField != null) {
    		message.setX(xField);
    		hasFields = true;
    	}

    	Float yField = model.getFloat("track_data_y");
    	if (yField != null) {
    		message.setY(yField);
    		hasFields = true;
    	}

    	Float zField = model.getFloat("track_data_z");
    	if (zField != null) {
    		message.setZ(zField);
    		hasFields = true;
    	}

    	String gridNameField = model.getString("track_data_grid_name");
    	if (gridNameField != null) {
    		message.setGridName(gridNameField);
    		hasFields = true;
    	}

    	Integer getNeighborsField = model.getInteger("track_data_get_neighbors");
    	if (getNeighborsField != null) {
    		message.setGetNeighbors(getNeighborsField);
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
	
	public TrackData setId(String id) {
		this.id = id;
		return this;
	}

    public Boolean hasX()  {
        return x == null ? false : true;
    }

	public Float getX() {
		return x;
	}
	
	public TrackData setX(Float x) {
		this.x = x;
		return this;
	}

    public Boolean hasY()  {
        return y == null ? false : true;
    }

	public Float getY() {
		return y;
	}
	
	public TrackData setY(Float y) {
		this.y = y;
		return this;
	}

    public Boolean hasZ()  {
        return z == null ? false : true;
    }

	public Float getZ() {
		return z;
	}
	
	public TrackData setZ(Float z) {
		this.z = z;
		return this;
	}

    public Boolean hasDynamicMessage()  {
        return dynamicMessage == null ? false : true;
    }

	public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public TrackData setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
		return this;
	}

    public Boolean hasGridName()  {
        return gridName == null ? false : true;
    }

	public String getGridName() {
		return gridName;
	}
	
	public TrackData setGridName(String gridName) {
		this.gridName = gridName;
		return this;
	}

    public Boolean hasGetNeighbors()  {
        return getNeighbors == null ? false : true;
    }

	public Integer getGetNeighbors() {
		return getNeighbors;
	}
	
	public TrackData setGetNeighbors(Integer getNeighbors) {
		this.getNeighbors = getNeighbors;
		return this;
	}

    public Boolean hasNeighborEntityType()  {
        return neighborEntityType == null ? false : true;
    }

	public EntityType getNeighborEntityType() {
		return neighborEntityType;
	}
	
	public TrackData setNeighborEntityType(EntityType neighborEntityType) {
		this.neighborEntityType = neighborEntityType;
		return this;
	}

    public Boolean hasEntityType()  {
        return entityType == null ? false : true;
    }

	public EntityType getEntityType() {
		return entityType;
	}
	
	public TrackData setEntityType(EntityType entityType) {
		this.entityType = entityType;
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

    public Schema<TrackData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackData newMessage()
    {
        return new TrackData();
    }

    public Class<TrackData> typeClass()
    {
        return TrackData.class;
    }

    public String messageName()
    {
        return TrackData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackData.class.getName();
    }

    public boolean isInitialized(TrackData message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 5:

                	message.id = input.readString();
                	break;

            	case 6:

                	message.x = input.readFloat();
                	break;

            	case 7:

                	message.y = input.readFloat();
                	break;

            	case 8:

                	message.z = input.readFloat();
                	break;

            	case 13:

                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;

            	case 14:

                	message.gridName = input.readString();
                	break;

            	case 15:

                	message.getNeighbors = input.readInt32();
                	break;

            	case 16:

                    message.neighborEntityType = EntityType.valueOf(input.readEnum());
                    break;

            	case 17:

                    message.entityType = EntityType.valueOf(input.readEnum());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, TrackData message) throws IOException
    {

    	if(message.id != null)
            output.writeString(5, message.id, false);

    	if(message.x == null)
            throw new UninitializedMessageException(message);

    	if(message.x != null)
            output.writeFloat(6, message.x, false);

    	if(message.y == null)
            throw new UninitializedMessageException(message);

    	if(message.y != null)
            output.writeFloat(7, message.y, false);

    	if(message.z != null)
            output.writeFloat(8, message.z, false);

    	if(message.dynamicMessage != null)
    		output.writeObject(13, message.dynamicMessage, DynamicMessage.getSchema(), false);

    	if(message.gridName != null)
            output.writeString(14, message.gridName, false);

    	if(message.getNeighbors == null)
            throw new UninitializedMessageException(message);

    	if(message.getNeighbors != null)
            output.writeInt32(15, message.getNeighbors, false);

    	if(message.neighborEntityType != null)
    	 	output.writeEnum(16, message.neighborEntityType.number, false);

    	if(message.entityType != null)
    	 	output.writeEnum(17, message.entityType.number, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 5: return "id";
        	
        	case 6: return "x";
        	
        	case 7: return "y";
        	
        	case 8: return "z";
        	
        	case 13: return "dynamicMessage";
        	
        	case 14: return "gridName";
        	
        	case 15: return "getNeighbors";
        	
        	case 16: return "neighborEntityType";
        	
        	case 17: return "entityType";
        	
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
    	
    	__fieldMap.put("id", 5);
    	
    	__fieldMap.put("x", 6);
    	
    	__fieldMap.put("y", 7);
    	
    	__fieldMap.put("z", 8);
    	
    	__fieldMap.put("dynamicMessage", 13);
    	
    	__fieldMap.put("gridName", 14);
    	
    	__fieldMap.put("getNeighbors", 15);
    	
    	__fieldMap.put("neighborEntityType", 16);
    	
    	__fieldMap.put("entityType", 17);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackData parseFrom(byte[] bytes) {
	TrackData message = new TrackData();
	ProtobufIOUtil.mergeFrom(bytes, message, TrackData.getSchema());
	return message;
}

public static TrackData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TrackData message = new TrackData();
	JsonIOUtil.mergeFrom(bytes, message, TrackData.getSchema(), false);
	return message;
}

public TrackData clone() {
	byte[] bytes = this.toByteArray();
	TrackData trackData = TrackData.parseFrom(bytes);
	return trackData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TrackData> schema = TrackData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TrackData.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, TrackData.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
