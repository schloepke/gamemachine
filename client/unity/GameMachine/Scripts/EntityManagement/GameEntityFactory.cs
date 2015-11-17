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
                    return Create(GameEntityType.Player);
                } else {
                    if (NetworkSettings.instance.serverClient) {
                        return null;
                    } else {
                        return Create(NetworkSettings.instance.username, NetworkSettings.instance.character, GameEntityType.Player);
                    }
                }
            }

            public IGameEntity Create(string entityId) {
                throw new System.NotImplementedException();
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

            public IGameEntity Create(GameEntityType entityType) {
                Character character = new Character();
                character.id = Settings.Instance().defaultCharacterId;
                return Create(Settings.Instance().defaultEntityId, character, entityType);
            }

            public IGameEntity Create(string entityId, Character character, GameEntityType entityType) {
                Vector3 spawnPosition;
                SpawnPoint spawnPoint = SpawnPoint.Instance();
                if (character == null) {
                    Debug.Log("NULL character");
                }

                spawnPosition = SpawnPoint.Instance().SpawnpointExact();
                return Create(entityId, character, entityType, spawnPosition);
            }

            public IGameEntity Create(string entityId, Character character, TrackData trackData) {
                SpawnPoint spawnPoint = GameComponent.Get<SpawnPoint>() as SpawnPoint;
                Vector3 spawnPosition = GmUtil.Instance.TrackdataToVector3(trackData);
                spawnPosition = spawnPoint.GroundedPosition(spawnPosition);
                GameEntityType entityType = GameEntity.GameEntityTypeFromTrackData(trackData);
                return Create(entityId, character, entityType, spawnPosition);
            }

            public IGameEntity Create(string entityId, Character character, GameEntityType entityType, Vector3 spawnPosition) {
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

                go = GameObject.Instantiate(prefab);
                go.transform.parent = GetEntityContainer().transform;

                if (setSpawnPoint) {
                    go.transform.position = spawnPosition;
                    SpawnPoint.Instance().spawned = true;
                    Debug.Log("Spawned at " + spawnPosition);
                }
                
                GameEntity gameEntity = go.GetComponent<GameEntity>() as GameEntity;
                LoadUmaModel(gameEntity);
                gameEntity.Init(entityId, character, entityType);
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
