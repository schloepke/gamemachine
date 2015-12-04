using UnityEngine;
using GameMachine.HttpApi;
using GameMachine.Common;
using GameMachine.Core;
using io.gamemachine.messages;

namespace GameMachine {
    namespace DefaultClient {
        public class Client : MonoBehaviour, GameMachineApp, IPlayerApi, ILoginUI, IZoneApi {
            public static Client instance;
            public static bool connected = false;
            public bool setZone = false;
            private bool reconnecting = false;
            private Zones zones = null;
            public Zone zone = null;
            public bool spawnOnMainSceneLoad = false;

            void Awake() {
                instance = this;
                DontDestroyOnLoad(transform.gameObject);
            }

            void Start() {
                UIController.instance.SetClient(this);

                Login.SetGameMachineApp(this);
            }

            void Update() {

            }

            public void ReconnectToZone() {
                if (zones == null) {
                    Debug.Log("zones not loaded, ignoring zone change request");
                }
                ZoneApi.instance.SetZone(zone.name, this);
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
                Login login = gameObject.AddComponent<Login>() as Login;
                login.SetLoginUi(this);
                login.DoLogin();
            }

            public void OnLoggedIn() {
                Debug.Log("Logged in");
                NetworkSettings.instance.loggedIn = true;

                ZoneApi.instance.GetZones(this);

                if (!reconnecting) {
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
                    if (spawnOnMainSceneLoad) {
                        Vector3 spawnPosition = SpawnPoint.Instance().SpawnpointExact(true);
                        Transform player = GamePlayer.Instance().playerTransform;
                        player.position = spawnPosition;

                        SpawnPoint.Instance().spawned = true;
                        Debug.Log("Player spawned at " + player.position);
                    }
                }
            }

            void IPlayerApi.OnPlayerCreated(Player player) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCreateError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayer(Player player) {
                if (player.role == Player.Role.Admin) {
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

            public void OnGetZones(Zones zones) {
                this.zones = zones;
                zone = zones.current;
                Debug.Log("Zones loaded");
            }

            public void OnGetZonesError(string error) {
                throw new System.NotImplementedException();
            }

            public void OnSetZone(Zone zone) {
                NetworkSettings.instance.character.region = zone.region;
                NetworkSettings.instance.character.zone = zone;
               
                if (string.IsNullOrEmpty(zone.hostname)) {
                    Debug.Log("Zone hostname is null");
                    return;
                }

                if (NetworkSettings.instance.sameAddress(zone.hostname,NetworkSettings.instance.hostname)) {
                    ZoneApi.instance.GetZones(this);
                } else {
                    NetworkSettings.instance.hostname = zone.hostname;
                    Reconnect();
                }

            }

            public void OnSetZoneError(string error) {
                Debug.Log("SetZone error " + error);
            }
        }
    }
}
