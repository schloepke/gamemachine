using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using GameMachine;
using GameMachine.HttpApi;
using GameMachine.Common;
using GameMachine.Core;
using System.IO;
using ProtoBuf;
using System.Linq;
using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;
using Player = io.gamemachine.messages.Player;

namespace GameMachine {
    namespace ServerClient {
        public class Client : MonoBehaviour, GameMachineApp, IPlayerApi, ILoginUI {
            public static bool connected = false;
            private Login login;
            private GameMachine.Core.App app;
            

            void Awake() {
                Application.targetFrameRate = 60;
                DontDestroyOnLoad(transform.gameObject);
               
            }

            void Start() {
                Login.SetGameMachineApp(this);
                login = gameObject.AddComponent<GameMachine.Login>() as GameMachine.Login;
                login.SetLoginUi(this);
                login.DoLogin();
            }

            public void OnLoggedIn() {
                Debug.Log("Logged in");
                app = gameObject.GetComponent<GameMachine.Core.App>() as GameMachine.Core.App;
                NetworkSettings.instance.loggedIn = true;
                GameObject go = GameComponent.Get<AssetLibrary>().Load("GameMachine");
                go.name = "GameMachine";
                PlayerApi.instance.GetPlayer(this);
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
                GameObject go = GameObject.Find("GameMachine");
                if (go != null) {
                    Destroy(go);
                }
               
                if (app != null) {
                    app.client.Stop();
                }
                Destroy(this.gameObject);
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
                if (player.role == "admin") {
                    NetworkSettings.instance.isAdmin = true;
                }
               
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
