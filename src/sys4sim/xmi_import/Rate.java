package sys4sim.xmi_import;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sys4sim.internal_model.ConstantRate;
import sys4sim.internal_model.ExponentialDistribution;
import sys4sim.internal_model.NormalDistribution;
import sys4sim.internal_model.PoissonDistribution;
import sys4sim.internal_model.TriangularDistribution;

public class Rate extends XmiObject implements java.lang.Cloneable {
	String baseActivityEdgeString;
	Edge baseActivityEdge;
	sys4sim.internal_model.Rate rate;
		
	public Rate clone() {
		Rate toReturn = null;
		try {
			toReturn = (Rate) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
		
	public static ArrayList<Rate> expandRates (ArrayList<Rate> oldRates, ArrayList<Edge> edges) {
		ArrayList<Rate> rates = new ArrayList<Rate>();
		for (Rate rate : oldRates) {
			for (Edge edge : edges) {
				if (edge.getXmiID().contains(rate.getBaseActivityEdge().getXmiID())) {
					Rate newRate = rate.clone();
					newRate.setXmiID(rate.getXmiID() + "_" + edge.getXmiID().substring(12));
					newRate.setBaseActivityEdge(edge);
					//System.out.println("Cloning Rate: " + rate.getXmiID());
					rates.add(newRate);
					
				} else {
					//System.out.println("Edge ID    : " + edge.getXmiID());
					//System.out.println("Rate - Edge: " + rate.getBaseActivityEdge().getXmiID());
				}
			}
		}
			
		return rates;
	}
		
	public void unstringRelations(Hashtable<String, XmiObject> hash) {
		baseActivityEdge = (Edge) hash.get(baseActivityEdgeString);
	}

	public sys4sim.internal_model.Rate getRate() {
		return rate;
	}

	public void setRate(sys4sim.internal_model.Rate rate) {
		this.rate = rate;
	}
	
	public void setRate(String string) {
		
		if (string == null) {
			return;
		}
		
		
		
		//Pattern pattern = Pattern.compile("(&lt;&lt;|<<)((\\w)*)(&gt;&gt;|>>)\\{([\\w\\., ]*)\\}");
		Pattern pattern = Pattern.compile("<<(\\w*)>>\\{([\\w=\\.\\, ]*)\\}");
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) {
			if (matcher.group(1).equals("NormalDistribution")) {
				String argumentString = matcher.group(2);
				NormalDistribution nd = new NormalDistribution();
				
				pattern = Pattern.compile("mean=([\\d\\.]*)([\\w/]*)");
				matcher = pattern.matcher(argumentString);
				matcher.find();
				nd.setMeanValue(Double.parseDouble(matcher.group(1)));
				nd.setMeanUnit(matcher.group(2));
				
				pattern = Pattern.compile("standardDeviation=([\\d\\.]*)([\\w/]*)");
				matcher = pattern.matcher(argumentString);
				matcher.find();
				nd.setStandardDeviationValue(Double.parseDouble(matcher.group(1)));
				nd.setStandardDeviationUnit(matcher.group(2));
				
				rate = nd;
				
				return;


			} else if (matcher.group(1).equals("PoissonDistribution")) {
				pattern = Pattern.compile("mean=([\\d\\.]*)([\\w/]*)");
				matcher = pattern.matcher(matcher.group(2));
				if (matcher.find()) {
					rate = new PoissonDistribution();
					((PoissonDistribution)rate).setExpectedValue(Double.parseDouble(matcher.group(1)));
					((PoissonDistribution)rate).setExpectedUnit(matcher.group(2));
					return;
				}
			} else if (matcher.group(1).equals("TriangularDistribution")) {
				String argumentString = matcher.group(2);
				TriangularDistribution td = new TriangularDistribution();
				
				pattern = Pattern.compile("min=([\\d\\.]*)([\\w/]*)");
				matcher = pattern.matcher(argumentString);
				matcher.find();
				td.setMinValue(Double.parseDouble(matcher.group(1)));
				td.setMinUnit(matcher.group(2));

				pattern = Pattern.compile("max=([\\d\\.]*)([\\w/]*)");
				matcher = pattern.matcher(argumentString);
				matcher.find();
				td.setMaxValue(Double.parseDouble(matcher.group(1)));
				td.setMaxUnit(matcher.group(2));

				pattern = Pattern.compile("mode=([\\d\\.]*)([\\w/]*)");
				matcher = pattern.matcher(argumentString);
				matcher.find();
				td.setModeValue(Double.parseDouble(matcher.group(1)));
				td.setModeUnit(matcher.group(2));

				rate = td;
				return;
			} else if (matcher.group(1).equals("ExponentialDistribution")) {
				String argumentString = matcher.group(2);
				ExponentialDistribution ed = new ExponentialDistribution();
				
				pattern = Pattern.compile("mean=([\\d\\.]*)([\\w/]*)");
				matcher = pattern.matcher(argumentString);
				matcher.find();
				ed.setMeanValue(Double.parseDouble(matcher.group(1)));
				ed.setMeanUnit(matcher.group(2));
				
				rate = ed;
				return;
			}
		}
			else {	
				pattern = Pattern.compile("([\\d\\.]*)([\\w/]*)");
				matcher = pattern.matcher(string);
				if (matcher.find()) {
					rate = new ConstantRate();
					((ConstantRate)rate).setValue(Double.parseDouble(matcher.group(1)));
					((ConstantRate)rate).setUnit(matcher.group(2));
					return;
				}
			}
		System.out.println("The given distribution cannot be found: " + string);
		
	}

	public String getBaseActivityEdgeString() {
		return baseActivityEdgeString;
	}
	public void setBaseActivityEdgeString(String baseActivityEdgeString) {
		this.baseActivityEdgeString = baseActivityEdgeString;
	}
	public Edge getBaseActivityEdge() {
		return baseActivityEdge;
	}
	public void setBaseActivityEdge(Edge baseActivityEdge) {
		this.baseActivityEdge = baseActivityEdge;
	}
}
