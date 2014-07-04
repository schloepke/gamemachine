
# Welcome to Game Machine

Game Machine is a server side platform for creating real time multiplayer games.  It was designed to be simple
to use while also being performant and inherently scalable.

###Simple solutions for complex problems
Game Machine provides higher level abstractions for tough problems like concurrency, persistence, and locality.  We use the actor model built on Akka to provide low latency concurrency, a distributed object store for fast data access, and distributed region management for locality and zones.

###Resilient and scalable by design
Scalable systems must be built around the premise that failure is natural and common.  A failure in one part of your system should not interrupt users.  With Game Machine we write resilient systems using actors and supervisors that monitor and react when something fails.  You can build hierarchies of control and management that can restart failed system, move them to another node, or simply let them fail and notify someone.

### Productve development environment
We leverage the power of the JVM to allow developers to build game logic quickly in jruby, and easily move that logic to scala or java if and when performance dictates. In Game Machine regardless of language all actors inherit from a single base class, and they all work the same way.  Migrating code to another JVM language is a matter of syntax, not structural change.

###Open source
We strongly believe that core technology should be open source.  This benefits everyone and allows more resource and time to be spent making games instead of architecture.  Our goal is to lower the cost of multiplayer games industry wide by providing a robust open source solution.

###Features

Out of the box:

* **[Area of interest](https://github.com/gamemachine/gamemachine/wiki/Area-of-Interest)**
* **[Chat/Matchmaking/Grouping](https://github.com/gamemachine/gamemachine/wiki/Matchmaking-&-Teams)**
* Login & Authentication
* **[Distributed object store](https://github.com/gamemachine/gamemachine/wiki/Object-database-archtiecture)**
* **[Locality (zones/regions)](https://github.com/gamemachine/gamemachine/wiki/Region-servers)**
* Simple framework for writing server side logic
* **[Fully distributed.  Easy to deploy and manage](https://github.com/gamemachine/gamemachine/wiki/Game-Machine-Cluster)**


###Getting Started

**[Play the public demo](https://github.com/gamemachine/gamemachine/wiki/Game-Machine-public-demo)**

**[Install the server](https://github.com/gamemachine/gamemachine/wiki/Installation)**

**[Download a Unity package](https://github.com/gamemachine/gamemachine/wiki/Unity-packages)**

  
 
###Support

Support is provided on our [gamemachine-users](https://groups.google.com/forum/#!forum/gamemachine-users) google group.

If you run into any bugs please file an issue and we will look into it asap.
