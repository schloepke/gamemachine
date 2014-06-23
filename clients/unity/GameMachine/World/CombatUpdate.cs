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

            if (GameMachine.World.Launcher.playerComponent.name == combatUpdate.target)
            {
                GameMachine.World.Launcher.playerComponent.Attacked(combatUpdate);
            }

            Npc npc;

            if (GameMachine.World.NpcManager.npcs.ContainsKey(combatUpdate.attacker))
            {
                npc = GameMachine.World.NpcManager.npcs [combatUpdate.attacker];
                npc.Attack(combatUpdate);
            } else if (GameMachine.World.NpcManager.npcs.ContainsKey(combatUpdate.target))
            {
                npc = GameMachine.World.NpcManager.npcs [combatUpdate.target];
                npc.Attacked(combatUpdate);
            }
        }
       
        public override string ToJson()
        {
            return JsonConvert.SerializeObject(this, Formatting.Indented);
        }
    }
}