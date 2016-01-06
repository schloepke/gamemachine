using UnityEngine;
using System.Collections;
using Character = io.gamemachine.messages.Character;
using System.Net;

namespace GameMachine {
    namespace Common {
        public class NetworkSettings : MonoBehaviour {

            public static NetworkSettings instance;

            [Header("Login/connect info")]
            public string gameId = "mygame";
            public string username = "TestPlayer";
            public string password = "pass";
            public string hostname = "127.0.0.1";
            public string httpHost = "127.0.0.1";

            [Header("Populated post login")]
            public string characterId;
            public bool loggedIn = false;
            public int authtoken;
            public Character character;

            [Header("Protocol/port info")]
            public Login.Protocol protocol = Login.Protocol.UDP;
            public bool httpUseSSL = false;
            public int httpPort = 3000;
            public int udpPort = 24130;
            public int tcpPort = 8910;
            

            [Header("Are we running as a server client/agent controller")]
            public bool serverClient = false;
            private string httpPrefix;

            public bool showClientStats = false;

            void Awake() {
                instance = this;
                
                if (httpUseSSL) {
                    httpPrefix = "https://";
                } else {
                    httpPrefix = "http://";
                }
            }

            public string BaseUri() {
                return httpPrefix + httpHost + ":" + httpPort;
            }

            public bool sameAddress(string address1, string address2) {
                IPHostEntry one = Dns.GetHostEntry(address1);
                IPHostEntry two = Dns.GetHostEntry(address2);
                IPAddress ipOne = one.AddressList[0];
                IPAddress ipTwo = two.AddressList[0];
                if (ipOne.ToString() == ipTwo.ToString()) {
                    return true;
                } else {
                    return false;
                }
            }

        }
    }
}
