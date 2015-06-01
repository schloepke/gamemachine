// Converted from UnityScript to C# at http://www.M2H.nl/files/js_to_c.php - by Mike Hergaarden
// C # manual conversion work by Yun Kyu Choi

using UnityEngine;
using UnityEditor;
using System;
using System.Collections;
using System.IO;
using System.Text;
using  System.Text.RegularExpressions;

enum SaveFormat
{
	Triangles,
	Quads
}
enum SaveResolution
{
	Full=0,
	Half,
	Quarter,
	Eighth,
	Sixteenth
}

class ExportTerrain : EditorWindow
{
	public static SaveFormat saveFormat = SaveFormat.Triangles;
	public static SaveResolution saveResolution = SaveResolution.Quarter;
    
	static TerrainData terrain;
	static Vector3 terrainPos;
	static Vector3 terrainWorldPos;
	public static Terrain terrainObject;
    
	public static int tCount;
	public static int counter;
	public static int totalCount;
	public static int progressUpdateInterval = 10000;
    
	[MenuItem("Terrain/Combine meshes")]
	public static void Combine ()
	{
		MeshFilter[] meshFilters = GameObject.Find ("terrain_meshes").GetComponentsInChildren<MeshFilter> ();
		CombineInstance[] combine = new CombineInstance[meshFilters.Length];
		int i = 0;
		while (i < meshFilters.Length) {
			combine [i].mesh = meshFilters [i].sharedMesh;
			//combine [i].transform = meshFilters [i].transform.localToWorldMatrix;
			//meshFilters [i].gameObject.active = false;
			i++;
		}
		GameObject go = new GameObject ();
		go.name = "combined_meshes";
		MeshFilter mfilter = go.AddComponent<MeshFilter> () as MeshFilter;
		mfilter.sharedMesh = new Mesh ();
		mfilter.sharedMesh.CombineMeshes (combine);
		MeshRenderer mr = go.AddComponent<MeshRenderer> () as MeshRenderer;
		mr.material = new Material (Shader.Find ("Diffuse"));
	}

	[MenuItem("Terrain/Rename meshes")]
	public static void RenameTerrainMeshes ()
	{
		GameObject p = GameObject.Find ("terrain_meshes");
		foreach (Transform meshObj in p.transform) {
			Transform child = meshObj.transform.Find ("default");
			child.parent = p.transform;
			string terrainName = meshObj.name;
			meshObj.name = "unused";
			child.name = terrainName;
		}
	}


	[MenuItem("Terrain/Position meshes")]
	public static void PositionTerrainMeshes ()
	{
		foreach (Transform meshObj in GameObject.Find ("terrain_meshes").transform) {
			string terrainName = meshObj.name.Replace ("MeshTerrain", "Terrain");
			GameObject terrainObj = GameObject.Find (terrainName);
			Debug.Log ("Positioning " + terrainObj.name + " " + meshObj.name + " at " + terrainObj.transform.position);
			//Vector3 pos = new Vector3 (terrainObj.transform.position.x - 4000, 0f, terrainObj.transform.position.z - 3000f);
			meshObj.transform.position = terrainObj.transform.position;
		}
	}

	[MenuItem("Terrain/Export All")]
	public static void ExportAll ()
	{
		saveResolution = SaveResolution.Eighth;
		//saveFormat = SaveFormat.Quads;
		foreach (Transform t in GameObject.Find ("Terrains").transform) {
			if (t.name != "Terrain27") {
				continue;
			}
			terrainObject = t.GetComponent<Terrain> () as Terrain;
			terrain = terrainObject.terrainData;
			terrainPos = Vector3.zero;//terrainObject.transform.position;
			terrainWorldPos = new Vector3 (terrainObject.transform.position.x, terrainObject.transform.position.y, terrainObject.transform.position.z);
			Export ();
		}
	}

	[MenuItem("Terrain/Export To Obj...")]
	static void Init ()
	{
		terrain = null;
		Terrain terrainObject = GameObject.Find ("Terrain10").GetComponent<Terrain> () as Terrain;//Selection.activeObject as Terrain;

		if (!terrainObject) {
			Debug.Log ("No terrain selected");
			terrainObject = Terrain.activeTerrain;
		}
		if (terrainObject) {
			terrain = terrainObject.terrainData;
			terrainPos = terrainObject.transform.position;
		}
        
		EditorWindow.GetWindow<ExportTerrain> ().Show ();
	}
    
	void OnGUI ()
	{
		if (!terrain) {
			GUILayout.Label ("No terrain found");
			if (GUILayout.Button ("Cancel")) {
				EditorWindow.GetWindow<ExportTerrain> ().Close ();
			}
			return;
		}
		saveFormat = (SaveFormat)EditorGUILayout.EnumPopup ("Export Format", saveFormat);
        
		saveResolution = (SaveResolution)EditorGUILayout.EnumPopup ("Resolution", saveResolution);
        
		if (GUILayout.Button ("Export")) {
			Export ();
		}
	}
    
	public static void Export ()
	{
		string fileName = Application.dataPath + "/mmo/Resources/terrain_mesh/Mesh" + terrainObject.name + ".obj";
		File.Delete (fileName);
		//string fileName = EditorUtility.SaveFilePanel ("Export .obj file", "", terrainObject.name, "obj");
		int w = terrain.heightmapWidth;
		int h = terrain.heightmapHeight;
		Vector3 meshScale = terrain.size;
		int tRes = (int)Mathf.Pow (2, (int)saveResolution);
		meshScale = new Vector3 (meshScale.x / (w - 1) * tRes, meshScale.y, meshScale.z / (h - 1) * tRes);
		Vector2 uvScale = new Vector2 (1.0f / (w - 1), 1.0f / (h - 1));
		float[,] tData = terrain.GetHeights (0, 0, w, h);
        
		w = (w - 1) / tRes + 1;
		h = (h - 1) / tRes + 1;
		Vector3[] tVertices = new Vector3[w * h];
		Vector2[] tUV = new Vector2[w * h];
        
		int[] tPolys;
        
		if (saveFormat == SaveFormat.Triangles) {
			tPolys = new int[(w - 1) * (h - 1) * 6];
		} else {
			tPolys = new int[(w - 1) * (h - 1) * 4];
		}
        
		// Build vertices and UVs
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				tVertices [y * w + x] = Vector3.Scale (meshScale, new Vector3 (-y, tData [x * tRes, y * tRes], x)) + terrainPos;
				tUV [y * w + x] = Vector2.Scale (new Vector2 (x * tRes, y * tRes), uvScale);
			}
		}
        
		int index = 0;
		if (saveFormat == SaveFormat.Triangles) {
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
		} else {
			// Build quad indices: 4 indices into vertex array for each quad
			for (int y = 0; y < h - 1; y++) {
				for (int x = 0; x < w - 1; x++) {
					// For each grid cell output one quad
					tPolys [index++] = (y * w) + x;
					tPolys [index++] = ((y + 1) * w) + x;
					tPolys [index++] = ((y + 1) * w) + x + 1;
					tPolys [index++] = (y * w) + x + 1;
				}
			}
		}
        
//		GameObject parent = GameObject.Find ("terrain_meshes");
//		GameObject go = new GameObject ();
//		go.transform.parent = parent.transform;
//		go.transform.position = terrainPos;
//		go.name = "Mesh" + terrainObject.name;
//		Mesh mesh = new Mesh ();
//		MeshFilter meshFilter = go.AddComponent<MeshFilter> ();
//		MeshRenderer mr = go.AddComponent<MeshRenderer> () as MeshRenderer;
//		mr.material = new Material (Shader.Find ("Diffuse"));
//		meshFilter.mesh = mesh;
//		
//		mesh.vertices = tVertices;
//		mesh.triangles = tPolys;
//		mesh.SetIndices (tPolys, MeshTopology.Quads, 0);
//
//
//		terrain = null;
//		return;

		// Export to .obj
		StreamWriter sw = new StreamWriter (fileName);
		try {
            
			sw.WriteLine ("# Unity terrain OBJ File");
            
			// Write vertices
			System.Threading.Thread.CurrentThread.CurrentCulture = new System.Globalization.CultureInfo ("en-US");
			counter = tCount = 0;
			totalCount = (tVertices.Length * 2 + (saveFormat == SaveFormat.Triangles ? tPolys.Length / 3 : tPolys.Length / 4)) / progressUpdateInterval;
			for (int i = 0; i < tVertices.Length; i++) {
				UpdateProgress ();
				StringBuilder sb = new StringBuilder ("v ", 20);
				// StringBuilder stuff is done this way because it's faster than using the "{0} {1} {2}"etc. format
				// Which is important when you're exporting huge terrains.
				sb.Append (tVertices [i].x.ToString ()).Append (" ").
                    Append (tVertices [i].y.ToString ()).Append (" ").
                        Append (tVertices [i].z.ToString ());
				sw.WriteLine (sb);
			}
			// Write UVs
			for (int i = 0; i < tUV.Length; i++) {
				UpdateProgress ();
				StringBuilder sb = new StringBuilder ("vt ", 22);
				sb.Append (tUV [i].x.ToString ()).Append (" ").
                    Append (tUV [i].y.ToString ());
				sw.WriteLine (sb);
			}
			if (saveFormat == SaveFormat.Triangles) {
				// Write triangles
				for (int i = 0; i < tPolys.Length; i += 3) {
					UpdateProgress ();
					StringBuilder sb = new StringBuilder ("f ", 43);
					sb.Append (tPolys [i] + 1).Append ("/").Append (tPolys [i] + 1).Append (" ").
                        Append (tPolys [i + 1] + 1).Append ("/").Append (tPolys [i + 1] + 1).Append (" ").
                            Append (tPolys [i + 2] + 1).Append ("/").Append (tPolys [i + 2] + 1);
					sw.WriteLine (sb);
				}
			} else {
				// Write quads
				for (int i = 0; i < tPolys.Length; i += 4) {
					UpdateProgress ();
					StringBuilder sb = new StringBuilder ("f ", 57);
					sb.Append (tPolys [i] + 1).Append ("/").Append (tPolys [i] + 1).Append (" ").
                        Append (tPolys [i + 1] + 1).Append ("/").Append (tPolys [i + 1] + 1).Append (" ").
                            Append (tPolys [i + 2] + 1).Append ("/").Append (tPolys [i + 2] + 1).Append (" ").
                            Append (tPolys [i + 3] + 1).Append ("/").Append (tPolys [i + 3] + 1);
					sw.WriteLine (sb);
				}
			}
		} catch (Exception err) {
			Debug.Log ("Error saving file: " + err.Message);
		}
		sw.Close ();
        
		terrain = null;
		EditorUtility.DisplayProgressBar ("Saving file to disc.", "This might take a while...", 1f);
		EditorWindow.GetWindow<ExportTerrain> ().Close ();      
		EditorUtility.ClearProgressBar ();
	}
    
	public static void UpdateProgress ()
	{
		if (counter++ == progressUpdateInterval) {
			counter = 0;
			EditorUtility.DisplayProgressBar ("Saving...", "", Mathf.InverseLerp (0, totalCount, ++tCount));
		}
	}
}