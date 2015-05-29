using UnityEngine;
using System.Collections;
using ChatMessage = io.gamemachine.messages.ChatMessage;

namespace GameMachine.Chat {
    public interface IChatUI {

        void LocalMessage(Color color,string message);
        void RemoteMessage(ChatMessage chatMessage);
    }
}
