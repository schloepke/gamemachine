package io.gamemachine.guild;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.core.CharacterService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Guild;
import io.gamemachine.messages.GuildInfo;
import io.gamemachine.messages.GuildInvite;
import io.gamemachine.messages.GuildInvites;
import io.gamemachine.messages.Guilds;

public class GuildService {

	private ConcurrentHashMap<String, Guild> guildIndex = new ConcurrentHashMap<String, Guild>();
	
	public static String name = GuildService.class.getSimpleName();
	private final Logger logger = LoggerFactory.getLogger(GuildService.class);

	private GuildService() {
		
	}
	private static class LazyHolder {
		private static final GuildService INSTANCE = new GuildService();
	}

	public static GuildService instance() {
		return LazyHolder.INSTANCE;
	}
	
	public Guilds guildList() {
		Guilds guilds = new Guilds();
		if (guildIndex.size() > 0) {
			for (Guild guild : guildIndex.values()) {
				guilds.addGuild(guild);
			}
		} else {
			List<Guild> guildList = Guild.db().findAll();
			for (Guild guild : guildList) {
				guildIndex.put(guild.id, guild);
			}
			guilds.setGuildList(guildList);
		}
		
		return guilds;
	}
	
	public Guild find(String id) {
		if (!guildIndex.containsKey(id)) {
			Guild guild = Guild.db().findFirst("guild_id = ?", id);
			if (guild == null) {
				return null;
			} else {
				guildIndex.put(id, guild);
			}
		}
		return guildIndex.get(id);
	}

	public void save(Guild guild) {
		Guild.db().save(guild);
		guildIndex.put(guild.id, guild);
	}

	public void destroy(Guild guild) {
		GuildInvite.db().deleteWhere("guild_invite_guild_id = ?", guild.id);
		removeAllFromGuild(guild.id);
		Guild.db().deleteWhere("guild_id = ?", guild.id);
		guildIndex.remove(guild.id);
	}

	
	public GuildInfo getCharacterGuild(String characterId) {
		Character character = CharacterService.instance().find(characterId);
		if (Strings.isNullOrEmpty(character.guildId)) {
			return null;
		} else {
			return getGuildInfo(character.guildId);
		}
	}
	
	public GuildInfo getGuildInfo(String guildId) {
		GuildInfo info = new GuildInfo();
		info.guild = find(guildId);
		if (info.guild == null) {
			return null;
		}
		
		for (Character character : Character.db().where("character_guild_id = ?", guildId)) {
			info.addCharacterId(character.id);
		}
		return info;
	}
	
	private void removeAllFromGuild(String guildId) {
		for (Character character : Character.db().where("character_guild_id = ?", guildId)) {
			CharacterService.instance().setGuild(character,null);
		}
	}
	
	public GuildInvites getInvites(String characterId) {
		List<GuildInvite> invites = GuildInvite.db().where("guild_invite_to = ?", characterId);
		GuildInvites guildInvites = new GuildInvites();
		guildInvites.setGuildInviteList(invites);
		return guildInvites;
	}
	
	public GuildInfo create(String characterId, String guildId) {
		Character character = CharacterService.instance().find(characterId);

		Guild guild = find(guildId);
		if (guild != null) {
			return null;
		}
		
		guild = new Guild();
		guild.id = guildId;
		guild.name = guildId;
		guild.ownerId = character.id;

		save(guild);
		CharacterService.instance().setGuild(character, guild.id);
		
		return getGuildInfo(guild.id);
	}

	public Boolean declineInvite(String characterId, String guildId) {
		Character character = CharacterService.instance().find(characterId);
		
		Guild guild = find(guildId);
		
		if (guild == null) {
			logger.warn("Guild not found " + guildId);
			return false;
		}
				
		GuildInvite invite = GuildInvite.db().findFirst("guild_invite_to = ? AND guild_invite_guild_id = ?", characterId, guildId);
		if (invite == null) {
			logger.warn("Invite not found for " + characterId);
			return false;
		}
		
		GuildInvite.db().delete(invite.recordId);
		CharacterService.instance().setGuild(character, null);
		logger.warn(characterId + " declined invite for " + guild.id);
		return true;
	}

	public GuildInfo acceptInvite(String characterId, String guildId) {
		Character character = CharacterService.instance().find(characterId);
		
		Guild guild = find(guildId);
		
		if (guild == null) {
			logger.warn("Guild not found " + guildId);
			return null;
		}
		
		if (!Strings.isNullOrEmpty(character.guildId)) {
			logger.warn(characterId + " is already in guild " + character.guildId);
			return null;
		}
		
		GuildInvite invite = GuildInvite.db().findFirst("guild_invite_to = ? AND guild_invite_guild_id = ?", characterId, guildId);
		if (invite == null) {
			logger.warn("Invite not found for " + characterId);
			return null;
		}
		
		GuildInvite.db().delete(invite.recordId);
		CharacterService.instance().setGuild(character, guild.id);
		logger.warn(characterId + " joined guild " + guild.id);
		return getGuildInfo(guild.id);
	}

	public Boolean invite(String from, String to, String guildId) {
		Character fromCharacter = CharacterService.instance().find(from);
		
		if (fromCharacter.id.equals(to)) {
			logger.warn("Cannot invite self ");
			return false;
		}
		
		Guild guild = find(guildId);
		if (fromCharacter.id.equals(guild.ownerId)) {
			GuildInvite invite = GuildInvite.db().findFirst("guild_invite_to = ? AND guild_invite_from = ?", to, from);
			if (invite != null) {
				logger.warn("Invite exists for "+to);
				return false;
			}
			invite = new GuildInvite();
			invite.id = UUID.randomUUID().toString();
			invite.to = to;
			invite.from = from;
			invite.guildId = guildId;
			GuildInvite.db().save(invite);
			return true;
		} else {
			logger.warn("Not authorized to invite " + from);
			return false;
		}
	}

	public Boolean leave(String characterId, String guildId) {
		
		Guild guild = find(guildId);
		if (guild == null) {
			return false;
		}
		
		Character character = CharacterService.instance().find(characterId);
		
		CharacterService.instance().setGuild(character, null);
		
		if (characterId.equals(guild.ownerId)) {
			destroy(guild);
		}
		
		return true;
	}

	public Boolean destroy(String characterId, String guildId) {
		Guild guild = find(guildId);
		if (guild == null) {
			return false;
		}
		
		if (characterId.equals(guild.ownerId)) {
			destroy(guild);
			return true;
		} else {
			return false;
		}
	}

	
}
