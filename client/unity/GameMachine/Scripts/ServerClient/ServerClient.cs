using UnityEngine;
using GameMachine.HttpApi;
using GameMachine.Common;
using GameMachine.Core;
using io.gamemachine.messages;

namespace GameMachine {
    namespace Client {
        public class ServerClient : MonoBehaviour, GameMachineApp, IPlayerApi, ILoginUI {
            public static bool connected = false;

            public string startScene;
            public string mainScene;
            
            public GameObject masterGameObject;
            private Login login;
            private App app;

            void Awake() {
                Application.targetFrameRate = 400;
                DontDestroyOnLoad(transform.gameObject);
            }

            void Start() {
                Login.SetGameMachineApp(this);
                login = gameObject.AddComponent<Login>() as Login;
                login.SetLoginUi(this);
                login.DoLogin();
            }

            public void OnLoggedIn() {
                Debug.Log("Logged in");
                app = gameObject.GetComponent<App>() as App;
                NetworkSettings.instance.loggedIn = true;
                GameObject.Instantiate(masterGameObject);
                PlayerApi.instance.GetPlayer(this);

                if (!string.IsNullOrEmpty(mainScene)) {
                    Application.LoadLevel(mainScene);
                }
               
            }

            public void OnLoginFailure(string error) {
                Debug.Log("Login Failed: " + error);
                Shutdown();
            }

            public void ConnectionTimeout() {
                Debug.Log("Connection timed out");
                connected = false;
                Shutdown();

            }

            private void Shutdown() {
                GameObject go = GameObject.Find(masterGameObject.name);
                if (go != null) {
                    Destroy(go);
                }
               
                if (app != null) {
                    app.client.Stop();
                }
                
                if (string.IsNullOrEmpty(startScene)) {
                    Debug.Log("start scene is null");
                } else {
                    Destroy(this.gameObject);
                    Application.LoadLevel(startScene);
                }
               
            }


            public void ConnectionEstablished() {
                Debug.Log("Connection established");
                connected = true;
            }

            private void OnLevelWasLoaded(int level) {
               
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
                Debug.Log(error);
            }

            void IPlayerApi.OnPasswordChanged(string result) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPasswordError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCharacters(Characters characters) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCharactersError(string error) {
                throw new System.NotImplementedException();
            }

            public void SetError(string error) {
                Debug.Log("Login error " + error);
            }
        }
    }
}
