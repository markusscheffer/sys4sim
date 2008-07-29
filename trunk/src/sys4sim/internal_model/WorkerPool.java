package sys4sim.internal_model;

import java.util.ArrayList;

public class WorkerPool extends ResourcePool {

	private ArrayList<Worker> elements = new ArrayList<Worker>();

	public ArrayList<Worker> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Worker> elements) {
		this.elements = elements;
	}

}
