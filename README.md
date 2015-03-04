# Welcome to Game Machine

The goal of Game Machine is to provide an inherently scalable, modern architecture for real-time multiplayer games that is straight forward and simple to use, while also being inherently scalable and performant.

#### Highlights

- Fully distributed platform that is inherently scalable to thousands of nodes.

- No separate server types such as login server, zone server, etc..  All nodes can serve all functions.  Automatic failover, no single points of failure, and simplified deployment/dev ops.
 
- Good abstractions for concurrency that are simple to understand and use.  Modern approach using the actor model and messaging.

- Industry standard messaging formats.  Efficient bit packing without having to resort to bit fields or custom binary formats.

- A modern approach to reliable messaging that puts reliabilty back at the correct layer of abstraction.

- Low latency persistence model based on a distributed memory cache with intelligent write behind cache to disk storage.  Plus built in support for direct access to relational databases.

-  Modular, pluggable systems for persistence and authentication.


#### Roadmap

With the core server fairly mature and stable at this point, we have started working on an actual game to showcase what Game Machine can do.  As much of the code as possible will be open source.  Which is to say all of the server code and a large chunk of the client code, minus a few commercial assets from the Unity asset store (which can all be purchased to make the client fully playable).

For the game itself we took the most challenging, complicated multiplayer game we could think of, a massive pvp mmo with land/sea combat and siege warfare.  

Server code is already being committed to master.  The client will be released in a separate repo once we figure out a good, clean way of keeping the free/commercial bits separate.

Note that a primary goal of this side project is to show what can be done on a functional level in a very short period of time.  The code itself is not necessarily production quality in all aspects when it comes to general code quality, unit testing, etc..  Although we are making sure it retains the scalability, performance, and other features we want to highlight. 
* Game Status Update

- Dynamically loading terrains and associated objects.  Now working with 64-100 unity terrains per map.  Zoning is a thing, but the maps are huge.

- Siege weapons working.  All server controlled so rotation/firing is all truely multiplayer

- Boats working with moving platform support for multiple players on a boat.  Still some glitches, move too fast and you can get dumped now and then.

- Functional combat system.  Handles single target and aoe damage all server controlled.

- harvesting/Crafting system.  Everything in game is craftable.

- Persistent structures (housing).  Craft a house plan, place it in the world and it persists.

- Destructable structures.  Everything that's a mesh can be destructable.  Siege weapons can now destroy structures.

- Npc ai.  Basic npc ai with pathfinding and basic leader/follow/patrol logic.  No npc combat yet.



Visit [www.gamemachine.io](http://www.gamemachine.io) for documentation.

