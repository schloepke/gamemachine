using UnityEngine;
using GameMachine;

namespace GameMachine
{
    public class MessagePoller : MonoBehaviour
    {
        // This triggers the actor system to deliver waiting messages
        void Update()
        {
            if (ActorSystem.Instance.Running)
            {
                ActorSystem.Instance.Update(6);
            }
        }
    }
}