package model.segmentanddrop;



import desmoj.core.simulator.*;
import desmoj.core.advancedModellingFeatures.Stock;

/**
 * This class represents the container blocks. It is a subclass of
 * desmoj.Stock and extends the latter in that stocks
 * are now able to carry a unique id.
 * 
 * @author Alexander Bentz & Michael Rave
 */

public class MyStock extends Stock {

	/** total number of instances of this class */
	protected static int numberOfStocks = 0;
	/** unique id */
	protected int id;
	
	
	/** constructor for objects with queue capacities and queue sort orders */
	public MyStock (Model owner, String name, int prodSortOrder, int prodQCapacity,
		int consSortOrder, int consQCapacity, long initialUnits, long capacity,
		boolean showInReport, boolean showInTrace) {
			
			super(owner, name, prodSortOrder, prodQCapacity, consSortOrder, consQCapacity,
				initialUnits, capacity, showInReport, showInTrace);
			numberOfStocks++;
			id = numberOfStocks;
	}// end constructor
	
	
	/** constructor for objects with unlimited queue capacities and FIFO-ordered queues */
	public MyStock (Model owner, String name, long initialUnits, long capacity,
		boolean showInReport, boolean showInTrace) {
			
			super(owner, name, initialUnits, capacity, showInReport, showInTrace); 
			numberOfStocks++;
			id = numberOfStocks;
	}// end constructor


	/** returns the id */
	public int getId() { return id; }

}// end class
