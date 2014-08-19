using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GameMachine.Core;
using GameMessage = GameMachine.Messages.GameMessage;
using RequestPlayerItems = GameMachine.Messages.RequestPlayerItems;
using PlayerItems = GameMachine.Messages.PlayerItems;
using PlayerItem = GameMachine.Messages.PlayerItem;
using AddPlayerItem = GameMachine.Messages.AddPlayerItem;
using RemovePlayerItem = GameMachine.Messages.RemovePlayerItem;

namespace GameMachine.Tutorial
{
    public class ItemManagementDemo : MonoBehaviour, GameMachine.Core.Behavior
    {
        private Dictionary<string, PlayerItem> playerItems = new Dictionary<string, PlayerItem> ();
        private Dictionary<string, PlayerItem> catalog = new Dictionary<string, PlayerItem> ();
        private Listbox itemsListbox;
        private Listbox inventoryListbox;

        private GameMessageHandler messageHandler = GameMessageHandler.Instance;

        private void Start ()
        {
            messageHandler.Register (this, "PlayerItems");

            RequestPlayerItems requestPlayerItems = new RequestPlayerItems ();
            requestPlayerItems.catalog = true;
            messageHandler.Send (requestPlayerItems, Destination.Inventory);
           
            itemsListbox = new Listbox (200, 150, 150, 300);
            inventoryListbox = new Listbox (700, 150, 150, 300);
        }
    
        public void OnMessage (object message)
        {
           
            if (message is PlayerItems) {
                PlayerItems playerItems = (PlayerItems)message;

                if (playerItems.catalog) {
                    itemsListbox.ClearItems ();
                    foreach (PlayerItem playerItem in playerItems.playerItem) {
                        itemsListbox.AddItem (playerItem.id, playerItem.name);
                        this.catalog [playerItem.id] = playerItem;
                    }
                    messageHandler.Send (new RequestPlayerItems (), Destination.Inventory);

                } else {
                    foreach (PlayerItem playerItem in playerItems.playerItem) {
                        if (playerItem.quantity <= 0) {
                            this.playerItems.Remove (playerItem.id);
                        } else {
                            this.playerItems [playerItem.id] = playerItem;
                        }

                    }
                    UpdateInventory ();
                }
            }
        }
         
        void UpdateInventory ()
        {
            inventoryListbox.ClearItems ();
            foreach (PlayerItem playerItem in playerItems.Values) {
                string desc = playerItem.name + " (" + playerItem.quantity + ")";
                inventoryListbox.AddItem (playerItem.id, desc);
            }
        }


        private void OnGUI ()
        {
            inventoryListbox.Show ("Select");
            itemsListbox.Show ("Select");

            if (inventoryListbox.selected) {
                if (GUI.Button (new Rect (780, 150, 200, 25), "Remove selected")) {
                    RemovePlayerItem removePlayerItem = new RemovePlayerItem ();
                    removePlayerItem.id = inventoryListbox.selection;
                    removePlayerItem.quantity = 1;
                    messageHandler.SendReliable (removePlayerItem, Destination.Inventory);
                }
            }

            if (itemsListbox.selected) {
                if (GUI.Button (new Rect (280, 150, 150, 25), "Add item")) {
                    PlayerItem catalogItem = catalog [itemsListbox.selection];

                    PlayerItem playerItem = new PlayerItem ();
                    playerItem.quantity = 1;
                    playerItem.name = catalogItem.name;
                    playerItem.id = catalogItem.id;

                    AddPlayerItem addPlayerItem = new AddPlayerItem ();
                    addPlayerItem.playerItem = playerItem;
                    messageHandler.SendReliable (addPlayerItem, Destination.Inventory);
                }
            }
        }

    }
}
