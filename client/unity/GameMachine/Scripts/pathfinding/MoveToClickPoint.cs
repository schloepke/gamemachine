using UnityEngine;

public class MoveToClickPoint : MonoBehaviour {

    public Transform destination;
    public GameObject obstacle;

    NavMeshAgent agent;
    
    void Start() {
        agent = GetComponent<NavMeshAgent>();
        for (float x=0; x<200;x+=5) {
            for (float z = 0; z < 200; z+= 5) {
                GameObject go = GameObject.Instantiate(obstacle);
                go.transform.position = new Vector3(x, obstacle.transform.position.y, z);
                
            }
        }
    }

    void Update() {

        if (destination != null) {
            agent.destination = destination.position;
            destination = null;
        }

        if (Input.GetMouseButtonDown(0)) {
            Debug.Log("Update");
            RaycastHit hit;

            if (Physics.Raycast(Camera.main.ScreenPointToRay(Input.mousePosition), out hit, 100)) {
                agent.destination = hit.point;
            }
            
            if (Physics.Raycast(Camera.current.ScreenPointToRay(Input.mousePosition), out hit, 1000)) {
                Debug.Log(hit.point);
                agent.destination = hit.point;
            }
        }
    }
}