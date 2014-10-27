package com.game_machine.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.Hashring;
import com.game_machine.core.PlayerCommands;
import com.game_machine.core.PlayerService;

import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.Entity;
import GameMachine.Messages.GameMessage;
import GameMachine.Messages.Player;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GameHandler extends UntypedActor {

	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private Map<String,ActorSelection> destinations = new HashMap<String,ActorSelection>();
	public static String name = "game_handler";
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ClientMessage) {
			ClientMessage clientMessage = (ClientMessage) message;
			if (clientMessage.getEntityCount() >= 1) {
				dispatchEntities(clientMessage.getEntityList());
			}
		} else {
			unhandled(message);
		}
	}

	private void dispatchEntities(List<Entity> entities) {
		for (Entity entity : entities) {
			if (entity.hasDestination()) {
				dispatchByDestination(entity);
				return;
			}

			if (entity.hasGameMessages()) {
				dispatchByGameMessage(entity);
			}

			dispatchByComponent(entity);
		}
	}

	

	private void dispatchByComponent(Entity entity) {
		ActorSelection sel;
		if (entity.hasEchoTest()) {
			sel = ActorUtil.getSelectionByName("GameMachine::GameSystems::RemoteEcho");
			sel.tell(entity,getSelf());
		}
		if (entity.hasChatInvite() || entity.hasChatMessage() || entity.hasChatStatus() || entity.hasLeaveChat() || entity.hasJoinChat()) {
			sel = ActorUtil.getSelectionByName("GameMachine::GameSystems::ChatManager");
			sel.tell(entity,getSelf());
		}
	}

	final class PlayerDestination {
		public final Player player;
		public final String agent;

		public PlayerDestination(Player player, String agent) {
			this.player = player;
			this.agent = agent;
		}
	}

	private PlayerDestination destinationToPlayer(String destination, String senderId) {
		if (destination == null || !destination.startsWith("player/")) {
			return null;
		}
		PlayerService playerService = PlayerService.getInstance();
		String[] parts = destination.split("/");
		Player recipientPlayer = playerService.find(parts[1]);
		if (recipientPlayer != null) {
			String playerGameId = playerService.getGameId(senderId);
			if (recipientPlayer.getGameId().equals(playerGameId)) {
				PlayerDestination pd = new PlayerDestination(recipientPlayer, parts[2]);
				return pd;
			} else {
				logger.info("Destination player " + recipientPlayer.getId() + " game id does not match sender ");
			}
		}
		return null;
	}

	private void dispatchByDestination(Entity entity) {
		PlayerDestination pd = destinationToPlayer(entity.getDestination(), entity.getPlayer().getId());
		if (pd != null) {
			Player recipientPlayer = new Player();
			recipientPlayer.setId(pd.player.getId());
			entity.setPlayer(recipientPlayer).setSenderId(entity.getPlayer().getId());
			PlayerCommands.sendToPlayer(entity, recipientPlayer.getId());
			logger.debug("Message sent to player destination " + recipientPlayer.getId());
			return;
		}

		String destination = entity.getDestination();
		if (!destination.equals("GameMachine/GameSystems/RemoteEcho")
				&& !destination.equals("GameMachine/GameSystems/Devnull")
				&& !destination.equals("GameMachine/GameSystems/TeamManager")
				&& !destination.equals("GameMachine/GameSystems/RegionService")) {
			logger.info("Bad destination "+destination);
			return;
		}
		
		String path = entity.getDestination().replaceAll("/", "::");
		if (!destinations.containsKey(destination)) {
			ActorSelection sel = ActorUtil.getSelectionByName(path);
			destinations.put(path,sel);
		}
		entity.setDestination(null);
		ActorSelection dest = destinations.get(path);
		dest.tell(entity, getSelf());
	}
	
	private void dispatchByGameMessage(Entity entity) {
		String destination = null;
		for (GameMessage gameMessage : entity.getGameMessages().getGameMessageList()) {
			if (gameMessage.hasDestinationId()) {
				destination = Integer.toString(gameMessage.getDestinationId());
			} else if (gameMessage.hasDestination()) {
				destination = gameMessage.getDestination();
				PlayerDestination pd = destinationToPlayer(entity.getDestination(), entity.getPlayer().getId());
				if (pd != null) {
					Player recipientPlayer = pd.player;
					if (pd.agent != null) {
						gameMessage.setAgentId(pd.agent);
					}
					PlayerCommands.sendGameMessage(gameMessage,recipientPlayer.getId());
					continue;
				}
			} else {
				continue;
			}
			
			if (GameMessageRoute.routes.containsKey(destination)) {
				GameMessageRoute route = GameMessageRoute.routes.get(destination);
				gameMessage.setPlayerId(entity.getPlayer().getId());
				ActorSelection sel;
				if (route.isDistributed()) {
					logger.debug("GameMessage route to="+route.getTo()+" player="+entity.getPlayer().getId());
					sel = ActorUtil.findDistributed(route.getTo(),entity.getPlayer().getId());
				} else {
					sel = ActorUtil.getSelectionByName(route.getTo());
				}
				sel.tell(gameMessage, getSelf());
			}
		}
	}
	


}
