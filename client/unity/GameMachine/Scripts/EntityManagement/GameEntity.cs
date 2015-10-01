using UnityEngine;
using System.Collections;
using GameMachine.Core;
using Character = io.gamemachine.messages.Character;
using TrackData = io.gamemachine.messages.TrackData;
using UserDefinedData = io.gamemachine.messages.UserDefinedData;

namespace GameMachine {
    namespace Common {

        public class GameEntity : MonoBehaviour, IGameEntity {

            private GameEntityType entityType = GameEntityType.None;
            private string entityId;
            private Character character;
            private IGameEntityInput inputController;
            private NetworkFields networkFields;
            private Vector3 serverLocation;
            private float lastUpdate;
            private Transform model;
            private IGameEntityController gameEntityController;
            private Transform spawnpoint;
            public float inactivityTimeout = 2f;
            private bool canMove = true;
            private Vector3 networkSpawnPoint;

            public static GameEntityType GameEntityTypeFromTrackData(TrackData trackData) {
                GameEntityType entityType;
                switch (trackData.entityType) {
                    case TrackData.EntityType.PLAYER:
                        entityType = GameEntityType.Player;
                        break;
                    case TrackData.EntityType.NPC:
                        entityType = GameEntityType.Npc;
                        break;
                    case TrackData.EntityType.SHIP:
                        entityType = GameEntityType.Vehicle;
                        break;
                    case TrackData.EntityType.OTHER:
                        entityType = GameEntityType.Other;
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
                networkFields = gameObject.GetComponent<NetworkFields>() as NetworkFields;
            }

            void Start() {
                lastUpdate = Time.time;
            }

            void Update() {
                if (gameEntityController.IsInitialized() && canMove) {
                    gameEntityController.ManualUpdate();
                    if (!IsPlayer()) {
                        if (Time.time - lastUpdate > inactivityTimeout) {
                            GameEntityManager.RemoveGameEntity(entityId);
                            Remove();
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

            private void SetSpawnPoint(Character character) {
                if (character.worldx != 0 && character.worldy != 0) {
                    float x = Util.Instance.IntToFloat(character.worldx, true);
                    float z = Util.Instance.IntToFloat(character.worldy, true);
                    float y = Util.Instance.IntToFloat(character.worldz, true);
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

                networkFields.SetVector3(GMKeyCode.Position, transform.position);
                networkFields.SetFloat(GMKeyCode.Heading, heading);
                networkFields.SetFloat(GMKeyCode.Vaxis, inputController.GetFloat(GMKeyCode.Vaxis));
                networkFields.SetFloat(GMKeyCode.Haxis, inputController.GetFloat(GMKeyCode.Haxis));
                networkFields.SetBool(GMKeyCode.MouseRight, inputController.GetBool(GMKeyCode.MouseRight));

                TrackData trackData = networkFields.GetTrackData();

                trackData.entityType = TrackData.EntityType.PLAYER;
                return trackData;
            }

            public void UpdateFromTrackData(TrackData trackData, bool hasDelta) {
                if (entityType == GameEntityType.None) {
                    entityType = GameEntityTypeFromTrackData(trackData);
                }

                networkFields.UpdateFromNetwork(trackData, hasDelta);

                serverLocation = networkFields.GetVector3(GMKeyCode.Position);

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
                return networkFields;
            }


            public void SetMovementState(int state) {
                if (state == 0) {
                    canMove = false;
                } else {
                    canMove = true;
                }
            }



            public void FreezeMovement(float duration) {
                canMove = false;
                Invoke("UnFreezeMovement", duration);
            }

            private void UnFreezeMovement() {
                canMove = true;
            }
        }
    }
}
