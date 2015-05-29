using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using GameMachine.Common;
using GameMachine.Core;
using ChatMessage = io.gamemachine.messages.ChatMessage;
using ChatInvite = io.gamemachine.messages.ChatInvite;

namespace GameMachine.Chat {
    public class ChatUI : MonoBehaviour, IChatUI {

        public List<string> autojoinChannels = new List<string>();
        public string currentGroup;
        public int maxMessages = 2;
        public bool testmode = false;
        private Messenger messenger = null;
        private ChatCommandHandler chatCommandHandler;
        private ChatApi chatApi;
        private InputField chatInput;
        private GameObject contentHolder;
        private GameObject messageTemplate;
        private GameObject scrollbar;
        private Queue<GameObject> messages = new Queue<GameObject>();
        private string playerId;
        
        void Start() {
            scrollbar = GameObject.Find("chat_scrollbar");
            contentHolder = GameObject.Find("chat_text");
            messageTemplate = transform.Find("msg_template").gameObject;
            messageTemplate.SetActive(false);
            chatInput = GameObject.Find("chat_input").GetComponent<InputField>() as InputField;
            
            chatApi = new DefaultChatImpl();

            if (testmode) {
                playerId = "TestUser";
                chatCommandHandler = new ChatCommandHandler(messenger, this, chatApi, playerId, testmode);
            } else {
                playerId = GameEntityManager.GetPlayerEntity().GetEntityId();
                messenger = ActorSystem.Instance.Find("Messenger") as Messenger;

                chatCommandHandler = new ChatCommandHandler(messenger, this, chatApi, playerId, testmode);

                Messenger.ChannelJoined channelCallback = ChannelJoined;
                messenger.OnChannelJoined(channelCallback);

                Messenger.ChannelLeft channelLeftCallback = ChannelLeft;
                messenger.OnChannelLeft(channelLeftCallback);

                Messenger.MessageReceived messageCallback = MessageReceived;
                messenger.OnMessageReceived(messageCallback);

                Messenger.InviteReceived inviteCallback = InviteReceived;
                messenger.OnInviteReceived(inviteCallback);

                foreach (string channel in autojoinChannels) {
                    messenger.JoinChannel(channel);
                }
                
                InvokeRepeating("UpdateChatStatus", 0.01f, 5.0F);
            }
        }

        void Update() {
            if (chatInput.isFocused) {
                InputManager.typing = true;
            } else {
                InputManager.typing = false;
            }
        }

        public void PointerEnter() {
            InputManager.chatfocus = true;
        }

        public void PointerExit() {
            InputManager.chatfocus = false;
        }

        public void StartDrag() {
            InputManager.chatdragging = true;
        }

        public void EndDrag() {
            InputManager.chatdragging = false;
        }

        public void AddMessage() {
            if (!string.IsNullOrEmpty(chatInput.text)) {
                if (testmode) {
                    LocalMessage(Color.green, chatInput.text);
                } else {
                    chatCommandHandler.process(chatInput.text);
                }
            }
            chatInput.text = "";
        }


        public void LocalMessage(Color color, string msg) {
            if (messages.Count >= maxMessages) {
                GameObject old = messages.Dequeue();
                Destroy(old);
            }

            GameObject go = GameObject.Instantiate(messageTemplate);
            go.transform.SetParent(contentHolder.transform);
            Text text = go.GetComponent<Text>() as Text;
            text.color = color;
            text.text = msg;
            messages.Enqueue(go);
            go.SetActive(true);
        }

        public void RemoteMessage(ChatMessage chatMessage) {
            string text;
            Color color;
            string channel = "local";

            string channelName = chatMessage.chatChannel.name;
            text = chatMessage.senderId + ": " + chatMessage.message;

            if (chatMessage.type == "group") {
                if (chatMessage.chatChannel.name.StartsWith("priv_")) {
                    color = Color.magenta;
                    channel = "group";
                } else {
                    color = Color.green;
                    channel = channelName;
                }

            } else {
                color = Color.white;
            }
            text = "[" + channel + "] " + text;

            LocalMessage(color, text);
        }

        private void UpdateChatStatus() {
            messenger.ChatStatus();
        }

        public void InviteReceived(object message) {
            ChatInvite chatInvite = message as ChatInvite;
            messenger.JoinChannel(chatInvite.channelName, chatInvite.invite_id);
        }

        public void ChannelLeft(string channelName) {
            if (channelName.StartsWith("priv_")) {
                if (channelName.EndsWith("group")) {
                    LeaveGroup(channelName);
                }
            } else {
                LocalMessage(Color.yellow, "You have left channel " + channelName);
            }
        }

        public void LeaveGroup(string channelName) {
            LocalMessage(Color.white, "You have left your group");
        }


        public void CreateGroup(string channelName) {
            ChannelUi.DestroyChannelUi(channelName);
            ChannelUi.CreateChannelUi(chatApi, this.gameObject, channelName, "Group");
            currentGroup = channelName;
            LocalMessage(Color.white, "You have joined a group");
        }

        public void ChannelJoined(string channelName) {
            // private message
            if (channelName.StartsWith("priv_")) {
                // private group
                if (channelName.EndsWith("group")) {
                    CreateGroup(channelName);
                }
            } else {
                LocalMessage(Color.white, "You have joined channel " + channelName);
            }
        }

      
        public void MessageReceived(object message) {
            ChatMessage chatMessage = message as ChatMessage;
            RemoteMessage(chatMessage);
        }

       
    }
}
