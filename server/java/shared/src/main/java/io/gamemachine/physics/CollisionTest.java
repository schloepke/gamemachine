package io.gamemachine.physics;

import org.ode4j.math.DVector3;
import org.ode4j.ode.DCapsule;
import org.ode4j.ode.DContactGeomBuffer;
import org.ode4j.ode.DRay;
import org.ode4j.ode.DSimpleSpace;
import org.ode4j.ode.OdeHelper;

public class CollisionTest {

	public static void run() {
		OdeHelper.initODE2(0);
		DSimpleSpace space = OdeHelper.createSimpleSpace(null);
		DCapsule cap = OdeHelper.createCapsule(null, 1, 1);
		space.add(cap);
		cap.setParams(0.5d, 2.0d);
		cap.setPosition(10d, 10d, 1d);

		DContactGeomBuffer contacts = new DContactGeomBuffer(1);
		DRay ray = OdeHelper.createRay(null, 0);
		space.add(ray);
		DVector3 a = new DVector3(50d, 50d, 50d);
		DVector3 b = new DVector3(10d, 10d, 2d);
		ray.setLength(500d);
		b.sub(a);
		b.normalize();
		System.out.println(b.toString());
		for (int i = 1; i < 10000; i++) {
			ray.set(a, b);
			int res = OdeHelper.collide(ray, cap, 1, contacts);
			//System.out.println("Collide result " + res);
			if (res >= 1) {
				//System.out.println("Contact at " + contacts.get().pos.toString());
			}
		}
		OdeHelper.closeODE();
	}
}
