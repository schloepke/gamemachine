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

            public Collider playerCollider;
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
                    if (instance.gameEntity != null) {
                        instance.characterId = GameEntityManager.GetPlayerEntity().GetCharacterId();
                        instance.playerTransform = GameEntityManager.GetPlayerEntity().GetTransform();
                        instance.playerRenderers = instance.playerTransform.gameObject.GetComponentsInChildren<SkinnedMeshRenderer>();
                        instance.playerCollider = instance.playerTransform.gameObject.GetComponent<Collider>() as Collider;
                    } else {
                        Debug.Log("Game player not present, assuming running as server");
                    }
                    
                    
                }
                return instance;
            }

        }
    }
}
