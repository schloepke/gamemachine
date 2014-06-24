using UnityEngine;
using System;
using System.Collections;
using  System.Collections.Generic;
using System.Collections;
using GameMachine.World;
using GameMachine;

namespace GameMachine.World
{
    public class Launcher : MonoBehaviour
    {

        public static GameMachine.World.Player playerComponent;
        public static GameObject sounds;
        public static string currentZone;
        public static bool worldStarted = false;
        public string playerName;


        public void StartWorld()
        {
            sounds = GameObject.Find("Sounds");
            GameObject world = GameObject.Find("World");
            StartChat();
        }

        public void AddPlayer()
        {
            GameObject player = (GameObject)Instantiate(Resources.Load("Player"));
            player.name = playerName;
            playerComponent = player.GetComponent<GameMachine.World.Player>();
            playerComponent.SetNameTag(User.Instance.username);
            AreaOfInterest tracker = player.AddComponent(Type.GetType("GameMachine.World.AreaOfInterest")) as AreaOfInterest;
        }

        public void RemovePlayer()
        {
            GameObject player = GameObject.Find(playerName);
            Destroy(player);
        }

        public void WaitForTerrain()
        {
            Terrain terrain = Terrain.activeTerrain;
            if (terrain == null)
            {
                Invoke("WaitForTerrain", 0.05f);
            } else
            {
                if (worldStarted)
                {
                    AddPlayer();
                    GameObject player = GameObject.Find(playerName);
                    playerComponent = player.GetComponent<GameMachine.World.Player>();
                    if (currentZone == "zone1")
                    {
                        playerComponent.Spawn(500f, 500f);
                    } else if (currentZone == "zone2")
                    {
                        playerComponent.Spawn(200f, 300f);
                    }
                } else
                {
                    StartWorld();
                    worldStarted = true;
                    AddPlayer();
                }
            }
        }

        public void EnterZone(string zone)
        {
            GameObject terrain = GameObject.Find(currentZone + "Terrain");
            Destroy(terrain);
            RemovePlayer();

            currentZone = zone;
            Application.LoadLevelAdditive(currentZone);
            Invoke("WaitForTerrain", 0.05f);
        }

        void Start()
        {
            currentZone = GameMachine.World.Player.vitals.zone;
            playerName = GameMachine.World.Player.vitals.id;
            Logger.Debug("Loading Zone " + currentZone);
            Application.LoadLevelAdditive(currentZone);
            Invoke("WaitForTerrain", 1.05f);
        }
	
        public static void StartChat()
        {
            GameObject world = GameObject.Find("World");
            GameObject chatBox = new GameObject("ChatBox");
            chatBox.transform.parent = world.transform;
            chatBox.AddComponent("Chat");
        }

    }
}
