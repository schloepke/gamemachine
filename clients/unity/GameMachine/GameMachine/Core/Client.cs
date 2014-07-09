using Entity = GameMachine.Messages.Entity;
using GameMachine;
using System;
namespace GameMachine.Core
{
	public interface Client
	{
		void SendEntity (Entity entity);
		void Start ();
		void Stop ();
		bool IsRunning ();
		void SetConnectionType (int connectionType);
	}
}

