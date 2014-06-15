using System.Collections;
using UnityEngine;
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
        public Client client;

        public void Login(string username, string password, Authentication.Success success, Authentication.Error error)
        {
            Authentication auth = new Authentication();
            StartCoroutine(auth.Authenticate(username, password, success, error));
        }

        public void Run(string username, string authtoken)
        {
            // Create client and start actor system
            client = new Client(username, authtoken);
            ActorSystem.Instance.Start(client);

            // Now create the actors
            CreateActors();
            Logger.Debug("Actor system started");
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

            EntityTracking entityTracking = new EntityTracking();
            entityTracking.AddComponentSet("Neighbors");
            ActorSystem.Instance.RegisterActor(entityTracking);
        }

        void Update()
        {
            if (ActorSystem.Instance.Running)
            {
                ActorSystem.Instance.Update(6);
            }
        }
    }
}
