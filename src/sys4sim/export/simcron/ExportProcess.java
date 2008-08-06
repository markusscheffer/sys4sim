package sys4sim.export.simcron;

import sys4sim.internal_model.Machine;
import sys4sim.internal_model.ModelBlock;
import sys4sim.internal_model.Process;

public class ExportProcess extends ExportObject {
	
	private Process process;
	private int count;
	private String name;
	private Type type;
	private ExportRate rate;
	
	public ExportProcess(Process process,String name, int count, Type type){
		this.process = process;
		this.count=count;
		this.name=name;
		this.type =type;
	}
	
	public String getMcString(){
		return "machine create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}
	
	public String getMpString(){
		//TODO ändern
		return Integer.toString(60);
	}
	
	public String getMpDistrString(){
		String result = rate.getMpString()+"\n";
		result = result + name+" distrib busy factor "+rate.getName();
		return result;
	}

	@Override
	public Type getType() {	
		return type;
	}
	
	public void addRate(ExportRate rate){
		this.rate=rate;
	}

	@Override
	public ModelBlock getInternalObjekt() {
		
		return process;
	}

}
