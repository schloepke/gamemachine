using UnityEngine;
using System.Collections;
using GameMachine.Core;
using Character = io.gamemachine.messages.Character;
using TrackData = io.gamemachine.messages.TrackData;
using UserDefinedData = io.gamemachine.messages.UserDefinedData;

namespace GameMachine {
    namespace Common {

        public class GameEntity : MonoBehaviour, IGameEntity {

            public GameEntityType entityType = GameEntityType.None;
            public bool testPlayer = false;
            public string entityId;
            public Character character;
            public IGameEntityInput inputController;
            private NetworkFields networkFields;
            public Vector3 serverLocation;
            public float lastUpdate;
            public Transform model;
            public AnimationController animationController;
            private GameEntityController gameEntityController;
            public Transform spawnpoint;
            private Settings settings;
            private Util util;
            public float inactivityTimeout = 2f;

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
                settings = Settings.Instance();
                util = Util.Instance;
                gameEntityController = gameObject.GetComponent<GameEntityController>() as GameEntityController;
                inputController = gameObject.GetComponent<IGameEntityInput>() as IGameEntityInput;
                networkFields = gameObject.GetComponent<NetworkFields>() as NetworkFields;
            }

            void Start() {
                lastUpdate = Time.time;
            }

            void Update() {
                if (gameEntityController.started) {
                    gameEntityController.ManualUpdate();
                    if (!IsPlayer()) {
                        if (Time.time - lastUpdate > inactivityTimeout) {
                            GameEntityManager.RemoveGameEntity(entityId);
                            Remove();
                        }
                    }
                }
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

            public void Init(string entityId, Character character, GameEntityType entityType) {
                this.character = character;
                this.entityType = entityType;
                this.entityId = entityId;

                gameObject.name = character.id;
                gameObject.tag = Settings.Instance().gameEntityTag;

                model = transform.Find("model");
                animationController = model.GetComponent<AnimationController>();


                if (this.entityType == GameEntityType.Player) {
                    inputController.SetControllerType("local");
                } else {
                    inputController.SetControllerType("remote");
                }
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

            public string GetName() {
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

            public AnimationController GetAnimationController() {
                return animationController;
            }

            public Vector3 GetServerLocation() {
                return serverLocation;
            }
        }
    }
}
