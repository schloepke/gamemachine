using System.Collections;
using System;
using UnityEngine;
using  System.Collections.Generic;
using System.Reflection;
using ProtoBuf;
using ProtoBuf.Meta;
using System.IO;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using NativeBytes = GameMachine.Messages.NativeBytes;
using TrackData = GameMachine.Messages.TrackData;
using UnityGameObject = GameMachine.Messages.UnityGameObject;
using UnityGameObjects = GameMachine.Messages.UnityGameObjects;


namespace GameMachine.Core
{
	
	public class NetworkObject : MonoBehaviour
	{

		public int scope;
		public string playerId;
		private List<Component> components = new List<Component> ();

		private void AddComponents ()
		{
			foreach (Component component in this.gameObject.GetComponents<Component>()) {
				if (NetworkObjectManager.typeModels.ContainsKey (component.GetType ().Name)) {
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
				RuntimeTypeModel model = NetworkObjectManager.typeModels [t.Name];
				object proxy = Deserialize (bytes, t);
				MetaType metaType = NetworkObjectManager.metaTypes [t.Name];
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

		void Start ()
		{
			NetworkObjectManager.AddView (this);
		}


	}
}