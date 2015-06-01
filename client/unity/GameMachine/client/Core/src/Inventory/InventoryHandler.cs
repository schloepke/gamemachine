using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GameMachine.Core;
using GameMessage = io.gamemachine.messages.GameMessage;
using RequestPlayerItems = io.gamemachine.messages.RequestPlayerItems;
using PlayerItems = io.gamemachine.messages.PlayerItems;
using PlayerItem = io.gamemachine.messages.PlayerItem;
using AddPlayerItem = io.gamemachine.messages.AddPlayerItem;
using RemovePlayerItem = io.gamemachine.messages.RemovePlayerItem;

namespace GameMachine {
    namespace Common {
        public class Destination {
            public const int Inventory = 1;
        }

        public class Inventory {
            private Dictionary<string, string> items = new Dictionary<string, string>();

            public bool HasItems() {
                return items.Count > 0;
            }

            public void ClearItems() {
                items.Clear();
            }

            public void RemoveItem(string id) {
                items.Remove(id);
            }

            public void AddItem(string id, string name) {
                items[id] = name;
            }
        }

        public class InventoryHandler : MonoBehaviour, GameMachine.Core.Behavior {
            public static Dictionary<string, PlayerItem> playerItems = new Dictionary<string, PlayerItem>();
            public static Dictionary<string, PlayerItem> catalog = new Dictionary<string, PlayerItem>();
            private Inventory itemsList;
            private Inventory inventoryList;
            public bool hasUpdate = false;

            private GameMessageHandler messageHandler = GameMessageHandler.Instance;

            private void Start() {
                messageHandler.Register(this, "PlayerItems");

                GetItems();

                itemsList = new Inventory();
                inventoryList = new Inventory();
            }

            public void OnMessage(object message) {

                if (message is PlayerItems) {
                    PlayerItems playerItems = (PlayerItems)message;

                    if (playerItems.catalog) {
                        itemsList.ClearItems();
                        foreach (PlayerItem playerItem in playerItems.playerItem) {
                            itemsList.AddItem(playerItem.id, playerItem.name);
                            InventoryHandler.catalog[playerItem.id] = playerItem;
                        }
                        messageHandler.Send(new RequestPlayerItems(), Destination.Inventory);

                    } else {
                        foreach (PlayerItem playerItem in playerItems.playerItem) {
                            if (playerItem.quantity <= 0) {
                                InventoryHandler.playerItems.Remove(playerItem.id);
                            } else {
                                InventoryHandler.playerItems[playerItem.id] = playerItem;
                            }

                        }
                        hasUpdate = true;
                        UpdateInventory();
                    }
                }
            }

            public void UpdateInventory() {
               // Use this to update your inventory UI

                foreach (PlayerItem playerItem in playerItems.Values) {
                    //inventoryUI.SetItem(playerItem);
                }
            }

            public void GetItems() {
                RequestPlayerItems requestPlayerItems = new RequestPlayerItems();
                requestPlayerItems.catalog = true;
                messageHandler.Send(requestPlayerItems, Destination.Inventory);
            }

            public void RemoveItem(string id, int quantity) {
                RemovePlayerItem removePlayerItem = new RemovePlayerItem();
                removePlayerItem.id = id;
                removePlayerItem.quantity = quantity;
                messageHandler.SendReliable(removePlayerItem, Destination.Inventory);
            }

            public void AddItem(string id, int quantity) {
                PlayerItem catalogItem = catalog[id];
                PlayerItem playerItem = new PlayerItem();
                playerItem.quantity = 1;
                playerItem.name = catalogItem.name;
                playerItem.id = catalogItem.id;

                AddPlayerItem addPlayerItem = new AddPlayerItem();
                addPlayerItem.playerItem = playerItem;
                messageHandler.SendReliable(addPlayerItem, Destination.Inventory);
            }
            

            public void OnError(object message) {
                throw new System.NotImplementedException();
            }
        }
    }
}
