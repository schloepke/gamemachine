using UnityEngine;
using System;
using System.Linq;
using GameMachine;
using GameMachine.HttpApi;
using GameMachine.Core;
using Entity = io.gamemachine.messages.Entity;
using EchoTest = io.gamemachine.messages.EchoTest;
using System.Collections;
using System.Collections.Generic;
using TrackData = io.gamemachine.messages.TrackData;
using ClientData = io.gamemachine.messages.UserDefinedData;
using TrackDataResponse = io.gamemachine.messages.TrackDataResponse;
using Character = io.gamemachine.messages.Character;

namespace GameMachine {
    namespace Common {

        public class GameEntityManager : MonoBehaviour, GameMachine.Trackable, ICharacterApi {
            private static Dictionary<string, IGameEntity> entities = new Dictionary<string, IGameEntity>();
            private static Dictionary<string, CharacterData> characters = new Dictionary<string, CharacterData>();
            private Dictionary<int, string> shortIdToEntityId = new Dictionary<int, string>();
            private Dictionary<string, float> lastUpdate = new Dictionary<string, float>();
            private static IGameEntity playerEntity;
            private int badShortId = 0;
            private IGameEntityFactory gameEntityFactory;
            public float characterTimeout = 2f;

            public class CharacterData {
                public Character character;
                public float lastLoadAttempt;
                public bool loaded = false;
            }

            public static int GameEntityCount() {
                return entities.Count;
            }

            public static List<IGameEntity> GetGameEntities() {
                return entities.Values.ToList();
            }

            public static IGameEntity GetPlayerEntity() {
                return playerEntity;
            }

            public static void SetPlayerEntity(IGameEntity entity) {
                playerEntity = entity;
            }

            public static void RemoveGameEntity(string entityId) {
                entities.Remove(entityId);
            }

            void Start() {
                gameEntityFactory = (IGameEntityFactory)GameComponent.Get<IGameEntityFactory>("GameEntityFactory");
                EntityTracking.Register(this);
            }

            public void TrackDataReceived(List<TrackData> trackDatas) {
                IGameEntity entity;
                foreach (TrackData trackData in trackDatas) {

                    // Vehicles
                    if (trackData.entityType == TrackData.EntityType.SHIP) {
                        continue;
                    }

                    if (trackData.id == "") {
                        if (trackData.shortId > 0) {
                            if (shortIdToEntityId.ContainsKey(trackData.shortId)) {
                                string entityId = shortIdToEntityId[trackData.shortId];
                                if (entities.ContainsKey(entityId)) {
                                    entity = entities[entityId];
                                    entity.UpdateFromTrackData(trackData, true);
                                    lastUpdate[entityId] = Time.time;
                                }
                            } else {
                                //Logger.Debug ("Entity for shortId  " + trackData.shortId + " not found type=" + trackData.entityType);
                                badShortId = trackData.shortId;
                            }
                        } else {
                            Debug.Log("Null id with no shortId");
                        }
                    } else {
                        string entityId = trackData.id;
                        if (entityId == NetworkSettings.instance.username) {
                            continue;
                        }

                        if (!entities.ContainsKey(entityId)) {


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
                                    continue;
                                }
                                
                                entity = gameEntityFactory.Create(entityId, characterData.character, trackData);
                                entities[entityId] = entity;
                                shortIdToEntityId[trackData.shortId] = entityId;
                                lastUpdate[entityId] = Time.time;
                            }
                        }


                        entity = entities[entityId];
                        entity.UpdateFromTrackData(trackData, false);
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

            public TrackData UpdateTracking() {

                if (playerEntity == null) {
                    return null;
                }

                TrackData trackData = playerEntity.GetTrackData();
                trackData.id = NetworkSettings.instance.username;
                if (trackData.entityType == null) {
                    trackData.entityType = TrackData.EntityType.PLAYER;
                }

                if (badShortId != 0) {
                    trackData.getNeighbors = 2;
                    badShortId = 0;
                } else {
                    trackData.getNeighbors = 1;
                }

                trackData.id = NetworkSettings.instance.username;
                Entity entity = new Entity();
                entity.id = "0";
                entity.trackData = trackData;
                ActorSystem.Instance.client.SendEntity(entity);
                //trackData.getNeighbors = 2;
                //trackData.zone = ZoneHandler.instance.currentZoneNumber();
                return null;
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