using System.Collections;
using System;
using  System.Collections.Generic;

namespace GameMachine
{
    public class TeamMemberSkills
    {
        public static Dictionary<string,List<TeamMemberSkill>> skills = new Dictionary<string,List<TeamMemberSkill>> ();
        public TeamMemberSkills ()
        {
        }

        public static void ClearSkills (string playerId)
        {
            skills [playerId] = new List<TeamMemberSkill> ();
        }

        public static List<TeamMemberSkill> GetSkills (string playerId)
        {
            if (!skills.ContainsKey (playerId)) {
                skills [playerId] = new List<TeamMemberSkill> ();
            }
            return skills [playerId];
        }

        public static void AddSkill (string playerId, string skillName, int skillValue)
        {
            TeamMemberSkill skill = new TeamMemberSkill (skillName, skillValue);
            if (!skills.ContainsKey (playerId)) {
                skills [playerId] = new List<TeamMemberSkill> ();
            }
            skills [playerId].Add (skill);
        }
    }
}

