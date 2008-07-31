package sys4sim.export.simcron;

import sys4sim.internal_model.Queue;
import sys4sim.internal_model.Sink;

public class ExportSink {

	private Sink sink;
	private int count;
	private String name;
	
	public ExportSink(Sink sink,String name, int count){
		this.sink=sink;
		this.count=count;
		this.name=name;
	}
	
	public String getMcString(){
		return "queue create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}
	
	public String getName(){
		return name;
	}
}
