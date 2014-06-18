using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GameMachine.World;

namespace GameMachine.World
{
    public class NpcManager : MonoBehaviour
    {

        public static Dictionary<string, GameObject> npcs = new Dictionary<string, GameObject>();

        NpcManager()
        {
        }

        public static void DestroyNcp(string name)
        {
            Destroy(npcs [name]);
            npcs.Remove(name);
        }

        public void UpdateFromTracking(TrackingUpdate update)
        {
            if (npcs.ContainsKey(update.entityId))
            {
                // Update existing npc
                GameObject npcObject = npcs [update.entityId];
                Npc npc = npcObject.GetComponent<Npc>();
                npc.UpdateTarget(new Vector3(update.x, update.z, update.y));

            } else
            {
                if (update.entityId == User.Instance.username)
                {
                    return;
                }
                // New npc
                GameObject npcObject = (GameObject)Instantiate(Resources.Load("Npc"));
                npcObject.name = update.entityId;
                Npc npc = npcObject.GetComponent<Npc>();
                npc.name = update.entityId;
                npcs.Add(update.entityId, npcObject);
                npc.transform.position = new Vector3(update.x, update.z, update.y);
                //npc.UpdateTarget(new Vector3(update.x, update.z, update.y));
            }
        }
    }
}