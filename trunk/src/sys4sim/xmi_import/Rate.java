package sys4sim.xmi_import;

import java.util.Hashtable;

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
		//TODO: parse the string, generate a internal model rate object
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
