
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
public final class GroundBlockObject implements Externalizable, Message<GroundBlockObject>, Schema<GroundBlockObject>, PersistableMessage{

	public enum Status implements io.protostuff.EnumLite<Status>
    {
    	
    	    	NONE(0),    	    	PLACED(1),    	    	CLEARED(2),    	    	TOP(3);    	        
        public final int number;
        
        private Status (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Status valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (PLACED);
    			    			case 2: return (CLEARED);
    			    			case 3: return (TOP);
    			                default: return null;
            }
        }
    }


    public static Schema<GroundBlockObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GroundBlockObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GroundBlockObject DEFAULT_INSTANCE = new GroundBlockObject();
    static final String defaultScope = GroundBlockObject.class.getSimpleName();

        public List<GmVector3> vertices;
	    			public GmVector3 position;
	    
        			public GmQuaternion rotation;
	    
        			public String id;
	    
        			public Integer verticeCount;
	    
        			public String tag;
	    
        			public String layer;
	    
        			public Integer gbLayer;
	    
        			public Boolean molded;
	    
        			public Status status; // = NONE:0;
	    
        			public Boolean canRemove;
	    
        			public Boolean isTop;
	    
        			public Integer gbType;
	    
        
	public static GroundBlockObjectCache cache() {
		return GroundBlockObjectCache.getInstance();
	}
	
	public static GroundBlockObjectStore store() {
		return GroundBlockObjectStore.getInstance();
	}


    public GroundBlockObject()
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
		
		public GroundBlockObject result(int timeout) {
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
	
	public static class GroundBlockObjectCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, GroundBlockObject> cache = new Cache<String, GroundBlockObject>(120, 5000);
		
		private GroundBlockObjectCache() {
		}
		
		private static class LazyHolder {
			private static final GroundBlockObjectCache INSTANCE = new GroundBlockObjectCache();
		}
	
		public static GroundBlockObjectCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, GroundBlockObject>(expiration, size);
		}
	
		public Cache<String, GroundBlockObject> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(GroundBlockObjectCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(GroundBlockObject message) {
			CacheUpdate cacheUpdate = new CacheUpdate(GroundBlockObjectCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public GroundBlockObject get(String id, int timeout) {
			GroundBlockObject message = cache.get(id);
			if (message == null) {
				message = GroundBlockObject.store().get(id, timeout);
			}
			return message;
		}
			
		public static GroundBlockObject setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			GroundBlockObject message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (GroundBlockObject) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				GroundBlockObject.store().set(message);
			} else {
				message = GroundBlockObject.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, GroundBlockObject.class.getField(field));
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
				GroundBlockObject.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class GroundBlockObjectStore {
	
		private GroundBlockObjectStore() {
		}
		
		private static class LazyHolder {
			private static final GroundBlockObjectStore INSTANCE = new GroundBlockObjectStore();
		}
	
		public static GroundBlockObjectStore getInstance() {
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
		
	    public void set(GroundBlockObject message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public GroundBlockObject get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, GroundBlockObject message) {
	    	GroundBlockObject clone = message.clone();
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
			
		public GroundBlockObject get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("GroundBlockObject");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			GroundBlockObject message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof GroundBlockObject) {
					message = (GroundBlockObject)result;
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
    	    	    	    	    	    	    	    	    	model.set("ground_block_object_id",null);
    	    	    	    	    	    	model.set("ground_block_object_vertice_count",null);
    	    	    	    	    	    	model.set("ground_block_object_tag",null);
    	    	    	    	    	    	model.set("ground_block_object_layer",null);
    	    	    	    	    	    	model.set("ground_block_object_gb_layer",null);
    	    	    	    	    	    	model.set("ground_block_object_molded",null);
    	    	    	    	    	    	    	model.set("ground_block_object_can_remove",null);
    	    	    	    	    	    	model.set("ground_block_object_is_top",null);
    	    	    	    	    	    	model.set("ground_block_object_gb_type",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("ground_block_object_id",id);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (verticeCount != null) {
    	       	    	model.setInteger("ground_block_object_vertice_count",verticeCount);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (tag != null) {
    	       	    	model.setString("ground_block_object_tag",tag);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (layer != null) {
    	       	    	model.setString("ground_block_object_layer",layer);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (gbLayer != null) {
    	       	    	model.setInteger("ground_block_object_gb_layer",gbLayer);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (molded != null) {
    	       	    	model.setBoolean("ground_block_object_molded",molded);
    	        		
    	}
    	    	    	    	    	    	
    	    	    	if (canRemove != null) {
    	       	    	model.setBoolean("ground_block_object_can_remove",canRemove);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (isTop != null) {
    	       	    	model.setBoolean("ground_block_object_is_top",isTop);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (gbType != null) {
    	       	    	model.setInteger("ground_block_object_gb_type",gbType);
    	        		
    	}
    	    	    }
    
	public static GroundBlockObject fromModel(Model model) {
		boolean hasFields = false;
    	GroundBlockObject message = new GroundBlockObject();
    	    	    	    	    	    	    	    	
    	    	    	String idField = model.getString("ground_block_object_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer verticeCountField = model.getInteger("ground_block_object_vertice_count");
    	    	
    	if (verticeCountField != null) {
    		message.setVerticeCount(verticeCountField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String tagField = model.getString("ground_block_object_tag");
    	    	
    	if (tagField != null) {
    		message.setTag(tagField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String layerField = model.getString("ground_block_object_layer");
    	    	
    	if (layerField != null) {
    		message.setLayer(layerField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer gbLayerField = model.getInteger("ground_block_object_gb_layer");
    	    	
    	if (gbLayerField != null) {
    		message.setGbLayer(gbLayerField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean moldedField = model.getBoolean("ground_block_object_molded");
    	    	
    	if (moldedField != null) {
    		message.setMolded(moldedField);
    		hasFields = true;
    	}
    	    	    	    	    	    	    	
    	    	    	Boolean canRemoveField = model.getBoolean("ground_block_object_can_remove");
    	    	
    	if (canRemoveField != null) {
    		message.setCanRemove(canRemoveField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean isTopField = model.getBoolean("ground_block_object_is_top");
    	    	
    	if (isTopField != null) {
    		message.setIsTop(isTopField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer gbTypeField = model.getInteger("ground_block_object_gb_type");
    	    	
    	if (gbTypeField != null) {
    		message.setGbType(gbTypeField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasVertices()  {
        return vertices == null ? false : true;
    }
        
		public List<GmVector3> getVerticesList() {
		if(this.vertices == null)
            this.vertices = new ArrayList<GmVector3>();
		return vertices;
	}

	public GroundBlockObject setVerticesList(List<GmVector3> vertices) {
		this.vertices = vertices;
		return this;
	}

	public GmVector3 getVertices(int index)  {
        return vertices == null ? null : vertices.get(index);
    }

    public int getVerticesCount()  {
        return vertices == null ? 0 : vertices.size();
    }

    public GroundBlockObject addVertices(GmVector3 vertices)  {
        if(this.vertices == null)
            this.vertices = new ArrayList<GmVector3>();
        this.vertices.add(vertices);
        return this;
    }
            	    	    	    	
    public GroundBlockObject removeVerticesByX(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByY(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByZ(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.z.equals(obj.z)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByXi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.xi.equals(obj.xi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByYi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.yi.equals(obj.yi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByZi(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.zi.equals(obj.zi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public GroundBlockObject removeVerticesByVertice(GmVector3 vertices)  {
    	if(this.vertices == null)
           return this;
            
       	Iterator<GmVector3> itr = this.vertices.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (vertices.vertice.equals(obj.vertice)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		    
    public Boolean hasPosition()  {
        return position == null ? false : true;
    }
        
		public GmVector3 getPosition() {
		return position;
	}
	
	public GroundBlockObject setPosition(GmVector3 position) {
		this.position = position;
		return this;	}
	
		    
    public Boolean hasRotation()  {
        return rotation == null ? false : true;
    }
        
		public GmQuaternion getRotation() {
		return rotation;
	}
	
	public GroundBlockObject setRotation(GmQuaternion rotation) {
		this.rotation = rotation;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public GroundBlockObject setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasVerticeCount()  {
        return verticeCount == null ? false : true;
    }
        
		public Integer getVerticeCount() {
		return verticeCount;
	}
	
	public GroundBlockObject setVerticeCount(Integer verticeCount) {
		this.verticeCount = verticeCount;
		return this;	}
	
		    
    public Boolean hasTag()  {
        return tag == null ? false : true;
    }
        
		public String getTag() {
		return tag;
	}
	
	public GroundBlockObject setTag(String tag) {
		this.tag = tag;
		return this;	}
	
		    
    public Boolean hasLayer()  {
        return layer == null ? false : true;
    }
        
		public String getLayer() {
		return layer;
	}
	
	public GroundBlockObject setLayer(String layer) {
		this.layer = layer;
		return this;	}
	
		    
    public Boolean hasGbLayer()  {
        return gbLayer == null ? false : true;
    }
        
		public Integer getGbLayer() {
		return gbLayer;
	}
	
	public GroundBlockObject setGbLayer(Integer gbLayer) {
		this.gbLayer = gbLayer;
		return this;	}
	
		    
    public Boolean hasMolded()  {
        return molded == null ? false : true;
    }
        
		public Boolean getMolded() {
		return molded;
	}
	
	public GroundBlockObject setMolded(Boolean molded) {
		this.molded = molded;
		return this;	}
	
		    
    public Boolean hasStatus()  {
        return status == null ? false : true;
    }
        
		public Status getStatus() {
		return status;
	}
	
	public GroundBlockObject setStatus(Status status) {
		this.status = status;
		return this;	}
	
		    
    public Boolean hasCanRemove()  {
        return canRemove == null ? false : true;
    }
        
		public Boolean getCanRemove() {
		return canRemove;
	}
	
	public GroundBlockObject setCanRemove(Boolean canRemove) {
		this.canRemove = canRemove;
		return this;	}
	
		    
    public Boolean hasIsTop()  {
        return isTop == null ? false : true;
    }
        
		public Boolean getIsTop() {
		return isTop;
	}
	
	public GroundBlockObject setIsTop(Boolean isTop) {
		this.isTop = isTop;
		return this;	}
	
		    
    public Boolean hasGbType()  {
        return gbType == null ? false : true;
    }
        
		public Integer getGbType() {
		return gbType;
	}
	
	public GroundBlockObject setGbType(Integer gbType) {
		this.gbType = gbType;
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

    public Schema<GroundBlockObject> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GroundBlockObject newMessage()
    {
        return new GroundBlockObject();
    }

    public Class<GroundBlockObject> typeClass()
    {
        return GroundBlockObject.class;
    }

    public String messageName()
    {
        return GroundBlockObject.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GroundBlockObject.class.getName();
    }

    public boolean isInitialized(GroundBlockObject message)
    {
        return true;
    }

    public void mergeFrom(Input input, GroundBlockObject message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.vertices == null)
                        message.vertices = new ArrayList<GmVector3>();
                                        message.vertices.add(input.mergeObject(null, GmVector3.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.position = input.mergeObject(message.position, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.rotation = input.mergeObject(message.rotation, GmQuaternion.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.verticeCount = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.tag = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.layer = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.gbLayer = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.molded = input.readBool();
                	break;
                	                	
                            	            	case 10:
            	                	                    message.status = Status.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 11:
            	                	                	message.canRemove = input.readBool();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.isTop = input.readBool();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.gbType = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GroundBlockObject message) throws IOException
    {
    	    	
    	    	
    	    	if(message.vertices != null)
        {
            for(GmVector3 vertices : message.vertices)
            {
                if(vertices != null) {
                   	    				output.writeObject(1, vertices, GmVector3.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.position != null)
    		output.writeObject(2, message.position, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.rotation != null)
    		output.writeObject(3, message.rotation, GmQuaternion.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(4, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.verticeCount != null)
            output.writeInt32(5, message.verticeCount, false);
    	    	
    	            	
    	    	
    	    	    	if(message.tag != null)
            output.writeString(6, message.tag, false);
    	    	
    	            	
    	    	
    	    	    	if(message.layer != null)
            output.writeString(7, message.layer, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gbLayer != null)
            output.writeInt32(8, message.gbLayer, false);
    	    	
    	            	
    	    	
    	    	    	if(message.molded != null)
            output.writeBool(9, message.molded, false);
    	    	
    	            	
    	    	
    	    	    	if(message.status != null)
    	 	output.writeEnum(10, message.status.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.canRemove != null)
            output.writeBool(11, message.canRemove, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isTop != null)
            output.writeBool(12, message.isTop, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gbType != null)
            output.writeInt32(13, message.gbType, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GroundBlockObject");
    	    	if(this.vertices != null) {
    		System.out.println("vertices="+this.vertices);
    	}
    	    	if(this.position != null) {
    		System.out.println("position="+this.position);
    	}
    	    	if(this.rotation != null) {
    		System.out.println("rotation="+this.rotation);
    	}
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.verticeCount != null) {
    		System.out.println("verticeCount="+this.verticeCount);
    	}
    	    	if(this.tag != null) {
    		System.out.println("tag="+this.tag);
    	}
    	    	if(this.layer != null) {
    		System.out.println("layer="+this.layer);
    	}
    	    	if(this.gbLayer != null) {
    		System.out.println("gbLayer="+this.gbLayer);
    	}
    	    	if(this.molded != null) {
    		System.out.println("molded="+this.molded);
    	}
    	    	if(this.status != null) {
    		System.out.println("status="+this.status);
    	}
    	    	if(this.canRemove != null) {
    		System.out.println("canRemove="+this.canRemove);
    	}
    	    	if(this.isTop != null) {
    		System.out.println("isTop="+this.isTop);
    	}
    	    	if(this.gbType != null) {
    		System.out.println("gbType="+this.gbType);
    	}
    	    	System.out.println("END GroundBlockObject");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "vertices";
        	        	case 2: return "position";
        	        	case 3: return "rotation";
        	        	case 4: return "id";
        	        	case 5: return "verticeCount";
        	        	case 6: return "tag";
        	        	case 7: return "layer";
        	        	case 8: return "gbLayer";
        	        	case 9: return "molded";
        	        	case 10: return "status";
        	        	case 11: return "canRemove";
        	        	case 12: return "isTop";
        	        	case 13: return "gbType";
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
    	    	__fieldMap.put("vertices", 1);
    	    	__fieldMap.put("position", 2);
    	    	__fieldMap.put("rotation", 3);
    	    	__fieldMap.put("id", 4);
    	    	__fieldMap.put("verticeCount", 5);
    	    	__fieldMap.put("tag", 6);
    	    	__fieldMap.put("layer", 7);
    	    	__fieldMap.put("gbLayer", 8);
    	    	__fieldMap.put("molded", 9);
    	    	__fieldMap.put("status", 10);
    	    	__fieldMap.put("canRemove", 11);
    	    	__fieldMap.put("isTop", 12);
    	    	__fieldMap.put("gbType", 13);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GroundBlockObject.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GroundBlockObject parseFrom(byte[] bytes) {
	GroundBlockObject message = new GroundBlockObject();
	ProtobufIOUtil.mergeFrom(bytes, message, GroundBlockObject.getSchema());
	return message;
}

public static GroundBlockObject parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GroundBlockObject message = new GroundBlockObject();
	JsonIOUtil.mergeFrom(bytes, message, GroundBlockObject.getSchema(), false);
	return message;
}

public GroundBlockObject clone() {
	byte[] bytes = this.toByteArray();
	GroundBlockObject groundBlockObject = GroundBlockObject.parseFrom(bytes);
	return groundBlockObject;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GroundBlockObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GroundBlockObject> schema = GroundBlockObject.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GroundBlockObject.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GroundBlockObject.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
