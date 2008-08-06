package sys4sim.export.simcron;

import sys4sim.internal_model.ModelBlock;
import sys4sim.internal_model.Queue;
import sys4sim.internal_model.Sink;

public class ExportSink extends ExportObject {

	private Sink sink;
	private int count;
	private String name;
	private Type type;
	
	public ExportSink(Sink sink,String name, int count,Type type){
		this.sink=sink;
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
	
	public String getName(){
		return name;
	}

	@Override
	public Type getType() {
		
		return type;
	}

	@Override
	public ModelBlock getInternalObjekt() {
		
		return sink;
	}
}
