using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GameMachine.Common;

public class MyGameEntity : MonoBehaviour, IGameEntity {

   

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    public string GetEntityId() {
        throw new System.NotImplementedException();
    }

    public string GetName() {
        throw new System.NotImplementedException();
    }

    public Transform GetTransform() {
        throw new System.NotImplementedException();
    }

    public io.gamemachine.messages.TrackData GetTrackData() {
        throw new System.NotImplementedException();
    }

    public void UpdateFromTrackData(io.gamemachine.messages.TrackData trackData, bool hasDelta) {
        throw new System.NotImplementedException();
    }

    public void Remove() {
        throw new System.NotImplementedException();
    }


    public void ResetTrackData() {
        throw new System.NotImplementedException();
    }

    public bool IsPlayer() {
        throw new System.NotImplementedException();
    }

    public bool IsNpc() {
        throw new System.NotImplementedException();
    }

    public bool IsOtherPlayer() {
        throw new System.NotImplementedException();
    }

    public GameEntityType GetGameEntityType() {
        throw new System.NotImplementedException();
    }

    public Vector3 GetServerLocation() {
        throw new System.NotImplementedException();
    }

    public AnimationController GetAnimationController() {
        throw new System.NotImplementedException();
    }
}
