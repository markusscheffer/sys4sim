package sys4sim.internal_model;

import java.util.ArrayList;

public class MachinePool extends ResourcePool {
	ArrayList<MachinePool> subPools = new ArrayList<MachinePool>();
	
	public ArrayList<MachinePool> getSubPools() {
		return subPools;
	}

	ArrayList<Machine> elements = new ArrayList<Machine>();
	
	@SuppressWarnings("unchecked")
	public ArrayList<Machine> getElements() {
		ArrayList<Machine> temp = (ArrayList<Machine>) elements.clone();
		for (MachinePool pool : subPools) {
			for (Machine machine : pool.getElements()) {
				temp.add(machine);
			}
		}
		return temp;
	}

	public void add (Machine machine) {
		elements.add(machine);
	}
	
}
