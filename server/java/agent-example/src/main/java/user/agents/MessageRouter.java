package user.agents;

/*
 * MessageRouter is a special agent that will receive all messages from the server that are not specifically addressed to an agent.  
 * 
 * The name you assign to this class in the UI must also be MessageRouter.
 * 
 */

import user.messages.Attack;
import io.gamemachine.client.messages.DynamicMessage;
import io.gamemachine.client.messages.Entity;
import io.gamemachine.client.messages.GameMessage;

import io.gamemachine.client.agent.CodeblockEnv;
import io.gamemachine.client.api.DynamicMessageUtil;
import io.gamemachine.codeblocks.Codeblock;

public class MessageRouter implements Codeblock {

	private CodeblockEnv env;
	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			System.out.println("Agent " + this.env.getAgentId() + " is awake");
		}
	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof GameMessage) {
			GameMessage gameMessage = (GameMessage) message;
			DynamicMessage dynamicMessage = gameMessage.getDynamicMessage();
			
			if (dynamicMessage != null) {
				if (dynamicMessage.getType().equals("Attack")) {
					Attack attack = (Attack) DynamicMessageUtil.fromDynamicMessage(dynamicMessage);
					this.env.sendToAgent("CombatManager", attack);
				}
			}
			
		} else if (message instanceof Entity) {
			Entity entity = (Entity)message;
			
			// Always comes as only message in entity
			if (entity.getNeighbors() != null) {
				this.env.sendToAgent("TrackingManager", entity.getNeighbors());
				return;
			}
						
			// Can be multiple chat related messages in a single entity
			if (entity.getChatChannels() != null) {
				this.env.sendToAgent("ChatManager", entity.getChatChannels());
			}
			if (entity.getChatMessage() != null) {
				this.env.sendToAgent("ChatManager", entity.getChatMessage());
			}
		}
	}

	@Override
	public void shutdown(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
