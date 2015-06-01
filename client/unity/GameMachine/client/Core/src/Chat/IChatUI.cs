using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using ChatMessage = io.gamemachine.messages.ChatMessage;

namespace GameMachine.Chat {
    public interface IChatUI {

        void LocalMessage(Color color,string message);
        void ShowChatMessage(ChatMessage chatMessage);
        string CurrentGroup();
        void RemoteChannelList(List<string> channels);
    }
}
