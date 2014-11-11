using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using DynamicMessages;
using DynamicMessage = io.gamemachine.messages.DynamicMessage;

public class DynamicChatMessageDemo : MonoBehaviour {

    private Messenger messenger;
    private Rect loginWindow;
    private float windowLeft;
    private float windowHeight;
    private float windowWidth;
    private float windowTop;
    public string moveTo = "";
    public string action = "";
    public string channelName = "mygame";
    public PlayerMove playerMove;

	void Start () {
        windowWidth = 500;
        windowHeight = 250;
        windowLeft = (Screen.width / 2) - windowWidth / 2;
        windowTop = (Screen.height / 2) - windowHeight / 2;
        loginWindow = new Rect(windowLeft, windowTop, windowWidth, windowHeight);

        messenger = ActorSystem.Instance.Find("Messenger") as Messenger;
        messenger.JoinChannel(channelName);
        Messenger.DynamicMessageReceived dynamicMessageCallback = DynamicMessageReceived;
        messenger.OnDynamicMessageReceived(channelName, dynamicMessageCallback);
	}
	
	void Update () {
	
	}

    void OnGUI()
    {
       
        loginWindow = GUI.Window(0, loginWindow, WindowFunction, "Dynamic messaging");
    }

    void WindowFunction(int windowID)
    {
       
        GUI.Label(new Rect(25, 20, 100, 30), "Move to");
        GUI.Label(new Rect(25, 60, 100, 30), "Take action");

        moveTo = GUI.TextField(new Rect(125, 20, 200, 25), moveTo);
        action = GUI.TextField(new Rect(125, 60, 200, 25), action);

        if (playerMove != null)
        {
            GUI.Label(new Rect(25, 150, 400, 60), "Last move: Player "+playerMove.playerId+" move to "+playerMove.to+" with action "+  playerMove.action);
        }


        if (GUI.Button(new Rect(200, 200, 100, 30), "Submit move"))
        {
            PlayerMove move = new PlayerMove();
            move.to = moveTo;
            move.action = action;
            move.playerId = User.Instance.username;
            messenger.SendDynamicMessage(User.Instance.username, channelName, move, "group");
        }
    }


    public void DynamicMessageReceived(DynamicMessage message)
    {
            playerMove = (PlayerMove)DynamicMessageUtil.FromDynamicMessage(message);
    }
}
