package sys4sim.xmi_import;

import java.util.Hashtable;


public class XmiObject {

	private String xmiID;

	public String getXmiID() {
		return xmiID;
	}

	public void setXmiID(String xmiID) {
		this.xmiID = xmiID;
	}
	
	public void unstringRelations(Hashtable<String, XmiObject> hash) {
		System.out.println("no specialized unstring code found: " + this.getClass().toString());
	}
}
