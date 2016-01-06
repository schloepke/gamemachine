using UnityEngine;
using io.gamemachine.messages;
using System;
using System.Collections.Generic;
using System.Linq;

namespace GameMachine {
    namespace Common {
        public class GameEntityFactory : MonoBehaviour {
            
            [Serializable]
            public class GameEntityPrefab {
                public string id;
                public GameObject prefab;
            }

            public static GameEntityFactory instance;

            public bool setSpawnPoint = false;
            public List<GameEntityPrefab> gameEntityPrefabs = new List<GameEntityPrefab>();

            void Awake() {
                instance = this;
                IGameEntity gameEntity = Create();
               if (gameEntity != null) {
                   GameEntityManager.SetPlayerEntity(gameEntity);
               }
            }
            
            public GameObject GetGameEntityPrefab(string id) {
                GameEntityPrefab  gameEntityPrefab = gameEntityPrefabs.Where(prefab => prefab.id == id).FirstOrDefault();
                if (gameEntityPrefab != null) {
                    return gameEntityPrefab.prefab;
                } else {
                    Debug.LogWarning("Unable to find game entity prefab " + id);
                    return null;
                }
            }

            public IGameEntity Create() {
                if (NetworkSettings.instance == null) {
                    return CreateLocal(GameEntityType.Player);
                } else {
                    if (NetworkSettings.instance.serverClient) {
                        return null;
                    } else {
                        Vector3 spawnPoint = SpawnPoint.Instance().GetPlayerSpawnpoint(NetworkSettings.instance.character);
                        return Create(NetworkSettings.instance.username, NetworkSettings.instance.character, GameEntityType.Player, spawnPoint, ControllerType.Local);
                    }
                }
            }

            private static GameObject GetEntityContainer() {
                GameObject container = GameObject.Find("game_entities");
                if (container == null) {
                    container = new GameObject();
                    container.name = "game_entities";
                    container.transform.position = Vector3.zero;
                    container.AddComponent<DoNotDestroy>();
                }
                return container;
            }

            private Vector3 GetSpawnpoint(GameEntityType entityType, Character character) {
                Vector3 spawnPoint;
                if (entityType == GameEntityType.Player) {
                    spawnPoint = SpawnPoint.Instance().GetPlayerSpawnpoint(character);
                } else {
                    spawnPoint = SpawnPoint.Instance().GetPlayerSpawnpoint(character);
                }
                return spawnPoint;
            }

            public IGameEntity CreateLocal(GameEntityType entityType) {
                Character character = new Character();
                character.id = Settings.Instance().defaultCharacterId;
                Vector3 spawnPoint = SpawnPoint.Instance().GetPlayerSpawnpoint(character);
                return Create(Settings.Instance().defaultEntityId, character, entityType, spawnPoint, ControllerType.Local);
            }

            public IGameEntity CreateLocalNpc(string entityId, Character character, Vector3 spawnPosition, GameObject prefab) {
                GameObject go = GameObject.Instantiate(prefab, spawnPosition, Quaternion.identity) as GameObject;
                SpawnPoint.Instance().spawned = true;

                go.transform.parent = GetEntityContainer().transform;

                GameEntity gameEntity = go.GetComponent<GameEntity>() as GameEntity;
                LoadUmaModel(gameEntity);
                gameEntity.Init(entityId, character, GameEntityType.Npc, ControllerType.Local);
                return gameEntity;
            }

            public IGameEntity CreateFromNetwork(string entityId, Character character, TrackData trackData) {
                Vector3 spawnPosition = GmUtil.TrackdataToVector3(trackData);
                GameEntityType entityType = GameEntity.GameEntityTypeFromTrackData(trackData);
                Vector3 spawnPoint = SpawnPoint.Instance().GetNpcSpawnpoint(spawnPosition);
                IGameEntity gameEntity = Create(entityId, character, entityType, spawnPoint, ControllerType.Remote);
                return gameEntity;
            }

            public IGameEntity Create(string entityId, Character character, GameEntityType entityType, Vector3 spawnPoint, ControllerType controllerType) {
                GameObject go;
                GameObject prefab = null;

                if (!string.IsNullOrEmpty(character.gameEntityPrefab)) {
                    prefab = GetGameEntityPrefab(character.gameEntityPrefab);
                } else {
                    if (gameEntityPrefabs.Count > 0) {
                        prefab = gameEntityPrefabs[0].prefab;
                    }
                }
                if (prefab == null) {
                    throw new UnityException("Unable to spawn gameentity, no prefab found");
                }

                
                go = GameObject.Instantiate(prefab, spawnPoint,Quaternion.identity) as GameObject;
                SpawnPoint.Instance().spawned = true;

                go.transform.parent = GetEntityContainer().transform;

                GameEntity gameEntity = go.GetComponent<GameEntity>() as GameEntity;
                LoadUmaModel(gameEntity);
                gameEntity.Init(entityId, character, entityType, controllerType);
                return gameEntity;
            }

            private GameObject LoadUmaModel(GameEntity gameEntity) {
                GameObject umaModel = GameObject.Find("UMAModel");
                if (umaModel == null) {
                    return null;
                }

                umaModel.name = "model";
                umaModel.transform.parent = gameEntity.GetTransform();
                umaModel.transform.localPosition = Vector3.zero;
                umaModel.transform.localRotation = Quaternion.identity;
                return umaModel.transform.Find("Root").gameObject;
            }

        }
    }
}
