using UnityEngine;
using System.Collections;
using UnityEngine.UI;
using System.Collections.Generic;
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

namespace GameMachine {
    namespace HttpApi {
        public class CharacterApi : MonoBehaviour {

            public static CharacterApi instance;
           

            void Awake() {
                instance = this;
            }


            public void GetCharacter(string playerId, string characterId, ICharacterApi caller) {
                StartCoroutine(GetCharacterRoutine(playerId, characterId, caller));
            }

            public IEnumerator GetCharacterRoutine(string playerId, string characterId, ICharacterApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/characters/get";
                var form = new WWWForm();
                
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                if (!string.IsNullOrEmpty(playerId)) {
                    form.AddField("otherPlayerId", playerId);
                }
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnCharacterGetError(playerId,characterId,www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Character character = Serializer.Deserialize<Character>(stream);
                    caller.OnCharacterGet(playerId,character);
                }
            }

            public void SetCharacter(string characterId, ICharacterApi caller) {
                StartCoroutine(SetCharacterRoutine(characterId, caller));
            }

            public IEnumerator SetCharacterRoutine(string characterId, ICharacterApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/characters/set_current";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnCharacterSetError(www.error);
                } else {
                    caller.OnCharacterSet(www.text);
                }
            }

            public void CreateCharacter(string characterId, ICharacterApi caller) {
                StartCoroutine(CreateCharacterRoutine(characterId, caller));
            }

            public IEnumerator CreateCharacterRoutine(string characterId, ICharacterApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/characters/create";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnCharacterCreateError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Character character = Serializer.Deserialize<Character>(stream);
                    if (character.id == "exists") {
                        caller.OnCharacterCreateError(characterId+" already exists");
                    } else {
                        caller.OnCharacterCreated(character);
                    }
                    
                }
            }

            public void DeleteCharacter(string characterId, ICharacterApi caller) {
                StartCoroutine(DeleteCharacterRoutine(characterId, caller));
            }

            public IEnumerator DeleteCharacterRoutine(string characterId, ICharacterApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/characters/delete";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnCharacterDeleteError(www.error);
                } else {
                    caller.OnCharacterDeleted(www.text);
                }
            }
        }
    }
}