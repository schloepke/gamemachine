using UnityEngine;
using System.Collections;

namespace GameMachine.World
{
    public class Zones : MonoBehaviour
    {

	
        void OnGUI()
        {

            if (GameMachine.World.Launcher.currentZone == "zone1")
            {
                GUI.Label(new Rect(Screen.width - 140, 10, 120, 30), "Zone: zone1");
                if (GUI.Button(new Rect(Screen.width - 140, 30, 120, 30), "Switch to zone2"))
                {
                    GUI.enabled = false;
                    GameObject world = GameObject.Find("World");
                    Launcher launcher = world.GetComponent<GameMachine.World.Launcher>();
                    launcher.EnterZone("zone2");

                }
            }

            if (GameMachine.World.Launcher.currentZone == "zone2")
            {
                GUI.Label(new Rect(Screen.width - 140, 10, 120, 30), "Zone: zone2");
                if (GUI.Button(new Rect(Screen.width - 140, 30, 120, 30), "Switch to zone1"))
                {
                    GUI.enabled = false;
                    GameObject world = GameObject.Find("World");
                    Launcher launcher = world.GetComponent<GameMachine.World.Launcher>();
                    launcher.EnterZone("zone1");
                
                }
            }

        }

    }
}
