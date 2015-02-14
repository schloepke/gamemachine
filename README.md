# Welcome to Game Machine

The goal of Game Machine is to provide an inherently scalable, modern architecture for real-time multiplayer games that is straight forward and simple to use, while also being inherently scalable and performant.

Highlights

- Fully distributed platform that is inherently scalable to thousands of nodes.

- No separate server types such as login server, zone server, etc..  All nodes can serve all functions.  Automatic failover, no single points of failure, and simplified deployment/dev ops.
 
- Good abstractions for concurrency that are simple to understand and use.  Modern approach using the actor model and messaging.

- Industry standard messaging formats.  Efficient bit packing without having to resort to bit fields or custom binary formats.

- A modern approach to reliable messaging that puts reliabilty back at the correct layer of abstraction.

- Low latency persistence model based on a distributed memory cache with intelligent write behind cache to disk storage.  Plus built in support for direct access to relational databases.

-  Modular, pluggable systems for persistence and authentication.

Visit [www.gamemachine.io](http://www.gamemachine.io) for documentation, packaged downloads, and more.

