package model.SegmentDeflectAndDrop;


import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.TimeUnit;



import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.ui.RefineryUtilities;

import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import desmoj.extensions.visualization2d.animation.BackgroundElementAnimation;
import desmoj.extensions.visualization2d.animation.Form;
import desmoj.extensions.visualization2d.animation.Position;
import desmoj.extensions.visualization2d.animation.core.simulator.ModelAnimation;
import desmoj.extensions.visualization2d.animation.core.simulator.SimProcessAnimation;
import desmoj.extensions.visualization2d.animation.internalTools.EntityTypeAnimation;
import desmoj.extensions.visualization2d.animation.internalTools.SimulationException;
import desmoj.extensions.visualization2d.engine.model.BackgroundElement;





public class Burst extends SimProcessAnimation {
	
	static EntityTypeAnimation entityType;

	static int p=1;
	static int q=1;
	static long  drop=0;
	static long succ=0;
			static{
				entityType = new EntityTypeAnimation();
				entityType.setId(Burst.class.getSimpleName());
				entityType.setGenereratedBy(Burst.class.getName());
				entityType.setIconSize(new Dimension(32,20));
				entityType.addPossibleState("active", "Burst-active");
				entityType.addPossibleState("passive", "Burst-passive");
				entityType.addPossibleState("fail", "Burst-dropped");
				entityType.addPossibleState("processing", "Burst-processing");
				entityType.addPossibleState("done", "Burst-done");
				entityType.addPossibleState("1", "one");
				entityType.addPossibleState("2", "two");
				entityType.addPossibleState("3", "three");
					}

	/** model reference */
  	private ModelBurstExchange model;
	/** is burst for import (== true) or for export (== false) */
	protected boolean isIncomingBurst;
	/** number of the Node, this Burst is destined to */
	protected int nodeBlock; 
	
boolean unsuccesful = false;
  	
  	long length;
  	
  	long start;
  	long count=0;
    long stop;
	long alterLength;
    long actual;
    boolean issegmented=false;
    boolean completed;
	/** constructor */
	public Burst(ModelAnimation owner) {
										super(owner, "burst", owner.traceIsOn());
										super.createAnimation(entityType.getId(), owner.animationIsOn());
										model = (ModelBurstExchange) owner;
										isIncomingBurst = model.getDistIsIncomingBurst();
										nodeBlock = (int) model.getDistDestination();
										length=model.getBurstLength();
										}// end constructor
	
	
	/** The lifeCycle() methods are one of the most import methods
	 * within DESMO-J-based simulations. This is where the real action happens. */
	public void lifeCycle() 
	{
				model = (ModelBurstExchange)getModel();
			
				this.setState("active");
				TimeSpan arrive = new TimeSpan(0.1, TimeUnit.MINUTES);
				TimeSpan arrive2 = new TimeSpan(0.1, TimeUnit.MINUTES);
				TimeSpan routeduration = new TimeSpan(0.5, TimeUnit.MINUTES);
				TimeSpan routedurationdeflection = new TimeSpan(6, TimeUnit.MINUTES);
				TimeSpan routedurationdrop = new TimeSpan(1, TimeUnit.MINUTES);
			
	
				
				try
				{
					if (nodeBlock==0)
						
						model.arrivalRoute.insert(this,new TimeSpan(0.7,TimeUnit.MINUTES));
					else
					if (nodeBlock==1)
						
						model.arrivalRoute2.insert(this,new TimeSpan(0.7,TimeUnit.MINUTES));
					else
					if (nodeBlock==2)
						
						model.arrivalRoute3.insert(this,new TimeSpan(0.7,TimeUnit.MINUTES));
					
				}
				catch (SimulationException e)
				{
					e.printStackTrace();
				}
				this.hold(new TimeSpan(0.7, TimeUnit.MINUTES));
				model.getCount().update(1);
				model.arrivalRoute.remove(this);
				model.arrivalRoute2.remove(this);
				model.arrivalRoute3.remove(this);
			
				
			if (nodeBlock==0)
						{
						
							
					
				if (!model.getBurstQueue().insert(this))
					
							{
					if (!model.getIdleWLQueue(nodeBlock).isEmpty())
					{ 
							
					WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
					wl.setState("passive");
					model.getIdleWLQueue(nodeBlock).remove(wl);
					sendTraceNote("burst withdraws WL from idle WLQueue," +
					" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
		
					wl.activateAfter(this);
			
				
		
					}
					model.getBurstQueue().remove(this);
			
					model.bgElement10.setBackground(Color.red);
					model.bgElement10.setTextColor(Color.white);
					model.bgElement10.setText("!!CONTENTION!!  "+"     Rescheduling    "+this.getName());
					this.setState("fail");
					
					
							model.getdefQueue2().insert(this);
							this.hold(new TimeSpan(9,TimeUnit.MINUTES));
							this.issegmented=true;
							if(!model.getActionWLQueue(nodeBlock).isEmpty())
							{
								
								WaveLength wl = (WaveLength) model.getActionWLQueue(nodeBlock).first(); 
								
							
								wl.interrupt(model.segmentBurst);
								
							
								
						
												
					
								
							} 
							else
							{
								//done interupting wavelength.
							}
									
			}
			else //inserting into burstqueue
						{
	
				this.setState("passive");
			//	this.completed=false;
				//this.issegmented=false;
				this.hold(arrive);
						sendTraceNote("Burst was inserted into Burst queue, current Burst queue" +
								" length: " + model.getBurstQueue().length());
					
						if (!model.getIdleWLQueue(nodeBlock).isEmpty())
							{ 
									
							WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
							wl.setState("passive");
							model.getIdleWLQueue(nodeBlock).remove(wl);
							sendTraceNote("burst withdraws WL from idle WLQueue," +
							" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
				
							wl.activateAfter(this);
					
							passivate();
				
							}
						else
							{
							passivate();
							}	
						
						
				 //inserted
				
						}
				
				
		
		
		} //nodeblock==0
			
			
			
			else
		
				
				if (nodeBlock==1)
				{
				
					
			
		if (!model.getBurstQueue2().insert(this))
			
					{
			model.getBurstQueue2().remove(this);
			if (!model.getIdleWLQueue(nodeBlock).isEmpty())
			{ 
					
			WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
			wl.setState("passive");
			model.getIdleWLQueue(nodeBlock).remove(wl);
			sendTraceNote("burst withdraws WL from idle WLQueue," +
			" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());

			wl.activateAfter(this);
	
		

			}
			model.bgElement10.setBackground(Color.red);
			model.bgElement10.setTextColor(Color.white);
			model.bgElement10.setText("!!CONTENTION!!  "+"     Rescheduling    "+this.getName());
			this.setState("fail");
			
			
					model.getdefQueue().insert(this);
					this.hold(new TimeSpan(9,TimeUnit.MINUTES));
					this.issegmented=true;
					if(!model.getActionWLQueue(nodeBlock).isEmpty())
					{
						
						WaveLength wl = (WaveLength) model.getActionWLQueue(nodeBlock).first(); 
						
					
						wl.interrupt(model.segmentBurst);
						
					
						
				
										
			
						
					} 
					else
					{
						//done interupting wavelength.
					}
							
	}
	else //inserting into burstqueue
				{

		this.setState("passive");
	//	this.completed=false;
		//this.issegmented=false;
		this.hold(arrive);
				sendTraceNote("Burst was inserted into Burst queue, current Burst queue" +
						" length: " + model.getBurstQueue2().length());
			
				if (!model.getIdleWLQueue(nodeBlock).isEmpty())
					{ 
							
					WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
					wl.setState("passive");
					model.getIdleWLQueue(nodeBlock).remove(wl);
					sendTraceNote("burst withdraws WL from idle WLQueue," +
					" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
		
					wl.activateAfter(this);
			
					passivate();
		
					}
				else
					{
					passivate();
					}	
				
				
		 //inserted
		
				}
		
		


} //nodeblock==1
			
			
				else
					
					
					
					{
					
						
				
			if (!model.getBurstQueue3().insert(this))
				
						{
				model.getBurstQueue3().remove(this);
				if (!model.getIdleWLQueue(nodeBlock).isEmpty())
				{ 
						
				WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
				wl.setState("passive");
				model.getIdleWLQueue(nodeBlock).remove(wl);
				sendTraceNote("burst withdraws WL from idle WLQueue," +
				" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
	
				wl.activateAfter(this);
		
			
	
				}
				model.bgElement10.setBackground(Color.red);
				model.bgElement10.setTextColor(Color.white);
				model.bgElement10.setText("!!CONTENTION!!  "+"     Rescheduling    "+this.getName());
				this.setState("fail");
				
				
						model.getdefQueue3().insert(this);
						this.hold(new TimeSpan(9,TimeUnit.MINUTES));
						this.issegmented=true;
						if(!model.getActionWLQueue(nodeBlock).isEmpty())
						{
							
							WaveLength wl = (WaveLength) model.getActionWLQueue(nodeBlock).first(); 
							
						
							wl.interrupt(model.segmentBurst);
							
						
							
					
											
				
							
						} 
						else
						{
							//done interupting wavelength.
						}
								
		}
		else //inserting into burstqueue
					{

			this.setState("passive");
		//	this.completed=false;
			//this.issegmented=false;
			this.hold(arrive);
					sendTraceNote("Burst was inserted into Burst queue, current Burst queue" +
							" length: " + model.getBurstQueue3().length());
				
					if (!model.getIdleWLQueue(nodeBlock).isEmpty())
						{ 
								
						WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
						wl.setState("passive");
						model.getIdleWLQueue(nodeBlock).remove(wl);
						sendTraceNote("burst withdraws WL from idle WLQueue," +
						" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
			
						wl.activateAfter(this);
				
						passivate();
			
						}
					else
						{
						passivate();
						}	
					
					
			 //inserted
			
					}
			
			


	} //nodeblock==2
		
			
			
			
			
			
			while(this.issegmented	)
			{
				
				
			if(nodeBlock==0)
			{
		
				this.hold(arrive2);
				model.getdefQueue2().remove(this);
	
			if(!model.getBurstQueue().insert(this))
			{if(model.getdefQueue2().length()>10)
			{model.getpacketsDroppedAfterSeg().update(15);
			model.getdroppedafterdef().insert(this);
			model.abc.setText(model.getpacketsDroppedAfterSeg().getValue()+"");
				this.passivate();
			}
			else{
				this.setState("fail");
			model.getdefQueue2().insert(this);
			//this.hold(arrive2);
			this.issegmented=true;
			}
	
			}
			else
				{
				
				model.bgElement10.setText(this.getName()+"     ReEnters    "+(nodeBlock+1));
				this.setState("passive");
				this.issegmented=false;
				passivate();
				}
		
			}//0
			else
				if(nodeBlock==1)
				{
			
					this.hold(arrive2);
					model.getdefQueue().remove(this);
		
				if(!model.getBurstQueue2().insert(this))
				{	if(model.getdefQueue().length()>10)
				{
					model.getpacketsDroppedAfterSeg().update(15);
					model.getdroppedafterdef().insert(this);
					model.abc.setText(model.getpacketsDroppedAfterSeg().getValue()+"");
				this.passivate();
				}
				else{
					this.setState("fail");
				model.getdefQueue().insert(this);
				//this.hold(arrive2);
				this.issegmented=true;
				}
				}
				else
					{
					
					model.bgElement10.setText(this.getName()+"     ReEnters    "+(nodeBlock+1));
					this.setState("passive");
					this.issegmented=false;
					passivate();
					}
			
				}//1
			
				else
					if(nodeBlock==2)
					{
				
						this.hold(arrive2);
						model.getdefQueue3().remove(this);
			
					if(!model.getBurstQueue3().insert(this))
					{
						
							if(model.getdefQueue3().length()>10)
						{model.getpacketsDroppedAfterSeg().update(15);
						model.getdroppedafterdef().insert(this);
						model.abc.setText(model.getpacketsDroppedAfterSeg().getValue()+"");
							this.passivate();
							
						}
						else{
							this.setState("fail");
						model.getdefQueue3().insert(this);
						//this.hold(arrive2);
						this.issegmented=true;
						}
						
						
					
			
					}
					else
						{
						model.bgElement10.setText(this.getName()+"     ReEnters    "+(nodeBlock+1));
						
						this.setState("passive");
						this.issegmented=false;
						passivate();
						}
				
					}//1
			
			
			
			
			
			}
		
			
			while(true)
			{
				
				if(this.completed==true)
				{
			this.setState("done");
					
			if(this.nodeBlock==0)
			{
			try
			{
				model.departureRoute.insert(this,new TimeSpan(0.7, TimeUnit.MINUTES));
			}
			catch (SimulationException e)
			{
				e.printStackTrace();
			}
			}
			
			else
				
		if(this.nodeBlock==1)
		{
		try
		{
			model.departureRoute2.insert(this,new TimeSpan(0.7, TimeUnit.MINUTES));
		}
		catch (SimulationException e)
		{
			e.printStackTrace();
		}
		}
		else
			if(this.nodeBlock==2)
			{
			try
			{
				model.departureRoute3.insert(this,new TimeSpan(0.7, TimeUnit.MINUTES));
			}
			catch (SimulationException e)
			{
				e.printStackTrace();
			}
			}
			
			this.hold(new TimeSpan(0.7, TimeUnit.MINUTES));

			model.departureRoute.remove(this);
			model.departureRoute2.remove(this);
			model.departureRoute3.remove(this);
			model.ouputQueue.insert(this);
			model.getCountSuccesful().update(1);
			model.getPacketsProcessed().update(this.length);	
			passivate();
				}
				else
					if(this.completed==false)
				{
						
						
						if(this.nodeBlock==0)
						{
					try{
						model.deflectionRoute.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
						}

						catch (SimulationException e)
							{
							e.printStackTrace();
							}
						this.hold(new TimeSpan(	0.5, TimeUnit.MINUTES));
						model.bgElement11.setText(this.getName()+"     Deflecting   ");
						model.deflectionRoute.remove(this);
						
						model.getsegQueue().insert(this);
						this.hold(new TimeSpan(5, TimeUnit.MINUTES));
						model.getsegQueue().remove(this);
						
						try{
							model.deflectionRoute2.insert(this);
							}
			
							catch (SimulationException e)
								{
								e.printStackTrace();
								}
							this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));

							model.deflectionRoute2.remove(this);
						
							model.getsegQueue3().insert(this);
							this.hold(new TimeSpan(4, TimeUnit.MINUTES));
							model.getsegQueue3().remove(this);
							try{
								model.deflectionRoute3.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
								}
				
								catch (SimulationException e)
									{
									e.printStackTrace();
									}
								this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));

								model.deflectionRoute3.remove(this);
								
							
								if(!model.getBurstQueue().insert(this))
								{this.setState("fail");
									try{
										model.departureUnsuccesfulRoute.insert(this,new TimeSpan(0.5 ,TimeUnit.MINUTES));
									}

									catch (SimulationException e)
										{
										e.printStackTrace();
										}
									this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));
									model.departureUnsuccesfulRoute.remove(this);
									model.getpacketsDroppedAfterSeg().update(this.length);
									model.abc.setText(model.getpacketsDroppedAfterSeg().getValue()+"");
									model.getPacketsDropped().update(15);
									model.getCountDropped().update(1);
										model.outputUnsuccesfulQueue.insert(this);
										this.passivate();
										
									
								}
								else
									
								{
									model.bgElement11.setText(this.getName()+"     Re-enters After Deflection   ");
								
								this.passivate();
								
							
								}
						}
						
						
						else
							if(this.nodeBlock==1)
							{
						try{
							model.deflectionRoute2.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
							}

							catch (SimulationException e)
								{
								e.printStackTrace();
								}
							this.hold(new TimeSpan(	0.5, TimeUnit.MINUTES));
							model.bgElement11.setText(this.getName()+"     Deflecting   ");
							model.deflectionRoute2.remove(this);
							
							model.getsegQueue3().insert(this);
							this.hold(new TimeSpan(4, TimeUnit.MINUTES));
							model.getsegQueue3().remove(this);
							
							try{
								model.deflectionRoute3.insert(this);
								}
				
								catch (SimulationException e)
									{
									e.printStackTrace();
									}
								this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));

								model.deflectionRoute3.remove(this);
							
								model.getsegQueue2().insert(this);
								this.hold(new TimeSpan(4, TimeUnit.MINUTES));
								model.getsegQueue2().remove(this);
								try{
									model.deflectionRoute.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
									}
					
									catch (SimulationException e)
										{
										e.printStackTrace();
										}
									this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));

									model.deflectionRoute.remove(this);
									
								
									if(!model.getBurstQueue2().insert(this))
									{this.setState("fail");
										try{
											model.departureUnsuccesfulRoute2.insert(this,new TimeSpan(0.5 ,TimeUnit.MINUTES));
										}

										catch (SimulationException e)
											{
											e.printStackTrace();
											}
										this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));
										model.departureUnsuccesfulRoute2.remove(this);
										model.getpacketsDroppedAfterSeg().update(this.length);
										model.abc.setText(model.getpacketsDroppedAfterSeg().getValue()+"");
										model.getPacketsDropped().update(15);
										model.getCountDropped().update(1);
											model.outputUnsuccesfulQueue.insert(this);
											this.passivate();
											
										
									}
									else
										
									{
										model.bgElement11.setText(this.getName()+"     Re-enters After Deflection   ");
									
									this.passivate();
									
								
									}
							}
							else
								if(this.nodeBlock==2)
								{
							try{
								model.deflectionRoute3.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
								}

								catch (SimulationException e)
									{
									e.printStackTrace();
									}
								this.hold(new TimeSpan(	0.5, TimeUnit.MINUTES));
								model.bgElement11.setText(this.getName()+"     Deflecting   ");
								model.deflectionRoute3.remove(this);
								
								model.getsegQueue2().insert(this);
								this.hold(new TimeSpan(4, TimeUnit.MINUTES));
								model.getsegQueue2().remove(this);
								
								try{
									model.deflectionRoute.insert(this);
									}
					
									catch (SimulationException e)
										{
										e.printStackTrace();
										}
									this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));

									model.deflectionRoute.remove(this);
								
									model.getsegQueue().insert(this);
									this.hold(new TimeSpan(4, TimeUnit.MINUTES));
									model.getsegQueue().remove(this);
									try{
										model.deflectionRoute2.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
										}
						
										catch (SimulationException e)
											{
											e.printStackTrace();
											}
										this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));

										model.deflectionRoute2.remove(this);
										
									
										if(!model.getBurstQueue3().insert(this))
										{this.setState("fail");
										model.bgElement11.setText(this.getName()+"     Dropping   ");
											try{
												model.departureUnsuccesfulRoute3.insert(this,new TimeSpan(0.5 ,TimeUnit.MINUTES));
											}

											catch (SimulationException e)
												{
												e.printStackTrace();
												}
											this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));
											model.departureUnsuccesfulRoute3.remove(this);
											model.getpacketsDroppedAfterSeg().update(this.length);
											model.abc.setText(model.getpacketsDroppedAfterSeg().getValue()+"");
											model.getPacketsDropped().update(15);
											model.getCountDropped().update(1);
												model.outputUnsuccesfulQueue.insert(this);
												this.passivate();
												
											
										}
										else
											
										{
											model.bgElement11.setText(this.getName()+"     Re-enters After Deflection   ");
										
										this.passivate();
										
									
										}
								}
						
						
						
				}
				
			}
				
			
				
			}
					
			

				
			
			}
			
				
				
				
		
		
	
	

    /****  ^  ************************************************************************************* 
	/**** /|\ ****///	Hinzugefuegt   ///	   				 ENDE   							  *
	/****  |  *************************************************************************************/
		
// end lifeCycle()

//end class

