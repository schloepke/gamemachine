using UnityEngine;
using GameMachine;
using System.Collections.Generic;
using System.Reflection;
using System.Collections;
using System;
using System.IO;
using ProtoBuf;
using System.Linq;
using GameMessage = io.gamemachine.messages.GameMessage;
using GameMessages = io.gamemachine.messages.GameMessages;
using Entity = io.gamemachine.messages.Entity;
using DynamicMessage = io.gamemachine.messages.DynamicMessage;

namespace GameMachine.Core
{
	public class GameMessageHandler
	{
		private static string dynamicNamespace = "DynamicMessages";
		private Dictionary<string, GameMachine.Core.Behavior> behaviorGameMessages = new Dictionary<string, GameMachine.Core.Behavior> ();
		private Dictionary<string, PropertyInfo> gameMessageProps = new Dictionary<string, PropertyInfo> ();
		private Dictionary<string, ReliableMessage> reliableMessages = new Dictionary<string, ReliableMessage> ();
		private List<GameMessage> gameMessages = new List<GameMessage> ();
		private List<string> reliableMessageIds = new List<string> ();

		static readonly GameMessageHandler _instance = new GameMessageHandler ();
		public static GameMessageHandler Instance {
			get {
				return _instance;
			}
		}
		
		GameMessageHandler ()
		{
			//ActorSystem.Instance.InvokeRepeating (this, "SendGameMessages");
		}

		public void SendGameMessages ()
		{
			if (reliableMessages.Count >= 1) {
				foreach (ReliableMessage reliableMessage in reliableMessages.Values) {
					if (reliableMessage.attempts >= reliableMessage.maxAttempts) {
						reliableMessageIds.Add (reliableMessage.gameMessage.messageId);
					} else if (reliableMessage.currentTime () - reliableMessage.lastUpdate > 1000.0f) {
						reliableMessage.attempts++;
						reliableMessage.lastUpdate = reliableMessage.currentTime ();
						gameMessages.Add (reliableMessage.gameMessage);
						Debug.Log ("ReliableMessage attempt " + reliableMessage.attempts);
					}
				}
                
				foreach (String messageId in reliableMessageIds) {
					Debug.Log ("Giving up on reliableMessage " + messageId);
					reliableMessages.Remove (messageId);
				}

				reliableMessageIds.Clear ();
			}

			if (gameMessages.Count >= 1) {
				Entity entity = new Entity ();
				entity.id = "gm";
				entity.gameMessages = new GameMessages ();
				foreach (GameMessage gameMessage in gameMessages) {
					entity.gameMessages.gameMessage.Add (gameMessage);
					if (gameMessage.messageId.Length != 0 && !reliableMessages.ContainsKey (gameMessage.messageId)) {
						reliableMessages.Add (gameMessage.messageId, new ReliableMessage (gameMessage));
					}
				}
				gameMessages.Clear ();
				ActorSystem.instance.TellRemote (entity);
			}

		}


		public void Send (object component, object destination, bool reliable)
		{
			GameMessage gameMessage;
			if (component is GameMessage) {
				gameMessage = (GameMessage)component;
			} else {
				gameMessage = new GameMessage ();
				Type t = component.GetType ();
				string field = Char.ToLowerInvariant (t.Name [0]) + t.Name.Substring (1);
				PropertyInfo prop = GameMessageProp (field);
				prop.SetValue (gameMessage, component, null);
			}

			if (destination is string) {
				gameMessage.destination = (string)destination;
			} else {
				gameMessage.destinationId = (int)destination;
			}
			if (reliable) {
				gameMessage.messageId = Guid.NewGuid ().ToString ();
			}
			gameMessages.Add (gameMessage);
            SendGameMessages();
		}

		public void SendReliable (object component, object destination)
		{
			Send (component, destination, true);
		}

		public void Send (object component, object destination)
		{
			Send (component, destination, false);
		}

		public DynamicMessage toDynamicMessage (object component)
		{
			DynamicMessage wrapper = new DynamicMessage ();
			Type t = component.GetType ();
			wrapper.type = t.Name;
			wrapper.message = Serialize (component);
			return wrapper;
		}

		public void SendDynamicMessage (object component, object destination)
		{
			Send (toDynamicMessage (component), destination);
		}

		public void SendDynamicMessageReliable (object component, object destination)
		{
			SendReliable (toDynamicMessage (component), destination);
		}

		public object Deserialize (byte[] bytes, Type t)
		{
			MemoryStream stream = new MemoryStream (bytes);
			return Serializer.NonGeneric.Deserialize (t, stream);
		}
        
		public byte[] Serialize (object message)
		{
			MemoryStream stream = new MemoryStream ();
			Serializer.Serialize (stream, message);
			return stream.ToArray ();
		}

		public void Register (GameMachine.Core.Behavior behavior, params string[] names)
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

        private void DeliverMessage(GameMessage gameMessage, bool hasError) {
            PropertyInfo prop;
            foreach (string componentName in behaviorGameMessages.Keys) {
                prop = GameMessageProp(componentName);
                object component = prop.GetValue(gameMessage, null);
                if (component != null) {
                    if (component is DynamicMessage) {
                        DynamicMessage dynamicMessage = (DynamicMessage)component;
                        Type t = Type.GetType(dynamicNamespace + "." + dynamicMessage.type);
                        object userObject = Deserialize(dynamicMessage.message, t);
                        behaviorGameMessages[componentName].OnMessage(userObject);
                    } else {
                        behaviorGameMessages[componentName].OnMessage(component);
                    }
                }
            }
        }

		public void DeliverMessages (GameMessages gameMessages)
		{
			

			foreach (GameMessage gameMessage in gameMessages.gameMessage) {
				if (gameMessage.messageId.Length != 0) {
					if (reliableMessages.ContainsKey (gameMessage.messageId)) {
						reliableMessages.Remove (gameMessage.messageId);
						//Logger.Debug ("Reliable message received " + gameMessage.messageId);
					} else {
						//Logger.Debug ("Duplicate Reliable message " + gameMessage.messageId);
						continue;
					}
				}
                DeliverMessage(gameMessage, false);

			}
		}
	}
}
