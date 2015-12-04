using UnityEngine;
using System.Collections;
using GameMachine.Common;
using System.IO;
using ProtoBuf;
using io.gamemachine.messages;

namespace GameMachine {
    namespace HttpApi {
        public class ZoneApi : MonoBehaviour {

            public static ZoneApi instance;

            void Awake() {
                instance = this;
            }

            void Start() {
            }


            public void GetZones(IZoneApi caller) {
                StartCoroutine(GetZonesRoutine(caller));
            }

            public IEnumerator GetZonesRoutine(IZoneApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/players/get_zones";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnGetZonesError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Zones zones = Serializer.Deserialize<Zones>(stream);
                    caller.OnGetZones(zones);
                }
            }

            public void SetZone(string zone, IZoneApi caller) {
                StartCoroutine(SetZoneRoutine(zone, caller));
            }

            public IEnumerator SetZoneRoutine(string zoneName, IZoneApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/players/set_zone";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("zone", zoneName);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnSetZoneError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Zone zone = Serializer.Deserialize<Zone>(stream);
                    caller.OnSetZone(zone);
                }
            }

        }
    }
}
