using UnityEngine;
using io.gamemachine.messages;
using System;

namespace GameMachine {
    namespace Common {

        public class GameEntity : MonoBehaviour, IGameEntity {

            public NetworkFieldsDb networkFieldsDb;
            public GameObject healthbarTemplate;
            private GameObject healthbar;
            private MeshRenderer healthbarHealth;
            private MeshRenderer healthbarBg;

            private NetworkFields networkFieldsDef;

            private GameEntityType entityType = GameEntityType.None;
            private string entityId;
            private Character character;
            private IGameEntityInput inputController;
            private Vector3 serverLocation;
            private float lastUpdate;
            private IGameEntityController gameEntityController;
            private Transform spawnpoint;
            public float inactivityTimeout = 2f;
            private bool canMove = true;
            private Vector3 networkSpawnPoint;
            private Vitals vitals = null;
            private bool active = false;

            public static GameEntityType GameEntityTypeFromTrackData(TrackData trackData) {
                GameEntityType entityType;
                switch (trackData.entityType) {
                    case TrackData.EntityType.Player:
                        entityType = GameEntityType.Player;
                        break;
                    case TrackData.EntityType.Npc:
                        entityType = GameEntityType.Npc;
                        break;
                    case TrackData.EntityType.Object:
                        entityType = GameEntityType.Vehicle;
                        break;
                    default:
                        entityType = GameEntityType.Other;
                        break;
                }

                if (entityType == GameEntityType.Player && trackData.id != NetworkSettings.instance.username) {
                    entityType = GameEntityType.OtherPlayer;
                }

                return entityType;
            }

            public static bool IsGameEntity(GameObject go) {
                return (go.tag == Settings.Instance().gameEntityTag);
            }

            public static IGameEntity FromGameObject(GameObject go) {
                return (IGameEntity)go.GetComponent(typeof(IGameEntity));
            }


            void Awake() {
                gameEntityController = gameObject.GetComponent<IGameEntityController>() as IGameEntityController;
                inputController = gameObject.GetComponent<IGameEntityInput>() as IGameEntityInput;
                networkFieldsDb.GetData();
                networkFieldsDef = networkFieldsDb.Clone();
                networkFieldsDef.Init();
                
            }

            void Start() {
                lastUpdate = Time.time;
            }

            void Update() {
                if (gameEntityController.IsInitialized() && canMove) {
                    gameEntityController.ManualUpdate();
                    if (!IsPlayer()) {
                        UpdateHealthbar();
                        if (Time.time - lastUpdate > inactivityTimeout) {
                            GameEntityManager.RemoveGameEntity(entityId);
                        }
                    }
                }
            }

            public IGameEntityController GetGameEntityController() {
                return gameEntityController;
            }

            public GameEntityType GetGameEntityType() {
                return entityType;
            }

            public bool IsPlayer() {
                return (entityType == GameEntityType.Player);
            }

            public bool IsNpc() {
                return (entityType == GameEntityType.Npc);
            }

            public bool IsOtherPlayer() {
                return (entityType == GameEntityType.OtherPlayer);
            }

            public Vector3 GetSpawnPoint() {
                return networkSpawnPoint;
            }

            private void UpdateHealthbar() {
                if (healthbarHealth.enabled) {
                    Camera cam = GameEntityCamera.instance.cam;
                    healthbar.transform.LookAt(healthbar.transform.position + cam.transform.rotation * Vector3.forward, cam.transform.rotation * Vector3.up);
                }
            }

            public void SetHealthbar(bool active, float scale) {
                healthbarHealth.enabled = active;
                healthbarBg.enabled = active;
                if (active) {
                    Transform t = healthbarHealth.transform;
                    t.localScale = new Vector3(scale, t.localScale.y, t.localScale.z);
                }
            }

            private void AddHealthbar() {
                healthbar = GameObject.Instantiate(healthbarTemplate, transform.position, transform.rotation) as GameObject;
                healthbar.name = "healthbar";
                healthbar.transform.position = new Vector3(transform.position.x, transform.position.y + 2f, transform.position.z);
                healthbar.transform.parent = transform;
                healthbarHealth = healthbar.transform.Find("health").GetComponent<MeshRenderer>() as MeshRenderer;
                healthbarBg = healthbar.transform.Find("background").GetComponent<MeshRenderer>() as MeshRenderer;
                SetHealthbar(false, 0f);
            }

            private void SetSpawnPoint(Character character) {
                if (character.worldx != 0 && character.worldz != 0) {
                    float x = GmUtil.Instance.IntToFloat(character.worldx);
                    float y = GmUtil.Instance.IntToFloat(character.worldy);
                    float z = GmUtil.Instance.IntToFloat(character.worldz);
                    networkSpawnPoint = new Vector3(x, y, z);
                }
            }

            public void Init(string entityId, Character character, GameEntityType entityType) {
                this.character = character;
                this.entityType = entityType;
                this.entityId = entityId;

                gameObject.name = character.id;
                gameObject.tag = Settings.Instance().gameEntityTag;
                

                if (this.entityType == GameEntityType.Player) {
                    inputController.SetControllerType("local");
                } else {
                    inputController.SetControllerType("remote");
                    gameObject.layer = LayerMask.NameToLayer(Settings.Instance().gameEntityTag);
                    AddHealthbar();
                }

                SetSpawnPoint(character);
            }

            public void Remove() {
                Destroy(gameObject);
            }

            public TrackData GetTrackData() {

                Vector3 forward = transform.forward;
                forward.y = 0;
                float heading = Quaternion.LookRotation(forward).eulerAngles.y;

                networkFieldsDef.SetPosition(transform.position);
                networkFieldsDef.SetFloat(GMKeyCode.Heading, heading);
                networkFieldsDef.SetFloat(GMKeyCode.Vaxis, inputController.GetFloat(GMKeyCode.Vaxis));
                networkFieldsDef.SetFloat(GMKeyCode.Haxis, inputController.GetFloat(GMKeyCode.Haxis));
                networkFieldsDef.SetBool(GMKeyCode.MouseRight, inputController.GetBool(GMKeyCode.MouseRight));

                TrackData trackData = networkFieldsDef.GetTrackData();

                trackData.entityType = TrackData.EntityType.Player;
                return trackData;
            }

            public void UpdateFromTrackData(TrackData trackData, bool hasDelta) {
                if (entityType == GameEntityType.None) {
                    entityType = GameEntityTypeFromTrackData(trackData);
                }

                networkFieldsDef.UpdateFromNetwork(trackData, hasDelta);

                serverLocation = networkFieldsDef.GetPosition();

                lastUpdate = Time.time;
            }

            public string GetCharacterId() {
                return character.id;
            }

            public string GetEntityId() {
                return entityId;
            }
            public Transform GetTransform() {
                return transform;
            }


            public void ResetTrackData() {
                throw new System.NotImplementedException();
            }

            public Vector3 GetServerLocation() {
                return serverLocation;
            }


            public NetworkFields GetNetworkFields() {
                return networkFieldsDef;
            }

            public bool IsInCombat() {
                if (vitals == null) {
                    return false;
                }
                return vitals.inCombat;
            }

            public bool IsDead() {
                if (vitals == null) {
                    return false;
                }
                return (vitals.dead == 1);
            }

            public void SetVitals(Vitals vitals) {
                this.vitals = vitals;
            }

            public Vitals GetVitals() {
                return vitals;
            }

            public bool HasVitals() {
                return (vitals != null);
            }

            public bool IsActive() {
                return active;
            }

            public void SetActive(bool active) {
                this.active = active;
            }

            public GameObject GetGameObject() {
                return gameObject;
            }
        }
    }
}
