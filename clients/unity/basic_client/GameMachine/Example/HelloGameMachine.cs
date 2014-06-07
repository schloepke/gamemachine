using UnityEngine;
using System;
using System.Collections;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using Player = GameMachine.Messages.Player;

public class HelloGameMachine : MonoBehaviour
{

    // Example of how to wire up GameMachine in your application.  We use the Start function
    // here as the entry point to kick everything off, but you can initialize anyplace you like.
   
    void Start()
    {
        // Replace with your own user object if you want. 
        User user = User.Instance;
        user.SetUser("player", "pass");

        // GameMachine.App is the only GameMachine class that is a MonoBehavior
        // You want to add it as a component to just ONE game object in your game.
        GameMachine.App.AppStarted callback = OnAppStarted;
        GameMachine.App app = this.gameObject.
            AddComponent(Type.GetType("GameMachine.App")) as GameMachine.App;
        app.Run(user.username, user.password, callback);
    }
	
    void Update()
    {
    }


    // When this is called GameMachine core is loaded, the client is connected, and
    // the actor system is running.
    void OnAppStarted()
    {
        // Start our chat example
        StartChat();

        // Setup the persistence layer.  This is an optional feature, see the Persistence class
        // for how it works.
        StartPersistence();

        StartAreaOfInterest();

        Entity entity = new Entity();
        entity.id = "route_test";
        entity.json = true;
        Player player = new Player();
        player.id = User.Instance.username;
        entity.player = player;
        ActorSystem.Instance.Find("/remote/Demo/ExampleController").Tell(entity);
        
    }

    void StartAreaOfInterest()
    {
        GameObject misc = GameObject.Find("Misc");
        misc.AddComponent("AreaOfInterest");
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
