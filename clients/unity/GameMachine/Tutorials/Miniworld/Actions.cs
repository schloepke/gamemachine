
using System;
using System.Collections;
using System.Collections.Generic;
namespace GameMachine.Tutorials.Miniworld
{
	public class Actions
	{

		public static Dictionary<int,string> numberToName = new Dictionary<int, string>
		{
			{ 1, "jump" }, 
			{ 2, "wave" }
		};

		public static Dictionary<int,string> nameToNumber = new Dictionary<int, string>
		{
			{ 1, "jump" }, 
			{ 2, "wave" }
		};
		
		public static string NameFor (int id)
		{
			return  numberToName [id];
		}

//		public static int NumberFor (string name)
//		{
//
//		}
	}
}

