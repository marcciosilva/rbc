package robot;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Vector3d;

import lejos.robotics.subsumption.Behavior;
import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;
import arbitrator.ArbitratorRBC;

public class Robot extends Agent {
	ArbitratorRBC arbitrator;
	private Behavior lastBehavior;
	CameraSensor camera;
	BufferedImage cameraImg;
	int currentFrame = 0;
	// velocidad de translacion
	double tv = 0.5f;
	Color currentAvgColor = new Color(0, 0, 0);

	public Robot(Vector3d position, String name) {
		super(position, name);
		// agregar camara
		camera = RobotFactory.addCameraSensor(this);
	}

	public void addArbitrator(ArbitratorRBC arb) {
		this.arbitrator = arb;
	}

	/** Initialize Agent's Behavior */
	public void initBehavior() {
		arbitrator.start();
	}

	/** Perform one step of Agent's Behavior */
	public void performBehavior() {
		currentFrame = (currentFrame + 1) % getFramesPerSecond();
		Behavior actualBehavior = arbitrator.getBehavior();
		if (!actualBehavior.equals(lastBehavior)) {
			if (lastBehavior != null) lastBehavior.suppress();
			actualBehavior.action();
		} else {
			actualBehavior.action();
		}

	}

	public CameraSensor getCamera() {
		return camera;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

}
