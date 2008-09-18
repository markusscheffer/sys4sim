package sys4sim.export.simcron;

import java.util.ArrayList;
import java.util.Hashtable;

import sys4sim.internal_model.ModelBlock;
import sys4sim.internal_model.Rate;
import sys4sim.internal_model.Sink;
import sys4sim.internal_model.Source;

public class ExportSource extends ExportObject{

	private Source source;
	private int count;
	private boolean containsRate=false;
	private String name, rateName;
	private Hashtable<ExportEntity, ExportRate> rateTable = new  Hashtable<ExportEntity,ExportRate>();
	private Type type;
	
	public ExportSource(Source source,String name, int count, Type type){
		this.source=source;
		this.count=count;
		this.name=name;
		this.type=type;
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
	
	public String getMpSampleString(String rateName){

		String result= name+" sample 1 distrib "+rateName;	
		return result;
	}
	
	public String getMpSourceString(){
		String result= name+" cval 1 busy min 0"+"\n"+
		name+" cval 1 ready min 0"+"\n"+
		name+" cval 1 ready max 0"+"\n"+
		name+" cval 1 load min 0"+"\n"+
		name+" cval 1 load max 0";
		
		return result;
	}
	
	public boolean getContainsRate(){
		return containsRate;
	}
	
	public void setRateName(String name){
		this.rateName = name;
	}
	
	public void addEntity(ExportEntity entity, ExportRate rate) {
		rateTable.put(entity,rate);
	}
	
	public Hashtable<ExportEntity, ExportRate> getEntityTable(){
		return rateTable;
	}

	@Override
	public Type getType() {
		
		return type;
	}

	@Override
	public ModelBlock getInternalObjekt() {
	
		return source;
	}
	
	public Source getSource(){
		return source;
	}
	
}
