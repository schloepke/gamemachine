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
* Game Status Update (3/28/15)

Should have a live demo up in the next few days now, working on getting a simple website setup for the game so folks can create accounts and try it out.  Here are some highlights of what's done so far.

-  Complete combat system based on status effects that is 100% server driven.  Status effects can apply to players, npc's, or any static game object.  Single target and aoe damage.  Effects can be single shot or tick over time.  There are also passive and active effects, where passive effects last for a set duration, and active effects are either one time or tick for X number of ticks.  Items are also tied into this where items can be linked to effects.  Currently if you equip an item that is linked it automatically applies the effect, such as armor increase, etc..  And automatically removes it when you unequip.

- Siege warfare and the ability to capture objectives.  Nearly everything in game is destructable and it's status tracked server side.  Currently you can take out walls with catapults, capture the keep, and npc logic is working so when it flips the npc's flip with it from hostile to friendly.  When you capture a keep the structure goes back to full health.  This is linked into a new territory system that is now in place, where when you capture a keep you gain an influence in a certain radius that can be used for stuff like taxation, extra harvesting bonuses, etc..

-  Guilds.  Guilds can claim and own structures.  Core functionality in place, no ui yet all command line driven through the chat interface.

-  Housing.  Fairly basic but functional.
 
-  Crafting system.  Everything in game is crafted.  System is based on a simple combination system right now.  Currently the starting map has around 9,000 harvestable resources on it.  

- Inventory.  Basic inventory.  No trading yet, although that's basically a workflow thing as the db tables are setup to make that fairly straight forward.

- Npc's.  Basica npc ai and combat is in.  Waypoint system for automating npc pathing with leaders/followers.  Have guards patrolling around, killing bandits they come across, and killing any players that perform hostile actions that they see.  Also tied into player owned structures so they can guard them for you.

- Most everything in game is server driven.  If you perform an action in the world your client acts on the response from the server just like every other player.  If you open the door to your house, we send a request to the server, you get back a reply, and then it opens.  This has worked much better then having a system where we treat your client differently then other clients.  For example if you open a door, it persists server side and everyone see's it opening just like you do.  Siege weapons moving and firing is handled the same way.


- More work then I wanted to do on the client side in Unity.  To support the map sizes I wanted (everything in this game is about being massive), I basically had to throw out a number of features in unity like their LOD system and such.  To be honest no engine is really designed for this kind of game.  On the good side I've got maps with 200,000 trees, 70,000 other misc objects, and frame rates still above 80 fps. 


- Client status.  Still been putting off getting the client source out.  With the way lots of assets are packaged in Unity, you need to leave them where the author's put them or stuff breaks.  So most of the git tools for handling large files just won't work because of this.  And although I don't have that many paid assets, there are enough where there is just no way to bulid the game without them.  Currently I'm looking at a couple of tools that will let me put the large files on amazon S3, which seems to be the best option I've found.  Hopefully I'll get around to this soon...



Visit [www.gamemachine.io](http://www.gamemachine.io) for documentation.

