using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using GameMachine;
using Newtonsoft.Json;
using GameMachine.Common;

namespace GameMachine.Core
{
    public class Authentication
    {
        public delegate void Success (Dictionary<string, string> values);
        public delegate void Error (string reason);

        public IEnumerator Authenticate (string gameId, string hostname, int httpPort, string username, string password, Success success, Error error)
        {
            string prefix;
            if (NetworkSettings.instance.httpUseSSL) {
                prefix = "https://";
            } else {
                prefix = "http://";
            }

            string uri = prefix + hostname + ":" + httpPort + "/api/client/login/" + gameId;
            Debug.Log ("Authenticating via " + uri);
            var form = new WWWForm ();
            form.AddField ("username", username);
            form.AddField ("password", password);
            WWW www = new WWW (uri, form.data, form.headers);
            yield return www;
        
            if (www.error != null) {
                Debug.Log(www.error);
                error (www.error);
            } else {
                Logger.Debug (www.text);
                Dictionary<string, string> values = JsonConvert.DeserializeObject<Dictionary<string, string>> (www.text);
                success (values);
            }
        }
    }
}
