
using System;
namespace GameMachine
{
	public interface GameMachineApp
	{
		void ConnectionTimeout ();
		void OnLoggedIn ();
	}
}

