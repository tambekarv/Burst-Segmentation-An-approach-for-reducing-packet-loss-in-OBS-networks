package model.dropPolicy;


import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

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
	static long processed;
	static long dropped;
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
					}

	/** model reference */
  	private ModelBurstExchange model;
	/** is burst for import (== true) or for export (== false) */
	protected boolean isIncomingBurst;
	/** number of the Node, this Burst is destined to */
	protected int nodeBlock; 
	
	protected boolean unsuccesful = false;
  	
  	long length;
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
				TimeSpan routeduration = new TimeSpan(0.5, TimeUnit.MINUTES);
				TimeSpan routedurationdeflection = new TimeSpan(5, TimeUnit.MINUTES);
				TimeSpan routedurationdrop = new TimeSpan(15, TimeUnit.MINUTES);
				
			
				try
				{
					if (nodeBlock==0)
						
						model.arrivalRoute.insert(this, routeduration);
					else
					if(nodeBlock==1)
						model.arrivalRoute2.insert(this,routeduration);
					else
						if(nodeBlock==2)
							model.arrivalRoute3.insert(this,routeduration);
				}
				catch (SimulationException e)
				{
					e.printStackTrace();
				}
				this.hold(routeduration);
				model.getCount().update(1);
				model.arrivalRoute.remove(this);
				model.arrivalRoute2.remove(this);
				model.arrivalRoute3.remove(this);
			if (nodeBlock==0)
						{
						
							
					
				if (!model.getBurstQueue().insert(this))
					
							{
					this.setState("fail");
					model.bgElement10.setBackground(Color.red);
					model.bgElement10.setTextColor(Color.white);
					model.bgElement10.setText("!!CONTENTION!!"+"Cannot process  "+this.getName()+"<br>"+"Dropping    "+this.getName()+"  of length  "+this.length);
					//model.text[model.j].setText(q+"->" +"Last Dropped Burst ="+this.getName()+"  "+"No Of Packets Dropped="+this.length+"at time:"+model.presentTime()+"<br>");
					//model.j++;
					q++;
							unsuccesful = true;
							model.getBurstQueue().remove(this);
					
								try{
									model.departureUnsuccesfulRoute.insert(this);
									}
					
									catch (SimulationException e)
										{
										e.printStackTrace();
										}
									this.hold(routeduration);

									model.departureUnsuccesfulRoute.remove(this);
									model.outputUnsuccesfulQueue.insert(this);
									model.getPacketsDropped().update(this.length);
									dropped=(dropped+this.length);
									//model.totaldropped.setText("dropped"+dropped);
									model.getCountDropped().update(1);
									model.bgElement10.setText("");
									model.bgElement10.setBackground(null);
									
									
				if (!model.getIdleWLQueue(nodeBlock).isEmpty())
						{ 
									
						WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
						model.getIdleWLQueue(nodeBlock).remove(wl);
						sendTraceNote("burst withdraws WL from idle WLQueue," +
						" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
					//model.getRMGInActionQueue().insert(rmg);
						wl.activateAfter(this);
				
						passivate();
					//model.getRMGInActionQueue().remove(rmg);
						}// end if
				else
					{
					passivate();
					}
			}
			else
						{
				
				//model.abc.setTextColor(Color.yellow);
				//model.abc.setText("Input :"+this.getName());
				this.hold(arrive);
				
	
				//model.getBurstQueue2().insert(this);
						sendTraceNote("Burst was inserted into Burst queue, current Burst queue" +
								" length: " + model.getBurstQueue().length());
				// is RMG available
						if (!model.getIdleWLQueue(nodeBlock).isEmpty())
							{ 
									
							WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
							model.getIdleWLQueue(nodeBlock).remove(wl);
							sendTraceNote("burst withdraws WL from idle WLQueue," +
							" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
					//model.getRMGInActionQueue().insert(rmg);
							wl.activateAfter(this);
					
							passivate();
					//model.getRMGInActionQueue().remove(rmg);
							}// end if
						else
							{
							passivate();
							}	
			
						}// end else
		}
		
		else
		{
			if (nodeBlock==1)
					{
					
									
					if (!model.getBurstQueue2().insert(this))
					
						{
						model.bgElement10.setBackground(Color.red);
						model.bgElement10.setTextColor(Color.white);
						model.bgElement10.setText("!!CONTENTION!!"+"Cannot process  "+this.getName()+"<br>"+"Dropping    "+this.getName()+"  of length  "+this.length);
						this.setState("fail");
						//model.text2[model.k].setText(p+"->" +"Last Dropped Burst ="+this.getName()+"  "+"No Of Packets Dropped="+this.length+"at time:"+model.presentTime()+"<br>");
					//	model.k++;
						p++;
						unsuccesful = true;
						model.getBurstQueue2().remove(this);
					
							try {
								model.departureUnsuccesfulRoute2.insert(this);
								}
					
							catch (SimulationException e)
								{
								e.printStackTrace();
								}
							this.hold(routeduration);

							model.departureUnsuccesfulRoute2.remove(this);
							model.outputUnsuccesfulQueue.insert(this);
							model.getCountDropped().update(1);
							model.getPacketsDropped().update(this.length);
							dropped=(dropped+this.length);
						//	model.totaldropped.setText("dropped"+dropped);
							model.bgElement10.setText("");
							model.bgElement10.setBackground(null);
				if (!model.getIdleWLQueue(nodeBlock).isEmpty())
					{ 
									
					WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
					model.getIdleWLQueue(nodeBlock).remove(wl);
					sendTraceNote("burst withdraws WL from idle WLQueue," +
						" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
					//model.getRMGInActionQueue().insert(rmg);
					wl.activateAfter(this);
				
					passivate();
					//model.getRMGInActionQueue().remove(rmg);
					}// end if
					else
					{
					passivate();
					}
			
						}
					else
					{
						//model.bgElement7.setTextColor(Color.yellow);
						//model.bgElement7.setText("Input :"+this.getName());
						this.hold(arrive);
				//model.getBurstQueue2().insert(this);
				sendTraceNote("Burst was inserted into Burst queue, current Burst queue" +
					" length: " + model.getBurstQueue2().length());
				// is RMG available
						if (!model.getIdleWLQueue(nodeBlock).isEmpty())
							{ 
									
							WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
							model.getIdleWLQueue(nodeBlock).remove(wl);
							sendTraceNote("burst withdraws WL from idle WLQueue," +
							" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
					//model.getRMGInActionQueue().insert(rmg);
							wl.activateAfter(this);
				
							passivate();
					//model.getRMGInActionQueue().remove(rmg);
							}// end if
						else
						{
					passivate();
						}	
					}
					sendTraceNote("truck leaves the system.");
					}
		/****  |  ************************************************************************************* 
		/**** \|/ ****///	Hinzugefuegt   /// 	   				 ANFANG 							  *
		/****  v  *************************************************************************************/
	
			
			
			else
			{
				if (nodeBlock==2)
						{
						
										
						if (!model.getBurstQueue3().insert(this))
						
							{
							model.bgElement10.setBackground(Color.red);
							model.bgElement10.setTextColor(Color.white);
							model.bgElement10.setText("!!CONTENTION!!"+"Cannot process  "+this.getName()+"<br>"+"Dropping    "+this.getName()+"  of length  "+this.length);
							this.setState("fail");
						//	model.text2[model.k].setText(p+"->" +"Last Dropped Burst ="+this.getName()+"  "+"No Of Packets Dropped="+this.length+"at time:"+model.presentTime()+"<br>");
						//	model.k++;
							p++;
							unsuccesful = true;
							model.getBurstQueue3().remove(this);
						
								try {
									model.departureUnsuccesfulRoute3.insert(this);
									}
						
								catch (SimulationException e)
									{
									e.printStackTrace();
									}
								this.hold(routeduration);

								model.departureUnsuccesfulRoute3.remove(this);
								model.outputUnsuccesfulQueue.insert(this);
								model.getCountDropped().update(1);
								model.getPacketsDropped().update(this.length);
								dropped=(dropped+this.length);
								//model.totaldropped.setText("dropped"+dropped);
								model.bgElement10.setText("");
								model.bgElement10.setBackground(null);
					if (!model.getIdleWLQueue(nodeBlock).isEmpty())
						{ 
										
						WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
						model.getIdleWLQueue(nodeBlock).remove(wl);
						sendTraceNote("burst withdraws WL from idle WLQueue," +
							" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
						//model.getRMGInActionQueue().insert(rmg);
						wl.activateAfter(this);
					
						passivate();
						//model.getRMGInActionQueue().remove(rmg);
						}// end if
						else
						{
						passivate();
						}
				
							}
						else
						{
							//model.bgElement7.setTextColor(Color.yellow);
							//model.bgElement7.setText("Input :"+this.getName());
							this.hold(arrive);
					//model.getBurstQueue2().insert(this);
					sendTraceNote("Burst was inserted into Burst queue, current Burst queue" +
						" length: " + model.getBurstQueue2().length());
					// is RMG available
							if (!model.getIdleWLQueue(nodeBlock).isEmpty())
								{ 
										
								WaveLength wl = (WaveLength) model.getIdleWLQueue(nodeBlock).first(); 
								model.getIdleWLQueue(nodeBlock).remove(wl);
								sendTraceNote("burst withdraws WL from idle WLQueue," +
								" idle WLQueue length: " + model.getIdleWLQueue(nodeBlock).length());
						//model.getRMGInActionQueue().insert(rmg);
								wl.activateAfter(this);
					
								passivate();
						//model.getRMGInActionQueue().remove(rmg);
								}// end if
							else
							{
						passivate();
							}	
						}
						sendTraceNote("truck leaves the system.");
						}
			
			
			
			}
			
			
		}	
	
		if (unsuccesful)
		{
			
			
			//model.getCountDropped().update(1);
			//model.outputUnsuccesfulQueue.insert(this);
		}
		else
		{	this.setState("done");
				if(nodeBlock==0)
				{
					
						try
						{
							model.departureRoute.insert(this);
						}
						catch (SimulationException e)
						{
							e.printStackTrace();
						}
						this.hold(routeduration);
					
					//	model.output.setTextColor(Color.cyan);
					//	model.output.setText("Output :"+this.getName());
						model.departureRoute.remove(this);
						model.ouputQueue.insert(this);
						model.getCountSuccesful().update(1);
						model.getPacketsProcessed().update(this.length);
						processed=processed+(this.length);
						//model.totalprocessed.setText("processed"+processed);
						
				}		//double realUniform = model.distRealUniform.sample();
				else
					if(nodeBlock==1)
					{
							
							try
							{
								model.departureRoute2.insert(this);
							}
							catch (SimulationException e)
							{
								e.printStackTrace();
							}
							this.hold(routeduration);
						
							//model.bgElement9.setTextColor(Color.cyan);
							//model.bgElement9.setText("Output :"+this.getName());
							model.departureRoute2.remove(this);
							model.ouputQueue.insert(this);
							model.getCountSuccesful().update(1);
							model.getPacketsProcessed().update(this.length);
							processed=processed+(this.length);
							//model.totalprocessed.setText("processed"+processed);
							
					}
					else
						if(nodeBlock==2)
						{
							try
							{
								model.departureRoute3.insert(this);
							}
							catch (SimulationException e)
							{
								e.printStackTrace();
							}
							this.hold(routeduration);
						
							//model.bgElement9.setTextColor(Color.cyan);
							//model.bgElement9.setText("Output :"+this.getName());
							model.departureRoute3.remove(this);
							model.ouputQueue.insert(this);
							model.getCountSuccesful().update(1);
							model.getPacketsProcessed().update(this.length);
							processed=processed+(this.length);
							//model.totalprocessed.setText("processed"+processed);
						}
		}
		

	    /****  ^  ************************************************************************************* 
		/**** /|\ ****///	Hinzugefuegt   ///	   				 ENDE   							  *
		/****  |  *************************************************************************************/
		
	}// end lifeCycle()
	
// end class
}
		