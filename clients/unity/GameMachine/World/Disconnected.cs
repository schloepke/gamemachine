using UnityEngine;
using System.Collections;
using System;
using GameMachine;
using GameMachine.Core;

namespace GameMachine.World
{
	public class Disconnected : MonoBehaviour
	{
        
		private Rect loginWindow;
		private float windowLeft;
		private float windowHeight;
		private float windowWidth;
		private float windowTop;

		private GameMachine.Core.App app;

		void OnGUI ()
		{
          
            
			loginWindow = GUI.Window (0, loginWindow, WindowFunction, "Disconnected from server");
		}
        
		void WindowFunction (int windowID)
		{

			GUI.Label (new Rect (25, 50, 400, 50), "You were disconnected from the server.  Please exit the game and restart it");

            
			if (GUI.Button (new Rect (200, 200, 100, 30), "Quit Game")) {
				Application.Quit ();
			}
		}
        
       
		void Start ()
		{
			windowWidth = 500;
			windowHeight = 250;
			windowLeft = (Screen.width / 2) - windowWidth / 2;
			windowTop = (Screen.height / 2) - windowHeight / 2;
			loginWindow = new Rect (windowLeft, windowTop, windowWidth, windowHeight);
		}
        
	}
}
