package model.deflectanddrop;


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
	
boolean completed= false;
static int q=0;
static int p =0;
  	
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
				model.getCount().update(1);
				this.setState("active");
				TimeSpan arrive = new TimeSpan(0.1, TimeUnit.MINUTES);
			
			
	
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
				
				model.arrivalRoute.remove(this);
				model.arrivalRoute2.remove(this);
				model.arrivalRoute3.remove(this);
				
				
				if(nodeBlock==0)	
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
				model.bgElement10.setText("!!CONTENTION!!  "+"     Deflecting    "+this.getName());
				this.setState("1");
				
				try{
					model.deflectionRoute.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
					}

					catch (SimulationException e)
						{
						e.printStackTrace();
						}
					this.hold(new TimeSpan(	0.5, TimeUnit.MINUTES));
					//model.bgElement11.setText(this.getName()+"     Deflecting   ");
					model.deflectionRoute.remove(this);
					if(model.getsegQueue().length()>5)
					{
						model.getPacketsDropped().update(15);
						model.abc.setText(model.getPacketsDropped().getValue()+"");	
						model.getdroppedafterdef().insert(this);
					this.passivate();
					}
					model.getsegQueue().insert(this);
					this.hold(new TimeSpan(10, TimeUnit.MINUTES));
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
						if(model.getsegQueue3().length()>5)
						{
							model.getPacketsDropped().update(15);
							
							model.abc.setText(model.getPacketsDropped().getValue()+"");	
							model.getdroppedafterdef().insert(this);
						this.passivate();
						}
						model.getsegQueue3().insert(this);
						this.hold(new TimeSpan(10, TimeUnit.MINUTES));
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
							{
								this.completed=false;
							}
							else
							{
								this.passivate();
							}
						
							
							
						}//not inserted.
			
			
			else //inserting into burstqueue
			{

	this.setState("passive");

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
			
			

	
			
	


			}//inserted 0
			
				}//node0
				
				
				else
					if(nodeBlock==1)	
					{
					
				if (!model.getBurstQueue2().insert(this))
					
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
					model.bgElement10.setText("!!CONTENTION!!  "+"     Deflecting    "+this.getName());
					this.setState("2");
					
					try{
						model.deflectionRoute2.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
						}

						catch (SimulationException e)
							{
							e.printStackTrace();
							}
						this.hold(new TimeSpan(	0.5, TimeUnit.MINUTES));
						//model.bgElement11.setText(this.getName()+"     Deflecting   ");
						model.deflectionRoute2.remove(this);
						if(model.getsegQueue3().length()>5)
						{
							model.getPacketsDropped().update(15);
							model.abc.setText(model.getPacketsDropped().getValue()+"");	
							model.getdroppedafterdef().insert(this);
						this.passivate();
						}
						model.getsegQueue3().insert(this);
						this.hold(new TimeSpan(10, TimeUnit.MINUTES));
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
							if(model.getsegQueue2().length()>5)
							{
								model.getPacketsDropped().update(15);
								model.abc.setText(model.getPacketsDropped().getValue()+"");	
								model.getdroppedafterdef().insert(this);
							this.passivate();
							}
							model.getsegQueue2().insert(this);
							this.hold(new TimeSpan(10, TimeUnit.MINUTES));
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
								{
									this.completed=false;
								}
							
								else
								{
									this.passivate();
								}
							
								
							}//not inserted.
				
				
				else //inserting into burstqueue
				{

		this.setState("passive");

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
				
				

		
				
		


				}//inserted 1
				
					}//node 1
				
					else
						if(nodeBlock==2)	
						{
						
					if (!model.getBurstQueue3().insert(this))
						
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
						model.getBurstQueue3().remove(this);
				
						model.bgElement10.setBackground(Color.red);
						model.bgElement10.setTextColor(Color.white);
						model.bgElement10.setText("!!CONTENTION!!  "+"     Deflecting    "+this.getName());
						this.setState("3");
						
						try{
							model.deflectionRoute3.insert(this,new TimeSpan(0.5, TimeUnit.MINUTES));
							}

							catch (SimulationException e)
								{
								e.printStackTrace();
								}
							this.hold(new TimeSpan(	0.5, TimeUnit.MINUTES));
							//model.bgElement11.setText(this.getName()+"     Deflecting   ");
							model.deflectionRoute3.remove(this);
							if(model.getsegQueue2().length()>5)
							{
								model.getPacketsDropped().update(15);
								model.abc.setText(model.getPacketsDropped().getValue()+"");
								model.getdroppedafterdef().insert(this);
							this.passivate();
							}
							model.getsegQueue2().insert(this);
							this.hold(new TimeSpan(10, TimeUnit.MINUTES));
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
								if(model.getsegQueue().length()>5)
								{
									model.getPacketsDropped().update(15);
									model.abc.setText(model.getPacketsDropped().getValue()+"");	
									model.getdroppedafterdef().insert(this);
								this.passivate();
								}
								model.getsegQueue().insert(this);
								this.hold(new TimeSpan(10, TimeUnit.MINUTES));
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
									{
										this.completed=false;
									}
									else
									{
										this.passivate();
									}
								
									
									
								}//not inserted.
					
					
					else //inserting into burstqueue
					{

			this.setState("passive");

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
					
					

			
					
			


					}//inserted 0
					
						}
				
				if(completed==false)
				{
					if(nodeBlock==0)
					{
						this.setState("fail");
						try{
							model.departureUnsuccesfulRoute.insert(this,new TimeSpan(0.5 ,TimeUnit.MINUTES));
						}

						catch (SimulationException e)
							{
							e.printStackTrace();
							}
						this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));
						model.departureUnsuccesfulRoute.remove(this);
						model.getPacketsDropped().update(15);
						model.getCountDropped().update(1);
							model.outputUnsuccesfulQueue.insert(this);
							
							
							
							/*
							model.text[model.k].setText( q +"->" +"Last Dropped Burst ="+this.getName()+"  "+"No Of Packets Dropped="+this.length+"at time:"+model.presentTime()+"<br>");
							model.k++;
							q++;*/
							this.passivate();
					}//node0complete==false
					else
						if(nodeBlock==1)
						{
							this.setState("fail");
							try{
								model.departureUnsuccesfulRoute2.insert(this,new TimeSpan(0.5 ,TimeUnit.MINUTES));
							}

							catch (SimulationException e)
								{
								e.printStackTrace();
								}
							this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));
							model.departureUnsuccesfulRoute2.remove(this);
							model.getPacketsDropped().update(15);
							model.getCountDropped().update(1);
								model.outputUnsuccesfulQueue.insert(this);
					
								this.passivate();
						}//node1complete==false
						else
							if(nodeBlock==2)
							{
								this.setState("fail");
								try{
									model.departureUnsuccesfulRoute3.insert(this,new TimeSpan(0.5 ,TimeUnit.MINUTES));
								}

								catch (SimulationException e)
									{
									e.printStackTrace();
									}
								this.hold(new TimeSpan(0.5, TimeUnit.MINUTES));
								model.departureUnsuccesfulRoute3.remove(this);
								model.getPacketsDropped().update(15);
								model.getCountDropped().update(1);
									model.outputUnsuccesfulQueue.insert(this);
						
									this.passivate();
							}//node2complete==false
					
				}//complete==false
				else
				{
					if(nodeBlock==0)
					{
						try
						{
							model.departureRoute.insert(this,new TimeSpan(0.7, TimeUnit.MINUTES));
						}
						catch (SimulationException e)
						{
							e.printStackTrace();
						}
						this.hold(new TimeSpan(0.7, TimeUnit.MINUTES));
						model.departureRoute.remove(this);
					
						model.ouputQueue.insert(this);
						model.getCountSuccesful().update(1);
						model.getPacketsProcessed().update(15);	
						
						passivate();
					}//node0 complete==true
					
					else
						if(nodeBlock==1)
						{
							try
							{
								model.departureRoute2.insert(this,new TimeSpan(0.7, TimeUnit.MINUTES));
							}
							catch (SimulationException e)
							{
								e.printStackTrace();
							}
							this.hold(new TimeSpan(0.7, TimeUnit.MINUTES));
							model.departureRoute2.remove(this);
						
							model.ouputQueue.insert(this);
							model.getCountSuccesful().update(1);
							model.getPacketsProcessed().update(15);	
							
							passivate();
						}//node1 complete==true
						else
							if(nodeBlock==2)
							{
								try
								{
									model.departureRoute3.insert(this,new TimeSpan(0.7, TimeUnit.MINUTES));
								}
								catch (SimulationException e)
								{
									e.printStackTrace();
								}
								this.hold(new TimeSpan(0.7, TimeUnit.MINUTES));
								model.departureRoute3.remove(this);
							
								model.ouputQueue.insert(this);
								model.getCountSuccesful().update(1);
								model.getPacketsProcessed().update(15);	
								
								passivate();
							}//node2 complete==true
				}//complete==true
				
	}//lifecycle
}//class