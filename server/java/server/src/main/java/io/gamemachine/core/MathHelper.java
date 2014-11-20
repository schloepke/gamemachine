package io.gamemachine.core;

public class MathHelper {

	public static long cantorize(final long k1, final long k2) {
	    return (k1 + k2) * (k1 + k2 + 1) / 2 + k2;
	  }
}
