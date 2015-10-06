
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
public final class ClientConnection implements Externalizable, Message<ClientConnection>, Schema<ClientConnection>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);



    public static Schema<ClientConnection> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ClientConnection getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ClientConnection DEFAULT_INSTANCE = new ClientConnection();
    static final String defaultScope = ClientConnection.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
							    public String gateway= null;
		    			    
		
    
        	
							    public String server= null;
		    			    
		
    
        	
							    public String type= null;
		    			    
		
    
        
	public static ClientConnectionCache cache() {
		return ClientConnectionCache.getInstance();
	}
	
	public static ClientConnectionStore store() {
		return ClientConnectionStore.getInstance();
	}


    public ClientConnection()
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
		
		public ClientConnection result(int timeout) {
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
	
	public static class ClientConnectionCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, ClientConnection> cache = new Cache<String, ClientConnection>(120, 5000);
		
		private ClientConnectionCache() {
		}
		
		private static class LazyHolder {
			private static final ClientConnectionCache INSTANCE = new ClientConnectionCache();
		}
	
		public static ClientConnectionCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, ClientConnection>(expiration, size);
		}
	
		public Cache<String, ClientConnection> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(ClientConnectionCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(ClientConnection message) {
			CacheUpdate cacheUpdate = new CacheUpdate(ClientConnectionCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public ClientConnection get(String id, int timeout) {
			ClientConnection message = cache.get(id);
			if (message == null) {
				message = ClientConnection.store().get(id, timeout);
			}
			return message;
		}
			
		public static ClientConnection setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			ClientConnection message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (ClientConnection) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				ClientConnection.store().set(message);
			} else {
				message = ClientConnection.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, ClientConnection.class.getField(field));
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
				ClientConnection.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class ClientConnectionStore {
	
		private ClientConnectionStore() {
		}
		
		private static class LazyHolder {
			private static final ClientConnectionStore INSTANCE = new ClientConnectionStore();
		}
	
		public static ClientConnectionStore getInstance() {
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
		
	    public void set(ClientConnection message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public ClientConnection get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, ClientConnection message) {
	    	ClientConnection clone = message.clone();
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
			
		public ClientConnection get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("ClientConnection");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			ClientConnection message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof ClientConnection) {
					message = (ClientConnection)result;
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
    	    	    	    	    	    	model.set("client_connection_id",null);
    	    	    	    	    	    	model.set("client_connection_gateway",null);
    	    	    	    	    	    	model.set("client_connection_server",null);
    	    	    	    	    	    	model.set("client_connection_type",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("client_connection_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (gateway != null) {
    	       	    	model.setString("client_connection_gateway",gateway);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (server != null) {
    	       	    	model.setString("client_connection_server",server);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (type != null) {
    	       	    	model.setString("client_connection_type",type);
    	        		
    	//}
    	    	    }
    
	public static ClientConnection fromModel(Model model) {
		boolean hasFields = false;
    	ClientConnection message = new ClientConnection();
    	    	    	    	    	
    	    			String idTestField = model.getString("client_connection_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String gatewayTestField = model.getString("client_connection_gateway");
		if (gatewayTestField != null) {
			String gatewayField = gatewayTestField;
			message.setGateway(gatewayField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String serverTestField = model.getString("client_connection_server");
		if (serverTestField != null) {
			String serverField = serverTestField;
			message.setServer(serverField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String typeTestField = model.getString("client_connection_type");
		if (typeTestField != null) {
			String typeField = typeTestField;
			message.setType(typeField);
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
	
	public ClientConnection setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getGateway() {
		return gateway;
	}
	
	public ClientConnection setGateway(String gateway) {
		this.gateway = gateway;
		return this;	}
	
		            
		public String getServer() {
		return server;
	}
	
	public ClientConnection setServer(String server) {
		this.server = server;
		return this;	}
	
		            
		public String getType() {
		return type;
	}
	
	public ClientConnection setType(String type) {
		this.type = type;
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

    public Schema<ClientConnection> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ClientConnection newMessage()
    {
        return new ClientConnection();
    }

    public Class<ClientConnection> typeClass()
    {
        return ClientConnection.class;
    }

    public String messageName()
    {
        return ClientConnection.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ClientConnection.class.getName();
    }

    public boolean isInitialized(ClientConnection message)
    {
        return true;
    }

    public void mergeFrom(Input input, ClientConnection message) throws IOException
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
            	                	                	message.gateway = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.server = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.type = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ClientConnection message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.gateway != null) {
            output.writeString(2, message.gateway, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.server != null) {
            output.writeString(3, message.server, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.type != null) {
            output.writeString(4, message.type, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ClientConnection");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.gateway != null) {
    		System.out.println("gateway="+this.gateway);
    	//}
    	    	//if(this.server != null) {
    		System.out.println("server="+this.server);
    	//}
    	    	//if(this.type != null) {
    		System.out.println("type="+this.type);
    	//}
    	    	System.out.println("END ClientConnection");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "gateway";
        	        	case 3: return "server";
        	        	case 4: return "type";
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
    	    	__fieldMap.put("gateway", 2);
    	    	__fieldMap.put("server", 3);
    	    	__fieldMap.put("type", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ClientConnection.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ClientConnection parseFrom(byte[] bytes) {
	ClientConnection message = new ClientConnection();
	ProtobufIOUtil.mergeFrom(bytes, message, ClientConnection.getSchema());
	return message;
}

public static ClientConnection parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ClientConnection message = new ClientConnection();
	JsonIOUtil.mergeFrom(bytes, message, ClientConnection.getSchema(), false);
	return message;
}

public ClientConnection clone() {
	byte[] bytes = this.toByteArray();
	ClientConnection clientConnection = ClientConnection.parseFrom(bytes);
	return clientConnection;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ClientConnection.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ClientConnection> schema = ClientConnection.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ClientConnection.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ClientConnection.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
