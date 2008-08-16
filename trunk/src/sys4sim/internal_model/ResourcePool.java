package sys4sim.internal_model;


public abstract class ResourcePool extends ModelElement{
	private int capacity = 1000000; // "infinite" capacity
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}