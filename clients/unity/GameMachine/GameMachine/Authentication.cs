using UnityEngine;
using System.Collections;
using GameMachine;

namespace GameMachine
{
    public class Authentication
    {
        public delegate void Success(string authtoken);
        public delegate void Error(string reason);
        private string uri;

        public Authentication()
        {
            uri = Config.authUri;
        }

        public IEnumerator Authenticate(string username, string password, Success success, Error error)
        {
            var form = new WWWForm();
            form.AddField("username", username);
            form.AddField("password", password);
            WWW www = new WWW(uri, form.data, form.headers);
            yield return www;
        
            if (www.error != null)
            {
                error(www.error);
            } else
            {
                success(www.text);
            }
        }
    }
}
