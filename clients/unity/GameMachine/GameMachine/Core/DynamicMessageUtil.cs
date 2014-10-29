using System.Collections;
using System.IO;
using System;
using ProtoBuf;
using System.Collections.Generic;
using System.Reflection;
using DynamicMessage = GameMachine.Messages.DynamicMessage;

public class DynamicMessageUtil
{

    private static string dynamicNamespace = "DynamicMessages";

    public static DynamicMessage ToDynamicMessage (object component)
    {
        DynamicMessage wrapper = new DynamicMessage ();
        Type t = component.GetType ();
        wrapper.type = t.Name;
        wrapper.message = Serialize (component);
        return wrapper;
    }

    public static object FromDynamicMessage (DynamicMessage dynamicMessage)
    {
        Type t = Type.GetType (dynamicNamespace + "." + dynamicMessage.type);
        MemoryStream stream = new MemoryStream (dynamicMessage.message);
        return Serializer.NonGeneric.Deserialize (t, stream);
    }

    private static byte[] Serialize (object message)
    {
        MemoryStream stream = new MemoryStream ();
        Serializer.Serialize (stream, message);
        return stream.ToArray ();
    }
}
