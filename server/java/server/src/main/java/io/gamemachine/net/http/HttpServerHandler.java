package io.gamemachine.net.http;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.authentication.Authentication;
import io.gamemachine.authentication.PlayerAuthentication;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.guild.GuildService;
import io.gamemachine.messages.BuildObjects;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Characters;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GuildInfo;
import io.gamemachine.messages.GuildInvites;
import io.gamemachine.messages.Guilds;
import io.gamemachine.messages.ItemSlots;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.Players;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Zone;
import io.gamemachine.regions.ZoneService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;
import plugins.HttpHandler;
import plugins.landrush.BuildObjectHandler;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

	public ChannelHandlerContext context = null;

	public HttpServerHandler(String actorName) {
	}

	private int login(String playerId, String password) {
		Player player = PlayerService.getInstance().find(playerId);
		if (player == null) {
			return 0;
		}

		return PlayerAuthentication.getInstance().authorize(playerId, password);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {

		Map<String, String> params = new HashMap<String, String>();

		if (!req.getDecoderResult().isSuccess()) {
			logger.info("Bad request");
			sendError(ctx, BAD_REQUEST);
			return;
		}

		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.getUri());
		for (Entry<String, List<String>> p : queryStringDecoder.parameters().entrySet()) {
			String key = p.getKey();
			List<String> vals = p.getValue();
			if (vals.size() >= 1) {
				params.put(key, vals.get(0));
			}
		}

		logger.debug(req.getUri()+" "+AppConfig.getEnv());
		if (req.getMethod() == POST) {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
			for (InterfaceHttpData data : decoder.getBodyHttpDatas()) {
				if (data.getHttpDataType() == HttpDataType.Attribute) {
					Attribute attribute = (Attribute) data;
					params.put(attribute.getName(), attribute.getValue());
				}
			}

			if (req.getUri().startsWith("/internal")) {
				if (AppConfig.getEnv().equals("development")) {
					if (req.getUri().startsWith("/internal/create_npc")) {
						Vitals.Template template = Vitals.Template.valueOf(Integer.parseInt(params.get("vitalsTemplate")));
						PlayerService.getInstance().createNpc(params.get("npcPlayerId"),  params.get("npcCharacterId"),params.get("prefab"), template);
						Ok(ctx, "");
						return;
					}
				}
				NotAuthorized(ctx);
				return;
			}
			
			if (req.getUri().startsWith("/api")) {

				if (req.getUri().startsWith("/api/client/login/")) {
					int authtoken = 0;
					String username = null;

					if (!Strings.isNullOrEmpty(params.get("username"))
							&& params.get("username").equals(AppConfig.getAgentSecret())) {
						Player player = PlayerService.getInstance().assignAgent();
						if (player == null) {
							logger.warn("Unable to assign agent");
							NotAuthorized(ctx);
							return;
						}

						String password = PlayerService.getInstance().getAgentPassword(player.id);
						authtoken = login(player.id, password);
						username = player.id;
						logger.warn("Agent " + player.id + " authtoken " + authtoken);
					} else {
						authtoken = login(params.get("username"), params.get("password"));
						username = params.get("username");
					}

					if (authtoken == 0) {
						NotAuthorized(ctx);
					} else {
						String json = PlayerAuthentication.getInstance().authenticationResponse(authtoken, username);
						Ok(ctx, json);
					}

					return;
				}

				if (req.getUri().startsWith("/api/players/create")) {

					String newPlayerId = params.get("new_player_id");
					String newPlayerPassword = params.get("new_player_password");
					Player player;

					if (PlayerService.getInstance().playerExists(newPlayerId, false)) {
						player = new Player();
						player.id = "exists";
					} else {
						PlayerService.getInstance().create(newPlayerId, AppConfig.getDefaultGameId());
						PlayerService.getInstance().setPassword(newPlayerId, newPlayerPassword);
						player = PlayerService.getInstance().find(newPlayerId);
					}

					Ok(ctx, player.toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/players/change_password")) {
					boolean authenticated = PlayerService.getInstance().authenticate(params.get("playerId"),
							params.get("password"));
					if (authenticated) {
						if (params.get("new_password").equals(params.get("password"))) {
							Ok(ctx, "Password must be different");
						} else {
							PlayerService.getInstance().setPassword(params.get("playerId"), params.get("new_password"));
							Ok(ctx, "OK");
						}
					} else {
						Ok(ctx, "Invalid Password");
					}
					return;
				}

				String playerId = params.get("playerId");
				Integer authtoken = Integer.parseInt(params.get("authtoken"));
				boolean isAdmin = PlayerService.getInstance().playerHasRole(playerId, Player.Role.Admin);
				boolean authenticated = Authentication.hasValidAuthtoken(playerId, authtoken);

				
				if (req.getUri().startsWith("/api/game")) {
					HttpHandler.Response response = HttpHandler.processRequest(req.getUri(), params, authenticated);
					if (response.status == HttpHandler.Response.Status.NOT_AUTHORIZED) {
						NotAuthorized(ctx);
						return;
					}
					if (response instanceof HttpHandler.StringResponse) {
						HttpHandler.StringResponse sr = (HttpHandler.StringResponse) response;
						Ok(ctx, sr.content);
					} else if (response instanceof HttpHandler.ByteResponse) {
						HttpHandler.ByteResponse br = (HttpHandler.ByteResponse) response;
						Ok(ctx, br.content);
					} else {
						logger.warn("Invalid response");
					}
					return;
				}

				
				if (!authenticated) {
					logger.warn("Http attempt without authentication");
					return;
				}

				if (req.getUri().startsWith("/api/players/get_zones")) {
					Ok(ctx, ZoneService.getPlayerZones(playerId).toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/players/set_zone")) {
					if (params.get("zone") == null) {
						NotAuthorized(ctx);
						return;
					}

					if (!ZoneService.zoneExists(params.get("zone"))) {
						NotAuthorized(ctx);
						return;
					}

					Zone zone = ZoneService.getZone(params.get("zone"));
					if (zone == null) {
						NotAuthorized(ctx);
						return;
					}

					PlayerService.getInstance().setZone(playerId, zone);
					Ok(ctx, zone.toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/players/get_other")) {
					Player player = PlayerService.getInstance().find(params.get("otherPlayerId"));
					Ok(ctx, player.toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/players/get")) {
					Player player = PlayerService.getInstance().find(params.get("playerId"));
					Ok(ctx, player.toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/characters/list")) {
					byte[] resp = CharacterService.instance().findPlayerCharacters(params.get("playerId"));
					Ok(ctx, resp);
					return;
				}

				if (req.getUri().startsWith("/api/characters/get")) {
					Character character;
					if (params.containsKey("otherPlayerId")) {
						character = CharacterService.instance().find(params.get("otherPlayerId"),
								params.get("characterId"));
					} else {
						character = CharacterService.instance().find(params.get("characterId"));
					}

					if (character == null) {
						if (params.containsKey("otherPlayerId")) {
							logger.info("no character for " + params.get("otherPlayerId") + " "
									+ params.get("characterId"));
						} else {
							logger.info("no character for " + params.get("characterId"));
						}

						return;
					}
					Ok(ctx, character.toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/build_objects/get_recent")) {
					byte[] resp = BuildObjectHandler.getBuildObjects(playerId, false);
					Ok(ctx, resp);
					return;
				}
				
				if (req.getUri().startsWith("/api/build_objects/get_all")) {
					byte[] resp = BuildObjectHandler.getBuildObjects(playerId, true);
					Ok(ctx, resp);
					return;
				}

				if (req.getUri().startsWith("/api/build_objects/save")) {
					byte[] bytes = Base64.decodeBase64(params.get("build_objects"));
					GameMessage gameMessage = new GameMessage();
					gameMessage.buildObjects = BuildObjects.parseFrom(bytes);
					GameMessageActor.tell(gameMessage, playerId, BuildObjectHandler.name);
					Ok(ctx, "OK");
					return;
				}
				
				if (req.getUri().startsWith("/api/characters/create")) {
					String data = null;
					if (params.containsKey("data")) {
						data = params.get("data");
					}

					Character character = CharacterService.instance().create(params.get("playerId"),
							params.get("characterId"), Vitals.Template.PlayerTemplate, data);
					if (character == null) {
						character = new Character();
						character.playerId = "na";
						character.id = "exists";
					} else {
						if (params.containsKey("umaData")) {
							CharacterService.instance().SetUmaData(character, params.get("umaData"));
						}
					}
					Ok(ctx, character.toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/characters/delete")) {
					CharacterService.instance().delete(params.get("playerId"), params.get("characterId"));
					Ok(ctx, params.get("characterId"));
					return;
				}

				
				if (req.getUri().startsWith("/api/characters/set_current")) {
					PlayerService.getInstance().setCharacter(playerId, params.get("characterId"));
					Ok(ctx, "OK");
					return;
				}
				
				if (req.getUri().startsWith("/api/characters/set_bind_point")) {
					Character character = CharacterService.instance().setBindPoint(params.get("characterId"), params.get("bindPoint"));
					Ok(ctx, character.toByteArray());
					return;
				}
				
				if (req.getUri().startsWith("/api/characters/set_item_slots")) {
					CharacterService.instance().setItemSlots(params.get("characterPlayerId"), params.get("characterId"), params.get("item_slots"));
					Ok(ctx, "OK");
					return;
				}

				// Guilds
				if (req.getUri().startsWith("/api/guild/list")) {
					Guilds guilds = GuildService.instance().guildList();
					if (guilds == null) {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					} else {
						Ok(ctx, guilds.toByteArray());
					}
					return;
				}
				
				if (req.getUri().startsWith("/api/guild/character_guild")) {
					GuildInfo info = GuildService.instance().getCharacterGuild(params.get("characterId"));
					if (info == null) {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					} else {
						Ok(ctx, info.toByteArray());
					}
					return;
				}
				
				if (req.getUri().startsWith("/api/guild/invites")) {
					GuildInvites invites = GuildService.instance().getInvites(params.get("characterId"));
					if (invites == null) {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					} else {
						Ok(ctx, invites.toByteArray());
					}
					return;
				}
				
				if (req.getUri().startsWith("/api/guild/create")) {
					GuildInfo info = GuildService.instance().create(params.get("characterId"), params.get("guildId"));
					if (info == null) {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					} else {
						Ok(ctx, info.toByteArray());
					}
					return;
				}
				
				if (req.getUri().startsWith("/api/guild/destroy")) {
					Boolean status = GuildService.instance().destroy(params.get("characterId"), params.get("guildId"));
					if (status) {
						Ok(ctx, "OK");
					} else {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					}
					return;
				}
				
				if (req.getUri().startsWith("/api/guild/leave")) {
					Boolean status = GuildService.instance().leave(params.get("characterId"), params.get("guildId"));
					if (status) {
						Ok(ctx, "OK");
					} else {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					}
					return;
				}
				
				if (req.getUri().startsWith("/api/guild/invite")) {
					Boolean status = GuildService.instance().invite(params.get("from"), params.get("to"), params.get("guildId"));
					if (status) {
						Ok(ctx, "OK");
					} else {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					}
					return;
				}
				
				if (req.getUri().startsWith("/api/guild/decline_invite")) {
					Boolean status = GuildService.instance().declineInvite(params.get("characterId"), params.get("guildId"));
					if (status) {
						Ok(ctx, "OK");
					} else {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					}
					return;
				}
				
				if (req.getUri().startsWith("/api/guild/accept_invite")) {
					GuildInfo info = GuildService.instance().acceptInvite(params.get("characterId"), params.get("guildId"));
					if (info == null) {
						sendError(ctx,HttpResponseStatus.BAD_REQUEST);
					} else {
						Ok(ctx, info.toByteArray());
					}
					return;
				}
				
				if (isAdmin) {
					if (req.getUri().startsWith("/api/admin/characters/list")) {
						byte[] resp = CharacterService.instance().findPlayerCharacters(params.get("get_playerId"));
						Ok(ctx, resp);
						return;
					}

					if (req.getUri().startsWith("/api/admin/players/set_password")) {
						PlayerService.getInstance().setPassword(params.get("set_player_id"),
								params.get("set_password"));
						Ok(ctx, "OK");
						return;
					}

					if (req.getUri().startsWith("/api/admin/players/delete")) {
						PlayerService.getInstance().delete(params.get("delete_player_id"));
						Ok(ctx, params.get("delete_player_id"));
						return;
					}

					if (req.getUri().startsWith("/api/admin/characters/search")) {
						List<Character> characterList = CharacterService.instance().search(params.get("search_string"));
						if (characterList.size() > 40) {
							characterList = characterList.subList(0, 40);
						}
						Characters characters = new Characters();
						characters.setCharactersList(characterList.subList(0, 40));
						Ok(ctx, characters.toByteArray());
						return;
					}

					if (req.getUri().startsWith("/api/admin/players/search")) {
						List<Player> playerList = PlayerService.getInstance().search(params.get("search_string"));
						if (playerList.size() > 40) {
							playerList = playerList.subList(0, 40);
						}
						Players players = new Players();
						players.setPlayerList(playerList);
						Ok(ctx, players.toByteArray());
						return;
					}

				}
			}

		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	private static void sendResponse(ChannelHandlerContext ctx, HttpResponseStatus status, String body) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer(body, CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static void sendResponse(ChannelHandlerContext ctx, HttpResponseStatus status, byte[] body) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer(body));
		response.headers().set(CONTENT_TYPE, "application/octet-stream");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static void NotAuthorized(ChannelHandlerContext ctx) {
		HttpResponseStatus status = HttpResponseStatus.FORBIDDEN;
		sendResponse(ctx, status, "Not authorized\r\n");
	}

	private static void Ok(ChannelHandlerContext ctx, byte[] body) {
		HttpResponseStatus status = HttpResponseStatus.OK;
		sendResponse(ctx, status, body);
	}

	private static void Ok(ChannelHandlerContext ctx, String body) {
		HttpResponseStatus status = HttpResponseStatus.OK;
		sendResponse(ctx, status, body);
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
}
