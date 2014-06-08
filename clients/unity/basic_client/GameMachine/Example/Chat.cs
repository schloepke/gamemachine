using UnityEngine;
using System.Collections;
using ChatMessage = GameMachine.Messages.ChatMessage;
using GameMachine;

// This class loads a UI component and sets up the callbacks to tie the UI to the messaging system.

// Note that the chat system is built using our messaging system, which uses a publish/subscribe model.  This makes it
// simple to use not just for chat but for general grouping amd matchmaking as well. 

// When joining and leaving channels, the server will send you a complete list of the channels you are subscribed to.
//  This can be accessed by calling messenger.subscriptions.  If you have set the flags for the channel to return
// subscribers (see below), the update from the server will include a complete subscriber list for each channel.
// You can access the subscribers list by calling messenger.subscribers, which returns a dictionary where the key
// is the channel name, and the value is a list of subscribers.

//  You can use the messenger system to create matchmaking systems or for groups.  It is designed for any kind of
// group based messeging that you need.  Chat messages are also not restricted to just text.  You can create chat
// messages with an attached entity, that can contain anything an entity can hold, which is literally anything.
// To send advanced messages use the SendChatMessage function instead of SendMessage. 



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

        // These are example of the functions available now that the system is running.


        // Join an initial channel.  The second argument is a flags string
        // that sets certain flags on a channel.  Currently 'subscribers' is the
        // one valid option.  If subscribers is set, status updates from the server
        // will include a complete subscriber list for each channel.
        messenger.joinChannel("global", "subscribers");

        //  
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
