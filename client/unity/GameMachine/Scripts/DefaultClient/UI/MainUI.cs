using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using GameMachine.Common;

namespace GameMachine {
    namespace ClientLib {
        public class MainUI : MonoBehaviour {
            public static MainUI instance;

            public GameObject account;
            public GameObject login;
            public GameObject password;
            public GameObject quit;

            void Awake() {
                instance = this;
            }

            void Start() {
                Show();
            }

            public void LoadLogin() {
                UIController.instance.LoadLogin();
            }

            public void LoadAccount() {
                UIController.instance.LoadAccount();
            }

            public void LoadPassword() {
                UIController.instance.LoadPassword();
            }

            public void QuitApp() {
                Application.Quit();
            }

            public void Show() {
                if (NetworkSettings.instance.loggedIn) {
                    account.SetActive(false);
                    login.SetActive(false);
                    password.SetActive(false);
                    quit.SetActive(false);
                } else {
                    account.SetActive(true);
                    login.SetActive(true);
                    password.SetActive(true);
                    quit.SetActive(false);
                }
            }

        }
    }
}
