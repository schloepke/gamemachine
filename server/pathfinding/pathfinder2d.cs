
using UnityEngine;
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Text;

namespace pathfinder
{
	public class Pathfinder2d
	{
		struct Point
		{
		    public int x, y;
		 
		    public Point(int px, int py)
		    {
		        x = px;
		        y = py;
		    }
		}

		public Pathfinder (int width, int height)
		{
			addPathfinder(1);
		}

		public Point[] FindPath (Point start, Point end, bool straight = false)
		{
			int straightPath = straight ? 1 : 0;
			int numPaths = findPath (1,start.x, start.y, end.x, end.y);
			
			Point[] paths = new Point[numPaths];
			for (int i = 0; i < numPaths; i++) {
				paths [i] = new Point (getXAt(1,i), getYAt(1,i));
			}
			return paths;
		}

		public setPassable(int x, int y)
		{
			setPassable(1,y,x);
		}

		public setBlocked(int x, int y)
		{
			setBlocked(1,y,x);
		}

		[DllImport("detour_path")]
		public static extern void addPathfinder (int pathfinder, int width, int height);

		[DllImport("detour_path")]
		public static extern void setPassable (int pathfinder, int x, int y);

		[DllImport("detour_path")]
		public static extern void setBlocked (int pathfinder, int x, int y);

		[DllImport("detour_path")]
		public static extern int getXAt (int pathfinder,int index);

		[DllImport("detour_path")]
		public static extern int getYAt (int pathfinder,int index);
		

		[DllImport("detour_path")]
		public static extern int findPath (
			int pathfinder,
            int startx,
            int starty,
            int endx,
            int endy
            );
	}
}
