using UnityEngine;
using System.Collections;
using TrackData = io.gamemachine.messages.TrackData;
using Entity = io.gamemachine.messages.Entity;
using GameMachine.Common;
using GameMachine.Core;

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
                td.y = -2;
                td.z = 1;
                td.neighborEntityType = TrackData.EntityType.All;

                gameEntityManager.UpdateTracking(td);
            }
        }
    }
}
