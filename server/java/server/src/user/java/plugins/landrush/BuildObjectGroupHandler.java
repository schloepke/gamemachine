package plugins.landrush;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.BuildObjectDatas;
import io.gamemachine.messages.BuildObjectGroup;
import io.gamemachine.messages.GameMessage;

public class BuildObjectGroupHandler extends GameMessageActor {

    public static String name = BuildObjectGroupHandler.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(BuildObjectGroupHandler.class);

    @Override
    public void awake() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {
        if (exactlyOnce(gameMessage)) {
            BuildObjectGroup boGroup = gameMessage.buildObjectGroup;
            reset(boGroup.group, boGroup.placedAt, boGroup.ownerId);
            setReply(gameMessage);
        }
    }

    private void reset(int group, long placedAt, String ownerId) {
        List<BuildObjectDatas> datas = BuildObjectDatas.db().where("build_object_datas_group = ?", group);
        for (BuildObjectDatas data : datas) {
            byte[] bytes = Base64.decodeBase64(data.dataText);
            BuildObject buildObject = BuildObject.parseFrom(bytes);
            buildObject.placedAt = placedAt;
            BuildObjectHandler.save(buildObject, ownerId, ownerId);
            BuildObjectHandler.setGridAndVitals(buildObject);
        }
    }
}
