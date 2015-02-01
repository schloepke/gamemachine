using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class GridMap
{

    public static GridVerticle[,] verticles;



    public static void Init (int w, int l)
    {
        verticles = new GridVerticle[w, l];
    }

    public static void AddCell (int x, int z, float slope, Vector3 vec)
    {
        GridVerticle verticle = verticles [x, z];
        if (verticle == null) {
            verticle = new GridVerticle ();
            verticles [x, z] = verticle;
        }
        GridCell cell = new GridCell ();
        cell.vector = vec;
        cell.slope = slope;
        verticle.cells.Add (cell);
    }

    public static GridVerticle GetVerticle (int x, int z)
    {
        return verticles [x, z];
    }
      
}
