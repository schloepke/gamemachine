using UnityEngine;
using System.Collections;

namespace GameMachine {
    public interface AnimationController {

        void SetController(RuntimeAnimatorController overrideController);
        void SetDefaultController();
    }
}
