
# Welcome to Game Machine

Game Machine is a server side platform for creating real time multiplayer games that won't make your head hurt.

Our goal is to solve the hard problems for you, and let you spend time writing game logic.

Game Machine uses the [Actor Model](http://en.wikipedia.org/wiki/Actor_model) for writing game logic and is based on [Akka](http://www.akka.io).  You create game logic inside actors that can communicate with other actors by sending messages to them.  You never have to deal with threads, locking, or concurrent data structures, just game logic.


Game Machine runs on the JVM, and provides first class support for ruby, java, and scala.  In addition we provide the [ability to run C#](https://github.com/gamemachine/gamemachine/wiki/C%23-Mono-support), although with some limitations in functionality.

###Features

Out of the box no server programming required:

* **[Area of interest](https://github.com/gamemachine/gamemachine/wiki/Area-of-Interest)**
* **[Chat/Matchmaking/Grouping](https://github.com/gamemachine/gamemachine/wiki/Group-messaging)**
* **[Login & Authentication](https://github.com/gamemachine/gamemachine/wiki/Login-and-authentication)**
* **[Simple persistence](https://github.com/gamemachine/gamemachine/wiki/Simple-persistence)**

###Getting Started

**[Install the server](https://github.com/gamemachine/gamemachine/wiki/Getting-started)**

**[Download the Unity client](https://github.com/gamemachine/gamemachine/tree/master/clients/unity/basic_client)**
  
 
###Writing server side game logic

* [Game Machine technical overview](https://github.com/gamemachine/gamemachine/wiki/Game-Machine-technical-overview) (start here)
* Creating your own game systems
