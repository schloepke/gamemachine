using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace GameMachine {
    public class WaypointRoute : MonoBehaviour {

        public enum Type {
            ReverseOnEnd,
            Circular,
            StopOnEnd
        }

        private List<Waypoint> waypoints = new List<Waypoint>();
        private int wpIndex = 0;
        private Waypoint currentWaypoint;

        public Type type;
        public bool showAtRuntime = false;
        

        void Start() {
            waypoints = transform.GetComponentsInChildren<Waypoint>().ToList();
            
            if (!showAtRuntime) {
                waypoints.ForEach(wp => Destroy(wp.GetComponent<MeshRenderer>()));
            }
            Reset();
        }

        public void Reset() {
            wpIndex = 0;
            currentWaypoint = waypoints.ToArray().ElementAt(wpIndex);
        }

        public int CurrentWaypointIndex() {
            return wpIndex;
        }

        public Waypoint CurrentWaypoint() {
            if (currentWaypoint == null) {
                return NextWaypoint();
            } else {
                return currentWaypoint;
            }
        }

        public Waypoint NextWaypoint() {
            if (wpIndex >= (waypoints.Count - 1)) {
                if (type == Type.ReverseOnEnd) {
                    waypoints.Reverse();
                    wpIndex = 0;
                } else if (type == Type.Circular) {
                    wpIndex = 0;
                }
            } else {
                wpIndex++;
            }

            return currentWaypoint = waypoints.ToArray().ElementAt(wpIndex);
        }
    }
}
