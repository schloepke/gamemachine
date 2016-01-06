using UnityEngine;
using System.Collections;
using GameMachine.Common;
using System.IO;
using ProtoBuf;
using io.gamemachine.messages;
using System;

namespace GameMachine {
    namespace HttpApi {
        public class CharacterApi : MonoBehaviour {

            public static CharacterApi instance;

            void Awake() {
                instance = this;
            }

            public void SetBindPoint(string characterId, string bindPoint, Action<Character> action) {
                StartCoroutine(SetBindPointRoutine(characterId, bindPoint, action));
            }

            public IEnumerator SetBindPointRoutine(string characterId, string bindPoint, Action<Character> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/characters/set_bind_point";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                form.AddField("bindPoint", bindPoint);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(null);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Character character = Serializer.Deserialize<Character>(stream);
                    action(character);
                }
            }

            public void CreateNpc(string playerId, string characterId, Vitals.Template template, string prefab, Action<Character> action) {
                StartCoroutine(CreateNpcRoutine(playerId, characterId, template, prefab, action));
            }

            public IEnumerator CreateNpcRoutine(string playerId, string characterId, Vitals.Template template, string prefab, Action<Character> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/internal/create_npc";
                var form = new WWWForm();

                form.AddField("npcCharacterId", characterId);
                form.AddField("npcPlayerId", playerId);
                form.AddField("prefab", prefab);
                form.AddField("vitalsTemplate", (int)template);
                
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(null);
                } else {
                    Character character = new Character();
                    character.playerId = playerId;
                    character.id = characterId;
                    character.gameEntityPrefab = prefab;
                    character.vitalsTemplate = template;
                    action(character);
                }
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

            public void SetItemSlots(string characterId, string slots,Action<bool> action) {
                StartCoroutine(SetItemSlotsRoutine(null, characterId, slots, action));
            }

            public void SetItemSlots(string playerId, string characterId, string slots, Action<bool> action) {
                StartCoroutine(SetItemSlotsRoutine(playerId, characterId, slots, action));
            }

            public IEnumerator SetItemSlotsRoutine(string playerId, string characterId, string slots, Action<bool> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/characters/set_item_slots";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                if (playerId == null) {
                    form.AddField("characterPlayerId", NetworkSettings.instance.username);
                } else {
                    form.AddField("characterPlayerId", playerId);
                }
                
                form.AddField("characterId", characterId);
                
                form.AddField("item_slots", slots);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(false);
                } else {
                    action(true);
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