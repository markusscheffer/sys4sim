package sys4sim.export.simcron;

import sys4sim.internal_model.Source;

public class ExportJob {

	private Source source;
	private int count;
	public ExportJob(Source source, int count) {
		this.source=source;
		this.count=count;
		
	}
	
	public String getMcString(){
		return "job create job"+count;
	}
	
	public String getMaString(){
		return "appear job"+count+" 0 0";
	}
	
	public String getMpLeaveString(){
		return "job"+count+" date leave 0";
	}
	
	public String getMpSampleString(){
		return "job"+count+" sample tech1 1";
	}

}
