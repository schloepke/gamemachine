
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
public final class CraftingRecipe implements Externalizable, Message<CraftingRecipe>, Schema<CraftingRecipe>, PersistableMessage{



    public static Schema<CraftingRecipe> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static CraftingRecipe getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final CraftingRecipe DEFAULT_INSTANCE = new CraftingRecipe();
    static final String defaultScope = CraftingRecipe.class.getSimpleName();

    	
							    public String id= null;
		    			    
		
    
            public List<CraftingElement> elements;
	    	
					public CraftingTool tool = null;
			    
		
    
        	
							    public boolean orderMatters= false;
		    			    
		
    
        	
							    public float timeToComplete= 0F;
		    			    
		
    
            public List<CraftingReward> rewards;
	    	
							    public int maxUses= 0;
		    			    
		
    
        	
							    public boolean active= false;
		    			    
		
    
        
	public static CraftingRecipeCache cache() {
		return CraftingRecipeCache.getInstance();
	}
	
	public static CraftingRecipeStore store() {
		return CraftingRecipeStore.getInstance();
	}


    public CraftingRecipe()
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
		
		public CraftingRecipe result(int timeout) {
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
	
	public static class CraftingRecipeCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, CraftingRecipe> cache = new Cache<String, CraftingRecipe>(120, 5000);
		
		private CraftingRecipeCache() {
		}
		
		private static class LazyHolder {
			private static final CraftingRecipeCache INSTANCE = new CraftingRecipeCache();
		}
	
		public static CraftingRecipeCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, CraftingRecipe>(expiration, size);
		}
	
		public Cache<String, CraftingRecipe> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(CraftingRecipeCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(CraftingRecipe message) {
			CacheUpdate cacheUpdate = new CacheUpdate(CraftingRecipeCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public CraftingRecipe get(String id, int timeout) {
			CraftingRecipe message = cache.get(id);
			if (message == null) {
				message = CraftingRecipe.store().get(id, timeout);
			}
			return message;
		}
			
		public static CraftingRecipe setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			CraftingRecipe message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (CraftingRecipe) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				CraftingRecipe.store().set(message);
			} else {
				message = CraftingRecipe.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, CraftingRecipe.class.getField(field));
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
				CraftingRecipe.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class CraftingRecipeStore {
	
		private CraftingRecipeStore() {
		}
		
		private static class LazyHolder {
			private static final CraftingRecipeStore INSTANCE = new CraftingRecipeStore();
		}
	
		public static CraftingRecipeStore getInstance() {
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
		
	    public void set(CraftingRecipe message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public CraftingRecipe get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, CraftingRecipe message) {
	    	CraftingRecipe clone = message.clone();
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
			
		public CraftingRecipe get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("CraftingRecipe");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			CraftingRecipe message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof CraftingRecipe) {
					message = (CraftingRecipe)result;
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
    	    	    	    	    	    	model.set("crafting_recipe_id",null);
    	    	    	    	    	    	    	    	model.set("crafting_recipe_order_matters",null);
    	    	    	    	    	    	model.set("crafting_recipe_time_to_complete",null);
    	    	    	    	    	    	    	model.set("crafting_recipe_max_uses",null);
    	    	    	    	    	    	model.set("crafting_recipe_active",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("crafting_recipe_id",id);
    	        		
    	//}
    	    	    	    	    	    	    	
    	    	    	//if (orderMatters != null) {
    	       	    	model.setBoolean("crafting_recipe_order_matters",orderMatters);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (timeToComplete != null) {
    	       	    	model.setFloat("crafting_recipe_time_to_complete",timeToComplete);
    	        		
    	//}
    	    	    	    	    	    	
    	    	    	//if (maxUses != null) {
    	       	    	model.setInteger("crafting_recipe_max_uses",maxUses);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (active != null) {
    	       	    	model.setBoolean("crafting_recipe_active",active);
    	        		
    	//}
    	    	    }
    
	public static CraftingRecipe fromModel(Model model) {
		boolean hasFields = false;
    	CraftingRecipe message = new CraftingRecipe();
    	    	    	    	    	
    	    	    	String idTestField = model.getString("crafting_recipe_id");
    	if (idTestField != null) {
    		String idField = idTestField;
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	    	    	
    	    	    	Boolean orderMattersTestField = model.getBoolean("crafting_recipe_order_matters");
    	if (orderMattersTestField != null) {
    		boolean orderMattersField = orderMattersTestField;
    		message.setOrderMatters(orderMattersField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Float timeToCompleteTestField = model.getFloat("crafting_recipe_time_to_complete");
    	if (timeToCompleteTestField != null) {
    		float timeToCompleteField = timeToCompleteTestField;
    		message.setTimeToComplete(timeToCompleteField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	    	
    	    	    	Integer maxUsesTestField = model.getInteger("crafting_recipe_max_uses");
    	if (maxUsesTestField != null) {
    		int maxUsesField = maxUsesTestField;
    		message.setMaxUses(maxUsesField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Boolean activeTestField = model.getBoolean("crafting_recipe_active");
    	if (activeTestField != null) {
    		boolean activeField = activeTestField;
    		message.setActive(activeField);
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
	
	public CraftingRecipe setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public List<CraftingElement> getElementsList() {
		if(this.elements == null)
            this.elements = new ArrayList<CraftingElement>();
		return elements;
	}

	public CraftingRecipe setElementsList(List<CraftingElement> elements) {
		this.elements = elements;
		return this;
	}

	public CraftingElement getElements(int index)  {
        return elements == null ? null : elements.get(index);
    }

    public int getElementsCount()  {
        return elements == null ? 0 : elements.size();
    }

    public CraftingRecipe addElements(CraftingElement elements)  {
        if(this.elements == null)
            this.elements = new ArrayList<CraftingElement>();
        this.elements.add(elements);
        return this;
    }
            	    	    	    	
    public CraftingRecipe removeElementsById(CraftingElement elements)  {
    	if(this.elements == null)
           return this;
            
       	Iterator<CraftingElement> itr = this.elements.iterator();
       	while (itr.hasNext()) {
    	CraftingElement obj = itr.next();
    	
    	    		if (elements.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeElementsByQuantity(CraftingElement elements)  {
    	if(this.elements == null)
           return this;
            
       	Iterator<CraftingElement> itr = this.elements.iterator();
       	while (itr.hasNext()) {
    	CraftingElement obj = itr.next();
    	
    	    		if (elements.quantity == obj.quantity) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeElementsByLevel(CraftingElement elements)  {
    	if(this.elements == null)
           return this;
            
       	Iterator<CraftingElement> itr = this.elements.iterator();
       	while (itr.hasNext()) {
    	CraftingElement obj = itr.next();
    	
    	    		if (elements.level == obj.level) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeElementsByOrder(CraftingElement elements)  {
    	if(this.elements == null)
           return this;
            
       	Iterator<CraftingElement> itr = this.elements.iterator();
       	while (itr.hasNext()) {
    	CraftingElement obj = itr.next();
    	
    	    		if (elements.order == obj.order) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		            
		public CraftingTool getTool() {
		return tool;
	}
	
	public CraftingRecipe setTool(CraftingTool tool) {
		this.tool = tool;
		return this;	}
	
		            
		public boolean getOrderMatters() {
		return orderMatters;
	}
	
	public CraftingRecipe setOrderMatters(boolean orderMatters) {
		this.orderMatters = orderMatters;
		return this;	}
	
		            
		public float getTimeToComplete() {
		return timeToComplete;
	}
	
	public CraftingRecipe setTimeToComplete(float timeToComplete) {
		this.timeToComplete = timeToComplete;
		return this;	}
	
		            
		public List<CraftingReward> getRewardsList() {
		if(this.rewards == null)
            this.rewards = new ArrayList<CraftingReward>();
		return rewards;
	}

	public CraftingRecipe setRewardsList(List<CraftingReward> rewards) {
		this.rewards = rewards;
		return this;
	}

	public CraftingReward getRewards(int index)  {
        return rewards == null ? null : rewards.get(index);
    }

    public int getRewardsCount()  {
        return rewards == null ? 0 : rewards.size();
    }

    public CraftingRecipe addRewards(CraftingReward rewards)  {
        if(this.rewards == null)
            this.rewards = new ArrayList<CraftingReward>();
        this.rewards.add(rewards);
        return this;
    }
            	    	    	    	
    public CraftingRecipe removeRewardsById(CraftingReward rewards)  {
    	if(this.rewards == null)
           return this;
            
       	Iterator<CraftingReward> itr = this.rewards.iterator();
       	while (itr.hasNext()) {
    	CraftingReward obj = itr.next();
    	
    	    		if (rewards.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeRewardsByQuantity(CraftingReward rewards)  {
    	if(this.rewards == null)
           return this;
            
       	Iterator<CraftingReward> itr = this.rewards.iterator();
       	while (itr.hasNext()) {
    	CraftingReward obj = itr.next();
    	
    	    		if (rewards.quantity == obj.quantity) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public CraftingRecipe removeRewardsByLevel(CraftingReward rewards)  {
    	if(this.rewards == null)
           return this;
            
       	Iterator<CraftingReward> itr = this.rewards.iterator();
       	while (itr.hasNext()) {
    	CraftingReward obj = itr.next();
    	
    	    		if (rewards.level == obj.level) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		            
		public int getMaxUses() {
		return maxUses;
	}
	
	public CraftingRecipe setMaxUses(int maxUses) {
		this.maxUses = maxUses;
		return this;	}
	
		            
		public boolean getActive() {
		return active;
	}
	
	public CraftingRecipe setActive(boolean active) {
		this.active = active;
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

    public Schema<CraftingRecipe> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public CraftingRecipe newMessage()
    {
        return new CraftingRecipe();
    }

    public Class<CraftingRecipe> typeClass()
    {
        return CraftingRecipe.class;
    }

    public String messageName()
    {
        return CraftingRecipe.class.getSimpleName();
    }

    public String messageFullName()
    {
        return CraftingRecipe.class.getName();
    }

    public boolean isInitialized(CraftingRecipe message)
    {
        return true;
    }

    public void mergeFrom(Input input, CraftingRecipe message) throws IOException
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
            	            		if(message.elements == null)
                        message.elements = new ArrayList<CraftingElement>();
                                        message.elements.add(input.mergeObject(null, CraftingElement.getSchema()));
                                        break;
                            	            	case 3:
            	                	                	message.tool = input.mergeObject(message.tool, CraftingTool.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.orderMatters = input.readBool();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.timeToComplete = input.readFloat();
                	break;
                	                	
                            	            	case 6:
            	            		if(message.rewards == null)
                        message.rewards = new ArrayList<CraftingReward>();
                                        message.rewards.add(input.mergeObject(null, CraftingReward.getSchema()));
                                        break;
                            	            	case 7:
            	                	                	message.maxUses = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.active = input.readBool();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, CraftingRecipe message) throws IOException
    {
    	    	
    	    	//if(message.id == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(1, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	if(message.elements != null)
        {
            for(CraftingElement elements : message.elements)
            {
                if( (CraftingElement) elements != null) {
                   	    				output.writeObject(2, elements, CraftingElement.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.tool != null)
    		output.writeObject(3, message.tool, CraftingTool.getSchema(), false);
    	    	
    	            	
    	    	//if(message.orderMatters == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Boolean)message.orderMatters != null) {
            output.writeBool(4, message.orderMatters, false);
        }
    	    	
    	            	
    	    	//if(message.timeToComplete == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Float)message.timeToComplete != null) {
            output.writeFloat(5, message.timeToComplete, false);
        }
    	    	
    	            	
    	    	
    	    	if(message.rewards != null)
        {
            for(CraftingReward rewards : message.rewards)
            {
                if( (CraftingReward) rewards != null) {
                   	    				output.writeObject(6, rewards, CraftingReward.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	//if(message.maxUses == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.maxUses != null) {
            output.writeInt32(7, message.maxUses, false);
        }
    	    	
    	            	
    	    	//if(message.active == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Boolean)message.active != null) {
            output.writeBool(8, message.active, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START CraftingRecipe");
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.elements != null) {
    		System.out.println("elements="+this.elements);
    	//}
    	    	//if(this.tool != null) {
    		System.out.println("tool="+this.tool);
    	//}
    	    	//if(this.orderMatters != null) {
    		System.out.println("orderMatters="+this.orderMatters);
    	//}
    	    	//if(this.timeToComplete != null) {
    		System.out.println("timeToComplete="+this.timeToComplete);
    	//}
    	    	//if(this.rewards != null) {
    		System.out.println("rewards="+this.rewards);
    	//}
    	    	//if(this.maxUses != null) {
    		System.out.println("maxUses="+this.maxUses);
    	//}
    	    	//if(this.active != null) {
    		System.out.println("active="+this.active);
    	//}
    	    	System.out.println("END CraftingRecipe");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "elements";
        	        	case 3: return "tool";
        	        	case 4: return "orderMatters";
        	        	case 5: return "timeToComplete";
        	        	case 6: return "rewards";
        	        	case 7: return "maxUses";
        	        	case 8: return "active";
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
    	    	__fieldMap.put("elements", 2);
    	    	__fieldMap.put("tool", 3);
    	    	__fieldMap.put("orderMatters", 4);
    	    	__fieldMap.put("timeToComplete", 5);
    	    	__fieldMap.put("rewards", 6);
    	    	__fieldMap.put("maxUses", 7);
    	    	__fieldMap.put("active", 8);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = CraftingRecipe.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static CraftingRecipe parseFrom(byte[] bytes) {
	CraftingRecipe message = new CraftingRecipe();
	ProtobufIOUtil.mergeFrom(bytes, message, CraftingRecipe.getSchema());
	return message;
}

public static CraftingRecipe parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	CraftingRecipe message = new CraftingRecipe();
	JsonIOUtil.mergeFrom(bytes, message, CraftingRecipe.getSchema(), false);
	return message;
}

public CraftingRecipe clone() {
	byte[] bytes = this.toByteArray();
	CraftingRecipe craftingRecipe = CraftingRecipe.parseFrom(bytes);
	return craftingRecipe;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, CraftingRecipe.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<CraftingRecipe> schema = CraftingRecipe.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, CraftingRecipe.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, CraftingRecipe.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
