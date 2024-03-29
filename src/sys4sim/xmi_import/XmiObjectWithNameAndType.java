package sys4sim.xmi_import;

import java.util.Hashtable;

public class XmiObjectWithNameAndType extends XmiObjectWithName {

	private String typeString;
	private UmlClass type;

	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		if (typeString != null) {
			type = (UmlClass) hash.get(typeString);
		}
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
