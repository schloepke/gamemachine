package User.Agents;

import User.Globals;
import User.Messages.Vitals;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.codeblocks.Codeblock;

public class PlayerRegen implements Codeblock {

	private CodeblockEnv env;

	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			System.out.println("Agent "+this.env.getAgentId()+" is awake");
			
			if (this.env.getReloadCount() == 0) {
				this.env.tick(1000, "regen");
			}
		}
	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof String) {
			String periodic = (String)message;
			if (periodic.equals("regen")) {
				regen();
			}
		}
	}
	
	private void regen() {
		for (Vitals vitals : Globals.getVitalsList()) {
			vitals.raiseHealth(1);
		}
	}
}
