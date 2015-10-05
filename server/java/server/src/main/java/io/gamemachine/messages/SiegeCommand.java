
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
public final class SiegeCommand implements Externalizable, Message<SiegeCommand>, Schema<SiegeCommand>, PersistableMessage{



    public static Schema<SiegeCommand> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static SiegeCommand getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final SiegeCommand DEFAULT_INSTANCE = new SiegeCommand();
    static final String defaultScope = SiegeCommand.class.getSimpleName();

    	
							    public int startLoad= 0;
		    			    
		
    
        	
							    public int fire= 0;
		    			    
		
    
        	
							    public float force= 0F;
		    			    
		
    
        	
							    public String id= null;
		    			    
		
    
        	
							    public String hitId= null;
		    			    
		
    
        	
					public GmVector3 hit = null;
			    
		
    
        	
							    public String skillId= null;
		    			    
		
    
        	
							    public int startUse= 0;
		    			    
		
    
        	
							    public int endUse= 0;
		    			    
		
    
        	
							    public String playerId= null;
		    			    
		
    
        	
							    public String targetType= null;
		    			    
		
    
        	
							    public String targetId= null;
		    			    
		
    
        
	public static SiegeCommandCache cache() {
		return SiegeCommandCache.getInstance();
	}
	
	public static SiegeCommandStore store() {
		return SiegeCommandStore.getInstance();
	}


    public SiegeCommand()
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
		
		public SiegeCommand result(int timeout) {
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
	
	public static class SiegeCommandCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, SiegeCommand> cache = new Cache<String, SiegeCommand>(120, 5000);
		
		private SiegeCommandCache() {
		}
		
		private static class LazyHolder {
			private static final SiegeCommandCache INSTANCE = new SiegeCommandCache();
		}
	
		public static SiegeCommandCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, SiegeCommand>(expiration, size);
		}
	
		public Cache<String, SiegeCommand> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(SiegeCommandCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(SiegeCommand message) {
			CacheUpdate cacheUpdate = new CacheUpdate(SiegeCommandCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public SiegeCommand get(String id, int timeout) {
			SiegeCommand message = cache.get(id);
			if (message == null) {
				message = SiegeCommand.store().get(id, timeout);
			}
			return message;
		}
			
		public static SiegeCommand setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			SiegeCommand message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (SiegeCommand) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				SiegeCommand.store().set(message);
			} else {
				message = SiegeCommand.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, SiegeCommand.class.getField(field));
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
				SiegeCommand.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class SiegeCommandStore {
	
		private SiegeCommandStore() {
		}
		
		private static class LazyHolder {
			private static final SiegeCommandStore INSTANCE = new SiegeCommandStore();
		}
	
		public static SiegeCommandStore getInstance() {
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
		
	    public void set(SiegeCommand message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public SiegeCommand get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, SiegeCommand message) {
	    	SiegeCommand clone = message.clone();
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
			
		public SiegeCommand get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("SiegeCommand");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			SiegeCommand message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof SiegeCommand) {
					message = (SiegeCommand)result;
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
    	    	    	    	    	    	model.set("siege_command_start_load",null);
    	    	    	    	    	    	model.set("siege_command_fire",null);
    	    	    	    	    	    	model.set("siege_command_force",null);
    	    	    	    	    	    	model.set("siege_command_id",null);
    	    	    	    	    	    	model.set("siege_command_hit_id",null);
    	    	    	    	    	    	    	model.set("siege_command_skill_id",null);
    	    	    	    	    	    	model.set("siege_command_start_use",null);
    	    	    	    	    	    	model.set("siege_command_end_use",null);
    	    	    	    	    	    	model.set("siege_command_player_id",null);
    	    	    	    	    	    	model.set("siege_command_target_type",null);
    	    	    	    	    	    	model.set("siege_command_target_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (startLoad != null) {
    	       	    	model.setInteger("siege_command_start_load",startLoad);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (fire != null) {
    	       	    	model.setInteger("siege_command_fire",fire);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (force != null) {
    	       	    	model.setFloat("siege_command_force",force);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("siege_command_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (hitId != null) {
    	       	    	model.setString("siege_command_hit_id",hitId);
    	        		
    	//}
    	    	    	    	    	    	
    	    	    	//if (skillId != null) {
    	       	    	model.setString("siege_command_skill_id",skillId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (startUse != null) {
    	       	    	model.setInteger("siege_command_start_use",startUse);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (endUse != null) {
    	       	    	model.setInteger("siege_command_end_use",endUse);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("siege_command_player_id",playerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (targetType != null) {
    	       	    	model.setString("siege_command_target_type",targetType);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (targetId != null) {
    	       	    	model.setString("siege_command_target_id",targetId);
    	        		
    	//}
    	    	    }
    
	public static SiegeCommand fromModel(Model model) {
		boolean hasFields = false;
    	SiegeCommand message = new SiegeCommand();
    	    	    	    	    	
    	    	    	Integer startLoadTestField = model.getInteger("siege_command_start_load");
    	if (startLoadTestField != null) {
    		int startLoadField = startLoadTestField;
    		message.setStartLoad(startLoadField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer fireTestField = model.getInteger("siege_command_fire");
    	if (fireTestField != null) {
    		int fireField = fireTestField;
    		message.setFire(fireField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Float forceTestField = model.getFloat("siege_command_force");
    	if (forceTestField != null) {
    		float forceField = forceTestField;
    		message.setForce(forceField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String idTestField = model.getString("siege_command_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String hitIdTestField = model.getString("siege_command_hit_id");
    	if (hitIdTestField != null) {
    		String hitIdField = hitIdTestField;
    		message.setHitId(hitIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	    	
    	    	    	String skillIdTestField = model.getString("siege_command_skill_id");
    	if (skillIdTestField != null) {
    		String skillIdField = skillIdTestField;
    		message.setSkillId(skillIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer startUseTestField = model.getInteger("siege_command_start_use");
    	if (startUseTestField != null) {
    		int startUseField = startUseTestField;
    		message.setStartUse(startUseField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer endUseTestField = model.getInteger("siege_command_end_use");
    	if (endUseTestField != null) {
    		int endUseField = endUseTestField;
    		message.setEndUse(endUseField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String playerIdTestField = model.getString("siege_command_player_id");
    	if (playerIdTestField != null) {
    		String playerIdField = playerIdTestField;
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String targetTypeTestField = model.getString("siege_command_target_type");
    	if (targetTypeTestField != null) {
    		String targetTypeField = targetTypeTestField;
    		message.setTargetType(targetTypeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String targetIdTestField = model.getString("siege_command_target_id");
    	if (targetIdTestField != null) {
    		String targetIdField = targetIdTestField;
    		message.setTargetId(targetIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public int getStartLoad() {
		return startLoad;
	}
	
	public SiegeCommand setStartLoad(int startLoad) {
		this.startLoad = startLoad;
		return this;	}
	
		            
		public int getFire() {
		return fire;
	}
	
	public SiegeCommand setFire(int fire) {
		this.fire = fire;
		return this;	}
	
		            
		public float getForce() {
		return force;
	}
	
	public SiegeCommand setForce(float force) {
		this.force = force;
		return this;	}
	
		            
		public String getId() {
		return id;
	}
	
	public SiegeCommand setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getHitId() {
		return hitId;
	}
	
	public SiegeCommand setHitId(String hitId) {
		this.hitId = hitId;
		return this;	}
	
		            
		public GmVector3 getHit() {
		return hit;
	}
	
	public SiegeCommand setHit(GmVector3 hit) {
		this.hit = hit;
		return this;	}
	
		            
		public String getSkillId() {
		return skillId;
	}
	
	public SiegeCommand setSkillId(String skillId) {
		this.skillId = skillId;
		return this;	}
	
		            
		public int getStartUse() {
		return startUse;
	}
	
	public SiegeCommand setStartUse(int startUse) {
		this.startUse = startUse;
		return this;	}
	
		            
		public int getEndUse() {
		return endUse;
	}
	
	public SiegeCommand setEndUse(int endUse) {
		this.endUse = endUse;
		return this;	}
	
		            
		public String getPlayerId() {
		return playerId;
	}
	
	public SiegeCommand setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		            
		public String getTargetType() {
		return targetType;
	}
	
	public SiegeCommand setTargetType(String targetType) {
		this.targetType = targetType;
		return this;	}
	
		            
		public String getTargetId() {
		return targetId;
	}
	
	public SiegeCommand setTargetId(String targetId) {
		this.targetId = targetId;
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

    public Schema<SiegeCommand> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public SiegeCommand newMessage()
    {
        return new SiegeCommand();
    }

    public Class<SiegeCommand> typeClass()
    {
        return SiegeCommand.class;
    }

    public String messageName()
    {
        return SiegeCommand.class.getSimpleName();
    }

    public String messageFullName()
    {
        return SiegeCommand.class.getName();
    }

    public boolean isInitialized(SiegeCommand message)
    {
        return true;
    }

    public void mergeFrom(Input input, SiegeCommand message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.startLoad = input.readInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.fire = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.force = input.readFloat();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.hitId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.hit = input.mergeObject(message.hit, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 7:
            	                	                	message.skillId = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.startUse = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.endUse = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.targetType = input.readString();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.targetId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, SiegeCommand message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (Integer)message.startLoad != null) {
            output.writeInt32(1, message.startLoad, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.fire != null) {
            output.writeInt32(2, message.fire, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Float)message.force != null) {
            output.writeFloat(3, message.force, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(4, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.hitId != null) {
            output.writeString(5, message.hitId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.hit != null)
    		output.writeObject(6, message.hit, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.skillId != null) {
            output.writeString(7, message.skillId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.startUse != null) {
            output.writeInt32(8, message.startUse, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.endUse != null) {
            output.writeInt32(9, message.endUse, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(10, message.playerId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.targetType != null) {
            output.writeString(11, message.targetType, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.targetId != null) {
            output.writeString(12, message.targetId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START SiegeCommand");
    	    	//if(this.startLoad != null) {
    		System.out.println("startLoad="+this.startLoad);
    	//}
    	    	//if(this.fire != null) {
    		System.out.println("fire="+this.fire);
    	//}
    	    	//if(this.force != null) {
    		System.out.println("force="+this.force);
    	//}
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.hitId != null) {
    		System.out.println("hitId="+this.hitId);
    	//}
    	    	//if(this.hit != null) {
    		System.out.println("hit="+this.hit);
    	//}
    	    	//if(this.skillId != null) {
    		System.out.println("skillId="+this.skillId);
    	//}
    	    	//if(this.startUse != null) {
    		System.out.println("startUse="+this.startUse);
    	//}
    	    	//if(this.endUse != null) {
    		System.out.println("endUse="+this.endUse);
    	//}
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	//if(this.targetType != null) {
    		System.out.println("targetType="+this.targetType);
    	//}
    	    	//if(this.targetId != null) {
    		System.out.println("targetId="+this.targetId);
    	//}
    	    	System.out.println("END SiegeCommand");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "startLoad";
        	        	case 2: return "fire";
        	        	case 3: return "force";
        	        	case 4: return "id";
        	        	case 5: return "hitId";
        	        	case 6: return "hit";
        	        	case 7: return "skillId";
        	        	case 8: return "startUse";
        	        	case 9: return "endUse";
        	        	case 10: return "playerId";
        	        	case 11: return "targetType";
        	        	case 12: return "targetId";
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
    	    	__fieldMap.put("startLoad", 1);
    	    	__fieldMap.put("fire", 2);
    	    	__fieldMap.put("force", 3);
    	    	__fieldMap.put("id", 4);
    	    	__fieldMap.put("hitId", 5);
    	    	__fieldMap.put("hit", 6);
    	    	__fieldMap.put("skillId", 7);
    	    	__fieldMap.put("startUse", 8);
    	    	__fieldMap.put("endUse", 9);
    	    	__fieldMap.put("playerId", 10);
    	    	__fieldMap.put("targetType", 11);
    	    	__fieldMap.put("targetId", 12);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = SiegeCommand.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static SiegeCommand parseFrom(byte[] bytes) {
	SiegeCommand message = new SiegeCommand();
	ProtobufIOUtil.mergeFrom(bytes, message, SiegeCommand.getSchema());
	return message;
}

public static SiegeCommand parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	SiegeCommand message = new SiegeCommand();
	JsonIOUtil.mergeFrom(bytes, message, SiegeCommand.getSchema(), false);
	return message;
}

public SiegeCommand clone() {
	byte[] bytes = this.toByteArray();
	SiegeCommand siegeCommand = SiegeCommand.parseFrom(bytes);
	return siegeCommand;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, SiegeCommand.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<SiegeCommand> schema = SiegeCommand.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, SiegeCommand.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, SiegeCommand.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
