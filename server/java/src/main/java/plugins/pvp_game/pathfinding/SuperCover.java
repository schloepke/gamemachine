package plugins.pvp_game.pathfinding;

import io.gamemachine.util.Vector3;

public class SuperCover {
	public static int cover(Graph graph, Vector3 from, Vector3 to, double cell_width, double cell_height) {
		int connections = Graph.useDiagonals ? 8 : 4;
		double maxSlope = Graph.maxSmoothSlope;
		double maxStep = Graph.maxStep;

		double x0 = from.x / cell_width;
		double y0 = from.y / cell_height;
		double x1 = to.x / cell_width;
		double y1 = to.y / cell_height;
		double dx = Math.abs(x1 - x0);
		double dy = Math.abs(y1 - y0);
		int x = ipart(x0);
		int y = ipart(y0);
		int num = 1;
		int sx, sy;
		double err;

		if (dx == 0) {
			sx = 0;
			err = Double.POSITIVE_INFINITY;
		} else if (x1 > x0) {
			sx = 1;
			num += ipart(x1) - x;
			err = rfpart(x0) * dy;
		} else {
			sx = -1;
			num += x - ipart(x1);
			err = fpart(x0) * dy;
		}

		if (dy == 0) {
			sy = 0;
			err = Double.NEGATIVE_INFINITY;
		} else if (y1 > y0) {
			sy = 1;
			num += ipart(y1) - y;
			err -= rfpart(y0) * dx;
		} else {
			sy = -1;
			num += y - ipart(y1);
			err -= fpart(y0) * dx;
		}

		// number of square to be plotted : num

		Node node = null;
		Node previous = null;
		//System.out.println("START " + height);
		while (true) {
			node = graph.getNode(x, y);
			//if (node != null) {
			//	System.out.println(node.position.z + " " + height);
			//}
			if (node == null) {
				return 1;
			}

			if (node.slope > maxSlope) {
				return 2;
			}

			if (previous != null) {
				if (previous.stepCost(node) > maxStep) {
					return 3;
				}
			}
			previous = node;

			if (--num == 0)
				break;

			if (err > 0) {
				err -= dx;
				y += sy;
			} else {
				err += dy;
				x += sx;
			}
		}
		
			return 0;
	}

	private static int ipart(double x) {
		return (int) Math.floor(x);
	}

	private static double fpart(double x) {
		return x - ipart(x);
	}

	private static double rfpart(double x) {
		return 1.0f - fpart(x);
	}
}
