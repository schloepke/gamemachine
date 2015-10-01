
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
public final class AgentController implements Externalizable, Message<AgentController>, Schema<AgentController>{



    public static Schema<AgentController> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static AgentController getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final AgentController DEFAULT_INSTANCE = new AgentController();
    static final String defaultScope = AgentController.class.getSimpleName();

        public List<Agent> agent;
	    			public Player player;
	    
        			public String gameId;
	    
        


    public AgentController()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	    	    	model.set("agent_controller_game_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	    	    	
    	    	    	if (gameId != null) {
    	       	    	model.setString("agent_controller_game_id",gameId);
    	        		
    	}
    	    	    }
    
	public static AgentController fromModel(Model model) {
		boolean hasFields = false;
    	AgentController message = new AgentController();
    	    	    	    	    	    	    	
    	    	    	String gameIdField = model.getString("agent_controller_game_id");
    	    	
    	if (gameIdField != null) {
    		message.setGameId(gameIdField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasAgent()  {
        return agent == null ? false : true;
    }
        
		public List<Agent> getAgentList() {
		if(this.agent == null)
            this.agent = new ArrayList<Agent>();
		return agent;
	}

	public AgentController setAgentList(List<Agent> agent) {
		this.agent = agent;
		return this;
	}

	public Agent getAgent(int index)  {
        return agent == null ? null : agent.get(index);
    }

    public int getAgentCount()  {
        return agent == null ? 0 : agent.size();
    }

    public AgentController addAgent(Agent agent)  {
        if(this.agent == null)
            this.agent = new ArrayList<Agent>();
        this.agent.add(agent);
        return this;
    }
            	    	    	    	
    public AgentController removeAgentById(Agent agent)  {
    	if(this.agent == null)
           return this;
            
       	Iterator<Agent> itr = this.agent.iterator();
       	while (itr.hasNext()) {
    	Agent obj = itr.next();
    	
    	    		if (agent.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentController removeAgentByCode(Agent agent)  {
    	if(this.agent == null)
           return this;
            
       	Iterator<Agent> itr = this.agent.iterator();
       	while (itr.hasNext()) {
    	Agent obj = itr.next();
    	
    	    		if (agent.code.equals(obj.code)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentController removeAgentByClassname(Agent agent)  {
    	if(this.agent == null)
           return this;
            
       	Iterator<Agent> itr = this.agent.iterator();
       	while (itr.hasNext()) {
    	Agent obj = itr.next();
    	
    	    		if (agent.classname.equals(obj.classname)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentController removeAgentByRemove(Agent agent)  {
    	if(this.agent == null)
           return this;
            
       	Iterator<Agent> itr = this.agent.iterator();
       	while (itr.hasNext()) {
    	Agent obj = itr.next();
    	
    	    		if (agent.remove.equals(obj.remove)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentController removeAgentByCompileResult(Agent agent)  {
    	if(this.agent == null)
           return this;
            
       	Iterator<Agent> itr = this.agent.iterator();
       	while (itr.hasNext()) {
    	Agent obj = itr.next();
    	
    	    		if (agent.compileResult.equals(obj.compileResult)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentController removeAgentByConcurrency(Agent agent)  {
    	if(this.agent == null)
           return this;
            
       	Iterator<Agent> itr = this.agent.iterator();
       	while (itr.hasNext()) {
    	Agent obj = itr.next();
    	
    	    		if (agent.concurrency.equals(obj.concurrency)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
		    
    public Boolean hasPlayer()  {
        return player == null ? false : true;
    }
        
		public Player getPlayer() {
		return player;
	}
	
	public AgentController setPlayer(Player player) {
		this.player = player;
		return this;	}
	
		    
    public Boolean hasGameId()  {
        return gameId == null ? false : true;
    }
        
		public String getGameId() {
		return gameId;
	}
	
	public AgentController setGameId(String gameId) {
		this.gameId = gameId;
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

    public Schema<AgentController> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public AgentController newMessage()
    {
        return new AgentController();
    }

    public Class<AgentController> typeClass()
    {
        return AgentController.class;
    }

    public String messageName()
    {
        return AgentController.class.getSimpleName();
    }

    public String messageFullName()
    {
        return AgentController.class.getName();
    }

    public boolean isInitialized(AgentController message)
    {
        return true;
    }

    public void mergeFrom(Input input, AgentController message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.agent == null)
                        message.agent = new ArrayList<Agent>();
                                        message.agent.add(input.mergeObject(null, Agent.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.player = input.mergeObject(message.player, Player.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.gameId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, AgentController message) throws IOException
    {
    	    	
    	    	
    	    	if(message.agent != null)
        {
            for(Agent agent : message.agent)
            {
                if(agent != null) {
                   	    				output.writeObject(1, agent, Agent.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	if(message.player == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.player != null)
    		output.writeObject(2, message.player, Player.getSchema(), false);
    	    	
    	            	
    	    	if(message.gameId == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.gameId != null)
            output.writeString(3, message.gameId, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START AgentController");
    	    	if(this.agent != null) {
    		System.out.println("agent="+this.agent);
    	}
    	    	if(this.player != null) {
    		System.out.println("player="+this.player);
    	}
    	    	if(this.gameId != null) {
    		System.out.println("gameId="+this.gameId);
    	}
    	    	System.out.println("END AgentController");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "agent";
        	        	case 2: return "player";
        	        	case 3: return "gameId";
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
    	    	__fieldMap.put("agent", 1);
    	    	__fieldMap.put("player", 2);
    	    	__fieldMap.put("gameId", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = AgentController.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static AgentController parseFrom(byte[] bytes) {
	AgentController message = new AgentController();
	ProtobufIOUtil.mergeFrom(bytes, message, AgentController.getSchema());
	return message;
}

public static AgentController parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	AgentController message = new AgentController();
	JsonIOUtil.mergeFrom(bytes, message, AgentController.getSchema(), false);
	return message;
}

public AgentController clone() {
	byte[] bytes = this.toByteArray();
	AgentController agentController = AgentController.parseFrom(bytes);
	return agentController;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, AgentController.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<AgentController> schema = AgentController.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, AgentController.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, AgentController.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
