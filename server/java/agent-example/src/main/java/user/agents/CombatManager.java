package user.agents;

import user.Globals;
import user.messages.Attack;
import user.messages.GameEntity;

import io.gamemachine.client.agent.CodeblockEnv;
import io.gamemachine.client.api.Api;
import io.gamemachine.client.api.ApiMessage;
import io.gamemachine.codeblocks.Codeblock;

public class CombatManager implements Codeblock {

	private CodeblockEnv env;
	private Api api;
	
	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			this.api = env.getApi();
			System.out.println("Agent " + this.env.getAgentId() + " is awake");
		}
	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof Attack) {
			handleAttack((Attack) message);
		}
	}

	// Example of how to handle combat.  We receive attack messages from clients, do some random damage, and then set the damage done on
	// the attack message we received and send it out to every player.  
	private void handleAttack(Attack attack) {
		GameEntity attacker = Globals.gameEntityFor(attack.attacker);
		if (attacker == null) {
			System.out.println("Invalid attacker " + attack.attacker + ".  No registered?");
			return;
		}
		
		GameEntity gameEntity = Globals.gameEntityFor(attack.target);
		if (gameEntity == null) {
			return;
		}

		int damage = (int) (Math.random() * 5 + 1);
		gameEntity.lowerHealth(damage);
		attack.damage = damage;
		System.out.println(attack.attacker + " attacked " + attack.target + " for " + damage + " damage");

		for (String playerId : Globals.getPlayerIds()) {
			ApiMessage apiMessage = this.api.newMessage();
			apiMessage.setDynamicMessage(attack);
			apiMessage.setDestination("player/" + playerId);
			apiMessage.send();
		}
	}

	@Override
	public void shutdown(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
