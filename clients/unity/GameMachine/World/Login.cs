using UnityEngine;
using System.Collections;
using System;
using GameMachine;

namespace GameMachine.World
{
    public class Login : MonoBehaviour
    {

        private Rect loginWindow;
        private float windowLeft;
        private float windowHeight;
        private float windowWidth;
        private float windowTop;
        private string username = "player";
        private string password = "pass";
        private bool hasError = false;
        private string loginError = "";
        private bool disableGui = false;
        private GameMachine.App app;

        void OnGUI()
        {


            loginWindow = GUI.Window(0, loginWindow, WindowFunction, "Game Machine Login");
        }
        
        void WindowFunction(int windowID)
        {
            if (disableGui)
            {
                GUI.enabled = false;
            }
            GUI.Label(new Rect(25, 50, 100, 30), "Username");
            GUI.Label(new Rect(25, 100, 100, 30), "Password");

            username = GUI.TextField(new Rect(125, 50, 200, 25), username);
            password = GUI.PasswordField(new Rect(125, 100, 200, 25), password, "*" [0], 25);

            if (hasError)
            {
                GUI.Label(new Rect(25, 150, 400, 30), "Login Failed: " + loginError);
            }


            if (GUI.Button(new Rect(200, 200, 100, 30), "Login"))
            {
                disableGui = true;

                GameMachine.Config.authUri = "http://192.168.1.8:3000/auth";
                GameMachine.Config.udpHost = "192.168.1.8";
                //GameMachine.Config.udpHost = "127.0.0.1";
                GameMachine.Config.udpPort = 8100;

                User user = User.Instance;
                user.SetUser(username.ToString(), password.ToString());
                GameMachine.Authentication.Success success = OnAuthenticationSuccess;
                GameMachine.Authentication.Error error = OnAuthenticationError;
                app = this.gameObject.AddComponent(Type.GetType("GameMachine.App")) as GameMachine.App;
                app.Login(user.username, user.password, success, error);
            }
        }

        void OnAuthenticationError(string error)
        {
            Logger.Debug("Authentication Failed: " + error);
            hasError = true;
            loginError = error.Replace(System.Environment.NewLine, "");
            disableGui = false;
        }

        public void OnAuthenticationSuccess(string authtoken)
        {
            GameMachine.App.AppStarted callback = OnAppStarted;
            app.OnAppStarted(callback);
            app.Run(User.Instance.username, authtoken);

        }

        public void OnAppStarted()
        {
            Application.LoadLevel("world_main");
        }

        // Use this for initialization
        void Start()
        {
            windowWidth = 500;
            windowHeight = 250;
            windowLeft = (Screen.width / 2) - windowWidth / 2;
            windowTop = (Screen.height / 2) - windowHeight / 2;
            loginWindow = new Rect(windowLeft, windowTop, windowWidth, windowHeight);
        }
	
    }
}
