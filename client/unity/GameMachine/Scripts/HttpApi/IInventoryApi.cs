using UnityEngine;
using System.Collections;
using RequestPlayerItems = io.gamemachine.messages.RequestPlayerItems;
using PlayerItems = io.gamemachine.messages.PlayerItems;
using PlayerItem = io.gamemachine.messages.PlayerItem;
using AddPlayerItem = io.gamemachine.messages.AddPlayerItem;
using RemovePlayerItem = io.gamemachine.messages.RemovePlayerItem;
using UpdatePlayerItem = io.gamemachine.messages.UpdatePlayerItem;

namespace GameMachine {
    namespace HttpApi {
        public interface IInventoryApi {

            void OnRequestPlayerItemsError(string error);
            void OnRequestPlayerItems(RequestPlayerItems requestPlayerItems);

            void OnAddPlayerItemError(string error);
            void OnAddPlayerItem(AddPlayerItem addPlayerItem);

            void OnUpdatePlayerItemError(string error);
            void OnUpdatePlayerItem(UpdatePlayerItem updatePlayerItem);

            void OnRemovePlayerItemError(string error);
            void OnRemovePlayerItem(RemovePlayerItem removePlayerItem);
        }
    }
}
