package io.gamemachine.pathfinding.grid;

import io.gamemachine.pathfinding.Node;
import io.gamemachine.util.Vector3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Array;

public class Funnel2d {

	public static List<Vector3> smoothPath(Node startNode, Node endNode, Array<Node> paths) {
		List<Vector3> points;
		Vector3 startPos = startNode.position;
		Vector3 destPos = endNode.position;

		if (paths.size <= 2) {
			points = new ArrayList<Vector3>();
			points.add(startPos);
			points.add(destPos);
			return points;
		}

		Vector3[] aLeftEndPts = new Vector3[paths.size - 1];
		Vector3[] aRightEndPts = new Vector3[paths.size - 1];
		computePortals(startNode, paths, aLeftEndPts, aRightEndPts);
		int maxNumSmoothPts = paths.size + 1;

		int numPortalPts = aLeftEndPts.length + aRightEndPts.length + 1 * 2 + 1 * 2; // +1
																						// for
																						// start
																						// +1
																						// for
																						// goal.
		Vector3[] portals = new Vector3[numPortalPts];
		portals[0] = startPos;
		portals[1] = startPos;

		int endPtCounter = 0;
		for (int i = 2; i < numPortalPts - 2; i += 2) {
			portals[i] = aLeftEndPts[endPtCounter];
			portals[i + 1] = aRightEndPts[endPtCounter];
			endPtCounter++;
		}
		portals[numPortalPts - 2] = destPos;
		portals[numPortalPts - 1] = destPos;
		Vector3[] smoothPathPts = new Vector3[maxNumSmoothPts];
		points = StringPull(portals, numPortalPts, smoothPathPts, maxNumSmoothPts);

		return points;
	}

	static double triArea2(Vector3 a, Vector3 b, Vector3 c) {
		double ax = b.x - a.x;
		double az = b.y - a.y;
		double bx = c.x - a.x;
		double bz = c.y - a.y;
		return bx * az - ax * bz;
	}

	public static boolean areVertsTheSame(Vector3 v1, Vector3 v2) {
		double eps = 0.001f * 0.001f;
		return (v1.subtract(v2).magnitudeSquared() < eps);
	}

	public static List<Vector3> StringPull(Vector3[] portals, int nportalPts, Vector3[] pts, int maxPts) {
		List<Vector3> points = new ArrayList<Vector3>();

		// Find straight path.
		int npts = 0;
		// Init scan state
		Vector3 portalApex = portals[0];
		Vector3 portalLeft = portals[2];
		Vector3 portalRight = portals[3];
		int apexIndex = 0, leftIndex = 0, rightIndex = 0;

		// Add start point.
		pts[0] = portalApex;
		npts++;

		for (int i = 1; ((i < (nportalPts / 2)) && (npts < maxPts)); ++i) {
			Vector3 left = portals[i * 2 + 0];
			Vector3 right = portals[i * 2 + 1];

			// Update right vertex.
			if (triArea2(portalApex, portalRight, right) <= 0.0f) {
				if (areVertsTheSame(portalApex, portalRight) || triArea2(portalApex, portalLeft, right) > 0.0f) {
					// System.out.println("tighten funnel");
					// Tighten the funnel.
					portalRight = right;
					rightIndex = i;
				} else {
					// Right over left, insert left to path and restart scan
					// from portal left point.
					// pts[npts] = portalLeft;
					npts++;
					points.add(portalLeft);
					// Make current left the new apex.
					portalApex = portalLeft;
					apexIndex = leftIndex;
					// Reset portal
					portalLeft = portalApex;
					portalRight = portalApex;
					leftIndex = apexIndex;
					rightIndex = apexIndex;
					// Restart scan
					i = apexIndex;
					continue;
				}
			}

			// Update left vertex.
			if (triArea2(portalApex, portalLeft, left) >= 0.0f) {
				if (areVertsTheSame(portalApex, portalLeft) || triArea2(portalApex, portalRight, left) < 0.0f) {
					// System.out.println("tighten funnel");
					// Tighten the funnel.
					portalLeft = left;
					leftIndex = i;
				} else {
					// Left over right, insert right to path and restart scan
					// from portal right point.
					// pts[npts] = portalRight;
					points.add(portalRight);
					npts++;
					// Make current right the new apex.
					portalApex = portalRight;
					apexIndex = rightIndex;
					// Reset portal
					portalLeft = portalApex;
					portalRight = portalApex;
					leftIndex = apexIndex;
					rightIndex = apexIndex;
					// Restart scan
					i = apexIndex;
					continue;
				}
			}
		}

		// Append last point to path.
		if (npts < maxPts) {
			// pts[npts] = portals[nportalPts-1];
			points.add(portals[nportalPts - 1]);
			npts++;
		}

		return points;
	}

	public static void computePortals(Node startNode, Array<Node> paths, Vector3[] leftEndPoints, Vector3[] rightEndPoints) {
		Vector3 startPos = startNode.position;

		if (paths.size < 2) {
			return;
		}

		for (int i = 0; i < paths.size - 1; i++) {
			Vector3 currentPos = paths.get(i).position;
			Vector3 nextPos = paths.get(i + 1).position;
			double m_cellSize = 1d;
			Vector3 center = Vector3.from(currentPos);
			center.x += (m_cellSize / 2.0f);
			center.y += (m_cellSize / 2.0f);

			Vector3 dist = new Vector3(1d, 1d, 1d);
			Vector3 min = center.subtract(dist);
			Vector3 max = center.addNew(dist);
			BoundingBox bounds = new BoundingBox(min, max);

			Vector3 leftPoint = null, rightPoint = null;
			startPos = nextPos;
			
			if (Graph.useDiagonals) {
				// topleft
				if (nextPos.y == currentPos.y + 1 && nextPos.x == currentPos.x - 1) {
					leftPoint = rightPoint = new Vector3(bounds.min.x, bounds.max.y, startPos.z);

					// topright
				} else if (nextPos.y == currentPos.y + 1 && nextPos.x == currentPos.x + 1) {
					leftPoint = rightPoint = new Vector3(bounds.max.x, bounds.max.y, startPos.z);

					// bottomleft
				} else if (nextPos.y == currentPos.y - 1 && nextPos.x == currentPos.x - 1) {
					leftPoint = rightPoint = new Vector3(bounds.min.x, bounds.min.y, startPos.z);

					// bottomright
				} else if (nextPos.y == currentPos.y - 1 && nextPos.x == currentPos.x + 1) {
					leftPoint = rightPoint = new Vector3(bounds.max.x, bounds.min.y, startPos.z);
				}
				if (leftPoint != null) {
					leftEndPoints[i] = leftPoint;
					rightEndPoints[i] = rightPoint;
					continue;
				}
			}

			// right
			if (nextPos.x == currentPos.x + 1) {
				leftPoint = new Vector3(bounds.max.x, bounds.max.y, startPos.z);
				rightPoint = new Vector3(bounds.max.x, bounds.min.y, startPos.z);

				// left
			} else if (nextPos.x == currentPos.x - 1) {
				leftPoint = new Vector3(bounds.min.x, bounds.min.y, startPos.z);
				rightPoint = new Vector3(bounds.min.x, bounds.max.y, startPos.z);

				// up
			} else if (nextPos.y == currentPos.y + 1) {
				leftPoint = new Vector3(bounds.min.x, bounds.max.y, startPos.z);
				rightPoint = new Vector3(bounds.max.x, bounds.max.y, startPos.z);

				// down
			} else if (nextPos.y == currentPos.y - 1) {
				leftPoint = new Vector3(bounds.max.x, bounds.min.y, startPos.z);
				rightPoint = new Vector3(bounds.min.x, bounds.min.y, startPos.z);

			} else {
				System.out.println("no bounds " + currentPos.toString() + "->" + nextPos.toString());
				leftPoint = Vector3.zero();
				rightPoint = Vector3.zero();
			}

			// System.out.println("position " + dir + " " +
			// currentPos.toString() + "->" + nextPos.toString() + " bounds "
			// + leftPoint.toString() + " " + rightPoint.toString());
			leftEndPoints[i] = leftPoint;
			rightEndPoints[i] = rightPoint;
		}
	}
}
