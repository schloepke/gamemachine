
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


import io.gamemachine.core.ActorUtil;

import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;

import io.gamemachine.core.PersistableMessage;


import io.gamemachine.objectdb.Cache;
import io.gamemachine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class NpcData implements Externalizable, Message<NpcData>, Schema<NpcData>, PersistableMessage{

	public enum NpcType implements io.protostuff.EnumLite<NpcType>
    {
    	
    	    	None(0),    	    	Guard(1),    	    	Bandit(2),    	    	Animal(3),    	    	Civilian(4);    	        
        public final int number;
        
        private NpcType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static NpcType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (None);
    			    			case 1: return (Guard);
    			    			case 2: return (Bandit);
    			    			case 3: return (Animal);
    			    			case 4: return (Civilian);
    			                default: return null;
            }
        }
    }


    public static Schema<NpcData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static NpcData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final NpcData DEFAULT_INSTANCE = new NpcData();
    static final String defaultScope = NpcData.class.getSimpleName();

    	
	    	    public String id= null;
	    		
    
        	
	    	    public GmVector3 spawnpoint;
	    		
    
        	
	    	    public String leader= null;
	    		
    
            public List<String> followers;
	    	
	    	    public String patrolRoute= null;
	    		
    
        	
	    	    public Waypoint waypoint;
	    		
    
        	
	    	    public NpcType npcType;
	    		
    
        
	public static NpcDataCache cache() {
		return NpcDataCache.getInstance();
	}
	
	public static NpcDataStore store() {
		return NpcDataStore.getInstance();
	}


    public NpcData()
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
		
		public NpcData result(int timeout) {
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
	
	public static class NpcDataCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, NpcData> cache = new Cache<String, NpcData>(120, 5000);
		
		private NpcDataCache() {
		}
		
		private static class LazyHolder {
			private static final NpcDataCache INSTANCE = new NpcDataCache();
		}
	
		public static NpcDataCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, NpcData>(expiration, size);
		}
	
		public Cache<String, NpcData> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(NpcDataCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(NpcData message) {
			CacheUpdate cacheUpdate = new CacheUpdate(NpcDataCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public NpcData get(String id, int timeout) {
			NpcData message = cache.get(id);
			if (message == null) {
				message = NpcData.store().get(id, timeout);
			}
			return message;
		}
			
		public static NpcData setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			NpcData message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (NpcData) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				NpcData.store().set(message);
			} else {
				message = NpcData.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, NpcData.class.getField(field));
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
				NpcData.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class NpcDataStore {
	
		private NpcDataStore() {
		}
		
		private static class LazyHolder {
			private static final NpcDataStore INSTANCE = new NpcDataStore();
		}
	
		public static NpcDataStore getInstance() {
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
		
	    public void set(NpcData message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public NpcData get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, NpcData message) {
	    	NpcData clone = message.clone();
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
			
		public NpcData get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("NpcData");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			NpcData message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof NpcData) {
					message = (NpcData)result;
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
    	    	    	    	    	    	model.set("npc_data_id",null);
    	    	    	    	    	    	    	model.set("npc_data_leader",null);
    	    	    	    	    	    	    	    	model.set("npc_data_patrol_route",null);
    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("npc_data_id",id);
    	        		
    	//}
    	    	    	    	    	    	
    	    	    	//if (leader != null) {
    	       	    	model.setString("npc_data_leader",leader);
    	        		
    	//}
    	    	    	    	    	    	    	
    	    	    	//if (patrolRoute != null) {
    	       	    	model.setString("npc_data_patrol_route",patrolRoute);
    	        		
    	//}
    	    	    	    	    }
    
	public static NpcData fromModel(Model model) {
		boolean hasFields = false;
    	NpcData message = new NpcData();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("npc_data_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	    	
    	    	    	String leaderTestField = model.getString("npc_data_leader");
    	if (leaderTestField != null) {
    		String leaderField = leaderTestField;
    		message.setLeader(leaderField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	    	    	
    	    	    	String patrolRouteTestField = model.getString("npc_data_patrol_route");
    	if (patrolRouteTestField != null) {
    		String patrolRouteField = patrolRouteTestField;
    		message.setPatrolRoute(patrolRouteField);
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
	
	public NpcData setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public GmVector3 getSpawnpoint() {
		return spawnpoint;
	}
	
	public NpcData setSpawnpoint(GmVector3 spawnpoint) {
		this.spawnpoint = spawnpoint;
		return this;	}
	
		            
		public String getLeader() {
		return leader;
	}
	
	public NpcData setLeader(String leader) {
		this.leader = leader;
		return this;	}
	
		            
		public List<String> getFollowersList() {
		if(this.followers == null)
            this.followers = new ArrayList<String>();
		return followers;
	}

	public NpcData setFollowersList(List<String> followers) {
		this.followers = followers;
		return this;
	}

	public String getFollowers(int index)  {
        return followers == null ? null : followers.get(index);
    }

    public int getFollowersCount()  {
        return followers == null ? 0 : followers.size();
    }

    public NpcData addFollowers(String followers)  {
        if(this.followers == null)
            this.followers = new ArrayList<String>();
        this.followers.add(followers);
        return this;
    }
        	
    
    
    
		            
		public String getPatrolRoute() {
		return patrolRoute;
	}
	
	public NpcData setPatrolRoute(String patrolRoute) {
		this.patrolRoute = patrolRoute;
		return this;	}
	
		            
		public Waypoint getWaypoint() {
		return waypoint;
	}
	
	public NpcData setWaypoint(Waypoint waypoint) {
		this.waypoint = waypoint;
		return this;	}
	
		            
		public NpcType getNpcType() {
		return npcType;
	}
	
	public NpcData setNpcType(NpcType npcType) {
		this.npcType = npcType;
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

    public Schema<NpcData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public NpcData newMessage()
    {
        return new NpcData();
    }

    public Class<NpcData> typeClass()
    {
        return NpcData.class;
    }

    public String messageName()
    {
        return NpcData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return NpcData.class.getName();
    }

    public boolean isInitialized(NpcData message)
    {
        return true;
    }

    public void mergeFrom(Input input, NpcData message) throws IOException
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
            	                	                	message.spawnpoint = input.mergeObject(message.spawnpoint, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.leader = input.readString();
                	break;
                	                	
                            	            	case 4:
            	            		if(message.followers == null)
                        message.followers = new ArrayList<String>();
                                    	message.followers.add(input.readString());
                	                    break;
                            	            	case 5:
            	                	                	message.patrolRoute = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.waypoint = input.mergeObject(message.waypoint, Waypoint.getSchema());
                    break;
                                    	
                            	            	case 7:
            	                	                    message.npcType = NpcType.valueOf(input.readEnum());
                    break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, NpcData message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.spawnpoint != null)
    		output.writeObject(2, message.spawnpoint, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.leader != null) {
            output.writeString(3, message.leader, false);
        }
    	    	
    	            	
    	    	
    	    	if(message.followers != null)
        {
            for(String followers : message.followers)
            {
                if( (String) followers != null) {
                   	            		output.writeString(4, followers, true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if( (String)message.patrolRoute != null) {
            output.writeString(5, message.patrolRoute, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.waypoint != null)
    		output.writeObject(6, message.waypoint, Waypoint.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.npcType != null)
    	 	output.writeEnum(7, message.npcType.number, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START NpcData");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.spawnpoint != null) {
    		System.out.println("spawnpoint="+this.spawnpoint);
    	//}
    	    	//if(this.leader != null) {
    		System.out.println("leader="+this.leader);
    	//}
    	    	//if(this.followers != null) {
    		System.out.println("followers="+this.followers);
    	//}
    	    	//if(this.patrolRoute != null) {
    		System.out.println("patrolRoute="+this.patrolRoute);
    	//}
    	    	//if(this.waypoint != null) {
    		System.out.println("waypoint="+this.waypoint);
    	//}
    	    	//if(this.npcType != null) {
    		System.out.println("npcType="+this.npcType);
    	//}
    	    	System.out.println("END NpcData");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "spawnpoint";
        	        	case 3: return "leader";
        	        	case 4: return "followers";
        	        	case 5: return "patrolRoute";
        	        	case 6: return "waypoint";
        	        	case 7: return "npcType";
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
    	    	__fieldMap.put("spawnpoint", 2);
    	    	__fieldMap.put("leader", 3);
    	    	__fieldMap.put("followers", 4);
    	    	__fieldMap.put("patrolRoute", 5);
    	    	__fieldMap.put("waypoint", 6);
    	    	__fieldMap.put("npcType", 7);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = NpcData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static NpcData parseFrom(byte[] bytes) {
	NpcData message = new NpcData();
	ProtobufIOUtil.mergeFrom(bytes, message, NpcData.getSchema());
	return message;
}

public static NpcData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	NpcData message = new NpcData();
	JsonIOUtil.mergeFrom(bytes, message, NpcData.getSchema(), false);
	return message;
}

public NpcData clone() {
	byte[] bytes = this.toByteArray();
	NpcData npcData = NpcData.parseFrom(bytes);
	return npcData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, NpcData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<NpcData> schema = NpcData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, NpcData.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, NpcData.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
