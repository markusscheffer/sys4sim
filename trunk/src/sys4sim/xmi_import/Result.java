package sys4sim.xmi_import;

import java.util.Hashtable;

public class Result extends XmiObjectWithName {
	private String visibility;
	private String outgoingString;
	private Edge outgoing;
	
	public void destringRelations (Hashtable<String, XmiObject> hash) {
		outgoing = (Edge) hash.get(outgoingString);
	}
	
	public Edge getOutgoing() {
		return outgoing;
	}
	public void setOutgoing(Edge outgoing) {
		this.outgoing = outgoing;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getOutgoingString() {
		return outgoingString;
	}
	public void setOutgoingString(String outgoing) {
		this.outgoingString = outgoing;
	}
}
