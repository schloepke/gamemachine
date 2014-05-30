using UnityEngine;
using System;
using System.Collections;
using GameMachine;

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

    }

    void StartChat()
    {
        GameObject camera = GameObject.Find("Main Camera");
        GameObject chatBox = new GameObject("ChatBox");
        chatBox.transform.parent = camera.transform;
        chatBox.AddComponent("Chat");
    }
}
