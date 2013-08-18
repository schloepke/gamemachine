
using System;
namespace GameMachine {
  class Actor {

    Actor () {}

    int onReceive(byte[] array) {
      Console.Out.WriteLine("onReceive called");
      Console.WriteLine(System.Text.Encoding.Default.GetString(array));
      return 1;
    }

  }
}
