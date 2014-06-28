using UnityEngine;
using System.Collections;
using System;
using GameMachine;

namespace GameMachine.World
{
		public class Login : MonoBehaviour
		{

				private Rect loginWindow;
				private float windowLeft;
				private float windowHeight;
				private float windowWidth;
				private float windowTop;
				public string username = "player";
				public string password = "pass";
				private bool hasError = false;
				private string loginError = "";
				private bool disableGui = false;
				private GameMachine.App app;
				private bool showLogin = true;

				public static GameMachine.RegionClient regionClient;
				private string authUri;
				public string udpHost = "127.0.0.1";
				public int udpPort = 24130;
				public int udpRegionPort = 24130;
				public bool useRegions = false;

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

								User user = User.Instance;
								user.SetUser (username.ToString (), password.ToString ());
								GameMachine.Authentication.Success success = OnAuthenticationSuccess;
								GameMachine.Authentication.Error error = OnAuthenticationError;

								authUri = "http://" + udpHost + ":3000/auth";
								app = this.gameObject.AddComponent (Type.GetType ("GameMachine.App")) as GameMachine.App;
								app.Login (authUri, user.username, user.password, success, error);
						}
				}

				void OnAuthenticationError (string error)
				{
						Logger.Debug ("Authentication Failed: " + error);
						hasError = true;
						loginError = error.Replace (System.Environment.NewLine, "");
						disableGui = false;
				}

				public void OnAuthenticationSuccess (string authtoken)
				{
						GameMachine.App.AppStarted callback = OnAppStarted;
						app.OnAppStarted (callback);

						GameMachine.App.ConnectionTimeout connectionCallback = OnConnectionTimeout;
						app.OnConnectionTimeout (connectionCallback);

						app.Run (udpHost, udpPort, User.Instance.username, authtoken);
						StartRegionClient (authtoken);

				}

				public void OnConnectionTimeout ()
				{
						Application.LoadLevel ("world_disconnected");
				}

				public void OnAppStarted ()
				{
						JsonModel.Register (typeof(Attack), "Example::Models::Attack", "Example/CombatController");
						JsonModel.Register (typeof(Vitals), "Example::Models::Vitals");
						JsonModel.Register (typeof(CombatUpdate), "Example::Models::CombatUpdate");
						Invoke ("WaitForPlayerVitals", 0.05f);
				}

				public void WaitForPlayerVitals ()
				{
						if (GameMachine.World.Player.vitals == null) {
								Invoke ("WaitForPlayerVitals", 0.05f);
						} else {
								OnPlayerVitalsReceived ();
						}
				}

				public void OnPlayerVitalsReceived ()
				{
						showLogin = false;
						Destroy (GameObject.Find ("Main Camera"));
						Application.LoadLevelAdditive ("world_main");
				}

				void StartRegionClient (string authtoken)
				{
						regionClient = this.gameObject.AddComponent (Type.GetType ("GameMachine.RegionClient")) as GameMachine.RegionClient;
						GameMachine.RegionClient.ConnectionTimeout connectionCallback = OnRegionConnectionTimeout;
						regionClient.OnConnectionTimeout (connectionCallback);

						RegionClient.RegionClientStarted callback = OnRegionClientStarted;
						regionClient.OnRegionClientStarted (callback);

						regionClient.Init (udpRegionPort, User.Instance.username, authtoken);
				}
        
				public void OnRegionConnectionTimeout ()
				{
						Logger.Debug ("Region Connection timed out");
				}
        
				void OnRegionClientStarted ()
				{
						Logger.Debug ("OnRegionClientStarted called");
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
