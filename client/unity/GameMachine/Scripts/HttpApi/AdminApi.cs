using UnityEngine;
using System.Collections;
using GameMachine;
using GameMachine.Common;
using GameMachine.Core;
using System.IO;
using ProtoBuf;
using System.Linq;
using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;
using Players = io.gamemachine.messages.Players;
using Player = io.gamemachine.messages.Player;
using io.gamemachine.messages;

namespace GameMachine {
    namespace HttpApi {
        public class AdminApi : MonoBehaviour {

            public static AdminApi instance;

            public delegate void OnProcessCommand(bool result);
            public OnProcessCommand onProcessCommand;

            void Awake() {
                instance = this;
            }

            // Use this for initialization
            void Start() {

            }

            public void SendProcessCommand(ProcessCommand command) {
                StartCoroutine(SendProcessCommandRoutine(command));
            }

            public IEnumerator SendProcessCommandRoutine(ProcessCommand command) {
                MemoryStream stream = new MemoryStream();
                Serializer.Serialize(stream, command);
                string data = System.Convert.ToBase64String(stream.ToArray());

                string uri = NetworkSettings.instance.BaseUri() + "/api/process_manager";
                var form = new WWWForm();
                form.AddField("process_command", data);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    onProcessCommand(false);
                } else {
                    onProcessCommand(true);
                    Debug.Log(www.text);
                }
            }

            public void PlayerCharacters(string playerId, IAdminApi caller) {
                StartCoroutine(PlayerCharactersRoutine(playerId, caller));
            }

            public IEnumerator PlayerCharactersRoutine(string playerId, IAdminApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/characters/list";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("get_playerId", playerId);
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

            public void SetPassword(string playerId, string password, IAdminApi caller) {
                StartCoroutine(SetPasswordRoutine(playerId, password, caller));
            }

            public IEnumerator SetPasswordRoutine(string playerId, string password, IAdminApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/admin/players/set_password";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("set_password", password);
                form.AddField("set_player_id", playerId);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnPlayerPasswordError(www.error);
                } else {
                    caller.OnPlayerPasswordSet(www.text);
                }
            }

            public void DeletePlayer(string playerId, IAdminApi caller) {
                StartCoroutine(DeletePlayerRoutine(playerId, caller));
            }

            public IEnumerator DeletePlayerRoutine(string playerId, IAdminApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/admin/players/delete";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("delete_player_id", playerId);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnPlayerDeleteError(www.error);
                } else {
                    caller.OnPlayerDeleted(www.text);
                }
            }

            public void CharacterSearch(string search, IAdminApi caller) {
                StartCoroutine(CharacterSearchRoutine(search, caller));
            }

            public IEnumerator CharacterSearchRoutine(string search, IAdminApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/admin/characters/search";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("search_string", search);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnCharacterSearchError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Characters characters = Serializer.Deserialize<Characters>(stream);
                    caller.OnCharacterSearch(characters);
                }
            }

            public void PlayerSearch(string search, IAdminApi caller) {
                StartCoroutine(PlayerSearchRoutine(search, caller));
            }

            public IEnumerator PlayerSearchRoutine(string search, IAdminApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/admin/players/search";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("search_string", search);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnPlayerSearchError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Players players = Serializer.Deserialize<Players>(stream);
                    caller.OnPlayerSearch(players);
                }
            }

        }
    }
}
