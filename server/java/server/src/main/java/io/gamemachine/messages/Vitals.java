
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





import org.javalite.common.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class Vitals implements Externalizable, Message<Vitals>, Schema<Vitals>{

private static final Logger logger = LoggerFactory.getLogger(Vitals.class);

	public enum VitalsType implements io.protostuff.EnumLite<VitalsType>
    {
    	
    	    	None(0),    	    	Character(1),    	    	Object(2);    	        
        public final int number;
        
        private VitalsType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static VitalsType defaultValue() {
        	return (None);
        }
        
        public static VitalsType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (None);
    			    			case 1: return (Character);
    			    			case 2: return (Object);
    			                default: return null;
            }
        }
    }
	public enum SubType implements io.protostuff.EnumLite<SubType>
    {
    	
    	    	SubTypeNone(0),    	    	Guard(1),    	    	AggressiveNpc(2),    	    	NeutralNpc(3),    	    	FriendlyNpc(4),    	    	AggressiveAnimal(5),    	    	NeutralAnimal(6),    	    	FriendlyAnimal(7),    	    	Player(8),    	    	Vehicle(9),    	    	Structure(10);    	        
        public final int number;
        
        private SubType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static SubType defaultValue() {
        	return (SubTypeNone);
        }
        
        public static SubType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (SubTypeNone);
    			    			case 1: return (Guard);
    			    			case 2: return (AggressiveNpc);
    			    			case 3: return (NeutralNpc);
    			    			case 4: return (FriendlyNpc);
    			    			case 5: return (AggressiveAnimal);
    			    			case 6: return (NeutralAnimal);
    			    			case 7: return (FriendlyAnimal);
    			    			case 8: return (Player);
    			    			case 9: return (Vehicle);
    			    			case 10: return (Structure);
    			                default: return null;
            }
        }
    }


    public static Schema<Vitals> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Vitals getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Vitals DEFAULT_INSTANCE = new Vitals();
    static final String defaultScope = Vitals.class.getSimpleName();

    	
							    public String characterId= null;
		    			    
		
    
        	
							    public int health= 0;
		    			    
		
    
        	
							    public int stamina= 0;
		    			    
		
    
        	
							    public int magic= 0;
		    			    
		
    
        	
							    public long lastCombat= 0L;
		    			    
		
    
        	
							    public int dead= 0;
		    			    
		
    
        	
							    public int armor= 0;
		    			    
		
    
        	
							    public int spellResist= 0;
		    			    
		
    
        	
							    public int elementalResist= 0;
		    			    
		
    
        	
							    public int spellPenetration= 0;
		    			    
		
    
        	
							    public int magicRegen= 0;
		    			    
		
    
        	
							    public int healthRegen= 0;
		    			    
		
    
        	
							    public int staminaRegen= 0;
		    			    
		
    
        	
							    public int changed= 0;
		    			    
		
    
        	
							    public String entityId= null;
		    			    
		
    
        	
							    public int staminaDrain= 0;
		    			    
		
    
        	
					public VitalsType type = VitalsType.defaultValue();
			    
		
    
        	
							    public int zone= 0;
		    			    
		
    
        	
					public SubType subType = SubType.defaultValue();
			    
		
    
        	
							    public String templateName= null;
		    			    
		
    
        


    public Vitals()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("vitals_character_id",null);
    	    	    	    	    	    	model.set("vitals_health",null);
    	    	    	    	    	    	model.set("vitals_stamina",null);
    	    	    	    	    	    	model.set("vitals_magic",null);
    	    	    	    	    	    	model.set("vitals_last_combat",null);
    	    	    	    	    	    	model.set("vitals_dead",null);
    	    	    	    	    	    	model.set("vitals_armor",null);
    	    	    	    	    	    	model.set("vitals_spell_resist",null);
    	    	    	    	    	    	model.set("vitals_elemental_resist",null);
    	    	    	    	    	    	model.set("vitals_spell_penetration",null);
    	    	    	    	    	    	model.set("vitals_magic_regen",null);
    	    	    	    	    	    	model.set("vitals_health_regen",null);
    	    	    	    	    	    	model.set("vitals_stamina_regen",null);
    	    	    	    	    	    	model.set("vitals_changed",null);
    	    	    	    	    	    	model.set("vitals_entity_id",null);
    	    	    	    	    	    	model.set("vitals_stamina_drain",null);
    	    	    	    	    	    	    	model.set("vitals_zone",null);
    	    	    	    	    	    	    	model.set("vitals_template_name",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (characterId != null) {
    	       	    	model.setString("vitals_character_id",characterId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (health != null) {
    	       	    	model.setInteger("vitals_health",health);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (stamina != null) {
    	       	    	model.setInteger("vitals_stamina",stamina);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (magic != null) {
    	       	    	model.setInteger("vitals_magic",magic);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (lastCombat != null) {
    	       	    	model.setLong("vitals_last_combat",lastCombat);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (dead != null) {
    	       	    	model.setInteger("vitals_dead",dead);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (armor != null) {
    	       	    	model.setInteger("vitals_armor",armor);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (spellResist != null) {
    	       	    	model.setInteger("vitals_spell_resist",spellResist);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (elementalResist != null) {
    	       	    	model.setInteger("vitals_elemental_resist",elementalResist);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (spellPenetration != null) {
    	       	    	model.setInteger("vitals_spell_penetration",spellPenetration);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (magicRegen != null) {
    	       	    	model.setInteger("vitals_magic_regen",magicRegen);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (healthRegen != null) {
    	       	    	model.setInteger("vitals_health_regen",healthRegen);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (staminaRegen != null) {
    	       	    	model.setInteger("vitals_stamina_regen",staminaRegen);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (changed != null) {
    	       	    	model.setInteger("vitals_changed",changed);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (entityId != null) {
    	       	    	model.setString("vitals_entity_id",entityId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (staminaDrain != null) {
    	       	    	model.setInteger("vitals_stamina_drain",staminaDrain);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (type != null) {
    	       	    	model.setInteger("vitals_type",type.number);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (zone != null) {
    	       	    	model.setInteger("vitals_zone",zone);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (subType != null) {
    	       	    	model.setInteger("vitals_sub_type",subType.number);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (templateName != null) {
    	       	    	model.setString("vitals_template_name",templateName);
    	        		
    	//}
    	    	    }
    
	public static Vitals fromModel(Model model) {
		boolean hasFields = false;
    	Vitals message = new Vitals();
    	    	    	    	    	
    	    			String characterIdTestField = model.getString("vitals_character_id");
		if (characterIdTestField != null) {
			String characterIdField = characterIdTestField;
			message.setCharacterId(characterIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer healthTestField = model.getInteger("vitals_health");
		if (healthTestField != null) {
			int healthField = healthTestField;
			message.setHealth(healthField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer staminaTestField = model.getInteger("vitals_stamina");
		if (staminaTestField != null) {
			int staminaField = staminaTestField;
			message.setStamina(staminaField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer magicTestField = model.getInteger("vitals_magic");
		if (magicTestField != null) {
			int magicField = magicTestField;
			message.setMagic(magicField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Long lastCombatTestField = model.getLong("vitals_last_combat");
		if (lastCombatTestField != null) {
			long lastCombatField = lastCombatTestField;
			message.setLastCombat(lastCombatField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer deadTestField = model.getInteger("vitals_dead");
		if (deadTestField != null) {
			int deadField = deadTestField;
			message.setDead(deadField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer armorTestField = model.getInteger("vitals_armor");
		if (armorTestField != null) {
			int armorField = armorTestField;
			message.setArmor(armorField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer spellResistTestField = model.getInteger("vitals_spell_resist");
		if (spellResistTestField != null) {
			int spellResistField = spellResistTestField;
			message.setSpellResist(spellResistField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer elementalResistTestField = model.getInteger("vitals_elemental_resist");
		if (elementalResistTestField != null) {
			int elementalResistField = elementalResistTestField;
			message.setElementalResist(elementalResistField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer spellPenetrationTestField = model.getInteger("vitals_spell_penetration");
		if (spellPenetrationTestField != null) {
			int spellPenetrationField = spellPenetrationTestField;
			message.setSpellPenetration(spellPenetrationField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer magicRegenTestField = model.getInteger("vitals_magic_regen");
		if (magicRegenTestField != null) {
			int magicRegenField = magicRegenTestField;
			message.setMagicRegen(magicRegenField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer healthRegenTestField = model.getInteger("vitals_health_regen");
		if (healthRegenTestField != null) {
			int healthRegenField = healthRegenTestField;
			message.setHealthRegen(healthRegenField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer staminaRegenTestField = model.getInteger("vitals_stamina_regen");
		if (staminaRegenTestField != null) {
			int staminaRegenField = staminaRegenTestField;
			message.setStaminaRegen(staminaRegenField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer changedTestField = model.getInteger("vitals_changed");
		if (changedTestField != null) {
			int changedField = changedTestField;
			message.setChanged(changedField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String entityIdTestField = model.getString("vitals_entity_id");
		if (entityIdTestField != null) {
			String entityIdField = entityIdTestField;
			message.setEntityId(entityIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer staminaDrainTestField = model.getInteger("vitals_stamina_drain");
		if (staminaDrainTestField != null) {
			int staminaDrainField = staminaDrainTestField;
			message.setStaminaDrain(staminaDrainField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    				message.setType(VitalsType.valueOf(model.getInteger("vitals_type")));
    	    	    	    	    	    	
    	    			Integer zoneTestField = model.getInteger("vitals_zone");
		if (zoneTestField != null) {
			int zoneField = zoneTestField;
			message.setZone(zoneField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    				message.setSubType(SubType.valueOf(model.getInteger("vitals_sub_type")));
    	    	    	    	    	    	
    	    			String templateNameTestField = model.getString("vitals_template_name");
		if (templateNameTestField != null) {
			String templateNameField = templateNameTestField;
			message.setTemplateName(templateNameField);
			hasFields = true;
		}
    	
    	    	
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public String getCharacterId() {
		return characterId;
	}
	
	public Vitals setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		            
		public int getHealth() {
		return health;
	}
	
	public Vitals setHealth(int health) {
		this.health = health;
		return this;	}
	
		            
		public int getStamina() {
		return stamina;
	}
	
	public Vitals setStamina(int stamina) {
		this.stamina = stamina;
		return this;	}
	
		            
		public int getMagic() {
		return magic;
	}
	
	public Vitals setMagic(int magic) {
		this.magic = magic;
		return this;	}
	
		            
		public long getLastCombat() {
		return lastCombat;
	}
	
	public Vitals setLastCombat(long lastCombat) {
		this.lastCombat = lastCombat;
		return this;	}
	
		            
		public int getDead() {
		return dead;
	}
	
	public Vitals setDead(int dead) {
		this.dead = dead;
		return this;	}
	
		            
		public int getArmor() {
		return armor;
	}
	
	public Vitals setArmor(int armor) {
		this.armor = armor;
		return this;	}
	
		            
		public int getSpellResist() {
		return spellResist;
	}
	
	public Vitals setSpellResist(int spellResist) {
		this.spellResist = spellResist;
		return this;	}
	
		            
		public int getElementalResist() {
		return elementalResist;
	}
	
	public Vitals setElementalResist(int elementalResist) {
		this.elementalResist = elementalResist;
		return this;	}
	
		            
		public int getSpellPenetration() {
		return spellPenetration;
	}
	
	public Vitals setSpellPenetration(int spellPenetration) {
		this.spellPenetration = spellPenetration;
		return this;	}
	
		            
		public int getMagicRegen() {
		return magicRegen;
	}
	
	public Vitals setMagicRegen(int magicRegen) {
		this.magicRegen = magicRegen;
		return this;	}
	
		            
		public int getHealthRegen() {
		return healthRegen;
	}
	
	public Vitals setHealthRegen(int healthRegen) {
		this.healthRegen = healthRegen;
		return this;	}
	
		            
		public int getStaminaRegen() {
		return staminaRegen;
	}
	
	public Vitals setStaminaRegen(int staminaRegen) {
		this.staminaRegen = staminaRegen;
		return this;	}
	
		            
		public int getChanged() {
		return changed;
	}
	
	public Vitals setChanged(int changed) {
		this.changed = changed;
		return this;	}
	
		            
		public String getEntityId() {
		return entityId;
	}
	
	public Vitals setEntityId(String entityId) {
		this.entityId = entityId;
		return this;	}
	
		            
		public int getStaminaDrain() {
		return staminaDrain;
	}
	
	public Vitals setStaminaDrain(int staminaDrain) {
		this.staminaDrain = staminaDrain;
		return this;	}
	
		            
		public VitalsType getType() {
		return type;
	}
	
	public Vitals setType(VitalsType type) {
		this.type = type;
		return this;	}
	
		            
		public int getZone() {
		return zone;
	}
	
	public Vitals setZone(int zone) {
		this.zone = zone;
		return this;	}
	
		            
		public SubType getSubType() {
		return subType;
	}
	
	public Vitals setSubType(SubType subType) {
		this.subType = subType;
		return this;	}
	
		            
		public String getTemplateName() {
		return templateName;
	}
	
	public Vitals setTemplateName(String templateName) {
		this.templateName = templateName;
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

    public Schema<Vitals> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Vitals newMessage()
    {
        return new Vitals();
    }

    public Class<Vitals> typeClass()
    {
        return Vitals.class;
    }

    public String messageName()
    {
        return Vitals.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Vitals.class.getName();
    }

    public boolean isInitialized(Vitals message)
    {
        return true;
    }

    public void mergeFrom(Input input, Vitals message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.stamina = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.magic = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.lastCombat = input.readInt64();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.dead = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.armor = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.spellResist = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.elementalResist = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.spellPenetration = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.magicRegen = input.readInt32();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.healthRegen = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.staminaRegen = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.changed = input.readInt32();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.entityId = input.readString();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.staminaDrain = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                    message.type = VitalsType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 19:
            	                	                	message.zone = input.readInt32();
                	break;
                	                	
                            	            	case 20:
            	                	                    message.subType = SubType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 21:
            	                	                	message.templateName = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Vitals message) throws IOException
    {
    	    	
    	    	//if(message.characterId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.characterId != null) {
            output.writeString(1, message.characterId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.health != null) {
            output.writeInt32(2, message.health, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.stamina != null) {
            output.writeInt32(3, message.stamina, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.magic != null) {
            output.writeInt32(4, message.magic, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.lastCombat != null) {
            output.writeInt64(5, message.lastCombat, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.dead != null) {
            output.writeInt32(6, message.dead, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.armor != null) {
            output.writeInt32(7, message.armor, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.spellResist != null) {
            output.writeInt32(8, message.spellResist, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.elementalResist != null) {
            output.writeInt32(9, message.elementalResist, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.spellPenetration != null) {
            output.writeInt32(10, message.spellPenetration, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.magicRegen != null) {
            output.writeInt32(11, message.magicRegen, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.healthRegen != null) {
            output.writeInt32(12, message.healthRegen, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.staminaRegen != null) {
            output.writeInt32(13, message.staminaRegen, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.changed != null) {
            output.writeInt32(14, message.changed, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.entityId != null) {
            output.writeString(15, message.entityId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.staminaDrain != null) {
            output.writeInt32(16, message.staminaDrain, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.type != null)
    	 	output.writeEnum(18, message.type.number, false);
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.zone != null) {
            output.writeInt32(19, message.zone, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.subType != null)
    	 	output.writeEnum(20, message.subType.number, false);
    	    	
    	            	
    	    	
    	    	    	if( (String)message.templateName != null) {
            output.writeString(21, message.templateName, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Vitals");
    	    	//if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	//}
    	    	//if(this.health != null) {
    		System.out.println("health="+this.health);
    	//}
    	    	//if(this.stamina != null) {
    		System.out.println("stamina="+this.stamina);
    	//}
    	    	//if(this.magic != null) {
    		System.out.println("magic="+this.magic);
    	//}
    	    	//if(this.lastCombat != null) {
    		System.out.println("lastCombat="+this.lastCombat);
    	//}
    	    	//if(this.dead != null) {
    		System.out.println("dead="+this.dead);
    	//}
    	    	//if(this.armor != null) {
    		System.out.println("armor="+this.armor);
    	//}
    	    	//if(this.spellResist != null) {
    		System.out.println("spellResist="+this.spellResist);
    	//}
    	    	//if(this.elementalResist != null) {
    		System.out.println("elementalResist="+this.elementalResist);
    	//}
    	    	//if(this.spellPenetration != null) {
    		System.out.println("spellPenetration="+this.spellPenetration);
    	//}
    	    	//if(this.magicRegen != null) {
    		System.out.println("magicRegen="+this.magicRegen);
    	//}
    	    	//if(this.healthRegen != null) {
    		System.out.println("healthRegen="+this.healthRegen);
    	//}
    	    	//if(this.staminaRegen != null) {
    		System.out.println("staminaRegen="+this.staminaRegen);
    	//}
    	    	//if(this.changed != null) {
    		System.out.println("changed="+this.changed);
    	//}
    	    	//if(this.entityId != null) {
    		System.out.println("entityId="+this.entityId);
    	//}
    	    	//if(this.staminaDrain != null) {
    		System.out.println("staminaDrain="+this.staminaDrain);
    	//}
    	    	//if(this.type != null) {
    		System.out.println("type="+this.type);
    	//}
    	    	//if(this.zone != null) {
    		System.out.println("zone="+this.zone);
    	//}
    	    	//if(this.subType != null) {
    		System.out.println("subType="+this.subType);
    	//}
    	    	//if(this.templateName != null) {
    		System.out.println("templateName="+this.templateName);
    	//}
    	    	System.out.println("END Vitals");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "characterId";
        	        	case 2: return "health";
        	        	case 3: return "stamina";
        	        	case 4: return "magic";
        	        	case 5: return "lastCombat";
        	        	case 6: return "dead";
        	        	case 7: return "armor";
        	        	case 8: return "spellResist";
        	        	case 9: return "elementalResist";
        	        	case 10: return "spellPenetration";
        	        	case 11: return "magicRegen";
        	        	case 12: return "healthRegen";
        	        	case 13: return "staminaRegen";
        	        	case 14: return "changed";
        	        	case 15: return "entityId";
        	        	case 16: return "staminaDrain";
        	        	case 18: return "type";
        	        	case 19: return "zone";
        	        	case 20: return "subType";
        	        	case 21: return "templateName";
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
    	    	__fieldMap.put("characterId", 1);
    	    	__fieldMap.put("health", 2);
    	    	__fieldMap.put("stamina", 3);
    	    	__fieldMap.put("magic", 4);
    	    	__fieldMap.put("lastCombat", 5);
    	    	__fieldMap.put("dead", 6);
    	    	__fieldMap.put("armor", 7);
    	    	__fieldMap.put("spellResist", 8);
    	    	__fieldMap.put("elementalResist", 9);
    	    	__fieldMap.put("spellPenetration", 10);
    	    	__fieldMap.put("magicRegen", 11);
    	    	__fieldMap.put("healthRegen", 12);
    	    	__fieldMap.put("staminaRegen", 13);
    	    	__fieldMap.put("changed", 14);
    	    	__fieldMap.put("entityId", 15);
    	    	__fieldMap.put("staminaDrain", 16);
    	    	__fieldMap.put("type", 18);
    	    	__fieldMap.put("zone", 19);
    	    	__fieldMap.put("subType", 20);
    	    	__fieldMap.put("templateName", 21);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Vitals.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Vitals parseFrom(byte[] bytes) {
	Vitals message = new Vitals();
	ProtobufIOUtil.mergeFrom(bytes, message, Vitals.getSchema());
	return message;
}

public static Vitals parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Vitals message = new Vitals();
	JsonIOUtil.mergeFrom(bytes, message, Vitals.getSchema(), false);
	return message;
}

public Vitals clone() {
	byte[] bytes = this.toByteArray();
	Vitals vitals = Vitals.parseFrom(bytes);
	return vitals;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Vitals.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Vitals> schema = Vitals.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Vitals.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Vitals.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
