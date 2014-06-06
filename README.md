
# Game Machine

Multiplayer game development is harder then it needs to be.  Game Machine is an attempt to solve that.

## The right architecture
Game Machine uses an actor model based on message passing, built on top of a state of the art distributed computing framework.  Everything in Game Machine is a node in the cluster, including clients.


## Better abstractions
Much of the difficulty in writing multiplayer games is that our methods for structuring our code, have been separate from how we handle concurrency.   Game Machine uses a model where concurrency is baked in.  You never have to deal with threads, locks, or concurrent data structures in your game code.  At the same time game logic is automatically concurrent, able to distribute itself over an entire cluster and run at high concurrency.

## Agile
We believe that the key to being productive is a combination of using the right tools for the job, and making smart engineering trade-offs.  Game Machine is based on the JVM and uses the [Akka](http://www.akka.io) framework for the core architecture.  Where possible we write higher level features in [Jruby](http://www.jruby.org), and move to java code where performance dictates.  There is also support for writing game logic in C#.

##Features

- UDP/TCP networking.
- Http for login/authorization.
- Fully distributed architecture.
  - Distributed player registry.
  - Distributed player/npc controllers.
  - Distributed grid with spatial hashing and fast neighbor lookups.
  - Chat/Group/Matchmaking based on pub/sub messaging.
- Entity component system.
  - Entities and components are protocol buffers.
  - Everything is an entity/message.
  - Client integration based on messaging.
- Object persistence
  - Distributed transactional updates.
  - Memory based with write behind cache to key/value store.
  - Pluggable persistence.  Coubhbase, Mapdb, and memory already supported.
- Pathfinding based on Recastnavigation
- Polyglot framework
  - Any JVM language
  - C#
- Web control panel


[Getting started with Game Machine](https://github.com/gamemachine/gamemachine/wiki/Getting-started)

