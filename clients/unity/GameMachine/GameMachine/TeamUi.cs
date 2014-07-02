using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using GameMachine.Models.Team;

namespace GameMachine
{
	public class TeamUi : MonoBehaviour
	{
		
		private Rect groupWindow;
		private float windowLeft;
		private float windowHeight;
		private float windowWidth;
		private float windowTop;
		private Teams teams;
		private TeamsManager teamsManager;
		private string teamName = "";
		private bool hasTeam = false;
		private Team currentTeam;
		private float leftTeamAt = Time.time;
		private Messenger messenger;

		void OnGUI ()
		{
			GUI.contentColor = Color.green;
			GUI.backgroundColor = Color.black;
			
			groupWindow = GUILayout.Window (200, groupWindow, WindowFunction2, "Teams", GUILayout.Width (windowWidth));
		}
		
		void WindowFunction2 (int windowID)
		{
			if (!hasTeam) {
				GUILayout.BeginHorizontal ();
				if (GUILayout.Button ("Create team")) {
					CreateTeam (teamName, "public");
				}
				if (GUILayout.Button ("Create private team")) {
					CreateTeam (teamName, "private");
				}
                
				teamName = GUILayout.TextField (teamName);
				GUILayout.EndHorizontal ();
			}			


			if (teams != null) {
				GUILayout.Label ("");
				foreach (Team team in teams.teams) {
					
					GUILayout.BeginHorizontal ();
					GUILayout.Label (team.name);
					GUILayout.Label (team.members.Count.ToString ());
					if (!hasTeam) {
						if (GUILayout.Button ("Join")) {
							JoinTeam joinTeam = new JoinTeam ();
							joinTeam.name = team.name;
							joinTeam.Send ();
						}
					}

					GUILayout.EndHorizontal ();
				}
			}

			GUI.DragWindow ();
		}

		private void CreateTeam (string teamName, string access)
		{
			CreateTeam createTeam = new CreateTeam ();
			createTeam.name = teamName;
			createTeam.owner = User.Instance.username;
			createTeam.access = access;
			createTeam.Send ();
		}

		private void JoinTeam (Team team)
		{
			if (Time.time - leftTeamAt < 5.0f) {
				return;
			}
			ChannelUi.DestroyChannelUi (team.name);
			ChannelUi channelUi = ChannelUi.CreateChannelUi (this.gameObject, team.name, "Team");
			if (User.Instance.username == team.owner) {
				channelUi.buttonName = "Disband Team";
			}
			channelUi.title = "Team " + team.name;
			hasTeam = true;
			ChannelUi.ChannelLeft channelLeft = OnChannelUiLeft;
			channelUi.OnChannelLeft (channelLeft);
		}

		// Sent in response to our TeamsRequest
		public void OnTeamsReceived (Teams teams)
		{
			this.teams = teams;
		}

		// This is our team, sent in response to our
		// TeamsRequest
		public void OnTeamReceived (Team team)
		{
			if (!hasTeam) {
				currentTeam = team;
				JoinTeam (team);
				messenger.leaveChannel ("Lobby");
			}
		}

		// We left/disbanded from the channel ui
		// If the owner of a team leaves, the server
		// will destroy the team and notify all members
		public void OnChannelUiLeft (string name)
		{
			LeaveTeam leaveTeam = new LeaveTeam ();
			leaveTeam.name = name;
			leaveTeam.Send ();
			hasTeam = false;
			currentTeam = null;
			leftTeamAt = Time.time;
			messenger.JoinChannel ("Lobby", "subscribers");
		}

		// Not really used right now although we get an ack
		// We wait until we actualy get the team back from a
		// TeamsRequest to create the channel ui.
		public void OnJoinedTeam (TeamJoined teamJoined)
		{
			//JoinTeam (teamJoined.name);
		}

		// Server told us we left the team.
		public void OnLeftTeam (TeamLeft teamLeft)
		{
			ChannelUi.DestroyChannelUi (teamLeft.name);
			hasTeam = false;
			currentTeam = null;
			leftTeamAt = Time.time;
		}

		public void OnTeamInviteReceived (TeamInvite teamInvite)
		{
			
		}

		public void SendTeamsRequest ()
		{
			TeamsRequest request = new TeamsRequest ();
			request.Send ();
		}

		public void OnLobbyLeft (string name)
		{

		}
		public void CreateLobby ()
		{
			// Create the channel
			messenger.JoinChannel ("Lobby", "subscribers");
			GameObject lobby = new GameObject ("Lobby");
			LobbyUi ui = lobby.AddComponent<GameMachine.LobbyUi> () as LobbyUi;
			ui.channelName = "Lobby";
		}

		// Use this for initialization
		void Start ()
		{
			windowWidth = 600;
			windowHeight = 200;
			windowLeft = 10;//(Screen.width / 2) - windowWidth / 2;
			windowTop = 10;//(Screen.height / 2) - windowHeight / 2;

			groupWindow = new Rect (windowLeft, windowTop, windowWidth, windowHeight);

			messenger = ActorSystem.Instance.Find ("Messenger") as Messenger;
			
			// Setup the callbacks for the different types of team messages we will
			// receive
			teamsManager = TeamsManager.Instance;
			TeamsManager.InviteReceived teamInviteCallback = OnTeamInviteReceived;
			teamsManager.OnInviteReceived (teamInviteCallback);
			TeamsManager.TeamsReceived teamsReceivedCallback = OnTeamsReceived;
			teamsManager.OnTeamsReceived (teamsReceivedCallback);
			TeamsManager.TeamReceived teamReceivedCallback = OnTeamReceived;
			teamsManager.OnTeamReceived (teamReceivedCallback);
			TeamsManager.JoinedTeam joinedTeamCallback = OnJoinedTeam;
			teamsManager.OnJoinedTeam (joinedTeamCallback);
			TeamsManager.LeftTeam leftTeamCallback = OnLeftTeam;
			teamsManager.OnLeftTeam (leftTeamCallback);

			CreateLobby ();
			InvokeRepeating ("SendTeamsRequest", 0.01f, 1.0F);
		}

	}
}
