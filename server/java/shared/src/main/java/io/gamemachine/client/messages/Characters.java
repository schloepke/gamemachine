
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
public final class Characters implements Externalizable, Message<Characters>, Schema<Characters>{



    public static Schema<Characters> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Characters getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Characters DEFAULT_INSTANCE = new Characters();

        public List<Character> characters;
	    			public String id;
	    
      
    public Characters()
    {
        
    }


	

	    
    public Boolean hasCharacters()  {
        return characters == null ? false : true;
    }
        
		public List<Character> getCharactersList() {
		if(this.characters == null)
            this.characters = new ArrayList<Character>();
		return characters;
	}

	public Characters setCharactersList(List<Character> characters) {
		this.characters = characters;
		return this;
	}

	public Character getCharacters(int index)  {
        return characters == null ? null : characters.get(index);
    }

    public int getCharactersCount()  {
        return characters == null ? 0 : characters.size();
    }

    public Characters addCharacters(Character characters)  {
        if(this.characters == null)
            this.characters = new ArrayList<Character>();
        this.characters.add(characters);
        return this;
    }
            	    	    	    	
    public Characters removeCharactersById(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByUmaData(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.umaData.equals(obj.umaData)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByHealth(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.health.equals(obj.health)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByRecordId(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByPlayerId(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.playerId.equals(obj.playerId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByPart(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.part.equals(obj.part)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByParts(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.parts.equals(obj.parts)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByWorldx(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldx.equals(obj.worldx)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByWorldy(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldy.equals(obj.worldy)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByWorldz(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.worldz.equals(obj.worldz)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByZone(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.zone.equals(obj.zone)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByStamina(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.stamina.equals(obj.stamina)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByMagic(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.magic.equals(obj.magic)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Characters removeCharactersByIncludeUmaData(Character characters)  {
    	if(this.characters == null)
           return this;
            
       	Iterator<Character> itr = this.characters.iterator();
       	while (itr.hasNext()) {
    	Character obj = itr.next();
    	
    	    		if (characters.includeUmaData.equals(obj.includeUmaData)) {
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
	
	public Characters setId(String id) {
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

    public Schema<Characters> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Characters newMessage()
    {
        return new Characters();
    }

    public Class<Characters> typeClass()
    {
        return Characters.class;
    }

    public String messageName()
    {
        return Characters.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Characters.class.getName();
    }

    public boolean isInitialized(Characters message)
    {
        return true;
    }

    public void mergeFrom(Input input, Characters message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.characters == null)
                        message.characters = new ArrayList<Character>();
                                        message.characters.add(input.mergeObject(null, Character.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Characters message) throws IOException
    {
    	    	
    	    	
    	    	if(message.characters != null)
        {
            for(Character characters : message.characters)
            {
                if(characters != null) {
                   	    				output.writeObject(1, characters, Character.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.id != null)
            output.writeString(2, message.id, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "characters";
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
    	    	__fieldMap.put("characters", 1);
    	    	__fieldMap.put("id", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Characters.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Characters parseFrom(byte[] bytes) {
	Characters message = new Characters();
	ProtobufIOUtil.mergeFrom(bytes, message, Characters.getSchema());
	return message;
}

public static Characters parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Characters message = new Characters();
	JsonIOUtil.mergeFrom(bytes, message, Characters.getSchema(), false);
	return message;
}

public Characters clone() {
	byte[] bytes = this.toByteArray();
	Characters characters = Characters.parseFrom(bytes);
	return characters;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Characters.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Characters> schema = Characters.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Characters.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
