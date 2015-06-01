using UnityEngine;
using System.Collections;
using GameMachine;

using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;

namespace GameMachine {
    namespace HttpApi {
        public interface ICharacterApi {
            void OnCharacterCreated(Character character);
            void OnCharacterCreateError(string error);

            void OnCharacterSet(string result);
            void OnCharacterSetError(string error);

            void OnCharacterGet(string playerId, Character character);
            void OnCharacterGetError(string playerId, string characterId, string error);

            void OnCharacterDeleted(string characterId);
            void OnCharacterDeleteError(string error);
        }
    }
}
