package user.agents;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.client.messages.TrackData;
import com.game_machine.client.messages.TrackData.EntityType;
import com.game_machine.codeblocks.Codeblock;

public class StressTest implements Codeblock {

	private CodeblockEnv env;
	private Api api;
	private ApiMessage apiMessage;
	private byte[] bytes;
	
	
	@Override
	public void awake(Object message) {
		this.env = (CodeblockEnv) message;
		this.api = env.getApi();
		
		apiMessage = this.api.newMessage();
		TrackData trackData = new TrackData();
		trackData.setId(api.getPlayerId());
		trackData.setEntityType(EntityType.NPC);
		trackData.setX(4f);
		trackData.setY(4f);
		trackData.setGetNeighbors(0);
		apiMessage.setTrackData(trackData);
		bytes = apiMessage.getClientMessage().toByteArray();
		
		
		if (this.env.getReloadCount() == 0) {
			this.env.tick(10, "ai");
		}
	}

	@Override
	public void run(Object arg0) throws Exception {
		for (int x = 0; x < 100; x++) {
			api.sendToNetwork(bytes);
		}
		this.env.tick(10, "ai");
		
	}

	@Override
	public void shutdown(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
