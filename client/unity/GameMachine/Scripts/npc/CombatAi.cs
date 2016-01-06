using UnityEngine;
using System.Collections;

namespace GameMachine {
    public interface CombatAi {
        bool TargetInRange();
        void SetWeapon(string name);
    }
}
