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
    namespace DefaultClient {
        public class Client : MonoBehaviour, GameMachineApp, IPlayerApi {
            public static bool connected = false;

            void Awake() {
                DontDestroyOnLoad(transform.gameObject);
            }

            void Start() {
                UIController.instance.SetClient(this);

                Login.SetGameMachineApp(this);
            }

            public void OnLoggedIn() {
                Debug.Log("Logged in");
                NetworkSettings.instance.loggedIn = true;
                PlayerApi.instance.GetPlayer(this);
            }

            public void OnLoginFailure(string error) {
                Debug.Log("Login Failed: " + error);
            }

            public void ConnectionTimeout() {
                Debug.Log("Connection timed out");
                connected = false;
            }

            public void ConnectionEstablished() {
                Debug.Log("Connection established");
                connected = true;
            }

            private void OnLevelWasLoaded(int level) {
                if (Application.loadedLevelName == UIController.instance.mainScene) {
                }
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
                UIController.instance.LoadCharacters();
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
        }
    }
}
