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
	
		if(rate.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.ExponentialDistribution")){
			
			if (((PoissonDistribution)rate).getExpectedUnit().equalsIgnoreCase("s")){
				//TODO noch ändern
				result =name+" type exponential\n"+name+" par 1 "+((PoissonDistribution)rate).getExpectedValue();
			} else {
				//Hier müssen noch anderen Einheiten ergänzt bzw dann umgerechnet werden
			}
		}
		else if(rate.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.NormalDistribution")){
			result =name+" type normal\n"+name+"par 1 "+((NormalDistribution)rate).getMeanValue()+"\n"+
			name+" par 2 "+((NormalDistribution)rate).getStandardDeviationValue();
		} else {
			//Default
			result =name+" type normal\n"+name+" par 1 10.0\n"+name+" par 2 10.0";
		}
		
		return result;
	}
	
	public String getName(){
		return name;
	}



}
