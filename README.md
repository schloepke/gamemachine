
#Game Machine

Game Machine is a highly performant, scalable massive multiplayer game engine written in
[Jruby](http://www.jruby.org).
It is based on [Akka](http://www.akka.io), an actor based messaging system
built on the JVM.  Game machine makes it simple to write performant, scalable
code using higher level abstractions.  

#Features

- UDP/UDT networking.
- Http for login/authorization.
- Fully distributed architecture.
  - Distributed player registry.
  - Distributed player/npc controllers.
  - Distributed grid with spatial hashing and fast neighbor lookups.
  - Distributed chat/group system.
- Entity component system.
  - Entities and components are protocol buffers.
  - Integrates seamlessly with Akka messaging.
  - Everything is an entity/message.
- Object persistence
  - Distributed transactional updates.
  - Memory based with write behind cache to key/value store.
  - Pluggable persistence.  Coubhbase, Mapdb, and memory already supported.

#Installation

Ubuntu 12 or 13

1. Clone the repo
2. Install jruby
3. Install gradle 1.6 (1.5 has bugs that can make the build fail)
3. bundle install
4. rake java:all
4. rake game:demo
5. Start server with jruby -J-Xmx1024m bin/game_machine --name=seed01 --server

This will give you a server running the demo game.

The demo game spawns 2000 npc's in the world.
Npc's within range will appear and chase you. Npc's will attack you and do
damage, but this is not yet reflected in the client.

Get the [Unity3d demo client](https://github.com/chrisochs/gm_unity_client).
In the client go into GameClient.cs and on line 74 change the ip to your
server's ip.  Now you can run the client and run around the world.  I can
usually get up to 200 npc's following me before the rendering drops the
framerate to around 2-5.

To run a 2 server cluster just  run the same command but pass it seed02 as the
name.

#Performance

My goal is to have all the core game systems scale out well within the context
of a single virtual world, using homogenized servers, to at least 100,000
concurrent players/npc's.

A secondary goal is to support large numbers of players within the same
vacinity.  This is mostly an issue of optimizing client bandwidth.  Currently
a single server can handle a few thousand concurrent players and npc's in the same
area, but the client reaches a point where it's just too much data.

The basic approach I have taken is to stick in ruby until benchmarking shows
that we need to move the functionality to java.  The spatial grid is a good
example of something that gained a lot by writing it in java.

Lots of work has gone into the entity tracking system and spatial grid.  On a
vmware instance running inside a quad core I5, I'm able to run up to 10,000 npc
controllers at once with 200% cpu utilization.  Each controller's update method
runs 10 times a second, doing a neighbor search, movement, and combat at each
update.

A more reasonable number with 2000 npc's sticks at around 30% cpu.

With the same setup the object persistence was able to handle pretty much
anything you could throw at it.  The write behind cache is tunable so that
effects performance quite a bit. Testing just the peristence layer I was
getting around 20,000 writes per second on a 2 machine cluster with couchbase
as the backend store. Performance depends a lot on the configuration of the
write behind cache.

More work needs to go into optimizing client bandwidth, specifically data sent
to the client as a result of neighbor queries. The server sends integers
instead of floats, and everything  is bit packed by default (protocol buffers
gives you this for free).  The next step is to explore sending local cell
coordinates only, or possibly use polar coordinates.

The client messaging pipeline will need more work.  We need a better testing
framework for large numbers of clients.  I've done some minimal testing to know
that we don't have significant bugs under reasonable client loads (+-2000
connections per server).

#Upcoming features

1. Web based admin ui using ruby on rails.
2. Integrated debugging/testing tools in the admin ui
3. Rdoc/YARD docs
4. Combat system (starting of one is in demo)
5. Initial release with published gem and unity client

