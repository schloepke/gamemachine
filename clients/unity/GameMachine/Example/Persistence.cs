using UnityEngine;
using System.Collections;
using GameMachine;
using Entity = GameMachine.Messages.Entity;

public class Persistence : MonoBehaviour
{

    // Use this for initialization
    void Start()
    {
        Logger.Debug("Persistence loaded");
        ObjectDb db = ActorSystem.Instance.Find("ObjectDb") as ObjectDb;
        ObjectDb.ObjectReceived callback = OnObjectReceived;
        db.OnObjectReceived(callback);
        db.SetPlayerId(User.Instance.username);


        Entity entity = new Entity();
        entity.id = "dbtest";
        db.Save(entity);
        db.Find(entity.id);
    }
	
    // Update is called once per frame
    void Update()
    {
	
    }

    void OnObjectReceived(object message)
    {
        Logger.Debug("Received object from server");
    }
}
