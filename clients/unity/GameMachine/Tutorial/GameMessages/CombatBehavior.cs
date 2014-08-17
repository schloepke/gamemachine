using UnityEngine;
using System.Collections;
using GameMachine;
using GameMessage = GameMachine.Messages.GameMessage;
using GameMachine.Core;

namespace GameMachine.Tutorials.GameMessages
{
    public class CombatBehavior : MonoBehaviour, GameMachine.Core.Behavior
    {

        private GameMessageHandler messageHandler = GameMessageHandler.Instance;

        public void OnMessage (object message)
        {
           
        }
        // Use this for initialization
        void Start ()
        {
            messageHandler.Register (this, "Attack");
            InvokeRepeating ("AttackTarget", 1.0f, 1.0f);


            GameMessage gameMessage = new GameMessage ();
            messageHandler.SendReliable (gameMessage, 1);
        }

        public void AttackTarget ()
        {

        }
        // Update is called once per frame
        void Update ()
        {
            //Debug.Log (playerName);
        }
    }
}
