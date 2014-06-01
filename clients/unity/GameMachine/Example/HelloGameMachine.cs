using UnityEngine;
using System;
using System.Collections;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using Player = GameMachine.Messages.Player;

public class HelloGameMachine : MonoBehaviour
{

    // Start GameMachine.  Calls OnAppStarted once it has loaded and initialized the network
    // and actor system.  Custom actors should be created after OnAppStarted is called.
    void Start()
    {
        User user = User.Instance;
        user.SetUser("player", "pass");

        GameMachine.App.AppStarted callback = OnAppStarted;
        GameMachine.App app = this.gameObject.
            AddComponent(Type.GetType("GameMachine.App")) as GameMachine.App;
        app.Run(user.username, user.password, callback);
    }
	
    void Update()
    {
    }


    void OnAppStarted()
    {
        StartChat();
        StartPersistence();
        Entity entity = new Entity();
        entity.id = "route_test";
        entity.json = true;
        Player player = new Player();
        player.id = User.Instance.username;
        entity.player = player;
        ActorSystem.Instance.Find("/remote/Demo/ExampleController").Tell(entity);
        
    }
    
    void StartChat()
    {
        GameObject camera = GameObject.Find("Main Camera");
        GameObject chatBox = new GameObject("ChatBox");
        chatBox.transform.parent = camera.transform;
        chatBox.AddComponent("Chat");
    }

    void StartPersistence()
    {
        GameObject camera = GameObject.Find("Main Camera");
        GameObject misc = new GameObject("Misc");
        misc.transform.parent = camera.transform;
        misc.AddComponent("Persistence");
    }

}
