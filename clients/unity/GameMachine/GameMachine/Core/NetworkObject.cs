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
	
	public class NetworkObject : MonoBehaviour
	{

		private static string strategy = "grid";
		private static string channel;
		public int scope;
		public string playerId;
		private List<Component> components = new List<Component> ();
		public static Dictionary<string, RuntimeTypeModel> typeModels = new Dictionary<string, RuntimeTypeModel> ();
		public static Dictionary<string, MetaType> metaTypes = new Dictionary<string, MetaType> ();
		public static Dictionary<int, List<NetworkObject>> views = new Dictionary<int, List<NetworkObject>> ();
		public static Dictionary<int, string> prefabs = new Dictionary<int, string> ();
		private EntityTracking entityTracking;
		public float updateInterval = 0.06f;

//		public static void SetStrategy (string strategy, string channel)
//		{
//			ProtoModelManager.strategy = strategy;
//			ProtoModelManager.channel = channel;
//		}
//
//		public static void SetStrategy (string strategy)
//		{
//			ProtoModelManager.strategy = strategy;
//		}
//
		public static void RegisterPrefab (int scope, string prefab)
		{
			prefabs [scope] = prefab;
		}

		public static void AddView (NetworkObject view)
		{
			if (!views.ContainsKey (view.scope)) {
				views [view.scope] = new List<NetworkObject> ();
			} 
			views [view.scope].Add (view);
		}

		public static void RemoveView (NetworkObject view)
		{
			if (views.ContainsKey (view.scope)) {
				views [view.scope].Remove (view);
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

		private void AddComponents ()
		{
			foreach (Component component in this.gameObject.GetComponents<Component>()) {
				if (typeModels.ContainsKey (component.GetType ().Name)) {
					components.Add (component);
				}
			}
		}

		private object Deserialize (byte[] bytes, Type t)
		{
			MemoryStream stream = new MemoryStream (bytes);
			return Serializer.NonGeneric.Deserialize (t, stream);
		}

		public byte[] Serialize (object component)
		{
			MemoryStream stream = new MemoryStream ();
			Serializer.Serialize (stream, component);
			return stream.ToArray ();
		}

		public void SyncComponents (UnityGameObject unityGameObject)
		{

			int i = 0;
			foreach (byte[] bytes in unityGameObject.byteArray) {
				Type t = components [i].GetType ();
				RuntimeTypeModel model = typeModels [t.Name];
				object proxy = Deserialize (bytes, t);
				MetaType metaType = metaTypes [t.Name];
				foreach (ValueMember valueMember in metaType.GetFields()) {
					string propertyName = valueMember.Name;
					PropertyInfo proxyProp = proxy.GetType ().GetProperty (propertyName);
					object component = components [i];
					PropertyInfo componentProp = component.GetType ().GetProperty (propertyName);
					object value = proxyProp.GetValue (proxy, null);
					componentProp.SetValue (component, value, null);
				}
				i++;
			}
		}

		public UnityGameObject ToUnityGameObject ()
		{
			UnityGameObject unityGameObject = new UnityGameObject ();
			foreach (Component component in components) {
				byte[] bytes = Serialize (component);
				unityGameObject.byteArray.Add (bytes);
				unityGameObject.scope = scope;
			}
			return unityGameObject;
		}

		public static void SyncGameMachineViews (string playerId, UnityGameObjects unityGameObjects)
		{
			foreach (UnityGameObject unityGameObject in unityGameObjects.unityGameObject) {
				if (views.ContainsKey (unityGameObject.scope)) {
					foreach (NetworkObject gameMachineView in views[unityGameObject.scope]) {
						gameMachineView.SyncComponents (unityGameObject);
					}
				} else {
					if (prefabs.ContainsKey (unityGameObject.scope)) {
						GameObject obj = (GameObject)Instantiate (Resources.Load ("TestGameObject"));
						NetworkObject view = obj.GetComponent<NetworkObject> ();
						view.scope = unityGameObject.scope;
						view.playerId = playerId;
						view.SyncComponents (unityGameObject);
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
		}

		void Start ()
		{
			NetworkObject.RegisterPrefab (2, "TestGameObject");
			NetworkObject.AddBuiltinModels ();
			NetworkObject.AddComponent ("SampleNetworkObject", "id", "v", "test");
			AddView (this);
			if (playerId == "me") {
				Test ();
			}

			//GameMachine.Core.App.onRunning (this, "StartNetworkObjectManager");

		}

		public void Test ()
		{

			for (int i = 0; i < 10; i++) {
				UnityGameObjects unityGameObjects = new UnityGameObjects ();
				UnityGameObject unityGameObject = ToUnityGameObject ();
				unityGameObject.scope = 2;
				unityGameObjects.unityGameObject.Add (unityGameObject);
				string player = "player_" + i.ToString ();
				SyncGameMachineViews (player, unityGameObjects);
			}
		}

		public void StartNetworkObjectManager ()
		{

			
			
//			SampleNetworkObject a = this.gameObject.AddComponent<SampleNetworkObject> ();
//			SampleNetworkObject b = this.gameObject.AddComponent<SampleNetworkObject> ();
//			b.id = "testing";
//						
//			
//			entityTracking = ActorSystem.Instance.Find ("EntityTracking") as EntityTracking;
//			EntityTracking.UpdateReceived callback = OnUpdateReceived;
//			entityTracking.OnUpdateReceived (callback);
//			InvokeRepeating ("SendNetworkObjects", updateInterval, updateInterval);
		}

		void SendTrackingUpdate (NativeBytes nativeBytes)
		{
			
//			Vector3 position = GameMachineView.currentPosition;
//
//		
//			TrackingUpdate update = new TrackingUpdate (User.Instance.username, position.x, position.z, position.y);
//			update.entityType = "player";
//			TrackData trackData = new TrackData ();
//			update.neighborEntityType = GameMachineView.neighborType;
//			update.trackData = trackData;
//			entityTracking.Update (update);
		}
		

	}
}