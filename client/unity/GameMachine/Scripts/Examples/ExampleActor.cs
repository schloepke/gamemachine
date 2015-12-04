using UnityEngine;
using System.Collections;
using GameMachine;
using io.gamemachine.messages;

public class ExampleActor : GameMessageActor {

	// Use this for initialization
	void Start () {
        Register();
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    public override void OnGameMessage(GameMessage gameMessage) {
        Reply(gameMessage);
    }
}
