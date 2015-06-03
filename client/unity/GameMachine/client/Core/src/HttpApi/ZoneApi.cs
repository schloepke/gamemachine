using UnityEngine;
using System.Collections;
using UnityEngine.UI;
using System.Collections.Generic;
using GameMachine;
using GameMachine.Common;
using GameMachine.Core;
using System.IO;
using ProtoBuf;
using System.Linq;
using ZoneInfos = io.gamemachine.messages.ZoneInfos;
using ZoneInfo = io.gamemachine.messages.ZoneInfo;

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
                    ZoneInfos infos = Serializer.Deserialize<ZoneInfos>(stream);
                    caller.OnGetZones(infos);
                }
            }

            public void SetZone(string zone, IZoneApi caller) {
                StartCoroutine(SetZoneRoutine(zone, caller));
            }

            public IEnumerator SetZoneRoutine(string zone, IZoneApi caller) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/players/set_zone";
                var form = new WWWForm();
                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("zone", zone);
                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    caller.OnSetZoneError(www.error);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    ZoneInfo info = Serializer.Deserialize<ZoneInfo>(stream);
                    caller.OnSetZone(info);
                }
            }

        }
    }
}
