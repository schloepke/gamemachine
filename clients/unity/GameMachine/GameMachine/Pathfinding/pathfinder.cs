

using UnityEngine;
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Text;

namespace GameMachine.Pathfinding
{
    public class Pathfinder
    {
        public static int maxPaths = 2056;
        
        public int navmeshId;
        
        public Pathfinder (int navmeshId, string navmeshPath)
        {
            this.navmeshId = navmeshId;
            addNavmesh (navmeshId, navmeshPath);
        }
        
        public Crowd createCrowd (int id)
        {
            return new Crowd (id, navmeshId);
        }
        
        public ResultPath FindPath (Vector3 start, Vector3 end, bool straight = false)
        {
            ResultPath path = new ResultPath ();
            
            int straightPath = straight ? 1 : 0;
            float[,] resultPath = new float[Pathfinder.maxPaths, 3];
            int numPaths = findPath (navmeshId, start.z, start.y, start.x, end.z, end.y, end.x, straightPath, resultPath);
            
            if (numPaths <= 0) {
                path.error = numPaths;
                return path;
            }
            Vector3[] paths = new Vector3[numPaths];
            for (int i = 0; i < numPaths; i++) {
                paths [i] = new Vector3 (resultPath [i, 2], resultPath [i, 1], resultPath [i, 0]);
            }
            path.path = paths;
            path.error = 0;
            path.pathCount = numPaths;
            return path;
        }
        
        [DllImport("detour_path")]
        public static extern void gmapSetPassable (int navmeshId, int a, int b, int radius);
        
        [DllImport("detour_path")]
        public static extern void gmapSetBlocked (int navmeshId, int a, int b, int radius);
        
        [DllImport("detour_path")]
        public static extern void addNavmesh (int map, string filename);
        
        [DllImport("detour_path")]
        public static extern int findPath (
            int navmeshId,
            float startx,
            float starty,
            float startz,
            float endx,
            float endy,
            float endz,
            int find_straight_path,
            [In, Out]  float[,] resultPath
        );
    }
}
