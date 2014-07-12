using System.Collections;
using System;
using UnityEngine;
using  System.Collections.Generic;
using System.Reflection;
using GameMachine;
using GameMachine.Models;
using Entity = GameMachine.Messages.Entity;
using NativeBytes = GameMachine.Messages.NativeBytes;
using TrackData = GameMachine.Messages.TrackData;
using UnityGameObject = GameMachine.Messages.UnityGameObject;
using UnityGameObjects = GameMachine.Messages.UnityGameObjects;
using Newtonsoft.Json;
using ProtoBuf;
using ProtoBuf.Meta;
using System.IO;
using System.Reflection;

namespace GameMachine.Core
{
	
	public class NetworkObjectManager : MonoBehaviour
	{
		
		private static string strategy = "grid";
		private static string channel;
		private static string gridNeighborType;
		public static Vector3 gridPosition = new Vector3 ();

		public static Dictionary<string, RuntimeTypeModel> typeModels = new Dictionary<string, RuntimeTypeModel> ();
		public static Dictionary<string, MetaType> metaTypes = new Dictionary<string, MetaType> ();
		public static Dictionary<int, List<NetworkObject>> networkObjects = new Dictionary<int, List<NetworkObject>> ();
		public static Dictionary<int, string> prefabs = new Dictionary<int, string> ();
		private EntityTracking entityTracking;
		public float updateInterval = 0.06f;

		public static void SetGridPosition (Vector3 gridPosition)
		{
			NetworkObjectManager.gridPosition = gridPosition;
		}

		public static void SetChatStrategy (string strategy, string channel)
		{
			NetworkObjectManager.strategy = strategy;
			NetworkObjectManager.channel = channel;
		}
		
		public static void SetGridStrategy (string strategy, string neighborType)
		{
			NetworkObjectManager.strategy = strategy;
			NetworkObjectManager.gridNeighborType = neighborType;
		}
		
		public static void RegisterPrefab (string prefab, int scope)
		{
			prefabs [scope] = prefab;
		}
		
		public static void AddView (NetworkObject view)
		{
			if (!networkObjects.ContainsKey (view.scope)) {
				networkObjects [view.scope] = new List<NetworkObject> ();
			} 
			networkObjects [view.scope].Add (view);
		}
		
		public static void RemoveView (NetworkObject view)
		{
			if (networkObjects.ContainsKey (view.scope)) {
				networkObjects [view.scope].Remove (view);
			} 
		}
		
		public static void AddComponent (params string[] memberNames)
		{
			string componentName = memberNames [0];
			RuntimeTypeModel model = RuntimeTypeModel.Default;
			MetaType metaType = model.Add (Type.GetType (componentName), false);
			metaType.UseConstructor = false;
			for (int i = 1; i < memberNames.Length; i++) {
				metaType.Add (memberNames [i]);
			}
			metaTypes [componentName] = metaType;
			typeModels [componentName] = model;
		}

		public static void SyncNetworkObjects (string playerId, UnityGameObjects unityGameObjects)
		{
			foreach (UnityGameObject unityGameObject in unityGameObjects.unityGameObject) {
				if (networkObjects.ContainsKey (unityGameObject.scope)) {
					foreach (NetworkObject gameMachineView in networkObjects[unityGameObject.scope]) {
						gameMachineView.SyncComponents (unityGameObject);
					}
				} else {
					if (prefabs.ContainsKey (unityGameObject.scope)) {
						GameObject obj = (GameObject)Instantiate (Resources.Load ("TestGameObject"));
						NetworkObject networkObject = obj.GetComponent<NetworkObject> ();
						networkObject.scope = unityGameObject.scope;
						networkObject.playerId = playerId;
						networkObject.SyncComponents (unityGameObject);
					}
					
				}
			}
		}
		
		private static void AddBuiltinModels ()
		{
			RuntimeTypeModel model = RuntimeTypeModel.Default;
			model.Add (typeof(Vector3), false).Add ("x", "y", "z");
			model.Add (typeof(Vector2), false).Add ("x", "y");
			model.Add (typeof(Vector4), false).Add ("x", "y", "z", "w");
			model.Add (typeof(Quaternion), false).Add ("x", "y", "z", "w");
		}
		
		void OnUpdateReceived (List<TrackingUpdate> updates)
		{
			foreach (TrackingUpdate trackingUpdate in updates) {
				if (trackingUpdate.entityId != User.Instance.username && trackingUpdate.trackData.unityGameObjects != null) {
					NetworkObjectManager.SyncNetworkObjects (trackingUpdate.trackData.id, trackingUpdate.trackData.unityGameObjects);
				}

			}
		}

		void Awake ()
		{
			NetworkObjectManager.AddBuiltinModels ();
		}

		void Start ()
		{
			entityTracking = ActorSystem.Instance.Find ("EntityTracking") as EntityTracking;
			EntityTracking.UpdateReceived callback = OnUpdateReceived;
			entityTracking.OnUpdateReceived (callback);
			InvokeRepeating ("SendUnityGameObjects", updateInterval, updateInterval);

		}
		

		
		public void SendUnityGameObjects ()
		{
			int gameObjectCount = 0;
			string username = User.Instance.username;
			UnityGameObjects unityGameObjects = new UnityGameObjects ();
			foreach (int scope in networkObjects.Keys) {
				foreach (NetworkObject networkObject in networkObjects[scope]) {
					if (networkObject.playerId == username) {
						unityGameObjects.unityGameObject.Add (networkObject.ToUnityGameObject ());
						gameObjectCount++;
					}
				}
			}

			if (strategy == "grid") {
				TrackingUpdate update = new TrackingUpdate (username, gridPosition.x, gridPosition.z, gridPosition.y);
				update.entityType = "player";
				TrackData trackData = new TrackData ();
				if (gameObjectCount >= 1) {
					trackData.unityGameObjects = unityGameObjects;
				}
				update.neighborEntityType = gridNeighborType;
				update.trackData = trackData;
				entityTracking.Update (update);
			}

		}

	}
}