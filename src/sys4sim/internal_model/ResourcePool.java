package sys4sim.internal_model;

import java.util.ArrayList;

public abstract class ResourcePool {
	private int capacity = 1000000; // "infinite" capacity
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}