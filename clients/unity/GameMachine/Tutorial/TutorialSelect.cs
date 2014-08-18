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
            menu.AddItem ("tutorial_inventory", "Persistence - inventory system");
            menu.AddItem ("tutorial_none", "None");
            menu.AddItem ("tutorial_mmo", "Area of interest - Mmo demo");
            menu.AddItem ("tutorial_messaging", "Messaging, Chat & Groups");
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