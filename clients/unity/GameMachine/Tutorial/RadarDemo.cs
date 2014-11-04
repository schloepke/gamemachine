using UnityEngine;
using System;
using System.Linq;
using GameMachine;
using GameMachine.Core;
using GameMachine.Tutorial;
using Entity = GameMachine.Messages.Entity;
using EchoTest = GameMachine.Messages.EchoTest;
using System.Collections;
using System.Collections.Generic;
using TrackData = GameMachine.Messages.TrackData;

public class RadarDemo : MonoBehaviour, GameMachine.Trackable
{

    public GameObject player;
    public static Dictionary<string, DemoNpc> entities = new Dictionary<string, DemoNpc> ();

    void Start ()
    {
        EntityTracking.Register (this);
        GameObject[] gos;
        gos = GameObject.FindGameObjectsWithTag ("Player");
        player = gos [0];

        Vector3 position;
        
        position = new Vector3 (200f, 0f, 200f);
        player.transform.position = position;
        InvokeRepeating ("RemoveExpiredEntities", 0.005f, 1.0f);
    }
	
    public void RemoveExpiredEntities ()
    {
        var itemsToRemove = entities.Where (f => (Time.time - f.Value.lastUpdate) > 1f).ToArray ();
        foreach (var item in itemsToRemove) {
            Destroy (item.Value.gameObject);
            entities.Remove (item.Key);
        }
    }

    public void TrackDataReceived (List<TrackData> trackDatas)
    {
        DemoNpc entity;
        foreach (TrackData trackData in trackDatas) {
            string entityId = trackData.id;
            Vector3 pos = new Vector3 (trackData.x, trackData.z, trackData.y);
            if (!entities.ContainsKey (entityId)) {
                entity = new DemoNpc ();
                entity.gameObject = new GameObject ();
                entity.gameObject.name = entityId;
                entity.gameObject.tag = "entity";
                entities [entityId] = entity;
            }
            
            entity = entities [entityId];
            entity.lastUpdate = Time.time;
            entity.gameObject.transform.position = pos;
        }
    }
    
    public TrackData UpdateTracking ()
    {
        if (player == null) {
            return null;
        }

        TrackData trackData = new TrackData ();
        //trackData.gridName = "mygrid";
        
        trackData.x = player.transform.position.x;
        trackData.y = player.transform.position.z;
        trackData.z = player.transform.position.y;


        return trackData;
    }
}
