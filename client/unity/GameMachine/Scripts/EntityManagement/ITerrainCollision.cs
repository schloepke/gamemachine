using UnityEngine;
using System.Collections;

namespace GameMachine {
    public interface ITerrainCollision {

        void Initialize();
        void CollisionCheck();
    }
}
