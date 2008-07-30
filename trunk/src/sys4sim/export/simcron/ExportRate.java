package sys4sim.export.simcron;

import sys4sim.internal_model.NormalDistribution;
import sys4sim.internal_model.PoissonDistribution;
import sys4sim.internal_model.Rate;

public class ExportRate {

	
	private Rate rate;
	private int count;
	private String name;
	
	public ExportRate(Rate rate,String name, int count) {
		this.rate=rate;
		this.count= count;
		this.name = name;
	}
	
	public String getMcString(){
		return "distrib create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}
	
	public String getMpString(){
		//Default Einstellung
		String result = "distrib"+count+" par 1 10.0" ;
	
		if(rate.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.PoissonDistribution")){
			result ="distrib2 type exponential\n"+"distrib"+count+"par 1 "+((PoissonDistribution)rate).getExpectedValue();
		}
		else if(rate.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.NormalDistribution")){
			result ="distrib2 type normal\n"+"distrib"+count+"par 1 "+((NormalDistribution)rate).getMeanValue()+"\n"+
			"distrib"+count+"par 1 "+((NormalDistribution)rate).getStandardDeviationValue();
		}
		
		return result;
	}
	
	public String getName(){
		return name;
	}


}
