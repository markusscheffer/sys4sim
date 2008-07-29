package sys4sim.internal_model;

import java.util.ArrayList;

public class WorkerPool extends ResourcePool {

	ArrayList<WorkerPool> subPools = new ArrayList<WorkerPool>();
	
	public ArrayList<WorkerPool> getSubPools() {
		return subPools;
	}

	ArrayList<Worker> elements = new ArrayList<Worker>();
	
	@SuppressWarnings("unchecked")
	public ArrayList<Worker> getElements() {
		ArrayList<Worker> temp = (ArrayList<Worker>) elements.clone();
		for (WorkerPool pool : subPools) {
			for (Worker worker : pool.getElements()) {
				temp.add(worker);
			}
		}
		return temp;
	}

	public void add (Worker worker) {
		elements.add(worker);
	}

}
