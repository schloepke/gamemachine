
package io.gamemachine.client.messages;

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


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class PlayerSkills implements Externalizable, Message<PlayerSkills>, Schema<PlayerSkills>{



    public static Schema<PlayerSkills> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerSkills getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerSkills DEFAULT_INSTANCE = new PlayerSkills();

        public List<PlayerSkill> playerSkill;
	  
    public PlayerSkills()
    {
        
    }


	

	    
    public Boolean hasPlayerSkill()  {
        return playerSkill == null ? false : true;
    }
        
		public List<PlayerSkill> getPlayerSkillList() {
		if(this.playerSkill == null)
            this.playerSkill = new ArrayList<PlayerSkill>();
		return playerSkill;
	}

	public PlayerSkills setPlayerSkillList(List<PlayerSkill> playerSkill) {
		this.playerSkill = playerSkill;
		return this;
	}

	public PlayerSkill getPlayerSkill(int index)  {
        return playerSkill == null ? null : playerSkill.get(index);
    }

    public int getPlayerSkillCount()  {
        return playerSkill == null ? 0 : playerSkill.size();
    }

    public PlayerSkills addPlayerSkill(PlayerSkill playerSkill)  {
        if(this.playerSkill == null)
            this.playerSkill = new ArrayList<PlayerSkill>();
        this.playerSkill.add(playerSkill);
        return this;
    }
            	    	    	    	
    public PlayerSkills removePlayerSkillById(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByName(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.name.equals(obj.name)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByRecordId(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByCategory(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.category.equals(obj.category)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByDamageType(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.damageType.equals(obj.damageType)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByIcon_path(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.icon_path.equals(obj.icon_path)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByDescription(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.description.equals(obj.description)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByResource(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.resource.equals(obj.resource)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByResourceCost(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.resourceCost.equals(obj.resourceCost)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByCharacterId(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.characterId.equals(obj.characterId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByWeaponType(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.weaponType.equals(obj.weaponType)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByRange(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.range.equals(obj.range)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByStatusEffectId(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.statusEffectId.equals(obj.statusEffectId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByLevel(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.level.equals(obj.level)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByResourceCostPerTick(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.resourceCostPerTick.equals(obj.resourceCostPerTick)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByIsComboPart(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.isComboPart.equals(obj.isComboPart)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByIsPassive(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.isPassive.equals(obj.isPassive)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillBySkillType(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.skillType.equals(obj.skillType)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByIcon_uuid(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.icon_uuid.equals(obj.icon_uuid)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public PlayerSkills removePlayerSkillByStatusEffects(PlayerSkill playerSkill)  {
    	if(this.playerSkill == null)
           return this;
            
       	Iterator<PlayerSkill> itr = this.playerSkill.iterator();
       	while (itr.hasNext()) {
    	PlayerSkill obj = itr.next();
    	
    	    		if (playerSkill.statusEffects.equals(obj.statusEffects)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
	
  
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

    public Schema<PlayerSkills> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerSkills newMessage()
    {
        return new PlayerSkills();
    }

    public Class<PlayerSkills> typeClass()
    {
        return PlayerSkills.class;
    }

    public String messageName()
    {
        return PlayerSkills.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerSkills.class.getName();
    }

    public boolean isInitialized(PlayerSkills message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerSkills message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.playerSkill == null)
                        message.playerSkill = new ArrayList<PlayerSkill>();
                                        message.playerSkill.add(input.mergeObject(null, PlayerSkill.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerSkills message) throws IOException
    {
    	    	
    	    	
    	    	if(message.playerSkill != null)
        {
            for(PlayerSkill playerSkill : message.playerSkill)
            {
                if(playerSkill != null) {
                   	    				output.writeObject(1, playerSkill, PlayerSkill.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerSkill";
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
    	    	__fieldMap.put("playerSkill", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerSkills.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerSkills parseFrom(byte[] bytes) {
	PlayerSkills message = new PlayerSkills();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerSkills.getSchema());
	return message;
}

public static PlayerSkills parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerSkills message = new PlayerSkills();
	JsonIOUtil.mergeFrom(bytes, message, PlayerSkills.getSchema(), false);
	return message;
}

public PlayerSkills clone() {
	byte[] bytes = this.toByteArray();
	PlayerSkills playerSkills = PlayerSkills.parseFrom(bytes);
	return playerSkills;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerSkills.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerSkills> schema = PlayerSkills.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerSkills.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
