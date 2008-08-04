package sys4sim.xmi_import;

import java.util.ArrayList;
import java.util.Hashtable;

public class Association extends XmiObject {
	private ArrayList<UmlClass> ends = new ArrayList<UmlClass>();
	private ArrayList<String> endStrings = new ArrayList<String>();
	
	public ArrayList<String> getEndStrings() {
		return endStrings;
	}

	public void setEndStrings(ArrayList<String> endStrings) {
		this.endStrings = endStrings;
	}

	public ArrayList<UmlClass> getEnds() {
		return ends;
	}

	public void setEnds(ArrayList<UmlClass> ends) {
		this.ends = ends;
	}
	
	public void unstringRelations(Hashtable<String, XmiObject> hash) {
		for (String classString : getEndStrings()) {
			XmiObject owner = ((OwnedAttribute) hash.get(classString)).getOwner();
			ends.add((UmlClass) owner);
		}
	}
	
}
