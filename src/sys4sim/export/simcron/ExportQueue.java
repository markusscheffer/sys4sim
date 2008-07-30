package sys4sim.export.simcron;

import sys4sim.internal_model.Queue;

public class ExportQueue {
	
	private Queue queue;
	private int count;
	private String name;
	
	public ExportQueue(Queue queue, String name,int count){
		this.queue=queue;
		this.count=count;
		this.name=name;
	}
	
	public String getMcString(){
		return "queue create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}

}
