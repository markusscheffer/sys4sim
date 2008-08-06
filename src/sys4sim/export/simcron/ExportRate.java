package sys4sim.export.simcron;

import sys4sim.internal_model.NormalDistribution;
import sys4sim.internal_model.PoissonDistribution;
import sys4sim.internal_model.Rate;

public class ExportRate {

	
	private Rate rate;

	private String name;
	
	public ExportRate(Rate rate,String name) {
		this.rate=rate;

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
		String result = name+" par 1 10.0" ;
	
		if(rate.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.PoissonDistribution")){
			result =name+" type exponential\n"+name+" par 1 "+((PoissonDistribution)rate).getExpectedValue();
		}
		else if(rate.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.NormalDistribution")){
			result ="distrib2 type normal\n"+name+"par 1 "+((NormalDistribution)rate).getMeanValue()+"\n"+
			name+"par 1 "+((NormalDistribution)rate).getStandardDeviationValue();
		}
		
		return result;
	}
	
	public String getName(){
		return name;
	}



}
