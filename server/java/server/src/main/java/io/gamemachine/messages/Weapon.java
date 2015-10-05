
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





import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class Weapon implements Externalizable, Message<Weapon>, Schema<Weapon>{



    public static Schema<Weapon> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Weapon getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Weapon DEFAULT_INSTANCE = new Weapon();
    static final String defaultScope = Weapon.class.getSimpleName();

    	
							    public int attack= 0;
		    			    
		
    
        	
							    public int delay= 0;
		    			    
		
    
        


    public Weapon()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("weapon_attack",null);
    	    	    	    	    	    	model.set("weapon_delay",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (attack != null) {
    	       	    	model.setInteger("weapon_attack",attack);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (delay != null) {
    	       	    	model.setInteger("weapon_delay",delay);
    	        		
    	//}
    	    	    }
    
	public static Weapon fromModel(Model model) {
		boolean hasFields = false;
    	Weapon message = new Weapon();
    	    	    	    	    	
    	    	    	Integer attackTestField = model.getInteger("weapon_attack");
    	if (attackTestField != null) {
    		int attackField = attackTestField;
    		message.setAttack(attackField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer delayTestField = model.getInteger("weapon_delay");
    	if (delayTestField != null) {
    		int delayField = delayTestField;
    		message.setDelay(delayField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public int getAttack() {
		return attack;
	}
	
	public Weapon setAttack(int attack) {
		this.attack = attack;
		return this;	}
	
		            
		public int getDelay() {
		return delay;
	}
	
	public Weapon setDelay(int delay) {
		this.delay = delay;
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

    public Schema<Weapon> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Weapon newMessage()
    {
        return new Weapon();
    }

    public Class<Weapon> typeClass()
    {
        return Weapon.class;
    }

    public String messageName()
    {
        return Weapon.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Weapon.class.getName();
    }

    public boolean isInitialized(Weapon message)
    {
        return true;
    }

    public void mergeFrom(Input input, Weapon message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.attack = input.readInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.delay = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Weapon message) throws IOException
    {
    	    	
    	    	//if(message.attack == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.attack != null) {
            output.writeInt32(1, message.attack, false);
        }
    	    	
    	            	
    	    	//if(message.delay == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.delay != null) {
            output.writeInt32(2, message.delay, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Weapon");
    	    	//if(this.attack != null) {
    		System.out.println("attack="+this.attack);
    	//}
    	    	//if(this.delay != null) {
    		System.out.println("delay="+this.delay);
    	//}
    	    	System.out.println("END Weapon");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "attack";
        	        	case 2: return "delay";
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
    	    	__fieldMap.put("attack", 1);
    	    	__fieldMap.put("delay", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Weapon.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Weapon parseFrom(byte[] bytes) {
	Weapon message = new Weapon();
	ProtobufIOUtil.mergeFrom(bytes, message, Weapon.getSchema());
	return message;
}

public static Weapon parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Weapon message = new Weapon();
	JsonIOUtil.mergeFrom(bytes, message, Weapon.getSchema(), false);
	return message;
}

public Weapon clone() {
	byte[] bytes = this.toByteArray();
	Weapon weapon = Weapon.parseFrom(bytes);
	return weapon;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Weapon.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Weapon> schema = Weapon.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Weapon.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Weapon.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
