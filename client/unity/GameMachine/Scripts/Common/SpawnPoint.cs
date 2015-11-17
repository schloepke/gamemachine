using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine {
    namespace Common {
        public class SpawnPoint : MonoBehaviour {

            public enum SpawnType {
                Normal,
                FromLocalSaved,
                SpawnObject
            }

            public List<GameObject> defaultSpawnPoints = new List<GameObject>();
            public float heightOffset = 3f;
            public float randomRangeForObjectSpawn = 20f;
            public SpawnType spawnType;
            public bool spawned = false;

            public static SpawnPoint Instance() {
                return GameObject.FindObjectOfType<SpawnPoint>() as SpawnPoint;
            }

            void Start() {
                InvokeRepeating("SaveLocal", 1f, 5f);
            }

            public void SaveLocal() {
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

            public Vector3 GetSavedLocal() {
                float x = PlayerPrefs.GetFloat("spawnx");
                float y = PlayerPrefs.GetFloat("spawny");
                float z = PlayerPrefs.GetFloat("spawnz");
                return new Vector3(x, y, z);
            }

            public Vector3 GetObjectSpawnPoint() {
                GameObject spawnPoint = GameObject.Find("spawnpoint");
                if (spawnPoint == null) {
                    Debug.Log("Unable to find spawnpoint, giving up");
                    return Vector3.zero;
                }

                Vector3 randomizedPoint = new Vector3(
                    spawnPoint.transform.position.x + Random.Range(-randomRangeForObjectSpawn, randomRangeForObjectSpawn),
                    spawnPoint.transform.position.y,
                    spawnPoint.transform.position.z + Random.Range(-randomRangeForObjectSpawn, randomRangeForObjectSpawn)
                    );

                Vector3 groundedPosition = GroundedPosition(randomizedPoint, heightOffset);
                if (groundedPosition != Vector3.zero) {
                    return groundedPosition;
                } else {
                    Debug.Log("Unable to find grounded position from spawnpoint, giving up");
                    return Vector3.zero;
                }
            }

            public Vector3 GetSavedSpawnPoint() {
                return GetSavedLocal();
            }

            public Vector3 GetNetworkSpawnPoint() {
                IGameEntity player = GameEntityManager.GetPlayerEntity();
                if (player != null) {
                    Vector3 networkSpawnPoint = player.GetSpawnPoint();
                    if (networkSpawnPoint != Vector3.zero) {
                        return networkSpawnPoint;
                    }
                }
                return Vector3.zero;
            }
            
            public Vector3 SpawnpointExact() {
                if (spawnType == SpawnType.SpawnObject) {
                    return GetObjectSpawnPoint();
                } else if (spawnType == SpawnType.FromLocalSaved) {
                    return GetSavedSpawnPoint();
                }

                Vector3 spawnpoint = Vector3.zero;

                spawnpoint = GetNetworkSpawnPoint();
                if (spawnpoint != Vector3.zero && PointOverGround(spawnpoint)) {
                    return spawnpoint;
                }

                spawnpoint = GetSavedSpawnPoint();
                if (spawnpoint != Vector3.zero && PointOverGround(spawnpoint)) {
                    return spawnpoint;
                }

                spawnpoint = GetObjectSpawnPoint();
                if (spawnpoint != Vector3.zero && PointOverGround(spawnpoint)) {
                    return spawnpoint;
                }


                return spawnpoint;
            }

            public bool PointOverGround(Vector3 point) {
                Vector3 groundedPosition = GroundedPosition(point);
                if (groundedPosition != Vector3.zero) {
                    return true;
                } else {
                    return false;
                }
            }

            public Vector3 SpawnpointOnTerrain() {
                Vector3 groundedPosition;
                Vector3 saved = GetSavedLocal();
                if (saved != Vector3.zero) {
                    groundedPosition = GroundedPosition(saved, heightOffset);
                    if (groundedPosition != Vector3.zero) {
                        return groundedPosition;
                    } else {
                        Debug.Log("Unable to find grounded position from last save point");
                    }
                }

                GameObject spawnPoint = GameObject.Find("spawnpoint");
                if (spawnPoint == null) {
                    Debug.Log("Unable to find spawnpoint, giving up");
                    return Vector3.zero;
                }

                groundedPosition = GroundedPosition(spawnPoint.transform.position, heightOffset);
                if (groundedPosition != Vector3.zero) {
                    return groundedPosition;
                } else {
                    Debug.Log("Unable to find grounded position from spawnpoint, giving up");
                    return Vector3.zero;
                }
            }

            public Vector3 GetClosest(Vector3 position) {
                float max = 100000f;
                GameObject best = null;
                foreach (GameObject spawnPoint in defaultSpawnPoints) {
                    float dist = Vector3.Distance(spawnPoint.transform.position, position);
                    if (dist < max) {
                        max = dist;
                        best = spawnPoint;
                    }
                }
                return GroundedPosition(best.transform.position, heightOffset);
            }

            public Vector3 GroundedPosition(Vector3 position) {
                return GroundedPosition(position, heightOffset);
            }

            public Vector3 GroundedPosition(Vector3 position, float offset) {
                Vector3 start = new Vector3(position.x, 900f, position.z);
                RaycastHit[] hits;
                hits = Physics.RaycastAll(start, Vector3.down, 1000f);
                foreach (RaycastHit hit in hits) {
                    if (hit.collider != null) {
                        Terrain terrain = hit.collider.gameObject.GetComponent<Terrain>() as Terrain;
                        if (terrain != null || hit.collider.gameObject.tag == "gm_placed_block") {
                            Debug.Log("Found spawnpoint " + hit.collider.gameObject.name);
                            return new Vector3(position.x, hit.point.y + offset, position.z);
                        }
                    }
                   
                }
                return Vector3.zero;
            }
        }
    }
}
