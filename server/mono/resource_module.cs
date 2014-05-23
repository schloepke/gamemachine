using System.Net;
using System.Net.Sockets;
using System;
using System.Threading;
using System.Net.Http;
using  System.Linq;
using Nancy;
using Nancy.Hosting.Self;
public class ResourceModule : NancyModule
{
    public ResourceModule() : base("/actor")
    {
        Get["/message", true] = async (x, ct) => {
			//Console.WriteLine (this.Request);
            return "OK";
        };
    }
}
