package io.gamemachine.net.http;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.gamemachine.authentication.Authable;
import io.gamemachine.authentication.Authentication;
import io.gamemachine.authentication.AuthorizedPlayers;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Characters;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.Players;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import plugins.pvp_game.CharacterHandler;
import plugins.world_builder.WorldBuilderHandler;
import io.gamemachine.messages.Character;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

	public ChannelHandlerContext context = null;
	private Authable playerAuth;
	private HttpHelper httpHelper;

	public HttpServerHandler(String actorName, HttpHelper httpHelper) {
		this.httpHelper = httpHelper;
		this.playerAuth = AuthorizedPlayers.getPlayerAuthentication();
	}

	private Integer login(String playerId, String password) {
		Player player = PlayerService.getInstance().find(playerId);
		if (player == null) {
			return null;
		}

		return playerAuth.authorize(playerId, password);
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

		logger.info(req.getUri());
		if (req.getMethod() == POST) {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
			for (InterfaceHttpData data : decoder.getBodyHttpDatas()) {
				if (data.getHttpDataType() == HttpDataType.Attribute) {
					Attribute attribute = (Attribute) data;
					params.put(attribute.getName(), attribute.getValue());
				}
			}

			if (req.getUri().startsWith("/api")) {

				if (req.getUri().startsWith("/api/client/login/")) {
					Integer authtoken = login(params.get("username"), params.get("password"));
					if (authtoken == null) {
						NotAuthorized(ctx);
					} else {
						String json = httpHelper.client_auth_response(authtoken);
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
				boolean isAdmin = PlayerService.getInstance().playerHasRole(playerId, "admin");

				if (!Authentication.isAuthenticated(playerId, authtoken)) {
					return;
				}

				if (req.getUri().startsWith("/api/players/get")) {
					Player player = PlayerService.getInstance().find(params.get("playerId"));
					Ok(ctx, player.toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/characters/list")) {
					byte[] resp = CharacterService.getInstance().findPlayerCharacters(params.get("playerId"));
					Ok(ctx, resp);
					return;
				}

				if (req.getUri().startsWith("/api/characters/get")) {
					Character character = CharacterService.getInstance().find(params.get("otherPlayerId"),params.get("characterId"));
					Ok(ctx, character.toByteArray());
					return;
				}
				
				if (req.getUri().startsWith("/api/build_objects/get")) {
					byte[] resp = WorldBuilderHandler.getBuildObjects();
					Ok(ctx, resp);
					return;
				}

				if (req.getUri().startsWith("/api/characters/create")) {
					Character character = CharacterService.getInstance().create(params.get("playerId"),	params.get("characterId"), params.get("umaData"));
					if (character == null) {
						character = new Character();
						character.playerId = "na";
						character.id = "exists";
					}
					Ok(ctx, character.toByteArray());
					return;
				}

				if (req.getUri().startsWith("/api/characters/delete")) {
					CharacterService.getInstance().delete(params.get("playerId"),params.get("characterId"));
					Ok(ctx, params.get("characterId"));
					return;
				}
				
				if (req.getUri().startsWith("/api/characters/set_current")) {
					PlayerService.getInstance().setCharacter(playerId,params.get("characterId"));
					Ok(ctx, "OK");
					return;
				}

				if (isAdmin) {
					if (req.getUri().startsWith("/api/admin/characters/list")) {
						byte[] resp = CharacterService.getInstance().findPlayerCharacters(params.get("get_playerId"));
						Ok(ctx, resp);
						return;
					}
					
					if (req.getUri().startsWith("/api/admin/players/set_password")) {
						PlayerService.getInstance().setPassword(params.get("set_player_id"), params.get("set_password"));
						Ok(ctx, "OK");
						return;
					}

					if (req.getUri().startsWith("/api/admin/players/delete")) {
						PlayerService.getInstance().delete(params.get("delete_player_id"));
						Ok(ctx, params.get("delete_player_id"));
						return;
					}

					if (req.getUri().startsWith("/api/admin/characters/search")) {
						List<Character> characterList = CharacterService.getInstance().search(params.get("search_string"));
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

			if (req.getUri().startsWith("/characters/create")) {
				CharacterHandler.createCharacter(params.get("playerId"), params.get("id"), params.get("umaData"));
				byte[] resp = CharacterHandler.getPvpCharacters(params.get("playerId"));
				Ok(ctx, resp);
			}

			if (req.getUri().startsWith("/characters/get")) {
				byte[] resp = CharacterHandler.getPvpCharacters(params.get("playerId"));
				Ok(ctx, resp);
			}

		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	private static void sendResponse(ChannelHandlerContext ctx, HttpResponseStatus status, String body) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer(body,
				CharsetUtil.UTF_8));
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
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("Failure: "
				+ status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
}
