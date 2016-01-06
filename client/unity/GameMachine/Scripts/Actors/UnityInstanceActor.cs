using System;
using GameMachine;
using io.gamemachine.messages;
using GameMachine.Core;

public class UnityInstanceActor : GameMessageActor {

    //private UnityInstanceStatus.State state = UnityInstanceStatus.State.Idle;
    //private UnityInstanceStatus.State requestedState = UnityInstanceStatus.State.None;

    void Start() {
        Register();
    }

   
    void Update() {

    }

    public override void OnGameMessage(GameMessage gameMessage) {
        //requestedState = gameMessage.unityInstanceStatus.requestedState;
        gameMessage.unityInstanceStatus.state = gameMessage.unityInstanceStatus.requestedState;

        Reply(gameMessage);
    }

}
