using UnityEngine;
using System.Collections;
using GameMachine;

// This class loads a UI component and sets up the callbacks to tie the UI to the messaging system.

// Note that the chat system is built using our messaging system, which uses a publish/subscribe model.  This makes it
// simple to use not just for chat but for general grouping amd matchmaking as well. 

// To create a simple matchmaking system, just create a channel to use as the lobby channel.  When players leave
// the lobby to join a game, simply have them leave the lobby channel and join the game specific channel. 
// TODO  The above requires that we expose the subscriber list for each channel in Messenger.  We are receiving that data
// but Messenger is not currently extracting it and making it available.



// To replace the UI with you own you will need to provide your own UI game object with
// a method for receiving and sending messages.  This example uses a javascript ui, so we have to use
// the SendMessage functionality to pass messages back and forth.

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

        // Join an initial channel.  You do not have to do this here, it's just
        // to show how you can join channels via code.
        messenger.joinChannel("global");
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
    
    public void MessageReceived(string message)
    {
        Logger.Debug("Chat message " + message);
        chatGui.SendMessage("receiveMessage", message);
    }

    public void SendChatMessage(string message)
    {
        chatCommands.process(User.Instance.username, message);
    }

    void Update()
    {
        
    }
}
