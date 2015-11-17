using UnityEngine;
using System.Collections.Generic;
using System.Collections;

namespace GameMachine {
    namespace Animation {
        public class AnimationController : MonoBehaviour {

            public enum BlockState {
                None,
                PreAnimation,
                Animation
            }

            private Animator animator;
            private AnimatorStateInfo stateInfo;
            private Dictionary<AnimationLayer, int> layerIndex = new Dictionary<AnimationLayer, int>();
            private Dictionary<AnimationLayer, int> currentLayerTrigger = new Dictionary<AnimationLayer, int>();

            private Dictionary<AnimationLayer, Dictionary<AnimationName, int>> layers = new Dictionary<AnimationLayer, Dictionary<AnimationName, int>>();
            private Dictionary<int, string> triggerNames = new Dictionary<int, string>();

            private RuntimeAnimatorController defaultController;
            private float lastReset;
            private float lastAnimation;
            private int blockingId = -1;
            private int blockingLayer = -1;
            private BlockState blockState = BlockState.None;

            void Awake() {
                animator = GetComponentInChildren<Animator>() as Animator;
                defaultController = animator.runtimeAnimatorController;
            }

            public void OnAnimatorMove() {
            }

            // Update is called once per frame
            void Update() {
               
            }

            public void SetDefaultController() {
                animator.runtimeAnimatorController = defaultController;
            }

            public void SetController(RuntimeAnimatorController controller) {
                animator.runtimeAnimatorController = controller;
            }

            private IEnumerator SetCurrentTrigger(AnimationLayer layer) {
                yield return new WaitForEndOfFrame();
                int layerId = GetLayerId(layer);
                stateInfo = animator.GetCurrentAnimatorStateInfo(layerId);
                SetCurrentTrigger(layer, stateInfo.fullPathHash);
            }

            public void SetAnimation(AnimationName name) {
                SetAnimation(name, AnimationLayer.Base);
            }

            public void SetAnimation(AnimationName name, AnimationLayer layer, bool blocking=false) {
                
                int layerId = GetLayerId(layer);

                if (animator.IsInTransition(layerId)) {
                    return;
                }

                stateInfo = animator.GetCurrentAnimatorStateInfo(layerId);
                
                
                if (blockingId != -1) {
                    AnimatorStateInfo blockInfo = animator.GetCurrentAnimatorStateInfo(blockingLayer);
                    if (blockState == BlockState.PreAnimation) {
                        if (blockInfo.fullPathHash == blockingId) {
                            blockState = BlockState.Animation;
                        }
                        return;
                    } else if (blockState == BlockState.Animation) {
                        if (blockInfo.fullPathHash == blockingId) {
                            return;
                        } else {
                            blockingId = -1;
                            blockState = BlockState.None;
                        }
                    }
                }

                int triggerId = GetTriggerId(name, layer);
                int currentTrigger = GetCurrentTrigger(layer);

                if (blocking) {
                    blockingId = triggerId;
                    blockingLayer = layerId;
                    blockState = BlockState.PreAnimation;
                }

                if (currentTrigger == triggerId) {
                    if (Time.time - lastReset > 0.20 && currentTrigger != stateInfo.fullPathHash) {
                        SetCurrentTrigger(layer, stateInfo.fullPathHash);
                        lastReset = Time.time;
                    } else {
                        return;
                    }
                }

                if (stateInfo.fullPathHash == triggerId) {
                    return;
                }

                animator.SetTrigger(triggerId);
                SetCurrentTrigger(layer, triggerId);

            }

            private int GetTriggerId(AnimationName name, AnimationLayer layer) {
                if (!layers.ContainsKey(layer)) {
                    layers[layer] = new Dictionary<AnimationName, int>();
                }

                if (!layers[layer].ContainsKey(name)) {
                    string pathName = layer.ToString() + "." + name.ToString();
                    int pathHash = Animator.StringToHash(pathName);
                    layers[layer][name] = pathHash;
                    triggerNames[pathHash] = pathName;
                }

                return layers[layer][name];
            }

            private int GetLayerId(AnimationLayer layer) {
                if (!layerIndex.ContainsKey(layer)) {
                    layerIndex[layer] = animator.GetLayerIndex(layer.ToString());
                }
                return layerIndex[layer];
            }

            private int GetCurrentTrigger(AnimationLayer layer) {
                if (!currentLayerTrigger.ContainsKey(layer)) {
                    currentLayerTrigger[layer] = -1;
                }
                return currentLayerTrigger[layer];
            }

            private void SetCurrentTrigger(AnimationLayer layer, int triggerId) {
                currentLayerTrigger[layer] = triggerId;
            }
        }
    }
}
