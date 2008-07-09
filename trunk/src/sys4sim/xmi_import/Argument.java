package sys4sim.xmi_import;

import java.util.Hashtable;

public class Argument extends XmiObjectWithName {
	private String visibility;
	private String incomingString;
	private Edge incoming;
	
	public void unstringRelations(Hashtable<String, XmiObject> hash) {
		incoming = (Edge) hash.get(incomingString);
	}
	
	public Edge getIncoming() {
		return incoming;
	}
	public void setIncoming(Edge incoming) {
		this.incoming = incoming;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getIncomingString() {
		return incomingString;
	}
	public void setIncomingString(String incoming) {
		this.incomingString = incoming;
	}
}
