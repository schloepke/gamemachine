using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine.Chat {
    public interface ChatApi {

        void Speed(int speed);
        void Stuck();
        string GuildName();
        void GuildCreate(string id, string name);
        void GuildDestroy();
        void GuildInvite(string characterId);
        void GuildLeave();
        void GuildMembers();
        void GuildList();
        void GuildMessage(string message);
        bool IsGuildChannel(string channel);
        void InviteToChannel(string invitee, string channel);
        string ChatUser();
        void Territories();
        void ChannelWindowClosed();
        void ChannelWindowOpened();
        void ChannelList(List<string> channels);
        
        void ShowHelp();

    }
}
