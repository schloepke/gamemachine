using  ProtoBuf;
using Entity = GameMachine.Messages.Entity;
using GameMachine;

public class RemoteActorRef : UntypedActor
{
    private string destination;

    public RemoteActorRef(string _destination)
    {
        destination = _destination;
    }

    public override void OnReceive(object message)
    {
        actorSystem.TellRemote(destination, message as Entity);
    }
}