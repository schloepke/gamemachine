using Entity = io.gamemachine.messages.Entity;
using GameMachine;
using System;
using System.Collections.Generic;
namespace GameMachine.Core
{
	public interface Client
	{
		void SendEntities (List<Entity> entities);
		void SendEntity (Entity entity);
		void Start ();
		void Stop ();
		bool IsRunning ();
		void SetConnectionType (int connectionType);
        bool ReceivedPlayerConnected();
        void SendPlayerConnect();
        void Send(byte[] bytes);
        void Reconnect();
        long GetBytesIn();
        long GetBytesOut();
        void ResetBytes();
	}
}

