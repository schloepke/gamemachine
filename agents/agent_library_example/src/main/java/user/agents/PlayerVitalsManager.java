package user.agents;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import user.Globals;
import user.messages.PlayerUpdate;
import user.messages.Vitals;
import Client.Messages.ChatChannel;
import Client.Messages.TrackData.EntityType;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.client.api.Cloud;
import com.game_machine.codeblocks.Codeblock;

public class PlayerVitalsManager implements Codeblock {

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
		} else if (message instanceof ChatChannel) {
			ChatChannel channel = (ChatChannel) message;
			if (channel.hasSubscribers()) {
				updateVitals(channel.getSubscribers().getSubscriberIdList());
			}
		}
	}

	/*
	 *  We use the subscribers list from the chat channel we create for our game to keep an updated list of logged in players.
	 *  Here we ensure that we have a Vitals instance for every subscriber in our global vitals map.  If the map has a player that is not
	 *  in the current subscriber list, we consider them logged out and remove them.
	 */
	private void updateVitals(List<String> playerIds) {

		// Add new players
		for (String playerId : playerIds) {
			// Don't add ourselves
			if (playerId.equals(api.getPlayerId())) {
				continue;
			}
			Vitals vitals = Globals.getVitalsFor(playerId);
			if (vitals == null) {
				vitals = this.cloud.find(playerId, Vitals.class);
				if (vitals == null) {
					vitals = new Vitals(playerId);
				}
				vitals.updated = true;
				Globals.setVitalsFor(vitals);
			}
		}

		// Remove players that have logged out.
		Iterator<Map.Entry<String, Vitals>> iter = Globals.getVitals().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Vitals> entry = iter.next();
			String id = entry.getKey();
			Vitals vitals = Globals.getVitalsFor(id);
			if (!playerIds.contains(id) && vitals.entityType == EntityType.PLAYER) {
				iter.remove();
				this.cloud.delete(vitals.id, Vitals.class);
			}
		}
	}

	/*
	 *  Actually save the vitals to the cloud periodically.
	 */
	private void saveVitals() {
		System.out.println("Tracking " + Globals.getVitalsList().size() + " vitals");
		for (Vitals vitals : Globals.getVitalsList()) {
			if (vitals.updated) {
				if (this.cloud.save(vitals.id, Vitals.class, vitals)) {
					vitals.updated = false;
				}
			}
		}
	}

	/*
	 *  Update clients with the vitals of other players
	 */
	private void sendVitals() {
		PlayerUpdate playerUpdate = new PlayerUpdate();
		playerUpdate.vitals = Globals.getVitalsList();
		for (String playerId : Globals.getPlayerIds()) {
			Vitals vitals = Globals.getVitalsFor(playerId);
			ApiMessage apiMessage = this.api.newMessage();
			apiMessage.setDynamicMessage(playerUpdate);
			apiMessage.setDestination("player/" + vitals.id);
			apiMessage.send();
		}
	}
}
