
#Game Machine

Game Machine is a highly performant, scalable massive multiplayer game engine written in
[Jruby](http://www.jruby.org).
It is based on [Akka](http://www.akka.io), an actor based messaging system
built on the JVM.  Game machine makes it simple to write performant, scalable
code using higher level abstractions.  

#Features

- UDP/UDT/TCP networking.
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
- Pathfinding based on Recastnavigation
- Polyglot framework
  - Java
  - Scala
  - Jruby
  - C#

#Installation

The old gem installation has been removed.  We are now putting together an
install based on Vagrant, which will provide a complete working server out of
the box.

#Documentation

Documentation and SDK's for Java, Ruby, and C# will be coming soon.

#What's new

C# actors via embedding mono is the main big feature that was added.  We are
currentl working on the end user api and a Unity3D SDK.
