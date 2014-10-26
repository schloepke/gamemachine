package User.Agents;

import Client.Messages.DynamicMessage;
import Client.Messages.Entity;
import Client.Messages.GameMessage;
import User.Messages.Attack;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.DynamicMessageUtil;
import com.game_machine.codeblocks.Codeblock;

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
				this.env.sendToAgent("CombatManager", entity.getNeighbors());
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

}
