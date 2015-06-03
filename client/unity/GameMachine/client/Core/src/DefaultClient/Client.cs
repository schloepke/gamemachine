using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
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
using ZoneInfo = io.gamemachine.messages.ZoneInfo;
using ZoneInfos = io.gamemachine.messages.ZoneInfos;

namespace GameMachine {
    namespace DefaultClient {
        public class Client : MonoBehaviour, GameMachineApp, IPlayerApi, ILoginUI, IZoneApi {
            public static Client instance;
            public static bool connected = false;
            public bool setZone = false;
            public string currentZone;
            public List<string> zones = new List<string>();
            private bool reconnecting = false;
            public ZoneInfos zoneInfos;
            public ZoneInfo zoneInfo;
            public bool forceReconnectOnZone = false;
            public bool zoneSupport = false;

            void Awake() {
                instance = this;
                DontDestroyOnLoad(transform.gameObject);
            }

            void Start() {
                UIController.instance.SetClient(this);

                Login.SetGameMachineApp(this);
            }

            void Update() {
                if (zoneSupport && setZone) {
                    setZone = false;
                    if (!string.IsNullOrEmpty(currentZone)) {
                        ReconnectToZone();
                    }
                    
                }
            }

            public void ReconnectToZone() {
                if (zoneInfos == null) {
                    Debug.Log("ZoneInfos not loaded, ignoring zone change request");
                }

                foreach (ZoneInfo info in zoneInfos.zoneInfo) {
                    if (info.id == currentZone) {
                        Debug.Log("Setting new zone to " + info.id);
                        ZoneApi.instance.SetZone(info.id, this);
                        return;
                    }
                }
            }

            public void Reconnect() {
                App app = gameObject.GetComponent<App>() as App;
                app.client.Stop();
                ActorSystem.instance.client = null;
                Destroy(gameObject.GetComponent<Login>());
                Destroy(gameObject.GetComponent<App>());
                reconnecting = true;
                Invoke("DoReconnect", 1f);
                
            }

            void DoReconnect() {
                Login.SetGameMachineApp(this);
                Login login = gameObject.AddComponent<GameMachine.Login>() as GameMachine.Login;
                login.SetLoginUi(this);
                login.DoLogin();
            }

            public void OnLoggedIn() {
                Debug.Log("Logged in");
                NetworkSettings.instance.loggedIn = true;

                if (zoneSupport) {
                    ZoneApi.instance.GetZones(this);
                }
                
                if (reconnecting) {

                } else {
                    PlayerApi.instance.GetPlayer(this);
                }
                reconnecting = false;
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

            public void SetError(string error) {
                Debug.Log("login error " + error);
            }

            public void OnGetZones(ZoneInfos infos) {
                zoneInfos = infos;
                zones.Clear();
                foreach (ZoneInfo info in zoneInfos.zoneInfo) {
                    zones.Add(info.id);
                    if (info.current) {
                        zoneInfo = info;
                        currentZone = info.id;
                    }
                }
                Debug.Log("Zones loaded");
            }

            public void OnGetZonesError(string error) {
                throw new System.NotImplementedException();
            }

            public void OnSetZone(ZoneInfo info) {
                NetworkSettings.instance.character.zone = info.number;
                if (info.hostname == NetworkSettings.instance.hostname) {
                    if (forceReconnectOnZone) {
                        Reconnect();
                    } else {
                        ZoneApi.instance.GetZones(this);
                    }
                } else {
                    NetworkSettings.instance.hostname = info.hostname;
                    Reconnect();
                }
                
            }

            public void OnSetZoneError(string error) {
                Debug.Log("SetZone error " + error);
            }
        }
    }
}
