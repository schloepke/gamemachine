using UnityEngine;
using System.Collections;
using System;

namespace GameMachine {

    [Serializable]
    public class NpcDef {
        public enum WeaponType {
            Melee,
            Ranged
        }

        public string prefabName;
        public int count;
        public string weaponName;
        public WeaponType weaponType;
    }
}
