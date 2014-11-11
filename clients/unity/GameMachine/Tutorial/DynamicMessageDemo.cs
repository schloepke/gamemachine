using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using DynamicMessages;
using DynamicMessage = io.gamemachine.messages.DynamicMessage;

public class DynamicMessageDemo : MonoBehaviour, GameMachine.Core.Behavior
{

    private Messenger messenger;
    private Rect loginWindow;
    private float windowLeft;
    private float windowHeight;
    private float windowWidth;
    private float windowTop;
    public string playerId = "";
    public string agentController = "controller4";
    public Attack lastAttack;
    private GameMessageHandler messageHandler;

    void Start ()
    {
        windowWidth = 500;
        windowHeight = 250;
        windowLeft = (Screen.width / 2) - windowWidth / 2;
        windowTop = (Screen.height / 2) - windowHeight / 2;
        loginWindow = new Rect (windowLeft, windowTop, windowWidth, windowHeight);
        messageHandler = GameMessageHandler.Instance;
        messageHandler.Register (this, "DynamicMessage");
    }

    void Update ()
    {

    }

    void OnGUI ()
    {

        loginWindow = GUI.Window (0, loginWindow, WindowFunction, "Dynamic messaging");
    }

    void WindowFunction (int windowID)
    {

        GUI.Label (new Rect (25, 20, 100, 30), "Player to attack");

        playerId = GUI.TextField (new Rect (125, 20, 200, 25), playerId);

        if (lastAttack != null) {
            string message = "Attack: player " + lastAttack.attacker + " attacked " + lastAttack.target + " with attack " + lastAttack.attack + " for " + lastAttack.damage + " damage";
            GUI.Label (new Rect (25, 150, 400, 60), message);
        }


        if (GUI.Button (new Rect (200, 200, 100, 30), "Attack player")) {
            Attack attack = new Attack ();
            attack.attacker = User.Instance.username;

            attack.target = playerId;
            attack.attack = "fist";
            messageHandler.SendDynamicMessage (attack, "player/" + agentController);
        }
    }

    public void OnMessage (object message)
    {

        if (message is Attack) {
            lastAttack = (Attack)message;
        }
    }


}
