using UnityEngine;
using System.Collections;
using UnityEngine.UI;
using io.gamemachine.messages;
using GameMachine.HttpApi;
using GameMachine.Common;

namespace GameMachine {
    public class GuildHandler : MonoBehaviour {

        public static GuildHandler instance;

        public GameObject memberTemplate;
        public GameObject uiPanel;
        public Text guildName;
        public GameObject memberPanel;
        public GameObject invitePanel;
        public GameObject createGuildPanel;
        public GameObject inviteTemplate;
        public InputField guildNameInput;
        public InputField inviteName;

        private Guild guild = null;

        void Awake() {
            instance = this;
        }

        void Start() {
        }
       
        void Update() {
            if (guildNameInput.isFocused || inviteName.isFocused) {
                InputState.SetInput("guild", true);
                return;
            } else {
                InputState.SetInput("guild", false);
            }

            if (Input.GetKeyDown(KeyBinds.Binding("Guild"))) {
                Toggle();
            }
        }

        public void Toggle() {
            if (uiPanel.activeInHierarchy) {
                Disable();
            } else {
                Enable();
            }
        }
        public void Enable() {
            uiPanel.SetActive(true);
            UpdateStatus();
        }

        public void Disable() {
            uiPanel.SetActive(false);
        }

        public void SendInvite() {
            Character character = GamePlayer.Instance().gameEntity.GetCharacter();
            GuildApi.instance.Invite(character.id, inviteName.text, guild.id, (status) => {
                inviteName.text = "";
                UpdateStatus();
            });
        }

        public void AcceptInvite(GameObject go) {
            Character character = GamePlayer.Instance().gameEntity.GetCharacter();
            GuildInviteInfo inviteInfo = go.GetComponent<GuildInviteInfo>();
            GuildApi.instance.AcceptInvite(character.id, inviteInfo.guildId, (GuildInfo info) => {
                UpdateStatus();
            });
        }

        public void DeclineInvite(GameObject go) {
            Character character = GamePlayer.Instance().gameEntity.GetCharacter();
            GuildInviteInfo inviteInfo = go.GetComponent<GuildInviteInfo>();
            GuildApi.instance.DeclineInvite(character.id, inviteInfo.guildId, (status) => {
                UpdateStatus();
            });
        }

        public void LeaveGuild() {
            Character character = GamePlayer.Instance().gameEntity.GetCharacter();
            GuildApi.instance.Leave(character.id, guild.id, (status) => {
                if (status) {
                    character.guildId = null;
                    GamePlayer.Instance().gameEntity.SetCharacter(character);
                    guild = null;
                }
                UpdateStatus();
            });
        }

        public void CreateGuild() {
            Character character = GamePlayer.Instance().gameEntity.GetCharacter();
            GuildApi.instance.Create(character.id, guildNameInput.text, (GuildInfo info) => {
                guildNameInput.text = "";

                if (info != null) {
                    character.guildId = info.guild.id;
                    GamePlayer.Instance().gameEntity.SetCharacter(character);
                    guild = info.guild;
                }
                UpdateStatus();
            });
        }
        
        private void ShowInvites(GuildInvites invites) {
            GmUtil.DestroyChildren(invitePanel.transform);
            foreach (GuildInvite invite in invites.guildInvite) {
                GameObject go = GameObject.Instantiate(inviteTemplate);
                go.SetActive(true);
                go.transform.SetParent(invitePanel.transform);
                go.name = invite.guildId;
                Text text = go.transform.Find("invite_text").GetComponent<Text>();
                text.text = "Invite to " + invite.guildId;

                GuildInviteInfo info = go.GetComponent<GuildInviteInfo>();
                info.guildId = invite.guildId;
                info.from = invite.from;
                info.to = invite.to;
            }
        }

        private void UpdateStatus() {
            Character character = GamePlayer.Instance().gameEntity.GetCharacter();

            createGuildPanel.SetActive(false);
            invitePanel.SetActive(false);
            memberPanel.SetActive(false);

            GuildApi.instance.GetCharacterGuild(character.id, (GuildInfo info) => {
                if (info == null) {
                    GuildApi.instance.GetInvites(character.id, (GuildInvites invites) => {
                        if (invites.guildInvite.Count > 0) {
                            invitePanel.SetActive(true);
                            ShowInvites(invites);
                        } else {
                            createGuildPanel.SetActive(true);
                        }
                    });
                } else {
                    guild = info.guild;
                    memberPanel.SetActive(true);
                    guildName.text = "Guild: " + guild.id;
                }
                
            });
        }
    }
}
