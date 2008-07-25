package sys4sim.export.simcron;

import sys4sim.internal_model.Machine;

public class ExportMachine {
	
	private Machine machine;
	private int count;
	
	public ExportMachine(Machine machine,int count){
		this.machine = machine;
		this.count=count;
	}
	
	public String getMcString(){
		return "machine create machine"+count;
	}
	
	public String getMaString(){
		return "appear machine"+count+" 0 0";
	}

}
