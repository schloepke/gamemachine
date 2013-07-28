
#Game Machine

Game Machine is a highly performant, scalable game engine written in [Jruby](http://www.jruby.org)
It is based on [Akka](http://www.akka.io), an actor based messaging system
built on the JVM.  Game machine makes it simple to write performant, scalable
code using higher level abstractions.  

Game Machine is opinionated software.  It comes from several years of working
with games at scale, and looks to solve some of the problems that most
massive multiplayer games run into.

It is mostly written in ruby, although we use a lot of java libraries under the covers.  Jruby
gives us the ability to write code in a productive environment, while leveraging the performance
and concurrency available in the JVM.

The core design is based on actors and message passing using an entity component framework for game logic.
Game systems are actors that operate on messages which are entities and components.

The model for client/server communication is messaging based, and there is no
message ordering or set request/response cycle. One of the main reasons why
most mmo game engines scale poorly is because they force ordered messaging
and try to have reliable messaging when such a thing simply cannot exist
without suffering from extreme performance and scalability issues.

Game Machine is based on on the 'let it crash' model.  Systems can self heal
and notify other systems when there is a problem. Instead of message
guarantees, we build systems where the same message can be sent more then once
without causing any bad effects.  This is easily done with lighweight state
machines within the actor/game system.  Each game system is responsible for a
small part of the system, and only reacts to messages it is sent.  



Currently, Game Machine is still under heavy development.  Most of the core systems are feature complete and there will be a release branch within the next month.  Here are the main items that need to get done before we get to an official release.

1. Documentation
2. Finish install system.  (working as of 6/25)
3. Game system for player location tracking (working as of 6/27)
4. Simple combat system
5. Round out a few unit tests
6. Figure out a better way to do integration testing (currently in rspec)
7. Try to get rid of more java source files, mainly netty udt.
8. Working UNITY client that can connect and interact with the server


## Entity component system
Entity component systems are great for creating almost any kind of game.
Game Machine's version treats entities and components as pure data.
The game logic is contained within systems that act on groups of
components.  We call these component groups aspects.

Clients send entities to the game server, which are dispatched to the game
systems based on which components the entity has. An entity can be dispatched
to multiple game systems, and those game systems can in turn send the entities
on to other game systems.  At any point in the process, messages can be sent
back to the client.  


## Extensible
Systems based on message passing are by their nature much easier to integrate 
with then monolithic applications.  In GameMachine you can plug any
functionality you want into the system by just creating an actor that can send
and receive messages to and from other actors in the system.  

Jruby also makes it easy to integrate with other JVM languages, or to use C/C++
libraries.  For example, it would be straight forward to take a custom physics
engine written in C and load it into an actor, where you could pass messages
back and forth between the physics engine and the rest of GameMachine.

The main requirement for external libraries is that they not use global state.
It's ok if they are not thread safe.  What you cannot have is multiple
instances of some object that share state.  You must be able to instantiate one
instance per actor.  The actor system will guarantee that the instance is only
run in a single thread at a time.


## Distributed data store
Game Machine has a unique distributed memory store that is designed
specifically for games.  We trade eventual consistency for eventual durability,
and gain fully consistent writes that are transactional.

Most updates in games are to data that changes often, and if it is lost, is not
the end of the world. What effects you have on you, your current hit points,
your last location, are all things that do not need to be written to disk at
every update.  We take advantage of this by having a memory store with a
tunable write behind cache to your favorite key/value store.

The write behind cache has two tuning knobs.  The interval to write a specific
player's data, and the maximum number of writes per second across all players.

Transactions are accomplished through creating a ring of actors and hashing
object id's to the actors using consistent hashing.  Every object id is hashed
to a specific actor.  This means all get/put requests for a specific id are
serialized.  For updates we provide something akin to a remote procedure.  You
specify a block of code to run on update.  That code runs on the actor, the
actor hands you your object, and you update it and return the updated object.
This all happens in a completely serialized, transactional manner.

The consistent hashing ring is across all servers in the cluster.  The cluster
dynamically adapts to servers being added and removed.  When a server goes
down, there are a few seconds where messages will either be lost, or your
update will return a timeout message letting you retry or handle the failure in
some other way.

For all data store operations, you can choose to use a blocking or non blocking
call.  Blocking calls guarantee that the action was performed, although at a
small cost.  The amount of time you block is usually just network time on the
internal lan, as all updates are done in memory.


## UDT, Udp, and Http
Reliable Udp is supported via UDT.  Udp is also provided, and http is used for things like user login's
where you need the ssl security that http can provide out of the box.

UDT is the recommended protocol.  While not as fast as Udp, it does not suffer from a lot of the  performance
problems that TCP does over wide area networks.  The main limitation of UDP is the packet size.
In most cases udp is fine for multiplayer games as you rarely send so much data that it exceeds the max packet size.  At
present, Game Machine cannot handle udp messages that are split over muliple packets.

There are no plans to support TCP at present.  It is an inferior protocol in almost every way for games.


## Distributed chat system
We made use of Akka's built in distributed pub/sub for the chat system.  It is
designed to use minimal network resources. messages are sent to
each node, then distributed to players on that node.  Every player has it's own
actor for receiving messages which means there is no single bottleneck for
chat messages. The underlying messaging system can also be used for general
pub/sub messaging.


## Messages are 100% protocol buffers
In a messaging system based on Akka, serialization is a central part of messaging. Game Machine uses
protocol buffers for all messaging, from internal messages to the messages sent and received by the client.
Protocol buffers also have the advantage of being sparse and not carrying any schema information.

Game Machine uses the protostuff protobuf library, which provides a more user friendly interface
for creating messages then Google's library.  You are not forced into using the builder syntax, although it 
is available if you want it.

While I realize this might be somewhat of a controversial choice, it is such a
central part of the system that allowing different formats would add a ton of extra
complexity for questionable value.  If someone comes up with a better all
around serialization format, we would definitely look at it.

some formats that were considered, however briefly:

1. MsgPack - No message types, not a good fit
2. Json - Too heavy, not a sparse format


## Ruby for game logic on the client
Using ruby for client side game logic is an experiment that I'm hoping to try
out.  Recently a version of ruby called mini ruby was released that provides a
lighweight, fast, easy to embed ruby vm.  There are a lot of advantages to
using ruby over more traditional game scripting languages like lua.

If the game client is java, you get easy access to ruby via jruby.

The advantage of using ruby is that it's highly productive, you can reuse the
same code on client and server, and you can make use of the great testing tools
in the ruby ecosystem.  


## Why Jruby?
Originally I had planned on making Game Machine a framework that you could use
with any jvm language.  While you can write game systems using java or clojure
and get access to most of the core functionality, I decided against making that
a primary goal.  My experience has been that trying to do too much usually ends
badly, and in this case would probably mean I'd be forever just getting a first
version out the door.

That said, there is very little functionality you miss if you write your game
systems in another jvm language.  Passing a message from a jruby actor to a
clojure or scala actor works just fine.  All actors inherit from the core akka
actor classes which are in scala, and all messages are protocol buffers,
not ruby objects.

