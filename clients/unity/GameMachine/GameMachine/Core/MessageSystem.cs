using GameMachine;
using System.Collections.Generic;
using System.Reflection;
using System.Collections;
using System;
using System.IO;
using ProtoBuf;
using System.Linq;
using GameMessage = GameMachine.Messages.GameMessage;
using GameMessages = GameMachine.Messages.GameMessages;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine.Core
{
	public class MessageSystem
	{
		private Dictionary<string, GameMachine.Core.Behavior> behaviorGameMessages = new Dictionary<string, GameMachine.Core.Behavior> ();
		private Dictionary<string, PropertyInfo> gameMessageProps = new Dictionary<string, PropertyInfo> ();
		private List<GameMessage> gameMessages = new List<GameMessage> ();

		static readonly MessageSystem _instance = new MessageSystem ();
		public static MessageSystem Instance {
			get {
				return _instance;
			}
		}
		
		MessageSystem ()
		{
			ActorSystem.Instance.InvokeRepeating (this, "SendGameMessages");
		}

		public void SendGameMessages ()
		{
			if (gameMessages.Count >= 1) {
				Entity entity = new Entity ();
				entity.id = "gm";
				entity.gameMessages = new GameMessages ();
				foreach (GameMessage gameMessage in gameMessages) {
					entity.gameMessages.gameMessage.Add (gameMessage);
				}
				gameMessages.Clear ();
				ActorSystem.Instance.TellRemote (entity);
			}

		}

		private GameMessage CurrentGameMessage ()
		{
			GameMessage gameMessage;
			if (gameMessages.Count >= 1) {
				gameMessage = gameMessages.Last ();
			} else {
				gameMessage = new GameMessage ();
				gameMessages.Add (gameMessage);
			}
			return gameMessage;
		}

		public void Send (object component)
		{
			GameMessage currentGameMessage = CurrentGameMessage ();
			Type t = component.GetType ();
			string field = Char.ToLowerInvariant (t.Name [0]) + t.Name.Substring (1);
			PropertyInfo prop = GameMessageProp (field);
			object current = prop.GetValue (currentGameMessage, null);
			if (current == null) {
				prop.SetValue (currentGameMessage, component, null);
			} else {
				gameMessages.Add (currentGameMessage);
				currentGameMessage = CurrentGameMessage ();
				prop.SetValue (currentGameMessage, component, null);
			}
		}

		public void Send (GameMessage gameMessage)
		{
			gameMessages.Add (gameMessage);
		}

		public void RegisterBehavior (GameMachine.Core.Behavior behavior, params string[] names)
		{
			for (int i = 0; i < names.Length; i++) {
				string name = Char.ToLowerInvariant (names [i] [0]) + names [i].Substring (1);
				behaviorGameMessages [name] = behavior;
			}
		}

		private PropertyInfo GameMessageProp (string componentName)
		{
			PropertyInfo prop;
			if (gameMessageProps.ContainsKey (componentName)) {
				prop = gameMessageProps [componentName];
			} else {
				GameMessage gameMessage = new GameMessage ();
				prop = gameMessage.GetType ().GetProperty (componentName);
				gameMessageProps [componentName] = prop;
			}
			return prop;
		}

		public void DeliverMessages (GameMessages gameMessages)
		{
			PropertyInfo prop;
			foreach (string componentName in behaviorGameMessages.Keys) {
				foreach (GameMessage gameMessage in gameMessages.gameMessage) {
					prop = GameMessageProp (componentName);
					object component = prop.GetValue (gameMessage, null);
					if (component != null) {
						behaviorGameMessages [componentName].OnMessage (component);
					}
				}

			}
		}
	}
}
