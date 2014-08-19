using UnityEngine;
using System.Collections;
using GameMachine;
using GameMachine.Chat;

public class TeamDemo : MonoBehaviour
{

    // Use this for initialization
    void Start ()
    {
        this.gameObject.AddComponent<ChatManager> ();
        this.gameObject.AddComponent<TeamUi> ();
    }
	
    // Update is called once per frame
    void Update ()
    {
	
    }
}
