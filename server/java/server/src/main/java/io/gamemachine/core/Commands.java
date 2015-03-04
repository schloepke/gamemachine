package io.gamemachine.core;

import java.util.List;

import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.ClientManagerRegister;
import io.gamemachine.messages.Entity;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.RemovePlayerItem;
import akka.actor.ActorSelection;

public class Commands {

	public static void clientManagerRegister(String name) {
		ClientManagerRegister register = new ClientManagerRegister();
		register.setRegisterType("actor").setName(name);
		register.setEvents("");
		Entity entity = new Entity();
		entity.setClientManagerRegister(register);
		ActorSelection sel = ActorUtil.getSelectionByName("GameMachine::ClientManager");
		sel.tell(entity, null);
	}
	
	public static void addPlayerItem(String playerId, String id, int quantity) {
		PlayerItem playerItem = new PlayerItem();
		playerItem.quantity = quantity;
		playerItem.id = id;

		AddPlayerItem addPlayerItem = new AddPlayerItem();
		addPlayerItem.playerItem = playerItem;
		GameMessage msg = new GameMessage();
		msg.playerId = playerId;
		msg.addPlayerItem = addPlayerItem;
		msg.authtoken = AuthToken.setToken();
		PlayerCommands.SendLocal(msg, "item_manager");
	}
	
	public static void removePlayerItem(String playerId, String id, int quantity) {
		RemovePlayerItem removePlayerItem = new RemovePlayerItem();
		removePlayerItem.id = id;
		removePlayerItem.quantity = quantity;
		GameMessage msg = new GameMessage();
		msg.playerId = playerId;
		msg.removePlayerItem = removePlayerItem;
		msg.authtoken = AuthToken.setToken();
		PlayerCommands.SendLocal(msg, "item_manager");
	}
	
	public static List<io.gamemachine.messages.Character> getCharacters() {
		return io.gamemachine.messages.Character.db().findAll();
	}
	
	public static void seedCharacterItem(String id, int quantity) {
		for (io.gamemachine.messages.Character character : getCharacters()) {
			Player player = PlayerService.getInstance().find(character.playerId);
			if (player.role.equals("player")) {
				removePlayerItem(character.playerId,id,quantity);
				addPlayerItem(character.playerId,id,quantity);
			}
		}
	}

}
