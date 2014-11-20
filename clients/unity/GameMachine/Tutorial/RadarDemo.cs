using UnityEngine;
using System;
using System.Linq;
using GameMachine;
using GameMachine.Core;
using GameMachine.Tutorial;
using Entity = io.gamemachine.messages.Entity;
using EchoTest = io.gamemachine.messages.EchoTest;
using System.Collections;
using System.Collections.Generic;
using TrackData = io.gamemachine.messages.TrackData;
using TrackDataResponse = io.gamemachine.messages.TrackDataResponse;

public class RadarDemo : MonoBehaviour, GameMachine.Trackable
{

    public GameObject player;
    public static Dictionary<string, DemoNpc> entities = new Dictionary<string, DemoNpc> ();
    public static Dictionary<int, string> shortIdToEntityId = new Dictionary<int, string> ();

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
        Logger.Debug ("Entity count " + entities.Count);
    }

    public void TrackDataReceived (List<TrackData> trackDatas)
    {
        DemoNpc entity;
        Vector3 entityPos;
        foreach (TrackData trackData in trackDatas) {

            //Logger.Debug (trackData.id + "-" + trackData.shortId);
            if (trackData.id == "" && trackData.shortId > 0) {
                if (shortIdToEntityId.ContainsKey (trackData.shortId)) {
                    entity = entities [shortIdToEntityId [trackData.shortId]];
                    entityPos = new Vector3 (entity.gameObject.transform.position.x, entity.gameObject.transform.position.y, entity.gameObject.transform.position.z);
                    //Logger.Debug ("Has delta " + trackData.ix + "." + trackData.iy);
                    entityPos.x += (float)(trackData.ix / 100f);
                    entityPos.z += (float)(trackData.iy / 100f);
                    entity.gameObject.transform.position = entityPos;
                    entity.lastUpdate = Time.time;
                } else {
                    Logger.Debug ("Entity for shortId  " + trackData.shortId + " not found");
                }
            } else {
                string entityId = trackData.id;
                if (entityId == User.Instance.username) {
                    continue;
                }
                if (!entities.ContainsKey (entityId)) {
                    entity = new DemoNpc ();
                    entity.gameObject = new GameObject ();
                    entity.gameObject.name = entityId;
                    entity.gameObject.tag = "entity";
                    entities [entityId] = entity;
                    shortIdToEntityId [trackData.shortId] = entityId;
                }

                entity = entities [entityId];
                float x = trackData.x / 100f;
                float y = trackData.y / 100f;
                entity.gameObject.transform.position = new Vector3 (x, 0f, y);
                entity.lastUpdate = Time.time;
            }
        }
    }
    
    public TrackData UpdateTracking ()
    {
        if (player == null) {
            return null;
        }

        TrackData trackData = new TrackData ();
        //trackData.gridName = "mygrid";

        trackData.x = EntityTracking.ToInt (player.transform.position.x);
        trackData.y = EntityTracking.ToInt (player.transform.position.z);
        trackData.z = EntityTracking.ToInt (player.transform.position.y);

        return trackData;
    }

    public void HandleTrackDataResponse (TrackDataResponse response)
    {
        if (response.reason == TrackDataResponse.REASON.RESEND) {

        }
    }
}
