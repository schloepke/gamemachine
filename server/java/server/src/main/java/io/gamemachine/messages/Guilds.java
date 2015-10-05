
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
public final class Guilds implements Externalizable, Message<Guilds>, Schema<Guilds>{



    public static Schema<Guilds> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Guilds getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Guilds DEFAULT_INSTANCE = new Guilds();
    static final String defaultScope = Guilds.class.getSimpleName();

        public List<Guild> guild;
	    


    public Guilds()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static Guilds fromModel(Model model) {
		boolean hasFields = false;
    	Guilds message = new Guilds();
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public List<Guild> getGuildList() {
		if(this.guild == null)
            this.guild = new ArrayList<Guild>();
		return guild;
	}

	public Guilds setGuildList(List<Guild> guild) {
		this.guild = guild;
		return this;
	}

	public Guild getGuild(int index)  {
        return guild == null ? null : guild.get(index);
    }

    public int getGuildCount()  {
        return guild == null ? 0 : guild.size();
    }

    public Guilds addGuild(Guild guild)  {
        if(this.guild == null)
            this.guild = new ArrayList<Guild>();
        this.guild.add(guild);
        return this;
    }
            	    	    	    	
    public Guilds removeGuildById(Guild guild)  {
    	if(this.guild == null)
           return this;
            
       	Iterator<Guild> itr = this.guild.iterator();
       	while (itr.hasNext()) {
    	Guild obj = itr.next();
    	
    	    		if (guild.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Guilds removeGuildByOwnerId(Guild guild)  {
    	if(this.guild == null)
           return this;
            
       	Iterator<Guild> itr = this.guild.iterator();
       	while (itr.hasNext()) {
    	Guild obj = itr.next();
    	
    	    		if (guild.ownerId.equals(obj.ownerId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Guilds removeGuildByRecordId(Guild guild)  {
    	if(this.guild == null)
           return this;
            
       	Iterator<Guild> itr = this.guild.iterator();
       	while (itr.hasNext()) {
    	Guild obj = itr.next();
    	
    	    		if (guild.recordId == obj.recordId) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Guilds removeGuildByName(Guild guild)  {
    	if(this.guild == null)
           return this;
            
       	Iterator<Guild> itr = this.guild.iterator();
       	while (itr.hasNext()) {
    	Guild obj = itr.next();
    	
    	    		if (guild.name.equals(obj.name)) {
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

    public Schema<Guilds> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Guilds newMessage()
    {
        return new Guilds();
    }

    public Class<Guilds> typeClass()
    {
        return Guilds.class;
    }

    public String messageName()
    {
        return Guilds.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Guilds.class.getName();
    }

    public boolean isInitialized(Guilds message)
    {
        return true;
    }

    public void mergeFrom(Input input, Guilds message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.guild == null)
                        message.guild = new ArrayList<Guild>();
                                        message.guild.add(input.mergeObject(null, Guild.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Guilds message) throws IOException
    {
    	    	
    	    	
    	    	if(message.guild != null)
        {
            for(Guild guild : message.guild)
            {
                if( (Guild) guild != null) {
                   	    				output.writeObject(1, guild, Guild.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Guilds");
    	    	//if(this.guild != null) {
    		System.out.println("guild="+this.guild);
    	//}
    	    	System.out.println("END Guilds");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "guild";
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
    	    	__fieldMap.put("guild", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Guilds.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Guilds parseFrom(byte[] bytes) {
	Guilds message = new Guilds();
	ProtobufIOUtil.mergeFrom(bytes, message, Guilds.getSchema());
	return message;
}

public static Guilds parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Guilds message = new Guilds();
	JsonIOUtil.mergeFrom(bytes, message, Guilds.getSchema(), false);
	return message;
}

public Guilds clone() {
	byte[] bytes = this.toByteArray();
	Guilds guilds = Guilds.parseFrom(bytes);
	return guilds;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Guilds.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Guilds> schema = Guilds.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Guilds.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Guilds.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
