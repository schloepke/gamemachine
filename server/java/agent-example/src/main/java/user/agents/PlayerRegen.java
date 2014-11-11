package user.agents;

import user.Globals;
import user.messages.GameEntity;

import io.gamemachine.client.agent.CodeblockEnv;
import io.gamemachine.codeblocks.Codeblock;

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
				this.env.tick(1000, "regen");
			}
		}
	}
	
	private void regen() {
		for (GameEntity vitals : Globals.gameEntitiesList()) {
			vitals.raiseHealth(1);
		}
	}

	@Override
	public void shutdown(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
