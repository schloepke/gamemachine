using UnityEngine;
using System.Collections;

namespace GameMachine.World
{
    public class Zones : MonoBehaviour
    {

        // Use this for initialization
        void Start()
        {
	
        }
	
        void OnGUI()
        {

            if (GUI.Button(new Rect(Screen.width - 100, 20, 90, 30), "Zone2"))
            {
                GUI.enabled = false;
                GameObject world = GameObject.Find("World");
                Launcher launcher = world.GetComponent<GameMachine.World.Launcher>();
                launcher.EnterZone("zone2");

            }

        }
        // Update is called once per frame
        void Update()
        {
	
        }
    }
}
