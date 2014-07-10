using UnityEngine;
using System;
using System.Collections;
using GameMachine;
using GameMachine.Core;
using GameMachine.Chat;
using Entity = GameMachine.Messages.Entity;
using Player = GameMachine.Messages.Player;
using GameMachine.Models.Team;
using ProtoBuf;
using ProtoBuf.Meta;
using System.Reflection;


namespace GameMachine.Example
{

	public class HelloGameMachine : MonoBehaviour, GameMachineApp
	{


		public string
			foo;

		void Start ()
		{
			Login.SetGameMachineApp (this);
			UpdateState ();
		}

		public void OnLoginFailure (string error)
		{
			Logger.Debug ("Authentication Failed: " + error);
		}

		public void ConnectionEstablished ()
		{
			Logger.Debug ("Connection established");
		}

		public void ConnectionTimeout ()
		{
			Logger.Debug ("Connection timed out");
		}

		public void UpdateState ()
		{
			Entity entity = new Entity ();

			GameMachine.Messages.NativeBytes b = new GameMachine.Messages.NativeBytes ();
			string schema = RuntimeTypeModel.Default.GetSchema (typeof(HelloGameMachine));


			//System.Attribute[] attrs = System.Attribute.GetCustomAttributes (typeof(HelloGameMachine));
			Type t = this.GetType ();
			foreach (FieldInfo field in t.GetFields()) {
				foreach (Attribute attr in field.GetCustomAttributes(true)) {
					if (attr is ProtoMemberAttribute) {
						ProtoMemberAttribute a = (ProtoMemberAttribute)attr;
						Debug.Log (a.Tag);
					}
				}
			}

//			foreach (System.Attribute attr in attrs) {
//				Debug.Log (attr.GetType ());
//				if (attr is ProtoMemberAttribute) {
//					ProtoMemberAttribute a = (ProtoMemberAttribute)attr;
//					Debug.Log (a.Name);
//				}
//			}

			//Debug.Log (schema);
			//entity.id = id;
			//entity.nativeBytes = new GameMachine.Messages.NativeBytes();
			//entity.nativeBytes.bytes = "";
		}

		// This is called once we have a connection and everything is started
		public void OnLoggedIn ()
		{
			// Start our chat example
			StartChat ();

			// Start sending/receiving location updates
			StartAreaOfInterest ();
			Logger.Debug ("AreaOfInterest started");
			//Login.regionClient.Connect ("zone", "192.168.1.8");

		}

		void StartAreaOfInterest ()
		{
			GameObject misc = GameObject.Find ("Game");
			misc.AddComponent ("AreaOfInterest");
		}

		void StartChat ()
		{
			GameObject camera = GameObject.Find ("Camera");
			GameObject chatBox = new GameObject ("ChatBox");
			chatBox.transform.parent = camera.transform;
			chatBox.AddComponent ("Chat");

			// Add Teams
			chatBox.AddComponent<TeamUi> ();
		}

	}
}
