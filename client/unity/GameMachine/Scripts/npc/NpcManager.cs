using UnityEngine;
using System.Collections;
using GameMachine.HttpApi;
using io.gamemachine.messages;
using System;
using GameMachine.Common;
using System.Collections.Generic;
using GameMachine.Core;
using ProtoBuf;
using System.IO;

namespace GameMachine {
    public class NpcManager : MonoBehaviour {

        public static NpcManager instance;

        public List<string> prefabNames = new List<string>();
        public int gameEntitiesPerPrefab = 100;
        public float awarenessRange = 100f;
        public float agroRange = 40f;
        public float groupUpdateInterval = 2f;
        public float rangedDistance = 20f;
        public float meleeDistance = 2f;
        public LayerMask hitMask;
        public LayerMask gameEntityLosMask;
        public LayerMask buildObjectLosMask;

        public bool createServerNpcs = false;
        public bool useNavmesh = true;
        public float npcUpdateInterval = 1f;
        public float reachedTargetLength = 1f;
        private List<IGameEntity> gameEntities = new List<IGameEntity>();
        private Dictionary<string, IGameEntity> characterIdToGameEntity = new Dictionary<string, IGameEntity>();
        private Dictionary<string, IGameEntity> entityIdToGameEntity = new Dictionary<string, IGameEntity>();
        private Dictionary<string, Queue<Character>> npcQueue = new Dictionary<string, Queue<Character>>();

        private static void Save(Characters characters) {
            MemoryStream stream = new MemoryStream();
            GmSerializer serializer = new GmSerializer();
            serializer.Serialize(stream, characters);
            PlayerPrefs.SetString("npcs", Convert.ToBase64String(stream.ToArray()));
            Debug.Log(characters.characters.Count + " npc characters saved");
        }

        private static Characters Load() {
            if (!PlayerPrefs.HasKey("npcs")) {
                return null;
            }
            byte[] bytes = Convert.FromBase64String(PlayerPrefs.GetString("npcs"));
            MemoryStream stream = new MemoryStream(bytes);
            GmSerializer serializer = new GmSerializer();
            return serializer.Deserialize(stream, new Characters(), typeof(Characters)) as Characters;
        }

        void Awake() {
            instance = this;
        }

        void Start() {
            if (createServerNpcs) {
                if (NetworkSettings.instance == null) {
                    gameObject.AddComponent<NetworkSettings>();
                    gameObject.AddComponent<CharacterApi>();
                }
                Invoke("CreateNpcs", 1f);
            } else {
                LoadNpcs();
            }
        }

        private void CreateNpcs() {
            Characters characters = new Characters();
            int createdCount = 0;
            int npcCount = prefabNames.Count * gameEntitiesPerPrefab;
            Queue<string> names = NpcName.GetNames(npcCount);

            foreach (string prefabName in prefabNames) {
                for (int i = 0; i < gameEntitiesPerPrefab; i++) {
                    string playerId = prefabName + i;
                    string characterId = names.Dequeue();

                    CharacterApi.instance.CreateNpc(playerId, characterId, Vitals.Template.NpcTemplate, prefabName, (Character character) => {
                        characters.characters.Add(character);
                        createdCount++;
                        if (createdCount == npcCount) {
                            Save(characters);
                        }
                    });
                }
            }
            Debug.Log("Creating  characters");

        }

        private void LoadNpcs() {
            Characters characters = Load();
            if (characters == null || characters.characters.Count == 0) {
                Debug.Log("Unable to load npc characters");
                return;
            }

            foreach (Character character in characters.characters) {
                if (!npcQueue.ContainsKey(character.gameEntityPrefab)) {
                    Queue<Character> queue = new Queue<Character>();
                    npcQueue[character.gameEntityPrefab] = queue;
                }
                npcQueue[character.gameEntityPrefab].Enqueue(character);
            }


            NpcGroup[] groups = GameObject.FindObjectsOfType<NpcGroup>();
            int npcOrder = 0;

            foreach (NpcGroup group in groups) {
                Character character = npcQueue[group.leaderDef.prefabName].Dequeue();
                CreateNpc(group, group.leaderDef, character, npcOrder);
                npcOrder++;

                foreach (NpcDef npcDef in group.memberDefs) {
                    for (int i = 0; i < npcDef.count; i++) {
                        character = npcQueue[npcDef.prefabName].Dequeue();
                        CreateNpc(group, npcDef, character, npcOrder);
                        npcOrder++;
                    }
                }
                group.InitFormation();
            }
        }

        private void CreateNpc(NpcGroup group, NpcDef npcDef, Character character, int order) {
            Vector3 spawnPosition = SpawnPoint.Instance().GetObjectSpawnPoint(gameObject);
            GameObject prefab = GameEntityFactory.instance.GetGameEntityPrefab(npcDef.prefabName);
            IGameEntity gameEntity = CreateGameEntity(character.playerId, character, spawnPosition, prefab);
            Npc npc = gameEntity.GetGameObject().AddComponent<Npc>() as Npc;
            npc.group = group;
            npc.gameEntity = gameEntity;

            if (order == 0) {
                group.SetLeader(npc);
            }
            npc.order = order;
            npc.SetNpcDef(npcDef);
            group.AddNpc(npc);
            gameEntities.Add(gameEntity);
            characterIdToGameEntity[character.id] = gameEntity;
            entityIdToGameEntity[gameEntity.GetEntityId()] = gameEntity;
        }

        private IGameEntity CreateGameEntity(string entityId, Character character, Vector3 spawnPosition, GameObject prefab) {
            IGameEntity gameEntity = GameEntityFactory.instance.CreateLocalNpc(entityId, character, spawnPosition, prefab);
            gameEntity.SetTargetPosition(spawnPosition);
            return gameEntity;
        }

        public IGameEntity GetEntityByCharacterId(string characterId) {
            if (characterIdToGameEntity.ContainsKey(characterId)) {
                return characterIdToGameEntity[characterId];
            } else {
                return null;
            }
        }

        public IGameEntity GetEntityById(string entityId) {
            if (entityIdToGameEntity.ContainsKey(entityId)) {
                return entityIdToGameEntity[entityId];
            } else {
                return null;
            }
        }

        public bool HasNpc(string characterId) {
            return characterIdToGameEntity.ContainsKey(characterId);
        }

        public void UpdateTracking() {
            AgentTrackData agentTrackData = new AgentTrackData();
            int count = 0;
            foreach (IGameEntity gameEntity in gameEntities) {
                TrackData trackData = gameEntity.GetTrackData();
                trackData.id = gameEntity.GetEntityId();
                trackData.entityType = TrackData.EntityType.Npc;
                agentTrackData.trackData.Add(trackData);
                count++;
                if (count > 30) {
                    SendTrackDatas(agentTrackData);
                    agentTrackData = new AgentTrackData();
                    count = 0;
                }
            }
            SendTrackDatas(agentTrackData);
        }

        private void SendTrackDatas(AgentTrackData agentTrackData) {
            if (agentTrackData.trackData.Count > 0) {
                Entity entity = new Entity();
                entity.id = "0";
                entity.agentTrackData = agentTrackData;

                if (ActorSystem.instance.client != null) {
                    ActorSystem.instance.client.SendEntity(entity);
                }
            }
        }

    }
}
