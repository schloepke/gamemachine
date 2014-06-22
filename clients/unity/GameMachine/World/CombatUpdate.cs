using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using Newtonsoft.Json;

namespace GameMachine.World
{
    
    public class CombatUpdate : JsonModel
    {
        public string target { get; set; }
        public string attacker { get; set; }
        public string combat_ability { get; set; }
        public int damage { get; set; }
        public int target_health { get; set; }
        

        public static void Receive(string json)
        {
            CombatUpdate combatUpdate = JsonConvert.DeserializeObject < CombatUpdate >(json);
        }
       
        public override string ToJson()
        {
            return JsonConvert.SerializeObject(this, Formatting.Indented);
        }
    }
}