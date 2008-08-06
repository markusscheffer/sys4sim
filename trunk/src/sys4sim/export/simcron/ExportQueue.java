package sys4sim.export.simcron;

import sys4sim.internal_model.ModelBlock;
import sys4sim.internal_model.Queue;

public class ExportQueue extends ExportObject {
	
	private Queue queue;
	private int count;
	private String name;
	private Type type;
	
	public ExportQueue(Queue queue, String name,int count, Type type){
		this.queue=queue;
		this.count=count;
		this.name=name;
		this.type=type;
	}
	
	public String getMcString(){
		return "queue create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}

	@Override
	public Type getType() {
		
		return type;
	}

	@Override
	public ModelBlock getInternalObjekt() {
		
		return queue;
	}

}
