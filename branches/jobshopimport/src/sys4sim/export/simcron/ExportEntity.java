package sys4sim.export.simcron;

import sys4sim.internal_model.Entity;
import sys4sim.internal_model.Source;

public class ExportEntity {

	private Entity entity;
	private int count;
	private String name;
	
	public ExportEntity(Entity entity,String name, int count) {
		this.entity=entity;
		this.count=count;
		this.name=name;
	}
	
	public String getMcString(){
		return "job create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}
	
	public String getMpLeaveString(){
		return name+" date leave 0";
	}
	
	public String getMpString(){
		return name+" sample tech1 1";
	}

}
