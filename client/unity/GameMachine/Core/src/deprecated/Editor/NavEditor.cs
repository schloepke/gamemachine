using UnityEngine;
using System.Collections;
using UnityEditor;

[InitializeOnLoad]
public class NavEditor : EditorWindow
{

    static NavEditor ()
    {
        //EditorApplication.update += Update;
    }

    void OnGUI ()
    {
       
    }

    void SceneGUI (SceneView sceneview)
    {
        if (Event.current.type == EventType.mouseDown) {
            Debug.Log ("MouseDown");
        }
    }

    public static void CastRayToWorld ()
    {
        //Ray ray = Camera.main.ScreenPointToRay (Input.mousePosition);    
        //Vector3 point = ray.origin + (ray.direction * 500f);
        //Debug.Log ("World point " + point);
        //Vector3 target = Camera.main.ScreenToWorldPoint (Input.mousePosition);
        //Debug.Log (target);
        Camera camera = SceneView.lastActiveSceneView.camera;
        if (camera != null) {
            Ray ray = camera.ScreenPointToRay (Input.mousePosition);
            RaycastHit hit;
        
            if (Physics.Raycast (ray, out hit)) {
                Debug.Log (hit.point);
            }
        }
        
    }
    // Use this for initialization
    void Start ()
    {
	
    }
	
    static void Update ()
    {
        //CastRayToWorld ();
        if (Input.GetMouseButtonDown (0)) {
            Debug.Log ("mousedown");
            CastRayToWorld ();
        }
    }
}
