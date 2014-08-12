/*
Based on EditorObjExporter.cs and TerrainObjExporter.  Exports the current terrain plus selected objects to a single
wavefront .obj file.

The approach is to not invert the x axis, as negatives mess with pathfinding libraries like recastnavigation. 
Instead we swap x and z on everything, which works perfectly for pathfinding.  With recast we just have to swap x and z
on the vectors that we give it and get back from it.
 
This should be put in your "Editor"-folder. Use by selecting the objects you want to export, and select
"Custom->Export Scene" which will export to for_navmesh.obj in the root of your unity project.

  */
using UnityEngine;
using UnityEditor;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System;

public class SceneExporter : ScriptableObject
{
    private static int vertexOffset = 0;
    private static int normalOffset = 0;
    private static int uvOffset = 0;

    public static string TerrainToString (int vertexOffset, int normalOffset, int uvOffset)
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

        int[] tPolys;
		
        tPolys = new int[(w - 1) * (h - 1) * 6];

        // Build vertices
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                tVertices [y * w + x] = Vector3.Scale (meshScale, new Vector3 (x, tData [x * tRes, y * tRes], y)) + terrainPos;
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
		
        StringBuilder meshSb = new StringBuilder ();
        meshSb.Append ("g ").Append ("terrain").Append ("\n");
		
        // Write vertices
        System.Threading.Thread.CurrentThread.CurrentCulture = new System.Globalization.CultureInfo ("en-US");

        string vStr;
        for (int i = 0; i < tVertices.Length; i++) {
            vStr = string.Format ("v {0} {1} {2}\n", tVertices [i].x, tVertices [i].y, tVertices [i].z);
            meshSb.Append (vStr);
        }

        // Write triangles
        string faceStr;
        for (int i = 0; i < tPolys.Length; i += 3) {
            faceStr = string.Format ("f {0}/{0} {1}/{1} {2}/{2}\n", 
           		tPolys [i] + 1 + vertexOffset,
           		tPolys [i + 1] + 1 + normalOffset,
           		tPolys [i + 2] + 1 + uvOffset
            );

            meshSb.Append (faceStr);
        }
        terrain = null;
        return meshSb.ToString ();
    }
	
    private static string MeshToString (MeshFilter mf)
    {
        Mesh m = mf.sharedMesh;

        StringBuilder sb = new StringBuilder ();
		
        sb.Append ("g ").Append (mf.name).Append ("\n");
        foreach (Vector3 lv in m.vertices) {
            Vector3 wv = mf.transform.TransformPoint (lv);
            sb.Append (string.Format ("v {0} {1} {2}\n", wv.z, wv.y, wv.x));
        }
        //sb.Append ("\n");
				
        for (int material=0; material < m.subMeshCount; material ++) {
            //sb.Append ("\n");
						
            int[] triangles = m.GetTriangles (material);
            for (int i=0; i<triangles.Length; i+=3) {
                sb.Append (
					string.Format ("f {2}/{2} {1}/{1} {0}/{0}\n",
				    	triangles [i] + 1 + vertexOffset, triangles [i + 1] + 1 + normalOffset, triangles [i + 2] + 1 + uvOffset)
                );
            }
        }
		
        vertexOffset += m.vertices.Length;
        normalOffset += m.normals.Length;
        uvOffset += m.uv.Length;
		
        return sb.ToString ();
    }
	
    private static void Clear ()
    {
        vertexOffset = 0;
        normalOffset = 0;
        uvOffset = 0;
    }

    private static void MeshesToFile (MeshFilter[] mf, string filename)
    {
        using (StreamWriter sw = new StreamWriter(filename)) {

            for (int i = 0; i < mf.Length; i++) {
                sw.Write (MeshToString (mf [i]));
            }

            //string meshstr = TerrainToString (vertexOffset, normalOffset, uvOffset);
            //sw.Write (meshstr);
        }

    }
		
    [MenuItem ("Custom/Export Scene")]
    static void ExportWholeSelectionToSingle ()
    {
			
        Transform[] selection = Selection.GetTransforms (SelectionMode.Editable | SelectionMode.ExcludePrefab);
		
        if (selection.Length == 0) {
            EditorUtility.DisplayDialog ("No source object selected!", "Please select one or more target objects", "");
            return;
        }
		
        int exportedObjects = 0;
		
        ArrayList mfList = new ArrayList ();
		
        for (int i = 0; i < selection.Length; i++) {
            Component[] meshfilter = selection [i].GetComponentsInChildren (typeof(MeshFilter));
			
            for (int m = 0; m < meshfilter.Length; m++) {
                exportedObjects++;
                mfList.Add (meshfilter [m]);
            }
        }
		
        if (exportedObjects > 0) {
            MeshFilter[] mf = new MeshFilter[mfList.Count];
			
            for (int i = 0; i < mfList.Count; i++) {
                mf [i] = (MeshFilter)mfList [i];
            }
			
            string filename = "for_navmesh.obj";
            MeshesToFile (mf, filename);
			
			
            EditorUtility.DisplayDialog ("Objects exported", "Exported " + exportedObjects + " objects to " + filename, "");
        } else
            EditorUtility.DisplayDialog ("Objects not exported", "Make sure at least some of your selected objects have mesh filters!", "");
    }
			
}