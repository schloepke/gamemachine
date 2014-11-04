package user.agents;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import user.Globals;
import user.messages.GameEntity;
import Client.Messages.ChatChannel;
import Client.Messages.TrackData.EntityType;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.client.api.Cloud;
import com.game_machine.codeblocks.Codeblock;

public class GameEntitiesManager implements Codeblock {

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
				this.env.tick(60, "update_client_game_entities");
				this.env.tick(5000, "save_game_entities");
			}
		}
	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof String) {
			String periodic = (String) message;
			if (periodic.equals("update_client_game_entities")) {
				updateClientGameEntities();
			} else if (periodic.equals("save_game_entities")) {
				saveGameEntities();
				this.env.tick(5000, "save_game_entities");
			}
		} else if (message instanceof ChatChannel) {
			ChatChannel channel = (ChatChannel) message;
			if (channel.hasSubscribers()) {
				updateGameEntities(channel.getSubscribers().getSubscriberIdList());
			}
		}
	}

	/*
	 *  We use the subscribers list from the chat channel we create for our game to keep an updated list of logged in players.
	 *  Here we ensure that we have a Vitals instance for every subscriber in our global vitals map.  If the map has a player that is not
	 *  in the current subscriber list, we consider them logged out and remove them.
	 */
	private void updateGameEntities(List<String> playerIds) {

		// Add new players
		for (String playerId : playerIds) {
			// Don't add ourselves
			if (playerId.equals(api.getPlayerId())) {
				continue;
			}
			GameEntity vitals = Globals.gameEntityFor(playerId);
			if (vitals == null) {
				vitals = this.cloud.find(playerId, GameEntity.class);
				if (vitals == null) {
					vitals = new GameEntity(playerId);
				}
				vitals.updated = true;
				Globals.setGameEntity(vitals);
			}
		}

		// Remove players that have logged out.
		Iterator<Map.Entry<String, GameEntity>> iter = Globals.getVitals().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, GameEntity> entry = iter.next();
			String id = entry.getKey();
			GameEntity vitals = Globals.gameEntityFor(id);
			if (!playerIds.contains(id) && vitals.entityType == EntityType.PLAYER) {
				iter.remove();
				this.cloud.delete(vitals.id, GameEntity.class);
			}
		}
	}

	/*
	 *  If a game entity has changed (updated=true), save it and also update the trackdata dynamic message with the current ClientGameEntity
	 */
	private void saveGameEntities() {
		System.out.println("Tracking " + Globals.gameEntitiesList().size() + " game entities");
		for (GameEntity gameEntity : Globals.gameEntitiesList()) {
			if (gameEntity.updated) {
				
				// Send to server so it updates the trackdata with the latest info
				ApiMessage apiMessage = this.api.newMessage();
				apiMessage.setTrackDataUpdate(gameEntity.id,gameEntity.toClientGameEntity());
				apiMessage.send();
				
				// save to the cloud
				if (this.cloud.save(gameEntity.id, GameEntity.class, gameEntity)) {
					gameEntity.updated = false;
				}
			}
		}
	}

	/*
	 *  At startup initialize the trackdatas on the server for all entities we know about
	 *  
	 */
	private void updateClientGameEntities() {
		for (GameEntity gameEntity : Globals.gameEntitiesList()) {
			ApiMessage apiMessage = this.api.newMessage();
			apiMessage.setTrackDataUpdate(gameEntity.id,gameEntity.toClientGameEntity());
			apiMessage.send();
		}
		
	}

	@Override
	public void shutdown(Object arg0) throws Exception {
		for (GameEntity vitals : Globals.gameEntitiesList()) {
			this.cloud.delete(vitals.id, GameEntity.class);
		}
		Globals.clearVitals();
	}
}
