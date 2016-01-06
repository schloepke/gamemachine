using UnityEngine;
using System.Collections;
using GameMachine.Common;
using System.IO;
using ProtoBuf;
using io.gamemachine.messages;
using System;

namespace GameMachine {
    namespace HttpApi {
        public class GuildApi : MonoBehaviour {

            public static GuildApi instance;

            void Awake() {
                instance = this;
            }

            public void GetInvites(string characterId, Action<GuildInvites> action) {
                StartCoroutine(GetInvitesRoutine(characterId, action));
            }

            public IEnumerator GetInvitesRoutine(string characterId, Action<GuildInvites> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/invites";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(null);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    GuildInvites invites = Serializer.Deserialize<GuildInvites>(stream);
                    action(invites);
                }
            }

            public void GetCharacterGuild(string characterId, Action<GuildInfo> action) {
                StartCoroutine(GetCharacterGuildRoutine(characterId, action));
            }

            public IEnumerator GetCharacterGuildRoutine(string characterId, Action<GuildInfo> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/character_guild";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(null);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    GuildInfo guildInfo = Serializer.Deserialize<GuildInfo>(stream);
                    action(guildInfo);
                }
            }

            public void GetGuilds(Action<Guilds> action) {
                StartCoroutine(GetGuildsRoutine(action));
            }

            public IEnumerator GetGuildsRoutine( Action<Guilds> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/list";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(null);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    Guilds guilds = Serializer.Deserialize<Guilds>(stream);
                    action(guilds);
                }
            }

            public void Create(string characterId, string guildId, Action<GuildInfo> action) {
                StartCoroutine(CreateRoutine(characterId, guildId, action));
            }

            public IEnumerator CreateRoutine(string characterId, string guildId, Action<GuildInfo> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/create";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                form.AddField("guildId", guildId);
               

                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(null);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    GuildInfo guildInfo = Serializer.Deserialize<GuildInfo>(stream);
                    action(guildInfo);
                }
            }

            public void Destroy(string characterId, string guildId, Action<bool> action) {
                StartCoroutine(DestroyRoutine(characterId, guildId, action));
            }

            public IEnumerator DestroyRoutine(string characterId, string guildId, Action<bool> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/destroy";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                form.AddField("guildId", guildId);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(false);
                } else {
                    action(true);
                }
            }

            public void Leave(string characterId, string guildId, Action<bool> action) {
                StartCoroutine(LeaveRoutine(characterId, guildId, action));
            }

            public IEnumerator LeaveRoutine(string characterId, string guildId, Action<bool> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/leave";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                form.AddField("guildId", guildId);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(false);
                } else {
                    action(true);
                }
            }

            public void Invite(string from, string to, string guildId, Action<bool> action) {
                StartCoroutine(InviteRoutine(from, to, guildId, action));
            }

            public IEnumerator InviteRoutine(string from, string to, string guildId, Action<bool> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/invite";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("from", from);
                form.AddField("to", to);
                form.AddField("guildId", guildId);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(false);
                } else {
                    action(true);
                }
            }

            public void AcceptInvite(string characterId, string guildId, Action<GuildInfo> action) {
                StartCoroutine(AcceptInviteRoutine(characterId, guildId, action));
            }

            public IEnumerator AcceptInviteRoutine(string characterId, string guildId, Action<GuildInfo> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/accept_invite";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                form.AddField("guildId", guildId);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(null);
                } else {
                    MemoryStream stream = new MemoryStream(www.bytes);
                    GuildInfo guildInfo = Serializer.Deserialize<GuildInfo>(stream);
                    action(guildInfo);
                }
            }

            public void DeclineInvite(string characterId, string guildId, Action<bool> action) {
                StartCoroutine(DeclineInviteRoutine(characterId, guildId, action));
            }

            public IEnumerator DeclineInviteRoutine(string characterId, string guildId, Action<bool> action) {
                string uri = NetworkSettings.instance.BaseUri() + "/api/guild/decline_invite";
                var form = new WWWForm();

                form.AddField("playerId", NetworkSettings.instance.username);
                form.AddField("authtoken", NetworkSettings.instance.authtoken);
                form.AddField("characterId", characterId);
                form.AddField("guildId", guildId);


                WWW www = new WWW(uri, form.data, form.headers);
                yield return www;

                if (www.error != null) {
                    Debug.Log(www.error);
                    action(false);
                } else {
                    action(true);
                }
            }

        }
    }
}