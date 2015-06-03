using UnityEngine;
using System.Collections;
using ZoneInfos = io.gamemachine.messages.ZoneInfos;
using ZoneInfo = io.gamemachine.messages.ZoneInfo;

namespace GameMachine {
    namespace HttpApi {
        public interface IZoneApi {

            void OnGetZones(ZoneInfos infos);
            void OnGetZonesError(string error);

            void OnSetZone(ZoneInfo info);
            void OnSetZoneError(string error);
        }
    }
}
