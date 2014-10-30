using System.Collections;
using System;
using  System.Collections.Generic;

namespace GameMachine
{
    public class TeamRequirements
    {
        public static Dictionary<string,List<string>> requirements = new Dictionary<string,List<string>> ();

        public static List<string> GetRequirements (string teamName)
        {
            if (!requirements.ContainsKey (teamName)) {
                requirements [teamName] = new List<string> ();
            }
            return requirements [teamName];
        }

        public static void ClearRequirements (string teamName)
        {
            requirements [teamName] = new List<string> ();
        }

        public static void AddRequirement (string teamName, string expr)
        {
            if (!requirements.ContainsKey (teamName)) {
                requirements [teamName] = new List<string> ();
            }
            requirements [teamName].Add (expr);
        }

        public TeamRequirements ()
        {
        }
    }
}

