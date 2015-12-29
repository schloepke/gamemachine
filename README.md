# Welcome to Game Machine

Game Machine is a modern platform for large scale realtime games such as mmo's and moba's.   Massive scalability and ease of use make it the most productive platform for creating complex multiplayer games.  



## 3.0 Beta 1 is now out.  Documentation is being worked on.  See the REL3_0 branch for more information

Visit our [documentation wiki](http://www.gamemachine.io/confluence/display/GMD/Game+Machine+Documentation) for a getting started guide and downloads.

#### Highlights

- Fully distributed platform that is inherently scalable.  Simple deployment and dev ops.

- Good abstractions for concurrency that are simple to understand and use.  Modern approach using the actor model and messaging.

- Fully integrated persistence.  Our protocol buffers messaging has a baked in ORM that allows for server and client to use the same message classes.  You can persist messages in a single line of code, and query the database and get back protocol buffer messages ready to send to the client without any intermediate translation.

- Unity integration.  Game Machine can manage pools of Unity instances which can be assigned to short term matches, static zones, or whatever you want them for.  Fast two way communication.  Unity editor scripts for deploying unity to the server from within Unity.

- Highly advanced combat system that can handle most any type of game.  And a growing collection of other genre specific plugins.

-  Area of interest.  Multiple area of interest grids can be assigned on demand.

- The most efficient space optimization in the industry.  Multiple techniques used to allow for hundreds of entities in visual range at low bandwidth rates.

- UDP, TCP, and Http based on Netty.

- Fully distributed chat/messaging system.

- Region/zone handling that is fully distributed and simple to use.  Regions are automatically moved from down servers to another available node in the cluster.  Based on the proven Akka system. 





