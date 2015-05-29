using UnityEngine;
using System.Collections;
using GameMachine;

using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;

using Players = io.gamemachine.messages.Players;
using Player = io.gamemachine.messages.Player;

namespace GameMachine {
    namespace Api {
        public interface IPlayerApi {


            void OnPlayerCreated(Player player);
            void OnPlayerCreateError(string error);

            void OnPlayer(Player player);
            void OnPlayerError(string error);

            void OnPasswordChanged(string result);
            void OnPasswordError(string error);

            //void OnPlayerDeleted(string playerId);
            //void OnPlayerDeletedError(string error);

            void OnPlayerCharacters(Characters characters);
            void OnPlayerCharactersError(string error);

            
        }
    }
}
