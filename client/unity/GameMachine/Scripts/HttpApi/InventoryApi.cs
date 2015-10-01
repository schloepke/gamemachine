using UnityEngine;
using System.Collections;
using GameMachine;
using GameMachine.Common;
using GameMachine.Core;
using System.IO;
using ProtoBuf;
using System.Linq;
using RequestPlayerItems = io.gamemachine.messages.RequestPlayerItems;
using PlayerItems = io.gamemachine.messages.PlayerItems;
using PlayerItem = io.gamemachine.messages.PlayerItem;
using AddPlayerItem = io.gamemachine.messages.AddPlayerItem;
using RemovePlayerItem = io.gamemachine.messages.RemovePlayerItem;
using UpdatePlayerItem = io.gamemachine.messages.UpdatePlayerItem;

namespace GameMachine {
    namespace HttpApi {
        public class InventoryApi : MonoBehaviour {

            public static InventoryApi instance;


            void Awake() {
                instance = this;
            }

            public void RequestPlayerItems(RequestPlayerItems requestPlayerItems, IInventoryApi caller) {
                StartCoroutine(RequestPlayerItemsRoutine(requestPlayerItems, caller));
            }

            public IEnumerator RequestPlayerItemsRoutine(RequestPlayerItems requestPlayerItems, IInventoryApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/game/inventory/request_player_items";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);

                MemoryStream stream = new MemoryStream();
                Serializer.Serialize(stream, requestPlayerItems);
                string content = System.Convert.ToBase64String(stream.ToArray());
                form.AddField("content", content);

                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnRequestPlayerItemsError(www.error);
                } else {
                    stream = new MemoryStream(www.bytes);
                    requestPlayerItems = Serializer.Deserialize<RequestPlayerItems>(stream);
                    caller.OnRequestPlayerItems(requestPlayerItems);
                }
            }

            public void AddPlayerItem(AddPlayerItem addPlayerItem, IInventoryApi caller) {
                StartCoroutine(AddPlayerItemRoutine(addPlayerItem, caller));
            }

            public IEnumerator AddPlayerItemRoutine(AddPlayerItem addPlayerItem, IInventoryApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/game/inventory/add_player_item";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);

                MemoryStream stream = new MemoryStream();
                Serializer.Serialize(stream, addPlayerItem);
                string content = System.Convert.ToBase64String(stream.ToArray());
                form.AddField("content", content);

                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnAddPlayerItemError(www.error);
                } else {
                    stream = new MemoryStream(www.bytes);
                    addPlayerItem = Serializer.Deserialize<AddPlayerItem>(stream);
                    caller.OnAddPlayerItem(addPlayerItem);
                }
            }

            public void UpdatePlayerItem(UpdatePlayerItem updatePlayerItem, IInventoryApi caller) {
                StartCoroutine(UpdatePlayerItemRoutine(updatePlayerItem, caller));
            }

            public IEnumerator UpdatePlayerItemRoutine(UpdatePlayerItem updatePlayerItem, IInventoryApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/game/inventory/update_player_item";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);

                MemoryStream stream = new MemoryStream();
                Serializer.Serialize(stream, updatePlayerItem);
                string content = System.Convert.ToBase64String(stream.ToArray());
                form.AddField("content", content);

                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnUpdatePlayerItemError(www.error);
                } else {
                    stream = new MemoryStream(www.bytes);
                    updatePlayerItem = Serializer.Deserialize<UpdatePlayerItem>(stream);
                    caller.OnUpdatePlayerItem(updatePlayerItem);
                }
            }

            public void RemovePlayerItem(RemovePlayerItem removePlayerItem, IInventoryApi caller) {
                StartCoroutine(RemovePlayerItemRoutine(removePlayerItem, caller));
            }

            public IEnumerator RemovePlayerItemRoutine(RemovePlayerItem removePlayerItem, IInventoryApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/game/inventory/remove_player_item";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);

                MemoryStream stream = new MemoryStream();
                Serializer.Serialize(stream, removePlayerItem);
                string content = System.Convert.ToBase64String(stream.ToArray());
                form.AddField("content", content);

                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnRemovePlayerItemError(www.error);
                } else {
                    stream = new MemoryStream(www.bytes);
                    removePlayerItem = Serializer.Deserialize<RemovePlayerItem>(stream);
                    caller.OnRemovePlayerItem(removePlayerItem);
                }
            }
        }
    }
}
