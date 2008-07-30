package sys4sim.export.simcron;

import sys4sim.internal_model.Sink;
import sys4sim.internal_model.Source;

public class ExportSource {

	private Source source;
	private int count;
	private boolean containsRate=false;
	private String name, rateName;
	
	public ExportSource(Source source,String name, int count){
		this.source=source;
		this.count=count;
		this.name=name;
	/*	if (source.getRate()!=null) {
			containsRate=true;
		}*/
	}
	
	public String getMcString(){
		return "queue create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}
	
	public String getMpString(){
		String result="";
		if (containsRate) {
			result= "queue"+count+"sample 1 distrib "+rateName;
		};
			result= result+"queue"+count+"cval 1 busy min 0"+
			"queue"+count+"cval 1 busy max 0"+
			"queue"+count+"cval 1 ready min 0"+
			"queue"+count+"cval 1 ready max 0"+
			"queue"+count+"cval 1 load min 0"+
			"queue"+count+"cval 1 load max 0";
		
		return result;
	}
	
	public boolean getContainsRate(){
		return containsRate;
	}
	
	public void setRateName(String name){
		this.rateName = name;
	}
}
