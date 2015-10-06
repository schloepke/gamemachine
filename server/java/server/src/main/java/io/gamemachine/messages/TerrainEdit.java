
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
public final class TerrainEdit implements Externalizable, Message<TerrainEdit>, Schema<TerrainEdit>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(TerrainEdit.class);

	public enum Type implements io.protostuff.EnumLite<Type>
    {
    	
    	    	NONE(0),    	    	DETAIL(1),    	    	ALPHA(2),    	    	HEIGHT(3),    	    	TREE(4);    	        
        public final int number;
        
        private Type (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Type defaultValue() {
        	return (NONE);
        }
        
        public static Type valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (DETAIL);
    			    			case 2: return (ALPHA);
    			    			case 3: return (HEIGHT);
    			    			case 4: return (TREE);
    			                default: return null;
            }
        }
    }


    public static Schema<TerrainEdit> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TerrainEdit getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TerrainEdit DEFAULT_INSTANCE = new TerrainEdit();
    static final String defaultScope = TerrainEdit.class.getSimpleName();

    	
							    public int x= 0;
		    			    
		
    
        	
							    public int y= 0;
		    			    
		
    
        	
							    public int detailLayer= 0;
		    			    
		
    
        	
							    public int value= 0;
		    			    
		
    
        	
					public Type type = Type.defaultValue();
			    
		
    
        	
							    public String id= null;
		    			    
		
    
        	
							    public int recordId= 0;
		    			    
		
    
        	
							    public int texture= 0;
		    			    
		
    
        	
							    public float height= 0F;
		    			    
		
    
        	
							    public long createdAt= 0L;
		    			    
		
    
        	
							    public String terrain= null;
		    			    
		
    
        
	public static TerrainEditCache cache() {
		return TerrainEditCache.getInstance();
	}
	
	public static TerrainEditStore store() {
		return TerrainEditStore.getInstance();
	}


    public TerrainEdit()
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
		
		public TerrainEdit result(int timeout) {
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
	
	public static class TerrainEditCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, TerrainEdit> cache = new Cache<String, TerrainEdit>(120, 5000);
		
		private TerrainEditCache() {
		}
		
		private static class LazyHolder {
			private static final TerrainEditCache INSTANCE = new TerrainEditCache();
		}
	
		public static TerrainEditCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, TerrainEdit>(expiration, size);
		}
	
		public Cache<String, TerrainEdit> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(TerrainEditCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(TerrainEdit message) {
			CacheUpdate cacheUpdate = new CacheUpdate(TerrainEditCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public TerrainEdit get(String id, int timeout) {
			TerrainEdit message = cache.get(id);
			if (message == null) {
				message = TerrainEdit.store().get(id, timeout);
			}
			return message;
		}
			
		public static TerrainEdit setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			TerrainEdit message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (TerrainEdit) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				TerrainEdit.store().set(message);
			} else {
				message = TerrainEdit.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, TerrainEdit.class.getField(field));
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
				TerrainEdit.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class TerrainEditStore {
	
		private TerrainEditStore() {
		}
		
		private static class LazyHolder {
			private static final TerrainEditStore INSTANCE = new TerrainEditStore();
		}
	
		public static TerrainEditStore getInstance() {
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
		
	    public void set(TerrainEdit message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public TerrainEdit get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, TerrainEdit message) {
	    	TerrainEdit clone = message.clone();
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
			
		public TerrainEdit get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("TerrainEdit");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			TerrainEdit message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof TerrainEdit) {
					message = (TerrainEdit)result;
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
    	    	    	    	    	    	model.set("terrain_edit_x",null);
    	    	    	    	    	    	model.set("terrain_edit_y",null);
    	    	    	    	    	    	model.set("terrain_edit_detail_layer",null);
    	    	    	    	    	    	model.set("terrain_edit_value",null);
    	    	    	    	    	    	    	model.set("terrain_edit_id",null);
    	    	    	    	    	    	    	    	    	model.set("terrain_edit_texture",null);
    	    	    	    	    	    	model.set("terrain_edit_height",null);
    	    	    	    	    	    	model.set("terrain_edit_created_at",null);
    	    	    	    	    	    	model.set("terrain_edit_terrain",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (x != null) {
    	       	    	model.setInteger("terrain_edit_x",x);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (y != null) {
    	       	    	model.setInteger("terrain_edit_y",y);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (detailLayer != null) {
    	       	    	model.setInteger("terrain_edit_detail_layer",detailLayer);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (value != null) {
    	       	    	model.setInteger("terrain_edit_value",value);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (type != null) {
    	       	    	model.setInteger("terrain_edit_type",type.ordinal());
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("terrain_edit_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//model.setInteger("id",recordId);
    	
    	    	    	    	    	
    	    	    	//if (texture != null) {
    	       	    	model.setInteger("terrain_edit_texture",texture);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (height != null) {
    	       	    	model.setFloat("terrain_edit_height",height);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (createdAt != null) {
    	       	    	model.setLong("terrain_edit_created_at",createdAt);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (terrain != null) {
    	       	    	model.setString("terrain_edit_terrain",terrain);
    	        		
    	//}
    	    	    }
    
	public static TerrainEdit fromModel(Model model) {
		boolean hasFields = false;
    	TerrainEdit message = new TerrainEdit();
    	    	    	    	    	
    	    			Integer xTestField = model.getInteger("terrain_edit_x");
		if (xTestField != null) {
			int xField = xTestField;
			message.setX(xField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer yTestField = model.getInteger("terrain_edit_y");
		if (yTestField != null) {
			int yField = yTestField;
			message.setY(yField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer detailLayerTestField = model.getInteger("terrain_edit_detail_layer");
		if (detailLayerTestField != null) {
			int detailLayerField = detailLayerTestField;
			message.setDetailLayer(detailLayerField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer valueTestField = model.getInteger("terrain_edit_value");
		if (valueTestField != null) {
			int valueField = valueTestField;
			message.setValue(valueField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    				message.setType(Type.valueOf(model.getInteger("terrain_edit_type")));
    	    	    	    	    	    	
    	    			String idTestField = model.getString("terrain_edit_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    			Integer textureTestField = model.getInteger("terrain_edit_texture");
		if (textureTestField != null) {
			int textureField = textureTestField;
			message.setTexture(textureField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Float heightTestField = model.getFloat("terrain_edit_height");
		if (heightTestField != null) {
			float heightField = heightTestField;
			message.setHeight(heightField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Long createdAtTestField = model.getLong("terrain_edit_created_at");
		if (createdAtTestField != null) {
			long createdAtField = createdAtTestField;
			message.setCreatedAt(createdAtField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String terrainTestField = model.getString("terrain_edit_terrain");
		if (terrainTestField != null) {
			String terrainField = terrainTestField;
			message.setTerrain(terrainField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public int getX() {
		return x;
	}
	
	public TerrainEdit setX(int x) {
		this.x = x;
		return this;	}
	
		            
		public int getY() {
		return y;
	}
	
	public TerrainEdit setY(int y) {
		this.y = y;
		return this;	}
	
		            
		public int getDetailLayer() {
		return detailLayer;
	}
	
	public TerrainEdit setDetailLayer(int detailLayer) {
		this.detailLayer = detailLayer;
		return this;	}
	
		            
		public int getValue() {
		return value;
	}
	
	public TerrainEdit setValue(int value) {
		this.value = value;
		return this;	}
	
		            
		public Type getType() {
		return type;
	}
	
	public TerrainEdit setType(Type type) {
		this.type = type;
		return this;	}
	
		            
		public String getId() {
		return id;
	}
	
	public TerrainEdit setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public TerrainEdit setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public int getTexture() {
		return texture;
	}
	
	public TerrainEdit setTexture(int texture) {
		this.texture = texture;
		return this;	}
	
		            
		public float getHeight() {
		return height;
	}
	
	public TerrainEdit setHeight(float height) {
		this.height = height;
		return this;	}
	
		            
		public long getCreatedAt() {
		return createdAt;
	}
	
	public TerrainEdit setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
		return this;	}
	
		            
		public String getTerrain() {
		return terrain;
	}
	
	public TerrainEdit setTerrain(String terrain) {
		this.terrain = terrain;
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

    public Schema<TerrainEdit> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TerrainEdit newMessage()
    {
        return new TerrainEdit();
    }

    public Class<TerrainEdit> typeClass()
    {
        return TerrainEdit.class;
    }

    public String messageName()
    {
        return TerrainEdit.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TerrainEdit.class.getName();
    }

    public boolean isInitialized(TerrainEdit message)
    {
        return true;
    }

    public void mergeFrom(Input input, TerrainEdit message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.x = input.readInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.y = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.detailLayer = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.value = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                    message.type = Type.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 6:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.texture = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.height = input.readFloat();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.createdAt = input.readInt64();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.terrain = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TerrainEdit message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (Integer)message.x != null) {
            output.writeInt32(1, message.x, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.y != null) {
            output.writeInt32(2, message.y, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.detailLayer != null) {
            output.writeInt32(3, message.detailLayer, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.value != null) {
            output.writeInt32(4, message.value, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.type != null)
    	 	output.writeEnum(5, message.type.number, false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(6, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(7, message.recordId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.texture != null) {
            output.writeInt32(8, message.texture, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Float)message.height != null) {
            output.writeFloat(9, message.height, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.createdAt != null) {
            output.writeInt64(10, message.createdAt, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.terrain != null) {
            output.writeString(11, message.terrain, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START TerrainEdit");
    	    	//if(this.x != null) {
    		System.out.println("x="+this.x);
    	//}
    	    	//if(this.y != null) {
    		System.out.println("y="+this.y);
    	//}
    	    	//if(this.detailLayer != null) {
    		System.out.println("detailLayer="+this.detailLayer);
    	//}
    	    	//if(this.value != null) {
    		System.out.println("value="+this.value);
    	//}
    	    	//if(this.type != null) {
    		System.out.println("type="+this.type);
    	//}
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	//if(this.texture != null) {
    		System.out.println("texture="+this.texture);
    	//}
    	    	//if(this.height != null) {
    		System.out.println("height="+this.height);
    	//}
    	    	//if(this.createdAt != null) {
    		System.out.println("createdAt="+this.createdAt);
    	//}
    	    	//if(this.terrain != null) {
    		System.out.println("terrain="+this.terrain);
    	//}
    	    	System.out.println("END TerrainEdit");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "x";
        	        	case 2: return "y";
        	        	case 3: return "detailLayer";
        	        	case 4: return "value";
        	        	case 5: return "type";
        	        	case 6: return "id";
        	        	case 7: return "recordId";
        	        	case 8: return "texture";
        	        	case 9: return "height";
        	        	case 10: return "createdAt";
        	        	case 11: return "terrain";
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
    	    	__fieldMap.put("x", 1);
    	    	__fieldMap.put("y", 2);
    	    	__fieldMap.put("detailLayer", 3);
    	    	__fieldMap.put("value", 4);
    	    	__fieldMap.put("type", 5);
    	    	__fieldMap.put("id", 6);
    	    	__fieldMap.put("recordId", 7);
    	    	__fieldMap.put("texture", 8);
    	    	__fieldMap.put("height", 9);
    	    	__fieldMap.put("createdAt", 10);
    	    	__fieldMap.put("terrain", 11);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TerrainEdit.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TerrainEdit parseFrom(byte[] bytes) {
	TerrainEdit message = new TerrainEdit();
	ProtobufIOUtil.mergeFrom(bytes, message, TerrainEdit.getSchema());
	return message;
}

public static TerrainEdit parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TerrainEdit message = new TerrainEdit();
	JsonIOUtil.mergeFrom(bytes, message, TerrainEdit.getSchema(), false);
	return message;
}

public TerrainEdit clone() {
	byte[] bytes = this.toByteArray();
	TerrainEdit terrainEdit = TerrainEdit.parseFrom(bytes);
	return terrainEdit;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TerrainEdit.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TerrainEdit> schema = TerrainEdit.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TerrainEdit.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, TerrainEdit.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
