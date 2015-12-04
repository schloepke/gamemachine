using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine {

    [CreateAssetMenu(menuName = @"Game Machine/ServerConfig")]
    public class ServerConfig : ScriptableObject {

        public string serverPath;
        public string configName;
        public bool runHeadless = false;
        public List<Object> buildScenes;
        public int deployCount = 5;
    }
}
