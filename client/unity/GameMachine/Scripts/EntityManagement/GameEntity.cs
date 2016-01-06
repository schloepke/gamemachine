using UnityEngine;
using io.gamemachine.messages;
using System;
using GameMachine.ClientLib;

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
            public string entityId;
            private Character character;
            private IGameEntityInput inputController;
            private Vector3 targetPosition;
            private float lastUpdate;
            private GameEntityController gameEntityController;
            private Transform spawnpoint;
            public float inactivityTimeout = 2f;
            private Vector3 networkSpawnPoint;
            private Vitals vitals = null;
            private bool active = false;
            private ControllerType controllerType;
            private float lastHeading;
            public int shortId;

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
                gameEntityController = gameObject.GetComponent<GameEntityController>() as GameEntityController;
                inputController = gameObject.GetComponent<IGameEntityInput>() as IGameEntityInput;
                networkFieldsDb.GetData();
                networkFieldsDef = networkFieldsDb.Clone();
                networkFieldsDef.Init();
                
            }

            void Start() {
                lastUpdate = Time.time;
            }

            void Update() {
                if (gameEntityController.IsInitialized()) {
                    gameEntityController.ManualUpdate();
                    if (!IsPlayer() && !IsLocalController()) {
                        UpdateHealthbar();
                        if (Time.time - lastUpdate > inactivityTimeout) {
                            GameEntityManager.instance.RemoveGameEntity(entityId, character.id, shortId);
                        }
                    }
                }

            }

            public GameEntityController GetGameEntityController() {
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
                if (healthbarHealth == null) {
                    return;
                }

                if (healthbarHealth.enabled) {
                    healthbar.transform.LookAt(healthbar.transform.position + Camera.main.transform.rotation * Vector3.forward, Camera.main.transform.rotation * Vector3.up);
                }
            }

            public void SetHealthbar(bool active, float scale) {
                if (healthbarHealth == null) {
                    return;
                }

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

            public void Init(string entityId, Character character, GameEntityType entityType, ControllerType controllerType) {
                this.character = character;
                this.entityType = entityType;
                this.entityId = entityId;
                this.controllerType = controllerType;

                gameObject.name = character.id;
                gameObject.tag = Settings.Instance().gameEntityTag;

                inputController.SetControllerType(controllerType);
                if (controllerType == ControllerType.Remote) {
                    gameObject.layer = LayerMask.NameToLayer(Settings.Instance().gameEntityTag);
                    AddHealthbar();
                }
                
                networkSpawnPoint = SpawnPoint.Instance().GetCharacterSpawnpoint(character);

                if (DefaultClient.OptimizeForServer()) {
                    Destroy(gameObject.GetComponentInChildren<SkinnedMeshRenderer>());
                }
            }

            public void Remove() {
                Destroy(gameObject);
            }

            public TrackData GetTrackData() {
                gameEntityController.SetNetworkFields(networkFieldsDef);

                TrackData trackData = networkFieldsDef.GetTrackData();

                trackData.entityType = TrackData.EntityType.Player;
                return trackData;
            }

            public void UpdateFromTrackData(TrackData trackData, bool hasDelta) {
                if (entityType == GameEntityType.None) {
                    entityType = GameEntityTypeFromTrackData(trackData);
                }

                networkFieldsDef.UpdateFromNetwork(trackData, hasDelta);

                targetPosition = networkFieldsDef.GetPosition();

                lastUpdate = Time.time;
            }

            public Character GetCharacter() {
                return character;
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

            public Vector3 GetTargetPosition() {
                return targetPosition;
            }

            public void SetTargetPosition(Vector3 targetPosition) {
                this.targetPosition = targetPosition;
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
            
            public bool MovementDisabled() {
                return InputState.CameraDisabled();
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

            public void SetActive(bool active, bool setLocation) {
                this.active = active;
                if (setLocation) {
                    transform.position = GetTargetPosition();
                }
            }

            public GameObject GetGameObject() {
                return gameObject;
            }

            public bool IsLocalController() {
                return (controllerType == ControllerType.Local);
            }

            public ControllerType GetControllerType() {
                return controllerType;
            }

            public void SetShortId(int shortId) {
                this.shortId = shortId;
            }

            public void SetCharacter(Character character) {
                this.character = character;
            }
        }
    }
}
