package user.agents;

import java.util.List;

import user.Globals;
import user.messages.GameEntity;
import com.game_machine.client.messages.Neighbors;
import com.game_machine.client.messages.TrackData;
import com.game_machine.client.messages.TrackData.EntityType;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.codeblocks.Codeblock;

public class TrackingManager implements Codeblock {

	private CodeblockEnv env;
	private Api api;

	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			this.api = env.getApi();
			System.out.println("Agent " + this.env.getAgentId() + " is awake");

			if (this.env.getReloadCount() == 0) {
				this.env.tick(50, "get_neighbors");
			}
		}
	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof String) {
			String periodic = (String) message;
			if (periodic.equals("get_neighbors")) {
				getNeighbors();
				this.env.tick(50, "get_neighbors");
			}
		} else if (message instanceof Neighbors) {
			Neighbors neighbors = (Neighbors) message;
			updatePositions(neighbors.getTrackDataList());
		}
	}

	private void updatePositions(List<TrackData> trackDatas) {
		for (TrackData trackData : trackDatas) {
			if (trackData.getEntityType() == TrackData.EntityType.PLAYER) {
				GameEntity vitals = Globals.gameEntityFor(trackData.getId());
				if (vitals != null) {
					Globals.grid.set(trackData);
					Globals.aoeGrid.set(trackData);
				}
			}
		}
	}

	private void getNeighbors() {

		// Note: The entity type of ALL is specific to controllers (players
		// with a role of agent_controller). It tells the server to send us
		// the entire grid not just entities within range. Normal clients do not
		// have access to this. Coordinates of -1 tell the server we are just
		// interested in getting neighbors, and not to
		// store our coords in the grid.
		ApiMessage apiMessage = this.api.newMessage();
		TrackData trackData = new TrackData();
		trackData.setId(this.api.getPlayerId());
		trackData.setEntityType(EntityType.PLAYER);
		trackData.setX(-1f);
		trackData.setY(-1f);
		trackData.setNeighborEntityType(EntityType.ALL);
		trackData.setGetNeighbors(1);
		apiMessage.setTrackData(trackData);
		apiMessage.send();

	}

	@Override
	public void shutdown(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
