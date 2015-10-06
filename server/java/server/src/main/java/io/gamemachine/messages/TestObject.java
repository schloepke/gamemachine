
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

import io.gamemachine.core.GameMachineLoader;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.actor.ActorSystem;
import org.javalite.activejdbc.Errors;


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
public final class TestObject implements Externalizable, Message<TestObject>, Schema<TestObject>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(TestObject.class);



    public static Schema<TestObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TestObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TestObject DEFAULT_INSTANCE = new TestObject();
    static final String defaultScope = TestObject.class.getSimpleName();

    	
							    public String optionalString= null;
		    			    
		
    
        	
							    public String requiredString= null;
		    			    
		
    
            public List<Integer> numbers;
	    	
							    public ByteString bstring;
		    			    
		
    
        	
							    public boolean bvalue= false;
		    			    
		
    
        	
							    public double dvalue= 0D;
		    			    
		
    
        	
							    public float fvalue= 0F;
		    			    
		
    
        	
							    public long numbers64= 0L;
		    			    
		
    
            public List<Player> player;
	    	
							    public int recordId= 0;
		    			    
		
    
        	
							    public String id= null;
		    			    
		
    
        
	public static TestObjectCache cache() {
		return TestObjectCache.getInstance();
	}
	
	public static TestObjectStore store() {
		return TestObjectStore.getInstance();
	}


    public TestObject()
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
		
		public TestObject result(int timeout) {
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
	
	public static class TestObjectCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, TestObject> cache = new Cache<String, TestObject>(120, 5000);
		
		private TestObjectCache() {
		}
		
		private static class LazyHolder {
			private static final TestObjectCache INSTANCE = new TestObjectCache();
		}
	
		public static TestObjectCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, TestObject>(expiration, size);
		}
	
		public Cache<String, TestObject> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(TestObjectCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(TestObject message) {
			CacheUpdate cacheUpdate = new CacheUpdate(TestObjectCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public TestObject get(String id, int timeout) {
			TestObject message = cache.get(id);
			if (message == null) {
				message = TestObject.store().get(id, timeout);
			}
			return message;
		}
			
		public static TestObject setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			TestObject message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (TestObject) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				TestObject.store().set(message);
			} else {
				message = TestObject.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, TestObject.class.getField(field));
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
				TestObject.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class TestObjectStore {
	
		private TestObjectStore() {
		}
		
		private static class LazyHolder {
			private static final TestObjectStore INSTANCE = new TestObjectStore();
		}
	
		public static TestObjectStore getInstance() {
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
		
	    public void set(TestObject message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public TestObject get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, TestObject message) {
	    	TestObject clone = message.clone();
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
			
		public TestObject get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("TestObject");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			TestObject message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof TestObject) {
					message = (TestObject)result;
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
	

	

	public static TestObjectDb db() {
		return TestObjectDb.getInstance();
	}
	
	public interface TestObjectAsyncDb {
		void save(TestObject message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class TestObjectAsyncDbImpl implements TestObjectAsyncDb {
	
		public void save(TestObject message) {
			TestObject.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	TestObject.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	TestObject.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class TestObjectDb {
	
		public Errors dbErrors;
		private TestObjectAsyncDb asyncDb = null;
		
		private TestObjectDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<TestObjectAsyncDbImpl>(TestObjectAsyncDb.class, TestObjectAsyncDbImpl.class));
			}
		}
		
		public void stop() {
			if (asyncDb != null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				TypedActor.get(system).stop(asyncDb);
				asyncDb = null;
			}
		}
		
		private static class LazyHolder {
			private static final TestObjectDb INSTANCE = new TestObjectDb();
		}
	
		public static TestObjectDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(TestObject message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(TestObject message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(TestObject message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.TestObject.open();
	    	}
	    	
	    	io.gamemachine.orm.models.TestObject model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.TestObject.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.TestObject();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
	    	}
	    		    	
	    	Boolean res = model.save();
	    	if (res) {
	    		message.setRecordId(model.getInteger("id"));
	    	} else {
	    		if (model.hasErrors()) {
	    			logger.warn("Save has errors");
	    			dbErrors = model.errors();
		    		Map<String, String> errors = dbErrors;
		    		for (String key : errors.keySet()) {
		    			logger.warn(key+": "+errors.get(key));
		    		}
	    		} else {
	    			logger.warn("Save failed unknown reason");
	    		}
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.TestObject.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.TestObject.open();
	    	int deleted = io.gamemachine.orm.models.TestObject.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.TestObject.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.TestObject.open();
	    	int deleted = io.gamemachine.orm.models.TestObject.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.TestObject.close();
	    	return result;
	    }
	    
	    public TestObject find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public TestObject find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.TestObject.open();
	    	}
	    	
	    	io.gamemachine.orm.models.TestObject model = io.gamemachine.orm.models.TestObject.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.TestObject.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		TestObject testObject = fromModel(model);
	    			    		return testObject;
	    	}
	    }
	    
	    public TestObject findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.TestObject.open();
	    	io.gamemachine.orm.models.TestObject model = io.gamemachine.orm.models.TestObject.findFirst(query, params);
	    	io.gamemachine.orm.models.TestObject.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		TestObject testObject = fromModel(model);
	    			    		return testObject;
	    	}
	    }
	    
	    public List<TestObject> findAll() {
	    	io.gamemachine.orm.models.TestObject.open();
	    	List<io.gamemachine.orm.models.TestObject> models = io.gamemachine.orm.models.TestObject.findAll();
	    	List<TestObject> messages = new ArrayList<TestObject>();
	    	for (io.gamemachine.orm.models.TestObject model : models) {
	    		TestObject testObject = fromModel(model);
	    			    		messages.add(testObject);
	    	}
	    	io.gamemachine.orm.models.TestObject.close();
	    	return messages;
	    }
	    
	    public List<TestObject> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<TestObject> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.TestObject.open();
	    	}
	    	List<io.gamemachine.orm.models.TestObject> models = io.gamemachine.orm.models.TestObject.where(query, params);
	    	List<TestObject> messages = new ArrayList<TestObject>();
	    	for (io.gamemachine.orm.models.TestObject model : models) {
	    		TestObject testObject = fromModel(model);
	    			    		messages.add(testObject);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.TestObject.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("test_object_optional_string",null);
    	    	    	    	    	    	model.set("test_object_required_string",null);
    	    	    	    	    	    	    	    	model.set("test_object_bstring",null);
    	    	    	    	    	    	model.set("test_object_bvalue",null);
    	    	    	    	    	    	model.set("test_object_dvalue",null);
    	    	    	    	    	    	model.set("test_object_fvalue",null);
    	    	    	    	    	    	model.set("test_object_numbers64",null);
    	    	    	    	    	    	    	    	    	    	model.set("test_object_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (optionalString != null) {
    	       	    	model.setString("test_object_optional_string",optionalString);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (requiredString != null) {
    	       	    	model.setString("test_object_required_string",requiredString);
    	        		
    	//}
    	    	    	    	    	    	    	
    	    	    	//if (bstring != null) {
    	       	    	model.set("test_object_bstring",bstring.toByteArray());
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (bvalue != null) {
    	       	    	model.setBoolean("test_object_bvalue",bvalue);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (dvalue != null) {
    	       	    	model.setDouble("test_object_dvalue",dvalue);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (fvalue != null) {
    	       	    	model.setFloat("test_object_fvalue",fvalue);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (numbers64 != null) {
    	       	    	model.setLong("test_object_numbers64",numbers64);
    	        		
    	//}
    	    	    	    	    	    	
    	    	    	//model.setInteger("id",recordId);
    	
    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("test_object_id",id);
    	        		
    	//}
    	    	    }
    
	public static TestObject fromModel(Model model) {
		boolean hasFields = false;
    	TestObject message = new TestObject();
    	    	    	    	    	
    	    			String optionalStringTestField = model.getString("test_object_optional_string");
		if (optionalStringTestField != null) {
			String optionalStringField = optionalStringTestField;
			message.setOptionalString(optionalStringField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String requiredStringTestField = model.getString("test_object_required_string");
		if (requiredStringTestField != null) {
			String requiredStringField = requiredStringTestField;
			message.setRequiredString(requiredStringField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	    	
    	    	    	
    	ByteString bstringField = null;
		Object bstringValue = model.get("test_object_bstring");
		if (bstringValue != null) {
			byte[] bstringBytes = Convert.toBytes(bstringValue);
			bstringField = ByteString.copyFrom(bstringBytes);
		}
    	    	
    	    	
    	    	    	    	    	    	
    	    			Boolean bvalueTestField = model.getBoolean("test_object_bvalue");
		if (bvalueTestField != null) {
			boolean bvalueField = bvalueTestField;
			message.setBvalue(bvalueField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Double dvalueTestField = model.getDouble("test_object_dvalue");
		if (dvalueTestField != null) {
			double dvalueField = dvalueTestField;
			message.setDvalue(dvalueField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Float fvalueTestField = model.getFloat("test_object_fvalue");
		if (fvalueTestField != null) {
			float fvalueField = fvalueTestField;
			message.setFvalue(fvalueField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Long numbers64TestField = model.getLong("test_object_numbers64");
		if (numbers64TestField != null) {
			long numbers64Field = numbers64TestField;
			message.setNumbers64(numbers64Field);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    			String idTestField = model.getString("test_object_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public String getOptionalString() {
		return optionalString;
	}
	
	public TestObject setOptionalString(String optionalString) {
		this.optionalString = optionalString;
		return this;	}
	
		            
		public String getRequiredString() {
		return requiredString;
	}
	
	public TestObject setRequiredString(String requiredString) {
		this.requiredString = requiredString;
		return this;	}
	
		            
		public List<Integer> getNumbersList() {
		if(this.numbers == null)
            this.numbers = new ArrayList<Integer>();
		return numbers;
	}

	public TestObject setNumbersList(List<Integer> numbers) {
		this.numbers = numbers;
		return this;
	}

	public int getNumbers(int index)  {
        return numbers == null ? null : numbers.get(index);
    }

    public int getNumbersCount()  {
        return numbers == null ? 0 : numbers.size();
    }

    public TestObject addNumbers(int numbers)  {
        if(this.numbers == null)
            this.numbers = new ArrayList<Integer>();
        this.numbers.add(numbers);
        return this;
    }
        	
    
    
    
		            
		public ByteString getBstring() {
		return bstring;
	}
	
	public TestObject setBstring(ByteString bstring) {
		this.bstring = bstring;
		return this;	}
	
		            
		public boolean getBvalue() {
		return bvalue;
	}
	
	public TestObject setBvalue(boolean bvalue) {
		this.bvalue = bvalue;
		return this;	}
	
		            
		public double getDvalue() {
		return dvalue;
	}
	
	public TestObject setDvalue(double dvalue) {
		this.dvalue = dvalue;
		return this;	}
	
		            
		public float getFvalue() {
		return fvalue;
	}
	
	public TestObject setFvalue(float fvalue) {
		this.fvalue = fvalue;
		return this;	}
	
		            
		public long getNumbers64() {
		return numbers64;
	}
	
	public TestObject setNumbers64(long numbers64) {
		this.numbers64 = numbers64;
		return this;	}
	
		            
		public List<Player> getPlayerList() {
		if(this.player == null)
            this.player = new ArrayList<Player>();
		return player;
	}

	public TestObject setPlayerList(List<Player> player) {
		this.player = player;
		return this;
	}

	public Player getPlayer(int index)  {
        return player == null ? null : player.get(index);
    }

    public int getPlayerCount()  {
        return player == null ? 0 : player.size();
    }

    public TestObject addPlayer(Player player)  {
        if(this.player == null)
            this.player = new ArrayList<Player>();
        this.player.add(player);
        return this;
    }
            	    	    	    	
    public TestObject removePlayerById(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByAuthenticated(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.authenticated == obj.authenticated) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByAuthtoken(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.authtoken == obj.authtoken) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByPasswordHash(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.passwordHash.equals(obj.passwordHash)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByGameId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.gameId.equals(obj.gameId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByRecordId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.recordId == obj.recordId) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByRole(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.role.equals(obj.role)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByLocked(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.locked == obj.locked) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByIp(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.ip == obj.ip) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByIpChangedAt(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.ipChangedAt == obj.ipChangedAt) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByCharacterId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.characterId.equals(obj.characterId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	        	
    
    
    
		            
		public int getRecordId() {
		return recordId;
	}
	
	public TestObject setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public String getId() {
		return id;
	}
	
	public TestObject setId(String id) {
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

    public Schema<TestObject> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TestObject newMessage()
    {
        return new TestObject();
    }

    public Class<TestObject> typeClass()
    {
        return TestObject.class;
    }

    public String messageName()
    {
        return TestObject.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TestObject.class.getName();
    }

    public boolean isInitialized(TestObject message)
    {
        return true;
    }

    public void mergeFrom(Input input, TestObject message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.optionalString = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.requiredString = input.readString();
                	break;
                	                	
                            	            	case 3:
            	            		if(message.numbers == null)
                        message.numbers = new ArrayList<Integer>();
                                    	message.numbers.add(input.readInt32());
                	                    break;
                            	            	case 4:
            	                	                	message.bstring = input.readBytes();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.bvalue = input.readBool();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.dvalue = input.readDouble();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.fvalue = input.readFloat();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.numbers64 = input.readInt64();
                	break;
                	                	
                            	            	case 9:
            	            		if(message.player == null)
                        message.player = new ArrayList<Player>();
                                        message.player.add(input.mergeObject(null, Player.getSchema()));
                                        break;
                            	            	case 10:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TestObject message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.optionalString != null) {
            output.writeString(1, message.optionalString, false);
        }
    	    	
    	            	
    	    	//if(message.requiredString == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.requiredString != null) {
            output.writeString(2, message.requiredString, false);
        }
    	    	
    	            	
    	    	
    	    	if(message.numbers != null)
        {
            for(int numbers : message.numbers)
            {
                if( (Integer) numbers != null) {
                   	            		output.writeInt32(3, numbers, true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if( (ByteString)message.bstring != null) {
            output.writeBytes(4, message.bstring, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.bvalue != null) {
            output.writeBool(5, message.bvalue, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Double)message.dvalue != null) {
            output.writeDouble(6, message.dvalue, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Float)message.fvalue != null) {
            output.writeFloat(7, message.fvalue, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.numbers64 != null) {
            output.writeInt64(8, message.numbers64, false);
        }
    	    	
    	            	
    	    	
    	    	if(message.player != null)
        {
            for(Player player : message.player)
            {
                if( (Player) player != null) {
                   	    				output.writeObject(9, player, Player.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(10, message.recordId, false);
        }
    	    	
    	            	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(12, message.id, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START TestObject");
    	    	//if(this.optionalString != null) {
    		System.out.println("optionalString="+this.optionalString);
    	//}
    	    	//if(this.requiredString != null) {
    		System.out.println("requiredString="+this.requiredString);
    	//}
    	    	//if(this.numbers != null) {
    		System.out.println("numbers="+this.numbers);
    	//}
    	    	//if(this.bstring != null) {
    		System.out.println("bstring="+this.bstring);
    	//}
    	    	//if(this.bvalue != null) {
    		System.out.println("bvalue="+this.bvalue);
    	//}
    	    	//if(this.dvalue != null) {
    		System.out.println("dvalue="+this.dvalue);
    	//}
    	    	//if(this.fvalue != null) {
    		System.out.println("fvalue="+this.fvalue);
    	//}
    	    	//if(this.numbers64 != null) {
    		System.out.println("numbers64="+this.numbers64);
    	//}
    	    	//if(this.player != null) {
    		System.out.println("player="+this.player);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	System.out.println("END TestObject");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "optionalString";
        	        	case 2: return "requiredString";
        	        	case 3: return "numbers";
        	        	case 4: return "bstring";
        	        	case 5: return "bvalue";
        	        	case 6: return "dvalue";
        	        	case 7: return "fvalue";
        	        	case 8: return "numbers64";
        	        	case 9: return "player";
        	        	case 10: return "recordId";
        	        	case 12: return "id";
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
    	    	__fieldMap.put("optionalString", 1);
    	    	__fieldMap.put("requiredString", 2);
    	    	__fieldMap.put("numbers", 3);
    	    	__fieldMap.put("bstring", 4);
    	    	__fieldMap.put("bvalue", 5);
    	    	__fieldMap.put("dvalue", 6);
    	    	__fieldMap.put("fvalue", 7);
    	    	__fieldMap.put("numbers64", 8);
    	    	__fieldMap.put("player", 9);
    	    	__fieldMap.put("recordId", 10);
    	    	__fieldMap.put("id", 12);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TestObject.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TestObject parseFrom(byte[] bytes) {
	TestObject message = new TestObject();
	ProtobufIOUtil.mergeFrom(bytes, message, TestObject.getSchema());
	return message;
}

public static TestObject parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TestObject message = new TestObject();
	JsonIOUtil.mergeFrom(bytes, message, TestObject.getSchema(), false);
	return message;
}

public TestObject clone() {
	byte[] bytes = this.toByteArray();
	TestObject testObject = TestObject.parseFrom(bytes);
	return testObject;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TestObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TestObject> schema = TestObject.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TestObject.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, TestObject.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
