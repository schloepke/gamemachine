package user.agents;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.codeblocks.Codeblock;

public class NpcController implements Codeblock {

	private CodeblockEnv env;
	
	@Override
	public void run(Object message) throws Exception {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			System.out.println("Agent "+this.env.getAgentId()+" is awake");
			
			if (this.env.getReloadCount() == 0) {
				//this.env.tick(1000, "regen");
			}
		}
		
	}

	@Override
	public void awake(Object message) {
		// TODO Auto-generated method stub
		
	}

}
