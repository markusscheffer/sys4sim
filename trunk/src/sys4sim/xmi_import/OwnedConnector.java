package sys4sim.xmi_import;

import java.util.ArrayList;

public class OwnedConnector extends XmiObjectWithName{
	private ArrayList<End> ends;

	public ArrayList<End> getEnds() {
		return ends;
	}

	public void setEnds(ArrayList<End> ends) {
		this.ends = ends;
	}
}
