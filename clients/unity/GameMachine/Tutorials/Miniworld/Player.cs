using UnityEngine;
using System.Collections;
using  System.Collections.Generic;

using GameMachine.Core;

namespace GameMachine.Tutorials.Miniworld
{
	public class Player : KAM3RA.Actor
	{


		protected override void Update ()
		{
			base.Update ();
		}
				
		
		public void Spawn (float x, float y)
		{
			Logger.Debug ("Spawn point " + x.ToString () + " " + y.ToString ());
			Terrain terrain = Terrain.activeTerrain;
			Vector3 vitalsVector = new Vector3 (x, 0f, y);
			float height = terrain.SampleHeight (vitalsVector);
			Vector3 spawnPoint = new Vector3 (x, (height + 0.05f), y);
			this.gameObject.transform.position = spawnPoint;
		}
		
		protected override void LateUpdate ()
		{
			// set the animation state here
			if (animation != null)
				animation.CrossFade (States.Name);
			
			// update sound -- not implemented in Actor but subs would implement
			UpdateSound ();
		}
		
		protected override void Start ()
		{
			//Spawn (vitals.x, vitals.y);
			SetNameTag (User.Instance.username);
			name = User.Instance.username;
			base.Start ();

		}


		
		

	}
}