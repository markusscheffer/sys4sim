package sys4sim.internal_model;

import java.util.ArrayList;

public class MachinePool extends ResourcePool {
	ArrayList<Machine> elements = new ArrayList<Machine>();
	
	public ArrayList<Machine> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Machine> elements) {
		this.elements = elements;
	}

}
