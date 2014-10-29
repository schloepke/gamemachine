using UnityEngine;
using System.Collections;
using GameMachine;
using GameMachine.Core;
using Entity = GameMachine.Messages.Entity;
using System.Collections.Generic;
using TrackData = GameMachine.Messages.TrackData;

public class Tracker : MonoBehaviour, GameMachine.Trackable
{

    // Use this for initialization
    void Start ()
    {
    }
	
    // Update is called once per frame
    void Update ()
    {
	
    }

    public void TrackDataReceived (List<TrackData> trackDatas)
    {
        foreach (TrackData trackData in trackDatas) {
            string entityId = trackData.id;
            Vector3 pos = new Vector3 (trackData.x, trackData.z, trackData.y);
        }
    }
    
    public TrackData UpdateTracking ()
    {
        TrackData trackData = new TrackData ();
        trackData.x = this.transform.position.x;
        trackData.y = this.transform.position.z;
        trackData.z = this.transform.position.y;
        
        return trackData;
    }
}
