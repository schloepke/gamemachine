using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using Newtonsoft.Json;

namespace GameMachine.World
{
    
    public class Vitals : JsonModel
    {
       
        public string entity_type { get; set; }
        public int health { get; set; }
        public int max_health { get; set; }
        public int offense_skill { get; set; }
        public int defense_skill { get; set; }
        public int x { get; set; }
        public int y { get; set; }

        public static void Receive(string json)
        {
            Vitals vitals = JsonConvert.DeserializeObject < Vitals >(json);
            GameMachine.World.Player.vitals = vitals;
        }
       
        public override string ToJson()
        {
            return JsonConvert.SerializeObject(this, Formatting.Indented);
        }
    }
}