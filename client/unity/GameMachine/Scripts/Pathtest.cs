using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Pathtest : MonoBehaviour {

    public GameObject destination;
    public float speed = 6f;
    public bool hasTarget = false;
    private Queue<Vector3> path = new Queue<Vector3>();
    public Vector3 target;

    void Start () {
	
	}

    void Update() {

        if (!hasTarget) {
            if (destination != null) {
                hasTarget = true;
                //List <Vector3> nodes = AStar.FindPath(transform.position, destination.transform.position);
                //foreach (Vector3 node in nodes) {
                //    path.Enqueue(node);
                //}
            }
        }

        if (hasTarget) {

            if (target == Vector3.zero) {
                if (path.Count > 0) {
                    target = path.Dequeue();
                } else {
                    hasTarget = false;
                    destination = null;
                    return;
                }
            }

            float step = speed * Time.deltaTime;
            transform.position = Vector3.MoveTowards(transform.position, target, step);
            if (Vector3.Distance(transform.position, target) <= 0.4f) {
                target = Vector3.zero;
            }
        }
       
    }
}
