package user.agents;

import user.Globals;
import user.messages.Attack;
import user.messages.Vitals;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.codeblocks.Codeblock;

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
	// the attack message we received and send it out to every player.  This is mostly for informational purposes for the client, so it
	// can display combat messages.  The Vitals messages sent by the PlayerVitalsManager is authoritative for player stats like health.
	private void handleAttack(Attack attack) {
		Vitals attacker = Globals.getVitalsFor(attack.attacker);
		if (attacker == null) {
			System.out.println("Invalid attacker " + attack.attacker + ".  No registered?");
			return;
		}
		
		Vitals vitals = Globals.getVitalsFor(attack.target);
		if (vitals == null) {
			return;
		}

		int damage = (int) (Math.random() * 5 + 1);
		vitals.lowerHealth(damage);
		attack.damage = damage;
		System.out.println(attack.attacker + " attacked " + attack.target + " for " + damage + " damage");

		for (String playerId : Globals.getPlayerIds()) {
			ApiMessage apiMessage = this.api.newMessage();
			apiMessage.setDynamicMessage(attack);
			apiMessage.setDestination("player/" + playerId);
			apiMessage.send();
		}
	}

}
