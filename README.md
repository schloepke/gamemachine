# Welcome to Game Machine

The goal of Game Machine is to provide an inherently scalable, modern architecture for real-time multiplayer games.  Our focus is on providing easy to use abstractions for hard problems, allowing even client side developers to write game logic that is concurrent and performant, without having to deal with low level concurrency.

## Version 2 is now out with a much simpler installation, unity packages, new features, and new documentation.

Visit our new [documentation wiki](http://www.gamemachine.io/confluence/display/GMD/Game+Machine+Documentation) for a getting started guide and downloads.

#### Highlights

- Fully distributed platform that is inherently scalable to thousands of nodes.

- No separate server types such as login server, zone server, etc..  All nodes can serve all functions.  Automatic failover, no single points of failure, and simplified deployment/dev ops.
 
- Good abstractions for concurrency that are simple to understand and use.  Modern approach using the actor model and messaging.

- Fully integrated persistence.  Protocol buffer messages can be directly persisted to the the object store or sql database.  Database schema's auto generated from protocol buffer definitions.

- Multiple caching layers for various needs.  Fully atomic local caching with persistence write through plus distributed off heap caching.

- Tunable write behind cache that decreases write load on the database.  Popular databases such as Postgres, Mysql, and Couchbase supported out of the box.

-  Modular, pluggable systems for persistence and authentication.

- Highly optimized spatial grid to track game entities and perform proximity queries.

- UDP or TCP based on Netty

- Chat system built on top of a distributed pub/sub architecture




