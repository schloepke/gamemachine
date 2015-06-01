
using System;
namespace GameMachine
{
	public interface GameMachineApp
	{
		void ConnectionTimeout ();
		void ConnectionEstablished ();
		void OnLoggedIn ();
		void OnLoginFailure (string reason);
	}
}

