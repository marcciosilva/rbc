package main;

import java.awt.Color;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import lejos.robotics.subsumption.Behavior;
import robot.Robot;
import simbad.gui.Simbad;
import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;
import arbitrator.ArbitratorRBC;
import behaviors.EvitarRojo;
import behaviors.Wander;

public class RedAvoider {

	static public class MyEnv extends EnvironmentDescription {
		public MyEnv() {
			light1IsOn = true;
			light2IsOn = true;
			Color3f red = new Color3f(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue());
			Color3f blue = new Color3f(Color.BLUE.getRed(), Color.BLUE.getGreen(),
					Color.BLUE.getBlue());
			this.boxColor = red;
			this.backgroundColor = blue;
			this.floorColor = blue;
			Box b1 = new Box(new Vector3d(6, 0, -3), new Vector3f(1, 1, 1), this);
			add(b1);
			Box b2 = new Box(new Vector3d(-1, 0, 5), new Vector3f(1, 1, 1), this);
			add(b2);
			Box b3 = new Box(new Vector3d(8, 0, 1), new Vector3f(1, 1, 1), this);
			add(b3);
			Box b4 = new Box(new Vector3d(-4, 0, -6), new Vector3f(1, 1, 1), this);
			add(b4);
			Wall w1 = new Wall(new Vector3d(9, 0, 0), 19, 1, this);
			w1.rotate90(1);
			add(w1);
			Wall w2 = new Wall(new Vector3d(-9, 0, 0), 19, 2, this);
			w2.rotate90(1);
			add(w2);
			Wall w3 = new Wall(new Vector3d(0, 0, 9), 19, 1, this);
			add(w3);
			Wall w4 = new Wall(new Vector3d(0, 0, -9), 19, 2, this);
			add(w4);
			Robot r = new Robot(new Vector3d(0, 0, 0), "robot 1");
			add(r);

			// agrego comportamientos
			Behavior[] bviorArray = new Behavior[2];
			Wander wander = new Wander(r);
			EvitarRojo evitR = new EvitarRojo(r);

			bviorArray[0] = wander;
			bviorArray[1] = evitR;

			// inicializo arbitrator con comportamientos
			ArbitratorRBC arb = new ArbitratorRBC(bviorArray);
			r.addArbitrator(arb);

		}
	}

	public static void main(String[] args) {
		Simbad simbad = new Simbad(new MyEnv(), false);
	}
}