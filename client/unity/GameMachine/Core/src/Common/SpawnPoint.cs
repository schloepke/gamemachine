using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine {
    namespace Common {
        public class SpawnPoint : MonoBehaviour {

            public List<GameObject> defaultSpawnPoints = new List<GameObject>();
            public float heightOffset = 3f;

            // Use this for initialization
            void Awake() {
            }

            public Vector3 GetFirst() {
                GameObject go = defaultSpawnPoints[0];
                return GroundedPosition(go.transform.position, heightOffset);
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
                return GroundedPosition(best.transform.position,heightOffset);
            }

            public Vector3 GroundedPosition(Vector3 position) {
                return GroundedPosition(position, heightOffset);
            }

            public Vector3 GroundedPosition(Vector3 position,float offset) {
                Vector3 start = new Vector3(position.x, 900f, position.z);
                RaycastHit[] hits;
                hits = Physics.RaycastAll(start, Vector3.down, 1000f);
                foreach (RaycastHit hit in hits) {
                    if (hit.collider != null) {
                        return new Vector3(position.x,hit.point.y+offset,position.z);
                    }
                }
                return new Vector3(position.x, position.y + offset, position.z);
            }
        }
    }
}
