package sys4sim.internal_model;

import java.util.ArrayList;

public class TransporterPool extends ResourcePool {
	private ArrayList<Transporter> elements = new ArrayList<Transporter>();

	public ArrayList<Transporter> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Transporter> elements) {
		this.elements = elements;
	}
	
	
}
