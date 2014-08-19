using UnityEngine;
using System.Collections;
using GameMachine.Core;

namespace GameMachine.Tutorial
{
    public class ChatDemo : MonoBehaviour
    {

        // Use this for initialization
        void Start ()
        {
            this.gameObject.AddComponent<Chat.ChatManager> ();
            Messenger messenger = ActorSystem.Instance.Find ("Messenger") as Messenger;
            messenger.JoinChannel ("Lobby");
        }
	
        // Update is called once per frame
        void Update ()
        {
	
        }
    }
}
