using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using GameMachine.Common;

namespace GameMachine {
    namespace DefaultClient {
        public class MainUI : MonoBehaviour {
            public static MainUI instance;
            private Transform panel;
            
            void Awake() {
                instance = this;
            }

            void Start() {
                panel = transform.Find("panel");
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

            public void LoadAdmin() {
                UIController.instance.LoadAdmin();
            }

            public void QuitApp() {
                Application.Quit();
            }

            public void Show() {
                if (NetworkSettings.instance.loggedIn) {
                    panel.Find("account").gameObject.SetActive(false);
                    panel.Find("login").gameObject.SetActive(false);
                    panel.Find("password").gameObject.SetActive(false);
                    panel.Find("quit").gameObject.SetActive(true);
                    if (NetworkSettings.instance.isAdmin) {
                        panel.Find("admin").gameObject.SetActive(true);
                    }
                } else {
                    panel.Find("account").gameObject.SetActive(true);
                    panel.Find("login").gameObject.SetActive(true);
                    panel.Find("quit").gameObject.SetActive(true);
                    panel.Find("password").gameObject.SetActive(true);
                    panel.Find("admin").gameObject.SetActive(false);
                }
            }

            

            public void Load() {
                gameObject.GetComponent<Canvas>().enabled = true;
            }
        }
    }
}
