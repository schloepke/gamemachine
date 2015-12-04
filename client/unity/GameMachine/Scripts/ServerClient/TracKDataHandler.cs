using UnityEngine;
using GameMachine.Common;
using GameMachine.Core;
using io.gamemachine.messages;

namespace GameMachine {
    namespace ServerClient {
        public class TracKDataHandler : MonoBehaviour {

            private GameEntityManager gameEntityManager;
            
            void Start() {
                gameEntityManager = GameComponent.Get<GameEntityManager>() as GameEntityManager;
                InvokeRepeating("UpdateTracking", 0.010f, App.gameTickInterval);
            }


            public void UpdateTracking() {

                TrackData td = new TrackData();
                td.x = -2;
                td.y = 1;
                td.z = -2;
                td.neighborEntityType = TrackData.EntityType.All;

                gameEntityManager.UpdateTracking(td);
            }
        }
    }
}
