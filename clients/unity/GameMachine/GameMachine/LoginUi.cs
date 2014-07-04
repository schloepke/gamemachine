using UnityEngine;
using System.Collections;
using System;
using GameMachine;
using GameMachine.Core;
using GameMachine.Chat;

namespace GameMachine
{
	public class LoginUi : MonoBehaviour, ILoginUi
	{
		
		private Rect loginWindow;
		private float windowLeft;
		private float windowHeight;
		private float windowWidth;
		private float windowTop;
		private bool hasError = false;
		private string loginError = "";
		private bool disableGui = false;
		private bool showLogin = true;
		private Login login;

		public string username = "player";
		public string password = "pass";
		public string udpHost = "127.0.0.1";
		public int udpPort = 24130;
		public int udpRegionPort = 24130;
		public bool useRegions = false;

			
		public void SetError (string error)
		{
			loginError = error;
			hasError = true;
			disableGui = false;
		}

		public void DisableLogin ()
		{
			showLogin = false;
		}

		void OnGUI ()
		{
			if (!showLogin) {
				return;
			}
			
			loginWindow = GUI.Window (0, loginWindow, WindowFunction, "Game Machine Login");
		}
		
		void WindowFunction (int windowID)
		{
			if (!showLogin) {
				return;
			}
			
			if (disableGui) {
				GUI.enabled = false;
			}
			GUI.Label (new Rect (25, 20, 100, 30), "Username");
			GUI.Label (new Rect (25, 60, 100, 30), "Password");
			GUI.Label (new Rect (25, 100, 100, 30), "Host");
			
			username = GUI.TextField (new Rect (125, 20, 200, 25), username);
			password = GUI.PasswordField (new Rect (125, 60, 200, 25), password, "*" [0], 25);
			udpHost = GUI.TextField (new Rect (125, 100, 200, 25), udpHost);
			
			if (hasError) {
				GUI.Label (new Rect (25, 150, 400, 60), "Login Failed: " + loginError);
			}
			
			
			if (GUI.Button (new Rect (200, 200, 100, 30), "Login")) {
				disableGui = true;
				login.SetParameters (udpHost, udpPort, udpRegionPort, useRegions);
				login.DoLogin (username, password);
			}
		}
		

		
		void Start ()
		{
			windowWidth = 500;
			windowHeight = 250;
			windowLeft = (Screen.width / 2) - windowWidth / 2;
			windowTop = (Screen.height / 2) - windowHeight / 2;
			loginWindow = new Rect (windowLeft, windowTop, windowWidth, windowHeight);
			login = this.gameObject.AddComponent<Login> () as Login;
			login.SetLoginUi (this);
		}
		
	}
}
