
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
public final class GmStats implements Externalizable, Message<GmStats>, Schema<GmStats>, PersistableMessage{



    public static Schema<GmStats> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GmStats getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GmStats DEFAULT_INSTANCE = new GmStats();
    static final String defaultScope = GmStats.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
							    public String action= null;
		    			    
		
    
        	
							    public long messageCountIn= 0L;
		    			    
		
    
        	
							    public long messageCountOut= 0L;
		    			    
		
    
        	
							    public long messageCountInOut= 0L;
		    			    
		
    
        	
							    public long bytesOut= 0L;
		    			    
		
    
        	
							    public int connectionCount= 0;
		    			    
		
    
        	
							    public long playerBytesOut= 0L;
		    			    
		
    
        	
							    public String playerId= null;
		    			    
		
    
        	
							    public long bytesPerMessageOut= 0L;
		    			    
		
    
        
	public static GmStatsCache cache() {
		return GmStatsCache.getInstance();
	}
	
	public static GmStatsStore store() {
		return GmStatsStore.getInstance();
	}


    public GmStats()
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
		
		public GmStats result(int timeout) {
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
	
	public static class GmStatsCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, GmStats> cache = new Cache<String, GmStats>(120, 5000);
		
		private GmStatsCache() {
		}
		
		private static class LazyHolder {
			private static final GmStatsCache INSTANCE = new GmStatsCache();
		}
	
		public static GmStatsCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, GmStats>(expiration, size);
		}
	
		public Cache<String, GmStats> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(GmStatsCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(GmStats message) {
			CacheUpdate cacheUpdate = new CacheUpdate(GmStatsCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public GmStats get(String id, int timeout) {
			GmStats message = cache.get(id);
			if (message == null) {
				message = GmStats.store().get(id, timeout);
			}
			return message;
		}
			
		public static GmStats setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			GmStats message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (GmStats) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				GmStats.store().set(message);
			} else {
				message = GmStats.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, GmStats.class.getField(field));
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
				GmStats.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class GmStatsStore {
	
		private GmStatsStore() {
		}
		
		private static class LazyHolder {
			private static final GmStatsStore INSTANCE = new GmStatsStore();
		}
	
		public static GmStatsStore getInstance() {
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
		
	    public void set(GmStats message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public GmStats get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, GmStats message) {
	    	GmStats clone = message.clone();
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
			
		public GmStats get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("GmStats");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			GmStats message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof GmStats) {
					message = (GmStats)result;
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
    	    	    	    	    	    	model.set("gm_stats_id",null);
    	    	    	    	    	    	model.set("gm_stats_action",null);
    	    	    	    	    	    	model.set("gm_stats_message_count_in",null);
    	    	    	    	    	    	model.set("gm_stats_message_count_out",null);
    	    	    	    	    	    	model.set("gm_stats_message_count_in_out",null);
    	    	    	    	    	    	model.set("gm_stats_bytes_out",null);
    	    	    	    	    	    	model.set("gm_stats_connection_count",null);
    	    	    	    	    	    	model.set("gm_stats_player_bytes_out",null);
    	    	    	    	    	    	model.set("gm_stats_player_id",null);
    	    	    	    	    	    	model.set("gm_stats_bytes_per_message_out",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("gm_stats_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (action != null) {
    	       	    	model.setString("gm_stats_action",action);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (messageCountIn != null) {
    	       	    	model.setLong("gm_stats_message_count_in",messageCountIn);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (messageCountOut != null) {
    	       	    	model.setLong("gm_stats_message_count_out",messageCountOut);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (messageCountInOut != null) {
    	       	    	model.setLong("gm_stats_message_count_in_out",messageCountInOut);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (bytesOut != null) {
    	       	    	model.setLong("gm_stats_bytes_out",bytesOut);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (connectionCount != null) {
    	       	    	model.setInteger("gm_stats_connection_count",connectionCount);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (playerBytesOut != null) {
    	       	    	model.setLong("gm_stats_player_bytes_out",playerBytesOut);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("gm_stats_player_id",playerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (bytesPerMessageOut != null) {
    	       	    	model.setLong("gm_stats_bytes_per_message_out",bytesPerMessageOut);
    	        		
    	//}
    	    	    }
    
	public static GmStats fromModel(Model model) {
		boolean hasFields = false;
    	GmStats message = new GmStats();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("gm_stats_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String actionTestField = model.getString("gm_stats_action");
    	if (actionTestField != null) {
    		String actionField = actionTestField;
    		message.setAction(actionField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Long messageCountInTestField = model.getLong("gm_stats_message_count_in");
    	if (messageCountInTestField != null) {
    		long messageCountInField = messageCountInTestField;
    		message.setMessageCountIn(messageCountInField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Long messageCountOutTestField = model.getLong("gm_stats_message_count_out");
    	if (messageCountOutTestField != null) {
    		long messageCountOutField = messageCountOutTestField;
    		message.setMessageCountOut(messageCountOutField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Long messageCountInOutTestField = model.getLong("gm_stats_message_count_in_out");
    	if (messageCountInOutTestField != null) {
    		long messageCountInOutField = messageCountInOutTestField;
    		message.setMessageCountInOut(messageCountInOutField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Long bytesOutTestField = model.getLong("gm_stats_bytes_out");
    	if (bytesOutTestField != null) {
    		long bytesOutField = bytesOutTestField;
    		message.setBytesOut(bytesOutField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer connectionCountTestField = model.getInteger("gm_stats_connection_count");
    	if (connectionCountTestField != null) {
    		int connectionCountField = connectionCountTestField;
    		message.setConnectionCount(connectionCountField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Long playerBytesOutTestField = model.getLong("gm_stats_player_bytes_out");
    	if (playerBytesOutTestField != null) {
    		long playerBytesOutField = playerBytesOutTestField;
    		message.setPlayerBytesOut(playerBytesOutField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String playerIdTestField = model.getString("gm_stats_player_id");
    	if (playerIdTestField != null) {
    		String playerIdField = playerIdTestField;
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Long bytesPerMessageOutTestField = model.getLong("gm_stats_bytes_per_message_out");
    	if (bytesPerMessageOutTestField != null) {
    		long bytesPerMessageOutField = bytesPerMessageOutTestField;
    		message.setBytesPerMessageOut(bytesPerMessageOutField);
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
	
	public GmStats setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getAction() {
		return action;
	}
	
	public GmStats setAction(String action) {
		this.action = action;
		return this;	}
	
		            
		public long getMessageCountIn() {
		return messageCountIn;
	}
	
	public GmStats setMessageCountIn(long messageCountIn) {
		this.messageCountIn = messageCountIn;
		return this;	}
	
		            
		public long getMessageCountOut() {
		return messageCountOut;
	}
	
	public GmStats setMessageCountOut(long messageCountOut) {
		this.messageCountOut = messageCountOut;
		return this;	}
	
		            
		public long getMessageCountInOut() {
		return messageCountInOut;
	}
	
	public GmStats setMessageCountInOut(long messageCountInOut) {
		this.messageCountInOut = messageCountInOut;
		return this;	}
	
		            
		public long getBytesOut() {
		return bytesOut;
	}
	
	public GmStats setBytesOut(long bytesOut) {
		this.bytesOut = bytesOut;
		return this;	}
	
		            
		public int getConnectionCount() {
		return connectionCount;
	}
	
	public GmStats setConnectionCount(int connectionCount) {
		this.connectionCount = connectionCount;
		return this;	}
	
		            
		public long getPlayerBytesOut() {
		return playerBytesOut;
	}
	
	public GmStats setPlayerBytesOut(long playerBytesOut) {
		this.playerBytesOut = playerBytesOut;
		return this;	}
	
		            
		public String getPlayerId() {
		return playerId;
	}
	
	public GmStats setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		            
		public long getBytesPerMessageOut() {
		return bytesPerMessageOut;
	}
	
	public GmStats setBytesPerMessageOut(long bytesPerMessageOut) {
		this.bytesPerMessageOut = bytesPerMessageOut;
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

    public Schema<GmStats> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GmStats newMessage()
    {
        return new GmStats();
    }

    public Class<GmStats> typeClass()
    {
        return GmStats.class;
    }

    public String messageName()
    {
        return GmStats.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GmStats.class.getName();
    }

    public boolean isInitialized(GmStats message)
    {
        return true;
    }

    public void mergeFrom(Input input, GmStats message) throws IOException
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
            	                	                	message.action = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.messageCountIn = input.readInt64();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.messageCountOut = input.readInt64();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.messageCountInOut = input.readInt64();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.bytesOut = input.readInt64();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.connectionCount = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.playerBytesOut = input.readInt64();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.bytesPerMessageOut = input.readInt64();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GmStats message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.action != null) {
            output.writeString(2, message.action, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.messageCountIn != null) {
            output.writeInt64(3, message.messageCountIn, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.messageCountOut != null) {
            output.writeInt64(4, message.messageCountOut, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.messageCountInOut != null) {
            output.writeInt64(5, message.messageCountInOut, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.bytesOut != null) {
            output.writeInt64(6, message.bytesOut, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.connectionCount != null) {
            output.writeInt32(7, message.connectionCount, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.playerBytesOut != null) {
            output.writeInt64(8, message.playerBytesOut, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(9, message.playerId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.bytesPerMessageOut != null) {
            output.writeInt64(10, message.bytesPerMessageOut, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GmStats");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.action != null) {
    		System.out.println("action="+this.action);
    	//}
    	    	//if(this.messageCountIn != null) {
    		System.out.println("messageCountIn="+this.messageCountIn);
    	//}
    	    	//if(this.messageCountOut != null) {
    		System.out.println("messageCountOut="+this.messageCountOut);
    	//}
    	    	//if(this.messageCountInOut != null) {
    		System.out.println("messageCountInOut="+this.messageCountInOut);
    	//}
    	    	//if(this.bytesOut != null) {
    		System.out.println("bytesOut="+this.bytesOut);
    	//}
    	    	//if(this.connectionCount != null) {
    		System.out.println("connectionCount="+this.connectionCount);
    	//}
    	    	//if(this.playerBytesOut != null) {
    		System.out.println("playerBytesOut="+this.playerBytesOut);
    	//}
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	//if(this.bytesPerMessageOut != null) {
    		System.out.println("bytesPerMessageOut="+this.bytesPerMessageOut);
    	//}
    	    	System.out.println("END GmStats");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "action";
        	        	case 3: return "messageCountIn";
        	        	case 4: return "messageCountOut";
        	        	case 5: return "messageCountInOut";
        	        	case 6: return "bytesOut";
        	        	case 7: return "connectionCount";
        	        	case 8: return "playerBytesOut";
        	        	case 9: return "playerId";
        	        	case 10: return "bytesPerMessageOut";
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
    	    	__fieldMap.put("action", 2);
    	    	__fieldMap.put("messageCountIn", 3);
    	    	__fieldMap.put("messageCountOut", 4);
    	    	__fieldMap.put("messageCountInOut", 5);
    	    	__fieldMap.put("bytesOut", 6);
    	    	__fieldMap.put("connectionCount", 7);
    	    	__fieldMap.put("playerBytesOut", 8);
    	    	__fieldMap.put("playerId", 9);
    	    	__fieldMap.put("bytesPerMessageOut", 10);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GmStats.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GmStats parseFrom(byte[] bytes) {
	GmStats message = new GmStats();
	ProtobufIOUtil.mergeFrom(bytes, message, GmStats.getSchema());
	return message;
}

public static GmStats parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GmStats message = new GmStats();
	JsonIOUtil.mergeFrom(bytes, message, GmStats.getSchema(), false);
	return message;
}

public GmStats clone() {
	byte[] bytes = this.toByteArray();
	GmStats gmStats = GmStats.parseFrom(bytes);
	return gmStats;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GmStats.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GmStats> schema = GmStats.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GmStats.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GmStats.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
