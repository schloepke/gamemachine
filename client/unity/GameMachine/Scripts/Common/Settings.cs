using UnityEngine;
using System.Collections;

namespace GameMachine {
    namespace Common {
        public class Settings : MonoBehaviour {
            private static Settings instance;

            public static string GetServerPath() {
                return PlayerPrefs.GetString("serverPath");
            }

            public float worldOffset = 0f;
            public float waterLevel = -100f;

            [Header("Default user/character in non networked scenes")]
            public string defaultEntityId = "TestPlayer";
            public string defaultCharacterId = "TestCharacter";

            [Header("Tag for all game entities")]
            public string gameEntityTag = "game_entity";

            [Header("Game entity camera obstacle mask")]
            public LayerMask cameraObstacles = 0;

            [Header("World interaction mask")]
            public LayerMask worldInteractionMask = 0;

            public static Settings Instance() {
                if (instance != null) {
                    return instance;
                }
                instance = GameComponent.Get<Settings>();
                return instance;
            }
        }
    }
}
