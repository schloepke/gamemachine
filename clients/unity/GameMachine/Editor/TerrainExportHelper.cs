// Converted from UnityScript to C# at http://www.M2H.nl/files/js_to_c.php - by Mike Hergaarden
// C # manual conversion work by Yun Kyu Choi

using UnityEngine;
using System;
using System.Collections;
using System.IO;
using System.Text;


class TerrainExportHelper
{

	public StringBuilder Export (int vertexOffset, int normalOffset, int uvOffset)
	{
		Terrain terrainObject = Terrain.activeTerrain;
		TerrainData terrain = terrainObject.terrainData;
		Vector3 terrainPos = terrainObject.transform.position;

		int w = terrain.heightmapWidth;
		int h = terrain.heightmapHeight;
		Vector3 meshScale = terrain.size;
		int tRes = (int)Mathf.Pow (2, 0);
		meshScale = new Vector3 (meshScale.x / (w - 1) * tRes, meshScale.y, meshScale.z / (h - 1) * tRes);
		Vector2 uvScale = new Vector2 (1.0f / (w - 1), 1.0f / (h - 1));
		float[,] tData = terrain.GetHeights (0, 0, w, h);
		
		w = (w - 1) / tRes + 1;
		h = (h - 1) / tRes + 1;
		Vector3[] tVertices = new Vector3[w * h];
		Vector2[] tUV = new Vector2[w * h];
		
		int[] tPolys;
		
		tPolys = new int[(w - 1) * (h - 1) * 6];

		// Build vertices and UVs
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				tVertices [y * w + x] = Vector3.Scale (meshScale, new Vector3 (-y, tData [x * tRes, y * tRes], x)) + terrainPos;
				tUV [y * w + x] = Vector2.Scale (new Vector2 (x * tRes, y * tRes), uvScale);
			}
		}
		
		int index = 0;
		// Build triangle indices: 3 indices into vertex array for each triangle
		for (int y = 0; y < h - 1; y++) {
			for (int x = 0; x < w - 1; x++) {
				// For each grid cell output two triangles
				tPolys [index++] = (y * w) + x;
				tPolys [index++] = ((y + 1) * w) + x;
				tPolys [index++] = (y * w) + x + 1;
					
				tPolys [index++] = ((y + 1) * w) + x;
				tPolys [index++] = ((y + 1) * w) + x + 1;
				tPolys [index++] = (y * w) + x + 1;
			}
		}

		StringBuilder meshstr = new StringBuilder ();
		meshstr.Append ("g ").Append ("terrain").Append ("\n");

		// Write vertices
		System.Threading.Thread.CurrentThread.CurrentCulture = new System.Globalization.CultureInfo ("en-US");

		for (int i = 0; i < tVertices.Length; i++) {
			StringBuilder sb = new StringBuilder ("v ", 20);
			// StringBuilder stuff is done this way because it's faster than using the "{0} {1} {2}"etc. format
			// Which is important when you're exporting huge terrains.
			sb.Append (tVertices [i].x.ToString ()).Append (" ").
					Append (tVertices [i].y.ToString ()).Append (" ").
						Append (tVertices [i].z.ToString ());
			meshstr.Append (sb.ToString () + "\n");
		}
		// Write UVs
		for (int i = 0; i < tUV.Length; i++) {
			StringBuilder sb = new StringBuilder ("vt ", 22);
			sb.Append (tUV [i].x.ToString ()).Append (" ").
					Append (tUV [i].y.ToString ());
			meshstr.Append (sb.ToString () + "\n");
		}
		// Write triangles
		for (int i = 0; i < tPolys.Length; i += 3) {
			StringBuilder sb = new StringBuilder ("f ", 43);
			sb.Append (tPolys [i] + 1 + vertexOffset).Append ("/").Append (tPolys [i] + 1 + vertexOffset).Append (" ").
					Append (tPolys [i + 1] + 1 + normalOffset).Append ("/").Append (tPolys [i + 1] + 1 + normalOffset).Append (" ").
							Append (tPolys [i + 2] + 1 + uvOffset).Append ("/").Append (tPolys [i + 2] + 1 + uvOffset);
			meshstr.Append (sb.ToString () + "\n");
		}
		terrain = null;
		return meshstr;
	}

}