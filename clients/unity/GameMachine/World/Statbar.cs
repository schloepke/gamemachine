using UnityEngine;
using System.Collections;

namespace GameMachine.World
{
	public class Statbar : MonoBehaviour
	{

		Rect box = new Rect (55, 10, 150, 20);

		private Texture2D background;
		private Texture2D foreground;

		public float health = 100;
		public int maxHealth = 100;

		void Start ()
		{

			background = new Texture2D (1, 1, TextureFormat.RGB24, false);
			foreground = new Texture2D (1, 1, TextureFormat.RGB24, false);

			background.SetPixel (0, 0, Color.red);
			foreground.SetPixel (0, 0, Color.green);

			background.Apply ();
			foreground.Apply ();
		}

		void OnGUI ()
		{
			GUI.contentColor = Color.green;
			GUI.Label (new Rect (10, 10, 40, 20), "Health");
			GUI.BeginGroup (box);
			{

				GUI.DrawTexture (new Rect (0, 0, box.width, box.height), background, ScaleMode.StretchToFill);
				GUI.DrawTexture (new Rect (0, 0, box.width * health / maxHealth, box.height), foreground, ScaleMode.StretchToFill);


			}
			GUI.EndGroup ();
			GUI.Label (new Rect (200 + 40, 0, 160, 30), "Npc's in view range: " + NpcManager.npcCount);
		}

	}
}