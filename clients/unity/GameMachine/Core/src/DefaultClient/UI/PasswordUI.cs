using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Linq;
using GameMachine.Common;
using GameMachine.Api;
using Player = io.gamemachine.messages.Player;

namespace GameMachine {
    namespace DefaultClient {
        public class PasswordUI : MonoBehaviour, IPlayerApi {
            private Transform panel;

            void Start() {
                panel = transform.Find("panel");
                gameObject.GetComponent<Canvas>().enabled = false;
            }

            public void Load() {
                gameObject.GetComponent<Canvas>().enabled = true;
                SetError("");
                ChangeButton().interactable = true;
            }

            public void ChangePassword() {
                SetError("");
                ChangeButton().interactable = false;
                PlayerApi.instance.ChangePassword(UsernameInput().text, PasswordInput().text, NewPasswordInput().text, this);
            }

            private InputField UsernameInput() {
                return panel.transform.Find("username_input").GetComponent<InputField>();
            }

            private InputField PasswordInput() {
                return panel.transform.Find("password_input").GetComponent<InputField>();
            }

            private InputField NewPasswordInput() {
                return panel.transform.Find("new_password_input").GetComponent<InputField>();
            }

            private Button ChangeButton() {
                return panel.transform.Find("change_password").GetComponent<Button>();
            }

            private void SetError(string text) {
                panel.transform.Find("error").Find("Text").GetComponent<Text>().text = text;
            }

            void IPlayerApi.OnPasswordChanged(string result) {
                ChangeButton().interactable = true;
                if (result == "OK") {
                    SetError("Password changed");
                } else {
                    SetError("Error: "+result);
                }
            }

            void IPlayerApi.OnPasswordError(string error) {
                ChangeButton().interactable = true;
                SetError("Error changing password");
            }

            void IPlayerApi.OnPlayerCreated(Player player) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCreateError(string error) {
                throw new System.NotImplementedException();
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
        }
    }
}
