
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
public final class ZoneInfos implements Externalizable, Message<ZoneInfos>, Schema<ZoneInfos>, PersistableMessage{



    public static Schema<ZoneInfos> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ZoneInfos getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ZoneInfos DEFAULT_INSTANCE = new ZoneInfos();
    static final String defaultScope = ZoneInfos.class.getSimpleName();

        public List<ZoneInfo> zoneInfo;
	    			public String id;
	    
        
	public static ZoneInfosCache cache() {
		return ZoneInfosCache.getInstance();
	}
	
	public static ZoneInfosStore store() {
		return ZoneInfosStore.getInstance();
	}


    public ZoneInfos()
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
		
		public ZoneInfos result(int timeout) {
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
	
	public static class ZoneInfosCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, ZoneInfos> cache = new Cache<String, ZoneInfos>(120, 5000);
		
		private ZoneInfosCache() {
		}
		
		private static class LazyHolder {
			private static final ZoneInfosCache INSTANCE = new ZoneInfosCache();
		}
	
		public static ZoneInfosCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, ZoneInfos>(expiration, size);
		}
	
		public Cache<String, ZoneInfos> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(ZoneInfosCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(ZoneInfos message) {
			CacheUpdate cacheUpdate = new CacheUpdate(ZoneInfosCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public ZoneInfos get(String id, int timeout) {
			ZoneInfos message = cache.get(id);
			if (message == null) {
				message = ZoneInfos.store().get(id, timeout);
			}
			return message;
		}
			
		public static ZoneInfos setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			ZoneInfos message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (ZoneInfos) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				ZoneInfos.store().set(message);
			} else {
				message = ZoneInfos.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, ZoneInfos.class.getField(field));
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
				ZoneInfos.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class ZoneInfosStore {
	
		private ZoneInfosStore() {
		}
		
		private static class LazyHolder {
			private static final ZoneInfosStore INSTANCE = new ZoneInfosStore();
		}
	
		public static ZoneInfosStore getInstance() {
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
		
	    public void set(ZoneInfos message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public ZoneInfos get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, ZoneInfos message) {
	    	ZoneInfos clone = message.clone();
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
			
		public ZoneInfos get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("ZoneInfos");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			ZoneInfos message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof ZoneInfos) {
					message = (ZoneInfos)result;
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
    	    	    	    	    	    	    	model.set("zone_infos_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("zone_infos_id",id);
    	        		
    	}
    	    	    }
    
	public static ZoneInfos fromModel(Model model) {
		boolean hasFields = false;
    	ZoneInfos message = new ZoneInfos();
    	    	    	    	    	    	
    	    	    	String idField = model.getString("zone_infos_id");
    	    	
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


	    
    public Boolean hasZoneInfo()  {
        return zoneInfo == null ? false : true;
    }
        
		public List<ZoneInfo> getZoneInfoList() {
		if(this.zoneInfo == null)
            this.zoneInfo = new ArrayList<ZoneInfo>();
		return zoneInfo;
	}

	public ZoneInfos setZoneInfoList(List<ZoneInfo> zoneInfo) {
		this.zoneInfo = zoneInfo;
		return this;
	}

	public ZoneInfo getZoneInfo(int index)  {
        return zoneInfo == null ? null : zoneInfo.get(index);
    }

    public int getZoneInfoCount()  {
        return zoneInfo == null ? 0 : zoneInfo.size();
    }

    public ZoneInfos addZoneInfo(ZoneInfo zoneInfo)  {
        if(this.zoneInfo == null)
            this.zoneInfo = new ArrayList<ZoneInfo>();
        this.zoneInfo.add(zoneInfo);
        return this;
    }
            	    	    	    	
    public ZoneInfos removeZoneInfoById(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByRecordId(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByNode(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.node.equals(obj.node)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByActorName(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.actorName.equals(obj.actorName)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByAssigned(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.assigned.equals(obj.assigned)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByNumber(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.number.equals(obj.number)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByHostname(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.hostname.equals(obj.hostname)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ZoneInfos removeZoneInfoByCurrent(ZoneInfo zoneInfo)  {
    	if(this.zoneInfo == null)
           return this;
            
       	Iterator<ZoneInfo> itr = this.zoneInfo.iterator();
       	while (itr.hasNext()) {
    	ZoneInfo obj = itr.next();
    	
    	    		if (zoneInfo.current.equals(obj.current)) {
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
	
	public ZoneInfos setId(String id) {
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

    public Schema<ZoneInfos> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ZoneInfos newMessage()
    {
        return new ZoneInfos();
    }

    public Class<ZoneInfos> typeClass()
    {
        return ZoneInfos.class;
    }

    public String messageName()
    {
        return ZoneInfos.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ZoneInfos.class.getName();
    }

    public boolean isInitialized(ZoneInfos message)
    {
        return true;
    }

    public void mergeFrom(Input input, ZoneInfos message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.zoneInfo == null)
                        message.zoneInfo = new ArrayList<ZoneInfo>();
                                        message.zoneInfo.add(input.mergeObject(null, ZoneInfo.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ZoneInfos message) throws IOException
    {
    	    	
    	    	
    	    	if(message.zoneInfo != null)
        {
            for(ZoneInfo zoneInfo : message.zoneInfo)
            {
                if(zoneInfo != null) {
                   	    				output.writeObject(1, zoneInfo, ZoneInfo.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(2, message.id, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START ZoneInfos");
    	    	if(this.zoneInfo != null) {
    		System.out.println("zoneInfo="+this.zoneInfo);
    	}
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	System.out.println("END ZoneInfos");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "zoneInfo";
        	        	case 2: return "id";
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
    	    	__fieldMap.put("zoneInfo", 1);
    	    	__fieldMap.put("id", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ZoneInfos.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ZoneInfos parseFrom(byte[] bytes) {
	ZoneInfos message = new ZoneInfos();
	ProtobufIOUtil.mergeFrom(bytes, message, ZoneInfos.getSchema());
	return message;
}

public static ZoneInfos parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ZoneInfos message = new ZoneInfos();
	JsonIOUtil.mergeFrom(bytes, message, ZoneInfos.getSchema(), false);
	return message;
}

public ZoneInfos clone() {
	byte[] bytes = this.toByteArray();
	ZoneInfos zoneInfos = ZoneInfos.parseFrom(bytes);
	return zoneInfos;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ZoneInfos.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ZoneInfos> schema = ZoneInfos.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ZoneInfos.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, ZoneInfos.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
