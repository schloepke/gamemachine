package User.Agents;

import Client.Messages.ChatChannel;
import Client.Messages.ChatChannels;
import Client.Messages.ChatMessage;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.codeblocks.Codeblock;

public class ChatManager implements Codeblock {

	private CodeblockEnv env;
	private Api api;

	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			this.api = env.getApi();
			System.out.println("Agent " + this.env.getAgentId() + " is awake");

			subscribeToChannels();
			
			if (this.env.getReloadCount() == 0) {
				this.env.tick(5000, "tick");
			}
		}
	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof String) {
			String periodic = (String) message;
			if (periodic.equals("tick")) {
				
				// Spam the lobby
				ApiMessage apiMessage = this.api.newMessage();
				apiMessage.setChatMessage("group", "Lobby", "La la la");
				apiMessage.send();
				
				// Request a subscriber list by sending a ChatStatus
				apiMessage = this.api.newMessage();
				apiMessage.setChatStatus();
				apiMessage.send();
				
				this.env.tick(5000, "tick");
			}
		} else if (message instanceof ChatChannels) {
			ChatChannels channels = (ChatChannels)message;
			for (ChatChannel channel : channels.getChatChannelList()) {
				if (channel.getName().equals(this.api.getGameid())) {
					this.env.sendToAgent("PlayerManager", channel);
				}
			}
		} else if (message instanceof ChatMessage) {
			ChatMessage chatMessage = (ChatMessage)message;
			//System.out.println("ChatManager got message "+chatMessage.getMessage());
		}
		
	}
	
	// Subscribe to Lobby for fun, and to a channel with our game id for player management
	private void subscribeToChannels() {
		ApiMessage apiMessage = this.api.newMessage();
		apiMessage.setJoinPublicChat(this.api.getGameid());
		apiMessage.send();
		
		apiMessage = this.api.newMessage();
		apiMessage.setJoinPublicChat("Lobby");
		apiMessage.send();
	}
}
