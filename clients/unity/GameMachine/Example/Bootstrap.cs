using UnityEngine;
using System.Collections;
using GameMachine;
using Entity = GameMachine.Messages.Entity;

/*
 * Complete Example of how to authenticate and start the Udp client and actor system.
 * The basics steps are as follows.
 * 
 * 1. Login via http with a username and password.  The http server will return an authorization token. 
 * 
 * 2. Initialize the Udp client with the username and auth token.  The client uses non blocking IO and 
 * processes all sends and receives in separate threads.  Received messages are put into a concurrent queue
 * where the actor system will later dequeue them and deliver to the actors.
 * 
 * 3.  Start the actor system.  On startup the actor system will take care of starting up actors responsible for
 * core features such as chat.
 * 
 * 4.  Create your game specific actors.
 * 
 */

namespace Example
{
    public class Bootstrap : MonoBehaviour
    {

        public ActorSystem actorSystem;


        void Start()
        {
            string username = "player";
            string password = "pass";
            Login(username, password);
        }

        public void Login(string username, string password)
        {
            Authentication auth = new Authentication();
            Authentication.Success success = AuthenticationSuccess;
            Authentication.Error error = AuthenticationError;
        
            StartCoroutine(auth.Authenticate(username, password, success, error));
        }

        public void AuthenticationSuccess(string username, string authtoken)
        {
            Debug.Log(authtoken);
            Client client = new Client(username, authtoken);
        
            actorSystem = new ActorSystem(client);
            Debug.Log("Actor system created");

            CreateActors();
        }

        public void AuthenticationError(string reason)
        {
            Debug.Log("Authentication Failed: " + reason);
        }

        public void CreateActors()
        {
            // Create actors
            TestActor testActor = new TestActor();
            actorSystem.RegisterActor(testActor);
        
            Entity entity = new Entity();
            entity.id = "test";
            actorSystem.Find("TestActor").Tell(entity);
            actorSystem.Find("DeadLetters").Tell(entity);
            actorSystem.Find("/remote/Devnull").Tell(entity);
            actorSystem.Find("/remote/default").Tell(entity);
        }

        void Update()
        {
            if (actorSystem != null)
            {
                actorSystem.Update(6);
            }
        }
    }
}
