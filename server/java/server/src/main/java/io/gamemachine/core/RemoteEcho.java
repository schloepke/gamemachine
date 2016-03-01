package io.gamemachine.core;

import io.gamemachine.messages.Entity;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class RemoteEcho extends UntypedActor {

    public static String name = "GameMachine::GameSystems::RemoteEcho";
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Entity) {
            Entity entity = (Entity) message;
            PlayerMessage.tell(entity, entity.getPlayer().getId());
        }

    }

}
