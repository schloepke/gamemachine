using UnityEngine;
using System.Collections;
using TrackData = io.gamemachine.messages.TrackData;
using Character = io.gamemachine.messages.Character;

namespace GameMachine {
    namespace Common {
        public class GameEntityFactory : MonoBehaviour, IGameEntityFactory {

            void Awake() {
               IGameEntity gameEntity = Create();
               GameEntityManager.SetPlayerEntity(gameEntity);
            }

            void Start() {
                
            }

            public IGameEntity Create() {
                if (NetworkSettings.instance == null) {
                    return Create(GameEntityType.Player);
                } else {
                    return Create(NetworkSettings.instance.username, NetworkSettings.instance.character, GameEntityType.Player);
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
                SpawnPoint spawnPoint = GameComponent.Get<SpawnPoint>() as SpawnPoint;
                if (character == null) {
                    Debug.Log("NULL character");
                }
                if (character.worldx != 0 && character.worldy != 0) {
                    float x;
                    float y;
                    float z;

                    x = Util.Instance.IntToFloat(character.worldx, true);
                    z = Util.Instance.IntToFloat(character.worldy, true);
                    y = Util.Instance.IntToFloat(character.worldz, true);

                    spawnPosition = spawnPoint.GroundedPosition(new Vector3(x, y, z));
                } else {
                    spawnPosition = spawnPoint.GetFirst();
                }

                return Create(entityId, character, entityType, spawnPosition);
            }

            public IGameEntity Create(string entityId, Character character, TrackData trackData) {
                SpawnPoint spawnPoint = GameComponent.Get<SpawnPoint>() as SpawnPoint;
                Vector3 spawnPosition = Util.Instance.TrackdataToVector3(trackData);
                spawnPosition = spawnPoint.GroundedPosition(spawnPosition);
                GameEntityType entityType = GameEntity.GameEntityTypeFromTrackData(trackData);
                return Create(entityId, character, entityType, spawnPosition);
            }

            public IGameEntity Create(string entityId, Character character, GameEntityType entityType, Vector3 spawnPosition) {
                GameObject go = GameComponent.Get<AssetLibrary>().Load("GameEntity");
                go.transform.parent = GetEntityContainer().transform;
                go.transform.position = spawnPosition;
                GameEntity gameEntity = go.GetComponent<GameEntity>() as GameEntity;
                gameEntity.Init(entityId, character, entityType);
                return gameEntity;
            }

        }
    }
}
