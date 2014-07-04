using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using GameMachine.Models.Team;
using GameMachine.Core;
using GameMachine.Chat;

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
		private string teamMaxMembers = "6";
		public bool hasTeam = false;
		public Team currentTeam;
		public ChannelUi currentChannelUi;
		private float leftTeamAt = Time.time;
		private Messenger messenger;
		private TeamInvite teamInvite;
		private Dictionary<string, Team> playerTeams = new Dictionary<string, Team> ();

		void OnGUI ()
		{
			GUI.contentColor = Color.green;
			GUI.backgroundColor = Color.black;
			
			groupWindow = GUILayout.Window (200, groupWindow, WindowFunction2, "Teams", GUILayout.Width (windowWidth));
		}
		
		void WindowFunction2 (int windowID)
		{
			GUILayout.BeginHorizontal ();
			if (hasTeam) {
				if (currentTeam.match_id == null) {
					if (GUILayout.Button ("Find Match")) {
						FindMatch ();
					}
				} else {
					GUILayout.Label ("Joined match " + currentTeam.match_id + " on server " + currentTeam.match_server);
				}

			} else {
				if (GUILayout.Button ("Create team")) {
					CreateTeam (teamName, "public");
				}
				if (GUILayout.Button ("Create private team")) {
					CreateTeam (teamName, "private");
				}
				GUILayout.Label ("Name:");
				teamName = GUILayout.TextField (teamName);
				GUILayout.Label ("Max members:");
				teamMaxMembers = GUILayout.TextField (teamMaxMembers);

			}			
			GUILayout.EndHorizontal ();

			if (teams != null) {
				GUILayout.Label ("");
				foreach (Team team in teams.teams) {
					
					GUILayout.BeginHorizontal ();
					GUILayout.Label (team.name + " (" + team.access + ")");
					GUILayout.Label (team.members.Count.ToString ());
					if (!hasTeam && team.access == "public") {
						if (GUILayout.Button ("Join")) {
							JoinTeam joinTeam = new JoinTeam ();
							joinTeam.name = team.name;
							joinTeam.Send ();
						}
					}

					GUILayout.EndHorizontal ();
				}
			}

			if (!hasTeam && teamInvite != null) {
				GUILayout.BeginHorizontal ();
				if (GUILayout.Button ("Accept invite to " + teamInvite.name)) {
					TeamAcceptInvite accept = new TeamAcceptInvite ();
					accept.invite_id = teamInvite.invite_id;
					accept.name = teamInvite.name;
					accept.Send ();
					teamInvite = null;

				}

				GUILayout.EndHorizontal ();
			}
			GUI.DragWindow ();
		}

		private void FindMatch ()
		{
			FindMatch findMatch = new FindMatch ();
			findMatch.team_name = currentTeam.name;
			findMatch.Send ();
		}
		private void CreateTeam (string teamName, string access)
		{
			CreateTeam createTeam = new CreateTeam ();
			createTeam.name = teamName;
			createTeam.owner = User.Instance.username;
			createTeam.access = access;
			createTeam.max_members = System.Convert.ToInt32 (teamMaxMembers);
			createTeam.Send ();
		}

		private void JoinTeam (Team team)
		{
			if (Time.time - leftTeamAt < 1.0f) {
				return;
			}
			ChannelUi.DestroyChannelUi (team.name);
			currentChannelUi = ChannelUi.CreateChannelUi (this.gameObject, team.name, "Team");
			currentChannelUi.buttonName = "Leave Team";
			currentChannelUi.title = "Team " + team.name;
			hasTeam = true;
			ChannelUi.ChannelLeft channelLeft = OnChannelUiLeft;
			currentChannelUi.OnChannelLeft (channelLeft);
		}

		public Teams GetTeams ()
		{
			return teams;
		}

		public Dictionary<string, Team> GetPlayerTeams ()
		{
			return playerTeams;
		}
		// Sent in response to our TeamsRequest
		public void OnTeamsReceived (Teams teams)
		{
			this.teams = teams;
			playerTeams.Clear ();
			foreach (Team team in teams.teams) {
				foreach (string member in team.members) {
					playerTeams [member] = team;
				}
			}
		}

		// This is our team, sent in response to our
		// TeamsRequest
		public void OnTeamReceived (Team team)
		{
			currentTeam = team;
			if (hasTeam) {
				if (currentTeam.match_id != null) {
				}
			} else {
				JoinTeam (team);
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
			currentChannelUi = null;
			ChannelUi.DestroyChannelUi (teamLeft.name);
			hasTeam = false;
			currentTeam = null;
			leftTeamAt = Time.time;
		}

		public void OnTeamInviteReceived (TeamInvite teamInvite)
		{
			this.teamInvite = teamInvite;
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
			ui.teamUi = this;
		}

		public string CurrentTeamOwner ()
		{
			return currentTeam.owner;
		}

		public bool IsOwner ()
		{
			return (hasTeam && currentTeam.owner == User.Instance.username);
		}

		public bool IsPrivateOwner ()
		{
			return (IsOwner () && currentTeam.access == "private");
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
