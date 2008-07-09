package sys4sim.xmi_import;

import java.util.Hashtable;

public class XmiObjectWithNameAndType extends XmiObjectWithName {

	private String typeString;
	private UmlClass type;

	public void destringRelations (Hashtable<String, XmiObject> hash) {
		type = (UmlClass) hash.get(typeString);
	}
	
	public UmlClass getType() {
		return type;
	}

	public void setType(UmlClass type) {
		this.type = type;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String type) {
		this.typeString = type;
	}
}
