
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using ProtoBuf;
using PMesh = io.gamemachine.messages.Mesh;
using MeshNode = io.gamemachine.messages.MeshNode;
using PVector3 = io.gamemachine.messages.Vector3;
using TriangleMesh = io.gamemachine.messages.TriangleMesh;
using TriangleMesh2 = io.gamemachine.messages.TriangleMesh2;
using PathData = io.gamemachine.messages.PathData;
using GridData = io.gamemachine.messages.GridData;
using GridNode = io.gamemachine.messages.GridNode;
using PGridVerticle = io.gamemachine.messages.GridVerticle;
using GameMachine;

public class GridExport
{

    public static int w = 600;
    public static int l = 600;
    public static int h = 50;
    public static float cellWidth = 0.5f;

    public static void AddColliders (MeshFilter mFilter)
    {
        NavMeshTriangulation tri = NavMesh.CalculateTriangulation ();
        mFilter.mesh.vertices = tri.vertices;
        mFilter.mesh.triangles = tri.indices;
        GameObject p = mFilter.gameObject;
        MeshCollider collider = p.GetComponent<MeshCollider> ();
        collider.sharedMesh = mFilter.mesh;

        Object[] meshFilters = Resources.FindObjectsOfTypeAll (typeof(MeshFilter));
        foreach (Object obj in meshFilters) {
            MeshFilter meshFilter = (MeshFilter)obj;
            GameObject parent = meshFilter.gameObject;
            MeshCollider c = parent.GetComponent<MeshCollider> ();
            if (c == null) {
                parent.AddComponent<MeshCollider> ();
            }
        }

    }

    public static void ExportNavMesh ()
    {

        GridMap.Init (w, l);
        Vector3[,,] verticles = new Vector3[w, l, h];
        float[,,] slopes = new float[w, l, h];

        Vector3 point;
        int verticleCount = 0;

        Terrain terrain = Terrain.activeTerrain;
        int meshMask = ~(1 << LayerMask.NameToLayer ("navmesh"));
        int layerMask = (1 << LayerMask.NameToLayer ("navmesh"));

        Debug.Log ("navmesh layer is " + LayerMask.NameToLayer ("navmesh"));
        RaycastHit hit1 = new RaycastHit ();
        RaycastHit hitc = new RaycastHit ();
        Dictionary<float,bool> navlayers;

        float xf = 0f;
        for (int x = 0; x < w; x++) {
            float zf = 0f;
            for (int z = 0; z < l; z++) {
                
                Vector3 verticle = new Vector3 (-1f, -1f, -1f);
                int vertLayerCount = 0;
                navlayers = new Dictionary<float,bool> ();

                for (int y = 1; y < h; y+=2) {
                    
                    Vector3 center = new Vector3 (xf + cellWidth / 2, 0f, zf + cellWidth / 2) + Vector3.up * y;
                    hit1 = new RaycastHit ();
                    bool raym = Physics.Raycast (center, -Vector3.up, out hit1, y + 0.005F, layerMask);
                    if (!raym) {
                        continue;
                    }

                    if (navlayers.ContainsKey (hit1.point.y)) {
                        continue;
                    }

                    hitc = new RaycastHit ();
                    bool rayc = Physics.Raycast (center, -Vector3.up, out hitc, y + 0.005F, meshMask);
                    if (!rayc) {
                        continue;
                    }
                   
                    if (hitc.point.y == verticle.y) {
                        continue; 
                    }

                    //if (!Walkable (xf, zf, y)) {
                    //    navlayers.Add (hit1.point.y, true);
                    //    continue;
                    //}

                    float angle = Vector3.Angle (hitc.normal, Vector3.up);
                                                
                    verticle = new Vector3 (hitc.point.x, hitc.point.y, hitc.point.z);
                    GridMap.AddCell (x, z, angle, verticle);
                    verticles [x, z, vertLayerCount] = verticle;
                    slopes [x, z, vertLayerCount] = angle;
                    vertLayerCount++;
                    verticleCount++;
                    navlayers.Add (hit1.point.y, true);
                }
                zf = zf + cellWidth;

            }
            xf = xf + cellWidth;
        }
        
        DrawGrid (verticles, slopes);
        Debug.Log ("verticleCount=" + verticleCount);
        SaveGrid (verticles, slopes);

    }

    public static bool Walkable (float xf, float zf, int y)
    {
        int layerMask = (1 << LayerMask.NameToLayer ("navmesh"));
        float dist = cellWidth;
        RaycastHit hit1 = new RaycastHit ();
        RaycastHit hit2 = new RaycastHit ();
        RaycastHit hit3 = new RaycastHit ();
        RaycastHit hit4 = new RaycastHit ();
        Vector3 position1 = new Vector3 (xf, 0f, zf) + Vector3.up * y;
        Vector3 position2 = new Vector3 (xf, 0f, zf + dist) + Vector3.up * y;
        Vector3 position3 = new Vector3 (xf + dist, 0f, zf + dist) + Vector3.up * y;
        Vector3 position4 = new Vector3 (xf + dist, 0f, zf) + Vector3.up * y;
        
        bool ray1 = Physics.Raycast (position1, -Vector3.up, out hit1, y + 0.005F, layerMask);
        bool ray2 = Physics.Raycast (position2, -Vector3.up, out hit2, y + 0.005F, layerMask);
        bool ray3 = Physics.Raycast (position3, -Vector3.up, out hit3, y + 0.005F, layerMask);
        bool ray4 = Physics.Raycast (position4, -Vector3.up, out hit4, y + 0.005F, layerMask);
        
        if (ray1 && ray2 && ray3 && ray4) {
            return true;
        } else {
            return false;
        }
    }




    public static GridNode ToGridNode (Vector3 point, float slope)
    {
        GridNode node = new GridNode ();
        PVector3 vec = new PVector3 ();
        vec.x = point.x;
        vec.y = point.z;
        vec.z = point.y;
        node.point = vec;
        node.slope = slope;
        return node;
    }

    public static void SaveGrid (Vector3[,,] verticles, float[,,] slopes)
    {
        GridData data = new GridData ();
        data.w = w;
        data.h = l;

        for (int x = 0; x < w; x++) {
            for (int z = 0; z < l; z++) {
                PGridVerticle v = new PGridVerticle ();
                v.x = x;
                v.y = z;
                for (int y = 0; y < h; y++) {
                    Vector3 verticle = verticles [x, z, y];
                    if (verticle != Vector3.zero) {
                        float slope = slopes [x, z, y];
                        GridNode node = ToGridNode (verticle, slope);
                        v.gridNodes.Add (node);
                    }
                }
                data.gridVerticles.Add (v);
            }
        }
        MemoryStream stream = new MemoryStream ();
        Serializer.Serialize (stream, data);
        File.WriteAllBytes ("grid_data.bin", stream.ToArray ());
    }

    public static void DrawSquare (Vector3 verticle)
    {
        Color color = Color.red;
        Vector3 v1 = new Vector3 (verticle.x, verticle.y, verticle.z);
        Vector3 v2 = new Vector3 (verticle.x + cellWidth, verticle.y, verticle.z);
        Vector3 v3 = new Vector3 (verticle.x + cellWidth, verticle.y, verticle.z + cellWidth);
        Vector3 v4 = new Vector3 (verticle.x, verticle.y, verticle.z + cellWidth);
        Debug.DrawLine (v1, v2, color, 10000);
        Debug.DrawLine (v2, v3, color, 10000);
        Debug.DrawLine (v3, v4, color, 10000);
        Debug.DrawLine (v4, v1, color, 10000);
    }

    public static void DrawGrid (Vector3[,,] verticles, float[,,] slopes)
    {
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < l; z++) {
                for (int y = 0; y < h; y++) {
                    Vector3 verticle = verticles [x, z, y];
                    if (verticle != Vector3.zero) {
                        float slope = slopes [x, z, y];
                        //if (slope <= 45) {
                        DrawSquare (verticle);
                        //}
                    }
                }
            }
        }
    }


}
