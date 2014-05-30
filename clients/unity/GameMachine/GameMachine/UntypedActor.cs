using GameMachine;
using System.IO;
using System;
using System.Collections;
using  System.Collections.Generic;
using  System.Text.RegularExpressions;

namespace GameMachine
{
    public abstract class UntypedActor : IActor
    {
        protected List<List<string>> componentSets = new List<List<string>>();
        public ActorSystem actorSystem;

        public UntypedActor()
        {
        }
	
        public void AddComponentSet(string componentsStr)
        {
            List<string> componentSet = new List<string>();
            foreach (string component in componentsStr.Split(" ".ToCharArray(), StringSplitOptions.RemoveEmptyEntries))
            {
                string name = Char.ToLowerInvariant(component [0]) + component.Substring(1);
                componentSet.Add(name);
            }
            componentSets.Add(componentSet);
        }

        public List<List<string>> GetComponentSets()
        {
            return componentSets;
        }

        public void SetActorSystem(ActorSystem _actorSystem)
        {
            actorSystem = _actorSystem;
        }
		
        public abstract void OnReceive(object message);

        public void Tell(object message)
        {
            OnReceive(message);
        }

        public void Unhandled(object message)
        {
            actorSystem.Find("DeadLetters").Tell(message);
        }
    }
}
