using GameMachine;
using GameMachine.Models.Team;
using GameMachine.Core;

namespace GameMachine
{
	public sealed class TeamsManager
	{
		public delegate void InviteReceived (TeamInvite teamInvite);
		public delegate void TeamsReceived (Teams teams);
		public delegate void TeamReceived (Team team);
		public delegate void JoinedTeam (TeamJoined teamJoined);
		public delegate void LeftTeam (TeamLeft teamLeft);
		
		public InviteReceived inviteReceived;
		public TeamsReceived teamsReceived;
		public TeamReceived teamReceived;
		public JoinedTeam joinedTeam;
		public LeftTeam leftTeam;

		static readonly TeamsManager _instance = new TeamsManager ();
		public static TeamsManager Instance {
			get {
				return _instance;
			}
		}
		
		TeamsManager ()
		{
		}

		public void OnInviteReceived (InviteReceived callback)
		{
			inviteReceived = callback;
		}

		public void OnTeamReceived (TeamReceived callback)
		{
			teamReceived = callback;
		}
        

		public void OnTeamsReceived (TeamsReceived callback)
		{
			teamsReceived = callback;
		}

		public void OnJoinedTeam (JoinedTeam callback)
		{
			joinedTeam = callback;
		}

		public void OnLeftTeam (LeftTeam callback)
		{
			leftTeam = callback;
		}
	}
}