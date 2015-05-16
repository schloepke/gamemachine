package plugins.pvp_game.npc;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.NpcData;
import io.gamemachine.messages.NpcDatas;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.google.common.io.Files;

public class NpcManager extends GameMessageActor {

	public static String name = NpcManager.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	public Map<String, ActorRef> npcs = new HashMap<String, ActorRef>();
	public static Map<String,io.gamemachine.util.Vector3> positions = new ConcurrentHashMap<String,io.gamemachine.util.Vector3>();
	
	public NpcDatas npcDatas;
	
	
	@Override
	public void awake() {
		
		npcDatas = loadNpcData();
		if (npcDatas == null) {
			return;
		}
		createNpcActors();
		
		scheduleOnce(50l,"tick");
	}

	private void createNpcActors() {
		int actorCount = 0;
		for (NpcData data : npcDatas.npcData) {
			if (npcs.containsKey(data.id)) {
				continue;
			}
			
			ActorRef ref = getContext().actorOf(Props.create(NpcController.class,data), data.id);
			npcs.put(data.id, ref);
			actorCount++;
		}
		logger.warning(actorCount+" Npc actors started");
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (gameMessage.hasPathData()) {
			npcs.get(gameMessage.pathData.id).tell(gameMessage, null);
		} else if (gameMessage.hasNpcData()) {
			NpcData data = gameMessage.npcData;
			if (data.id.equals("start")) {
				npcDatas = new NpcDatas();
				logger.warning("Npc data start");
			} else if (data.id.equals("finished")) {
				saveNpcData();
				logger.warning("Npc data saved");
				createNpcActors();
			} else {
				npcDatas.addNpcData(data);
			}
		}
	}


	@Override
	public void onTick(String message) {
		scheduleOnce(50l,"tick");
		
	}


	@Override
	public void onPlayerDisconnect(String playerId) {
		// TODO Auto-generated method stub
		
	}
	
	public File npcDataPath() {
		return new File(AppConfig.getEnvRoot()+"/npc_data.bin");
	}
	
	public void saveNpcData() {
		try {
			File path = npcDataPath();
			Files.write(npcDatas.toByteArray(),path);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public NpcDatas loadNpcData() {
		try {
			File path = npcDataPath();
			byte[] bytes = Files.toByteArray(path);
			return NpcDatas.parseFrom(bytes);
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
}
