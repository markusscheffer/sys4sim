package sys4sim.internal_model;

import java.util.ArrayList;

public class TransporterPool extends ResourcePool {
	ArrayList<TransporterPool> subPools = new ArrayList<TransporterPool>();
	
	public ArrayList<TransporterPool> getSubPools() {
		return subPools;
	}

	ArrayList<Transporter> elements = new ArrayList<Transporter>();
	
	@SuppressWarnings("unchecked")
	public ArrayList<Transporter> getElements() {
		ArrayList<Transporter> temp = (ArrayList<Transporter>) elements.clone();
		for (TransporterPool pool : subPools) {
			for (Transporter transporter : pool.getElements()) {
				temp.add(transporter);
			}
		}
		return temp;
	}

	public void add (Transporter transporter) {
		elements.add(transporter);
	}	
	
}
