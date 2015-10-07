package plugins.pvp_game;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Guild;
import io.gamemachine.messages.GuildAction;
import io.gamemachine.messages.GuildMemberList;
import io.gamemachine.messages.GuildMembers;
import io.gamemachine.messages.Guilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.google.common.base.Strings;

public class GuildHandler extends GameMessageActor {

	public static ConcurrentHashMap<String, Guild> guilds = new ConcurrentHashMap<String, Guild>();
	private Map<String,String> invites = new HashMap<String,String>();
	public static String npcGuild = "new_dawn";
	public static String npcGuildName = "New Dawn";
	
	public static String name = GuildHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	public static boolean exists(String id) {
		return (find(id) != null);
	}
		
	public static List<String> memberList(String id) {
		List<String> members = new ArrayList<String>();
		 for (GuildMembers member : GuildMembers.db().where("guild_members_guild_id = ?", id)) {
			 members.add(member.playerId);
		 }
		 return members;
	}
		
	public static boolean memberOf(String id, String playerId) {
		if (exists(id)) {
			if (memberList(id).contains(playerId)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static Guild find(String id) {
		if (!guilds.containsKey(id)) {
			Guild guild = Guild.db().findFirst("guild_id = ?", id);
			if (guild == null) {
				return null;
			} else {
				guilds.put(id, guild);
			}
		} 
		return guilds.get(id);
	}
	
	public static boolean inGuild(String guildId, String playerId) {
		Guild guild = playerGuild(playerId);
		if (guild != null && guild.id.equals(guildId)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Guild playerGuild(String playerId) {
		for (Guild guild : Guild.db().findAll()) {
			if (memberOf(guild.id,playerId)) {
				return guild;
			}
		}
		return null;
	}
	
	private GuildMemberList members(String id) {
		GuildMemberList gml = new GuildMemberList();
		gml.guildId = id;
		 for (String member : memberList(id)) {
			 gml.addPlayerId(member);
		 }
		 return gml;
	}
	
	private void addMember(String guildId, String memberId) {
		GuildMembers member = GuildMembers.db().findFirst("guild_members_player_id = ?", memberId);
		if (member == null) {
			member = new GuildMembers();
			member.guildId = guildId;
			member.playerId = memberId;
			GuildMembers.db().save(member);
		}
	}
	
	private void removeMember(String guildId, String memberId) {
		GuildMembers.db().deleteWhere("guild_members_player_id = ?", memberId);
	}
	
	private void save(Guild guild) {
		Guild.db().save(guild);
		guilds.put(guild.id, guild);
	}
	
	private void destroy(Guild guild) {
		//TerritoryHandler.removeOwner(guild.id);
		Guild.db().deleteWhere("guild_id = ?", guild.id);
		guilds.remove(guild.id);
		GuildMembers.db().deleteWhere("guild_members_guild_id = ?", guild.id);
	}
	
	@Override
	public void awake() {
		
		if (find(npcGuild) == null) {
			Guild guild = new Guild();
			guild.id = npcGuild;
			guild.name = npcGuildName;
			guild.ownerId = npcGuild;
							
			save(guild);
		}
		
		for (Guild guild : Guild.db().findAll()) {
			guilds.put(guild.id, guild);
		}
		
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		
		if (gameMessage.guildAction != null) {
			GuildAction guildAction = gameMessage.guildAction;
			Guild guild = null;
			if (guildAction.action.equals("guilds")) {
				Guilds guilds = new Guilds();
				guilds.guild = Guild.db().findAll();
				gameMessage.guilds = guilds;
				PlayerCommands.sendGameMessage(gameMessage, playerId);
				
			} else if (guildAction.action.equals("members")) {
				guild = playerGuild(playerId);
				if (guild != null) {
					Guilds guilds = new Guilds();
					guilds.addGuild(guild);
					gameMessage.guilds = guilds;
					gameMessage.guildMemberList = members(guild.id);
					PlayerCommands.sendGameMessage(gameMessage, playerId);	
				} else {
					logger.warning(playerId+" is not in a guild ");
					return;
				}
				
			} else if (guildAction.action.equals("create")) {
				if (exists(guildAction.guildId)) {
					return;
				}
				
				guild = new Guild();
				guild.id = guildAction.guildId;
				guild.name = guildAction.guildName;
				guild.ownerId = playerId;
								
				save(guild);
				addMember(guild.id,playerId);
				gameMessage.guildAction.response = "success";
				PlayerCommands.sendGameMessage(gameMessage, playerId);
			} else {
				
				if (!Strings.isNullOrEmpty(guildAction.guildId)) {
					guild = find(guildAction.guildId);
				}
				
				
				if (guildAction.action.equals("accept_invite")) {
					if (invites.containsKey(playerId)) {
						String invite = invites.get(playerId);
						String[] parts = invite.split(Pattern.quote("|"));
						String guildId = parts[0];
						String inviteId = parts[1];
						guild = find(guildId);
						
						
						
						if (invite.equals(guildAction.inviteId)) {
							if (guild == null) {
								logger.warning("Guild not found "+guildId);
								return;
							}
							
							if (memberOf(guild.id,playerId)) {
								logger.warning(playerId+" is already in guild "+guild.id);
								return;
							}
							
							addMember(guild.id,playerId);
							gameMessage.guildAction.response = "success";
							PlayerCommands.sendGameMessage(gameMessage, playerId);
							logger.warning(playerId+" joined guild "+guild.id);
						} else {
							logger.warning(inviteId +" does not match  "+guildAction.inviteId);
						}
					} else {
						logger.warning("Invite not found for "+playerId);
					}
					
				} else if (guildAction.action.equals("invite")) {
					if (playerId.equals(guild.ownerId)) {
						Random rand = new Random();
						String inviteId = guild.id+"|"+String.valueOf(rand.nextInt(10000) + 10);
						gameMessage.guildAction.inviteId = inviteId;
						
						// fixme, player id will be null here most likely
						String to = CharacterService.instance().find(guildAction.to).playerId;
						invites.put(to, inviteId);
						if (!Strings.isNullOrEmpty(to)) {
							PlayerCommands.sendGameMessage(gameMessage, to);
							logger.warning("Invite sent from "+playerId+" to "+guildAction.to);
						} else {
							logger.warning("Player id not found for "+guildAction.to);
						}
						
					} else {
						gameMessage.guildAction.response = "Not owner";
						PlayerCommands.sendGameMessage(gameMessage, playerId);
					}
						
				} else if (guildAction.action.equals("leave")) {
					removeMember(guild.id,playerId);
					if (playerId.equals(guild.ownerId)) {
						destroy(guild);
					}
					
					gameMessage.guildAction.response = "success";
					PlayerCommands.sendGameMessage(gameMessage, playerId);
				
				} else if (guildAction.action.equals("destroy")) {
					if (playerId.equals(guild.ownerId)) {
						destroy(guild);
						gameMessage.guildAction.response = "success";
					} else {
						gameMessage.guildAction.response = "Not owner";
					}
					PlayerCommands.sendGameMessage(gameMessage, playerId);
				}
			}
		}
		
	}

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		// TODO Auto-generated method stub
		
	}

}
