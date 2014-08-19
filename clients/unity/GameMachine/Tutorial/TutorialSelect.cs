using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine.Tutorial
{
    public class TutorialSelect : MonoBehaviour
    {
        Listbox menu = new Listbox (Screen.width - 350, 10, 400, 150);

        private void Start ()
        {
            menu.itemWidth = 400f;
            menu.AddItem ("item_management_demo", "Persistence - Item management");
            menu.AddItem ("chat_demo", "Chat & Groups");
            menu.AddItem ("team_demo", "Teams & Matchmaking");
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