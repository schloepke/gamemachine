package io.gamemachine.util;

public class Vector3 {
	public double x;
	public double y;
	public double z;

	public Vector3() {
	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector3 zero() {
		return new Vector3(0f,0f,0f);
	}
	
	public static Vector3 from(Vector3 other) {
		Vector3 vec = new Vector3(other.x, other.y, other.z);
		return vec;
	}

	public Vector3(Vector3 other) {
		x = other.x;
		y = other.y;
		z = other.z;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	public Vector3 set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3 set(Vector3 other) {
		x = other.x;
		y = other.y;
		z = other.z;
		return this;
	}

	public boolean isEqualTo(Vector3 other) {
		return (x == other.x && y == other.y && z == other.z);
	}
	
	public void addLocal(Vector3 other) {
		x += other.x;
		y += other.y;
		z += other.z;
	}

	public Vector3 add(Vector3 other) {
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	public void addLocal(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public Vector3 sub(Vector3 other) {
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}
	
	public Vector3 subtract(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	public Vector3 addNew(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	public Vector3 scl (double scalar) {
		return this.set(this.x * scalar, this.y * scalar, this.z * scalar);
	}
	
	public double angle(Vector3 other) {
		// TODO: Find a faster arccos and sqrt method

		double lengthSquare = x * x + y * y + z * z;
		double otherLengthSquare = other.x * other.x + other.y * other.y + other.z * other.z;

		double angle = Math.acos((x * other.x + y * other.y + z * other.z)
				/ Math.sqrt(lengthSquare * otherLengthSquare));

		return angle;
	}

	public Vector3 cross(Vector3 other) {
		return new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	public double dot(Vector3 other) {
		return (x * other.x + y * other.y + z * other.z);
	}

	public void crossLocal(Vector3 other) {
		set(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	public Vector3 multiply(double scalar) {
		return new Vector3(x * scalar, y * scalar, z * scalar);
	}

	public void multiplyLocal(double scalar) {
		x = scalar * x;
		y = scalar * y;
		z = scalar * z;
	}

	public Vector3 normalize() {
		double length = Math.sqrt(x * x + y * y + z * z);
		if (length > Double.MIN_NORMAL) {
			return new Vector3(x / length, y / length, z / length);
		} else {
			return new Vector3(0, 0, 0);
		}
	}

	public void normalizeLocal() {
		double length = Math.sqrt(x * x + y * y + z * z);
		if (length > Double.MIN_NORMAL) {
			x /= length;
			y /= length;
			z /= length;
		} else {
			x = y = z = 0;
		}
	}

	public double magnitudeSquared() {
	    return Math.pow(this.x,2) + Math.pow(this.y,2);
	}
	
	// Obtain distance between position vectors
	public double distance(Vector3 other) {
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2));
	}
	
	public double distance2d(Vector3 other) {
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
	}
	
	public double distance2d(double otherX, double otherY) {
		return Math.sqrt(Math.pow(x - otherX, 2) + Math.pow(y - otherY, 2));
	}

	// Project the vector onto the plane passing through the origin,
	// perpendicular to the given normal
	public Vector3 projectNormal(Vector3 normal) {
		return subtract(normal.multiply(this.dot(normal)));
	}

	public Vector3 rotate(Vector3 normal, double angle) {
		// Parametric equation for circle in 3D space:
		// P = Rcos(t)u + Rsin(t)nxu + c
		//
		// Where:
		// -u is a unit vector from the centre of the circle to any point
		// on the circumference
		// -R is the radius
		// -n is a unit vector perpendicular to the plane
		// -c is the centre of the circle.

		// TODO: obtain a more efficient sin function

		Vector3 rotated;

		rotated = normal.normalize();
		rotated.crossLocal(this);
		rotated.multiplyLocal(Math.sin(angle));
		rotated.addLocal(this.multiply(Math.cos(angle)));

		return rotated;
	}

	public static String toString(Vector3 v) {
		return "(" + v.x + ", " + v.y + ", " + v.z + ")";
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
