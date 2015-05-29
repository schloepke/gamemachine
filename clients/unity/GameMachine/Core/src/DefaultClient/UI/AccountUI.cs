using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using GameMachine.Common;
using GameMachine.Api;
using Player = io.gamemachine.messages.Player;

namespace GameMachine {
    namespace DefaultClient {
        public class AccountUI : MonoBehaviour, IPlayerApi {
            private Transform panel;

            void Start() {
                panel = transform.Find("panel");
                gameObject.GetComponent<Canvas>().enabled = false;
            }

            public void Load() {
                gameObject.GetComponent<Canvas>().enabled = true;
                SetError("");
                CreateButton().interactable = true;
            }

            public void CreateAccount() {
                SetError("");
                CreateButton().interactable = false;
                PlayerApi.instance.CreatePlayer(UsernameInput().text, PasswordInput().text, this);
            }

            private InputField UsernameInput() {
                return panel.transform.Find("username_input").GetComponent<InputField>();
            }

            private InputField PasswordInput() {
                return panel.transform.Find("password_input").GetComponent<InputField>();
            }

            private Button CreateButton() {
                return panel.transform.Find("create").GetComponent<Button>();
            }

            private void SetError(string text) {
                panel.transform.Find("error").Find("Text").GetComponent<Text>().text = text;
            }



            void IPlayerApi.OnPlayerCreated(Player player) {
                CreateButton().interactable = true;
                if (player.id == "exists") {
                    SetError("Username is already taken");
                } else {
                    CreateButton().interactable = true;
                    UIController.instance.LoadMain();
                    UIController.instance.Notice("Player " + player.id + " Created", Color.green);
                }
            }

            void IPlayerApi.OnPlayerCreateError(string error) {
                SetError(error);
                CreateButton().interactable = true;
            }

            void IPlayerApi.OnPlayerCharacters(io.gamemachine.messages.Characters characters) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCharactersError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayer(Player player) {
                throw new System.NotImplementedException();
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
        }
    }
}
