package sys4sim.xmi_import;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sys4sim.internal_model.NormalDistribution;
import sys4sim.internal_model.PoissonDistribution;

public class Rate extends XmiObject {
	String baseActivityEdgeString;
	Edge baseActivityEdge;
	sys4sim.internal_model.Rate rate;
	
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
		matcher.find();
		if (matcher.group(1).equals("NormalDistribution")) {
			pattern = Pattern.compile("mean=([\\d\\.]*)([\\w/]*)");
			matcher = pattern.matcher(matcher.group(2));
			matcher.find();
			//TODO: Not really matching normal distribution here!
			rate = new NormalDistribution();
			((NormalDistribution)rate).setMeanValue(Double.parseDouble(matcher.group(1)));
			((NormalDistribution)rate).setMeanUnit(matcher.group(2));
			
			
		
		} else if (matcher.group(1).equals("PoissonDistribution")) {
			pattern = Pattern.compile("mean=([\\d\\.]*)([\\w/]*)");
			matcher = pattern.matcher(matcher.group(2));
			matcher.find();
			
			rate = new PoissonDistribution();
			((PoissonDistribution)rate).setExpectedValue(Double.parseDouble(matcher.group(1)));
			((PoissonDistribution)rate).setExpectedUnit(matcher.group(2));
			} else {
				System.out.println("The given distribution cannot be found.");
			}
		
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
