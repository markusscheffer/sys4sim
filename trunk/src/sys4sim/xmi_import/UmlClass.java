package sys4sim.xmi_import;

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
}
