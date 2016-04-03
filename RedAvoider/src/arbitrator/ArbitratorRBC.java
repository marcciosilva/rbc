package arbitrator;

/*
 * Implementacion del arbitro de lejos adaptado al simulador
 */
import lejos.robotics.subsumption.Behavior;

public class ArbitratorRBC {
	private Behavior[] bviorArray;
	
	public ArbitratorRBC(Behavior[] bviorArray){
		this.bviorArray = bviorArray;
	}
	
	public void start(){
		
	}
	
	public Behavior getBehavior(){
		boolean takeControl = false;
		int id = bviorArray.length-1;
		while (!takeControl){
			if (bviorArray[id].takeControl()) takeControl = true;
			else id--;
		}
		return bviorArray[id];
	}
}
