using System.Collections;
using UnityEngine;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using GameMachine.Models.Team;

namespace GameMachine
{
	public class App : MonoBehaviour
	{
		public Client client;
		public static RemoteEcho remoteEcho;

		public bool running = false;
		public bool connected = false;


		private double lastEcho = 0;
		private double echosPerSecond = 1;
		private double echoInterval;
		private double lastEchoReceived = 0;
		public double echoTimeout = 5.0f;
		public float disconnectTime = 10f;

		public delegate void AppStarted ();
		public delegate void ConnectionTimeout ();
		private AppStarted appStarted;
		private ConnectionTimeout connectionTimeout;

		private bool appStartedCalled = false;


		public void OnAppStarted (AppStarted callback)
		{
			appStarted = callback;
		}

		public void OnConnectionTimeout (ConnectionTimeout connectionTimeout)
		{
			this.connectionTimeout = connectionTimeout;
		}

		public void Login (string uri, string username, string password, Authentication.Success success, Authentication.Error error)
		{
			Authentication auth = new Authentication ();
			StartCoroutine (auth.Authenticate (uri, username, password, success, error));
		}

		public void Run (string host, int port, string username, string authtoken)
		{
			// Create client and start actor system
			client = new Client (host, port, username, authtoken);
			client.Start ();
			ActorSystem.Instance.Start (client);

			// Now create the actors
			StartCoreActors ();
		}

		public void StartCoreActors ()
		{
			// Actors that come with GameMachine

			ObjectDb db = new ObjectDb ();
			db.AddComponentSet ("ObjectdbGetResponse");
			ActorSystem.Instance.RegisterActor (db);

			Messenger messenger = new Messenger ();
			messenger.AddComponentSet ("ChatMessage");
			messenger.AddComponentSet ("ChatChannels");
			messenger.AddComponentSet ("ChatInvite");
			ActorSystem.Instance.RegisterActor (messenger);

			EntityTracking entityTracking = new EntityTracking ();
			entityTracking.AddComponentSet ("Neighbors");
			ActorSystem.Instance.RegisterActor (entityTracking);


			remoteEcho = new RemoteEcho ();
			remoteEcho.AddComponentSet ("EchoTest");
			ActorSystem.Instance.RegisterActor (remoteEcho);

			RegionHandler regionHandler = new RegionHandler ();
			regionHandler.AddComponentSet ("Regions");
			ActorSystem.Instance.RegisterActor (regionHandler);


			RemoteEcho.EchoReceived callback = OnEchoReceived;
			remoteEcho.OnEchoReceived (callback);

			// Json models
			JsonModel.Register (typeof(Team), "GameMachine::Models::Team", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(CreateTeam), "GameMachine::Models::CreateTeam", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(DestroyTeam), "GameMachine::Models::DestroyTeam", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(JoinTeam), "GameMachine::Models::JoinTeam", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(TeamAcceptInvite), "GameMachine::Models::TeamAcceptInvite", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(TeamInvite), "GameMachine::Models::TeamInvite", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(Teams), "GameMachine::Models::Teams", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(TeamsRequest), "GameMachine::Models::TeamsRequest", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(TeamJoined), "GameMachine::Models::TeamJoined", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(LeaveTeam), "GameMachine::Models::LeaveTeam", "GameMachine/GameSystems/TeamManager");
			JsonModel.Register (typeof(TeamLeft), "GameMachine::Models::TeamLeft", "GameMachine/GameSystems/TeamManager");

			running = true;
			Logger.Debug ("App running - waiting to verify connection");
		}

		void UpdateRegions ()
		{
			RegionHandler.SendRequest ();
		}

		void OnEchoReceived ()
		{
			if (!connected) {
				connected = true;
				Logger.Debug ("App connected");
				if (!appStartedCalled) {
					appStarted ();
					appStartedCalled = true;
					InvokeRepeating ("UpdateRegions", 0.01f, 20.0F);
				}
			}

			lastEchoReceived = Time.time;
		}

		void Start ()
		{
			Application.runInBackground = true;
			echoInterval = 0.60 / echosPerSecond;
			lastEchoReceived = Time.time;
			InvokeRepeating ("UpdateNetwork", 0.010f, 0.06F);
		}

		void OnApplicationQuit ()
		{
			if (client != null) {
				Logger.Debug ("Stopping client");
				client.Stop ();
			}
		}


		void UpdateNetwork ()
		{
			if (!running) {
				return;
			}
            
           
			if (running && ActorSystem.Instance.Running) {
				ActorSystem.Instance.Update ();
			}
                
			if (Time.time > (lastEcho + echoInterval)) {
				lastEcho = Time.time;
                    
				if ((Time.time - lastEchoReceived) >= echoTimeout) {
					connected = false;
					Logger.Debug ("Echo timeout");
					if ((Time.time - lastEchoReceived) >= disconnectTime) {
						Logger.Debug ("Connection timeout");
						connectionTimeout ();
					}
				}
				remoteEcho.Echo ();
			}
		}


	}
}
