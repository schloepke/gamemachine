using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using GameMachine.Common;
using GameMachine.HttpApi;

namespace GameMachine {
    namespace DefaultClient {
        public class LoginUI : MonoBehaviour, ILoginUI {

            private Login login;
            private Transform panel;
           
            void Start() {
                panel = transform.Find("panel");
                gameObject.GetComponent<Canvas>().enabled = false;
            }

            public void Load() {
                gameObject.GetComponent<Canvas>().enabled = true;
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
                UIController.instance.GetLogin().DoLogin();
            }

            

            public void SetError(string error) {
                Debug.Log(error);
                SetLoginError(error);
                LoginButton().interactable = true;
            }

            private InputField UsernameInput() {
                return panel.transform.Find("username_input").GetComponent<InputField>();
            }

            private InputField PasswordInput() {
                return panel.transform.Find("password_input").GetComponent<InputField>();
            }

            private Button LoginButton() {
                return panel.transform.Find("login_button").GetComponent<Button>();
            }

            private void SetLoginError(string text) {
                panel.transform.Find("login_error").Find("Text").GetComponent<Text>().text = text;
            }
        }
    }
}
