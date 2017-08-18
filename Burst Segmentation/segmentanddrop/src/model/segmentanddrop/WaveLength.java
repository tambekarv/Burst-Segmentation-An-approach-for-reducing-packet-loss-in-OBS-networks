package model.segmentanddrop;

import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.*;
import desmoj.extensions.visualization2d.animation.core.simulator.ModelAnimation;
import desmoj.extensions.visualization2d.animation.core.simulator.SimProcessAnimation;
import desmoj.extensions.visualization2d.animation.internalTools.EntityTypeAnimation;
  


public class WaveLength extends SimProcessAnimation {

	static EntityTypeAnimation entityType;

	static{
		entityType = new EntityTypeAnimation();
		entityType.setId(WaveLength.class.getSimpleName());
		entityType.setIconSize(new Dimension(32,20));
		entityType.setGenereratedBy(WaveLength.class.getName());
		entityType.addPossibleState("active", "WL-active");
		entityType.addPossibleState("passive", "WL-passive");
	}

	/** model reference */
	private ModelBurstExchange model;
	/** reference to the Stock object, this WL is working for */
	private int stock;
	
	
	/** constructor */
	public WaveLength(ModelAnimation owner, int stock) {
		super(owner, "WL", owner.traceIsOn());
		super.createAnimation(entityType.getId(), owner.animationIsOn());
		model = (ModelBurstExchange) owner;
		this.stock = stock;
	}// end constructor
	
	
	/** The lifeCycle() methods are one of the most import methods
	 * within DESMO-J-based simulations. This is where the real action happens. */
	public void lifeCycle() {
		while (true) {
			if (stock==0)
			{
				
			
					
				
			
			
					
					
					
					
					/*Burst nextBurst = (Burst) model.getInActionQueue(stock).first();
					model.getInActionQueue(stock).remove(nextBurst);
					nextBurst.interrupt(getInterruptCode());
				
					model.getsegQueue2().insert(nextBurst);
					
					nextBurst.setState("fail");
					nextBurst.activateAfter(this);
					*/
				
					
				
				
					
					
					
				
		
				
			if (model.getBurstQueue().isEmpty()) {   
				
				model.getIdleWLQueue(stock).insert(this);
				this.setState("passive");
				sendTraceNote("idle WL inserts itself into idle WLQueue," +
					" idle WLQueue length: " + model.getIdleWLQueue(stock).length());
				passivate();
			 }// end if
			else { 
			
			
					Burst nextBurst = (Burst) model.getBurstQueue().first();
				model.getBurstQueue().remove(nextBurst);
				
				nextBurst.completed=true;
				//hinzugefügt
				model.getInActionQueue(stock).insert(nextBurst);
				nextBurst.start=(model.getInActionQueue(stock).first().presentTime().getTimeRounded())/100;
				nextBurst.setState("processing");
			//	model.processing.setText("LastProcessed:  "+nextBurst.getName());
				//ende
				
				if (nextBurst.isIncomingBurst) {
					if (model.getStock(stock).getAvail() > 0) {
						//model.getStock(stock).retrieve(this,1);
						model.getStock(stock).retrieve(nextBurst.length);
						model.getActionWLQueue(stock).insert(this);
						this.setState("active");
						hold(new TimeSpan(model.getDistBurstProcessTime(), TimeUnit.MINUTES));
						model.getActionWLQueue(stock).remove(this);
						this.setState("passive");
					}// end if
				
				
				}// end if
				else {
					if (model.getStock(stock).getAvail() < model.CAPACITY_OF_CONTAINER_BLOCK[stock]) {
						//model.getStock(stock).store(this, 1);
						model.getStock(stock).store(nextBurst.length);
						
						model.getActionWLQueue(stock).insert(this);
						this.setState("active");
						hold(new TimeSpan(model.getDistBurstProcessTime(), TimeUnit.MINUTES));
						model.getActionWLQueue(stock).remove(this);
						this.setState("passive");
					}// end if
				
				}// end else
				
				//hinzugefügt
				if(this.isInterrupted())
				{
					
					nextBurst.completed=false;
					nextBurst.setState("1");
					nextBurst.stop=(((model.getInActionQueue(stock).first().presentTime().getTimeRounded())/100)+3);
					nextBurst.actual=((nextBurst.stop-nextBurst.start));
						//model.totalprocessed.setText(nextBurst.actual+"     Preempted for Segmentation   ");
					
					model.getInActionQueue(stock).remove(nextBurst);
					nextBurst.length=nextBurst.length -nextBurst.actual;
					
				
					nextBurst.activate(new TimeSpan(0)); 
					this.clearInterruptCode();
				
				}	
				else
				{
				
				model.getInActionQueue(stock).remove(nextBurst);
				
				//ende
				
				nextBurst.activate(new TimeSpan(0)); 
				}
				}// end else
			
			}
		
			else
			{if(stock==1)
			{
				if (model.getBurstQueue2().isEmpty()) {   
					this.setState("passive");
					model.getIdleWLQueue(stock).insert(this);
					sendTraceNote("idle WL inserts itself into idle WLQueue," +
						" idle WLQueue length: " + model.getIdleWLQueue(stock).length());
					passivate();
				 }// end if
				else { 
					
					
						Burst nextBurst = (Burst) model.getBurstQueue2().first();
					model.getBurstQueue2().remove(nextBurst);
					nextBurst.completed=true;
					//hinzugefügt
					model.getInActionQueue(stock).insert(nextBurst);
					nextBurst.start=(model.getInActionQueue(stock).first().presentTime().getTimeRounded())/100;
					nextBurst.setState("processing");
//					model.bgElement8.setText("LastProcessed:  "+nextBurst.getName());
					//ende
					
					if (nextBurst.isIncomingBurst) {
						if (model.getStock(stock).getAvail() > 0) {
							//model.getStock(stock).retrieve(this,1);
							model.getStock(stock).retrieve(nextBurst.length);
							model.getActionWLQueue(stock).insert(this);
							this.setState("active");
							hold(new TimeSpan(model.getDistBurstProcessTime(), TimeUnit.MINUTES));
							model.getActionWLQueue(stock).remove(this);
							this.setState("passive");
						}// end if
						//else zweig ist noch sinnlos
						//else
						//	model.getCount().update(1);
						//model.getPacketsProcessed().update(nextBurst.length);
					
					}// end if
					else {
						if (model.getStock(stock).getAvail() < model.CAPACITY_OF_CONTAINER_BLOCK[stock]) {
							//model.getStock(stock).store(this, 1);
							model.getStock(stock).store(nextBurst.length);
							
							model.getActionWLQueue(stock).insert(this);
							this.setState("active");
							hold(new TimeSpan(model.getDistBurstProcessTime(), TimeUnit.MINUTES));
							model.getActionWLQueue(stock).remove(this);
							this.setState("passive");
						}// end if
						//else-zweig ist noch sinnlos.
					//	else
						//	model.getCount().update(1);
					//	model.getPacketsProcessed().update(nextBurst.length);
					}// end else
					
					//hinzugefügt
					if(this.isInterrupted())
					{
						
						nextBurst.completed=false;
						nextBurst.stop=(((model.getInActionQueue(stock).first().presentTime().getTimeRounded())/100)+3);
						nextBurst.actual=((nextBurst.stop-nextBurst.start));
						
						nextBurst.setState("2");
						//	model.totalprocessed.setText(nextBurst.actual+"     Preempted for Segmentation   ");
						nextBurst.length=nextBurst.length -nextBurst.actual;
						model.getInActionQueue(stock).remove(nextBurst);
					
						nextBurst.activate(new TimeSpan(0)); 
						this.clearInterruptCode();
					
					}	
					else
					{
					
					model.getInActionQueue(stock).remove(nextBurst);
					
					//ende
					
					nextBurst.activate(new TimeSpan(0)); 
					}
					}
			}
				
				else
				{
					if(stock==2)
					{
						if (model.getBurstQueue3().isEmpty()) {   
							this.setState("passive");
							model.getIdleWLQueue(stock).insert(this);
							sendTraceNote("idle WL inserts itself into idle WLQueue," +
								" idle WLQueue length: " + model.getIdleWLQueue(stock).length());
							passivate();
						 }// end if
						else { 
							
							
								Burst nextBurst = (Burst) model.getBurstQueue3().first();
							model.getBurstQueue3().remove(nextBurst);
							nextBurst.completed=true;
							//hinzugefügt
							model.getInActionQueue(stock).insert(nextBurst);
							nextBurst.start=(model.getInActionQueue(stock).first().presentTime().getTimeRounded())/100;
							nextBurst.setState("processing");
//							model.bgElement8.setText("LastProcessed:  "+nextBurst.getName());
							//ende
							
							if (nextBurst.isIncomingBurst) {
								if (model.getStock(stock).getAvail() > 0) {
									//model.getStock(stock).retrieve(this,1);
									model.getStock(stock).retrieve(nextBurst.length);
									model.getActionWLQueue(stock).insert(this);
									this.setState("active");
									hold(new TimeSpan(model.getDistBurstProcessTime(), TimeUnit.MINUTES));
									model.getActionWLQueue(stock).remove(this);
									this.setState("passive");
								}// end if
								//else zweig ist noch sinnlos
								//else
								//	model.getCount().update(1);
							//	model.getPacketsProcessed().update(nextBurst.length);
							
							}// end if
							else {
								if (model.getStock(stock).getAvail() < model.CAPACITY_OF_CONTAINER_BLOCK[stock]) {
									//model.getStock(stock).store(this, 1);
									model.getStock(stock).store(nextBurst.length);
									
									model.getActionWLQueue(stock).insert(this);
									this.setState("active");
									hold(new TimeSpan(model.getDistBurstProcessTime(), TimeUnit.MINUTES));
									model.getActionWLQueue(stock).remove(this);
									this.setState("passive");
								}// end if
								//else-zweig ist noch sinnlos.
								//else
								//	model.getCount().update(1);
							//	model.getPacketsProcessed().update(nextBurst.length);
							}// end else
							
							//hinzugefügt
						
							if(this.isInterrupted())
							{
								
								nextBurst.completed=false;
								nextBurst.stop=(((model.getInActionQueue(stock).first().presentTime().getTimeRounded())/100)+3);
								nextBurst.actual=((nextBurst.stop-nextBurst.start));
									//model.totalprocessed.setText(nextBurst.actual+"     Preempted for Segmentation   ");
								
								nextBurst.setState("3");
								nextBurst.length=nextBurst.length -nextBurst.actual;
								
								model.getInActionQueue(stock).remove(nextBurst);
								nextBurst.activate(new TimeSpan(0)); 
								this.clearInterruptCode();
							
							}	
							else
							{
							
							model.getInActionQueue(stock).remove(nextBurst);
							
							//ende
							
							nextBurst.activate(new TimeSpan(0)); 
							}
					
						}}
				
				
				}// end else
			}
		}//end while
	}//end lifeCycle() 
	 
}// end class