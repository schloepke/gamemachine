package io.gamemachine.routing;

import io.gamemachine.messages.GameMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Strings;

public class GameMessageRoute {

    public static final Map<String, GameMessageRoute> namedRoutes = new ConcurrentHashMap<String, GameMessageRoute>();
    public static final Map<Integer, GameMessageRoute> routes = new ConcurrentHashMap<Integer, GameMessageRoute>();

    public final Integer id;
    public final String name;
    public final String to;
    public final boolean distributed;

    public GameMessageRoute(Integer id, String name, String to, boolean distributed) {
        this.id = id;
        this.name = name;
        this.to = to;
        this.distributed = distributed;
    }

    public static void add(int id, String to, boolean distributed) {
        GameMessageRoute route = new GameMessageRoute(id, null, to, distributed);
        routes.put(id, route);
    }

    public static void add(String name, String to, boolean distributed) {
        GameMessageRoute nameRoute = new GameMessageRoute(null, name, to, distributed);
        namedRoutes.put(name, nameRoute);
    }

    public static void add(int id, String name, String to, boolean distributed) {
        GameMessageRoute route = new GameMessageRoute(id, name, to, distributed);
        routes.put(id, route);
        namedRoutes.put(name, route);
    }

    public static GameMessageRoute routeFor(String name) {
        if (namedRoutes.containsKey(name)) {
            return namedRoutes.get(name);
        } else {
            return null;
        }
    }

    public static GameMessageRoute routeFor(GameMessage gameMessage) {
        if (gameMessage.destinationId > 0) {
            if (routes.containsKey(gameMessage.destinationId)) {
                return routes.get(gameMessage.destinationId);
            } else {
                return null;
            }
        } else if (!Strings.isNullOrEmpty(gameMessage.destination)) {
            if (namedRoutes.containsKey(gameMessage.destination)) {
                return namedRoutes.get(gameMessage.destination);
            } else {
                return null;
            }
        }
        return null;
    }

}
