using UnityEngine;
using GameMachine.HttpApi;
using GameMachine.Common;
using GameMachine.Core;
using io.gamemachine.messages;

namespace GameMachine {
    namespace ClientLib {
        public class DefaultClient : MonoBehaviour, GameMachineApp, IPlayerApi, ILoginUI, IZoneApi {
            public static DefaultClient instance;
            public static bool connected = false;
            private bool reconnecting = false;
            private Zones zones = null;
            private Zone zone = null;
            private App app;
            public string mainScene;
            private string startScene;

            [Header("auto create username/character using shared secret")]
            public bool autoLogin = false;

            [Header("Remove components not needed in server mode (client ui/camera/lighting/etc)")]
            public bool optimizeForServer = false;
            
            public static bool OptimizeForServer() {
                if (instance != null && instance.optimizeForServer) {
                    return true;
                } else {
                    return false;
                }
            }

            void Awake() {
                instance = this;
                startScene = Application.loadedLevelName;
                DontDestroyOnLoad(transform.gameObject);
            }

            void Start() {
                UIController.instance.SetClient(this);

                Login.SetGameMachineApp(this);

                if (autoLogin) {
                    UIController.instance.GetLogin().DoLogin();
                }
            }

            public Zone GetZone() {
                return zone;
            }

            public void ReconnectToZone() {
                if (zones == null) {
                    Debug.Log("zones not loaded, ignoring zone change request");
                    return;
                }
                ConnectToZone(zone.name);
               
            }

            public void ConnectToZone(string name) {
                ZoneApi.instance.SetZone(zone.name, (newZone) => {
                    OnSetZone(newZone);
                });
            }

            public void Reconnect() {
                app.client.Stop();
                ActorSystem.instance.client = null;
                Destroy(gameObject.GetComponent<Login>());
                Destroy(gameObject.GetComponent<App>());
                reconnecting = true;
                Invoke("DoReconnect", 1f);

            }

            void DoReconnect() {
                Login.SetGameMachineApp(this);
                Login login = gameObject.AddComponent<Login>() as Login;
                login.SetLoginUi(this);
                login.DoLogin();
            }

            public void OnLoggedIn() {
                //Debug.Log("Logged in");
                app = gameObject.GetComponent<App>() as App;
                NetworkSettings.instance.loggedIn = true;

                ZoneApi.instance.GetZones(this);

                if (!reconnecting) {
                    PlayerApi.instance.GetPlayer(this);
                }
                reconnecting = false;
            }

            public void OnLoginFailure(string error) {
                Debug.Log("Login Failed: " + error);
                if (optimizeForServer) {
                    Invoke("Shutdown", 2f);
                }
                
            }

            public void ConnectionTimeout() {
                Debug.Log("Connection timed out");
                connected = false;
                if (optimizeForServer) {
                    Invoke("Shutdown", 2f);
                }
            }

            public void ConnectionEstablished() {
                //Debug.Log("Connection established");
                connected = true;
            }

            private void OnLevelWasLoaded(int level) {
                if (Application.loadedLevelName == mainScene) {
                }
            }

            private void Shutdown() {
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
            void IPlayerApi.OnPlayerCreated(Player player) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCreateError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayer(Player player) {
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

            public void OnGetZones(Zones zones) {
                this.zones = zones;
                zone = zones.current;
                //Debug.Log("Zones loaded");
            }

            public void OnGetZonesError(string error) {
                throw new System.NotImplementedException();
            }

            public void OnSetZone(Zone zone) {
                if (zone == null) {
                    Debug.Log("Zone is null");
                    return;
                }

                NetworkSettings.instance.character.region = zone.region;
                NetworkSettings.instance.character.zone = zone;
               
                if (string.IsNullOrEmpty(zone.hostname)) {
                    Debug.Log("Zone hostname is null");
                    return;
                }

                if (NetworkSettings.instance.sameAddress(zone.hostname,NetworkSettings.instance.hostname)) {
                    Debug.Log("Zone changed, same hostname");
                    ZoneApi.instance.GetZones(this);
                } else {
                    NetworkSettings.instance.hostname = zone.hostname;
                    Debug.Log("Zone changed, hostname is different reconnecting");
                    Reconnect();
                }

            }

            public void OnSetZoneError(string error) {
                Debug.Log("SetZone error " + error);
            }
        }
    }
}
