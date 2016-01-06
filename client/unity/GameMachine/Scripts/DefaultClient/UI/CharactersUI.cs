using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using GameMachine.Common;
using GameMachine.HttpApi;
using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;
using Player = io.gamemachine.messages.Player;
using System;

namespace GameMachine {
    namespace ClientLib {
        public class CharactersUI : MonoBehaviour, IPlayerApi, ICharacterApi {

            private Dictionary<string, CharacterOptions> characterOptions = new Dictionary<string, CharacterOptions>();
            private Dictionary<string, Character> characters = new Dictionary<string, Character>();

            private int maxCharacters = 5;
            private string characterId;
            public GameObject template;

            void Start() {
                template.SetActive(false);
            }

            public void Load() {
                SetCharacterDefaults();
                PlayerApi.instance.PlayerCharacters(this);
            }

            public void CreateCharacter(Button button) {
                UIController.instance.LoadCharacter(null);
            }

            public void DeleteCharacter(Button button) {
                CharacterOptions options = button.transform.parent.GetComponent<CharacterOptions>();
                CharacterApi.instance.DeleteCharacter(options.characterId, this);
            }

            private void SetCharacterDefaults() {
                for (int i = 0; i <= maxCharacters; i++) {
                    string name = "character" + i+1;

                    GameObject go;
                    Transform got = transform.Find(name);
                    if (got == null) {
                        go = GameObject.Instantiate(template) as GameObject;
                        go.AddComponent<CharacterOptions>();
                    } else {
                        go = got.gameObject;
                    }

                    go.SetActive(true);
                    go.name = name;
                    go.transform.SetParent(transform);
                    characterOptions[name] = go.GetComponent<CharacterOptions>();
                    characterOptions[name].Load(name, true);
                }
            }

            public void LoginCharacter(Button button) {
                CharacterOptions options = button.transform.parent.GetComponent<CharacterOptions>();
                characterId = options.characterId;
                //Debug.Log("Using character " + characterId);
                CharacterApi.instance.SetCharacter(characterId, this);
            }

            void IPlayerApi.OnPlayerCharacters(Characters characters) {
                for (int i = 0; i <= maxCharacters; i++) {
                    if (characters.characters.ElementAtOrDefault(i) != null) {
                        Character character = characters.characters[i];
                        this.characters[character.id] = character;
                        string name = "character" + i+1;
                        characterOptions[name].Load(character.id, false);
                    }
                }

                if (DefaultClient.instance.autoLogin) {
                    if (characters.characters.Count > 0) {
                        Character character = characters.characters[0];
                        characterId = character.id;
                        CharacterApi.instance.SetCharacter(characterId, this);
                    }
                }
            }

            void ICharacterApi.OnCharacterSet(string result) {
                //Debug.Log("character set current result " + characterId);
                NetworkSettings.instance.characterId = characterId;
                UIController.instance.OnCharacterSelected(characters[characterId]);
            }

            void ICharacterApi.OnCharacterSetError(string error) {
                Debug.Log("error setting character, unable to fully login: "+error);
            }

            void IPlayerApi.OnPlayerCharactersError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCreated(Player player) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCreateError(string error) {
                throw new System.NotImplementedException();
            }
            
            void IPlayerApi.OnPlayer(Player player) {
            }

            void IPlayerApi.OnPlayerError(string error) {
                throw new System.NotImplementedException();
            }


            void IPlayerApi.OnPasswordChanged(string result) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPasswordError(string error) {
                throw new System.NotImplementedException();
            }

            void ICharacterApi.OnCharacterCreated(Character character) {
                throw new System.NotImplementedException();
            }

            void ICharacterApi.OnCharacterCreateError(string error) {
                throw new System.NotImplementedException();
            }

            

            void ICharacterApi.OnCharacterDeleted(string characterId) {
                Load();
            }

            void ICharacterApi.OnCharacterDeleteError(string error) {
                throw new System.NotImplementedException();
            }


            void ICharacterApi.OnCharacterGet(string playerId, Character character) {
                throw new System.NotImplementedException();
            }

            void ICharacterApi.OnCharacterGetError(string playerId, string characterId, string error) {
                throw new System.NotImplementedException();
            }

            public void OnCharacterSetEquippedItems(string result) {
                throw new NotImplementedException();
            }

            public void OnCharacterSetEquippedItemsError(string error) {
                throw new NotImplementedException();
            }

            public void OnNpcCreated(bool status) {
                throw new NotImplementedException();
            }
        }
    }
}
