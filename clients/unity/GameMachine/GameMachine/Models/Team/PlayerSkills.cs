using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
    
    public class PlayerSkills : JsonModel
    {
        public Dictionary<string,int> skills = new Dictionary<string,int> ();
        
        public void AddSkill (string skillName, int skillValue)
        {
            if (!skills.ContainsKey (skillName)) {
                skills [skillName] = skillValue;
            }
        }

        public static void Receive (string json)
        {
            PlayerSkills playerSkills = JsonConvert.DeserializeObject < PlayerSkills > (json);
        }
        
        public override string ToJson ()
        {
            return JsonConvert.SerializeObject (this, Formatting.Indented);
        }
    }
}