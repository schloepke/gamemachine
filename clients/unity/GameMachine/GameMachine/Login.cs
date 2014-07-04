using UnityEngine;
using System.Collections;
using System;
using GameMachine;
using GameMachine.Core;
using GameMachine.Chat;

namespace GameMachine
{
	public class Login : MonoBehaviour
	{
		private GameMachine.Core.App app;
		public static GameMachine.Core.RegionClient regionClient;
		private string authUri;
		private int udpPort = 24130;
		private string udpHost;
		private int udpRegionPort = 24130;
		public bool useRegions = false;
		private ILoginUi loginUi;
		private GameMachineApp userApp;

		public void SetGameMachineApp (GameMachineApp userApp)
		{
			this.userApp = userApp;
		}

		public void SetLoginUi (ILoginUi loginUi)
		{
			this.loginUi = loginUi;
		}

		public void SetParameters (string udpHost, int udpPort, int udpRegionPort, bool useRegions)
		{
			this.udpHost = udpHost;
			this.udpPort = udpPort;
			this.udpRegionPort = udpRegionPort;
			this.useRegions = useRegions;
		}

		public void DoLogin (string username, string password)
		{
			User user = User.Instance;
			user.SetUser (username.ToString (), password.ToString ());
			GameMachine.Core.Authentication.Success success = OnAuthenticationSuccess;
			GameMachine.Core.Authentication.Error error = OnAuthenticationError;
			
			authUri = "http://" + udpHost + ":3000/auth";
			app = this.gameObject.AddComponent (Type.GetType ("GameMachine.Core.App")) as GameMachine.Core.App;
			app.Login (authUri, user.username, user.password, success, error);
		}

		void OnAuthenticationError (string error)
		{
			Logger.Debug ("Authentication Failed: " + error);
			loginUi.SetError (error.Replace (System.Environment.NewLine, ""));
		}


		void OnAuthenticationSuccess (string authtoken)
		{
			GameMachine.Core.App.AppStarted callback = OnAppStarted;
			app.OnAppStarted (callback);
			
			GameMachine.Core.App.ConnectionTimeout connectionCallback = OnConnectionTimeout;
			app.OnConnectionTimeout (connectionCallback);
			
			app.Run (udpHost, udpPort, User.Instance.username, authtoken);
			StartRegionClient (authtoken);
		}

		public void OnAppStarted ()
		{
			userApp.OnLoggedIn ();
			loginUi.DisableLogin ();
		}

		public void OnConnectionTimeout ()
		{
			userApp.ConnectionTimeout ();
		}

		public void OnRegionConnectionTimeout ()
		{
			Logger.Debug ("Region Connection timed out");
		}
		
		void OnRegionClientStarted ()
		{
			Logger.Debug ("OnRegionClientStarted called");
		}

		void StartRegionClient (string authtoken)
		{
			regionClient = this.gameObject.AddComponent (Type.GetType ("GameMachine.Core.RegionClient")) as RegionClient;
			RegionClient.ConnectionTimeout connectionCallback = OnRegionConnectionTimeout;
			regionClient.OnConnectionTimeout (connectionCallback);
			
			RegionClient.RegionClientStarted callback = OnRegionClientStarted;
			regionClient.OnRegionClientStarted (callback);
			
			regionClient.Init (udpRegionPort, User.Instance.username, authtoken);
		}

		void Start ()
		{

		}
	
	}
}
