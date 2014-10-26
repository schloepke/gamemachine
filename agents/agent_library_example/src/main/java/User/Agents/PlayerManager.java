package User.Agents;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Client.Messages.ChatChannel;
import User.Globals;
import User.Messages.PlayerUpdate;
import User.Messages.Vitals;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.client.api.Cloud;
import com.game_machine.client.api.Cloud.StringResponse;
import com.game_machine.client.api.DynamicMessageUtil;
import com.game_machine.codeblocks.Codeblock;

public class PlayerManager implements Codeblock {

	private CodeblockEnv env;
	private Api api;
	private Cloud cloud;

	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			this.api = env.getApi();
			this.cloud = api.getCloud();
			System.out.println("Agent " + this.env.getAgentId() + " is awake");

			if (this.env.getReloadCount() == 0) {
				this.env.tick(60, "send_player_updates");
				this.env.tick(5000, "save_player_vitals");
			}
		}
	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof String) {
			String periodic = (String) message;
			if (periodic.equals("send_player_updates")) {
				sendVitals();
				this.env.tick(60, "send_player_updates");
			} else if (periodic.equals("save_player_vitals")) {
				saveVitals();
				this.env.tick(5000, "save_player_vitals");
			}
			// Subscribers to the game channel is a reliable method to maintain
			// a list of logged in players
		} else if (message instanceof ChatChannel) {
			ChatChannel channel = (ChatChannel) message;
			if (channel.getSubscribers() != null && channel.getSubscribers().getSubscriberIdCount() > 0) {
				List<String> subscribers = channel.getSubscribers().getSubscriberIdList();
				updateVitals(subscribers);
			}
		}
	}

	private void updateVitals(List<String> playerIds) {

		// Add new players
		for (String playerId : playerIds) {
			Vitals vitals = Globals.getVitalsFor(playerId);
			if (vitals == null) {
				vitals = new Vitals(playerId);
				Globals.setVitalsFor(vitals);
			}
		}

		// Remove players that have logged out.
		Iterator<Map.Entry<String, Vitals>> iter = Globals.getVitals().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Vitals> entry = iter.next();
			String id = entry.getKey();
			Vitals vitals = Globals.getVitalsFor(id);
			if (!playerIds.contains(id) && vitals.entityType.equals("player")) {
				iter.remove();

			}
		}
	}

	private void saveVitals() {
		System.out.println("Tracking "+Globals.getVitalsList().size()+" vitals");
		for (Vitals vitals : Globals.getVitalsList()) {
			if (vitals.updated) {
				byte[] bytes = DynamicMessageUtil.serialize(Vitals.class, vitals);
				StringResponse response = this.cloud.put(vitals.dbkey(), bytes);
				if (response.getStatus() == 204) {
					vitals.updated = false;
				} else {
					System.out.println("Error saving player data status=" + response.getStatus());
				}

			}
		}
	}

	// Send vitals to all entities that are players and are not us
	private void sendVitals() {
		PlayerUpdate playerUpdate = new PlayerUpdate();
		playerUpdate.vitals = Globals.getVitalsList();
		for (Vitals vitals : playerUpdate.vitals) {
			if (vitals.id.equals(this.api.getPlayerId()) || vitals.entityType.equals("npc")) {
				continue;
			}
			ApiMessage apiMessage = this.api.newMessage();
			apiMessage.setDynamicMessage(playerUpdate);
			apiMessage.setDestination("player/" + vitals.id);
			apiMessage.send();
		}
	}
}
