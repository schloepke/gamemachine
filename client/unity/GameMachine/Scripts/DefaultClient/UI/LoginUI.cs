using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using GameMachine.Common;
using GameMachine.HttpApi;

namespace GameMachine {
    namespace ClientLib {
        public class LoginUI : MonoBehaviour, ILoginUI {

            private Login login;
           
            public void Load() {
                SetLoginError("");
                LoginButton().interactable = true;
                UsernameInput().text = PlayerPrefs.GetString("username");
                PasswordInput().text = PlayerPrefs.GetString("password");
            }

            public void Dologin() {
                SetLoginError("");
                LoginButton().interactable = false;
                NetworkSettings.instance.username = UsernameInput().text;
                NetworkSettings.instance.password = PasswordInput().text;
                PlayerPrefs.SetString("username", NetworkSettings.instance.username);
                PlayerPrefs.SetString("password", NetworkSettings.instance.password);
                Login login = UIController.instance.GetLogin();
                if (login != null) {
                    login.DoLogin();
                }
                
            }

            

            public void SetError(string error) {
                Debug.Log(error);
                SetLoginError(error);
                LoginButton().interactable = true;
            }

            private InputField UsernameInput() {
                return transform.Find("username_input").GetComponent<InputField>();
            }

            private InputField PasswordInput() {
                return transform.Find("password_input").GetComponent<InputField>();
            }

            private Button LoginButton() {
                return transform.Find("login_button").GetComponent<Button>();
            }

            private void SetLoginError(string text) {
                transform.Find("login_error").Find("Text").GetComponent<Text>().text = text;
            }
        }
    }
}
