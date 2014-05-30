using UnityEngine;
using System.Collections;
using GameMachine;

/*
 * Authenticates the user and starts up the actor system.  
 * 
 * The basics steps are as follows.
 * 
 * 1. Login via http with a username and password.  The http server will return an authorization token. 
 * 
 * 2. Initialize the Udp client with the username and auth token.  The client uses non blocking IO and 
 * processes all sends and receives in separate threads.  Received messages are put into a concurrent queue
 * where the actor system will later dequeue them and deliver to the actors.
 * 
 * 3.  Start the actor system. 
 * 
 * 4.  Create the actors
 * 
 */

namespace GameMachine
{
    public class App : MonoBehaviour
    {
        public delegate void AppStarted();

        private AppStarted appStarted;

        public void Run(string username, string password, AppStarted callback)
        {
            appStarted = callback;
            Authentication auth = new Authentication();
            Authentication.Success success = AuthenticationSuccess;
            Authentication.Error error = AuthenticationError;
        
            StartCoroutine(auth.Authenticate(username, password, success, error));
        }

        public void AuthenticationSuccess(string username, string authtoken)
        {
            // Create client and start actor system
            Client client = new Client(username, authtoken);
            ActorSystem.Instance.Start(client);

            // Now create the actors
            CreateActors();
            appStarted();
            Logger.Debug("Actor system started");
        }

        public void AuthenticationError(string reason)
        {
            Logger.Debug("Authentication Failed: " + reason);
        }

        public void CreateActors()
        {
            // Actors that come with GameMachine

            ObjectDb db = new ObjectDb();
            db.AddComponentSet("ObjectdbGetResponse");
            ActorSystem.Instance.RegisterActor(db);

            Messenger messenger = new Messenger();
            messenger.AddComponentSet("ChatMessage");
            messenger.AddComponentSet("ChatChannels");
            ActorSystem.Instance.RegisterActor(messenger);
        }

        // This triggers the actor system to deliver waiting messages
        void Update()
        {
            if (ActorSystem.Instance.Running)
            {
                ActorSystem.Instance.Update(6);
            }
        }
    }
}
