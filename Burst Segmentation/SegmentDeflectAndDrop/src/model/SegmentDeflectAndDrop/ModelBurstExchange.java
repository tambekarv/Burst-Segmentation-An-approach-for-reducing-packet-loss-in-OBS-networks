package model.SegmentDeflectAndDrop;





import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import model.ConfigTool2d;




import java.awt.Font;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import desmoj.core.dist.BoolDistBernoulli;
import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.DiscreteDistConstant;
import desmoj.core.dist.DiscreteDistEmpirical;
import desmoj.core.dist.DiscreteDistPoisson;
import desmoj.core.dist.DiscreteDistUniform;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.InterruptCode;
import desmoj.core.simulator.PatternBasedTimeFormatter;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.QueueBased;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.statistic.Count;
import desmoj.extensions.visualization2d.animation.BackgroundElementAnimation;
import desmoj.extensions.visualization2d.animation.BackgroundLineAnimation;
import desmoj.extensions.visualization2d.animation.CmdGeneration;
import desmoj.extensions.visualization2d.animation.Comment;
import desmoj.extensions.visualization2d.animation.Form;
import desmoj.extensions.visualization2d.animation.FormExt;
import desmoj.extensions.visualization2d.animation.Position;
import desmoj.extensions.visualization2d.animation.core.advancedModellingFeatures.StockAnimation;
import desmoj.extensions.visualization2d.animation.core.simulator.ModelAnimation;
import desmoj.extensions.visualization2d.animation.core.simulator.ProcessQueueAnimation;
import desmoj.extensions.visualization2d.animation.core.statistic.CountAnimation;
import desmoj.extensions.visualization2d.animation.transport.TransportRouteAnimation;
import desmoj.extensions.visualization2d.animation.transport.TransportStationAnimation;
import desmoj.extensions.visualization2d.engine.Constants;
import desmoj.extensions.visualization2d.engine.command.Parameter;
import desmoj.extensions.visualization2d.engine.model.BackgroundElement;

/**
 * This fourth assignment simulates an average day on a container terminal.
 * It thereby focuses on a small part of the whole.
 * 
 * Trucks arrive from outside the terminal in order to deliver
 * a container to or to pick up a container from the container 
 * terminal. The former are called export containers while the
 * latter are called import containers.
 * 
 * This assignmen takes the time span between two truck arrivals
 * into consideration and is a model applying the process-oriented
 * simulation approach.
 * 
 * !!! This fourth assignment simulates three container blocks, each
 * represented using a desmoj.Stock object !!!
 * 
 * @author Alexander Bentz & Michael Rave
 */

public class ModelBurstExchange extends ModelAnimation {

	// CONSTANTS:
	/** capacities of the container blocks */
	protected final int[] CAPACITY_OF_CONTAINER_BLOCK = {4000, 4000, 4000};
	/** mean time for a container storage */
	protected final double CONTAINER_STORAGE_TIME = 10.0;
	/** initial fillings of the container blocks */
	protected final int[] INITIAL_FILLING = {2000, 2000, 2000};
	
	protected final int[] PROD_CAP = {2000, 2000, 2000};
	
	protected final int[] CONS_CAP = {2000, 2000, 2000};
	/** mean number of import containers */
	protected final double MEAN_NUMBER_OF_IMPORTS = 0.5;
	/** mean inter-arrival time of trucks */
	protected final double MEAN_TRUCK_INTER_ARRIVAL_TIME = 1.67;
	/** number of analysed container blocks */
	protected final int NUMBER_OF_CONTAINER_BLOCKS = 3;
	/** maximal length of truck queue (number of lanes) */
	protected final int NUMBER_OF_LANES = 3;
	public boolean segment=false;
	public boolean segment2=false;
Comment com;
public static int j=0;
public static int k=0;
	protected InterruptCode segmentBurst;
	BackgroundElementAnimation abc;
	BackgroundElementAnimation processing;
	// DISTRIBUTIONS:
	/** determines whether a container is an import container loaded
	 * onto a truck or an export container unloaded from a truck */
	private BoolDistBernoulli distIsIncomingBurst;	
	/** determines the time span between two truck arrivals */
	protected DiscreteDistPoisson			distBurstInterArrivalTime;

	/** determines the time span for a container storage */
	private DiscreteDistConstant<Double> distBurstProcessTime;	
	/** determines to which container block a truck is destined to */
	private DiscreteDistEmpirical<Integer> distDestination;
//	protected DiscreteDistPoisson			distBurstInterArrivalTime;
	DefaultPieDataset dataset;
	private DiscreteDistUniform burstLengthStream;
	// STATISTICS:
	/** number of rejected trucks */
	private CountAnimation count;
private Point[] p;
	
	// WAITING QUEUES:
	/** queue array for the RMGs being idle */
	private ProcessQueueAnimation<WaveLength>[] idleWLQueue;
	/** queue object for trucks waiting for container exchange */
	private ProcessQueueAnimation<Burst> burstQueue;
	/** queue object for trucks waiting for container exchange */
	private ProcessQueueAnimation<Burst> burstQueue2;

	private ProcessQueueAnimation<Burst> burstQueue3;
	private ProcessQueueAnimation<Burst> segQueue;
	private ProcessQueueAnimation<Burst> segQueue2;
	private ProcessQueueAnimation<Burst> segQueue3;
	private ProcessQueueAnimation<Burst> defQueue;
	private ProcessQueueAnimation<Burst> defQueue2;
	private ProcessQueueAnimation<Burst> defQueue3;

	// STOCK:
	/** the stock array representing the container blocks */
	private StockAnimation[] nodeBlock;

	// BackgroundElements
	private BackgroundElementAnimation bgElement;
	private BackgroundElementAnimation bgElement2;
	private BackgroundElementAnimation bgElement3;
	private BackgroundElementAnimation bgElement4;
	private BackgroundElementAnimation bgElement5;
	private BackgroundElementAnimation bgElement6;
	 BackgroundElementAnimation bgElement7;
	 BackgroundElementAnimation bgElement8;
	BackgroundElementAnimation bgElement9;
	 BackgroundElementAnimation bgElement10;
	 BackgroundElementAnimation bgElement11;
	 BackgroundElementAnimation bgElement12;
	 BackgroundElementAnimation bgElement13;
	 BackgroundElementAnimation bgElement14;
	 BackgroundElementAnimation bgElement16;
	 
	private BackgroundElementAnimation sourcenode;
	private ProcessQueueAnimation<Burst> droppedafterdef;
	//private BackgroundLineAnimation Line;
	/****  |  ************************************************************************************* 
	/**** \|/ ****///	Hinzugefuegt   /// 	   				 ANFANG 							  *
	/****  v  *************************************************************************************/
	
	private CountAnimation countSuccesful;
	private CountAnimation countUnsuccesful;
	private CountAnimation packetsDropped;
	private CountAnimation packetsProcessed;
	private CountAnimation packetsDroppedAfterSeg;
	

	
	//TransportStationen
	TransportStationAnimation arrivalSource;
	TransportStationAnimation arrivalSource2;
	TransportStationAnimation arrivalSource3;
	TransportStationAnimation arrivalSink;
	TransportStationAnimation arrivalSink2;
	TransportStationAnimation arrivalSink3;
	TransportStationAnimation deflectionSource;
	TransportStationAnimation deflectionSource3;
	TransportStationAnimation deflectionDestination;
	TransportStationAnimation deflectionSource2;
	TransportStationAnimation deflectionDestination2;
	TransportStationAnimation deflectionDestination3;
	TransportStationAnimation departureSource;
	TransportStationAnimation departureSink;
	TransportStationAnimation departureSource3;
	TransportStationAnimation departureSink3;
	TransportStationAnimation postdeflectsource;
	TransportStationAnimation postdeflectsink;
	TransportStationAnimation postdeflectsource2;
	TransportStationAnimation postdeflectsink2;
	
	TransportStationAnimation departureSource2;
	TransportStationAnimation departureSink2;
	TransportStationAnimation departureUnsuccesfulSource;
	TransportStationAnimation departureUnsuccesfulSink;
	TransportStationAnimation departureUnsuccesfulSource3;
	TransportStationAnimation departureUnsuccesfulSink3;
	TransportStationAnimation departureUnsuccesfulSource2;
	TransportStationAnimation departureUnsuccesfulSink2;
	
	//TransportRouten
	TransportRouteAnimation<Burst> arrivalRoute;
	TransportRouteAnimation<Burst> arrivalRoute3;
	TransportRouteAnimation<Burst> deflectionRoute;
	TransportRouteAnimation<Burst> deflectionRoute2;
	TransportRouteAnimation<Burst> deflectionRoute3;
	TransportRouteAnimation<Burst> arrivalRoute2;
	TransportRouteAnimation<Burst> departureRoute;
	TransportRouteAnimation<Burst> departureRoute2;
	TransportRouteAnimation<Burst> departureRoute3;
	TransportRouteAnimation<Burst> departureUnsuccesfulRoute;
	TransportRouteAnimation<Burst> departureUnsuccesfulRoute2;
	TransportRouteAnimation<Burst> departureUnsuccesfulRoute3;
	TransportRouteAnimation<Burst> postdeflectroute;
	TransportRouteAnimation<Burst> postdeflectroute2;
	//Queues
	ProcessQueueAnimation<Burst> ouputQueue;
	ProcessQueueAnimation<Burst> outputUnsuccesfulQueue;
	
	ProcessQueueAnimation<Burst>[] actionBurstQueue;
	
	ProcessQueueAnimation<WaveLength>[] actionWLQueue;
	
BackgroundElementAnimation[]	text;
BackgroundElementAnimation[]	text2;
	protected BackgroundElementAnimation[]	income;
	protected BackgroundElementAnimation	output;

    /****  ^  ************************************************************************************* 
	/**** /|\ ****///	Hinzugefuegt   ///	   				 ENDE   							  *
	/****  |  *************************************************************************************/
	

	/** standard constructor */
	public ModelBurstExchange(String modelName, CmdGeneration cmdGen, 
			boolean showInReport, boolean showInTrace, boolean showInAnimation) {
		super(null, modelName,	cmdGen, showInReport, showInTrace, showInAnimation);
		this.setModelAuthor("Priyank Dhutia");
		this.setModelDate("25.01.2014");
		this.setModelDescription(this.description());
		this.addIcon("Burst-active", "truck4_active.gif");
		this.addIcon("Burst-passive", "truck4_iddle.gif");
		this.addIcon("Burst-dropped", "rejected.gif");
		this.addIcon("one", "1.gif");
		this.addIcon("two", "2.gif");
		this.addIcon("three", "3.gif");
		this.addIcon("Burst-deflected", "deflected.gif");
		this.addIcon("Burst-processing", "processing.gif");
		this.addIcon("Burst-done", "done.gif");
		this.addIcon("WL-active", "processing.gif");
		this.addIcon("WL-passive", "crane2_iddle.gif");
		this.addEntityTypeAnimation(Burst.entityType);
		this.addEntityTypeAnimation(WaveLength.entityType);
		this.setGeneratedBy(ModelBurstExchange.class.getName());
	}// end constructor
	
	/** initializes model components, required by superclass */
	public void initAnimation() {
		
		abc= new BackgroundElementAnimation(this, "trial1", null, null, 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(840,200), new Form(100, 29), 
				Color.white, Color.blue, this.animationIsOn());
		/*	bgElement7= new BackgroundElementAnimation(this, "trial1", null, "INPUT", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(810,5), new Form(100, 29), 
				Color.white, Color.blue, this.animationIsOn());
		
		processing= new BackgroundElementAnimation(this, "trial1", null, "Processing.", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(-100,5), new Form(140, 29), 
				Color.black, Color.pink, this.animationIsOn());
		
		bgElement8= new BackgroundElementAnimation(this, "trial1", null, "Processing.", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(690,5), new Form(140, 29), 
				Color.black, Color.pink, this.animationIsOn());
		
		output= new BackgroundElementAnimation(this, "trial1", null, "OUTPUT.", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(40,5), new Form(140, 29), 
				Color.orange, Color.gray, this.animationIsOn());
		
		bgElement9= new BackgroundElementAnimation(this, "trial1", null, "OUTPUT.", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(550,5), new Form(140, 29), 
				Color.orange, Color.gray, this.animationIsOn());
		
		*/
		 dataset = new DefaultPieDataset();
		bgElement10= new BackgroundElementAnimation(this, "trial1", null, "", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Big,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(140,140), new Form(840, 25), 
				Color.red, Color.lightGray, this.animationIsOn());

		bgElement11 = new BackgroundElementAnimation(this, "trial1", null, "", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Big,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(140,110), new Form(840, 25), 
				Color.red, Color.lightGray, this.animationIsOn());
		
		bgElement12= new BackgroundElementAnimation(this, "trial1", null, "", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(770,-290), new Form(300, 15), 
				Color.black, Color.yellow, this.animationIsOn());
		
		bgElement13= new BackgroundElementAnimation(this, "trial1", null, "", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(770,-270), new Form(300, 15), 
				Color.black, Color.yellow, this.animationIsOn());
		
		bgElement14= new BackgroundElementAnimation(this, "trial1", null, "", 
				BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
				BackgroundElement.TEXT_Style_Bold, 0, new Position(770,-250), new Form(300, 15), 
				Color.black, Color.yellow, this.animationIsOn());
		
		bgElement16 = new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
				"IntermediateNodes", BackgroundElement.TEXT_POSITION_TopLeft, BackgroundElement.TEXT_Size_Big, 
				BackgroundElement.TEXT_Style_Bold, 1, new Position(140,-140), 
				new Form(840,550), Color.black, Color.pink, this.animationIsOn());
		/*
		text = new BackgroundElementAnimation[100];
		for (int i=0; i< 100; i++)
		{
			text[i] = new BackgroundElementAnimation(this, "trial", null,null , 
					BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
					BackgroundElement.TEXT_Style_Plain, 1.0, new Position(0,(200+i*30)), new Form(550, 20), 
					Color.black, Color.lightGray, this.animationIsOn());
		}
		
		text2 = new BackgroundElementAnimation[100];
		for (int i=0; i< 100; i++)
		{
			text2[i] = new BackgroundElementAnimation(this, "trial", null, null, 
					BackgroundElement.TEXT_POSITION_Middle, BackgroundElement.TEXT_Size_Normal,
					BackgroundElement.TEXT_Style_Plain, 1.0, new Position(700,(200+i*30)), new Form(550, 20), 
					Color.black, Color.orange, this.animationIsOn());
		}

		
		
		*/
		actionBurstQueue = new ProcessQueueAnimation[3];
		
			actionBurstQueue[0] = new ProcessQueueAnimation<Burst>(this, "InProcessBurst" , 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			actionBurstQueue[0].createAnimation(	new Position (-130,-150),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
					this.animationIsOn());
			actionBurstQueue[1] = new ProcessQueueAnimation<Burst>(this, "InProcessBurst" , 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			actionBurstQueue[1].createAnimation(new Position(40,-150),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
					this.animationIsOn());
			
			actionBurstQueue[2] = new ProcessQueueAnimation<Burst>(this, "InProcessBurst" , 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			actionBurstQueue[2].createAnimation(new Position(290,-150),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
					this.animationIsOn());
		
		distBurstProcessTime = new DiscreteDistConstant<Double>(this, "time span for processing burst",
			CONTAINER_STORAGE_TIME, this.reportIsOn(), this.traceIsOn());
		distIsIncomingBurst = new BoolDistBernoulli(this, "incoming or outgoing",
			MEAN_NUMBER_OF_IMPORTS, this.reportIsOn(), this.traceIsOn());
		distBurstInterArrivalTime = new DiscreteDistPoisson(this, "time span between burst arrivals",
			MEAN_TRUCK_INTER_ARRIVAL_TIME, this.reportIsOn(), this.traceIsOn());
		distDestination = new DiscreteDistEmpirical<Integer>(this, "truck to container block assignment",
				this.reportIsOn(), this.traceIsOn());
		distDestination.addEntry(0, 0.3);	// 20% to the first stock
		distDestination.addEntry(1, 0.3);  // 35% to the second stock
		distDestination.addEntry(2, 0.3);	// 45% to the third stock
		burstLengthStream = new DiscreteDistUniform(this, "Burst Length Stream", 15,15, true, false);
		//BackgroundElementAnimation headline = 
		/*new BackgroundElementAnimation(this, "Headline", null,  
				"Deflect And Drop Policy", BackgroundElement.TEXT_POSITION_TopLeft, BackgroundElement.TEXT_Size_Big, 
				BackgroundElement.TEXT_Style_Bold, 2, new Position(300,-420), 
				null, Color.black, Color.orange, this.animationIsOn());
		*/
		burstQueue = new ProcessQueueAnimation<Burst>(this, "ArrivedBurst", 
				ProcessQueue.FIFO, NUMBER_OF_LANES, this.reportIsOn(), this.traceIsOn());
		burstQueue.createAnimation(	new Position (-210,-150),
				new FormExt(false, 2, Burst.entityType.getId(),-25,10),
				this.animationIsOn());
		
		burstQueue2 = new ProcessQueueAnimation<Burst>(this, "ArrivedBurst", 
				ProcessQueue.FIFO, NUMBER_OF_LANES, this.reportIsOn(), this.traceIsOn());
		burstQueue2.createAnimation(	new Position(120,-150),
				new FormExt(false, 2, Burst.entityType.getId(),-25,10),
			this.animationIsOn());
		
		burstQueue3 = new ProcessQueueAnimation<Burst>(this, "ArrivedBurst", 
				ProcessQueue.FIFO, NUMBER_OF_LANES, this.reportIsOn(), this.traceIsOn());
		burstQueue3.createAnimation(	new Position(370,-150),
				new FormExt(false, 2, Burst.entityType.getId(),-25,10),
			this.animationIsOn());
		
		segQueue = new ProcessQueueAnimation<Burst>(this, "Deflected Bursts", 
				ProcessQueue.FIFO, 500, this.reportIsOn(), this.traceIsOn());
		segQueue .createAnimation(	new Position(80,-49),
				new FormExt(true, 3, Burst.entityType.getId(),34,-20),
			this.animationIsOn());
		
		
		segQueue2 = new ProcessQueueAnimation<Burst>(this, "Deflected Bursts", 
				ProcessQueue.FIFO, 500, this.reportIsOn(), this.traceIsOn());
		segQueue2 .createAnimation(	new Position(-170,-49),
				new FormExt(true, 3, Burst.entityType.getId(),34,-20),
			this.animationIsOn());
		segQueue3 = new ProcessQueueAnimation<Burst>(this, "Deflected Bursts", 
				ProcessQueue.FIFO, 500, this.reportIsOn(), this.traceIsOn());
		segQueue3 .createAnimation(	new Position(330,-49),
				new FormExt(true, 3, Burst.entityType.getId(),34,-20),
			this.animationIsOn());
		
		
		
		
		
		
		
		
		defQueue = new ProcessQueueAnimation<Burst>(this, "Rescheduled Bursts", 
				ProcessQueue.FIFO, 500, this.reportIsOn(), this.traceIsOn());
		defQueue .createAnimation(	new Position(80,20),
				new FormExt(true, 3, Burst.entityType.getId(),34,-20),
			this.animationIsOn());
		
		
		defQueue2 = new ProcessQueueAnimation<Burst>(this, "Rescheduled Bursts", 
				ProcessQueue.FIFO, 500, this.reportIsOn(), this.traceIsOn());
		defQueue2 .createAnimation(	new Position(-170,20),
				new FormExt(true, 3, Burst.entityType.getId(),34,-20),
			this.animationIsOn());
		defQueue3 = new ProcessQueueAnimation<Burst>(this, "Rescheduled Bursts", 
				ProcessQueue.FIFO, 500, this.reportIsOn(), this.traceIsOn());
		defQueue3 .createAnimation(	new Position(330,20),
				new FormExt(true, 3, Burst.entityType.getId(),34,-20),
			this.animationIsOn());
		
	    this.droppedafterdef = new ProcessQueueAnimation<Burst>(this, "DroppedDueToConten", this.reportIsOn(), this.traceIsOn());
	    this.droppedafterdef.createAnimation(new Position(420,200),
				   new FormExt(true, 1, Burst.entityType.getId(),90,-10),
				   this.animationIsOn());
		
		idleWLQueue = new ProcessQueueAnimation[3];
	
		
			idleWLQueue[0] = new ProcessQueueAnimation<WaveLength>(this, "IdleWaveLength", 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			idleWLQueue[0].createAnimation(new Position(-210,-290),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
				this.animationIsOn());
			
			idleWLQueue[1] = new ProcessQueueAnimation<WaveLength>(this, "IdleWaveLength", 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			idleWLQueue[1].createAnimation(new Position(120,-290),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
				this.animationIsOn());
			
			idleWLQueue[2] = new ProcessQueueAnimation<WaveLength>(this, "IdleWaveLength", 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			idleWLQueue[2].createAnimation(new Position(370,-290),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
				this.animationIsOn());
		
		nodeBlock = new StockAnimation[NUMBER_OF_CONTAINER_BLOCKS];
		for (int i = 0; i < NUMBER_OF_CONTAINER_BLOCKS; i++){
			nodeBlock[i] = new StockAnimation(this, "NodeBlock" + (i),(int)QueueBased.FIFO,(int)PROD_CAP[i],(int)QueueBased.FIFO,(int)CONS_CAP[i],
				(long) INITIAL_FILLING[i], (long) CAPACITY_OF_CONTAINER_BLOCK[i],
				this.reportIsOn(), this.traceIsOn());
			/*nodeBlock[i].createAnimation(new Position(i*700,-80),
					new FormExt(false, 1, WaveLength.entityType.getId()), this.animationIsOn());*/
		}
		
/****  |  ************************************************************************************* 
/**** \|/ ****///	Hinzugefuegt   /// 	   				 ANFANG 							  *
/****  v  *************************************************************************************/
		
		count = new CountAnimation(this, "TotalBursts", this.reportIsOn(), this.traceIsOn());
		count.createAnimation(new Position(870, -85), (new Form(40,2)), this.animationIsOn());
		
		packetsDroppedAfterSeg = new CountAnimation(this, " PackDrop(AfterSeg)", this.reportIsOn(), this.traceIsOn());
		packetsDroppedAfterSeg.createAnimation(new Position(750,-85), (new Form(40,2)), this.animationIsOn());
		
		countSuccesful = new CountAnimation(this, "BurstsProcessed", this.reportIsOn(), this.traceIsOn());
		countSuccesful.createAnimation(new Position(750,200), new Form(), this.animationIsOn());
		
		countUnsuccesful = new CountAnimation(this, "Dropped Bursts", this.reportIsOn(), this.traceIsOn());
		countUnsuccesful.createAnimation(new Position(630,-85), (new Form(40,2)), this.animationIsOn());
		
		packetsDropped = new CountAnimation(this, "PacketsDropped", this.reportIsOn(), this.traceIsOn());
		packetsDropped.createAnimation(new Position(650,200), (new Form(40,2)), this.animationIsOn());
		
		
		packetsProcessed= new CountAnimation(this, "PacketsProcessed", this.reportIsOn(), this.traceIsOn());
		packetsProcessed.createAnimation(new Position(550,200), new Form(), this.animationIsOn());
		
		actionWLQueue = new ProcessQueueAnimation[3];
		
			actionWLQueue[0] = new ProcessQueueAnimation<WaveLength>(this, "ActiveWavelength", 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			actionWLQueue[0].createAnimation(new Position(-130,-290),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
					this.animationIsOn());
			
			actionWLQueue[1] = new ProcessQueueAnimation<WaveLength>(this, "ActiveWavelength", 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			actionWLQueue[1].createAnimation(new Position(40,-290),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
					this.animationIsOn());
	
		
			actionWLQueue[2] = new ProcessQueueAnimation<WaveLength>(this, "ActiveWavelength", 
					ProcessQueue.FIFO, 2, this.reportIsOn(), this.traceIsOn());
			actionWLQueue[2].createAnimation(new Position(290,-290),
					new FormExt(false, 2, Burst.entityType.getId(),-25,10),
					this.animationIsOn());
		
		
		// Transport-Stationen und Routen
		this.arrivalSource			= new TransportStationAnimation(this, "    -- Ingress Node --  ", new Position(800,90),this.animationIsOn());
		this.arrivalSink			= new TransportStationAnimation(this, "  IncomingPort  ", new Position(-210,-180),this.animationIsOn());
		Position[] zp = new Position[2]; zp[0] = new Position(285,90); zp[1]= new Position(-210, 90);
		this.arrivalRoute			= new TransportRouteAnimation<Burst>(this, "Arrival Route", 55.0, 
				
		this.arrivalSource, this.arrivalSink, zp, this.animationIsOn());
		this.arrivalRoute.setAttributes(-500, 3, Color.blue);
		
		
		this.arrivalSource2			= new TransportStationAnimation(this, "  --  Ingress Node --    ", new Position(800,90),this.animationIsOn());
		this.arrivalSink2			= new TransportStationAnimation(this, null, new Position(130,-180),this.animationIsOn());
		Position[] zp1 = new Position[2]; zp1[0] = new Position(130,90); zp1[1]= new Position(130, 90);

		
		this.arrivalRoute2			= new TransportRouteAnimation<Burst>(this, "Arrival Route", 55.0, 
				this.arrivalSource2, this.arrivalSink2, zp1, this.animationIsOn());
		this.arrivalRoute2.setAttributes(-500, 3, Color.blue);
		
		
		this.arrivalSource3		= new TransportStationAnimation(this, "  --  Ingress Node --    ", new Position(800,90),this.animationIsOn());
		this.arrivalSink3			= new TransportStationAnimation(this, null, new Position(350,-180),this.animationIsOn());
		Position[] zp11 = new Position[2]; zp11[0] = new Position(350,90); zp11[1]= new Position(350, 90);

		
		this.arrivalRoute3			= new TransportRouteAnimation<Burst>(this, "Arrival Route", 55.0, 
				this.arrivalSource3, this.arrivalSink3, zp11, this.animationIsOn());
		this.arrivalRoute3.setAttributes(-500, 3, Color.blue);
		
	/*	this.postdeflectsource		= new TransportStationAnimation(this, null, new Position(575,-20),this.animationIsOn());
		this.postdeflectsink			= new TransportStationAnimation(this, null, new Position(290,50),this.animationIsOn());
		Position[] zp2 = new Position[2]; zp2[0] = new Position(575,50); zp2[1]= new Position(290,50);
		
		this.postdeflectroute			= new TransportRouteAnimation<Burst>(this, "", 1.0, 
				this.postdeflectsource, this.postdeflectsink, zp2, this.animationIsOn());
		
		
		
		this.postdeflectsource2		= new TransportStationAnimation(this, "", new Position(-130,-55),this.animationIsOn());
		this.postdeflectsink2			= new TransportStationAnimation(this, "", new Position(290,50),this.animationIsOn());
		Position[] zp21 = new Position[2]; zp21[0] = new Position(-130,50); zp21[1]= new Position(290,50);
		
		this.postdeflectroute2			= new TransportRouteAnimation<Burst>(this, "", 1.0, 
				this.postdeflectsource2, this.postdeflectsink2, zp21, this.animationIsOn());
		
		*/
		
		this.deflectionSource			= new TransportStationAnimation(this, null, new Position(-190,-49),this.animationIsOn());
		
		this.deflectionDestination		= new TransportStationAnimation(this, null, new Position(80,-49),this.animationIsOn());
		zp = new Position[2]; zp[0] = new Position(-190,-49); zp[1]= new Position(90,-49);

		this.deflectionRoute			= new TransportRouteAnimation<Burst>(this, "FromNode1", 1.0, 
		
				this.deflectionSource, this.deflectionDestination, zp, this.animationIsOn());
		this.deflectionRoute.setAttributes(-500, 3, Color.red);
	
		
		this.deflectionSource2			= new TransportStationAnimation(this, null, new Position(60,-49),this.animationIsOn());
		this.deflectionDestination2		= new TransportStationAnimation(this,null, new Position(300,-49),this.animationIsOn());
		zp = new Position[2]; zp[0] = new Position(60,-49); zp[1]= new Position(300,-49);

		this.deflectionRoute2			= new TransportRouteAnimation<Burst>(this, "", 1.0, 
				this.deflectionSource2, this.deflectionDestination2, zp,this.animationIsOn()); 
		this.deflectionRoute2.setAttributes(-500, 3, Color.red);
		
		
		this.deflectionSource3			= new TransportStationAnimation(this, null, new Position(300,-49),this.animationIsOn());
		this.deflectionDestination3		= new TransportStationAnimation(this,null, new Position(-190,-49),this.animationIsOn());
		zp = new Position[3]; zp[0] = new Position(450,-49); zp[1]= new Position(450,80);zp[2]= new Position(-190,80);
		

		this.deflectionRoute3			= new TransportRouteAnimation<Burst>(this, "", 1.0, 
				this.deflectionSource3, this.deflectionDestination3, zp,this.animationIsOn()); 
		this.deflectionRoute3.setAttributes(-500, 3, Color.red);
		
		
		this.departureSource			= new TransportStationAnimation(this, null, new Position(-130,-110),this.animationIsOn());
		this.departureSink			= new TransportStationAnimation(this, "  Destination  ", new Position(700,-330),this.animationIsOn());
		zp = new Position[4]; zp[0] = new Position(-50,-110); zp[1]= new Position(-50,-400);zp[2]= new Position(580,-400);zp[3]= new Position(580,-330);
		
		// Route Abfahrt
		this.departureRoute			= new TransportRouteAnimation<Burst>(this, "> ", 5, 
						this.departureSource, this.departureSink, zp, this.animationIsOn());
			
		this.departureRoute.setAttributes(-500, 3, Color.GREEN);
		
		
		this.departureSource2			= new TransportStationAnimation(this, null, new Position(100,-110),this.animationIsOn());
		this.departureSink2			= new TransportStationAnimation(this,"  Destination  ", new Position(700,-330),this.animationIsOn());
		zp = new Position[4]; zp[0] = new Position(200,-110); zp[1]= new Position(200,-400);zp[2]= new Position(580,-400);zp[3]= new Position(580,-330);
		
		// Route Abfahrt
		this.departureRoute2			= new TransportRouteAnimation<Burst>(this, "<<", 1, 
						this.departureSource2, this.departureSink2, zp, this.animationIsOn());
		this.departureRoute2.setAttributes(-500, 3, Color.GREEN);
	
		
		
		this.departureSource3			= new TransportStationAnimation(this, null, new Position(300,-110),this.animationIsOn());
		this.departureSink3			= new TransportStationAnimation(this,"  Destination  ", new Position(700,-330),this.animationIsOn());
		zp = new Position[4]; zp[0] = new Position(450,-110); zp[1]= new Position(450,-400);zp[2]= new Position(580,-400);zp[3]= new Position(580,-330);
		
		// Route Abfahrt
		this.departureRoute3			= new TransportRouteAnimation<Burst>(this, "<<", 1, 
						this.departureSource3, this.departureSink3, zp, this.animationIsOn());
		this.departureRoute3.setAttributes(-500, 3, Color.GREEN);
		
		
		
		
		//this.abfahrtUnbearbeitetSource	= new TransportStationAnimation(this, "AbfahrtUnbearbeitetSource", new Position(500,420),this.animationIsOn());
		this.departureUnsuccesfulSource	= new TransportStationAnimation(this, null, new Position(-130,-70),this.animationIsOn());
		this.departureUnsuccesfulSink	= new TransportStationAnimation(this, null, new Position(700,-150),this.animationIsOn());
		zp = new Position[4]; zp[0] = new Position(-60,-70); zp[1]= new Position(-60,-410);zp[2]= new Position(530,-410);zp[3]= new Position(530,-150);
		
		this.departureUnsuccesfulRoute	= new TransportRouteAnimation<Burst>(this, ">>", 1.0, 
						this.departureUnsuccesfulSource, this.departureUnsuccesfulSink, zp, this.animationIsOn());
		this.departureUnsuccesfulRoute.setAttributes(-500, 3, Color.magenta);

		
		this.departureUnsuccesfulSource2	= new TransportStationAnimation(this, null, new Position(150,-70),this.animationIsOn());
		this.departureUnsuccesfulSink2	= new TransportStationAnimation(this, "Dropped Burst Sink", new Position(700,-150),this.animationIsOn());
		zp = new Position[4]; zp[0] = new Position(190,-70); zp[1]= new Position(190,-410);zp[2]= new Position(530,-410);zp[3]= new Position(530,-150);
		
		this.departureUnsuccesfulRoute2	= new TransportRouteAnimation<Burst>(this, "<<", 1.0, 
						this.departureUnsuccesfulSource2, this.departureUnsuccesfulSink2, zp, this.animationIsOn());
		this.departureUnsuccesfulRoute2.setAttributes(-500, 3, Color.magenta);
		
		
		
		
		this.departureUnsuccesfulSource3	= new TransportStationAnimation(this, null, new Position(350,-70),this.animationIsOn());
		this.departureUnsuccesfulSink3	= new TransportStationAnimation(this, "Dropped Burst Sink", new Position(700,-150),this.animationIsOn());
		zp = new Position[4]; zp[0] = new Position(430,-70); zp[1]= new Position(430,-410);zp[2]= new Position(530,-410);zp[3]= new Position(530,-150);
		
		this.departureUnsuccesfulRoute3	= new TransportRouteAnimation<Burst>(this, "<<", 1.0, 
						this.departureUnsuccesfulSource3, this.departureUnsuccesfulSink3, zp, this.animationIsOn());
		this.departureUnsuccesfulRoute3.setAttributes(-500, 3, Color.magenta);
		// inActionQueue
		//this.inActionQueue = new ProcessQueueAnimation<Truck>(this, "inActionQueue", this.reportIsOn(), this.traceIsOn());
		//this.inActionQueue.createAnimation(new Position(200, 520),
			//	   new FormExt(false, 3, Truck.entityType.getId()),
			//	   this.animationIsOn());
		
		// RMGinActionQueue
		//this.RMGinActionQueue = new ProcessQueueAnimation<RMG>(this, "RMGinActionQueue", this.reportIsOn(), this.traceIsOn());
		//this.RMGinActionQueue.createAnimation(new Position(100, 520),
		//		   new FormExt(false, 3, Truck.entityType.getId()),
		//		   this.animationIsOn());
		
		
		// Queues als Ausgang
		this.ouputQueue = new ProcessQueueAnimation<Burst>(this, "ProcessedBursts(O/P)", this.reportIsOn(), this.traceIsOn());
		this.ouputQueue.createAnimation(new Position(880,-365),
				   new FormExt(true, 1, Burst.entityType.getId(),90,-10),
				   this.animationIsOn());
		
		this.outputUnsuccesfulQueue = new ProcessQueueAnimation<Burst>(this, "DroppedBursts(O/P)", this.reportIsOn(), this.traceIsOn());
	    this.outputUnsuccesfulQueue.createAnimation(new Position(880,-185),
				   new FormExt(true, 1, Burst.entityType.getId(),90,-10),
				   this.animationIsOn());
	    
	    segmentBurst= new InterruptCode("Segment Burst Arrived") ;

/****  ^  ************************************************************************************* 
/**** /|\ ****///	Hinzugefuegt   ///	   				 ENDE   							  *
/****  |  *************************************************************************************/

	sourcenode = new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
				"BurstAssembly<br>Ingress Node ", BackgroundElement.TEXT_POSITION_TopMiddle, BackgroundElement.TEXT_Size_Big, 
				BackgroundElement.TEXT_Style_Bold, 0, new Position(800,70), 
				new Form(200,85), Color.white, Color.black, this.animationIsOn());
	  
		
		
		bgElement = new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
					"OUTPUT ", BackgroundElement.TEXT_POSITION_TopMiddle, BackgroundElement.TEXT_Size_Big, 
					BackgroundElement.TEXT_Style_Bold, 1, new Position(770,-270), 
					new Form(400,300), Color.black, Color.yellow, this.animationIsOn());
	
		
			bgElement2 = new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
					"INPUT:<BR> Packets Are Assembled into Bursts and inserted into OBS NW ", BackgroundElement.TEXT_POSITION_TopMiddle, BackgroundElement.TEXT_Size_Big, 
					BackgroundElement.TEXT_Style_Bold, 1, new Position(770,52), 
					new Form(400,200), Color.black, Color.magenta, this.animationIsOn());

			bgElement3 = new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
					"NODE1 ", BackgroundElement.TEXT_POSITION_TopMiddle, BackgroundElement.TEXT_Size_Big, 
					BackgroundElement.TEXT_Style_Bold, 0, new Position(-170,-160), 
					new Form(180,450), Color.white, Color.black, this.animationIsOn());
			
			bgElement6 = new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
					"NODE2 ", BackgroundElement.TEXT_POSITION_TopMiddle, BackgroundElement.TEXT_Size_Big, 
					BackgroundElement.TEXT_Style_Bold, 0, new Position(80,-160), 
					new Form(180,450), Color.white, Color.black, this.animationIsOn());
			
			bgElement2= new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
					"NODE3 ", BackgroundElement.TEXT_POSITION_TopMiddle, BackgroundElement.TEXT_Size_Big, 
					BackgroundElement.TEXT_Style_Bold, 0, new Position(330,-160), 
					new Form(180,450), Color.white, Color.black, this.animationIsOn());
	
			
			bgElement4 = new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
					"BurstDis_assembly<br>Egress Node ", BackgroundElement.TEXT_POSITION_TopMiddle, BackgroundElement.TEXT_Size_Big, 
					BackgroundElement.TEXT_Style_Bold, 0, new Position(700,-350), 
					new Form(200,85), Color.white, Color.black, this.animationIsOn());
			
			bgElement5 =  new BackgroundElementAnimation(this, "NODE1-Set of WaveLength", null,  
					"DroppedBursts ", BackgroundElement.TEXT_POSITION_TopMiddle, BackgroundElement.TEXT_Size_Big, 
					BackgroundElement.TEXT_Style_Bold,0, new Position(700,-170), 
					new Form(200,85), Color.white, Color.black, this.animationIsOn());
		//	Line = new BackgroundLineAnimation(this,"line1",2,new Position(-80,-230), 1,Color.BLACK,this.animationIsOn());
	}// end init()


	/** schedules the first events and processes onto the framework's internal event list */
	public void doInitialSchedules() {
		WaveLength wl = null;
		for (int i = 0; i < NUMBER_OF_CONTAINER_BLOCKS; i++) {
			for (int j = 0; j <= 1; j++) {
				wl = new WaveLength(this, i);
				wl.activate(new TimeSpan(0));
			}// end for j
		}// end for i
		// create a truck source
		BurstGenerator firstarrival = new BurstGenerator(this, this.getCmdGen());
		firstarrival.schedule(new TimeSpan(getDistBurstInterArrivalTime(), TimeUnit.MINUTES));   
	}// end doInitialSchedules()


	/** returns a description of this model, required by superclass */
	public String description() {
		String out =
		"Burst Segmentation - an approach for reducing Packet Loss In Optical Burst Switched Networks.(Segment and Drop Policy)";
		return Parameter.replaceSyntaxSign(out);
	}// end description()


	/** runs the experiment */
	public static void main(String[] args) {
		
		String modelName = ConfigTool2d.getModelName(ModelBurstExchange.class.getName());
		ConfigTool2d.checkIconPath(modelName);
		String expName = "firstExp";
		ConfigTool2d.createExperimentDir(modelName, expName);
		   
	   // Initialisierung der CmdGeneration für Animation (neu Hinzugefügt)
		CmdGeneration cmdGen = new CmdGeneration(
				ConfigTool2d.buildCmdsPath(modelName, expName, Constants.FILE_EXTENSION_CMD), 
				ConfigTool2d.buildCmdsPath(modelName, expName, Constants.FILE_EXTENSION_LOG_0), 
				ConfigTool2d.buildIconPathURL(modelName));

		ModelBurstExchange model = new ModelBurstExchange(modelName, cmdGen, true, false, true);
		Experiment experiment = new Experiment(modelName, ConfigTool2d.buildOutputPath(modelName, expName));

		TimeInstant simBegin, simEnd;
	    simBegin = new TimeInstant(new GregorianCalendar(2014, Calendar.JANUARY, 1, 10, 00));
	    simEnd 	 = new TimeInstant(new GregorianCalendar(2014, Calendar.JANUARY, 2, 10, 00));
		cmdGen.setStartStopTime(simBegin, simEnd, TimeZone.getDefault());
		model.connectToExperiment(experiment);

		// setze Experiment-Parameter
		experiment.getDistributionManager().setSeed(6789);
		experiment.setShowProgressBar(true);
		experiment.tracePeriod(new TimeInstant(0), new TimeInstant(simEnd.getTimeAsDouble()));  // set the period of the trace
		experiment.debugPeriod(new TimeInstant(0), new TimeInstant(simEnd.getTimeAsDouble()));   // and debug output

		experiment.setTimeFormatter(new PatternBasedTimeFormatter(true));
		System.out.println("Begin: "+simBegin+"    End: "+simEnd);

		// start the experiment
		cmdGen.experimentStart(experiment, 10.0);
		
		
		// Beende CmdGeneration (neu Hinzugefügt)
		cmdGen.close();
		//
		experiment.report();
		experiment.finish();
		
		
		// Start des AnimationViewers
		ConfigTool2d.createViewer(cmdGen.getCmdFileURL(), cmdGen.getIconPathURL());

	}// end main()

	public ProcessQueueAnimation<Burst> getdroppedafterdef() { 
		return droppedafterdef; 
		}
	public double getDistBurstProcessTime() { 
		return distBurstProcessTime.sample(); 
		}
	
	public boolean getDistIsIncomingBurst() {	
		return distIsIncomingBurst.sample(); 
		}
	
	public double getDistBurstInterArrivalTime() { 
		return distBurstInterArrivalTime.sample(); 
		}
	
	public long getDistDestination() { 
		return distDestination.sample(); 
		}
	
	public Count getCount() { 
		return count;	
		}
	
	public ProcessQueueAnimation<WaveLength> 	getIdleWLQueue(int i) { 
		return idleWLQueue[i]; 
		}
	
	public ProcessQueueAnimation<Burst> getBurstQueue() { 
		return burstQueue; 
		}
	
	public ProcessQueueAnimation<Burst> getBurstQueue2() { 
		return burstQueue2; 
		}
	
	public ProcessQueueAnimation<Burst> getBurstQueue3() { 
		return burstQueue3; 
		}
	public ProcessQueueAnimation<Burst> getsegQueue() { 
		return segQueue; 
		}
	
	
	public ProcessQueueAnimation<Burst> getsegQueue2() { 
		return segQueue2; 
		}
	
	public ProcessQueueAnimation<Burst> getsegQueue3() { 
		return segQueue3; 
		}
	
	
	public ProcessQueueAnimation<Burst> getdefQueue() { 
		return defQueue; 
		}
	
	
	public ProcessQueueAnimation<Burst> getdefQueue2() { 
		return defQueue2; 
		}
	
	public ProcessQueueAnimation<Burst> getdefQueue3() { 
		return defQueue3; 
		}
	
	public StockAnimation getStock(int i) { 
		return nodeBlock[i]; 
		}
	
	/****  |  ************************************************************************************* 
	/**** \|/ ****///	Hinzugefuegt   /// 	   				 ANFANG 							  *
	/****  v  *************************************************************************************/
	
	public ProcessQueueAnimation<Burst> getInActionQueue(int i)
	{ 
		return actionBurstQueue[i]; 
	}
	public ProcessQueueAnimation<WaveLength> getActionWLQueue(int i)
	{ 
		return actionWLQueue[i]; 
	}
	public Count getCountDropped()
	{
		return countUnsuccesful;
	}
	public Count getCountSuccesful()
	{
		return countSuccesful;
	}

	public Count getPacketsDropped()
	{
		return packetsDropped;
	}
	
	public Count getPacketsProcessed()
	{
		return packetsProcessed;
	}
	
	public Count getpacketsDroppedAfterSeg()
	{
		return packetsDroppedAfterSeg;
	}
	protected long getBurstLength() {
	    
		return burstLengthStream.sample();
	}
	
    /****  ^  ************************************************************************************* 
	/**** /|\ ****///	Hinzugefuegt   ///	   				 ENDE   							  *
	/****  |  *************************************************************************************/
	
}
