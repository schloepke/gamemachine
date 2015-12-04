using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using System;
using GameMachine;
using GameMachine.Core;
using GameMachine.Chat;
using GameMachine.Common;

namespace GameMachine
{
    public class Login : MonoBehaviour
    {
        public enum Protocol {
            UDP,
            TCP
        }

        private App app;
        private string authUri;
        private int port = 0;
        private string hostname;
        private ILoginUI loginUi;
        public static GameMachineApp userApp;

        public static void SetGameMachineApp (GameMachineApp userApp)
        {
            Login.userApp = userApp;
        }

        public void SetLoginUi (ILoginUI loginUi)
        {
            this.loginUi = loginUi;
        }

        public void DoLogin ()
        {
            User user = User.Instance;
            user.SetUser(NetworkSettings.instance.username.ToString(), NetworkSettings.instance.password.ToString());
            Authentication.Success success = OnAuthenticationSuccess;
            Authentication.Error error = OnAuthenticationError;
			
            app = gameObject.GetComponent<App>() as App;
            if (app == null) {
                app = gameObject.AddComponent<App>() as App;
            }

            app.Login(NetworkSettings.instance.gameId, NetworkSettings.instance.httpHost, NetworkSettings.instance.httpPort, user.username, user.password, success, error);
        }

        void OnAuthenticationError (string error)
        {
            if (loginUi != null) {
                loginUi.SetError (error.Replace (System.Environment.NewLine, ""));
            }
            userApp.OnLoginFailure (error);
        }

        private Protocol StringToProtocol(string prot) {
            if (prot == "UDP") {
                return Protocol.UDP;
            } else {
                return Protocol.TCP;
            }
        }

        void OnAuthenticationSuccess (Dictionary<string, string> info)
        {
            string authtoken = info ["authtoken"];

            if (NetworkSettings.instance.username != info["username"]) {
                NetworkSettings.instance.username = info["username"];
                User.Instance.username = NetworkSettings.instance.username;
                Debug.Log("username reset " + NetworkSettings.instance.username);
            }

            if (info ["protocol"] != "ANY") {
                NetworkSettings.instance.protocol = StringToProtocol(info["protocol"]);

                if (NetworkSettings.instance.protocol == Protocol.TCP) {
                    hostname = info ["tcp_host"];
                    port = Int32.Parse (info ["tcp_port"]);
                } else if (NetworkSettings.instance.protocol == Protocol.UDP) {
                    hostname = info ["udp_host"];
                    port = Int32.Parse (info ["udp_port"]);
                }
            } else {
                if (NetworkSettings.instance.protocol == Protocol.TCP) {
                    port = NetworkSettings.instance.tcpPort;
                } else if (NetworkSettings.instance.protocol == Protocol.UDP) {
                    port = NetworkSettings.instance.udpPort;
                }
                hostname = NetworkSettings.instance.hostname;
            }


            Debug.Log("Authtoken: " + authtoken + " Protocol:" + NetworkSettings.instance.protocol + " Hostname:" + hostname + " port:" + port);
            App.AppStarted callback = OnAppStarted;
            app.OnAppStarted (callback);

            App.ConnectionTimeout connectionCallback = OnConnectionTimeout;
            app.OnConnectionTimeout (connectionCallback);

            NetworkSettings.instance.authtoken = Int32.Parse(authtoken);
            app.Run(NetworkSettings.instance.protocol, hostname, port, User.Instance.username, NetworkSettings.instance.authtoken);
        }

        public void OnAppStarted ()
        {
            userApp.OnLoggedIn ();
        }

        public void OnConnectionTimeout ()
        {
            userApp.ConnectionTimeout ();
        }

       
        void Start ()
        {

        }
	
    }
}
