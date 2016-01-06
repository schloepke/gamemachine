using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Linq;
using GameMachine.Common;
using GameMachine.HttpApi;
using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;
using Player = io.gamemachine.messages.Player;
using System;

namespace GameMachine {
    namespace ClientLib {
        public class CharacterUI : MonoBehaviour, ICharacterApi {

            public void Load(string characterId) {
                SetError("");
                CreateButton().interactable = true;
                NameInput().text = "";
            }

            public void Create() {
                SetError("");
                CreateButton().interactable = false;
                CharacterApi.instance.CreateCharacter(NameInput().text, this);
            }

            private InputField NameInput() {
                return transform.Find("name_input").GetComponent<InputField>();
            }

            private Button CreateButton() {
                return transform.Find("create").GetComponent<Button>();
            }

            public void LoadCharacters() {
                UIController.instance.LoadCharacters();
            }

            private void SetError(string text) {
                transform.Find("error").Find("Text").GetComponent<Text>().text = text;
                CreateButton().interactable = true;
            }


            void ICharacterApi.OnCharacterCreated(Character character) {
                UIController.instance.LoadCharacters();
            }

            void ICharacterApi.OnCharacterCreateError(string error) {
                SetError(error);
            }

            void ICharacterApi.OnCharacterSet(string result) {
                throw new System.NotImplementedException();
            }

            void ICharacterApi.OnCharacterSetError(string error) {
                throw new System.NotImplementedException();
            }

            void ICharacterApi.OnCharacterDeleted(string characterId) {
            }

            void ICharacterApi.OnCharacterDeleteError(string error) {
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