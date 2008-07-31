package sys4sim.export.simcron;

import sys4sim.internal_model.Machine;
import sys4sim.internal_model.Process;

public class ExportProcess {
	
	private Process process;
	private int count;
	private String name;
	
	public ExportProcess(Process process,String name, int count){
		this.process = process;
		this.count=count;
		this.name=name;
	}
	
	public String getMcString(){
		return "machine create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}

}
