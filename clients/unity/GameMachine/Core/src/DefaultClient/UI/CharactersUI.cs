using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using GameMachine.Common;
using GameMachine.Api;
using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;
using Player = io.gamemachine.messages.Player;

namespace GameMachine {
    namespace DefaultClient {
        public class CharactersUI : MonoBehaviour, IPlayerApi, ICharacterApi {

            private Dictionary<string, CharacterOptions> characterOptions = new Dictionary<string, CharacterOptions>();
            private Dictionary<string, Character> characters = new Dictionary<string, Character>();

            private int maxCharacters = 5;
            private string characterId;
            GameObject template;

            void Start() {
                gameObject.GetComponent<Canvas>().enabled = false;
                template = transform.Find("panel").Find("character").gameObject;
                template.SetActive(false);
            }

            public void Load() {
                gameObject.GetComponent<Canvas>().enabled = true;
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

                    GameObject go = GameObject.Find(name);
                    if (go == null) {
                        go = GameObject.Instantiate(template) as GameObject;
                        go.AddComponent<CharacterOptions>();
                    }

                    go.SetActive(true);
                    go.name = name;
                    go.transform.SetParent(transform.Find("panel"));
                    characterOptions[name] = go.GetComponent<CharacterOptions>();
                    characterOptions[name].Load(name, true);
                }
            }

            public void LoginCharacter(Button button) {
                CharacterOptions options = button.transform.parent.GetComponent<CharacterOptions>();
                characterId = options.characterId;
                Debug.Log("Using character " + characterId);
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
            }

            void ICharacterApi.OnCharacterSet(string result) {
                Debug.Log("character set current result " + characterId);
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
        }
    }
}
