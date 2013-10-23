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
        // would capture routes to /products/list sent as a GET request
        Post["/message"] = parameters => {
			Console.WriteLine (this.Request.Body);
            return "The list of products";
        };
    }
}