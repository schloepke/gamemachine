# Welcome to Game Machine

Game Machine is a server side platform for creating real time multiplayer games.  It was designed to be simple
to use while also being performant and inherently scalable.

###Simple solutions for complex problems
Game Machine provides higher level abstractions for tough problems like concurrency, persistence, and locality.  We use the actor model built on Akka to provide low latency concurrency, a distributed object store for fast data access, and distributed region management for locality and zones.

###Resilient and scalable by design
Game Machine takes a very different approach to scaling then traditional game servers.  Our system is fully distributed and we have no single points of failure or single server bottlenecks.  The architecture is incredibly simple for what it does, and can support tens of thousands of concurrent users and hundreds of thousands of state updates per second, all within the same virtual world without zones.

### Productve development environment
Game Machine is a polyglot architecture.  We use the languages and tools that are the best at what they do. The core of Game Machine is written in the JVM, and we provide support for extensions in C/C++ as well as .NET.  Please see our [language guide](https://github.com/gamemachine/gamemachine/wiki/Language-guide) for more information.


###Open source
We strongly believe that core technology should be open source.  This benefits everyone and allows more resource and time to be spent making games instead of architecture.  Our goal is to lower the cost of multiplayer games industry wide by providing a robust open source solution.

###Features

Out of the box:

* [Area of interest](https://github.com/gamemachine/gamemachine/wiki/Area-of-Interest)
* [Chat/Matchmaking/Grouping](https://github.com/gamemachine/gamemachine/wiki/Matchmaking-&-Teams)
* Login & Authentication
* [Distributed object store](https://github.com/gamemachine/gamemachine/wiki/Object-database-archtiecture)
* [Locality (zones/regions)](https://github.com/gamemachine/gamemachine/wiki/Region-servers)
* Simple framework for writing server side logic
* [Fully distributed.  Easy to deploy and manage](https://github.com/gamemachine/gamemachine/wiki/Game-Machine-Cluster)


###Getting Started


####Server
* [Install the server](https://github.com/gamemachine/gamemachine/wiki/Installation)
* [Game Machine basics](https://github.com/gamemachine/gamemachine/wiki/Game-Machine-basics)

After that we suggest taking a look at the code in games/example which runs the world server, or looking at the matchmaking system.

####Client

* [Play the public demo](https://github.com/gamemachine/gamemachine/wiki/Game-Machine-public-demo)
* [Download a Unity package](https://github.com/gamemachine/gamemachine/wiki/Unity-packages)



####Architecture

* [Client architecture](https://github.com/gamemachine/gamemachine/wiki/Client-Architecture)
* [Server architecture](https://github.com/gamemachine/gamemachine/wiki/Server-architecture) 
 
###Support

Support is provided on our [gamemachine-users](https://groups.google.com/forum/#!forum/gamemachine-users) google group.

If you run into any bugs please file an issue and we will look into it asap.
