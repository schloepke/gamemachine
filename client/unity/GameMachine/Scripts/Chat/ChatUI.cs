using UnityEngine;
using UnityEngine.UI;
using System.Linq;
using System.Collections;
using System.Collections.Generic;
using GameMachine.Common;
using GameMachine.Core;
using GameMachine.HttpApi;
using ChatMessage = io.gamemachine.messages.ChatMessage;
using ChatInvite = io.gamemachine.messages.ChatInvite;

namespace GameMachine.Chat {
    public class ChatUI : MonoBehaviour, IChatUI, IPlayerApi {

        public List<string> autojoinChannels = new List<string>();
        public string currentGroup;
        public int maxMessages = 2;
        public bool testmode = false;
        private Messenger messenger = null;
        private ChatCommandHandler chatCommandHandler;
        private InputField chatInput;
        private GameObject contentHolder;
        private GameObject messageTemplate;
        private Canvas canvas;

        private GameObject groupContainer;
        private GameObject groupMemberContainer;
        private GameObject groupMemberTemplate;
        private Queue<GameObject> messages = new Queue<GameObject>();
        private string playerId;
        private string characterId;
        private Dictionary<string, GameObject> groupMembers = new Dictionary<string, GameObject>();
        private bool chatActive = false;
        private bool groupWindowActive = false;
        
        void Start() {
            if (!GamePlayer.IsNetworked()) {
                Destroy(this.gameObject);
                return;
            }

            canvas = gameObject.GetComponent<Canvas>() as Canvas;
            canvas.enabled = false;
            contentHolder = GameObject.Find("chat_text");
            messageTemplate = transform.Find("msg_template").gameObject;
            messageTemplate.SetActive(false);

            groupContainer = GameObject.Find("group_container");
            groupMemberContainer = GameObject.Find("group_member_container");
            groupMemberTemplate = GameObject.Find("group_member");
            groupMemberTemplate.SetActive(false);
            groupContainer.SetActive(false);

            chatInput = GameObject.Find("chat_input").GetComponent<InputField>() as InputField;

            playerId = GameEntityManager.GetPlayerEntity().GetEntityId();
            characterId = GameEntityManager.GetPlayerEntity().GetCharacterId();
            messenger = ActorSystem.instance.Find("Messenger") as Messenger;

            chatCommandHandler = new ChatCommandHandler(messenger, this, playerId);

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

            InvokeRepeating("UpdateChatStatus", 0.01f, 2.0F);
        }

        void Update() {
            if (chatInput.isFocused) {
                InputState.SetInput("chat", true);
            } else {
                InputState.SetInput("chat", false);
                if (!InputState.KeyInputActive() && Input.GetKeyDown(KeyBinds.Binding("Chat"))) {
                    if (chatActive) {
                        canvas.enabled = false;
                        InputState.chatfocus = false;
                        InputState.dragging = false;
                        chatActive = false;
                    } else {
                        canvas.enabled = true;
                        chatActive = true;
                    }
                }
            }
        }


        public void PointerEnter() {
            InputState.chatfocus = true;
        }

        public void PointerExit() {
            InputState.chatfocus = false;
        }

        public void StartDrag() {
            InputState.dragging = true;
        }

        public void EndDrag() {
            InputState.dragging = false;
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

        public void ShowChatMessage(ChatMessage chatMessage) {
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

        public void RemoteChannelList(List<string> channels) {
            LocalMessage(Color.white, "Channel list");
            foreach (string channel in channels) {
                if (channel.StartsWith("priv")) {
                    continue;
                }
                LocalMessage(Color.white, channel);
            }
        }
        private void UpdateChatStatus() {
            messenger.ChatStatus();
            UpdateGroupMembers();
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

        private void OpenGroupWindow() {
            if (groupWindowActive) {
                return;
            }
            groupContainer.SetActive(true);
            groupWindowActive = true;
        }

        private void CloseGroupWindow() {
            if (!groupWindowActive) {
                return;
            }
            List<string> local = new List<string>(groupMembers.Keys);
            foreach (string memberName in local) {
                RemoveGroupMember(memberName);
            }
            groupContainer.SetActive(false);
            groupWindowActive = false;
        }

        private void AddGroupMember(string playerId, string characterId) {
            if (!groupWindowActive) {
                OpenGroupWindow();
            }

            GameObject go = GameObject.Instantiate(groupMemberTemplate);
            go.SetActive(true);
            go.transform.SetParent(groupMemberContainer.transform);
            Text text = go.transform.Find("Text").GetComponent<Text>() as Text;
            text.text = characterId;
            groupMembers[playerId] = go;
        }

        private void RemoveGroupMember(string playerId) {
            GameObject go = groupMembers[playerId];
            groupMembers.Remove(playerId);
            Destroy(go);
        }

        public void RequestLeaveGroup() {
            messenger.leaveChannel(Messenger.SanitizeChannelName(currentGroup));
        }

        public void LeaveGroup(string channelName) {
            LocalMessage(Color.white, "You have left your group");
            CloseGroupWindow();
            currentGroup = null;
        }

        void UpdateGroupMembers() {

            if (currentGroup == null) {
                return;
            }

            if (messenger.subscribers.ContainsKey(currentGroup)) {
                foreach (string member in messenger.subscribers[currentGroup]) {
                    if (member == playerId) {
                        continue;
                    }
                    if (!groupMembers.ContainsKey(member)) {
                        PlayerApi.instance.GetPlayer(member, this);

                    }
                }

                List<string> local = new List<string>(groupMembers.Keys);
                List<string> channelsToRemove = local.Except(messenger.subscribers[currentGroup]).ToList();

                foreach (string memberName in channelsToRemove) {
                    RemoveGroupMember(memberName);
                }
            } else {
                CloseGroupWindow();
            }
        }

        public void CreateGroup(string channelName) {
            currentGroup = channelName;
            LocalMessage(Color.white, "You have joined a group");
            AddGroupMember(playerId, characterId);
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
            ShowChatMessage(chatMessage);
        }

        public string CurrentGroup() {
            return currentGroup;
        }



        public void OnPlayerCreated(io.gamemachine.messages.Player player) {
            throw new System.NotImplementedException();
        }

        public void OnPlayerCreateError(string error) {
            throw new System.NotImplementedException();
        }

        public void OnPlayer(io.gamemachine.messages.Player player) {
            AddGroupMember(player.id, player.characterId);
        }

        public void OnPlayerError(string error) {
            throw new System.NotImplementedException();
        }

        public void OnPasswordChanged(string result) {
            throw new System.NotImplementedException();
        }

        public void OnPasswordError(string error) {
            throw new System.NotImplementedException();
        }

        public void OnPlayerCharacters(io.gamemachine.messages.Characters characters) {
            throw new System.NotImplementedException();
        }

        public void OnPlayerCharactersError(string error) {
            throw new System.NotImplementedException();
        }
    }
}
