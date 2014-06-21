using GameMachine;
using System;
using Entity = GameMachine.Messages.Entity;
using Regions = GameMachine.Messages.Regions;
using  System.Collections.Generic;
using  System.Text.RegularExpressions;

namespace GameMachine
{
    public class RegionHandler : UntypedActor
    {
        public static Dictionary<string, string> regions = new Dictionary<string, string>();

        public static void SendRequest()
        {
            Entity entity = new Entity();
            entity.id = "regions";
            ActorSystem.Instance.FindRemote("GameMachine/GameSystems/RegionManager").Tell(entity);
            Logger.Debug("Send region request");
        }
        
        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;
            if (entity.regions != null)
            {
                List<string> r;
                List<string> regionServer;
                r = new List<string>(entity.regions.regions.Split("|".ToCharArray(), StringSplitOptions.RemoveEmptyEntries));
                foreach (string region in r)
                {
                    regionServer = new List<string>(region.Split("=".ToCharArray(), StringSplitOptions.RemoveEmptyEntries));
                    regions [regionServer [0]] = regionServer [1];
                    Logger.Debug("adding region " + regionServer [0] + " = " + regionServer [1]);
                }
            }
        }
    }
}