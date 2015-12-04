# Welcome to Game Machine

The goal of Game Machine is to provide an inherently scalable, modern architecture for real-time multiplayer games.  Our focus is on providing easy to use abstractions for hard problems, allowing even client side developers to write game logic that is concurrent and performant, without having to deal with low level concurrency.

Visit our new [documentation wiki](http://www.gamemachine.io/confluence/display/GMD/Game+Machine+Documentation) for a getting started guide and downloads.


## What's new in version 3

- Lots of unity integration work.  Integrated process manager that can manage a pool of Unity instances.  From within game machine you can then request instances on demand, release them when you are done.  Full two way communication between Unity and Game Machine.  Process manager can run externally or embedded.

- Regions/zones were cleaned up.  Zones are now lightweight and used for both traditional mmo type zones and to handle smaller limited time instance matchups.  Unity instances are assigned zones either through a static config or on demand.  Zones are automatically assigned their own area of interest grid.

- Plugin system

- Several new plugins.  Most notable is a sophisticated combat system

- Lots of bug fixing and cleanup.

#### Installation


Game Machine now requires an sql database, it's no longer optional.

server/config/db has _base.sql files for postgres/mysql that have commands to create a user/databse and the required entities table.  Building Game Machine will then create a _generated.sql file per database type that has the schema for all of the tables generated from protobuf defintions.

The docs in confluence aren't up to date yet, that's next.  There are a lot of smaller changes in areas outside the core api, mostly good stuff that makes life easier.  Until the docs are updated please post on the google group with any questions.


