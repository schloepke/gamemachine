
using GameMachine;
using GameMachine.Animation;
using UnityEngine;

public interface GameEntityController {

    bool IsInitialized();
    void ManualUpdate();
    float GetPlayerSpeed();
    void SetPlayerSpeed(float speed);
    void SetNetworkFields(NetworkFields networkFields);
    
}
