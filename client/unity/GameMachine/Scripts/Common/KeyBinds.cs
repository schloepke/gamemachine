using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

namespace GameMachine {
    public class KeyBinds : MonoBehaviour {

        private static Dictionary<string, KeyCode> bindings = new Dictionary<string, KeyCode>()
        {
            {"RunWalk", KeyCode.R },
            {"Crafting", KeyCode.K },
            {"Inventory", KeyCode.I },
            {"Character", KeyCode.C},
            {"Building", KeyCode.B },
            {"Help", KeyCode.H },
            {"Chat", KeyCode.O },
            {"Use", KeyCode.F },
            {"PlayerLight", KeyCode.L },
            {"Guild", KeyCode.G }
        };

        public static KeyCode Binding(string name) {
            return bindings[name];
        }

    }
}
