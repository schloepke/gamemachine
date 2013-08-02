
using System;
namespace GameMachine {
	class Actor {

		Actor () {
		}

    int onReceive(byte[] array) {
      Console.Out.WriteLine("onReceive called");
      return 1;
    }

	}
}
