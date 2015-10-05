
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
public final class GuildAction implements Externalizable, Message<GuildAction>, Schema<GuildAction>{



    public static Schema<GuildAction> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GuildAction getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GuildAction DEFAULT_INSTANCE = new GuildAction();
    static final String defaultScope = GuildAction.class.getSimpleName();

    	
							    public String action= null;
		    			    
		
    
        	
							    public String to= null;
		    			    
		
    
        	
							    public String from= null;
		    			    
		
    
        	
							    public String response= null;
		    			    
		
    
        	
							    public String guildId= null;
		    			    
		
    
        	
							    public int recordId= 0;
		    			    
		
    
        	
							    public String inviteId= null;
		    			    
		
    
        	
							    public String guildName= null;
		    			    
		
    
        


    public GuildAction()
    {
        
    }


	

	public static GuildActionDb db() {
		return GuildActionDb.getInstance();
	}
	
	public interface GuildActionAsyncDb {
		void save(GuildAction message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class GuildActionAsyncDbImpl implements GuildActionAsyncDb {
	
		public void save(GuildAction message) {
			GuildAction.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	GuildAction.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	GuildAction.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class GuildActionDb {
	
		public Errors dbErrors;
		private GuildActionAsyncDb asyncDb = null;
		
		private GuildActionDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<GuildActionAsyncDbImpl>(GuildActionAsyncDb.class, GuildActionAsyncDbImpl.class));
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
			private static final GuildActionDb INSTANCE = new GuildActionDb();
		}
	
		public static GuildActionDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(GuildAction message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(GuildAction message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(GuildAction message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildAction.open();
	    	}
	    	
	    	io.gamemachine.orm.models.GuildAction model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.GuildAction.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.GuildAction();
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
	    		io.gamemachine.orm.models.GuildAction.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.GuildAction.open();
	    	int deleted = io.gamemachine.orm.models.GuildAction.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.GuildAction.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.GuildAction.open();
	    	int deleted = io.gamemachine.orm.models.GuildAction.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.GuildAction.close();
	    	return result;
	    }
	    
	    public GuildAction find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public GuildAction find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildAction.open();
	    	}
	    	
	    	io.gamemachine.orm.models.GuildAction model = io.gamemachine.orm.models.GuildAction.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildAction.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		GuildAction guildAction = fromModel(model);
	    			    		return guildAction;
	    	}
	    }
	    
	    public GuildAction findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.GuildAction.open();
	    	io.gamemachine.orm.models.GuildAction model = io.gamemachine.orm.models.GuildAction.findFirst(query, params);
	    	io.gamemachine.orm.models.GuildAction.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		GuildAction guildAction = fromModel(model);
	    			    		return guildAction;
	    	}
	    }
	    
	    public List<GuildAction> findAll() {
	    	io.gamemachine.orm.models.GuildAction.open();
	    	List<io.gamemachine.orm.models.GuildAction> models = io.gamemachine.orm.models.GuildAction.findAll();
	    	List<GuildAction> messages = new ArrayList<GuildAction>();
	    	for (io.gamemachine.orm.models.GuildAction model : models) {
	    		GuildAction guildAction = fromModel(model);
	    			    		messages.add(guildAction);
	    	}
	    	io.gamemachine.orm.models.GuildAction.close();
	    	return messages;
	    }
	    
	    public List<GuildAction> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<GuildAction> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildAction.open();
	    	}
	    	List<io.gamemachine.orm.models.GuildAction> models = io.gamemachine.orm.models.GuildAction.where(query, params);
	    	List<GuildAction> messages = new ArrayList<GuildAction>();
	    	for (io.gamemachine.orm.models.GuildAction model : models) {
	    		GuildAction guildAction = fromModel(model);
	    			    		messages.add(guildAction);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.GuildAction.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("guild_action_action",null);
    	    	    	    	    	    	model.set("guild_action_to",null);
    	    	    	    	    	    	model.set("guild_action_from",null);
    	    	    	    	    	    	model.set("guild_action_response",null);
    	    	    	    	    	    	model.set("guild_action_guild_id",null);
    	    	    	    	    	    	    	    	    	model.set("guild_action_invite_id",null);
    	    	    	    	    	    	model.set("guild_action_guild_name",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (action != null) {
    	       	    	model.setString("guild_action_action",action);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (to != null) {
    	       	    	model.setString("guild_action_to",to);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (from != null) {
    	       	    	model.setString("guild_action_from",from);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (response != null) {
    	       	    	model.setString("guild_action_response",response);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (guildId != null) {
    	       	    	model.setString("guild_action_guild_id",guildId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	//if (inviteId != null) {
    	       	    	model.setString("guild_action_invite_id",inviteId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (guildName != null) {
    	       	    	model.setString("guild_action_guild_name",guildName);
    	        		
    	//}
    	    	    }
    
	public static GuildAction fromModel(Model model) {
		boolean hasFields = false;
    	GuildAction message = new GuildAction();
    	    	    	    	    	
    	    	    	String actionTestField = model.getString("guild_action_action");
    	if (actionTestField != null) {
    		String actionField = actionTestField;
    		message.setAction(actionField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String toTestField = model.getString("guild_action_to");
    	if (toTestField != null) {
    		String toField = toTestField;
    		message.setTo(toField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String fromTestField = model.getString("guild_action_from");
    	if (fromTestField != null) {
    		String fromField = fromTestField;
    		message.setFrom(fromField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String responseTestField = model.getString("guild_action_response");
    	if (responseTestField != null) {
    		String responseField = responseTestField;
    		message.setResponse(responseField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String guildIdTestField = model.getString("guild_action_guild_id");
    	if (guildIdTestField != null) {
    		String guildIdField = guildIdTestField;
    		message.setGuildId(guildIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	    	    	    	
    	    	    	String inviteIdTestField = model.getString("guild_action_invite_id");
    	if (inviteIdTestField != null) {
    		String inviteIdField = inviteIdTestField;
    		message.setInviteId(inviteIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String guildNameTestField = model.getString("guild_action_guild_name");
    	if (guildNameTestField != null) {
    		String guildNameField = guildNameTestField;
    		message.setGuildName(guildNameField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getAction() {
		return action;
	}
	
	public GuildAction setAction(String action) {
		this.action = action;
		return this;	}
	
		            
		public String getTo() {
		return to;
	}
	
	public GuildAction setTo(String to) {
		this.to = to;
		return this;	}
	
		            
		public String getFrom() {
		return from;
	}
	
	public GuildAction setFrom(String from) {
		this.from = from;
		return this;	}
	
		            
		public String getResponse() {
		return response;
	}
	
	public GuildAction setResponse(String response) {
		this.response = response;
		return this;	}
	
		            
		public String getGuildId() {
		return guildId;
	}
	
	public GuildAction setGuildId(String guildId) {
		this.guildId = guildId;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public GuildAction setRecordId(int recordId) {
		this.recordId = recordId;
		return this;	}
	
		            
		public String getInviteId() {
		return inviteId;
	}
	
	public GuildAction setInviteId(String inviteId) {
		this.inviteId = inviteId;
		return this;	}
	
		            
		public String getGuildName() {
		return guildName;
	}
	
	public GuildAction setGuildName(String guildName) {
		this.guildName = guildName;
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

    public Schema<GuildAction> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GuildAction newMessage()
    {
        return new GuildAction();
    }

    public Class<GuildAction> typeClass()
    {
        return GuildAction.class;
    }

    public String messageName()
    {
        return GuildAction.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GuildAction.class.getName();
    }

    public boolean isInitialized(GuildAction message)
    {
        return true;
    }

    public void mergeFrom(Input input, GuildAction message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.action = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.to = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.from = input.readString();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.response = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.guildId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.inviteId = input.readString();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.guildName = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GuildAction message) throws IOException
    {
    	    	
    	    	//if(message.action == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.action != null) {
            output.writeString(1, message.action, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.to != null) {
            output.writeString(2, message.to, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.from != null) {
            output.writeString(3, message.from, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.response != null) {
            output.writeString(4, message.response, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.guildId != null) {
            output.writeString(5, message.guildId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(6, message.recordId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.inviteId != null) {
            output.writeString(7, message.inviteId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.guildName != null) {
            output.writeString(8, message.guildName, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GuildAction");
    	    	//if(this.action != null) {
    		System.out.println("action="+this.action);
    	//}
    	    	//if(this.to != null) {
    		System.out.println("to="+this.to);
    	//}
    	    	//if(this.from != null) {
    		System.out.println("from="+this.from);
    	//}
    	    	//if(this.response != null) {
    		System.out.println("response="+this.response);
    	//}
    	    	//if(this.guildId != null) {
    		System.out.println("guildId="+this.guildId);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	//if(this.inviteId != null) {
    		System.out.println("inviteId="+this.inviteId);
    	//}
    	    	//if(this.guildName != null) {
    		System.out.println("guildName="+this.guildName);
    	//}
    	    	System.out.println("END GuildAction");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "action";
        	        	case 2: return "to";
        	        	case 3: return "from";
        	        	case 4: return "response";
        	        	case 5: return "guildId";
        	        	case 6: return "recordId";
        	        	case 7: return "inviteId";
        	        	case 8: return "guildName";
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
    	    	__fieldMap.put("action", 1);
    	    	__fieldMap.put("to", 2);
    	    	__fieldMap.put("from", 3);
    	    	__fieldMap.put("response", 4);
    	    	__fieldMap.put("guildId", 5);
    	    	__fieldMap.put("recordId", 6);
    	    	__fieldMap.put("inviteId", 7);
    	    	__fieldMap.put("guildName", 8);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GuildAction.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GuildAction parseFrom(byte[] bytes) {
	GuildAction message = new GuildAction();
	ProtobufIOUtil.mergeFrom(bytes, message, GuildAction.getSchema());
	return message;
}

public static GuildAction parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GuildAction message = new GuildAction();
	JsonIOUtil.mergeFrom(bytes, message, GuildAction.getSchema(), false);
	return message;
}

public GuildAction clone() {
	byte[] bytes = this.toByteArray();
	GuildAction guildAction = GuildAction.parseFrom(bytes);
	return guildAction;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GuildAction.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GuildAction> schema = GuildAction.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, GuildAction.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, GuildAction.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
