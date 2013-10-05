using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Text;

namespace pathfinder
{
    public class Pathfinder
    {
        static void Main(string[] args)
        {
            string filename = "test.bin";
            int res;
            float[,] resultPath = new float[100,3];
            res = loadNavMesh(1,filename);
            Console.Out.WriteLine(res);
            res = FindSmoothPath(1, 1, 500.0f, 0.2f, 526.0f, 528.0f, 0.2f, 511.0f, resultPath);
            Console.Out.WriteLine(res);
            for (int i = 0; i < res; i++)
            {
           
                Console.WriteLine(string.Format("{0} {1} {2}", resultPath[i,0], resultPath[i,1], resultPath[i,2]));
            }
        }

        public static int FindStraightPath(
            int queryId,
            int map,
            float startx,
            float starty,
            float startz,
            float endx,
            float endy,
            float endz,
            float[,] resultPath
            )
        {
            return findPath(queryId, map, startx, starty, startz, endx, endy, endz, 1, resultPath);
        }

        public static int FindSmoothPath(
            int queryId,
            int map,
            float startx,
            float starty,
            float startz,
            float endx,
            float endy,
            float endz,
            float[,] resultPath
            )
        {
            return findPath(queryId, map, startx, starty, startz, endx, endy, endz, 0, resultPath);
        }


        [DllImport("pathfind")]
        public static extern int loadNavMesh(int map, string filename);

        [DllImport("pathfind")]
        public static extern int findPath(
            int queryId,
            int map,
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
