using UnityEngine;
using System.Collections;
using TrackData = io.gamemachine.messages.TrackData;
using Character = io.gamemachine.messages.Character;

namespace GameMachine {
    namespace Common {
        public interface IGameEntityFactory {

            IGameEntity Create();
            IGameEntity Create(string entityId);
            IGameEntity Create(string entityId, Character character, TrackData trackData);
        }
    }
}
