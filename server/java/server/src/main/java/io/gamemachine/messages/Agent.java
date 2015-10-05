
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
public final class Agent implements Externalizable, Message<Agent>, Schema<Agent>, PersistableMessage{



    public static Schema<Agent> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Agent getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Agent DEFAULT_INSTANCE = new Agent();
    static final String defaultScope = Agent.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
							    public String code= null;
		    			    
		
    
        	
							    public String classname= null;
		    			    
		
    
        	
							    public boolean remove= false;
		    			    
		
    
        	
							    public String compileResult= null;
		    			    
		
    
        	
							    public int concurrency= 0;
		    			    
		
    
        
	public static AgentCache cache() {
		return AgentCache.getInstance();
	}
	
	public static AgentStore store() {
		return AgentStore.getInstance();
	}


    public Agent()
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
		
		public Agent result(int timeout) {
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
	
	public static class AgentCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Agent> cache = new Cache<String, Agent>(120, 5000);
		
		private AgentCache() {
		}
		
		private static class LazyHolder {
			private static final AgentCache INSTANCE = new AgentCache();
		}
	
		public static AgentCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Agent>(expiration, size);
		}
	
		public Cache<String, Agent> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(AgentCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Agent message) {
			CacheUpdate cacheUpdate = new CacheUpdate(AgentCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Agent get(String id, int timeout) {
			Agent message = cache.get(id);
			if (message == null) {
				message = Agent.store().get(id, timeout);
			}
			return message;
		}
			
		public static Agent setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Agent message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Agent) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Agent.store().set(message);
			} else {
				message = Agent.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Agent.class.getField(field));
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
				Agent.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class AgentStore {
	
		private AgentStore() {
		}
		
		private static class LazyHolder {
			private static final AgentStore INSTANCE = new AgentStore();
		}
	
		public static AgentStore getInstance() {
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
		
	    public void set(Agent message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Agent get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Agent message) {
	    	Agent clone = message.clone();
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
			
		public Agent get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Agent");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Agent message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Agent) {
					message = (Agent)result;
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
    	    	    	    	    	    	model.set("agent_id",null);
    	    	    	    	    	    	model.set("agent_code",null);
    	    	    	    	    	    	model.set("agent_classname",null);
    	    	    	    	    	    	model.set("agent_remove",null);
    	    	    	    	    	    	model.set("agent_compile_result",null);
    	    	    	    	    	    	model.set("agent_concurrency",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("agent_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (code != null) {
    	       	    	model.setString("agent_code",code);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (classname != null) {
    	       	    	model.setString("agent_classname",classname);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (remove != null) {
    	       	    	model.setBoolean("agent_remove",remove);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (compileResult != null) {
    	       	    	model.setString("agent_compile_result",compileResult);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (concurrency != null) {
    	       	    	model.setInteger("agent_concurrency",concurrency);
    	        		
    	//}
    	    	    }
    
	public static Agent fromModel(Model model) {
		boolean hasFields = false;
    	Agent message = new Agent();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("agent_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String codeTestField = model.getString("agent_code");
    	if (codeTestField != null) {
    		String codeField = codeTestField;
    		message.setCode(codeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String classnameTestField = model.getString("agent_classname");
    	if (classnameTestField != null) {
    		String classnameField = classnameTestField;
    		message.setClassname(classnameField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Boolean removeTestField = model.getBoolean("agent_remove");
    	if (removeTestField != null) {
    		boolean removeField = removeTestField;
    		message.setRemove(removeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String compileResultTestField = model.getString("agent_compile_result");
    	if (compileResultTestField != null) {
    		String compileResultField = compileResultTestField;
    		message.setCompileResult(compileResultField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer concurrencyTestField = model.getInteger("agent_concurrency");
    	if (concurrencyTestField != null) {
    		int concurrencyField = concurrencyTestField;
    		message.setConcurrency(concurrencyField);
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
	
	public Agent setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getCode() {
		return code;
	}
	
	public Agent setCode(String code) {
		this.code = code;
		return this;	}
	
		            
		public String getClassname() {
		return classname;
	}
	
	public Agent setClassname(String classname) {
		this.classname = classname;
		return this;	}
	
		            
		public boolean getRemove() {
		return remove;
	}
	
	public Agent setRemove(boolean remove) {
		this.remove = remove;
		return this;	}
	
		            
		public String getCompileResult() {
		return compileResult;
	}
	
	public Agent setCompileResult(String compileResult) {
		this.compileResult = compileResult;
		return this;	}
	
		            
		public int getConcurrency() {
		return concurrency;
	}
	
	public Agent setConcurrency(int concurrency) {
		this.concurrency = concurrency;
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

    public Schema<Agent> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Agent newMessage()
    {
        return new Agent();
    }

    public Class<Agent> typeClass()
    {
        return Agent.class;
    }

    public String messageName()
    {
        return Agent.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Agent.class.getName();
    }

    public boolean isInitialized(Agent message)
    {
        return true;
    }

    public void mergeFrom(Input input, Agent message) throws IOException
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
            	                	                	message.code = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.classname = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.remove = input.readBool();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.compileResult = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.concurrency = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Agent message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	//if(message.code == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.code != null) {
            output.writeString(2, message.code, false);
        }
    	    	
    	            	
    	    	//if(message.classname == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.classname != null) {
            output.writeString(3, message.classname, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.remove != null) {
            output.writeBool(5, message.remove, false);
        }
    	    	
    	            	
    	    	//if(message.compileResult == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.compileResult != null) {
            output.writeString(6, message.compileResult, false);
        }
    	    	
    	            	
    	    	//if(message.concurrency == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.concurrency != null) {
            output.writeInt32(7, message.concurrency, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Agent");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.code != null) {
    		System.out.println("code="+this.code);
    	//}
    	    	//if(this.classname != null) {
    		System.out.println("classname="+this.classname);
    	//}
    	    	//if(this.remove != null) {
    		System.out.println("remove="+this.remove);
    	//}
    	    	//if(this.compileResult != null) {
    		System.out.println("compileResult="+this.compileResult);
    	//}
    	    	//if(this.concurrency != null) {
    		System.out.println("concurrency="+this.concurrency);
    	//}
    	    	System.out.println("END Agent");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "code";
        	        	case 3: return "classname";
        	        	case 5: return "remove";
        	        	case 6: return "compileResult";
        	        	case 7: return "concurrency";
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
    	    	__fieldMap.put("code", 2);
    	    	__fieldMap.put("classname", 3);
    	    	__fieldMap.put("remove", 5);
    	    	__fieldMap.put("compileResult", 6);
    	    	__fieldMap.put("concurrency", 7);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Agent.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Agent parseFrom(byte[] bytes) {
	Agent message = new Agent();
	ProtobufIOUtil.mergeFrom(bytes, message, Agent.getSchema());
	return message;
}

public static Agent parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Agent message = new Agent();
	JsonIOUtil.mergeFrom(bytes, message, Agent.getSchema(), false);
	return message;
}

public Agent clone() {
	byte[] bytes = this.toByteArray();
	Agent agent = Agent.parseFrom(bytes);
	return agent;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Agent.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Agent> schema = Agent.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Agent.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Agent.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
