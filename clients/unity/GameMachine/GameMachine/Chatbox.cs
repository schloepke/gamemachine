using UnityEngine;
using System;
using System.Collections;
using  System.Collections.Generic;
using  System.Text.RegularExpressions;
using GameMachine;

public class Chatbox : MonoBehaviour
{


    public GUISkin skin;
    static bool  showChat = true;
    private string inputField = "";
    private Vector2 scrollPosition;
    private int width = 400;
    private int height = 180;
    private string playerName;
    private float lastUnfocus = 0.0f;
    private Rect window;
    private float lastEntry = 0.0f;
    private List<string> chatEntries = new List<string>();


    void  Awake()
    {
        window = new Rect(0, Screen.height - height, width, height);
        window.y = Screen.height - height;
        lastEntry = Time.time;
    }

    void  CloseChatWindow()
    {
        showChat = false;
        inputField = "";
        chatEntries.Clear();
    }

    void  ShowChatWindow()
    {
        showChat = true;
        inputField = "";
        chatEntries.Clear();
    }

    void  OnGUI()
    {
        if (!showChat)
        {
            return;
        }
	
        GUI.skin = skin;
        GUI.backgroundColor = new Color(161, 176, 131);
        GUI.backgroundColor = Color.black;
        GUI.contentColor = Color.green;
        window = GUI.Window(5, window, GlobalChatWindow, "Chatbox");
        GUI.contentColor = Color.white;
    }

    void  GlobalChatWindow(int id)
    {
        GUILayout.BeginVertical();
        GUILayout.EndVertical();
        scrollPosition = GUILayout.BeginScrollView(scrollPosition);
  
        foreach (string entry in chatEntries)
        {
            string text;
            GUI.contentColor = Color.white;
            List<string> words = new List<string>(entry.Split("|".ToCharArray(), StringSplitOptions.RemoveEmptyEntries));
            if (words.Count == 1)
            {
                text = words [0];
            } else
            {
                text = words [1];
                if (words [0] == "yellow")
                {
                    GUI.contentColor = Color.yellow;
                } else  if (words [0] == "green")
                {
                    GUI.contentColor = Color.green;
                } else  if (words [0] == "red")
                {
                    GUI.contentColor = Color.red;
                } else  if (words [0] == "magenta")
                {
                    GUI.contentColor = Color.magenta;
                }
            }
            GUILayout.BeginHorizontal();
            GUILayout.Label(text);
            GUILayout.EndHorizontal();
        }
        GUI.contentColor = Color.white;
        GUILayout.EndScrollView();
    

        if (Event.current.type == EventType.keyDown && Event.current.character.ToString() == "\n" && inputField.Length > 0)
        {
            SendMessage(inputField);
        } else if (Event.current.type == EventType.keyDown && Event.current.character.ToString() == "\n" && inputField.Length == 0)
        {
            inputField = ""; 
            GUI.UnfocusWindow();
            lastUnfocus = Time.time;
        }

        GUI.SetNextControlName("Chat input field");
        inputField = GUILayout.TextField(inputField);
        GUI.DragWindow();
    }

    void  SendMessage(string msg)
    {
        msg = msg.Replace("\n", "");
        msg = msg.Replace("\n", "");
        inputField = "";
        lastUnfocus = Time.time;
        this.gameObject.GetComponent<GameMachine.Chat>().SendChatMessage(msg);
    }

    public void  AddMessage(string color, string msg)
    {
        msg = color + "|" + msg;
        chatEntries.Add(msg);
        lastEntry = Time.time;

        if (chatEntries.Count > 40)
        {
            chatEntries.RemoveAt(0);
        }
        scrollPosition.y = 1000000; 
    }


}