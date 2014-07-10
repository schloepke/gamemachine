using System;
using UnityEngine;
using ProtoBuf;
using GameMachine.Core;


[ProtoContract(SkipConstructor = true)]
public class SampleDataModel
{

	public string test { get; set; }

	[ProtoMember(1)]
	public string id { get; set; }
	
	[ProtoMember(2)]
	public Vector3 v { get; set; }

}
