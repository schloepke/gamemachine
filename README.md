
#Game Machine

Game Machine is a highly performant, scaleable game engine written in [Jruby](http://www.jruby.org)
with a bit of java thrown
where necessary.  It is based on [Akka](http://www.akka.io), an actor based messaging system built on the JVM.

Game Machine is opinionated software.  It comes from several years of working with games at scale, and looks to solve some of the problems that most massive multiplayer games run into.

It is mostly written in ruby, although we use a lot of java libraries under the covers.  Jruby
gives us the ability to write code in a productive environment, while leveraging the performance
and concurrency available in the JVM.

The core design is based on actors and message passing using an entity component framework for game logic.
Game systems are actors that operate on messages which are entities and components.

Currently, Game Machine is still under heavy development.  Most of the core systems are feature complete and there will be a release branch within the next month.  Here are the main items that need to get done before we get to an official release.

1. Documentation
2. Finish install system.  Similar to rails,  it installs an app skeleton to work from
3. Game system for player location tracking
4. Simple combat system
5. Round out a few unit tests
6. Figure out a better way to do integration testing (currently in rspec)
7. Try to get rid of more java source files, mainly netty udt.
8. Working UNITY client that can connect and interact with the server


## Entity component system
Entity component systems are great for creating almost any kind of game.  Game Machine's version treats entities and components as pure data.  The game logic is contained within systems that act on groups of
components.  We call these component groups aspects.

## Distributed data store
Game Machine has a unique distributed memory store that writes behind to any nosql key/value store.
It supports fully consistent updates at high write volume.  The write behind cache is fully tunable.
Most nosql databases are eventually consistent, which doesn't work too well in high write volume 
environments that massive multiplayer games have.  Game Machine has eventually durable writes that are
fully consistent and transactional in every other way.  Write/read times are deterministic and fast.  You can also choose to have specific updates be fully durable if needed, and bypass the write behind cache.

## UDT, Udp, and Http
Reliable Udp is supported via UDT.  Udp is also provided, and http is used for things like user login's
where you need the ssl security that http can provide out of the box.

UDT is the recommended protocol.  While not as fast as Udp, it does not suffer from a lot of the  performance
problems that TCP does over wide area networks.  The main limitation of UDP is the packet size.  In most cases udp is fine for multiplayer games as you rarely send so much data that it exceeds the max packet size.  At
present, Game Machine cannot handle udp messages that are split over muliple packets.

There are no plans to support TCP at present.  It is an inferior protocol in almost every way for games.

## Distributed chat system
We made use of Akka's built in distributed pub/sub for the chat system.

## Messages are 100% protocol buffers
In a messaging system based on Akka, serialization is a central part of messaging. Game Machine uses
protocol buffers for all messaging, from internal messages to the messages sent and received by the client.
Protocol buffers also have the advantage of being sparse and not carrying any schema information.

Game Machine uses the protostuff protobuf library, which provides a more user friendly interface
for creating messages then Google's library.  You are not forced into using the builder syntax, although it 
is available if you want it.

While I realize this might be somewhat of a controversial choice, it is such a central part of the system that allowing different formats would add a ton of extra complexity for no value.
