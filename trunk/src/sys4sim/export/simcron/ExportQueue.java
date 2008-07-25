package sys4sim.export.simcron;

import sys4sim.internal_model.Queue;

public class ExportQueue {
	
	private Queue queue;
	private int count;
	
	public ExportQueue(Queue queue, int count){
		this.queue=queue;
		this.count=count;
	}
	
	public String getMcString(){
		return "queue create queue"+count;
	}
	
	public String getMaString(){
		return "appear queue"+count+" 0 0";
	}

}
