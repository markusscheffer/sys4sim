package sys4sim.xmi_import;

import java.util.ArrayList;
import java.util.Hashtable;

public class UmlClass extends PackagedElement {

	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		// nothing to do here
	}
	
	public void importGeneralization (Generalization gen) {
		PackagedElement element = gen.getExtended();
		copyPorts(element);
		copyAttributes(element);
		copyConnectors(element);
		for (Generalization upperGen : element.getGeneralizations()) {
			importGeneralization(upperGen);
		}
	}
	
	public static UmlClass getSystem (ArrayList<UmlClass> classes) {
		ArrayList<UmlClass> matches = new ArrayList<UmlClass>();
		for (UmlClass class1 : classes) {
			if (class1.isSubclassOf("SimulationSystem")) {
				matches.add(class1);
			}
		}
		// TODO: brutal hack: if there is more than one sibling of type simulation system, result is random
		return  matches.get(0);
	}
	
	public boolean isSubclassOf(String subclass) {
		for (Generalization gen : getGeneralizations()) {
			UmlClass class1 = (UmlClass) gen.getExtended();
			if (class1.getName().equals(subclass)) {
				return true;
			} else {
				if (class1.isSubclassOf(subclass)) {
					return true;
				}
			}
		}
		return false;
	}
}
