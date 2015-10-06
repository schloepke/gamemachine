
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
public final class TrackData implements Externalizable, Message<TrackData>, Schema<TrackData>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(TrackData.class);

	public enum EntityType implements io.protostuff.EnumLite<EntityType>
    {
    	
    	    	NONE(0),    	    	PLAYER(1),    	    	NPC(2),    	    	OTHER(3),    	    	ALL(4),    	    	SHIP(5),    	    	ANY(6);    	        
        public final int number;
        
        private EntityType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static EntityType defaultValue() {
        	return (NONE);
        }
        
        public static EntityType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (PLAYER);
    			    			case 2: return (NPC);
    			    			case 3: return (OTHER);
    			    			case 4: return (ALL);
    			    			case 5: return (SHIP);
    			    			case 6: return (ANY);
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

    	
							    public int ix= 0;
		    			    
		
    
        	
							    public int iy= 0;
		    			    
		
    
        	
							    public int iz= 0;
		    			    
		
    
        	
							    public String id= null;
		    			    
		
    
        	
							    public int x= 0;
		    			    
		
    
        	
							    public int y= 0;
		    			    
		
    
        	
							    public int z= 0;
		    			    
		
    
        	
					public DynamicMessage dynamicMessage = null;
			    
		
    
        	
							    public String gridName= null;
		    			    
		
    
        	
							    public int getNeighbors= 0;
		    			    
		
    
        	
					public EntityType neighborEntityType = EntityType.defaultValue();
			    
		
    
        	
					public EntityType entityType = EntityType.defaultValue();
			    
		
    
        	
							    public int shortId= 0;
		    			    
		
    
        	
					public UserDefinedData userDefinedData = null;
			    
		
    
        	
							    public int broadcast= 0;
		    			    
		
    
        	
							    public String characterId= null;
		    			    
		
    
        	
							    public int rx= 0;
		    			    
		
    
        	
							    public int ry= 0;
		    			    
		
    
        	
							    public int rz= 0;
		    			    
		
    
        	
							    public int rw= 0;
		    			    
		
    
        	
							    public int vaxis= 0;
		    			    
		
    
        	
							    public int haxis= 0;
		    			    
		
    
        	
							    public int speed= 0;
		    			    
		
    
        	
							    public float velX= 0F;
		    			    
		
    
        	
							    public float velZ= 0F;
		    			    
		
    
        	
							    public int zone= 0;
		    			    
		
    
        	
							    public int hidden= 0;
		    			    
		
    
        	
							    public int yaxis= 0;
		    			    
		
    
        
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
			TrackData message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof TrackData) {
					message = (TrackData)result;
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
	

	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("track_data_ix",null);
    	    	    	    	    	    	model.set("track_data_iy",null);
    	    	    	    	    	    	model.set("track_data_iz",null);
    	    	    	    	    	    	model.set("track_data_id",null);
    	    	    	    	    	    	model.set("track_data_x",null);
    	    	    	    	    	    	model.set("track_data_y",null);
    	    	    	    	    	    	model.set("track_data_z",null);
    	    	    	    	    	    	    	model.set("track_data_grid_name",null);
    	    	    	    	    	    	model.set("track_data_get_neighbors",null);
    	    	    	    	    	    	    	    	model.set("track_data_short_id",null);
    	    	    	    	    	    	    	model.set("track_data_broadcast",null);
    	    	    	    	    	    	model.set("track_data_character_id",null);
    	    	    	    	    	    	model.set("track_data_rx",null);
    	    	    	    	    	    	model.set("track_data_ry",null);
    	    	    	    	    	    	model.set("track_data_rz",null);
    	    	    	    	    	    	model.set("track_data_rw",null);
    	    	    	    	    	    	model.set("track_data_vaxis",null);
    	    	    	    	    	    	model.set("track_data_haxis",null);
    	    	    	    	    	    	model.set("track_data_speed",null);
    	    	    	    	    	    	model.set("track_data_vel_x",null);
    	    	    	    	    	    	model.set("track_data_vel_z",null);
    	    	    	    	    	    	model.set("track_data_zone",null);
    	    	    	    	    	    	model.set("track_data_hidden",null);
    	    	    	    	    	    	model.set("track_data_yaxis",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (ix != null) {
    	       	    	model.setInteger("track_data_ix",ix);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (iy != null) {
    	       	    	model.setInteger("track_data_iy",iy);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (iz != null) {
    	       	    	model.setInteger("track_data_iz",iz);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("track_data_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (x != null) {
    	       	    	model.setInteger("track_data_x",x);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (y != null) {
    	       	    	model.setInteger("track_data_y",y);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (z != null) {
    	       	    	model.setInteger("track_data_z",z);
    	        		
    	//}
    	    	    	    	    	    	
    	    	    	//if (gridName != null) {
    	       	    	model.setString("track_data_grid_name",gridName);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (getNeighbors != null) {
    	       	    	model.setInteger("track_data_get_neighbors",getNeighbors);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (neighborEntityType != null) {
    	       	    	model.setInteger("track_data_neighbor_entity_type",neighborEntityType.ordinal());
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (entityType != null) {
    	       	    	model.setInteger("track_data_entity_type",entityType.ordinal());
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (shortId != null) {
    	       	    	model.setInteger("track_data_short_id",shortId);
    	        		
    	//}
    	    	    	    	    	    	
    	    	    	//if (broadcast != null) {
    	       	    	model.setInteger("track_data_broadcast",broadcast);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (characterId != null) {
    	       	    	model.setString("track_data_character_id",characterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rx != null) {
    	       	    	model.setInteger("track_data_rx",rx);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (ry != null) {
    	       	    	model.setInteger("track_data_ry",ry);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rz != null) {
    	       	    	model.setInteger("track_data_rz",rz);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rw != null) {
    	       	    	model.setInteger("track_data_rw",rw);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (vaxis != null) {
    	       	    	model.setInteger("track_data_vaxis",vaxis);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (haxis != null) {
    	       	    	model.setInteger("track_data_haxis",haxis);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (speed != null) {
    	       	    	model.setInteger("track_data_speed",speed);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (velX != null) {
    	       	    	model.setFloat("track_data_vel_x",velX);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (velZ != null) {
    	       	    	model.setFloat("track_data_vel_z",velZ);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (zone != null) {
    	       	    	model.setInteger("track_data_zone",zone);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (hidden != null) {
    	       	    	model.setInteger("track_data_hidden",hidden);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (yaxis != null) {
    	       	    	model.setInteger("track_data_yaxis",yaxis);
    	        		
    	//}
    	    	    }
    
	public static TrackData fromModel(Model model) {
		boolean hasFields = false;
    	TrackData message = new TrackData();
    	    	    	    	    	
    	    			Integer ixTestField = model.getInteger("track_data_ix");
		if (ixTestField != null) {
			int ixField = ixTestField;
			message.setIx(ixField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer iyTestField = model.getInteger("track_data_iy");
		if (iyTestField != null) {
			int iyField = iyTestField;
			message.setIy(iyField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer izTestField = model.getInteger("track_data_iz");
		if (izTestField != null) {
			int izField = izTestField;
			message.setIz(izField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String idTestField = model.getString("track_data_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer xTestField = model.getInteger("track_data_x");
		if (xTestField != null) {
			int xField = xTestField;
			message.setX(xField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer yTestField = model.getInteger("track_data_y");
		if (yTestField != null) {
			int yField = yTestField;
			message.setY(yField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer zTestField = model.getInteger("track_data_z");
		if (zTestField != null) {
			int zField = zTestField;
			message.setZ(zField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	
    	    			String gridNameTestField = model.getString("track_data_grid_name");
		if (gridNameTestField != null) {
			String gridNameField = gridNameTestField;
			message.setGridName(gridNameField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer getNeighborsTestField = model.getInteger("track_data_get_neighbors");
		if (getNeighborsTestField != null) {
			int getNeighborsField = getNeighborsTestField;
			message.setGetNeighbors(getNeighborsField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    				message.setNeighborEntityType(EntityType.valueOf(model.getInteger("track_data_neighbor_entity_type")));
    	    	    	    	    	    	
    				message.setEntityType(EntityType.valueOf(model.getInteger("track_data_entity_type")));
    	    	    	    	    	    	
    	    			Integer shortIdTestField = model.getInteger("track_data_short_id");
		if (shortIdTestField != null) {
			int shortIdField = shortIdTestField;
			message.setShortId(shortIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	
    	    			Integer broadcastTestField = model.getInteger("track_data_broadcast");
		if (broadcastTestField != null) {
			int broadcastField = broadcastTestField;
			message.setBroadcast(broadcastField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String characterIdTestField = model.getString("track_data_character_id");
		if (characterIdTestField != null) {
			String characterIdField = characterIdTestField;
			message.setCharacterId(characterIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer rxTestField = model.getInteger("track_data_rx");
		if (rxTestField != null) {
			int rxField = rxTestField;
			message.setRx(rxField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer ryTestField = model.getInteger("track_data_ry");
		if (ryTestField != null) {
			int ryField = ryTestField;
			message.setRy(ryField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer rzTestField = model.getInteger("track_data_rz");
		if (rzTestField != null) {
			int rzField = rzTestField;
			message.setRz(rzField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer rwTestField = model.getInteger("track_data_rw");
		if (rwTestField != null) {
			int rwField = rwTestField;
			message.setRw(rwField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer vaxisTestField = model.getInteger("track_data_vaxis");
		if (vaxisTestField != null) {
			int vaxisField = vaxisTestField;
			message.setVaxis(vaxisField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer haxisTestField = model.getInteger("track_data_haxis");
		if (haxisTestField != null) {
			int haxisField = haxisTestField;
			message.setHaxis(haxisField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer speedTestField = model.getInteger("track_data_speed");
		if (speedTestField != null) {
			int speedField = speedTestField;
			message.setSpeed(speedField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Float velXTestField = model.getFloat("track_data_vel_x");
		if (velXTestField != null) {
			float velXField = velXTestField;
			message.setVelX(velXField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Float velZTestField = model.getFloat("track_data_vel_z");
		if (velZTestField != null) {
			float velZField = velZTestField;
			message.setVelZ(velZField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer zoneTestField = model.getInteger("track_data_zone");
		if (zoneTestField != null) {
			int zoneField = zoneTestField;
			message.setZone(zoneField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer hiddenTestField = model.getInteger("track_data_hidden");
		if (hiddenTestField != null) {
			int hiddenField = hiddenTestField;
			message.setHidden(hiddenField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer yaxisTestField = model.getInteger("track_data_yaxis");
		if (yaxisTestField != null) {
			int yaxisField = yaxisTestField;
			message.setYaxis(yaxisField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public int getIx() {
		return ix;
	}
	
	public TrackData setIx(int ix) {
		this.ix = ix;
		return this;	}
	
		            
		public int getIy() {
		return iy;
	}
	
	public TrackData setIy(int iy) {
		this.iy = iy;
		return this;	}
	
		            
		public int getIz() {
		return iz;
	}
	
	public TrackData setIz(int iz) {
		this.iz = iz;
		return this;	}
	
		            
		public String getId() {
		return id;
	}
	
	public TrackData setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public int getX() {
		return x;
	}
	
	public TrackData setX(int x) {
		this.x = x;
		return this;	}
	
		            
		public int getY() {
		return y;
	}
	
	public TrackData setY(int y) {
		this.y = y;
		return this;	}
	
		            
		public int getZ() {
		return z;
	}
	
	public TrackData setZ(int z) {
		this.z = z;
		return this;	}
	
		            
		public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public TrackData setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
		return this;	}
	
		            
		public String getGridName() {
		return gridName;
	}
	
	public TrackData setGridName(String gridName) {
		this.gridName = gridName;
		return this;	}
	
		            
		public int getGetNeighbors() {
		return getNeighbors;
	}
	
	public TrackData setGetNeighbors(int getNeighbors) {
		this.getNeighbors = getNeighbors;
		return this;	}
	
		            
		public EntityType getNeighborEntityType() {
		return neighborEntityType;
	}
	
	public TrackData setNeighborEntityType(EntityType neighborEntityType) {
		this.neighborEntityType = neighborEntityType;
		return this;	}
	
		            
		public EntityType getEntityType() {
		return entityType;
	}
	
	public TrackData setEntityType(EntityType entityType) {
		this.entityType = entityType;
		return this;	}
	
		            
		public int getShortId() {
		return shortId;
	}
	
	public TrackData setShortId(int shortId) {
		this.shortId = shortId;
		return this;	}
	
		            
		public UserDefinedData getUserDefinedData() {
		return userDefinedData;
	}
	
	public TrackData setUserDefinedData(UserDefinedData userDefinedData) {
		this.userDefinedData = userDefinedData;
		return this;	}
	
		            
		public int getBroadcast() {
		return broadcast;
	}
	
	public TrackData setBroadcast(int broadcast) {
		this.broadcast = broadcast;
		return this;	}
	
		            
		public String getCharacterId() {
		return characterId;
	}
	
	public TrackData setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		            
		public int getRx() {
		return rx;
	}
	
	public TrackData setRx(int rx) {
		this.rx = rx;
		return this;	}
	
		            
		public int getRy() {
		return ry;
	}
	
	public TrackData setRy(int ry) {
		this.ry = ry;
		return this;	}
	
		            
		public int getRz() {
		return rz;
	}
	
	public TrackData setRz(int rz) {
		this.rz = rz;
		return this;	}
	
		            
		public int getRw() {
		return rw;
	}
	
	public TrackData setRw(int rw) {
		this.rw = rw;
		return this;	}
	
		            
		public int getVaxis() {
		return vaxis;
	}
	
	public TrackData setVaxis(int vaxis) {
		this.vaxis = vaxis;
		return this;	}
	
		            
		public int getHaxis() {
		return haxis;
	}
	
	public TrackData setHaxis(int haxis) {
		this.haxis = haxis;
		return this;	}
	
		            
		public int getSpeed() {
		return speed;
	}
	
	public TrackData setSpeed(int speed) {
		this.speed = speed;
		return this;	}
	
		            
		public float getVelX() {
		return velX;
	}
	
	public TrackData setVelX(float velX) {
		this.velX = velX;
		return this;	}
	
		            
		public float getVelZ() {
		return velZ;
	}
	
	public TrackData setVelZ(float velZ) {
		this.velZ = velZ;
		return this;	}
	
		            
		public int getZone() {
		return zone;
	}
	
	public TrackData setZone(int zone) {
		this.zone = zone;
		return this;	}
	
		            
		public int getHidden() {
		return hidden;
	}
	
	public TrackData setHidden(int hidden) {
		this.hidden = hidden;
		return this;	}
	
		            
		public int getYaxis() {
		return yaxis;
	}
	
	public TrackData setYaxis(int yaxis) {
		this.yaxis = yaxis;
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
                            	case 1:
            	                	                	message.ix = input.readSInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.iy = input.readSInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.iz = input.readSInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.x = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.y = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.z = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;
                                    	
                            	            	case 9:
            	                	                	message.gridName = input.readString();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.getNeighbors = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                    message.neighborEntityType = EntityType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 12:
            	                	                    message.entityType = EntityType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 13:
            	                	                	message.shortId = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.userDefinedData = input.mergeObject(message.userDefinedData, UserDefinedData.getSchema());
                    break;
                                    	
                            	            	case 15:
            	                	                	message.broadcast = input.readInt32();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.rx = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.ry = input.readInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.rz = input.readInt32();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.rw = input.readInt32();
                	break;
                	                	
                            	            	case 21:
            	                	                	message.vaxis = input.readInt32();
                	break;
                	                	
                            	            	case 22:
            	                	                	message.haxis = input.readInt32();
                	break;
                	                	
                            	            	case 23:
            	                	                	message.speed = input.readInt32();
                	break;
                	                	
                            	            	case 24:
            	                	                	message.velX = input.readFloat();
                	break;
                	                	
                            	            	case 25:
            	                	                	message.velZ = input.readFloat();
                	break;
                	                	
                            	            	case 26:
            	                	                	message.zone = input.readInt32();
                	break;
                	                	
                            	            	case 27:
            	                	                	message.hidden = input.readInt32();
                	break;
                	                	
                            	            	case 28:
            	                	                	message.yaxis = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackData message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (Integer)message.ix != null) {
            output.writeSInt32(1, message.ix, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.iy != null) {
            output.writeSInt32(2, message.iy, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.iz != null) {
            output.writeSInt32(3, message.iz, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(4, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.x != null) {
            output.writeInt32(5, message.x, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.y != null) {
            output.writeInt32(6, message.y, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.z != null) {
            output.writeInt32(7, message.z, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.dynamicMessage != null)
    		output.writeObject(8, message.dynamicMessage, DynamicMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.gridName != null) {
            output.writeString(9, message.gridName, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.getNeighbors != null) {
            output.writeInt32(10, message.getNeighbors, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.neighborEntityType != null)
    	 	output.writeEnum(11, message.neighborEntityType.number, false);
    	    	
    	            	
    	    	//if(message.entityType == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.entityType != null)
    	 	output.writeEnum(12, message.entityType.number, false);
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.shortId != null) {
            output.writeInt32(13, message.shortId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.userDefinedData != null)
    		output.writeObject(14, message.userDefinedData, UserDefinedData.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.broadcast != null) {
            output.writeInt32(15, message.broadcast, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.characterId != null) {
            output.writeString(16, message.characterId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rx != null) {
            output.writeInt32(17, message.rx, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.ry != null) {
            output.writeInt32(18, message.ry, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rz != null) {
            output.writeInt32(19, message.rz, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rw != null) {
            output.writeInt32(20, message.rw, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.vaxis != null) {
            output.writeInt32(21, message.vaxis, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.haxis != null) {
            output.writeInt32(22, message.haxis, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.speed != null) {
            output.writeInt32(23, message.speed, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Float)message.velX != null) {
            output.writeFloat(24, message.velX, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Float)message.velZ != null) {
            output.writeFloat(25, message.velZ, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.zone != null) {
            output.writeInt32(26, message.zone, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.hidden != null) {
            output.writeInt32(27, message.hidden, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.yaxis != null) {
            output.writeInt32(28, message.yaxis, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START TrackData");
    	    	//if(this.ix != null) {
    		System.out.println("ix="+this.ix);
    	//}
    	    	//if(this.iy != null) {
    		System.out.println("iy="+this.iy);
    	//}
    	    	//if(this.iz != null) {
    		System.out.println("iz="+this.iz);
    	//}
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
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
    	    	//if(this.dynamicMessage != null) {
    		System.out.println("dynamicMessage="+this.dynamicMessage);
    	//}
    	    	//if(this.gridName != null) {
    		System.out.println("gridName="+this.gridName);
    	//}
    	    	//if(this.getNeighbors != null) {
    		System.out.println("getNeighbors="+this.getNeighbors);
    	//}
    	    	//if(this.neighborEntityType != null) {
    		System.out.println("neighborEntityType="+this.neighborEntityType);
    	//}
    	    	//if(this.entityType != null) {
    		System.out.println("entityType="+this.entityType);
    	//}
    	    	//if(this.shortId != null) {
    		System.out.println("shortId="+this.shortId);
    	//}
    	    	//if(this.userDefinedData != null) {
    		System.out.println("userDefinedData="+this.userDefinedData);
    	//}
    	    	//if(this.broadcast != null) {
    		System.out.println("broadcast="+this.broadcast);
    	//}
    	    	//if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
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
    	    	//if(this.vaxis != null) {
    		System.out.println("vaxis="+this.vaxis);
    	//}
    	    	//if(this.haxis != null) {
    		System.out.println("haxis="+this.haxis);
    	//}
    	    	//if(this.speed != null) {
    		System.out.println("speed="+this.speed);
    	//}
    	    	//if(this.velX != null) {
    		System.out.println("velX="+this.velX);
    	//}
    	    	//if(this.velZ != null) {
    		System.out.println("velZ="+this.velZ);
    	//}
    	    	//if(this.zone != null) {
    		System.out.println("zone="+this.zone);
    	//}
    	    	//if(this.hidden != null) {
    		System.out.println("hidden="+this.hidden);
    	//}
    	    	//if(this.yaxis != null) {
    		System.out.println("yaxis="+this.yaxis);
    	//}
    	    	System.out.println("END TrackData");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "ix";
        	        	case 2: return "iy";
        	        	case 3: return "iz";
        	        	case 4: return "id";
        	        	case 5: return "x";
        	        	case 6: return "y";
        	        	case 7: return "z";
        	        	case 8: return "dynamicMessage";
        	        	case 9: return "gridName";
        	        	case 10: return "getNeighbors";
        	        	case 11: return "neighborEntityType";
        	        	case 12: return "entityType";
        	        	case 13: return "shortId";
        	        	case 14: return "userDefinedData";
        	        	case 15: return "broadcast";
        	        	case 16: return "characterId";
        	        	case 17: return "rx";
        	        	case 18: return "ry";
        	        	case 19: return "rz";
        	        	case 20: return "rw";
        	        	case 21: return "vaxis";
        	        	case 22: return "haxis";
        	        	case 23: return "speed";
        	        	case 24: return "velX";
        	        	case 25: return "velZ";
        	        	case 26: return "zone";
        	        	case 27: return "hidden";
        	        	case 28: return "yaxis";
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
    	    	__fieldMap.put("ix", 1);
    	    	__fieldMap.put("iy", 2);
    	    	__fieldMap.put("iz", 3);
    	    	__fieldMap.put("id", 4);
    	    	__fieldMap.put("x", 5);
    	    	__fieldMap.put("y", 6);
    	    	__fieldMap.put("z", 7);
    	    	__fieldMap.put("dynamicMessage", 8);
    	    	__fieldMap.put("gridName", 9);
    	    	__fieldMap.put("getNeighbors", 10);
    	    	__fieldMap.put("neighborEntityType", 11);
    	    	__fieldMap.put("entityType", 12);
    	    	__fieldMap.put("shortId", 13);
    	    	__fieldMap.put("userDefinedData", 14);
    	    	__fieldMap.put("broadcast", 15);
    	    	__fieldMap.put("characterId", 16);
    	    	__fieldMap.put("rx", 17);
    	    	__fieldMap.put("ry", 18);
    	    	__fieldMap.put("rz", 19);
    	    	__fieldMap.put("rw", 20);
    	    	__fieldMap.put("vaxis", 21);
    	    	__fieldMap.put("haxis", 22);
    	    	__fieldMap.put("speed", 23);
    	    	__fieldMap.put("velX", 24);
    	    	__fieldMap.put("velZ", 25);
    	    	__fieldMap.put("zone", 26);
    	    	__fieldMap.put("hidden", 27);
    	    	__fieldMap.put("yaxis", 28);
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
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
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
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
