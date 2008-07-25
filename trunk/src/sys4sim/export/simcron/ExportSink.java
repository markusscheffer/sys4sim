package sys4sim.export.simcron;

import sys4sim.internal_model.Queue;
import sys4sim.internal_model.Sink;

public class ExportSink {

	private Sink sink;
	private int count;
	private String name;
	
	public ExportSink(Sink sink, int count){
		this.sink=sink;
		this.count=count;
		this.name="queue"+count;
	}
	
	public String getMcString(){
		return "queue create queue"+count;
	}
	
	public String getMaString(){
		return "appear queue"+count+" 0 0";
	}
	
	public String getName(){
		return name;
	}
}
