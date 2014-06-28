using UnityEngine;
using System.Collections;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using NativeBytes = GameMachine.Messages.NativeBytes;

namespace GameMachine.Example
{
    public class Persistence : MonoBehaviour
    {

        // The persistence layer will scope data it saves to the player.  This is done on the server side
        // and is transparent to the client.  This is a convenience feature for prototyping and development. Do
        // not use this in a production app.

        // **IMPORTANT**
        // Please note that this is still not completely secure, as a hacked client can still write out data 
        // you are not expecting and there is no way to validate the data before saving it (other then the scoping).  Any server logic that
        // you write that depends on data saved using this feature is WRONG.  Do not do that.  You have been warned.


        void Start()
        {

            //Setup.  The following 3 steps are required for peristence to work

            // 1. Find the ObjectDb actor.  It is started automatically by the actor system
            ObjectDb db = ActorSystem.Instance.Find("ObjectDb") as ObjectDb;

            // 2. Set the callback to receive objects from the server
            ObjectDb.ObjectReceived callback = OnObjectReceived;
            db.OnObjectReceived(callback);

            // 3. Set the player id
            db.SetPlayerId(User.Instance.username);



            // Saving and retrieving entities
            Entity entity = new Entity();
            entity.id = "dbtest";
            db.Save(entity);
            db.Find(entity.id);


            // If you want to save arbitrary binary data, you must wrap it in a NativeBytes message and attach that
            // message to the entity you are saving.

            // Simple byte array to use as a test
            string str = "Howdy";
            byte[] bytes = new byte[str.Length * sizeof(char)];
            System.Buffer.BlockCopy(str.ToCharArray(), 0, bytes, 0, bytes.Length);

            // Wrap the byte array and save it
            NativeBytes nativeBytes = new NativeBytes();
            nativeBytes.bytes = bytes;
            entity.nativeBytes = nativeBytes;
            entity.id = "native_bytes_test";
            db.Save(entity);
            db.Find(entity.id);

        }
	
        void OnObjectReceived(object message)
        {
            Entity entity = message as Entity;

            Logger.Debug("Received entity with id " + entity.id + " from server");

            // Look for native byte messages
            if (entity.nativeBytes != null)
            {
                //byte[] bytes = entity.nativeBytes.bytes;
                //Logger.Debug("Found native bytes");
            }

        }
    }
}
