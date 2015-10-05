
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

import io.gamemachine.core.GameMachineLoader;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.actor.ActorSystem;
import org.javalite.activejdbc.Errors;


import io.gamemachine.core.ActorUtil;

import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;

import io.gamemachine.core.PersistableMessage;


import io.gamemachine.objectdb.Cache;
import io.gamemachine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class CraftableItem implements Externalizable, Message<CraftableItem>, Schema<CraftableItem>, PersistableMessage{



    public static Schema<CraftableItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftableItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftableItem DEFAULT_INSTANCE = new CraftableItem();
    static final String defaultScope = CraftableItem.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
        	
							    public String item1= null;
		    			    
		
    
        	
							    public int item1_quantity= 0;
		    			    
		
    
        	
							    public String item2= null;
		    			    
		
    
        	
							    public int item2_quantity= 0;
		    			    
		
    
        	
							    public String item3= null;
		    			    
		
    
        	
							    public int item3_quantity= 0;
		    			    
		
    
        	
							    public String item4= null;
		    			    
		
    
        	
							    public int item4_quantity= 0;
		    			    
		
    
        	
							    public int recordId= 0;
		    			    
		
    
        
	public static CraftableItemCache cache() {
		return CraftableItemCache.getInstance();
	}
	
	public static CraftableItemStore store() {
		return CraftableItemStore.getInstance();
	}


    public CraftableItem()
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
		
		public CraftableItem result(int timeout) {
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
	
	public static class CraftableItemCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, CraftableItem> cache = new Cache<String, CraftableItem>(120, 5000);
		
		private CraftableItemCache() {
		}
		
		private static class LazyHolder {
			private static final CraftableItemCache INSTANCE = new CraftableItemCache();
		}
	
		public static CraftableItemCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, CraftableItem>(expiration, size);
		}
	
		public Cache<String, CraftableItem> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(CraftableItemCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(CraftableItem message) {
			CacheUpdate cacheUpdate = new CacheUpdate(CraftableItemCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public CraftableItem get(String id, int timeout) {
			CraftableItem message = cache.get(id);
			if (message == null) {
				message = CraftableItem.store().get(id, timeout);
			}
			return message;
		}
			
		public static CraftableItem setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			CraftableItem message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (CraftableItem) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				CraftableItem.store().set(message);
			} else {
				message = CraftableItem.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, CraftableItem.class.getField(field));
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
				CraftableItem.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class CraftableItemStore {
	
		private CraftableItemStore() {
		}
		
		private static class LazyHolder {
			private static final CraftableItemStore INSTANCE = new CraftableItemStore();
		}
	
		public static CraftableItemStore getInstance() {
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
		
	    public void set(CraftableItem message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public CraftableItem get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, CraftableItem message) {
	    	CraftableItem clone = message.clone();
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
			
		public CraftableItem get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("CraftableItem");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			CraftableItem message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof CraftableItem) {
					message = (CraftableItem)result;
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
	

	

	public static CraftableItemDb db() {
		return CraftableItemDb.getInstance();
	}
	
	public interface CraftableItemAsyncDb {
		void save(CraftableItem message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class CraftableItemAsyncDbImpl implements CraftableItemAsyncDb {
	
		public void save(CraftableItem message) {
			CraftableItem.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	CraftableItem.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	CraftableItem.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class CraftableItemDb {
	
		public Errors dbErrors;
		private CraftableItemAsyncDb asyncDb = null;
		
		private CraftableItemDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<CraftableItemAsyncDbImpl>(CraftableItemAsyncDb.class, CraftableItemAsyncDbImpl.class));
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
			private static final CraftableItemDb INSTANCE = new CraftableItemDb();
		}
	
		public static CraftableItemDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(CraftableItem message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(CraftableItem message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(CraftableItem message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.CraftableItem.open();
	    	}
	    	
	    	io.gamemachine.orm.models.CraftableItem model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.CraftableItem.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.CraftableItem();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
	    	}
	    		    	
	    	Boolean res = model.save();
	    	if (res) {
	    		message.setRecordId(model.getInteger("id"));
	    	} else {
	    		dbErrors = model.errors();
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.CraftableItem.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.CraftableItem.open();
	    	int deleted = io.gamemachine.orm.models.CraftableItem.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.CraftableItem.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.CraftableItem.open();
	    	int deleted = io.gamemachine.orm.models.CraftableItem.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.CraftableItem.close();
	    	return result;
	    }
	    
	    public CraftableItem find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public CraftableItem find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.CraftableItem.open();
	    	}
	    	
	    	io.gamemachine.orm.models.CraftableItem model = io.gamemachine.orm.models.CraftableItem.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.CraftableItem.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		CraftableItem craftableItem = fromModel(model);
	    			    		return craftableItem;
	    	}
	    }
	    
	    public CraftableItem findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.CraftableItem.open();
	    	io.gamemachine.orm.models.CraftableItem model = io.gamemachine.orm.models.CraftableItem.findFirst(query, params);
	    	io.gamemachine.orm.models.CraftableItem.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		CraftableItem craftableItem = fromModel(model);
	    			    		return craftableItem;
	    	}
	    }
	    
	    public List<CraftableItem> findAll() {
	    	io.gamemachine.orm.models.CraftableItem.open();
	    	List<io.gamemachine.orm.models.CraftableItem> models = io.gamemachine.orm.models.CraftableItem.findAll();
	    	List<CraftableItem> messages = new ArrayList<CraftableItem>();
	    	for (io.gamemachine.orm.models.CraftableItem model : models) {
	    		CraftableItem craftableItem = fromModel(model);
	    			    		messages.add(craftableItem);
	    	}
	    	io.gamemachine.orm.models.CraftableItem.close();
	    	return messages;
	    }
	    
	    public List<CraftableItem> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<CraftableItem> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.CraftableItem.open();
	    	}
	    	List<io.gamemachine.orm.models.CraftableItem> models = io.gamemachine.orm.models.CraftableItem.where(query, params);
	    	List<CraftableItem> messages = new ArrayList<CraftableItem>();
	    	for (io.gamemachine.orm.models.CraftableItem model : models) {
	    		CraftableItem craftableItem = fromModel(model);
	    			    		messages.add(craftableItem);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.CraftableItem.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("craftable_item_id",null);
    	    	    	    	    	    	model.set("craftable_item_item1",null);
    	    	    	    	    	    	model.set("craftable_item_item1_quantity",null);
    	    	    	    	    	    	model.set("craftable_item_item2",null);
    	    	    	    	    	    	model.set("craftable_item_item2_quantity",null);
    	    	    	    	    	    	model.set("craftable_item_item3",null);
    	    	    	    	    	    	model.set("craftable_item_item3_quantity",null);
    	    	    	    	    	    	model.set("craftable_item_item4",null);
    	    	    	    	    	    	model.set("craftable_item_item4_quantity",null);
    	    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("craftable_item_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (item1 != null) {
    	       	    	model.setString("craftable_item_item1",item1);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (item1_quantity != null) {
    	       	    	model.setInteger("craftable_item_item1_quantity",item1_quantity);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (item2 != null) {
    	       	    	model.setString("craftable_item_item2",item2);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (item2_quantity != null) {
    	       	    	model.setInteger("craftable_item_item2_quantity",item2_quantity);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (item3 != null) {
    	       	    	model.setString("craftable_item_item3",item3);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (item3_quantity != null) {
    	       	    	model.setInteger("craftable_item_item3_quantity",item3_quantity);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (item4 != null) {
    	       	    	model.setString("craftable_item_item4",item4);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (item4_quantity != null) {
    	       	    	model.setInteger("craftable_item_item4_quantity",item4_quantity);
    	        		
    	//}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    }
    
	public static CraftableItem fromModel(Model model) {
		boolean hasFields = false;
    	CraftableItem message = new CraftableItem();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("craftable_item_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String item1TestField = model.getString("craftable_item_item1");
    	if (item1TestField != null) {
    		String item1Field = item1TestField;
    		message.setItem1(item1Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer item1_quantityTestField = model.getInteger("craftable_item_item1_quantity");
    	if (item1_quantityTestField != null) {
    		int item1_quantityField = item1_quantityTestField;
    		message.setItem1_quantity(item1_quantityField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String item2TestField = model.getString("craftable_item_item2");
    	if (item2TestField != null) {
    		String item2Field = item2TestField;
    		message.setItem2(item2Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer item2_quantityTestField = model.getInteger("craftable_item_item2_quantity");
    	if (item2_quantityTestField != null) {
    		int item2_quantityField = item2_quantityTestField;
    		message.setItem2_quantity(item2_quantityField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String item3TestField = model.getString("craftable_item_item3");
    	if (item3TestField != null) {
    		String item3Field = item3TestField;
    		message.setItem3(item3Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer item3_quantityTestField = model.getInteger("craftable_item_item3_quantity");
    	if (item3_quantityTestField != null) {
    		int item3_quantityField = item3_quantityTestField;
    		message.setItem3_quantity(item3_quantityField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String item4TestField = model.getString("craftable_item_item4");
    	if (item4TestField != null) {
    		String item4Field = item4TestField;
    		message.setItem4(item4Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer item4_quantityTestField = model.getInteger("craftable_item_item4_quantity");
    	if (item4_quantityTestField != null) {
    		int item4_quantityField = item4_quantityTestField;
    		message.setItem4_quantity(item4_quantityField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getId() {
		return id;
	}
	
	public CraftableItem setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public String getItem1() {
		return item1;
	}
	
	public CraftableItem setItem1(String item1) {
		this.item1 = item1;
		return this;	}
	
		            
		public int getItem1_quantity() {
		return item1_quantity;
	}
	
	public CraftableItem setItem1_quantity(int item1_quantity) {
		this.item1_quantity = item1_quantity;
		return this;	}
	
		            
		public String getItem2() {
		return item2;
	}
	
	public CraftableItem setItem2(String item2) {
		this.item2 = item2;
		return this;	}
	
		            
		public int getItem2_quantity() {
		return item2_quantity;
	}
	
	public CraftableItem setItem2_quantity(int item2_quantity) {
		this.item2_quantity = item2_quantity;
		return this;	}
	
		            
		public String getItem3() {
		return item3;
	}
	
	public CraftableItem setItem3(String item3) {
		this.item3 = item3;
		return this;	}
	
		            
		public int getItem3_quantity() {
		return item3_quantity;
	}
	
	public CraftableItem setItem3_quantity(int item3_quantity) {
		this.item3_quantity = item3_quantity;
		return this;	}
	
		            
		public String getItem4() {
		return item4;
	}
	
	public CraftableItem setItem4(String item4) {
		this.item4 = item4;
		return this;	}
	
		            
		public int getItem4_quantity() {
		return item4_quantity;
	}
	
	public CraftableItem setItem4_quantity(int item4_quantity) {
		this.item4_quantity = item4_quantity;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public CraftableItem setRecordId(int recordId) {
		this.recordId = recordId;
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

    public Schema<CraftableItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftableItem newMessage()
    {
        return new CraftableItem();
    }

    public Class<CraftableItem> typeClass()
    {
        return CraftableItem.class;
    }

    public String messageName()
    {
        return CraftableItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftableItem.class.getName();
    }

    public boolean isInitialized(CraftableItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftableItem message) throws IOException
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
            	                	                	message.item1 = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.item1_quantity = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.item2 = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.item2_quantity = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.item3 = input.readString();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.item3_quantity = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.item4 = input.readString();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.item4_quantity = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftableItem message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.item1 != null) {
            output.writeString(2, message.item1, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.item1_quantity != null) {
            output.writeInt32(3, message.item1_quantity, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.item2 != null) {
            output.writeString(4, message.item2, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.item2_quantity != null) {
            output.writeInt32(5, message.item2_quantity, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.item3 != null) {
            output.writeString(6, message.item3, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.item3_quantity != null) {
            output.writeInt32(7, message.item3_quantity, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.item4 != null) {
            output.writeString(8, message.item4, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.item4_quantity != null) {
            output.writeInt32(9, message.item4_quantity, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(10, message.recordId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START CraftableItem");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.item1 != null) {
    		System.out.println("item1="+this.item1);
    	//}
    	    	//if(this.item1_quantity != null) {
    		System.out.println("item1_quantity="+this.item1_quantity);
    	//}
    	    	//if(this.item2 != null) {
    		System.out.println("item2="+this.item2);
    	//}
    	    	//if(this.item2_quantity != null) {
    		System.out.println("item2_quantity="+this.item2_quantity);
    	//}
    	    	//if(this.item3 != null) {
    		System.out.println("item3="+this.item3);
    	//}
    	    	//if(this.item3_quantity != null) {
    		System.out.println("item3_quantity="+this.item3_quantity);
    	//}
    	    	//if(this.item4 != null) {
    		System.out.println("item4="+this.item4);
    	//}
    	    	//if(this.item4_quantity != null) {
    		System.out.println("item4_quantity="+this.item4_quantity);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	System.out.println("END CraftableItem");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "item1";
        	        	case 3: return "item1_quantity";
        	        	case 4: return "item2";
        	        	case 5: return "item2_quantity";
        	        	case 6: return "item3";
        	        	case 7: return "item3_quantity";
        	        	case 8: return "item4";
        	        	case 9: return "item4_quantity";
        	        	case 10: return "recordId";
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
    	    	__fieldMap.put("item1", 2);
    	    	__fieldMap.put("item1_quantity", 3);
    	    	__fieldMap.put("item2", 4);
    	    	__fieldMap.put("item2_quantity", 5);
    	    	__fieldMap.put("item3", 6);
    	    	__fieldMap.put("item3_quantity", 7);
    	    	__fieldMap.put("item4", 8);
    	    	__fieldMap.put("item4_quantity", 9);
    	    	__fieldMap.put("recordId", 10);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftableItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftableItem parseFrom(byte[] bytes) {
	CraftableItem message = new CraftableItem();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftableItem.getSchema());
	return message;
}

public static CraftableItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftableItem message = new CraftableItem();
	JsonIOUtil.mergeFrom(bytes, message, CraftableItem.getSchema(), false);
	return message;
}

public CraftableItem clone() {
	byte[] bytes = this.toByteArray();
	CraftableItem craftableItem = CraftableItem.parseFrom(bytes);
	return craftableItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftableItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftableItem> schema = CraftableItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftableItem.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, CraftableItem.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
