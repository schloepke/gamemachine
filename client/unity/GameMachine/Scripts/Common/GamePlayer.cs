using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GameMachine.Core;

namespace GameMachine {
    namespace Common {
        public class GamePlayer : MonoBehaviour {

            public enum ControllerType {
                FirstPerson,
                ThirdPerson
            }

            private static GamePlayer instance;

            public ControllerType controllerType = ControllerType.ThirdPerson;

            public Transform playerTransform;
            public SkinnedMeshRenderer[] playerRenderers;
            public string characterId = null;
            public bool useGameMachinePlayer = true;
            public IGameEntity gameEntity;
            public bool useUMA = false;

            public static bool IsNetworked() {
                return ActorSystem.instance != null;
            }

            public static GamePlayer Instance() {
                if (instance != null) {
                    return instance;
                }
                instance = GameObject.Find("GamePlayer").GetComponent<GamePlayer>() as GamePlayer;
                if (instance.useGameMachinePlayer) {
                    instance.gameEntity = GameEntityManager.GetPlayerEntity();
                    instance.characterId = GameEntityManager.GetPlayerEntity().GetCharacterId();
                    instance.playerTransform = GameEntityManager.GetPlayerEntity().GetTransform();
                    instance.playerRenderers = instance.playerTransform.gameObject.GetComponentsInChildren<SkinnedMeshRenderer>();
                    
                }
                return instance;
            }

        }
    }
}
