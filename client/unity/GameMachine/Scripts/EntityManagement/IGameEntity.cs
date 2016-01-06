using UnityEngine;
using io.gamemachine.messages;

namespace GameMachine {
    namespace Common {
        public interface IGameEntity {

            bool IsPlayer();
            bool IsNpc();
            bool IsOtherPlayer();
            GameEntityType GetGameEntityType();
            Vector3 GetTargetPosition();
            void SetTargetPosition(Vector3 targetPosition);
            GameEntityController GetGameEntityController();
            string GetEntityId();
            string GetCharacterId();
            Character GetCharacter();
            void SetCharacter(Character character);
            Transform GetTransform();
            TrackData GetTrackData();
            void UpdateFromTrackData(TrackData trackData, bool hasDelta);
            void Remove();
            NetworkFields GetNetworkFields();
            Vector3 GetSpawnPoint();
            bool IsInCombat();
            bool IsDead();
            void SetVitals(Vitals vitals);
            Vitals GetVitals();
            bool MovementDisabled();
            bool HasVitals();
            bool IsActive();
            void SetActive(bool active, bool setLocation);
            GameObject GetGameObject();
            void SetHealthbar(bool active, float percent);
            bool IsLocalController();
            ControllerType GetControllerType();
            void SetShortId(int shortId);
        }
    }
}
