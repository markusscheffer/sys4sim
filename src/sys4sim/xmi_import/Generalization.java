package sys4sim.xmi_import;

import java.util.Hashtable;

public class Generalization extends XmiObject{
	private String extendedString;
	private PackagedElement extended;
	
	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		if (extendedString != null) {
			extended = (PackagedElement) hash.get(extendedString);
		}
	}
	
	public PackagedElement getExtended() {
		return extended;
	}

	public void setExtended(PackagedElement extended) {
		this.extended = extended;
	}

	public String getExtendedString() {
		return extendedString;
	}

	public void setExtendedString(String extended) {
		this.extendedString = extended;
	}
	

}
