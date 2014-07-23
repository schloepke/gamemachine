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
		private string protocol = "UDP";
		private int port = 0;
		private string hostname;
		private int udpRegionPort = 24130;
		public bool useRegions = false;
		private ILoginUi loginUi;
		public static GameMachineApp userApp;

		public static void SetGameMachineApp (GameMachineApp userApp)
		{
			Login.userApp = userApp;
		}

		public void SetLoginUi (ILoginUi loginUi)
		{
			this.loginUi = loginUi;
		}

		public void SetUdpParameters (string hostname, int udpPort, int udpRegionPort, bool useRegions)
		{
			this.hostname = hostname;
			this.port = udpPort;
			this.udpRegionPort = udpRegionPort;
			this.useRegions = useRegions;
		}

		public void SetTcpParameters (string hostname, int tcpPort, bool useRegions)
		{
			this.hostname = hostname;
			this.port = tcpPort;
			this.useRegions = useRegions;
		}

		public void DoLogin (string protocol, string username, string password)
		{
			this.protocol = protocol;
			User user = User.Instance;
			user.SetUser (username.ToString (), password.ToString ());
			GameMachine.Core.Authentication.Success success = OnAuthenticationSuccess;
			GameMachine.Core.Authentication.Error error = OnAuthenticationError;
			
			authUri = "http://" + hostname + ":3000/auth";
			app = this.gameObject.AddComponent (Type.GetType ("GameMachine.Core.App")) as GameMachine.Core.App;
			app.Login (authUri, user.username, user.password, success, error);
		}

		void OnAuthenticationError (string error)
		{
			if (loginUi != null) {
				loginUi.SetError (error.Replace (System.Environment.NewLine, ""));
			}
			Login.userApp.OnLoginFailure (error);
		}


		void OnAuthenticationSuccess (string authtoken)
		{
			GameMachine.Core.App.AppStarted callback = OnAppStarted;
			app.OnAppStarted (callback);
			
			GameMachine.Core.App.ConnectionTimeout connectionCallback = OnConnectionTimeout;
			app.OnConnectionTimeout (connectionCallback);

			app.Run (protocol, hostname, port, User.Instance.username, authtoken);

			StartRegionClient (authtoken);
		}

		public void OnAppStarted ()
		{
			userApp.OnLoggedIn ();
			loginUi.DisableLogin ();
		}

		public void OnConnectionTimeout ()
		{
			Login.userApp.ConnectionTimeout ();
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
			
			regionClient.Init (port, User.Instance.username, authtoken);
		}

		void Start ()
		{

		}
	
	}
}
