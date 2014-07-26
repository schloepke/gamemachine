using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using TrackData = GameMachine.Messages.TrackData;
using GameMachine.Core;
using GameMachine.Tutorials.Miniworld;

namespace GameMachine.Tutorials.Miniworld
{
	public class EntityManager : MonoBehaviour, GameMachine.Trackable
	{

		private KAM3RA.User cameraUser;
		private GameObject player;
		private GameMachine.Messages.Vector3 directionVector = new GameMachine.Messages.Vector3 ();
		public static Dictionary<string, OtherPlayer> players = new Dictionary<string, OtherPlayer> ();

		void Start ()
		{
			player = GameObject.Find (User.Instance.username);
			cameraUser = GameObject.Find ("KAM3RA").GetComponent<KAM3RA.User> ();

			// Tell the entity tracking system to call back to us
			EntityTracking.Register (this);

			// Remove other players where we have not received any updates on them for a while

			InvokeRepeating ("RemoveStalePlayers", 2, 1.5F);
		}

		public void RemoveStalePlayers ()
		{
			var itemsToRemove = players.Where (f => (Time.time - f.Value.lastUpdate) > 1.5f).ToArray ();
			foreach (var item in itemsToRemove) {
				Destroy (item.Value.gameObject);
				players.Remove (item.Key);
			}
		}

		public void TrackDataReceived (List<TrackData> trackDatas)
		{


			foreach (TrackData trackData in trackDatas) {
				if (trackData.id == GameMachine.Core.User.Instance.username) {
					continue;
				}
				if (players.ContainsKey (trackData.id)) {
					Vector3 target = new Vector3 (trackData.x, trackData.z, trackData.y);
					Vector3 direction = new Vector3 (trackData.direction.x, trackData.direction.y, trackData.direction.z);
					players [trackData.id].UpdatePlayer (target, direction, trackData.speed, trackData.action);
				} else {
					GameObject gameObject = (GameObject)Instantiate (Resources.Load ("OtherPlayerCharacter"));
					OtherPlayer otherPlayer = gameObject.GetComponent<OtherPlayer> () as OtherPlayer;
					otherPlayer.name = trackData.id;
					gameObject.name = trackData.id;
					otherPlayer.transform.position = otherPlayer.SpawnLocation (new Vector3 (trackData.x, 0f, trackData.y));
					players [trackData.id] = otherPlayer;
				}
			}

		}

		public TrackData UpdateTracking ()
		{
			TrackData trackData = new TrackData ();

			directionVector.x = cameraUser.Direction.x;
			directionVector.z = cameraUser.Direction.z;
			//directionVector.y = cameraUser.Direction.y;
			trackData.direction = directionVector;

			if (Input.GetAxis ("Jump") != 0f) {
				trackData.action = 1;
			}
			trackData.x = player.transform.position.x;
			trackData.y = player.transform.position.z;
			trackData.z = player.transform.position.y;

			return trackData;
		}
	}
}
