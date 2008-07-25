package sys4sim.export.simcron;

import sys4sim.internal_model.Connector;

public class ExportTech {
	
	private Connector connector;
	private int count;
	
	public ExportTech(Connector connector, int count){
		this.connector=connector;
		this.count=count;
	}
	
	public String getMcString(){
		return "tech create tech"+count;
	}
	
	public String getMaString(){
		return "appear tech"+count+" 0 0";
	}
	
	public String getMpString(String name){
		String result="";
		if(connector.getSource().getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source")){
			result="tech"+count+"pass append"+"";
			
		}
		
		return "apperar tech"+count+" 0 0";
	}
}
