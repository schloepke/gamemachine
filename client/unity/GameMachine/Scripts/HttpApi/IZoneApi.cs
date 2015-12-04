using io.gamemachine.messages;

namespace GameMachine {
    namespace HttpApi {
        public interface IZoneApi {

            void OnGetZones(Zones zones);
            void OnGetZonesError(string error);

            void OnSetZone(Zone zone);
            void OnSetZoneError(string error);
        }
    }
}
