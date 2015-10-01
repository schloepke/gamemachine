
public interface IGameEntityController {

    bool IsInitialized();
    void ManualUpdate();
    float GetSwimSpeed();
    float GetRunSpeed();
    float GetWalkSpeed();
    void SetSwimSpeed(float speed);
    void SetRunSpeed(float speed);
    void SetWalkSpeed(float speed);
}
