using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Linq;
using GameMachine.Common;
using GameMachine.HttpApi;
using Player = io.gamemachine.messages.Player;

namespace GameMachine {
    namespace ClientLib {
        public class PasswordUI : MonoBehaviour, IPlayerApi {

            public void Load() {
                SetError("");
                ChangeButton().interactable = true;
            }

            public void ChangePassword() {
                SetError("");
                ChangeButton().interactable = false;
                PlayerApi.instance.ChangePassword(UsernameInput().text, PasswordInput().text, NewPasswordInput().text, this);
            }

            private InputField UsernameInput() {
                return transform.Find("username_input").GetComponent<InputField>();
            }

            private InputField PasswordInput() {
                return transform.Find("password_input").GetComponent<InputField>();
            }

            private InputField NewPasswordInput() {
                return transform.Find("new_password_input").GetComponent<InputField>();
            }

            private Button ChangeButton() {
                return transform.Find("change_password").GetComponent<Button>();
            }

            private void SetError(string text) {
                transform.Find("error").Find("Text").GetComponent<Text>().text = text;
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
