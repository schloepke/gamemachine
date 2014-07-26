using System;

namespace GameMachine.Core
{
	public interface Behavior
	{
		void OnMessage (object message);
	}
}
