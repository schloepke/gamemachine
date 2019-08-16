# Game Machine is basically dead, if nobody has noticed.
When I created Game Machine it wasn't possible to do it in .NET, the tools were not there.  But times have changed, and it makes more sense in most cases to use .NET.

My suggestion for smaller studios and teams is use Orleans.  Teams with skilled backend engineers who are funded or established, there are some advantages to Akka.NET.  The basic difference is Orleans is easy, but slow.  Akka.NET requires a decent amount of up front architecture to be built, but starts to be more interesting at scale and over a longer period of use.


# Welcome to Game Machine

Game Machine is a modern platform for large scale realtime games such as mmo's and moba's.   Massive scalability and ease of use make it the most productive platform for creating complex multiplayer games.  



## 3.0 Release is out.  

3.0 is primarily a cleanup/bug fix branch with a lot of small refactoring of numerous systems.  Major changes of note:

- Unity editor GUI to manage building and running Game Machine from inside Unity.  No need to use the command line at all anymore.
 
- Protocol buffers are now built client side into their own dll.  We bake a serializer into the dll that avoids using reflection.  There are still some parts of the client that use reflection, but this is the start of a move to make Game Machine mobile friendly.

- Lots of Utility for managing Unity processes that interact with Game machine.  For games that need to run Unity to handle stuff like physics and pathfinding.  Game machine can now auto create temporary users that your Unity instances can obtain via a shared secret, and auto login to Game machine in an automated fashion.  New process management system allows Game machine to manage pools of Unity instances.  The editor GUI has automated build tools for creating unique Unity installations that can run under the process management system.

-  Game specific features such as a full combat and guild system were added.

- A relational database is now required.  Most of the nosql support is still present but is being deprecated.

The documentation wiki is NOT updated yet for 3.0.  There should be an updated getting started guide and unitypackage downloads very shortly, in the next couple of days.

[documentation wiki](http://www.gamemachine.io/confluence/display/GMD/Game+Machine+Documentation)

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





