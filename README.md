# Welcome to Game Machine

The goal of Game Machine is to provide an inherently scalable, modern architecture for real-time multiplayer games that is straight forward and simple to use, while also being inherently scalable and performant.

#### Highlights

- Fully distributed platform that is inherently scalable to thousands of nodes.

- No separate server types such as login server, zone server, etc..  All nodes can serve all functions.  Automatic failover, no single points of failure, and simplified deployment/dev ops.
 
- Good abstractions for concurrency that are simple to understand and use.  Modern approach using the actor model and messaging.

- Fully integrated persistence.  Protocol buffer messages can be directly persisted to the the object store or sql database.  Database schema's auto generated from protocol buffer definitions.

- Multiple caching layers for various needs.  Fully atomic local caching with persistence write through plus distributed off heap caching.

- Tunable write behind cache that decreases write load on the database.  Popular databases such as Postgres, Mysql, and Couchbase supported out of the box.

-  Modular, pluggable systems for persistence and authentication.

- Highly optimized spatial grid to track game entities and perform proximity queries.


#### Roadmap

With the core server fairly mature and stable at this point, we have started working on an actual game to both showcase what Game Machine is capable of, and provide additional functionality in the form of genre specific modules.  As much of the code as possible will be open source.  Which is to say all of the server code and a large chunk of the client code, minus a few commercial assets from the Unity asset store (which can all be purchased to make the client fully playable).

For the game itself we took the most challenging, complicated multiplayer game we could think of, a massive pvp mmo with land/sea combat and siege warfare.  


* Game Status Update (4/1/15)

Just cut an early release of the game with most of the below functional.  It's running on a live server.  You can download a windows client here:

https://s3.amazonaws.com/gamemachine/pvp_mmo.zip

The state of the code is that while it's not completely prototype, it's not yet production ready, so don't pull down master and start using it in your game.

-  Complete combat system based on status effects that is 100% server driven.  Status effects can apply to players, npc's, or any static game object.  Single target and aoe damage.  Effects can be single shot or tick over time.  There are also passive and active effects, where passive effects last for a set duration, and active effects are either one time or tick for X number of ticks.  Items are also tied into this where items can be linked to effects.  Currently if you equip an item that is linked it automatically applies the effect, such as armor increase, etc..  And automatically removes it when you unequip.

- Siege warfare and the ability to capture objectives.  Nearly everything in game is destructable and it's status tracked server side.  Currently you can take out walls with catapults, capture the keep, and npc logic is working so when it flips the npc's flip with it from hostile to friendly.  When you capture a keep the structure goes back to full health.  This is linked into a new territory system that is now in place, where when you capture a keep you gain an influence in a certain radius that can be used for stuff like taxation, extra harvesting bonuses, etc..

-  Guilds.  Guilds can claim and own structures.  Core functionality in place, no ui yet all command line driven through the chat interface.

-  Housing.  Fairly basic but functional.
 
-  Crafting system.  Everything in game is crafted.  System is based on a simple combination system right now.  Currently the starting map has around 9,000 harvestable resources on it.  

- Inventory.  Basic inventory.  No trading yet, although that's basically a workflow thing as the db tables are setup to make that fairly straight forward.

- Npc's.  Basica npc ai and combat is in.  Waypoint system for automating npc pathing with leaders/followers.  Have guards patrolling around, killing bandits they come across, and killing any players that perform hostile actions that they see.  Also tied into player owned structures so they can guard them for you.

- Ship combat.  Ships themselves work, with multiple players on a ship.  Need to get cannons in game for the combat side to work.

- Most everything in game is server driven.  If you perform an action in the world your client acts on the response from the server just like every other player.  If you open the door to your house, we send a request to the server, you get back a reply, and then it opens.  This has worked much better then having a system where we treat your client differently then other clients.  For example if you open a door, it persists server side and everyone see's it opening just like you do.  Siege weapons moving and firing is handled the same way.


Visit [www.gamemachine.io](http://www.gamemachine.io) for documentation.

