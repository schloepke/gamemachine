# Welcome to Game Machine

Game Machine is a statement as much as a platform.  It started out of my own personal frustration with how the game industry approached many of the problems in this domain.

For one, we continue to solve the same problems over and over in isolation.  We failed to learn the benefits of open sourcing our core technologies, and in the process many of our approaches and much of our architecture have become dated and obsolete.

Much of what we do in this area is not outright bad or wrong (although that exists also), it's just not good.

The goal of Game Machine is to provide an inherently scalable, modern architecture for real-time multiplayer games that is straight forward and simple to use, while also being inherently scalable and performant.

Making multiplayer games, especially large virtual worlds, has often been thought of as something only a big studio could tackle.  In fact many large studios rather like this stereotype, and saw now reason to change it either perceptually or in fact.   
The truth is that while some of the challenges are, well challenging.  We absolutely can do better.  Much better.  With modern approaches to scalability and good abstractions, even small indie studios should be able to tackle complex multiplayer games without having to hire some industry veteran network guru.

Following is a short list of the key things we have done in Game Machine towards our goal.

- Fully distributed platform that is inherently scalable to thousands of nodes.

- No separate server types such as login server, zone server, etc..  All nodes can serve all functions.  Not only does this eliminate single points of failure, it significantly simplifies deployment and dev ops in general.

- Good abstractions for concurrency that are simple to understand and use.  Modern approach using the actor model and messaging.

- Industry standard messaging formats.  No custom binary bit field protocols (No you don't need those, and yes they are evil).

- A modern approach to reliable messaging that puts reliabilty back at the correct layer of abstraction.

- Low latency persistence model based on a distributed memory cache with intelligent write behind cache to disk storage.  Can scale to tens of thousands of writes per second by just adding more nodes.

-  Modular, pluggable systems for persistence and authentication.

Visit [www.gamemachine.io](http://www.gamemachine.io) for documentation, packaged downloads, and more.

