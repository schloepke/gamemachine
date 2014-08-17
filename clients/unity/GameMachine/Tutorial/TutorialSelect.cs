using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine.Tutorial
{
    public class TutorialSelect : MonoBehaviour
    {
        Listbox menu = new Listbox (110, 10, 400, 150);

        private void Start ()
        {
            menu.itemWidth = 400f;
            menu.AddItem ("tutorial_inventory", "Persistence - Sample inventory system");
            menu.AddItem ("tutorial_none", "None");
            menu.AddItem ("tutorial_mmo", "Area of interest - Mmo demo");
            menu.AddItem ("tutorial_pubsub", "Distributed pub/sub - Chat & Groups");
            menu.AddItem ("tutorial_teams", "Teams & Matchmaking");
        }
    
        private void OnGUI ()
        {
            menu.Show ("Select");
            if (menu.selected) {
                Application.LoadLevel (menu.selection);
                menu.selected = false;
            }
        }
    }
}