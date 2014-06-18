using UnityEngine;
using System.Collections;
using ChatMessage = GameMachine.Messages.ChatMessage;
using GameMachine;

namespace GameMachine.World
{
    public class Chat : MonoBehaviour
    {
        private Component chatGui;
        private Messenger messenger;
        private ChatCommands chatCommands;

        void Start()
        {
            // Gui component.
            chatGui = this.gameObject.AddComponent("FPSChat");

            // The messaging actor
            messenger = ActorSystem.Instance.Find("Messenger") as Messenger;

            // Parses the chat language used by the gui and calls messenger
            // Feel free to provide your own implementation, this is mostly a starting point.
            // Supported syntax is documented in the source
            chatCommands = new ChatCommands(messenger);

            // Setup callacks so we get notified when we join/leave channels and receive messages
            Messenger.ChannelJoined channelCallback = ChannelJoined;
            messenger.OnChannelJoined(channelCallback);

            Messenger.ChannelLeft channelLeftCallback = ChannelLeft;
            messenger.OnChannelLeft(channelLeftCallback);
        
            Messenger.MessageReceived messageCallback = MessageReceived;
            messenger.OnMessageReceived(messageCallback);

            // Join an initial channel.  The second argument is a flags string
            // that sets certain flags on a channel.  Currently 'subscribers' is the
            // one valid option.  If subscribers is set, status updates from the server
            // will include a complete subscriber list for each channel.
            messenger.joinChannel("global", "subscribers");

            // Send this whenever you want a list of subscribed channels, and the optional
            // subscriber list if you have set the subscribers flag.  Remember you get this
            // automatically when you join/leave a channel.
            messenger.ChatStatus();
        }
	
        public void ChannelLeft(string channelName)
        {
            Logger.Debug("Left " + channelName);
            chatGui.SendMessage("receiveMessage", "You have left " + channelName);
        
        }

        public void ChannelJoined(string channelName)
        {
            Logger.Debug("Joined " + channelName);
            chatGui.SendMessage("receiveMessage", "You have joined " + channelName);

        }

        // This is our callback function.  Messenger will send us
        // ChatMessage objects.  We also use this for our chat commands
        // handler, that will send error messages to this function to 
        // be send to the gui. (If we had a C# chat gui this would be cleaner).
        public void MessageReceived(object message)
        {
            string text;
            string name = message.GetType().Name;
            if (name == "string")
            {
                text = message as string;
            } else
            {
                ChatMessage chatMessage = message as ChatMessage;
                text = chatMessage.message;
            }
            Logger.Debug("Chat message " + text);
            chatGui.SendMessage("receiveMessage", text);
        }

        public void SendChatMessage(string message)
        {
            chatCommands.process(User.Instance.username, message);
        }

        void Update()
        {
        
        }
    }
}
