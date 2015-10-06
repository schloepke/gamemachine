
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
public final class StatusEffect implements Externalizable, Message<StatusEffect>, Schema<StatusEffect>, PersistableMessage{

private static final Logger logger = LoggerFactory.getLogger(StatusEffect.class);

	public enum Type implements io.protostuff.EnumLite<Type>
    {
    	
    	    	StatusEffectNone(0),    	    	AttributeDecrease(1),    	    	AttributeIncrease(2),    	    	Heal(3),    	    	Slow(4),    	    	Stun(5),    	    	Root(6),    	    	Death(7),    	    	ArmorIncrease(8),    	    	SpellResistIncrease(9),    	    	ElementalResistIncrease(10),    	    	SpellPenetrationIncrease(11),    	    	MagicRegenIncrease(12),    	    	HealthRegenIncrease(13),    	    	StaminaRegenIncrease(14),    	    	ArmorDecrease(15),    	    	SpellResistDecrease(16),    	    	ElementalResistDecrease(17),    	    	MagicRegenDecrease(18),    	    	HealthRegenDecrease(19),    	    	StaminaRegenDecrease(20),    	    	Speed(21);    	        
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
        	return (StatusEffectNone);
        }
        
        public static Type valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (StatusEffectNone);
    			    			case 1: return (AttributeDecrease);
    			    			case 2: return (AttributeIncrease);
    			    			case 3: return (Heal);
    			    			case 4: return (Slow);
    			    			case 5: return (Stun);
    			    			case 6: return (Root);
    			    			case 7: return (Death);
    			    			case 8: return (ArmorIncrease);
    			    			case 9: return (SpellResistIncrease);
    			    			case 10: return (ElementalResistIncrease);
    			    			case 11: return (SpellPenetrationIncrease);
    			    			case 12: return (MagicRegenIncrease);
    			    			case 13: return (HealthRegenIncrease);
    			    			case 14: return (StaminaRegenIncrease);
    			    			case 15: return (ArmorDecrease);
    			    			case 16: return (SpellResistDecrease);
    			    			case 17: return (ElementalResistDecrease);
    			    			case 18: return (MagicRegenDecrease);
    			    			case 19: return (HealthRegenDecrease);
    			    			case 20: return (StaminaRegenDecrease);
    			    			case 21: return (Speed);
    			                default: return null;
            }
        }
    }
	public enum DamageType implements io.protostuff.EnumLite<DamageType>
    {
    	
    	    	DamageTypeNone(0),    	    	Physical(1),    	    	Magic(2),    	    	Elemental(3);    	        
        public final int number;
        
        private DamageType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static DamageType defaultValue() {
        	return (DamageTypeNone);
        }
        
        public static DamageType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (DamageTypeNone);
    			    			case 1: return (Physical);
    			    			case 2: return (Magic);
    			    			case 3: return (Elemental);
    			                default: return null;
            }
        }
    }


    public static Schema<StatusEffect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static StatusEffect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final StatusEffect DEFAULT_INSTANCE = new StatusEffect();
    static final String defaultScope = StatusEffect.class.getSimpleName();

    	
					public Type type = Type.defaultValue();
			    
		
    
        	
							    public String id= null;
		    			    
		
    
        	
							    public int duration= 0;
		    			    
		
    
        	
							    public int ticks= 0;
		    			    
		
    
        	
							    public String attribute= null;
		    			    
		
    
        	
							    public int minValue= 0;
		    			    
		
    
        	
							    public int maxValue= 0;
		    			    
		
    
        	
							    public String particleEffect= null;
		    			    
		
    
        	
					public DamageType damageType = DamageType.defaultValue();
			    
		
    
        	
							    public String icon_path= null;
		    			    
		
    
        	
							    public String icon_uuid= null;
		    			    
		
    
        
	public static StatusEffectCache cache() {
		return StatusEffectCache.getInstance();
	}
	
	public static StatusEffectStore store() {
		return StatusEffectStore.getInstance();
	}


    public StatusEffect()
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
		
		public StatusEffect result(int timeout) {
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
	
	public static class StatusEffectCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, StatusEffect> cache = new Cache<String, StatusEffect>(120, 5000);
		
		private StatusEffectCache() {
		}
		
		private static class LazyHolder {
			private static final StatusEffectCache INSTANCE = new StatusEffectCache();
		}
	
		public static StatusEffectCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, StatusEffect>(expiration, size);
		}
	
		public Cache<String, StatusEffect> getCache() {
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
			CacheUpdate cacheUpdate = new CacheUpdate(StatusEffectCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(StatusEffect message) {
			CacheUpdate cacheUpdate = new CacheUpdate(StatusEffectCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public StatusEffect get(String id, int timeout) {
			StatusEffect message = cache.get(id);
			if (message == null) {
				message = StatusEffect.store().get(id, timeout);
			}
			return message;
		}
			
		public static StatusEffect setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			StatusEffect message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (StatusEffect) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				StatusEffect.store().set(message);
			} else {
				message = StatusEffect.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, StatusEffect.class.getField(field));
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
				StatusEffect.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class StatusEffectStore {
	
		private StatusEffectStore() {
		}
		
		private static class LazyHolder {
			private static final StatusEffectStore INSTANCE = new StatusEffectStore();
		}
	
		public static StatusEffectStore getInstance() {
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
		
	    public void set(StatusEffect message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public StatusEffect get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, StatusEffect message) {
	    	StatusEffect clone = message.clone();
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
			
		public StatusEffect get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("StatusEffect");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			StatusEffect message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof StatusEffect) {
					message = (StatusEffect)result;
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
    	    	    	    	    	    	    	model.set("status_effect_id",null);
    	    	    	    	    	    	model.set("status_effect_duration",null);
    	    	    	    	    	    	model.set("status_effect_ticks",null);
    	    	    	    	    	    	model.set("status_effect_attribute",null);
    	    	    	    	    	    	model.set("status_effect_min_value",null);
    	    	    	    	    	    	model.set("status_effect_max_value",null);
    	    	    	    	    	    	model.set("status_effect_particle_effect",null);
    	    	    	    	    	    	    	model.set("status_effect_icon_path",null);
    	    	    	    	    	    	model.set("status_effect_icon_uuid",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (type != null) {
    	       	    	model.setInteger("status_effect_type",type.ordinal());
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (id != null) {
    	       	    	model.setString("status_effect_id",id);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (duration != null) {
    	       	    	model.setInteger("status_effect_duration",duration);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (ticks != null) {
    	       	    	model.setInteger("status_effect_ticks",ticks);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (attribute != null) {
    	       	    	model.setString("status_effect_attribute",attribute);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (minValue != null) {
    	       	    	model.setInteger("status_effect_min_value",minValue);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (maxValue != null) {
    	       	    	model.setInteger("status_effect_max_value",maxValue);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (particleEffect != null) {
    	       	    	model.setString("status_effect_particle_effect",particleEffect);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (damageType != null) {
    	       	    	model.setInteger("status_effect_damage_type",damageType.ordinal());
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (icon_path != null) {
    	       	    	model.setString("status_effect_icon_path",icon_path);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (icon_uuid != null) {
    	       	    	model.setString("status_effect_icon_uuid",icon_uuid);
    	        		
    	//}
    	    	    }
    
	public static StatusEffect fromModel(Model model) {
		boolean hasFields = false;
    	StatusEffect message = new StatusEffect();
    	    	    	    	    	
    				message.setType(Type.valueOf(model.getInteger("status_effect_type")));
    	    	    	    	    	    	
    	    			String idTestField = model.getString("status_effect_id");
		if (idTestField != null) {
			String idField = idTestField;
			message.setId(idField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer durationTestField = model.getInteger("status_effect_duration");
		if (durationTestField != null) {
			int durationField = durationTestField;
			message.setDuration(durationField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer ticksTestField = model.getInteger("status_effect_ticks");
		if (ticksTestField != null) {
			int ticksField = ticksTestField;
			message.setTicks(ticksField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String attributeTestField = model.getString("status_effect_attribute");
		if (attributeTestField != null) {
			String attributeField = attributeTestField;
			message.setAttribute(attributeField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer minValueTestField = model.getInteger("status_effect_min_value");
		if (minValueTestField != null) {
			int minValueField = minValueTestField;
			message.setMinValue(minValueField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer maxValueTestField = model.getInteger("status_effect_max_value");
		if (maxValueTestField != null) {
			int maxValueField = maxValueTestField;
			message.setMaxValue(maxValueField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String particleEffectTestField = model.getString("status_effect_particle_effect");
		if (particleEffectTestField != null) {
			String particleEffectField = particleEffectTestField;
			message.setParticleEffect(particleEffectField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    				message.setDamageType(DamageType.valueOf(model.getInteger("status_effect_damage_type")));
    	    	    	    	    	    	
    	    			String icon_pathTestField = model.getString("status_effect_icon_path");
		if (icon_pathTestField != null) {
			String icon_pathField = icon_pathTestField;
			message.setIcon_path(icon_pathField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String icon_uuidTestField = model.getString("status_effect_icon_uuid");
		if (icon_uuidTestField != null) {
			String icon_uuidField = icon_uuidTestField;
			message.setIcon_uuid(icon_uuidField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public Type getType() {
		return type;
	}
	
	public StatusEffect setType(Type type) {
		this.type = type;
		return this;	}
	
		            
		public String getId() {
		return id;
	}
	
	public StatusEffect setId(String id) {
		this.id = id;
		return this;	}
	
		            
		public int getDuration() {
		return duration;
	}
	
	public StatusEffect setDuration(int duration) {
		this.duration = duration;
		return this;	}
	
		            
		public int getTicks() {
		return ticks;
	}
	
	public StatusEffect setTicks(int ticks) {
		this.ticks = ticks;
		return this;	}
	
		            
		public String getAttribute() {
		return attribute;
	}
	
	public StatusEffect setAttribute(String attribute) {
		this.attribute = attribute;
		return this;	}
	
		            
		public int getMinValue() {
		return minValue;
	}
	
	public StatusEffect setMinValue(int minValue) {
		this.minValue = minValue;
		return this;	}
	
		            
		public int getMaxValue() {
		return maxValue;
	}
	
	public StatusEffect setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		return this;	}
	
		            
		public String getParticleEffect() {
		return particleEffect;
	}
	
	public StatusEffect setParticleEffect(String particleEffect) {
		this.particleEffect = particleEffect;
		return this;	}
	
		            
		public DamageType getDamageType() {
		return damageType;
	}
	
	public StatusEffect setDamageType(DamageType damageType) {
		this.damageType = damageType;
		return this;	}
	
		            
		public String getIcon_path() {
		return icon_path;
	}
	
	public StatusEffect setIcon_path(String icon_path) {
		this.icon_path = icon_path;
		return this;	}
	
		            
		public String getIcon_uuid() {
		return icon_uuid;
	}
	
	public StatusEffect setIcon_uuid(String icon_uuid) {
		this.icon_uuid = icon_uuid;
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

    public Schema<StatusEffect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public StatusEffect newMessage()
    {
        return new StatusEffect();
    }

    public Class<StatusEffect> typeClass()
    {
        return StatusEffect.class;
    }

    public String messageName()
    {
        return StatusEffect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return StatusEffect.class.getName();
    }

    public boolean isInitialized(StatusEffect message)
    {
        return true;
    }

    public void mergeFrom(Input input, StatusEffect message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                    message.type = Type.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 2:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.duration = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.ticks = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.attribute = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.minValue = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.maxValue = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.particleEffect = input.readString();
                	break;
                	                	
                            	            	case 9:
            	                	                    message.damageType = DamageType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 10:
            	                	                	message.icon_path = input.readString();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.icon_uuid = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, StatusEffect message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.type != null)
    	 	output.writeEnum(1, message.type.number, false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.id != null) {
            output.writeString(2, message.id, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.duration != null) {
            output.writeInt32(3, message.duration, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.ticks != null) {
            output.writeInt32(4, message.ticks, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.attribute != null) {
            output.writeString(5, message.attribute, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.minValue != null) {
            output.writeInt32(6, message.minValue, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.maxValue != null) {
            output.writeInt32(7, message.maxValue, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.particleEffect != null) {
            output.writeString(8, message.particleEffect, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.damageType != null)
    	 	output.writeEnum(9, message.damageType.number, false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.icon_path != null) {
            output.writeString(10, message.icon_path, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.icon_uuid != null) {
            output.writeString(11, message.icon_uuid, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START StatusEffect");
    	    	//if(this.type != null) {
    		System.out.println("type="+this.type);
    	//}
    	    	//if(this.id != null) {
    		System.out.println("id="+this.id);
    	//}
    	    	//if(this.duration != null) {
    		System.out.println("duration="+this.duration);
    	//}
    	    	//if(this.ticks != null) {
    		System.out.println("ticks="+this.ticks);
    	//}
    	    	//if(this.attribute != null) {
    		System.out.println("attribute="+this.attribute);
    	//}
    	    	//if(this.minValue != null) {
    		System.out.println("minValue="+this.minValue);
    	//}
    	    	//if(this.maxValue != null) {
    		System.out.println("maxValue="+this.maxValue);
    	//}
    	    	//if(this.particleEffect != null) {
    		System.out.println("particleEffect="+this.particleEffect);
    	//}
    	    	//if(this.damageType != null) {
    		System.out.println("damageType="+this.damageType);
    	//}
    	    	//if(this.icon_path != null) {
    		System.out.println("icon_path="+this.icon_path);
    	//}
    	    	//if(this.icon_uuid != null) {
    		System.out.println("icon_uuid="+this.icon_uuid);
    	//}
    	    	System.out.println("END StatusEffect");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "type";
        	        	case 2: return "id";
        	        	case 3: return "duration";
        	        	case 4: return "ticks";
        	        	case 5: return "attribute";
        	        	case 6: return "minValue";
        	        	case 7: return "maxValue";
        	        	case 8: return "particleEffect";
        	        	case 9: return "damageType";
        	        	case 10: return "icon_path";
        	        	case 11: return "icon_uuid";
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
    	    	__fieldMap.put("type", 1);
    	    	__fieldMap.put("id", 2);
    	    	__fieldMap.put("duration", 3);
    	    	__fieldMap.put("ticks", 4);
    	    	__fieldMap.put("attribute", 5);
    	    	__fieldMap.put("minValue", 6);
    	    	__fieldMap.put("maxValue", 7);
    	    	__fieldMap.put("particleEffect", 8);
    	    	__fieldMap.put("damageType", 9);
    	    	__fieldMap.put("icon_path", 10);
    	    	__fieldMap.put("icon_uuid", 11);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = StatusEffect.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static StatusEffect parseFrom(byte[] bytes) {
	StatusEffect message = new StatusEffect();
	ProtobufIOUtil.mergeFrom(bytes, message, StatusEffect.getSchema());
	return message;
}

public static StatusEffect parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	StatusEffect message = new StatusEffect();
	JsonIOUtil.mergeFrom(bytes, message, StatusEffect.getSchema(), false);
	return message;
}

public StatusEffect clone() {
	byte[] bytes = this.toByteArray();
	StatusEffect statusEffect = StatusEffect.parseFrom(bytes);
	return statusEffect;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, StatusEffect.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<StatusEffect> schema = StatusEffect.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, StatusEffect.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, StatusEffect.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
