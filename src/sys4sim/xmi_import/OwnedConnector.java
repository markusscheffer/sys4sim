package sys4sim.xmi_import;

import java.util.ArrayList;
import java.util.Hashtable;

public class OwnedConnector extends XmiObjectWithName{
	private ArrayList<End> ends = new ArrayList<End>();

	public ArrayList<End> getEnds() {
		return ends;
	}

	public void setEnds(ArrayList<End> ends) {
		this.ends = ends;
	}
	
	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		//nothing needed for this one.
	}
}
