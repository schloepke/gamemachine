using UnityEngine;
using System.Collections;
using GameMachine.Common;

namespace GameMachine.Chat {
    public class DefaultChatImpl : ChatApi {

        public void Speed(int speed) {
            throw new System.NotImplementedException();
        }

        public void Stuck() {
            throw new System.NotImplementedException();
        }

        public void GuildDestroy() {
            throw new System.NotImplementedException();
        }

        public void GuildInvite(string characterId) {
            throw new System.NotImplementedException();
        }

        public void GuildLeave() {
            throw new System.NotImplementedException();
        }

        public void GuildMembers() {
            throw new System.NotImplementedException();
        }

        public void GuildList() {
            throw new System.NotImplementedException();
        }

        public void GuildMessage(string message) {
            throw new System.NotImplementedException();
        }

        public bool IsGuildChannel(string channel) {
            return false;
        }

        public void InviteToChannel(string invitee, string channel) {
            throw new System.NotImplementedException();
        }


        public string ChatUser() {
            return GameEntityManager.GetPlayerEntity().GetEntityId();
        }


        public void Territories() {
            throw new System.NotImplementedException();
        }


        public void GuildCreate(string id, string name) {
            throw new System.NotImplementedException();
        }


        public string GuildName() {
            throw new System.NotImplementedException();
        }


        public void ChannelWindowClosed() {
            throw new System.NotImplementedException();
        }

        public void ChannelWindowOpened() {
            throw new System.NotImplementedException();
        }


        public void ShowHelp() {
            throw new System.NotImplementedException();
        }
    }
}
