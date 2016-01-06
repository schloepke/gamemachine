using UnityEngine;
using System.Collections;
using GameMachine.Common;
using System.IO;
using ProtoBuf;
using io.gamemachine.messages;

namespace GameMachine {
    namespace HttpApi {
        public class PlayerApi : MonoBehaviour {

            public static PlayerApi instance;

            void Awake() {
                instance = this;
            }

            void Start() {
            }

            public void ChangePassword(string username, string password, string newPassword, IPlayerApi caller) {
                StartCoroutine(ChangePasswordRoutine(username, password, newPassword, caller));
            }

            public IEnumerator ChangePasswordRoutine(string username, string password, string newPassword, IPlayerApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/players/change_password";
                var form = new WWWForm();
                form.AddField("playerId", username);
                form.AddField("password", password);
                form.AddField("new_password", newPassword);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnPasswordError(www.error);
                } else {
                    caller.OnPasswordChanged(www.text);
                }
            }

            public void GetPlayer(IPlayerApi caller) {
                StartCoroutine(GetPlayerRoutine(caller));
            }

            public IEnumerator GetPlayerRoutine(IPlayerApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/players/get";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnPlayerError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Player player = Serializer.Deserialize<Player>(stream);
                    caller.OnPlayer(player);
                }
            }

            public void GetPlayer(string playerId, IPlayerApi caller) {
                StartCoroutine(GetPlayerRoutine(playerId, caller));
            }

            public IEnumerator GetPlayerRoutine(string playerId, IPlayerApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/players/get_other";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("otherPlayerId", playerId);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnPlayerError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Player player = Serializer.Deserialize<Player>(stream);
                    caller.OnPlayer(player);
                }
            }

            public void CreatePlayer(string playerId, string password, IPlayerApi caller) {
                 StartCoroutine(CreatePlayerRoutine(playerId, password, caller));
            }

            public IEnumerator CreatePlayerRoutine(string playerId, string password, IPlayerApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/players/create";
                var form = new WWWForm();
                form.AddField("new_player_id", playerId);
                form.AddField("new_player_password", password);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnPlayerCreateError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Player player = Serializer.Deserialize<Player>(stream);
                    caller.OnPlayerCreated(player);
                }
            }

            


            public void PlayerCharacters(IPlayerApi caller) {
                StartCoroutine(PlayerCharactersRoutine(caller));
            }

            public IEnumerator PlayerCharactersRoutine(IPlayerApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/characters/list";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnPlayerCharactersError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Characters characters = Serializer.Deserialize<Characters>(stream);
                    caller.OnPlayerCharacters(characters);
                }
            }
        }
    }
}
