
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
public final class ComboAttack implements Externalizable, Message<ComboAttack>, Schema<ComboAttack>{



    public static Schema<ComboAttack> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ComboAttack getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ComboAttack DEFAULT_INSTANCE = new ComboAttack();

    			public String id;
	    
            public List<Attack> attack;
	  
    public ComboAttack()
    {
        
    }


	

	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public ComboAttack setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasAttack()  {
        return attack == null ? false : true;
    }
        
		public List<Attack> getAttackList() {
		if(this.attack == null)
            this.attack = new ArrayList<Attack>();
		return attack;
	}

	public ComboAttack setAttackList(List<Attack> attack) {
		this.attack = attack;
		return this;
	}

	public Attack getAttack(int index)  {
        return attack == null ? null : attack.get(index);
    }

    public int getAttackCount()  {
        return attack == null ? 0 : attack.size();
    }

    public ComboAttack addAttack(Attack attack)  {
        if(this.attack == null)
            this.attack = new ArrayList<Attack>();
        this.attack.add(attack);
        return this;
    }
            	    	    	    	
    public ComboAttack removeAttackByAttacker(Attack attack)  {
    	if(this.attack == null)
           return this;
            
       	Iterator<Attack> itr = this.attack.iterator();
       	while (itr.hasNext()) {
    	Attack obj = itr.next();
    	
    	    		if (attack.attacker.equals(obj.attacker)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ComboAttack removeAttackByTarget(Attack attack)  {
    	if(this.attack == null)
           return this;
            
       	Iterator<Attack> itr = this.attack.iterator();
       	while (itr.hasNext()) {
    	Attack obj = itr.next();
    	
    	    		if (attack.target.equals(obj.target)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public ComboAttack removeAttackBySkill(Attack attack)  {
    	if(this.attack == null)
           return this;
            
       	Iterator<Attack> itr = this.attack.iterator();
       	while (itr.hasNext()) {
    	Attack obj = itr.next();
    	
    	    		if (attack.skill.equals(obj.skill)) {
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

    public Schema<ComboAttack> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ComboAttack newMessage()
    {
        return new ComboAttack();
    }

    public Class<ComboAttack> typeClass()
    {
        return ComboAttack.class;
    }

    public String messageName()
    {
        return ComboAttack.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ComboAttack.class.getName();
    }

    public boolean isInitialized(ComboAttack message)
    {
        return true;
    }

    public void mergeFrom(Input input, ComboAttack message) throws IOException
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
            	            		if(message.attack == null)
                        message.attack = new ArrayList<Attack>();
                                        message.attack.add(input.mergeObject(null, Attack.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ComboAttack message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	
    	    	if(message.attack != null)
        {
            for(Attack attack : message.attack)
            {
                if(attack != null) {
                   	    				output.writeObject(2, attack, Attack.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "attack";
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
    	    	__fieldMap.put("attack", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = ComboAttack.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static ComboAttack parseFrom(byte[] bytes) {
	ComboAttack message = new ComboAttack();
	ProtobufIOUtil.mergeFrom(bytes, message, ComboAttack.getSchema());
	return message;
}

public static ComboAttack parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	ComboAttack message = new ComboAttack();
	JsonIOUtil.mergeFrom(bytes, message, ComboAttack.getSchema(), false);
	return message;
}

public ComboAttack clone() {
	byte[] bytes = this.toByteArray();
	ComboAttack comboAttack = ComboAttack.parseFrom(bytes);
	return comboAttack;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, ComboAttack.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<ComboAttack> schema = ComboAttack.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, ComboAttack.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
