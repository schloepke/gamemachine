using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using  System.Text.RegularExpressions;
using GameMachine.World;

namespace GameMachine.World
{
    public class NpcManager : MonoBehaviour
    {

        public static Dictionary<string, Npc> npcs = new Dictionary<string, Npc>();

        NpcManager()
        {
        }

        void Start()
        {
            InvokeRepeating("ExpireNpcs", 2, 0.5F);
        }

        public void ExpireNpcs()
        {
            var itemsToRemove = npcs.Where(f => (Time.time - f.Value.lastUpdate) > 0.5f).ToArray();
            foreach (var item in itemsToRemove)
            {
                Logger.Debug("Removing " + item.Key);
                Destroy(item.Value.gameObject);
                npcs.Remove(item.Key);
            }
                
        }

        public static void DestroyNpc(string name)
        {
            Destroy(npcs [name]);
            npcs.Remove(name);
        }

        public void UpdateFromTracking(List<TrackingUpdate> updates)
        {

            GameObject npcObject;

            foreach (TrackingUpdate update in updates)
            {

                if (npcs.ContainsKey(update.entityId))
                {
                    // Update existing npc
                    Npc npc = npcs [update.entityId];
                    npc.UpdateTarget(new Vector3(update.x, update.z, update.y));

                } else
                {
                    if (update.entityId != User.Instance.username)
                    {
                        // New npc
                        string npcType = "Npc";

                        if (update.entityId.StartsWith("male"))
                        {
                            npcType = "MaleNpc";
                        } else if (update.entityId.StartsWith("viking"))
                        {
                            npcType = "NPc";
                        } else if (update.entityId.StartsWith("golem"))
                        {
                            npcType = "GolemNpc";
                        } else if (update.entityId.StartsWith("worm"))
                        {
                            npcType = "WormNpc";
                        }

                        npcObject = (GameObject)Instantiate(Resources.Load(npcType));
                        npcObject.name = update.entityId;
                        Npc npc = npcObject.GetComponent<Npc>();
                        npc.name = update.entityId;
                        npcs.Add(update.entityId, npc);
                        npc.transform.position = npc.SpawnLocation(new Vector3(update.x, 0f, update.y));
                        //npc.transform.position = new Vector3(update.x, 40f, update.y);
                    }
                }
            }


        }
    }
}