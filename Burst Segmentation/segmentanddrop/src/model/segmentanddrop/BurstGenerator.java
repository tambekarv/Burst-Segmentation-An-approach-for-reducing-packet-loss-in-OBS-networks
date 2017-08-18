package model.segmentanddrop;



import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.*;
import desmoj.extensions.visualization2d.animation.CmdGeneration;


public class BurstGenerator extends ExternalEvent {
	
	/** model reference */
	private ModelBurstExchange 	model;
	
	
	/** constructor */
	public BurstGenerator(Model owner, CmdGeneration cmdGen) {
		super(owner, "Burst generator", true);
		this.model 	= (ModelBurstExchange) owner;
	}// end constructor
	
	
	/** The eventRoutine() describes what happens when this event occurs
	 *  in a model. The eventRoutine() method is very important, because
	 * this is where the real action happens and the changes in a system take place. */
	public void eventRoutine() {
		Burst newBurst = new Burst(model);
		newBurst.activateAfter(this);
		
		schedule(new TimeSpan(model.getDistBurstInterArrivalTime(), TimeUnit.MINUTES)); 
	}// end eventRoutine() 
	   
}// class
