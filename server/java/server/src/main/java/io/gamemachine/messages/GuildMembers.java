
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



@SuppressWarnings("unused")
public final class GuildMembers implements Externalizable, Message<GuildMembers>, Schema<GuildMembers>{



    public static Schema<GuildMembers> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GuildMembers getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GuildMembers DEFAULT_INSTANCE = new GuildMembers();
    static final String defaultScope = GuildMembers.class.getSimpleName();

    			public String guildId;
	    
        			public Integer recordId;
	    
        			public String playerId;
	    
        


    public GuildMembers()
    {
        
    }


	

	public static GuildMembersDb db() {
		return GuildMembersDb.getInstance();
	}
	
	public interface GuildMembersAsyncDb {
		void save(GuildMembers message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class GuildMembersAsyncDbImpl implements GuildMembersAsyncDb {
	
		public void save(GuildMembers message) {
			GuildMembers.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	GuildMembers.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	GuildMembers.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class GuildMembersDb {
	
		public Errors dbErrors;
		private GuildMembersAsyncDb asyncDb = null;
		
		private GuildMembersDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<GuildMembersAsyncDbImpl>(GuildMembersAsyncDb.class, GuildMembersAsyncDbImpl.class));
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
			private static final GuildMembersDb INSTANCE = new GuildMembersDb();
		}
	
		public static GuildMembersDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(GuildMembers message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(GuildMembers message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(GuildMembers message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildMembers.open();
	    	}
	    	
	    	io.gamemachine.orm.models.GuildMembers model = null;
	    	if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.GuildMembers.findFirst("id = ?", message.recordId);
	    	}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.GuildMembers();
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
	    		io.gamemachine.orm.models.GuildMembers.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.GuildMembers.open();
	    	int deleted = io.gamemachine.orm.models.GuildMembers.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.GuildMembers.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.GuildMembers.open();
	    	int deleted = io.gamemachine.orm.models.GuildMembers.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.GuildMembers.close();
	    	return result;
	    }
	    
	    public GuildMembers find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public GuildMembers find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildMembers.open();
	    	}
	    	
	    	io.gamemachine.orm.models.GuildMembers model = io.gamemachine.orm.models.GuildMembers.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildMembers.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		GuildMembers guildMembers = fromModel(model);
	    			    		return guildMembers;
	    	}
	    }
	    
	    public GuildMembers findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.GuildMembers.open();
	    	io.gamemachine.orm.models.GuildMembers model = io.gamemachine.orm.models.GuildMembers.findFirst(query, params);
	    	io.gamemachine.orm.models.GuildMembers.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		GuildMembers guildMembers = fromModel(model);
	    			    		return guildMembers;
	    	}
	    }
	    
	    public List<GuildMembers> findAll() {
	    	io.gamemachine.orm.models.GuildMembers.open();
	    	List<io.gamemachine.orm.models.GuildMembers> models = io.gamemachine.orm.models.GuildMembers.findAll();
	    	List<GuildMembers> messages = new ArrayList<GuildMembers>();
	    	for (io.gamemachine.orm.models.GuildMembers model : models) {
	    		GuildMembers guildMembers = fromModel(model);
	    			    		messages.add(guildMembers);
	    	}
	    	io.gamemachine.orm.models.GuildMembers.close();
	    	return messages;
	    }
	    
	    public List<GuildMembers> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<GuildMembers> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildMembers.open();
	    	}
	    	List<io.gamemachine.orm.models.GuildMembers> models = io.gamemachine.orm.models.GuildMembers.where(query, params);
	    	List<GuildMembers> messages = new ArrayList<GuildMembers>();
	    	for (io.gamemachine.orm.models.GuildMembers model : models) {
	    		GuildMembers guildMembers = fromModel(model);
	    			    		messages.add(guildMembers);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildMembers.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("guild_members_guild_id",null);
    	    	    	    	    	    	    	    	    	model.set("guild_members_player_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (guildId != null) {
    	       	    	model.setString("guild_members_guild_id",guildId);
    	        		
    	}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	if (playerId != null) {
    	       	    	model.setString("guild_members_player_id",playerId);
    	        		
    	}
    	    	    }
    
	public static GuildMembers fromModel(Model model) {
		boolean hasFields = false;
    	GuildMembers message = new GuildMembers();
    	    	    	    	    	
    	    	    	String guildIdField = model.getString("guild_members_guild_id");
    	    	
    	if (guildIdField != null) {
    		message.setGuildId(guildIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String playerIdField = model.getString("guild_members_player_id");
    	    	
    	if (playerIdField != null) {
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasGuildId()  {
        return guildId == null ? false : true;
    }
        
		public String getGuildId() {
		return guildId;
	}
	
	public GuildMembers setGuildId(String guildId) {
		this.guildId = guildId;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public GuildMembers setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public GuildMembers setPlayerId(String playerId) {
		this.playerId = playerId;
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

    public Schema<GuildMembers> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GuildMembers newMessage()
    {
        return new GuildMembers();
    }

    public Class<GuildMembers> typeClass()
    {
        return GuildMembers.class;
    }

    public String messageName()
    {
        return GuildMembers.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GuildMembers.class.getName();
    }

    public boolean isInitialized(GuildMembers message)
    {
        return true;
    }

    public void mergeFrom(Input input, GuildMembers message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.guildId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GuildMembers message) throws IOException
    {
    	    	
    	    	if(message.guildId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.guildId != null)
            output.writeString(1, message.guildId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(2, message.recordId, false);
    	    	
    	            	
    	    	if(message.playerId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(3, message.playerId, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GuildMembers");
    	    	if(this.guildId != null) {
    		System.out.println("guildId="+this.guildId);
    	}
    	    	if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	}
    	    	if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	}
    	    	System.out.println("END GuildMembers");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "guildId";
        	        	case 2: return "recordId";
        	        	case 3: return "playerId";
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
    	    	__fieldMap.put("guildId", 1);
    	    	__fieldMap.put("recordId", 2);
    	    	__fieldMap.put("playerId", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GuildMembers.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GuildMembers parseFrom(byte[] bytes) {
	GuildMembers message = new GuildMembers();
	ProtobufIOUtil.mergeFrom(bytes, message, GuildMembers.getSchema());
	return message;
}

public static GuildMembers parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GuildMembers message = new GuildMembers();
	JsonIOUtil.mergeFrom(bytes, message, GuildMembers.getSchema(), false);
	return message;
}

public GuildMembers clone() {
	byte[] bytes = this.toByteArray();
	GuildMembers guildMembers = GuildMembers.parseFrom(bytes);
	return guildMembers;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GuildMembers.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GuildMembers> schema = GuildMembers.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GuildMembers.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GuildMembers.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
