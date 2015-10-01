
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
public final class PathData implements Externalizable, Message<PathData>, Schema<PathData>, PersistableMessage{



    public static Schema<PathData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PathData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PathData DEFAULT_INSTANCE = new PathData();
    static final String defaultScope = PathData.class.getSimpleName();

    			public GmVector3 startPoint;
	    
        			public GmVector3 endPoint;
	    
            public List<GmVector3> nodes;
	    			public String id;
	    
        
	public static PathDataCache cache() {
		return PathDataCache.getInstance();
	}
	
	public static PathDataStore store() {
		return PathDataStore.getInstance();
	}


    public PathData()
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
		
		public PathData result(int timeout) {
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
	
	public static class PathDataCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, PathData> cache = new Cache<String, PathData>(120, 5000);
		
		private PathDataCache() {
		}
		
		private static class LazyHolder {
			private static final PathDataCache INSTANCE = new PathDataCache();
		}
	
		public static PathDataCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, PathData>(expiration, size);
		}
	
		public Cache<String, PathData> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(PathDataCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(PathData message) {
			CacheUpdate cacheUpdate = new CacheUpdate(PathDataCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public PathData get(String id, int timeout) {
			PathData message = cache.get(id);
			if (message == null) {
				message = PathData.store().get(id, timeout);
			}
			return message;
		}
			
		public static PathData setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			PathData message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (PathData) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				PathData.store().set(message);
			} else {
				message = PathData.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, PathData.class.getField(field));
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
				PathData.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class PathDataStore {
	
		private PathDataStore() {
		}
		
		private static class LazyHolder {
			private static final PathDataStore INSTANCE = new PathDataStore();
		}
	
		public static PathDataStore getInstance() {
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
		
	    public void set(PathData message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public PathData get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, PathData message) {
	    	PathData clone = message.clone();
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
			
		public PathData get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("PathData");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			PathData message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof PathData) {
					message = (PathData)result;
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
    	    	    	    	    	    	    	    	    	model.set("path_data_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("path_data_id",id);
    	        		
    	}
    	    	    }
    
	public static PathData fromModel(Model model) {
		boolean hasFields = false;
    	PathData message = new PathData();
    	    	    	    	    	    	    	    	
    	    	    	String idField = model.getString("path_data_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasStartPoint()  {
        return startPoint == null ? false : true;
    }
        
		public GmVector3 getStartPoint() {
		return startPoint;
	}
	
	public PathData setStartPoint(GmVector3 startPoint) {
		this.startPoint = startPoint;
		return this;	}
	
		    
    public Boolean hasEndPoint()  {
        return endPoint == null ? false : true;
    }
        
		public GmVector3 getEndPoint() {
		return endPoint;
	}
	
	public PathData setEndPoint(GmVector3 endPoint) {
		this.endPoint = endPoint;
		return this;	}
	
		    
    public Boolean hasNodes()  {
        return nodes == null ? false : true;
    }
        
		public List<GmVector3> getNodesList() {
		if(this.nodes == null)
            this.nodes = new ArrayList<GmVector3>();
		return nodes;
	}

	public PathData setNodesList(List<GmVector3> nodes) {
		this.nodes = nodes;
		return this;
	}

	public GmVector3 getNodes(int index)  {
        return nodes == null ? null : nodes.get(index);
    }

    public int getNodesCount()  {
        return nodes == null ? 0 : nodes.size();
    }

    public PathData addNodes(GmVector3 nodes)  {
        if(this.nodes == null)
            this.nodes = new ArrayList<GmVector3>();
        this.nodes.add(nodes);
        return this;
    }
            	    	    	    	
    public PathData removeNodesByX(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByY(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByZ(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.z.equals(obj.z)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByXi(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.xi.equals(obj.xi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByYi(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.yi.equals(obj.yi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByZi(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.zi.equals(obj.zi)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PathData removeNodesByVertice(GmVector3 nodes)  {
    	if(this.nodes == null)
           return this;
            
       	Iterator<GmVector3> itr = this.nodes.iterator();
       	while (itr.hasNext()) {
    	GmVector3 obj = itr.next();
    	
    	    		if (nodes.vertice.equals(obj.vertice)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public PathData setId(String id) {
		this.id = id;
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

    public Schema<PathData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PathData newMessage()
    {
        return new PathData();
    }

    public Class<PathData> typeClass()
    {
        return PathData.class;
    }

    public String messageName()
    {
        return PathData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PathData.class.getName();
    }

    public boolean isInitialized(PathData message)
    {
        return true;
    }

    public void mergeFrom(Input input, PathData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.startPoint = input.mergeObject(message.startPoint, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 2:
            	                	                	message.endPoint = input.mergeObject(message.endPoint, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 3:
            	            		if(message.nodes == null)
                        message.nodes = new ArrayList<GmVector3>();
                                        message.nodes.add(input.mergeObject(null, GmVector3.getSchema()));
                                        break;
                            	            	case 4:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PathData message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.startPoint != null)
    		output.writeObject(1, message.startPoint, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.endPoint != null)
    		output.writeObject(2, message.endPoint, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	if(message.nodes != null)
        {
            for(GmVector3 nodes : message.nodes)
            {
                if(nodes != null) {
                   	    				output.writeObject(3, nodes, GmVector3.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(4, message.id, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START PathData");
    	    	if(this.startPoint != null) {
    		System.out.println("startPoint="+this.startPoint);
    	}
    	    	if(this.endPoint != null) {
    		System.out.println("endPoint="+this.endPoint);
    	}
    	    	if(this.nodes != null) {
    		System.out.println("nodes="+this.nodes);
    	}
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	System.out.println("END PathData");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "startPoint";
        	        	case 2: return "endPoint";
        	        	case 3: return "nodes";
        	        	case 4: return "id";
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
    	    	__fieldMap.put("startPoint", 1);
    	    	__fieldMap.put("endPoint", 2);
    	    	__fieldMap.put("nodes", 3);
    	    	__fieldMap.put("id", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PathData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PathData parseFrom(byte[] bytes) {
	PathData message = new PathData();
	ProtobufIOUtil.mergeFrom(bytes, message, PathData.getSchema());
	return message;
}

public static PathData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PathData message = new PathData();
	JsonIOUtil.mergeFrom(bytes, message, PathData.getSchema(), false);
	return message;
}

public PathData clone() {
	byte[] bytes = this.toByteArray();
	PathData pathData = PathData.parseFrom(bytes);
	return pathData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PathData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PathData> schema = PathData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PathData.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, PathData.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
