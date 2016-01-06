using UnityEngine;
using System;
using System.Linq;
using GameMachine.HttpApi;
using GameMachine.Core;
using System.Collections.Generic;
using io.gamemachine.messages;

namespace GameMachine {
    namespace Common {

        public class GameEntityManager : MonoBehaviour, ICharacterApi {

            public static GameEntityManager instance;

            private static Dictionary<string, IGameEntity> gameEntities = new Dictionary<string, IGameEntity>();
            private static Dictionary<string, CharacterData> characters = new Dictionary<string, CharacterData>();
            private Dictionary<int, string> shortIdToEntityId = new Dictionary<int, string>();
            private Dictionary<string, float> lastUpdate = new Dictionary<string, float>();
            private static Dictionary<string, string> characterToEntityId = new Dictionary<string, string>();
            private static IGameEntity playerEntity;
            private int badShortId = 0;
            private GameEntityFactory gameEntityFactory;
            public float characterTimeout = 2f;
            
            public delegate void OnPlayerCharacterLoaded(Character character);
            public OnPlayerCharacterLoaded onPlayerCharacterLoaded;

            public delegate void OnEntityLoaded(string entityId, Character character);
            public OnEntityLoaded onEntityLoaded;

            public class CharacterData {
                public Character character;
                public float lastLoadAttempt;
                public bool loaded = false;
            }

            public static int GameEntityCount() {
                return gameEntities.Values.Where(entity => entity.IsActive()).Count();
            }

            public static IEnumerable<IGameEntity> GetGameEntities() {
                return gameEntities.Values.Where(entity => entity.IsActive());
            }

            public static IGameEntity GetGameEntityByCharacterId(string characterId) {
                if (playerEntity.GetCharacterId() == characterId) {
                    return playerEntity;
                }

                if (characterToEntityId.ContainsKey(characterId)) {
                    string entityId = characterToEntityId[characterId];
                    return GetGameEntityById(entityId);
                } else {
                    if (NpcManager.instance != null) {
                        return NpcManager.instance.GetEntityByCharacterId(characterId);
                    } else {
                        return null;
                    }
                }
            }

            public static IGameEntity GetGameEntityById(string entityId) {
                if (playerEntity.GetEntityId() == entityId) {
                    return playerEntity;
                }

                if (gameEntities.ContainsKey(entityId)) {
                    IGameEntity entity = gameEntities[entityId];
                    if (entity.IsActive()) {
                        return gameEntities[entityId];
                    } else {
                        return null;
                    }
                } else {
                    if (NpcManager.instance != null) {
                        return NpcManager.instance.GetEntityById(entityId);
                    } else {
                        return null;
                    }
                }
            }

            public static IGameEntity GetPlayerEntity() {
                return playerEntity;
            }

            public static void SetPlayerEntity(IGameEntity entity) {
                playerEntity = entity;
            }

            public void RemoveGameEntity(string entityId, string characterId, int shortId) {
                IGameEntity entity = gameEntities[entityId];
                gameEntities.Remove(entityId);
                characters.Remove(characterId);
                characterToEntityId.Remove(characterId);
                shortIdToEntityId.Remove(shortId);
                Destroy(entity.GetGameObject());
                //Debug.Log("Remove " + entityId + " " + characterId);
            }

            void Awake() {
                instance = this;
            }

            void Start() {
                if (GamePlayer.IsNetworked()) {
                    gameEntityFactory = GameEntityFactory.instance;
                }
            }

            public void TrackDataReceived(List<TrackData> trackDatas) {
                IGameEntity entity;
                foreach (TrackData trackData in trackDatas) {
                    // Vehicles
                    if (trackData.entityType == TrackData.EntityType.Object) {
                        continue;
                    }


                    if (string.IsNullOrEmpty(trackData.id)) {
                        if (trackData.shortId > 0) {
                            if (shortIdToEntityId.ContainsKey(trackData.shortId)) {
                                string entityId = shortIdToEntityId[trackData.shortId];
                                if (gameEntities.ContainsKey(entityId)) {
                                    entity = gameEntities[entityId];
                                    entity.UpdateFromTrackData(trackData, true);
                                    lastUpdate[entityId] = Time.time;
                                } else {
                                    Debug.Log("Entity not found: " + entityId);
                                }
                            } else {
                                //Debug.Log("Entity for shortId  " + trackData.shortId + " not found type=" + trackData.entityType);
                                badShortId = trackData.shortId;
                            }
                        } else {
                            Debug.Log("Null entityId with no shortId");
                        }
                    } else {
                        string entityId = trackData.id;
                        if (entityId == NetworkSettings.instance.username) {
                            continue;
                        }

                        if (!gameEntities.ContainsKey(entityId)) {

                            if (NpcManager.instance != null) {
                                if (NpcManager.instance.GetEntityById(entityId) != null) {
                                    //Debug.Log("Got local npc in trackdata "+entityId);
                                    continue;
                                }
                            }

                            if (trackData.shortId == 0) {
                                Debug.Log("No short id for " + entityId);
                                continue;
                            }

                            if (trackData.characterId == "") {
                                Debug.Log("No character id found for " + entityId);
                                continue;
                            }

                            if (!characters.ContainsKey(trackData.characterId)) {
                                CharacterData characterData = new CharacterData();
                                characterData.lastLoadAttempt = Time.time;
                                characters[trackData.characterId] = characterData;
                                CharacterApi.instance.GetCharacter(entityId, trackData.characterId, this);
                                continue;
                            }

                            if (characters.ContainsKey(trackData.characterId)) {
                                CharacterData characterData = characters[trackData.characterId];

                                if (!characterData.loaded) {
                                    if (Time.time - characterData.lastLoadAttempt > characterTimeout) {
                                        characterData.lastLoadAttempt = Time.time;
                                        CharacterApi.instance.GetCharacter(entityId, trackData.characterId, this);
                                    }
                                    //Debug.Log("Character not loaded " + trackData.characterId);
                                    continue;
                                }
                                
                                entity = gameEntityFactory.CreateFromNetwork(entityId, characterData.character, trackData);
                                entity.SetShortId(trackData.shortId);
                                entity.SetActive(true,false);
                                gameEntities[entityId] = entity;
                                shortIdToEntityId[trackData.shortId] = entityId;
                                characterToEntityId[trackData.characterId] = entityId;

                                lastUpdate[entityId] = Time.time;

                                if (onPlayerCharacterLoaded != null) {
                                    if (entity.IsOtherPlayer() || entity.IsNpc()) {
                                        onPlayerCharacterLoaded(characterData.character);
                                    }
                                }

                                if (onEntityLoaded != null) {
                                    onEntityLoaded(entityId, characterData.character);
                                }
                            }
                        }


                        entity = gameEntities[entityId];

                        if (entity.IsActive()) {
                            entity.UpdateFromTrackData(trackData, false);
                        } else {
                            entity.GetGameObject().SetActive(true);
                            entity.UpdateFromTrackData(trackData, false);
                            entity.SetActive(true, true);
                        }

                        lastUpdate[entityId] = Time.time;
                    }
                }
            }

            void ICharacterApi.OnCharacterGet(string playerId, Character character) {
                if (character == null) {
                    Debug.Log("Null character from api request");
                }
                CharacterData characterData = characters[character.id];
                characterData.character = character;
                characterData.loaded = true;

                
            }

            void ICharacterApi.OnCharacterGetError(string playerId, string characterId, string error) {
                CharacterData characterData = characters[characterId];
                characterData.lastLoadAttempt = Time.time;
                CharacterApi.instance.GetCharacter(playerId, characterId, this);
            }

            public void UpdateTracking(bool getNeighbors) {
                if (playerEntity == null) {
                    return;
                }
                
                TrackData trackData = playerEntity.GetTrackData();
                trackData.id = NetworkSettings.instance.username;
                trackData.neighborEntityType = TrackData.EntityType.Any;
               
                if (getNeighbors) {
                    if (badShortId != 0) {
                        trackData.getNeighbors = 2;
                        badShortId = 0;
                    } else {
                        trackData.getNeighbors = 1;
                    }
                } else {
                    trackData.getNeighbors = 0;
                }
               

                trackData.id = NetworkSettings.instance.username;
                Entity entity = new Entity();
                entity.id = "0";
                entity.trackData = trackData;
                if (ActorSystem.instance.client != null) {
                    ActorSystem.instance.client.SendEntity(entity);
                }
               
                //trackData.getNeighbors = 2;
                //trackData.zone = ZoneHandler.instance.currentZoneNumber();
            }
            
            public void HandleTrackDataResponse(TrackDataResponse response) {
                Debug.Log("TrackDataResponse=" + response.reason);
                if (response.reason == TrackDataResponse.REASON.RESEND) {

                }
            }

            void ICharacterApi.OnCharacterCreated(Character character) {
                throw new NotImplementedException();
            }

            void ICharacterApi.OnCharacterCreateError(string error) {
                throw new NotImplementedException();
            }

            void ICharacterApi.OnCharacterSet(string result) {
                throw new NotImplementedException();
            }

            void ICharacterApi.OnCharacterSetError(string error) {
                throw new NotImplementedException();
            }

            void ICharacterApi.OnCharacterDeleted(string characterId) {
                throw new NotImplementedException();
            }

            void ICharacterApi.OnCharacterDeleteError(string error) {
                throw new NotImplementedException();
            }

        }
    }
}