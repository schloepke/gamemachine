using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using io.gamemachine.messages;

namespace GameMachine {
    namespace Common {
        public class SpawnPoint : MonoBehaviour {

            public enum SpawnType {
                Normal,
                FromLocalSaved,
                SpawnObject
            }


            public float heightOffset = 0.5f;
            public float randomRangeForObjectSpawn = 20f;
            public SpawnType spawnType;
            public bool spawned = false;
            public string undergroundLayer = "gm_placed_block";
            public LayerMask undergroundMask;

            public static SpawnPoint Instance() {
                return GameObject.FindObjectOfType<SpawnPoint>() as SpawnPoint;
            }
            
            void Start() {
                undergroundMask = LayerMask.GetMask(undergroundLayer);
                if (!GamePlayer.IsNetworked()) {
                    InvokeRepeating("SaveLocal", 1f, 5f);
                }
            }

            public void SaveLocal() {
                if (GamePlayer.Instance() == null) {
                    return;
                }

                if (!spawned) {
                    return;
                }

                Transform t = GamePlayer.Instance().playerTransform;
                if (t == null) {
                    return;
                }

                Vector3 position = t.position;
                PlayerPrefs.SetFloat("spawnx", position.x);
                PlayerPrefs.SetFloat("spawny", position.y);
                PlayerPrefs.SetFloat("spawnz", position.z);
            }

            private Vector3 GetSavedLocal() {
                float x = PlayerPrefs.GetFloat("spawnx");
                float y = PlayerPrefs.GetFloat("spawny");
                float z = PlayerPrefs.GetFloat("spawnz");
                return new Vector3(x, y, z);
            }

            public Vector3 GetObjectSpawnPoint() {
                GameObject spawnPoint = GameObject.Find("spawnpoint");
                return GetObjectSpawnPoint(spawnPoint);
            }
            

            public Vector3 GetObjectSpawnPoint(GameObject spawnPoint) {
                if (spawnPoint == null) {
                    return GmUtil.Infinity;
                }

                Vector3 randomizedPoint = new Vector3(
                    spawnPoint.transform.position.x + Random.Range(-randomRangeForObjectSpawn, randomRangeForObjectSpawn),
                    spawnPoint.transform.position.y,
                    spawnPoint.transform.position.z + Random.Range(-randomRangeForObjectSpawn, randomRangeForObjectSpawn)
                    );

                Vector3 groundedPosition = GroundedPosition(randomizedPoint, heightOffset);
                if (groundedPosition != GmUtil.Infinity) {
                    return groundedPosition;
                } else {
                    Debug.Log("Unable to find grounded position from spawnpoint, giving up");
                    return GmUtil.Infinity;
                }
            }

            private Vector3 GetSavedSpawnPoint() {
                return GetSavedLocal();
            }

            public Vector3 GetCharacterSpawnpoint(Character character) {
                if (character.worldx != 0 && character.worldz != 0) {
                    float x = GmUtil.IntToFloat(character.worldx);
                    float y = GmUtil.IntToFloat(character.worldy);
                    float z = GmUtil.IntToFloat(character.worldz);
                    return new Vector3(x, y, z);
                } else {
                    return GmUtil.Infinity;
                }
            }
            
            public void SpawnHome() {
                Vector3 spawnpoint = GetObjectSpawnPoint();
                spawnpoint = GroundedPosition(spawnpoint, heightOffset);
                if (spawnpoint != GmUtil.Infinity) {
                    GamePlayer.Instance().playerTransform.position = spawnpoint;
                }
            }

            private Vector3 OffsetY(Vector3 position, float offset) {
                return new Vector3(position.x, position.y + offset, position.z);
            }

            public Vector3 GetNpcSpawnpoint(Vector3 position) {
                return GroundedPosition(position, heightOffset);
            }

            public Vector3 GetPlayerSpawnpoint(Character character) {
                if (spawnType == SpawnType.SpawnObject) {
                    return GetObjectSpawnPoint();
                } else if (spawnType == SpawnType.FromLocalSaved) {
                    return GetSavedSpawnPoint();
                }

                Vector3 spawnpoint = GmUtil.Infinity;

                if (GamePlayer.IsNetworked()) {
                    Vector3 networkPos = GetCharacterSpawnpoint(character);
                    if (networkPos != GmUtil.Infinity) {
                        Vector3 terrainPos = GroundedPosition(networkPos, 0f);
                        if (terrainPos != GmUtil.Infinity) {
                            if (terrainPos.y >= networkPos.y) {
                                if (OnPlacedBlock(OffsetY(networkPos, 0.5f))) {
                                    return networkPos;
                                } else {
                                    Debug.Log("Under terrain but no placed block");
                                    return OffsetY(terrainPos, 0.5f);
                                }
                            } else {
                                return networkPos;
                            }
                        }
                    }
                } else {
                    spawnpoint = GetSavedSpawnPoint();
                    spawnpoint = GroundedPosition(spawnpoint, heightOffset);
                    if (spawnpoint != GmUtil.Infinity) {
                        return spawnpoint;
                    }
                }

                spawnpoint = GetObjectSpawnPoint();
                spawnpoint = GroundedPosition(spawnpoint, heightOffset);
                if (spawnpoint != GmUtil.Infinity) {
                    return spawnpoint;
                }

                return spawnpoint;
            }

            private bool OnPlacedBlock(Vector3 position) {
                float distance = 4f;
                if (Physics.Raycast(position, Vector3.down, distance, undergroundMask)) {
                    return true;
                } else {
                    return false;
                }
            }

            public Vector3 GroundedPosition(Vector3 position) {
                return GroundedPosition(position, heightOffset);
            }

            private Vector3 GroundedPosition(Vector3 position, float offset) {
                Vector3 start = new Vector3(position.x, 900f, position.z);
                RaycastHit[] hits;
                hits = Physics.RaycastAll(start, Vector3.down, 1000f);
                foreach (RaycastHit hit in hits) {
                    if (hit.collider != null) {
                        Terrain terrain = hit.collider.gameObject.GetComponent<Terrain>() as Terrain;
                        if (terrain != null) {
                            Vector3 toReturn = new Vector3(position.x, hit.point.y + offset, position.z);
                            //Debug.Log("Found spawnpoint " + hit.collider.gameObject.name +" at "+ toReturn);
                            return toReturn;
                        }
                    }

                }
                return GmUtil.Infinity;
            }
        }
    }
}
